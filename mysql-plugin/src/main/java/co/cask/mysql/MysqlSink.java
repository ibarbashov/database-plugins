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

package co.cask.mysql;

import co.cask.cdap.api.annotation.Description;
import co.cask.cdap.api.annotation.Name;
import co.cask.cdap.api.annotation.Plugin;
import co.cask.cdap.etl.api.batch.BatchSink;
import co.cask.db.batch.sink.DBSink;
import com.google.common.collect.ImmutableMap;

import java.util.Map;


/**
 * Sink support for a MySQL database.
 */
@Plugin(type = BatchSink.PLUGIN_TYPE)
@Name(MysqlConstants.PLUGIN_NAME)
@Description("Writes records to a MySQL table. Each record will be written in a row in the table")
public class MysqlSink extends DBSink implements MysqlDriverNameProvider {

  private final MysqlSinkConfig mysqlSinkConfig;

  public MysqlSink(MysqlSinkConfig mysqlSinkConfig) {
    super(mysqlSinkConfig);
    this.mysqlSinkConfig = mysqlSinkConfig;
  }

  /**
   * MySQL action configuration.
   */
  public static class MysqlSinkConfig extends DBSinkConfig {
    @Name(MysqlConstants.AUTO_RECONNECT)
    @Description("Should the driver try to re-establish stale and/or dead connections")
    public Boolean autoReconnect;

    @Override
    public Map<String, String> getDBSpecificArguments() {

      return ImmutableMap.of(MysqlConstants.AUTO_RECONNECT, String.valueOf(autoReconnect));
    }
  }
}
