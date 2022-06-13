package io.leobert

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.file
import com.google.gson.Gson
import com.squareup.moshi.Moshi
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import java.time.Instant
import java.time.ZoneId
import kotlin.system.exitProcess

class UpdateReadmeCommand : CliktCommand() {

    val outputFile by option("-o", help = "The README.md file to write")
        .file()
        .required()

    override fun run() {
        val okHttpClient = OkHttpClient.Builder()
            .build()

        val githubActivity = fetchGithubActivity(okHttpClient)
        val blogActivity = fetchBlogActivity(okHttpClient, "2066737589654327")

        val newReadMe = createReadMe(githubActivity, blogActivity)
        outputFile.writeText(newReadMe)

        // TODO why do I need to do this
        exitProcess(0)
    }
}

private fun fetchBlogActivity(
    client: OkHttpClient,
    userId: String
): List<ActivityItem> {
    val blogApi = BlogApi.create(
        client, Gson()
    )

    //json: cannot unmarshal number into Go struct field QueryArticleListReq.cursor of type string
    return runBlocking {
        blogApi.blogList(BlogQueryParams(userId = userId))
    }.feeds?.map { feed ->
        ActivityItem(
            text = "[${feed.entry.title}](https://juejin.cn/post/${feed.entry.articleId})",
            timestamp = Instant.ofEpochSecond(feed.entry.mtime ?: feed.entry.ctime ?: 0, 0)
        )
    }?.take(10) ?: emptyList()
}

private fun fetchGithubActivity(
    client: OkHttpClient
): List<ActivityItem> {
    val moshi = Moshi.Builder().build()
    val githubApi = GitHubApi.create(client, moshi)
    val activity = runBlocking { githubApi.getUserActivity("leobert-lan") }
    return activity
        .filter { it.public }
        .mapNotNull { event ->
            when (val payload = event.payload) {
                UnknownPayload, null -> return@mapNotNull null
                is IssuesEventPayload -> {
                    ActivityItem(
                        "${payload.action} issue [#${payload.issue.number}](${payload.issue.htmlUrl}) on ${event.repo?.markdownUrl()}: \"${payload.issue.title}\"",
                        event.createdAt
                    )
                }
                is IssueCommentEventPayload -> {
                    ActivityItem(
                        "commented on [#${payload.issue.number}](${payload.comment.htmlUrl}) in ${event.repo?.markdownUrl()}",
                        event.createdAt
                    )
                }
                is PullRequestPayload -> {
                    val action = if (payload.pullRequest.merged == true) "merged" else payload.action
                    ActivityItem(
                        "$action PR [#${payload.number}](${payload.pullRequest.htmlUrl}) to ${event.repo?.markdownUrl()}: \"${payload.pullRequest.title}\"",
                        event.createdAt
                    )
                }
                is CreateEvent -> {
                    ActivityItem(
                        "created ${payload.refType}${payload.ref?.let { " `$it`" } ?: ""} on ${event.repo?.markdownUrl()}",
                        event.createdAt
                    )
                }
                is DeleteEvent -> {
                    ActivityItem(
                        "deleted ${payload.refType}${payload.ref?.let { " `$it`" } ?: ""} on ${event.repo?.markdownUrl()}",
                        event.createdAt
                    )
                }
            }
        }
        .take(10)
}

fun main(argv: Array<String>) {
    UpdateReadmeCommand().main(argv)
}

data class ActivityItem(
    val text: String,
    val timestamp: Instant
) {
    override fun toString(): String {
        return "**${timestamp.atZone(ZoneId.of("Asia/Shanghai")).toLocalDate()}** — $text"
    }
}