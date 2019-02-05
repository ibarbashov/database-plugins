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

/**
 * Represents Supported database providers.
 */
public enum DBProvider {
  MYSQL("mysql", "jdbc"),
  POSTGRES("postgres", "jdbc"),
  ORACLE("oracle", "jdbc");

  private final String jdbcPluginName;
  private final String jdbcPluginType;

  DBProvider(String jdbcPluginName, String jdbcPluginType) {
    this.jdbcPluginName = jdbcPluginName;
    this.jdbcPluginType = jdbcPluginType;
  }

  public String getJdbcPluginName() {
    return jdbcPluginName;
  }

  public String getJdbcPluginType() {
    return jdbcPluginType;
  }

  public static DBProvider fromString(String database) {
    return DBProvider.valueOf(database.toUpperCase());
  }
}
