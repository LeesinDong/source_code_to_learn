/*
 * Copyright 2019 NAVER Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.navercorp.pinpoint.collector.mapper.grpc.stat;

import com.navercorp.pinpoint.common.server.bo.stat.ActiveTraceBo;
import com.navercorp.pinpoint.common.server.bo.stat.AgentStatBo;
import com.navercorp.pinpoint.common.server.bo.stat.AgentStatDataPoint;
import com.navercorp.pinpoint.common.server.bo.stat.CpuLoadBo;
import com.navercorp.pinpoint.common.server.bo.stat.DataSourceBo;
import com.navercorp.pinpoint.common.server.bo.stat.DataSourceListBo;
import com.navercorp.pinpoint.common.server.bo.stat.DeadlockThreadCountBo;
import com.navercorp.pinpoint.common.server.bo.stat.DirectBufferBo;
import com.navercorp.pinpoint.common.server.bo.stat.FileDescriptorBo;
import com.navercorp.pinpoint.common.server.bo.stat.JvmGcBo;
import com.navercorp.pinpoint.common.server.bo.stat.JvmGcDetailedBo;
import com.navercorp.pinpoint.common.server.bo.stat.ResponseTimeBo;
import com.navercorp.pinpoint.common.server.bo.stat.TransactionBo;
import com.navercorp.pinpoint.grpc.Header;
import com.navercorp.pinpoint.grpc.trace.PActiveTrace;
import com.navercorp.pinpoint.grpc.trace.PAgentStat;
import com.navercorp.pinpoint.grpc.trace.PAgentStatBatch;
import com.navercorp.pinpoint.grpc.trace.PCpuLoad;
import com.navercorp.pinpoint.grpc.trace.PDataSource;
import com.navercorp.pinpoint.grpc.trace.PDataSourceList;
import com.navercorp.pinpoint.grpc.trace.PDeadlock;
import com.navercorp.pinpoint.grpc.trace.PDirectBuffer;
import com.navercorp.pinpoint.grpc.trace.PFileDescriptor;
import com.navercorp.pinpoint.grpc.trace.PJvmGc;
import com.navercorp.pinpoint.grpc.trace.PJvmGcDetailed;
import com.navercorp.pinpoint.grpc.trace.PResponseTime;
import com.navercorp.pinpoint.grpc.trace.PTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HyunGil Jeong
 */
@Component
public class GrpcAgentStatBatchMapper {

    @Autowired
    private GrpcJvmGcBoMapper jvmGcBoMapper;

    @Autowired
    private GrpcJvmGcDetailedBoMapper jvmGcDetailedBoMapper;

    @Autowired
    private GrpcCpuLoadBoMapper cpuLoadBoMapper;

    @Autowired
    private GrpcTransactionBoMapper transactionBoMapper;

    @Autowired
    private GrpcActiveTraceBoMapper activeTraceBoMapper;

    @Autowired
    private GrpcDataSourceBoMapper dataSourceBoMapper;

    @Autowired
    private GrpcResponseTimeBoMapper responseTimeBoMapper;

    @Autowired
    private GrpcDeadlockThreadCountBoMapper deadlockThreadCountBoMapper;

    @Autowired
    private GrpcFileDescriptorBoMapper fileDescriptorBoMapper;

    @Autowired
    private GrpcDirectBufferBoMapper directBufferBoMapper;

