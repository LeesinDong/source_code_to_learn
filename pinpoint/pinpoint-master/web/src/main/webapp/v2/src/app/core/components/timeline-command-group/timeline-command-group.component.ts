import { Component, OnInit, Input } from '@angular/core';
import { MessageQueueService, MESSAGE_TO } from 'app/shared/services';

@Component({
    selector: 'pp-timeline-command-group',
    templateUrl: './timeline-command-group.component.html',
    styleUrls: ['./timeline-command-group.component.css'],
})
export class TimelineCommandGroupComponent implements OnInit {
    @Input() pointingTime: string;
    constructor(
        private messageQueueService: MessageQueueService
    ) {}
    ngOnInit() {}
    onClickZoomIn(): void {
        this.messageQueueService.sendMessage({
            to: MESSAGE_TO.TIMELINE_ZOOM_IN,
            param: []
        });
    }
    onClickZoomOut(): void {
        this.messageQueueService.sendMessage({
            to: MESSAGE_TO.TIMELINE_ZOOM_OUT,
            param: []
        });
    }
    onClickPrev(): void {
        this.messageQueueService.sendMessage({
            to: MESSAGE_TO.TIMELINE_MOVE_PREV,
            param: []
        });
    }
    onClickNext(): void {
        this.messageQueueService.sendMessage({
            to: MESSAGE_TO.TIMELINE_MOVE_NEXT,
            param: []
        });
    }
    onClickNow(): void {
        this.messageQueueService.sendMessage({
            to: MESSAGE_TO.TIMELINE_MOVE_NOW,
            param: []
        });
    }
}
