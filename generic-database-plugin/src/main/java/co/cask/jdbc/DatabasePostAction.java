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
import co.cask.cdap.etl.api.batch.PostAction;
import co.cask.db.batch.action.QueryAction;
import co.cask.db.batch.action.QueryActionConfig;

/**
 * Represents database post action.
 */
@Plugin(type = PostAction.PLUGIN_TYPE)
@Name("MysqlQuery")
@Description("Runs a MySQL query after a pipeline run.")
public class DatabasePostAction extends QueryAction {

  public DatabaseQueryActionConfig databaseQueryActionConfig;

  public DatabasePostAction(DatabaseQueryActionConfig databaseQueryActionConfig) {
    super(databaseQueryActionConfig);
    this.databaseQueryActionConfig = databaseQueryActionConfig;
  }

  @Override
  public String getJdbcDriverName() {
    return databaseQueryActionConfig.jdbcDriverName;
  }

  /**
   * Database post action databaseQueryActionConfig.
   */
  public static class DatabaseQueryActionConfig extends QueryActionConfig {
    @Name(DatabaseConstants.JDBC_DRIVER_NAME)
    @Description("Name of the JDBC driver to use. This is the value of the 'jdbcDriverName' key defined in the JSON " +
            "file for the JDBC plugin.")
    public String jdbcDriverName;
  }
}
