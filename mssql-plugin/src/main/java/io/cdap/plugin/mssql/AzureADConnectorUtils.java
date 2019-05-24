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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;

/**
 * Performs connection by using Azure Active Directory Authentication
 */
public class AzureADConnectorUtils {

  public static Connection sqlPasswordConnection(Class<?> sqlServerDataSourceClass,
                                             SqlServerSource.SqlServerSourceConfig config)
    throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {

    Method setServerName = sqlServerDataSourceClass.getMethod("setServerName", String.class);
    Method setDatabaseName = sqlServerDataSourceClass.getMethod("setDatabaseName", String.class);
    Method setAuthentication = sqlServerDataSourceClass.getMethod("setAuthentication", String.class);
    Method setUser = sqlServerDataSourceClass.getMethod("setUser", String.class);
    Method setPassword = sqlServerDataSourceClass.getMethod("setPassword", String.class);
    Method getConnection = sqlServerDataSourceClass.getMethod("getConnection");

    Object instance = sqlServerDataSourceClass.newInstance();
    setServerName.invoke(instance, config.host);
    setDatabaseName.invoke(instance, config.database);
    setAuthentication.invoke(instance, config.azureAuthType);
    setUser.invoke(instance, config.user);
    setPassword.invoke(instance, config.password);

    return (Connection) getConnection.invoke(instance);
  }

  public static Connection activeDirectoryIntegratedConnection(Class<?> sqlServerDataSourceClass,
                                                               SqlServerSource.SqlServerSourceConfig config)
    throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {

    Method setServerName = sqlServerDataSourceClass.getMethod("setServerName", String.class);
    Method setDatabaseName = sqlServerDataSourceClass.getMethod("setDatabaseName", String.class);
    Method setAuthentication = sqlServerDataSourceClass.getMethod("setAuthentication", String.class);
    Method getConnection = sqlServerDataSourceClass.getMethod("getConnection");

    Object instance = sqlServerDataSourceClass.newInstance();
    setServerName.invoke(instance, config.host);
    setDatabaseName.invoke(instance, config.database);
    setAuthentication.invoke(instance, config.azureAuthType);

    return (Connection) getConnection.invoke(instance);
  }

  public static Connection activeDirectoryMSIConnection(Class<?> sqlServerDataSourceClass,
                                                    SqlServerSource.SqlServerSourceConfig config)
    throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {

    Method setServerName = sqlServerDataSourceClass.getMethod("setServerName", String.class);
    Method setDatabaseName = sqlServerDataSourceClass.getMethod("setDatabaseName", String.class);
    Method setAuthentication = sqlServerDataSourceClass.getMethod("setAuthentication", String.class);
    Method setMsiClientId = sqlServerDataSourceClass.getMethod("setMsiClientId", String.class);
    Method getConnection = sqlServerDataSourceClass.getMethod("getConnection");

    Object instance = sqlServerDataSourceClass.newInstance();
    setServerName.invoke(instance, config.host);
    setDatabaseName.invoke(instance, config.database);
    setAuthentication.invoke(instance, config.azureAuthType);
    setMsiClientId.invoke(instance, config.msiClientID);

    return (Connection) getConnection.invoke(instance);
  }

  public static Connection activeDirectoryPasswordConnection(Class<?> sqlServerDataSourceClass,
                                                    SqlServerSource.SqlServerSourceConfig config)
    throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {

    Method setServerName = sqlServerDataSourceClass.getMethod("setServerName", String.class);
    Method setDatabaseName = sqlServerDataSourceClass.getMethod("setDatabaseName", String.class);
    Method setAuthentication = sqlServerDataSourceClass.getMethod("setAuthentication", String.class);
    Method setPassword = sqlServerDataSourceClass.getMethod("setPassword", String.class);
    Method getConnection = sqlServerDataSourceClass.getMethod("getConnection");

    Object instance = sqlServerDataSourceClass.newInstance();
    setServerName.invoke(instance, config.host);
    setDatabaseName.invoke(instance, config.database);
    setAuthentication.invoke(instance, config.azureAuthType);
    setPassword.invoke(instance, config.msiClientID);

    return (Connection) getConnection.invoke(instance);
  }
}
