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
import co.cask.db.batch.source.DBSource;
import com.google.common.collect.ImmutableMap;

import java.util.Map;
import javax.annotation.Nullable;

import static co.cask.mysql.MysqlConstants.AUTO_RECONNECT;
import static co.cask.mysql.MysqlConstants.MAX_ROWS;
import static co.cask.mysql.MysqlConstants.PLUGIN_NAME;

/**
 * Batch source to read from MySQL.
 */
@Plugin(type = "batchsource")
@Name(PLUGIN_NAME)
@Description("Reads from a database table(s) using a configurable SQL query." +
  " Outputs one record for each row returned by the query.")
public class MysqlSource extends DBSource {

  private final MysqlSourceConfig mysqlConfig;

  public MysqlSource(MysqlSourceConfig sourceConfig) {
    super(sourceConfig);
    this.mysqlConfig = sourceConfig;
  }

  /**
   * MySQL source config.
   */
  public static class MysqlSourceConfig extends DBSourceConfig implements MysqlConfig {
    @Name(AUTO_RECONNECT)
    @Description("Should the driver try to re-establish stale and/or dead connections")
    public Boolean autoReconnect;

    @Name(MAX_ROWS)
    @Nullable
    @Description("The maximum number of rows to return (0, the default means return all rows)")
    public Integer maxRows;

    @Override
    public Map<String, String> getDBSpecificArguments() {
      ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();

      if (maxRows != null) {
        builder.put(MAX_ROWS, String.valueOf(maxRows));
      }

      builder.put(AUTO_RECONNECT, String.valueOf(autoReconnect));

      return builder.build();
    }
  }
}
