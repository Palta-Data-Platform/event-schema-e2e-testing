data class TestStartData(
    val sdkVersion: String,
    val nextAction: String,
    val testCases: Array<TestCaseEvent>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TestStartData

        return testCases.contentEquals(other.testCases)
    }

    override fun hashCode(): Int {
        return testCases.contentHashCode()
    }
}

data class TestCaseEvent(
    val timestamp: Int,
    val name: String,
    val sdkName: String,
    val sdkVersion: String,
    val eventProperties: Map<String, Any>,
    val headerProperties: Map<String, Any>,
    val contextProperties: Map<String, Any>
)