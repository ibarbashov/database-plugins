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

/**
 * MSSQL constants.
 */
public final class SqlServerConstants {

  private SqlServerConstants() {
    throw new AssertionError("Should not instantiate static utility class.");
  }

  public static final String PLUGIN_NAME = "SqlServer";
  public static final String INSTANCE_NAME = "instanceName";
  public static final String QUERY_TIMEOUT = "queryTimeout";
  public static final String SQL_SERVER_CONNECTION_STRING_FORMAT = "jdbc:sqlserver://%s:%s;databaseName=%s";
  public static final String CONNECTION_TYPE = "connectionType";
  public static final String AZURE_AD = "Azure Active Directory";
  public static final String AZURE_DRIVER_NAME = "azureDriverName";
  public static final String AZURE_DRIVER_TYPE = "azureDriverType";
  public static final String AZURE_AUTH_TYPE = "azureAuthType";
  public static final String MSI_CLIENT_ID = "msiClientID";

  // auth types
  public static final String ACTIVE_DIRECTORY_MSI = "ActiveDirectoryMSI";
  public static final String ACTIVE_DIRECTORY_INTEGRATED = "ActiveDirectoryIntegrated";
  public static final String ACTIVE_DIRECTORY_PASSWORD = "ActiveDirectoryPassword";
  public static final String SQL_PASSWORD = "SqlPassword";
}
