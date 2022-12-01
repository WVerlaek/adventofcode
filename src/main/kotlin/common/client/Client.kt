package common.client

import okhttp3.FormBody
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import java.nio.file.Files
import kotlin.io.path.Path

private const val sessionIdFile = ".secrets/session"
private const val sessionIdEnv = "AOC_SESSION"

private const val rightAnswer = "That's the right answer!"
private const val alreadyCompleted = "You don't seem to be solving the right level."
private const val wrongAnswerTooLow = "too low"
private const val wrongAnswerTooHigh = "too high"

private fun loadSessionId(): String {
    return System.getenv(sessionIdEnv)
        ?: String(Files.readAllBytes(Path(sessionIdFile))).trim()
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
        val responseStr = responseBody.string()
        val result = when {
            rightAnswer in responseStr -> "right answer!"
            alreadyCompleted in responseStr -> "not the right level, already completed?"
            wrongAnswerTooLow in responseStr -> "wrong, answer is too low"
            wrongAnswerTooHigh in responseStr -> "wrong, answer is too high"
            else -> responseStr
        }

        println("Response: $result")
        responseBody.close()
    }
}
