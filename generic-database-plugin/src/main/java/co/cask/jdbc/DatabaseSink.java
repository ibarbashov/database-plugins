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

package co.cask.jdbc;

import co.cask.cdap.api.annotation.Description;
import co.cask.cdap.api.annotation.Name;
import co.cask.cdap.api.annotation.Plugin;
import co.cask.cdap.etl.api.batch.BatchSink;
import co.cask.db.batch.sink.DBSink;

/**
 * Sink support for a generic database.
 */
@Plugin(type = BatchSink.PLUGIN_TYPE)
@Name(DatabaseConstants.PLUGIN_NAME)
@Description("Writes records to a database table. Each record will be written in a row in the table")
public class DatabaseSink extends DBSink {

  private final DatabaseSinkConfig databaseSinkConfig;

  public DatabaseSink(DatabaseSinkConfig databaseSinkConfig) {
    super(databaseSinkConfig);
    this.databaseSinkConfig = databaseSinkConfig;
  }

  @Override
  public String getJdbcDriverName() {
    return databaseSinkConfig.jdbcDriverName;
  }

  /**
   * Generic database sink configuration.
   */
  public static class DatabaseSinkConfig extends DBSinkConfig {
    @Name(DatabaseConstants.JDBC_DRIVER_NAME)
    @Description("Name of the JDBC driver to use. This is the value of the 'jdbcDriverName' key defined in the JSON " +
            "file for the JDBC plugin.")
    public String jdbcDriverName;
  }
}
