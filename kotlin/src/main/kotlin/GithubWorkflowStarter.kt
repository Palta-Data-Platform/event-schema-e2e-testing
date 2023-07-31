import com.google.gson.Gson
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class GithubWorkflowStarter(
    private val repo: String,
    private val owner: String,
    private val apiKey: String
) {
    fun startWorkflow(name: String, version: String) {
        val gson = Gson()

        val payload = mapOf(
            name to "event_type",
            mapOf(version to "version") to "client_payload"
        )

        val httpClient = HttpClient.newBuilder().build()
        val request = HttpRequest
            .newBuilder()
            .uri(URI.create("https://api.github.com/repos/$owner/$repo/dispatches"))
            .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(payload)))
            .header("accept", "application/vnd.github+json")
            .header("Authorization", "Bearer $apiKey")
            .build()

        httpClient.send(request, HttpResponse.BodyHandlers.ofString())
    }
}