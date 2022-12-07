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
private const val wrongAnswer = "That's not the right answer."
private val rateLimitAnswer = "You have (.*) left to wait".toRegex()

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

    fun postAnswer(year: Int, day: Int, level: Int, answer: String): Boolean {
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
        val (result, isErr) = when {
            rightAnswer in responseStr -> "right answer!" to false
            alreadyCompleted in responseStr -> "not the right level, already completed?" to false
            wrongAnswerTooLow in responseStr -> "wrong, answer is too low" to true
            wrongAnswerTooHigh in responseStr -> "wrong, answer is too high" to true
            wrongAnswer in responseStr -> "wrong answer" to true
            rateLimitAnswer.containsMatchIn(responseStr) -> "rate limited, try again in ${rateLimitAnswer.find(responseStr)!!.groupValues[1]}" to true
            else -> responseStr to true
        }

        val code = if (isErr) {
            "\u001B[1;31m" // Bold;red
        } else {
            "\u001B[1;32m" // Bold;green
        }
        println("${code}Response: $result\u001B[0m")
        responseBody.close()
        return !isErr
    }
}
