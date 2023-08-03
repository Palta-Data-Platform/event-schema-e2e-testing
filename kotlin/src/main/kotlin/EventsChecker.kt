import net.snowflake.client.jdbc.SnowflakeSQLException
import java.sql.ResultSet

class EventsChecker(private val snowflakeConnector: SnowflakeConnector) {
    fun check(testCase: TestCaseEvent): TestCaseRun {
        val resultSet: ResultSet

        try {
            resultSet = snowflakeConnector.runQuery(testCase.name, testCase.timestamp)
        } catch (e: SnowflakeSQLException) {
            return TestCaseRun(TestCaseResult.INCORRECT_QUERY, testCase.name)
        }

        if (!resultSet.next()) {
            return TestCaseRun(TestCaseResult.NOT_FOUND, testCase.name)
        }

        if (
            resultSet.getString("SDK_NAME") != testCase.sdkName
            || resultSet.getString("SDK_VERSION") != testCase.sdkVersion
        ) {
            return TestCaseRun(TestCaseResult.SDK_INFO_WRONG, testCase.name)
        }

        for ((key, value ) in testCase.eventProperties) {
            if (!checkProperty("p_$key`".uppercase(), value, resultSet)) {
                return TestCaseRun(TestCaseResult.EVENT_PROPERTY_WRONG, testCase.name)
            }
        }

        for ((key, value ) in testCase.contextProperties) {
            if (!checkProperty("c_$key".uppercase(), value, resultSet)) {
                return TestCaseRun(TestCaseResult.CONTEXT_PROPERTY_WRONG, testCase.name)
            }
        }

        for ((key, value ) in testCase.headerProperties) {
            if (!checkProperty("h_$key".uppercase(), value, resultSet)) {
                return TestCaseRun(TestCaseResult.HEADER_PROPERTY_WRONG, testCase.name)
            }
        }

        return TestCaseRun(TestCaseResult.OK, testCase.name)
    }

    private fun checkProperty(name: String, value: Any, resultSet: ResultSet): Boolean {
        if (value is String) {
            return resultSet.getString(name) == value
        } else if (value is Int) {
            return  resultSet.getInt(name) == value
        } else if (value is Boolean) {
            return resultSet.getBoolean(name) == value
        } else {
            return false
        }
    }
}