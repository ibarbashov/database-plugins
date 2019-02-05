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
import co.cask.db.batch.source.DBSource;

import java.util.Objects;

/**
 * Batch source to read from generic database.
 */
@Plugin(type = "batchsource")
@Name(DatabaseConstants.PLUGIN_NAME)
@Description("Reads from a database table(s) using a configurable SQL query." +
  " Outputs one record for each row returned by the query.")
public class DatabaseSource extends DBSource {

  private final DatabaseSourceConfig databaseSourceConfig;

  public DatabaseSource(DatabaseSourceConfig databaseSourceConfig) {
    super(databaseSourceConfig);
    this.databaseSourceConfig = databaseSourceConfig;
  }

  @Override
  public String getJdbcDriverName() {
    return Objects.nonNull(databaseSourceConfig) ? databaseSourceConfig.jdbcDriverName : null;
  }

  /**
   * Generic database source configuration.
   */
  public static class DatabaseSourceConfig extends DBSourceConfig {
    @Name(DatabaseConstants.JDBC_DRIVER_NAME)
    @Description("Name of the JDBC driver to use. This is the value of the 'jdbcDriverName' key defined in the JSON " +
            "file for the JDBC plugin.")
    public String jdbcDriverName;
  }
}