    public AgentStatBo map(final PAgentStatBatch agentStatBatch, final Header header) {
        if (agentStatBatch == null) {
            return null;
        }
        final String agentId = header.getAgentId();
        final long startTimestamp = header.getAgentStartTime();

        final AgentStatBo agentStatBo = new AgentStatBo();
        agentStatBo.setAgentId(agentId);
        agentStatBo.setStartTimestamp(startTimestamp);
        final int agentStatsSize = agentStatBatch.getAgentStatCount();

        final List<JvmGcBo> jvmGcBos = new ArrayList<>(agentStatsSize);
        final List<JvmGcDetailedBo> jvmGcDetailedBos = new ArrayList<>(agentStatsSize);
        final List<CpuLoadBo> cpuLoadBos = new ArrayList<>(agentStatsSize);
        final List<TransactionBo> transactionBos = new ArrayList<>(agentStatsSize);
        final List<ActiveTraceBo> activeTraceBos = new ArrayList<>(agentStatsSize);
        final List<DataSourceListBo> dataSourceListBos = new ArrayList<DataSourceListBo>(agentStatsSize);
        final List<ResponseTimeBo> responseTimeBos = new ArrayList<>(agentStatsSize);
        final List<DeadlockThreadCountBo> deadlockThreadCountBos = new ArrayList<>(agentStatsSize);
        final List<FileDescriptorBo> fileDescriptorBos = new ArrayList<>(agentStatsSize);
        final List<DirectBufferBo> directBufferBos = new ArrayList<>(agentStatsSize);

        for (PAgentStat agentStat : agentStatBatch.getAgentStatList()) {
            final long timestamp = agentStat.getTimestamp();
            // jvmGc
            if (agentStat.hasGc()) {
                final PJvmGc jvmGc = agentStat.getGc();
                final JvmGcBo jvmGcBo = this.jvmGcBoMapper.map(jvmGc);
                setBaseData(jvmGcBo, agentId, startTimestamp, timestamp);
                jvmGcBos.add(jvmGcBo);

                // jvmGcDetailed
                if (jvmGc.hasJvmGcDetailed()) {
                    final PJvmGcDetailed jvmGcDetailed = jvmGc.getJvmGcDetailed();
                    final JvmGcDetailedBo jvmGcDetailedBo = this.jvmGcDetailedBoMapper.map(jvmGcDetailed);
                    setBaseData(jvmGcDetailedBo, agentId, startTimestamp, timestamp);
                    jvmGcDetailedBos.add(jvmGcDetailedBo);
                }
            }

            // cpuLoad
            if (agentStat.hasCpuLoad()) {
                final PCpuLoad cpuLoad = agentStat.getCpuLoad();
                final CpuLoadBo cpuLoadBo = this.cpuLoadBoMapper.map(cpuLoad);
                setBaseData(cpuLoadBo, agentId, startTimestamp, timestamp);
                cpuLoadBos.add(cpuLoadBo);
            }

            // transaction
            if (agentStat.hasTransaction()) {
                final PTransaction transaction = agentStat.getTransaction();
                final TransactionBo transactionBo = this.transactionBoMapper.map(transaction);
                setBaseData(transactionBo, agentId, startTimestamp, timestamp);
                transactionBo.setCollectInterval(agentStat.getCollectInterval());
                transactionBos.add(transactionBo);
            }

            // activeTrace
            if (agentStat.hasActiveTrace()) {
                final PActiveTrace activeTrace = agentStat.getActiveTrace();
                if (activeTrace.hasHistogram()) {
                    final ActiveTraceBo activeTraceBo = this.activeTraceBoMapper.map(activeTrace);
                    setBaseData(activeTraceBo, agentId, startTimestamp, timestamp);
                    activeTraceBos.add(activeTraceBo);
                }
            }

            // datasource
            if (agentStat.hasDataSourceList()) {
                final PDataSourceList dataSourceList = agentStat.getDataSourceList();
                final DataSourceListBo dataSourceListBo = new DataSourceListBo();
                setBaseData(dataSourceListBo, agentId, startTimestamp, timestamp);
                for (PDataSource dataSource : dataSourceList.getDataSourceList()) {
                    final DataSourceBo dataSourceBo = dataSourceBoMapper.map(dataSource);
                    setBaseData(dataSourceBo, agentId, startTimestamp, timestamp);
                    dataSourceListBo.add(dataSourceBo);
                }
                dataSourceListBos.add(dataSourceListBo);
            }

            // response time
            if (agentStat.hasResponseTime()) {
                final PResponseTime responseTime = agentStat.getResponseTime();
                final ResponseTimeBo responseTimeBo = this.responseTimeBoMapper.map(responseTime);
                setBaseData(responseTimeBo, agentId, startTimestamp, timestamp);
                responseTimeBos.add(responseTimeBo);
            }

            // deadlock
            if (agentStat.hasDeadlock()) {
                final PDeadlock deadlock = agentStat.getDeadlock();
                final DeadlockThreadCountBo deadlockThreadCountBo = this.deadlockThreadCountBoMapper.map(deadlock);
                setBaseData(deadlockThreadCountBo, agentId, startTimestamp, timestamp);
                deadlockThreadCountBos.add(deadlockThreadCountBo);
            }

            // fileDescriptor
            if (agentStat.hasFileDescriptor()) {
                final PFileDescriptor fileDescriptor = agentStat.getFileDescriptor();
                final FileDescriptorBo fileDescriptorBo = this.fileDescriptorBoMapper.map(fileDescriptor);
                setBaseData(fileDescriptorBo, agentId, startTimestamp, timestamp);
                fileDescriptorBos.add(fileDescriptorBo);
            }

            // directBuffer
            if (agentStat.hasDirectBuffer()) {
                final PDirectBuffer directBuffer = agentStat.getDirectBuffer();
                final DirectBufferBo directBufferBo = this.directBufferBoMapper.map(directBuffer);
                setBaseData(directBufferBo, agentId, startTimestamp, timestamp);
                directBufferBos.add(directBufferBo);
            }
        }

        agentStatBo.setJvmGcBos(jvmGcBos);
        agentStatBo.setJvmGcDetailedBos(jvmGcDetailedBos);
        agentStatBo.setCpuLoadBos(cpuLoadBos);
        agentStatBo.setTransactionBos(transactionBos);
        agentStatBo.setActiveTraceBos(activeTraceBos);
        agentStatBo.setDataSourceListBos(dataSourceListBos);
        agentStatBo.setResponseTimeBos(responseTimeBos);
        agentStatBo.setDeadlockThreadCountBos(deadlockThreadCountBos);
        agentStatBo.setFileDescriptorBos(fileDescriptorBos);
        agentStatBo.setDirectBufferBos(directBufferBos);
        return agentStatBo;
    }

    private void setBaseData(AgentStatDataPoint agentStatDataPoint, String agentId, long startTimestamp, long timestamp) {
        agentStatDataPoint.setAgentId(agentId);
        agentStatDataPoint.setStartTimestamp(startTimestamp);
        agentStatDataPoint.setTimestamp(timestamp);
    }
}