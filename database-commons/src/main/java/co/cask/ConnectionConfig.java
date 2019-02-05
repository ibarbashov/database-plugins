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

import co.cask.cdap.api.annotation.Description;
import co.cask.cdap.api.annotation.Macro;
import co.cask.cdap.api.annotation.Name;
import co.cask.cdap.api.dataset.lib.KeyValue;
import co.cask.cdap.api.plugin.PluginConfig;
import co.cask.hydrator.common.KeyValueListParser;
import com.google.common.base.Strings;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.annotation.Nullable;

/**
 * Defines a base {@link PluginConfig} that Database source, sink, and action can all re-use.
 */
public abstract class ConnectionConfig extends PluginConfig implements DBProviderAware {
  public static final String CONNECTION_STRING = "connectionString";
  public static final String USER = "user";
  public static final String HOST = "host";
  public static final String PORT = "port";
  public static final String DATABASE = "database";
  public static final String PASSWORD = "password";
  public static final String CONNECTION_ARGUMENTS = "connectionArguments";
  public static final String JDBC_PLUGIN_NAME = "jdbcPluginName";
  public static final String COLUMN_NAME_CASE = "columnNameCase";

  @Name(HOST)
  @Description("Database host")
  public String host;

  @Name(PORT)
  @Description("Specific database port")
  public Integer port;

  @Name(DATABASE)
  @Description("Database name to connect")
  public String database;

  @Name(USER)
  @Description("User to use to connect to the specified database. Required for databases that " +
    "need authentication. Optional for databases that do not require authentication.")
  @Nullable
  @Macro
  public String user;

  @Name(PASSWORD)
  @Description("Password to use to connect to the specified database. Required for databases that " +
    "need authentication. Optional for databases that do not require authentication.")
  @Nullable
  @Macro
  public String password;

  @Name(CONNECTION_ARGUMENTS)
  @Description("A list of arbitrary string tag/value pairs as connection arguments. This is a semicolon-separated " +
    "list of key-value pairs, where each pair is separated by a equals '=' and specifies the key and value for the " +
    "argument. For example, 'key1=value1;key2=value' specifies that the connection will be given arguments 'key1' " +
    "mapped to 'value1' and the argument 'key2' mapped to 'value2'.")
  @Nullable
  @Macro
  public String connectionArguments;

  public ConnectionConfig() {
  }

  /**
   * Parses connection arguments into a {@link Properties}.
   *
   * @param connectionArguments See {@link ConnectionConfig#connectionArguments}.
   * @param user                See {@link ConnectionConfig#user}.
   * @param password            See {@link ConnectionConfig#password}.
   */
  public static Properties getConnectionArguments(@Nullable String connectionArguments,
                                                  @Nullable String user, @Nullable String password) {
    KeyValueListParser kvParser = new KeyValueListParser("\\s*;\\s*", "=");

    Map<String, String> connectionArgumentsMap = new HashMap<>();
    if (!Strings.isNullOrEmpty(connectionArguments)) {
      for (KeyValue<String, String> keyVal : kvParser.parse(connectionArguments)) {
        connectionArgumentsMap.put(keyVal.getKey(), keyVal.getValue());
      }
    }

    if (user != null) {
      connectionArgumentsMap.put("user", user);
      connectionArgumentsMap.put("password", password);
    }

    Properties properties = new Properties();
    properties.putAll(connectionArgumentsMap);

    return properties;
  }

  /**
   * @return a {@link Properties} of connection arguments, parsed from the config.
   */
  public Properties getConnectionArguments() {
    Properties arguments = getConnectionArguments(this.connectionArguments, user, password);
    arguments.putAll(getDBSpecificArguments());
    return arguments;
  }

  public abstract Map<String, String> getDBSpecificArguments();
}
