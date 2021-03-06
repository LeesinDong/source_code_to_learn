/*
 *  Copyright 2018 NAVER Corp.
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.navercorp.pinpoint.plugin.druid.interceptor;

import com.navercorp.pinpoint.bootstrap.context.MethodDescriptor;
import com.navercorp.pinpoint.bootstrap.context.SpanEventRecorder;
import com.navercorp.pinpoint.bootstrap.context.TraceContext;
import com.navercorp.pinpoint.bootstrap.interceptor.SpanEventSimpleAroundInterceptorForPlugin;
import com.navercorp.pinpoint.plugin.druid.DruidConstants;

/**
 * The type Data source get connection interceptor.
 *
 * @author Victor.Zxy
 * @version 1.8.1
 * @since 2017/07/21
 */
public class DataSourceGetConnectionInterceptor extends SpanEventSimpleAroundInterceptorForPlugin {

    /**
     * Instantiates a new Data source get connection interceptor.
     *
     * @param traceContext the trace context
     * @param descriptor   the descriptor
     */
    public DataSourceGetConnectionInterceptor(TraceContext traceContext, MethodDescriptor descriptor) {
        super(traceContext, descriptor);
    }

    @Override
    public void doInBeforeTrace(SpanEventRecorder recorder, final Object target, Object[] args) {
    }

    @Override
    public void doInAfterTrace(SpanEventRecorder recorder, Object target, Object[] args, Object result, Throwable throwable) {
        recorder.recordServiceType(DruidConstants.SERVICE_TYPE);
        if (args == null) {
//          getConnection() without any arguments
            recorder.recordApi(getMethodDescriptor());
        } else if (args.length == 2) {
//          skip args[1] because it's a password.
            recorder.recordApi(getMethodDescriptor(), args[0], 0);
        }
        recorder.recordException(throwable);
    }

}