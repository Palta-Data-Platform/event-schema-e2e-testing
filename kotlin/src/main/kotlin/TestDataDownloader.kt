import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import java.io.BufferedOutputStream
import java.io.FileOutputStream
import java.io.FileReader
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.util.zip.ZipFile

class TestDataDownloader {
    fun downloadTestData(artifact: GithubArtifact): TestStartData {
        downloadFile(URL(artifact.archiveDownloadUrl), "${artifact.name}.zip")
        unzip(artifact.name)
        return  decodeJSON(artifact.name)
    }

    private fun downloadFile(url: URL, fileName: String) {
        url.openStream().use { Files.copy(it, Paths.get(fileName)) }
    }

    private fun unzip(filename: String) {
        ZipFile("$filename.zip").use { zip ->
            zip.getInputStream(zip.getEntry("test.json")).use { inputStream ->
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
}