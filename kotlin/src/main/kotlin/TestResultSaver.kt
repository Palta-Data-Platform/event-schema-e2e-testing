import com.google.gson.Gson
import java.io.FileWriter

class TestResultSaver {
    fun save(results: List<TestCaseRun>, artifact: GithubArtifact) {
        val gson = Gson()
        val filePath = "${artifact.name.replace("start", "finish")}.json"

        val fileWriter = FileWriter(filePath)
        gson.toJson(results, fileWriter)
        fileWriter.flush()
        fileWriter.close()
    }
}