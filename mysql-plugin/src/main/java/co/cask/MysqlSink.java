/*
 * Copyright © 2019 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package co.cask;

import co.cask.cdap.api.annotation.Description;
import co.cask.cdap.api.annotation.Name;
import co.cask.cdap.api.annotation.Plugin;
import co.cask.cdap.api.data.format.StructuredRecord;
import co.cask.cdap.api.dataset.lib.KeyValue;
import co.cask.cdap.etl.api.Emitter;
import co.cask.cdap.etl.api.PipelineConfigurer;
import co.cask.cdap.etl.api.batch.BatchRuntimeContext;
import co.cask.cdap.etl.api.batch.BatchSink;
import co.cask.cdap.etl.api.batch.BatchSinkContext;
import co.cask.db.batch.sink.DBSink;
import com.google.common.collect.ImmutableMap;
import org.apache.hadoop.io.NullWritable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static co.cask.MysqlConstants.AUTO_RECONNECT;
import static co.cask.MysqlConstants.PLUGIN_NAME;


/**
 * Sink support for a mysql database.
 */
@Plugin(type = BatchSink.PLUGIN_TYPE)
@Name(PLUGIN_NAME)
@Description("Writes records to a mysql table. Each record will be written in a row in the table")
public class MysqlSink extends DBSink {

  private static final Logger LOG = LoggerFactory.getLogger(MysqlSink.class);
  private final MysqlSinkConfig mysqlSinkConfig;

  public MysqlSink(MysqlSinkConfig dbSinkConfig) {
    super(dbSinkConfig);
    mysqlSinkConfig = dbSinkConfig;
  }

  @Override
  public void configurePipeline(PipelineConfigurer pipelineConfigurer) {
    super.configurePipeline(pipelineConfigurer);
  }

  @Override
  public void prepareRun(BatchSinkContext context) {
    super.prepareRun(context);
  }

  @Override
  public void initialize(BatchRuntimeContext context) throws Exception {
    super.initialize(context);
  }

  @Override
  public void transform(StructuredRecord input, Emitter<KeyValue<DBRecord, NullWritable>> emitter) throws Exception {
    super.transform(input, emitter);
  }

  @Override
  public void destroy() {
    super.destroy();
  }


  /**
   *  Mysql action configuration.
   */
  public static class MysqlSinkConfig extends DBSinkConfig implements MysqlConfig {
    @Name(AUTO_RECONNECT)
    @Description("Should the driver try to re-establish stale and/or dead connections")
    public Boolean autoReconnect;

    @Override
    public Map<String, String> getDBSpecificArguments() {

      return ImmutableMap.of(AUTO_RECONNECT, String.valueOf(autoReconnect));
    }
  }
}
