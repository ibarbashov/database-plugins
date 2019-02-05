# Database Batch Sink


Description
-----------
Writes records to a database table. Each record will be written to a row in the table.


Use Case
--------
This sink is used whenever you need to write to a database table.
Suppose you periodically build a recommendation model for products on your online store.
The model is stored in a FileSet and you want to export the contents
of the FileSet to a database table where it can be served to your users.

Column names would be autodetected from input schema.

Properties
----------
**Reference Name:** Name used to uniquely identify this sink for lineage, annotating metadata, etc.

**Host:** host where database running on.

**Port:** database port.

**Database:** database name.

**Table Name:** Name of the table to export to. (Macro-enabled)

**Username:** User identity for connecting to the specified database. (Macro-enabled)

**Password:** Password to use to connect to the specified database. (Macro-enabled)

**Driver Name:** Name of the JDBC driver to use.

**Connection Arguments:** A list of arbitrary string tag/value pairs as connection arguments. These arguments
will be passed to the JDBC driver, as connection arguments, for JDBC drivers that may need additional configurations.
This is a semicolon-separated list of key-value pairs, where each pair is separated by a equals '=' and specifies
the key and value for the argument. For example, 'key1=value1;key2=value' specifies that the connection will be
given arguments 'key1' mapped to 'value1' and the argument 'key2' mapped to 'value2'. (Macro-enabled)

**Transaction Isolation Level:** The transaction isolation level for queries run by this sink.
Defaults to TRANSACTION_SERIALIZABLE. See java.sql.Connection#setTransactionIsolation for more details.
The Phoenix jdbc driver will throw an exception if the Phoenix database does not have transactions enabled
and this setting is set to true. For drivers like that, this should be set to TRANSACTION_NONE.

Example
-------

    {
        "name": "Database",
        "type": "batchsink",
        "properties": {
            "tableName": "users",
            "host": "localhost",
            "port": 3306,
            "database": "prod",
            "user": "root",
            "password": "root",
            "jdbcDriverName": 3306
        }
    }
