# Database Action


Description
-----------
Action that runs a database command.


Use Case
--------
The action can be used whenever you want to run a database command before or after a data pipeline.
For example, you may want to run a sql update command on a database before the pipeline source pulls data from tables.


Properties
----------
**Database Command:** The database command to execute.

**Host:** host where database running on.

**Port:** database port.

**Database:** database name.

**Username:** User identity for connecting to the specified database.

**Password:** Password to use to connect to the specified database.

**Driver Name:** Name of the JDBC driver to use.

**Connection Arguments:** A list of arbitrary string tag/value pairs as connection arguments. These arguments
will be passed to the JDBC driver, as connection arguments, for JDBC drivers that may need additional configurations.
This is a semicolon-separated list of key-value pairs, where each pair is separated by a equals '=' and specifies
the key and value for the argument. For example, 'key1=value1;key2=value' specifies that the connection will be
given arguments 'key1' mapped to 'value1' and the argument 'key2' mapped to 'value2'. (Macro-enabled)

Example
-------

    {
        "name": "Database",
        "plugin": {
            "name": "Database",
            "type": "action",
            "properties": {
                "query": "UPDATE table_name SET price = 20 WHERE ID = 6",
                "host": "localhost",
                "port": 3306,
                "database": "prod",
                "user": "user123",
                "password": "password-abc",
                "jdbcDriverName": "mysql"
            }
        }
    }
