class TestCaseCheckWorkflow(
    private val artifact: GithubArtifact,
    private val downloader: TestDataDownloader,
    private val snowflakeConnector: SnowflakeConnector,
    private val testResultSaver: TestResultSaver
) {
    fun start() {
        val testStartData = downloader.downloadTestData(artifact)
        val checker = EventsChecker(snowflakeConnector)
        var results: MutableList<TestCaseRun> = mutableListOf()

        for (testCase in testStartData.testCases) {
            results.add(checker.check(testCase))
        }

        testResultSaver.save(results, artifact)
    }
}