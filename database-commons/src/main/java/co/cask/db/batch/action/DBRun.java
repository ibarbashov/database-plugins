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

package co.cask.db.batch.action;

import co.cask.ConnectionConfig;
import co.cask.util.DBUtils;
import co.cask.util.DriverCleanup;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class used by database action plugins to run database commands
 */
public class DBRun {
  private final QueryConfig config;
  private final Class<? extends Driver> driverClass;
  private final String jdbcDriverName;

  public DBRun(QueryConfig config, String jdbcDriverName, Class<? extends Driver> driverClass) {
    this.config = config;
    this.driverClass = driverClass;
    this.jdbcDriverName = jdbcDriverName;
  }

  /**
   * Uses a configured JDBC driver to execute a SQL statement. The configurations of which JDBC driver
   * to use and which connection string to use come from the plugin configuration.
   */
  public void run() throws SQLException, InstantiationException, IllegalAccessException {
    DriverCleanup driverCleanup = null;
    try {
      driverCleanup = DBUtils.ensureJDBCDriverIsAvailable(driverClass,
                                                          DBUtils.createConnectionString(config, jdbcDriverName),
                                                          ConnectionConfig.JDBC_PLUGIN_TYPE,
                                                          jdbcDriverName);

      try (Connection connection = getConnection()) {
        try (Statement statement = connection.createStatement()) {
          statement.execute(config.query);
        }
      }
    } finally {
      if (driverCleanup != null) {
        driverCleanup.destroy();
      }
    }
  }

  private Connection getConnection() throws SQLException {
    return DriverManager.getConnection(
      DBUtils.createConnectionString(config, jdbcDriverName), config.getConnectionArguments());
  }
}
