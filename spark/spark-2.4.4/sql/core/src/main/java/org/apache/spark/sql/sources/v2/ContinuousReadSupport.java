/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.spark.sql.sources.v2;

import java.util.Optional;

import org.apache.spark.annotation.InterfaceStability;
import org.apache.spark.sql.sources.v2.reader.streaming.ContinuousReader;
import org.apache.spark.sql.types.StructType;

/**
 * A mix-in interface for {@link DataSourceV2}. Data sources can implement this interface to
 * provide data reading ability for continuous stream processing.
 */
@InterfaceStability.Evolving
public interface ContinuousReadSupport extends DataSourceV2 {
  /**
   * Creates a {@link ContinuousReader} to scan the data from this data source.
   *
   * @param schema the user provided schema, or empty() if none was provided
   * @param checkpointLocation a path to Hadoop FS scratch space that can be used for failure
   *                           recovery. Readers for the same logical source in the same query
   *                           will be given the same checkpointLocation.
   * @param options the options for the returned data source reader, which is an immutable
   *                case-insensitive string-to-string map.
   */
  ContinuousReader createContinuousReader(
    Optional<StructType> schema,
    String checkpointLocation,
    DataSourceOptions options);
}
