import com.google.gson.Gson
import java.io.FileWriter

class TestDataParser {
    @OptIn(ExperimentalStdlibApi::class)
    fun parseTestData(arguments: List<String>): TestStartData {
        val sdkName = arguments[0]
        val sdkVersion = arguments[1]
        val casesCount = (arguments.count() - 3) / 5
        val gson = Gson()
        var events: MutableList<TestCaseEvent> = mutableListOf()

        for (i in 0..<casesCount) {
            val eventProperties = gson.fromJson(arguments[3 + 2*casesCount + i], HashMap::class.java)
            val headerProperties = gson.fromJson(arguments[3 + 3*casesCount + i], HashMap::class.java)
            val contextProperties = gson.fromJson(arguments[3 + 4*casesCount + i], HashMap::class.java)

            events.add(
                TestCaseEvent(
                    arguments[i + 3].toInt(),
                    arguments[3 + casesCount + i],
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