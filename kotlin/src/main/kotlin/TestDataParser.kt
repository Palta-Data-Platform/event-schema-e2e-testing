import com.google.gson.Gson
import java.io.FileWriter

class TestDataParser {
    @OptIn(ExperimentalStdlibApi::class)
    fun parseTestData(arguments: List<String>): TestStartData {
        val sdkName = arguments[0]
        val sdkVersion = arguments[1]
        val casesCount = (arguments.count() - 2) / 5
        val gson = Gson()
        var events: MutableList<TestCaseEvent> = mutableListOf()

        for (i in 0..<casesCount) {
            val index = i * 5 + 3

            val eventProperties = gson.fromJson(arguments[index + 2], HashMap::class.java)
            val headerProperties = gson.fromJson(arguments[index + 3], HashMap::class.java)
            val contextProperties = gson.fromJson(arguments[index + 4], HashMap::class.java)

            events.add(
                TestCaseEvent(
                    arguments[index].toInt(),
                    arguments[index + 1],
                    sdkName,
                    sdkVersion,
                    eventProperties.mapKeys { it.key as String },
                    headerProperties.mapKeys { it.key as String },
                    contextProperties.mapKeys { it.key as String }
                )
            )
        }

        return TestStartData(sdkVersion, arguments[2], events.toTypedArray())
    }
}