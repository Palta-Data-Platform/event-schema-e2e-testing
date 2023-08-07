class TestCaseCheckWorkflow(
    private val artifact: GithubArtifact,
    private val downloader: TestDataDownloader,
    private val snowflakeConnector: SnowflakeConnector,
    private val testResultSaver: TestResultSaver,
    private val workflowStarter: GithubWorkflowStarter
) {
    fun start(): Boolean {
        val testStartData = downloader.downloadTestData(artifact)
        val checker = EventsChecker(snowflakeConnector)
        var results: MutableList<TestCaseRun> = mutableListOf()

        for (testCase in testStartData.testCases) {
            results.add(checker.check(testCase))
        }

        testResultSaver.save(results, artifact)
        val testSucceeded = results.all { it.result == TestCaseResult.OK }

        if (testSucceeded) {
            println("All test cases passed for ${artifact.name}. Starting next action: ${testStartData.nextAction}")
            workflowStarter.startWorkflow(testStartData.nextAction, testStartData.sdkVersion)
        }

        return testSucceeded
    }
}