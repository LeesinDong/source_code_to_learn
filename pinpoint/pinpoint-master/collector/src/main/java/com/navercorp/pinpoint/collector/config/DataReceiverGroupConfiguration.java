/*
 * Copyright 2017 NAVER Corp.
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

package com.navercorp.pinpoint.collector.config;

/**
 * @author Taejin Koo
 */
public interface DataReceiverGroupConfiguration {

    boolean isTcpEnable();

    String getTcpBindIp();

    int getTcpBindPort();

    boolean isUdpEnable();

    String getUdpBindIp();

    int getUdpBindPort();

    boolean isGrpcEnable();

    String getGrpcBindIp();

    int getGrpcBindPort();

    int getUdpReceiveBufferSize();

    int getWorkerThreadSize();

    int getWorkerQueueSize();

    boolean isWorkerMonitorEnable();

}
