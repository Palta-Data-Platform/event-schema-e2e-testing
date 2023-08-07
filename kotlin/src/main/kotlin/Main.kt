import com.google.gson.Gson
import org.slf4j.LoggerFactory
import kotlin.system.exitProcess

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

    if (artifactsToTest.isNotEmpty()) {
        val snowflakeConnector = SnowflakeConnector(snowflakePrivateKeyPath, snowflakeUser, snowflakeClient)
        val workflow = TestCaseCheckWorkflow(
            artifactsToTest.first(),
            TestDataDownloader(apiKey),
            snowflakeConnector,
            TestResultSaver(),
            GithubWorkflowStarter(repoName, repoOwner, apiKey)
        )

        val testSuccess = workflow.start()

        if (!testSuccess) {
            exitProcess(100)
        }
    }
}