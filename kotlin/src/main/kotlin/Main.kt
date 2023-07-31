import com.google.gson.Gson
import org.slf4j.LoggerFactory

fun main(args: Array<String>) {
    val action = args[0]
    val nextArgs = args.drop(1)

    if (action == "save") {
        TestStartDataWriter(TestDataParser()).writeTestData(nextArgs)
    } else if (action == "test") {
        test(nextArgs)
    } else {
        println("Specify action: save or test")
    }
}

private fun test(args: List<String>) {
    val repoOwner = args[0]
    val repoName = args[1]
    val apiKey = args[2]
    val snowflakePrivateKeyPath = args[3]
    val snowflakeUser = args[4]
    val snowflakeClient = args[5]

    val downloader = ArtifactDownloader(repoOwner, repoName, apiKey, ArtifactParserImpl())
    val artifacts = downloader.downloadArtifacts()
    val artifactsToTest = ArtifactsMatcher().unmatchedArtifacts(artifacts)

    val snowflakeConnector = SnowflakeConnector(snowflakePrivateKeyPath, snowflakeUser, snowflakeClient)

    for (artifact in artifactsToTest) {
        TestCaseCheckWorkflow(
            artifact,
            TestDataDownloader(),
            snowflakeConnector,
            TestResultSaver(),
            GithubWorkflowStarter(repoName, repoOwner, apiKey)
        )
    }
}