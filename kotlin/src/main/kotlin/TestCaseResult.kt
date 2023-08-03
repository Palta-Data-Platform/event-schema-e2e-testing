import com.google.gson.annotations.SerializedName

enum class TestCaseResult {
    OK, NOT_FOUND, INCORRECT_QUERY, EVENT_PROPERTY_WRONG, CONTEXT_PROPERTY_WRONG, HEADER_PROPERTY_WRONG, SDK_INFO_WRONG
}

data class TestCaseRun(val result: TestCaseResult, val eventName: String)
