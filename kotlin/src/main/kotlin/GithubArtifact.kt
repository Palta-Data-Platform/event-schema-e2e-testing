import com.google.gson.annotations.SerializedName

data class GithubArtifact(
    val id: Int,
    val expired: Boolean,
    val name: String,
    @SerializedName("archive_download_url") val archiveDownloadUrl: String
)

data class GithubArtifactsResponse(
    @SerializedName("total_count") val totalCount: Int,
    val artifacts: Array<GithubArtifact>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GithubArtifactsResponse

        if (totalCount != other.totalCount) return false
        return artifacts.contentEquals(other.artifacts)
    }

    override fun hashCode(): Int {
        var result = totalCount
        result = 31 * result + artifacts.contentHashCode()
        return result
    }
}
