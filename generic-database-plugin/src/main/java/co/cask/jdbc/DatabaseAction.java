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
import co.cask.cdap.etl.api.action.Action;
import co.cask.db.batch.action.DBAction;
import co.cask.db.batch.action.QueryConfig;

import java.util.Objects;

/**
 * Action that runs database command.
 */
@Plugin(type = Action.PLUGIN_TYPE)
@Name(DatabaseConstants.PLUGIN_NAME)
@Description("Action that runs a MySQL command")
public class DatabaseAction extends DBAction {

  private final DatabaseActionConfig databaseActionConfig;

  public DatabaseAction(DatabaseActionConfig databaseActionConfig) {
    super(databaseActionConfig);
    this.databaseActionConfig = databaseActionConfig;
  }

  @Override
  public String getJdbcDriverName() {
    return databaseActionConfig.jdbcDriverName;
  }

  /**
   * Database action databaseActionConfig.
   */
  public static class DatabaseActionConfig extends QueryConfig {
    @Name(DatabaseConstants.JDBC_DRIVER_NAME)
    @Description("Name of the JDBC driver to use. This is the value of the 'jdbcDriverName' key defined in the " +
            "JSON file for the JDBC plugin.")
    public String jdbcDriverName;
  }
}
