/*
 * Copyright 2019 NAVER Corp.
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

package com.navercorp.pinpoint.plugin.jdbc.mariadb;

import com.navercorp.pinpoint.bootstrap.plugin.test.PluginTestVerifier;
import com.navercorp.pinpoint.bootstrap.plugin.test.PluginTestVerifierHolder;
import com.navercorp.pinpoint.plugin.AgentPath;
import com.navercorp.pinpoint.test.plugin.Dependency;
import com.navercorp.pinpoint.test.plugin.JvmVersion;
import com.navercorp.pinpoint.test.plugin.PinpointAgent;
import com.navercorp.pinpoint.test.plugin.PinpointPluginTestSuite;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Method;
import java.util.Properties;

import static com.navercorp.pinpoint.bootstrap.plugin.test.Expectations.args;
import static com.navercorp.pinpoint.bootstrap.plugin.test.Expectations.cachedArgs;
import static com.navercorp.pinpoint.bootstrap.plugin.test.Expectations.event;
import static com.navercorp.pinpoint.bootstrap.plugin.test.Expectations.sql;

/**
 * <p>Notable class changes :<br/>
 * <ul>
 *     <li><tt>org.mariadb.jdbc.MariaDbPreparedStatementServer</tt> -> <tt>org.mariadb.jdbc.ServerSidePreparedStatement</tt></li>
 *     <li><tt>org.mariadb.jdbc.MariaDbPreparedStatementClient</tt> -> <tt>org.mariadb.jdbc.ClientSidePreparedStatement</tt></li>
 * </ul>
 * </p>
 *
 * @author HyunGil Jeong
 */
@RunWith(PinpointPluginTestSuite.class)
@PinpointAgent(AgentPath.PATH)
@JvmVersion(7)
@Dependency({ "org.mariadb.jdbc:mariadb-java-client:[1.8.0,2.min)", "ch.vorburger.mariaDB4j:mariaDB4j:2.2.2" })
public class MariaDB_1_8_0_to_2_0_0_IT extends MariaDB_IT_Base {

    // see CallableParameterMetaData#queryMetaInfos
    private  static final String CALLABLE_QUERY_META_INFOS_QUERY = "select param_list, returns, db, type from mysql.proc where name=? and db=DATABASE()";

    @Test
    public void testStatement() throws Exception {
        super.executeStatement();

        PluginTestVerifier verifier = PluginTestVerifierHolder.getInstance();
        verifier.printCache();

        // Driver#connect(String, Properties)
        Class<?> driverClass = Class.forName("org.mariadb.jdbc.Driver");
        Method connect = driverClass.getDeclaredMethod("connect", String.class, Properties.class);
        verifier.verifyTrace(event("MARIADB", connect, null, URL, DATABASE_NAME, cachedArgs(JDBC_URL)));

        // MariaDbStatement#executeQuery(String)
        Class<?> mariaDbStatementClass = Class.forName("org.mariadb.jdbc.MariaDbStatement");
        Method executeQuery = mariaDbStatementClass.getDeclaredMethod("executeQuery", String.class);
        verifier.verifyTrace(event("MARIADB_EXECUTE_QUERY", executeQuery, null, URL, DATABASE_NAME, sql(STATEMENT_NORMALIZED_QUERY, "1")));
    }

