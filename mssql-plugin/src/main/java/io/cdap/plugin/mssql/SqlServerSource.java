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

package io.cdap.plugin.mssql;

import com.google.common.collect.ImmutableMap;
import io.cdap.cdap.api.annotation.Description;
import io.cdap.cdap.api.annotation.Name;
import io.cdap.cdap.api.annotation.Plugin;
import io.cdap.cdap.api.plugin.PluginProperties;
import io.cdap.cdap.etl.api.PipelineConfigurer;
import io.cdap.cdap.etl.api.batch.BatchSource;
import io.cdap.cdap.etl.api.validation.InvalidStageException;
import io.cdap.plugin.db.batch.config.DBSpecificSourceConfig;
import io.cdap.plugin.db.batch.source.AbstractDBSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import javax.annotation.Nullable;

/**
 * Batch source to read from MSSQL.
 */
@Plugin(type = BatchSource.PLUGIN_TYPE)
@Name(SqlServerConstants.PLUGIN_NAME)
@Description("Reads from a database table(s) using a configurable SQL query." +
  " Outputs one record for each row returned by the query.")
public class SqlServerSource extends AbstractDBSource {

  private static final Logger LOG = LoggerFactory.getLogger(SqlServerSource.class);
  private final SqlServerSourceConfig sqlServerSourceConfig;
  private Class<?> sqlServerDataSourceClass;

  public SqlServerSource(SqlServerSourceConfig sqlServerSourceConfig) {
    super(sqlServerSourceConfig);
    this.sqlServerSourceConfig = sqlServerSourceConfig;
  }

  @Override
  public void configurePipeline(PipelineConfigurer pipelineConfigurer) {
    sqlServerDataSourceClass = pipelineConfigurer.usePluginClass(
      sqlServerSourceConfig.azureDriverType,
      sqlServerSourceConfig.azureDriverName,
      sqlServerSourceConfig.azureDriverType, PluginProperties.builder().build());

    super.configurePipeline(pipelineConfigurer);
  }

  @Override
  protected Connection getConnection() throws SQLException {
    if (SqlServerConstants.AZURE_AD.equals(sqlServerSourceConfig.connectionType)) {

      try {
      switch (sqlServerSourceConfig.azureAuthType) {
        case SqlServerConstants.ACTIVE_DIRECTORY_INTEGRATED:
          return AzureADConnectorUtils
            .activeDirectoryIntegratedConnection(sqlServerDataSourceClass, sqlServerSourceConfig);
        case SqlServerConstants.ACTIVE_DIRECTORY_MSI:
          return AzureADConnectorUtils.activeDirectoryMSIConnection(sqlServerDataSourceClass, sqlServerSourceConfig);
        case SqlServerConstants.ACTIVE_DIRECTORY_PASSWORD:
          return AzureADConnectorUtils
            .activeDirectoryPasswordConnection(sqlServerDataSourceClass, sqlServerSourceConfig);
        case SqlServerConstants.SQL_PASSWORD:
          return AzureADConnectorUtils.sqlPasswordConnection(sqlServerDataSourceClass, sqlServerSourceConfig);
      }

      } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | InstantiationException e) {
        throw new InvalidStageException("Unable to instantiate JDBC driver: " + e.getMessage(), e);
      }
    }

    return super.getConnection();
  }

  @Override
  protected String createConnectionString() {
    return String.format(SqlServerConstants.SQL_SERVER_CONNECTION_STRING_FORMAT,
                         sqlServerSourceConfig.host, sqlServerSourceConfig.port, sqlServerSourceConfig.database);
  }

  /**
   * MSSQL source config.
   */
  public static class SqlServerSourceConfig extends DBSpecificSourceConfig {

    @Name(SqlServerConstants.INSTANCE_NAME)
    @Description("The SQL Server instance name to connect to. When it is not specified, a connection is made" +
      " to the default instance. For the case where both the instanceName " +
      "and port are specified, see the notes for port.")
    @Nullable
    public String instanceName;

    @Name(SqlServerConstants.QUERY_TIMEOUT)
    @Description("The number of seconds to wait before a timeout has occurred on a query. The default value is -1, " +
      "which means infinite timeout. Setting this to 0 also implies to wait indefinitely.")
    @Nullable
    public Integer queryTimeout = -1;

    @Name(SqlServerConstants.CONNECTION_TYPE)
    @Description("What type of connection to use.")
    @Nullable
    public String connectionType;

    @Name(SqlServerConstants.AZURE_DRIVER_NAME)
    @Description("What plugin to use to create data source for connection with Azure.")
    @Nullable
    public String azureDriverName;

    @Name(SqlServerConstants.AZURE_DRIVER_TYPE)
    @Description("Azure Driver type.")
    @Nullable
    public String azureDriverType;

    @Name(SqlServerConstants.AZURE_AUTH_TYPE)
    @Description("What type of Azure connection to use.")
    @Nullable
    public String azureAuthType;

    @Name(SqlServerConstants.MSI_CLIENT_ID)
    @Description("MSI Client ID to connect to Azure Active Directory.")
    @Nullable
    public String msiClientID;

    @Override
    public String getConnectionString() {
      return String.format(SqlServerConstants.SQL_SERVER_CONNECTION_STRING_FORMAT, host, port, database);
    }

    @Override
    public Map<String, String> getDBSpecificArguments() {
      ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();

      if (instanceName != null) {
        builder.put(SqlServerConstants.INSTANCE_NAME, String.valueOf(instanceName));
      }

      return builder.put(SqlServerConstants.QUERY_TIMEOUT, String.valueOf(queryTimeout)).build();
    }
  }
}
