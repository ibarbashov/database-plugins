# Database Batch Source


Description
-----------
Reads from a database using a configurable SQL query.
Outputs one record for each row returned by the query.


Use Case
--------
The source is used whenever you need to read from a database. For example, you may want
to create daily snapshots of a database table by using this source and writing to
a TimePartitionedFileSet.


Properties
----------
**Reference Name:** Name used to uniquely identify this sink for lineage, annotating metadata, etc.

**Host:** host where database running on.

**Port:** database port.

**Database:** database name.

**Driver Name:** Name of the JDBC driver to use.

**Import Query:** The SELECT query to use to import data from the specified table.
You can specify an arbitrary number of columns to import, or import all columns using \*. The Query should
contain the '$CONDITIONS' string. For example, 'SELECT * FROM table WHERE $CONDITIONS'.
The '$CONDITIONS' string will be replaced by 'splitBy' field limits specified by the bounding query.
The '$CONDITIONS' string is not required if numSplits is set to one. (Macro-enabled)

**Bounding Query:** Bounding Query should return the min and max of the values of the 'splitBy' field.
For example, 'SELECT MIN(id),MAX(id) FROM table'. Not required if numSplits is set to one. (Macro-enabled)

**Split-By Field Name:** Field Name which will be used to generate splits. Not required if numSplits is set to one. (Macro-enabled)

**Number of Splits to Generate:** Number of splits to generate. (Macro-enabled)

**Username:** User identity for connecting to the specified database. (Macro-enabled)

**Password:** Password to use to connect to the specified database. (Macro-enabled)

**Connection Arguments:** A list of arbitrary string tag/value pairs as connection arguments. These arguments
will be passed to the JDBC driver, as connection arguments, for JDBC drivers that may need additional configurations.
This is a semicolon-separated list of key-value pairs, where each pair is separated by a equals '=' and specifies
the key and value for the argument. For example, 'key1=value1;key2=value' specifies that the connection will be
given arguments 'key1' mapped to 'value1' and the argument 'key2' mapped to 'value2'. (Macro-enabled)

**Transaction Isolation Level:** The transaction isolation level for queries run by this sink.
Defaults to TRANSACTION_SERIALIZABLE. See java.sql.Connection#setTransactionIsolation for more details.
The Phoenix jdbc driver will throw an exception if the Phoenix database does not have transactions enabled
and this setting is set to true. For drivers like that, this should be set to TRANSACTION_NONE.

**Schema:** The schema of records output by the source. This will be used in place of whatever schema comes
back from the query. However, it must match the schema that comes back from the query,
except it can mark fields as nullable and can contain a subset of the fields.


Example
-------

    {
        "name": "Database",
        "type": "batchsource",
        "properties": {
            "importQuery": "select id,name,email,phone from users where $CONDITIONS",
            "boundingQuery": "select min(id),max(id) from users",
            "splitBy": "id",
            "host": "localhost",
            "port": 3306,
            "database": "prod",
            "user": "root",
            "password": "root",
            "jdbcDriverName": "mysql"
        }
    }

For example, if the 'id' column is a primary key of type int and the other columns are
non-nullable varchars, output records will have this schema:

    +======================================+
    | field name     | type                |
    +======================================+
    | id             | int                 |
    | name           | string              |
    | email          | string              |
    | phone          | string              |
    +======================================+
