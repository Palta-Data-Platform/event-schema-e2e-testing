import com.google.gson.Gson
import java.io.FileWriter

class TestStartDataWriter(val testStartDataParser: TestDataParser) {
    fun writeTestData(arguments: List<String>) {
        val gson = Gson()
        val data = testStartDataParser.parseTestData(arguments)
        val fileWriter = FileWriter("test-start-${System.currentTimeMillis() / 1000}")
        gson.toJson(data, fileWriter)
        fileWriter.flush()
        fileWriter.close()
    }
}