    @Test
    public void testPreparedStatement() throws Exception {
        super.executePreparedStatement();

        PluginTestVerifier verifier = PluginTestVerifierHolder.getInstance();
        verifier.printCache();
        verifier.verifyTraceCount(3);

        // Driver#connect(String, Properties)
        Class<?> driverClass = Class.forName("org.mariadb.jdbc.Driver");
        Method connect = driverClass.getDeclaredMethod("connect", String.class, Properties.class);
        verifier.verifyTrace(event("MARIADB", connect, null, URL, DATABASE_NAME, cachedArgs(JDBC_URL)));

        // MariaDbConnection#prepareStatement(String)
        Class<?> mariaDbConnectionClass = Class.forName("org.mariadb.jdbc.MariaDbConnection");
        Method prepareStatement = mariaDbConnectionClass.getDeclaredMethod("prepareStatement", String.class);
        verifier.verifyTrace(event("MARIADB", prepareStatement, null, URL, DATABASE_NAME, sql(PREPARED_STATEMENT_QUERY, null)));

        // MariaDbPreparedStatementClient#executeQuery
        Class<?> clientSidePreparedStatementClass = Class.forName("org.mariadb.jdbc.ClientSidePreparedStatement");
        Method executeQuery = clientSidePreparedStatementClass.getDeclaredMethod("executeQuery");
        verifier.verifyTrace(event("MARIADB_EXECUTE_QUERY", executeQuery, null, URL, DATABASE_NAME, sql(PREPARED_STATEMENT_QUERY, null, "3")));
    }

    @Test
    public void testCallableStatement() throws Exception {
        super.executeCallableStatement();

        PluginTestVerifier verifier = PluginTestVerifierHolder.getInstance();
        verifier.printCache();
        verifier.verifyTraceCount(6);

        // Driver#connect(String, Properties)
        Class<?> driverClass = Class.forName("org.mariadb.jdbc.Driver");
        Method connect = driverClass.getDeclaredMethod("connect", String.class, Properties.class);
        verifier.verifyTrace(event("MARIADB", connect, null, URL, DATABASE_NAME, cachedArgs(JDBC_URL)));

        // MariaDbConnection#prepareCall(String)
        Class<?> mariaDbConnectionClass = Class.forName("org.mariadb.jdbc.MariaDbConnection");
        Method prepareCall = mariaDbConnectionClass.getDeclaredMethod("prepareCall", String.class);
        verifier.verifyTrace(event("MARIADB", prepareCall, null, URL, DATABASE_NAME, sql(CALLABLE_STATEMENT_QUERY, null)));

        // CallableProcedureStatement#registerOutParameter
        Class<?> abstractCallableProcedureStatementClass = Class.forName("org.mariadb.jdbc.CallableProcedureStatement");
        Method registerOutParameter = abstractCallableProcedureStatementClass.getMethod("registerOutParameter", int.class, int.class);
        verifier.verifyTrace(event("MARIADB", registerOutParameter, null, URL, DATABASE_NAME, args(2, CALLABLE_STATMENT_OUTPUT_PARAM_TYPE)));

        // MariaDbPreparedStatementServer#executeQuery
        Class<?> serverSidePreparedStatementClass = Class.forName("org.mariadb.jdbc.ServerSidePreparedStatement");
        Method executeQueryServer = serverSidePreparedStatementClass.getDeclaredMethod("executeQuery");
        verifier.verifyTrace(event("MARIADB_EXECUTE_QUERY", executeQueryServer, null, URL, DATABASE_NAME, sql(CALLABLE_STATEMENT_QUERY, null, CALLABLE_STATEMENT_INPUT_PARAM)));

        // MariaDbConnection#prepareStatement(String)
        Method prepareStatement = mariaDbConnectionClass.getDeclaredMethod("prepareStatement", String.class);
        verifier.verifyTrace(event("MARIADB", prepareStatement, null, URL, DATABASE_NAME, sql(CALLABLE_QUERY_META_INFOS_QUERY, null)));

        // MariaDbPreparedStatementClient#executeQuery
        Class<?> clientSidePreparedStatementClass = Class.forName("org.mariadb.jdbc.ClientSidePreparedStatement");
        Method executeQueryClient = clientSidePreparedStatementClass.getDeclaredMethod("executeQuery");
        verifier.verifyTrace(event("MARIADB_EXECUTE_QUERY", executeQueryClient, null, URL, DATABASE_NAME, sql(CALLABLE_QUERY_META_INFOS_QUERY, null, PROCEDURE_NAME)));
    }
}
