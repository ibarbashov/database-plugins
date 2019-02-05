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

package co.cask.mysql;

import co.cask.cdap.api.annotation.Description;
import co.cask.cdap.api.annotation.Name;
import co.cask.cdap.api.annotation.Plugin;
import co.cask.cdap.etl.api.batch.PostAction;
import co.cask.db.batch.action.QueryAction;
import co.cask.db.batch.action.QueryActionConfig;
import com.google.common.collect.ImmutableMap;

import java.util.Map;
import javax.annotation.Nullable;

/**
 * Represents MySQL post action.
 */
@Plugin(type = PostAction.PLUGIN_TYPE)
@Name(MysqlConstants.PLUGIN_NAME)
@Description("Runs a MySQL query after a pipeline run.")
public class MysqlPostAction extends QueryAction implements MysqlDriverNameProvider {

  private final MysqlQueryActionConfig mysqlQueryActionConfig;

  public MysqlPostAction(MysqlQueryActionConfig mysqlQueryActionConfig) {
    super(mysqlQueryActionConfig);
    this.mysqlQueryActionConfig = mysqlQueryActionConfig;
  }

  /**
   * MySQL post action mysqlQueryActionConfig.
   */
  public static class MysqlQueryActionConfig extends QueryActionConfig {

    @Name(MysqlConstants.AUTO_RECONNECT)
    @Description("Should the driver try to re-establish stale and/or dead connections")
    public Boolean autoReconnect;

    @Name(MysqlConstants.ALLOW_MULTIPLE_QUERIES)
    @Nullable
    @Description("Allow the use of ';' to delimit multiple queries during one statement (true/false)")
    public Boolean allowMultiQueries;


    @Override
    public Map<String, String> getDBSpecificArguments() {
      ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();

      if (allowMultiQueries != null) {
        builder.put(MysqlConstants.ALLOW_MULTIPLE_QUERIES, String.valueOf(allowMultiQueries));
      }

      builder.put(MysqlConstants.AUTO_RECONNECT, String.valueOf(autoReconnect));
      return builder.build();
    }
  }
}
