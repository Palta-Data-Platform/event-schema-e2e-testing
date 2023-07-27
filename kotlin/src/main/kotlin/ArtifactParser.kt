import com.google.gson.Gson

interface ArtifactParser {
    fun parse(data: String): Array<GithubArtifact>
}

class ArtifactParserImpl: ArtifactParser {
    override fun parse(data: String): Array<GithubArtifact> {
        val gson = Gson()
        val response = gson.fromJson(data, GithubArtifactsResponse::class.java)
        return response.artifacts
    }
}