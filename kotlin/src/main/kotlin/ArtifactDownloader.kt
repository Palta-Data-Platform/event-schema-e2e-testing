import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class ArtifactDownloader(
    private val repoOwner: String,
    private val repoName: String,
    private val apiKey: String,
    private val parser: ArtifactParser
) {
    fun downloadArtifacts(): Array<GithubArtifact> {
        val httpClient = HttpClient.newBuilder().build()
        val request = HttpRequest
            .newBuilder()
            .uri(URI.create("https://api.github.com/repos/$repoOwner/$repoName/actions/artifacts?per_page=100"))
            .GET()
            .header("accept", "application/vnd.github+json")
            .header("Authorization", "Bearer $apiKey")
            .build()

        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
        val responseBody = response.body()

        return parser.parse(responseBody)
    }
}