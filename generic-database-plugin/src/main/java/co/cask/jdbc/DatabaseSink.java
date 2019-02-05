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

import co.cask.ConnectionConfig;
import co.cask.cdap.api.annotation.Description;
import co.cask.cdap.api.annotation.Macro;
import co.cask.cdap.api.annotation.Name;
import co.cask.cdap.api.annotation.Plugin;
import co.cask.cdap.etl.api.batch.BatchSink;
import co.cask.db.batch.sink.AbstractDBSink;

import javax.annotation.Nullable;

/**
 * Sink support for a generic database.
 */
@Plugin(type = BatchSink.PLUGIN_TYPE)
@Name(DatabaseConstants.PLUGIN_NAME)
@Description("Writes records to a database table. Each record will be written in a row in the table")
public class DatabaseSink extends AbstractDBSink {

  private final DatabaseSinkConfig databaseSinkConfig;

  public DatabaseSink(DatabaseSinkConfig databaseSinkConfig) {
    super(databaseSinkConfig);
    this.databaseSinkConfig = databaseSinkConfig;
  }

  /**
   * Generic database sink configuration.
   */
  public static class DatabaseSinkConfig extends DBSinkConfig {
    @Name(ConnectionConfig.CONNECTION_STRING)
    @Description("JDBC connection string including database name.")
    @Macro
    public String connectionString;

    @Nullable
    @Name(TRANSACTION_ISOLATION_LEVEL)
    @Description("The transaction isolation level for queries run by this sink. " +
      "Defaults to TRANSACTION_SERIALIZABLE. See java.sql.Connection#setTransactionIsolation for more details. " +
      "The Phoenix jdbc driver will throw an exception if the Phoenix database does not have transactions enabled " +
      "and this setting is set to true. For drivers like that, this should be set to TRANSACTION_NONE.")
    @Macro
    public String transactionIsolationLevel;

    @Override
    public String getConnectionString() {
      return connectionString;
    }

    @Override
    public String getTransactionIsolationLevel() {
      return transactionIsolationLevel;
    }
  }
}
