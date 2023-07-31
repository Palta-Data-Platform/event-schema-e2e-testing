import java.io.File
import java.security.PrivateKey
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.util.Properties
import java.security.KeyFactory
import java.security.spec.PKCS8EncodedKeySpec

class SnowflakeConnector(private val privateKeyPath: String, private val username: String, private val client: String) {
    private val connection: Connection
    init {
        val jdbcUrl = "jdbc:snowflake://palta-prod.snowflakecomputing.com/"

        val prop = Properties()
        prop["private_key_file"] = privateKeyPath
        prop["private_key_file_pwd"] = ""
        prop["user"] = username

        Class.forName("net.snowflake.client.jdbc.SnowflakeDriver")
        connection = DriverManager.getConnection(jdbcUrl, prop)
    }
    fun runQuery(eventName: String, timestamp: Int): ResultSet {
        val tableName = "${client}.event.f_${eventName.camelToSnakeCase()}"
        val query = connection.prepareStatement(
            "SELECT * FROM $tableName WHERE event_ts >= TO_TIMESTAMP_NTZ(${timestamp - 5000}) AND event_ts <= TO_TIMESTAMP_NTZ(${timestamp + 5000})"
        )
        return query.executeQuery()
    }
}