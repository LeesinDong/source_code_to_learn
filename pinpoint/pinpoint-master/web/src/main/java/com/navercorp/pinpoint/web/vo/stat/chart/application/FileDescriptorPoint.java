/*
 * Copyright 2018 NAVER Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.navercorp.pinpoint.web.vo.stat.chart.application;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.navercorp.pinpoint.common.server.bo.stat.join.JoinFileDescriptorBo;
import com.navercorp.pinpoint.web.view.FileDescriptorPointSerializer;
import com.navercorp.pinpoint.web.vo.chart.Point;

/**
 * @author Roy Kim
 */
@JsonSerialize(using = FileDescriptorPointSerializer.class)
public class FileDescriptorPoint implements Point {

    private final long xVal;
    private final long yValForMin;
    private final String  agentIdForMin;
    private final long yValForMax;
    private final String agentIdForMax;
    private final long yValForAvg;

    public FileDescriptorPoint(long xVal, long yValForMin, String agentIdForMin, long yValForMax, String agentIdForMax, long yValForAvg) {
        this.xVal = xVal;
        this.yValForMin = yValForMin;
        this.agentIdForMin = agentIdForMin;
        this.yValForMax = yValForMax;
        this.agentIdForMax = agentIdForMax;
        this.yValForAvg = yValForAvg;
    }

    public long getYValForMin() {
        return yValForMin;
    }

    public String getAgentIdForMin() {
        return agentIdForMin;
    }

    public long getYValForMax() {
        return yValForMax;
    }

    public String getAgentIdForMax() {
        return agentIdForMax;
    }

    public long getYValForAvg() {
        return yValForAvg;
    }

    @Override
    public long getXVal() {
        return this.xVal;
    }

    public static class UncollectedFileDescriptorPointCreator implements UncollectedPointCreator<FileDescriptorPoint> {
        @Override
        public FileDescriptorPoint createUnCollectedPoint(long xVal) {
            return new FileDescriptorPoint(xVal, JoinFileDescriptorBo.UNCOLLECTED_VALUE, JoinFileDescriptorBo.UNKNOWN_AGENT, JoinFileDescriptorBo.UNCOLLECTED_VALUE, JoinFileDescriptorBo.UNKNOWN_AGENT, JoinFileDescriptorBo.UNCOLLECTED_VALUE);
        }
    }
}
