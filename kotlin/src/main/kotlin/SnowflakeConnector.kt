import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet

class SnowflakeConnector(private val token: String, private val client: String) {
    private val connection: Connection
    init {
        val jdbcUrl = "jdbc:postgresql://localhost:5432/example"
        connection = DriverManager.getConnection(jdbcUrl)
    }
    fun runQuery(eventName: String, timestamp: Int): ResultSet {
        val tableName = "${client}.event.${eventName.camelToSnakeCase()}"
        val query = connection.prepareStatement(
            "SELECT * FROM $tableName WHERE event_ts >= TO_TIMESTAMP_NTZ(${timestamp - 5000}) AND event_ts <= TO_TIMESTAMP_NTZ(${timestamp + 5000})"
        )
        return query.executeQuery()
    }
}