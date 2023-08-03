import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.util.zip.ZipFile
import kotlin.io.path.Path
import kotlin.io.path.createFile

class TestDataDownloader(private val apiKey: String) {
    fun downloadTestData(artifact: GithubArtifact): TestStartData {
        try {
            downloadFile(URL(artifact.archiveDownloadUrl), "${artifact.name}.zip")
            unzip(artifact.name)
            return decodeJSON(artifact.name)
        } finally {
            cleanUp(artifact.name)
        }
    }

    private fun downloadFile(url: URL, fileName: String) {
        Path(fileName).createFile()

        val connection = url.openConnection()
        connection.setRequestProperty("Authorization", "Bearer $apiKey")

        connection.getInputStream().copyTo(File(fileName).outputStream())
    }

    private fun unzip(filename: String) {
        ZipFile("$filename.zip").use { zip ->
            val entry = zip.getEntry(filename)
            zip.getInputStream(entry).use { inputStream ->
                val bos = BufferedOutputStream(FileOutputStream("$filename.json"))
                val bytesIn = ByteArray(4096)
                var read: Int
                while (inputStream.read(bytesIn).also { read = it } != -1) {
                    bos.write(bytesIn, 0, read)
                }
                bos.close()
            }
        }
    }

    private fun decodeJSON(filename: String): TestStartData {
        val gson = Gson()
        val jsonReader = JsonReader(FileReader("$filename.json"))
        return gson.fromJson(jsonReader, TestStartData::class.java)
    }

    private fun cleanUp(filename: String) {
        val json = File("$filename.json")
        val zip = File("$filename.zip")

        if (json.exists()) {
            json.delete()
        }

        if (zip.exists()) {
            zip.delete()
        }
    }
}