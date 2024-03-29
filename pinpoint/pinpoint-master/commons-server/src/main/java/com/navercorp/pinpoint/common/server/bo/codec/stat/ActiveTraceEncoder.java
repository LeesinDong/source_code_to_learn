/*
 * Copyright 2016 Naver Corp.
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

package com.navercorp.pinpoint.common.server.bo.codec.stat;

import com.navercorp.pinpoint.common.server.bo.stat.ActiveTraceBo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author HyunGil Jeong
 */
@Component
public class ActiveTraceEncoder extends AgentStatEncoder<ActiveTraceBo> {

    @Autowired
    public ActiveTraceEncoder(@Qualifier("activeTraceCodecV2") AgentStatCodec<ActiveTraceBo> activeTraceCodec) {
        super(activeTraceCodec);
    }
}
