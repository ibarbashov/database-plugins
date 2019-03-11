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

import co.cask.ConnectionConfig;
import co.cask.cdap.api.annotation.Description;
import co.cask.cdap.api.annotation.Macro;

/**
 * Config for Actions running database commands
 */
public abstract class QueryConfig extends ConnectionConfig {
  public static final String QUERY = "query";

  @Description("The database command to run.")
  @Macro
  public String query;

  public QueryConfig() {
    super();
  }
}
