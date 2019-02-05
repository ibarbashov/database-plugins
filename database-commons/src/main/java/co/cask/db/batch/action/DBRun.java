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

import co.cask.DBManager;
import co.cask.DBUtils;

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

  public DBRun(QueryConfig config, Class<? extends Driver> driverClass) {
    this.config = config;
    this.driverClass = driverClass;
  }

  /**
   * Uses a configured JDBC driver to execute a SQL statement. The configurations of which JDBC driver
   * to use and which connection string to use come from the plugin configuration.
   */
  public void run() throws SQLException, InstantiationException, IllegalAccessException {
    DBManager dbManager = new DBManager(config);

    try {
      dbManager.ensureJDBCDriverIsAvailable(driverClass);

      try (Connection connection = getConnection()) {
        try (Statement statement = connection.createStatement()) {
          statement.execute(config.query);
        }
      }
    } finally {
      dbManager.destroy();
    }
  }

  private Connection getConnection() throws SQLException {
    return DriverManager.getConnection(
      DBUtils.createConnectionString(config), config.getConnectionArguments());
  }
}
