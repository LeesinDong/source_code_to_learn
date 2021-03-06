import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subject, combineLatest } from 'rxjs';
import { takeUntil, filter, map, take } from 'rxjs/operators';

import { Actions } from 'app/shared/store';
import { StoreHelperService, NewUrlStateNotificationService, UrlRouteManagerService, AnalyticsService, TRACKED_EVENT_LIST } from 'app/shared/services';
import { ServerMapData } from 'app/core/components/server-map/class/server-map-data.class';

@Component({
    selector: 'pp-server-status-container',
    templateUrl: './server-status-container.component.html',
    styleUrls: ['./server-status-container.component.css'],
})
export class ServerStatusContainerComponent implements OnInit, OnDestroy {
    private unsubscribe = new Subject<void>();
    private enableRealTime: boolean;

    node: INodeInfo;
    isInfoPerServerShow = false;
    isLoading = false;
    serverMapData: ServerMapData;
    selectedTarget: ISelectedTarget;

    constructor(
        private storeHelperService: StoreHelperService,
        private newUrlStateNotificationService: NewUrlStateNotificationService,
        private urlRouteManagerService: UrlRouteManagerService,
        private analyticsService: AnalyticsService
    ) {}

    ngOnInit() {
        this.newUrlStateNotificationService.onUrlStateChange$.pipe(
            takeUntil(this.unsubscribe)
        ).subscribe((urlService: NewUrlStateNotificationService) => {
            this.enableRealTime = urlService.isRealTimeMode();
            this.isInfoPerServerShow = false;
            this.storeHelperService.dispatch(new Actions.ChangeServerMapDisableState(false));
            this.storeHelperService.dispatch(new Actions.ChangeInfoPerServerVisibleState(false));
        });
        this.connectStore();
    }

    ngOnDestroy() {
        this.unsubscribe.next();
        this.unsubscribe.complete();
    }

    private connectStore(): void {
        this.storeHelperService.getServerMapData(this.unsubscribe).subscribe((serverMapData: ServerMapData) => {
            this.serverMapData = serverMapData;
        });
        this.storeHelperService.getServerMapTargetSelected(this.unsubscribe).pipe(
            filter((target: ISelectedTarget) => !!target)
        ).subscribe((target: ISelectedTarget) => {
            this.selectedTarget = target;
            this.node = (target.isNode === true ? this.serverMapData.getNodeData(target.node[0]) as INodeInfo : null);
            if (target.clickParam && target.clickParam.isInstanceCount()) {
                this.onClickViewServer();
            }
        });
    }

    onClickViewServer(): void {
        this.analyticsService.trackEvent(TRACKED_EVENT_LIST.SHOW_SERVER_LIST);
        this.isInfoPerServerShow = !this.isInfoPerServerShow;
        this.storeHelperService.dispatch(new Actions.ChangeServerMapDisableState(this.isInfoPerServerShow));
        this.storeHelperService.dispatch(new Actions.ChangeInfoPerServerVisibleState(this.isInfoPerServerShow));
    }

    hasServerList(): boolean {
        if (this.selectedTarget) {
            if (this.selectedTarget.isNode && !this.selectedTarget.isMerged) {
                return this.selectedTarget.hasServerList;
            }
        }

        return false;
    }

    enableViewServer(): boolean {
        return !this.enableRealTime;
    }

    onClickOpenInspector(): void {
        this.analyticsService.trackEvent(TRACKED_EVENT_LIST.OPEN_INSPECTOR);
        combineLatest(
            this.newUrlStateNotificationService.onUrlStateChange$.pipe(
                map((urlService: NewUrlStateNotificationService) => urlService.isRealTimeMode())
            ),
            this.storeHelperService.getAgentSelection(this.unsubscribe)
        ).pipe(
            take(1),
        ).subscribe(([isRealTimeMode, selectedAgent]: [boolean, string]) => {
            this.urlRouteManagerService.openInspectorPage(isRealTimeMode, selectedAgent);
        });
    }

    getAngle(): string {
        return this.isInfoPerServerShow ? 'right' : 'left';
    }

    isWAS(): boolean {
        return this.selectedTarget.isWAS;
    }
}
