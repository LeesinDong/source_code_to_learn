/*
 * Copyright 2019 NAVER Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.navercorp.pinpoint.web.filter.visitor;

import com.navercorp.pinpoint.common.server.bo.SpanBo;
import com.navercorp.pinpoint.common.server.bo.SpanChunkBo;
import com.navercorp.pinpoint.common.server.bo.SpanEventBo;

/**
 * @author Woonduk Kang(emeroad)
 */
public class SpanVisitorAdaptor implements SpanVisitor {
    @Override
    public boolean visit(SpanBo spanBo) {
        return REJECT;
    }

    @Override
    public boolean visit(SpanChunkBo spanChunkBo) {
        return REJECT;
    }

    @Override
    public boolean visit(SpanEventBo spanEventBo) {
        return REJECT;
    }
}
