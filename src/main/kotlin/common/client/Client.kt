package common.client

import okhttp3.FormBody
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import java.nio.file.Files
import kotlin.io.path.Path

private const val sessionIdFile = ".secrets/session"

private fun loadSessionId(): String {
    return Files.readString(Path(sessionIdFile))
}

class Client(private val baseUrl: String = "https://adventofcode.com") {
    private val logging = HttpLoggingInterceptor().also {
        it.level = HttpLoggingInterceptor.Level.NONE
    }

    private val client = OkHttpClient.Builder()
        .retryOnConnectionFailure(false)
        .addInterceptor(logging)
        .build()

    private val headers = Headers.Builder()
        .set("Cookie", "session=${loadSessionId()}")
        .build()

    fun getInput(year: Int, day: Int): String {
        val url = "$baseUrl/$year/day/$day/input"
        val request = Request.Builder()
            .url(url)
            .headers(headers)
            .get()
            .build()
        val response = client.newCall(request).execute()
        val responseBody = response.body ?: throw RuntimeException("Failed to get input from $url, response code ${response.code}")
        val input = responseBody.string()
        responseBody.close()
        return input
    }

    fun postAnswer(year: Int, day: Int, level: Int, answer: String) {
        val url = "$baseUrl/$year/day/$day/answer"
        val requestBody = FormBody.Builder()
            .add("level", level.toString())
            .add("answer", answer)
            .build()
        val request = Request.Builder()
            .url(url)
            .headers(headers)
            .post(requestBody)
            .build()
        val response = client.newCall(request).execute()
        val responseBody = response.body ?: throw RuntimeException("Failed to get input from $url, response code ${response.code}")
        println("Uploaded answer (year $year, day $day, level $level). Response: ${responseBody.string()}")
        responseBody.close()
    }
}
