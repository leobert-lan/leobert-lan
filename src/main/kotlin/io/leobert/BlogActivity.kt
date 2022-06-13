package io.leobert

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

internal interface BlogApi {

    @POST("/content_api/v1/article/query_list")
    @Headers(value = ["Content-Type: application/json"])
    suspend fun blogList(@Body param: BlogQueryParams): Blogs

    companion object {
        fun create(client: OkHttpClient, gson: Gson): BlogApi {
            return Retrofit.Builder()
                .baseUrl("https://api.juejin.cn")
//                .validateEagerly(true)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create()
        }
    }
}

class BlogQueryParams(
    @SerializedName("user_id")
    val userId: String,

    /*1 by hot,2 by time*/
    @SerializedName("sort_type")
    val sortType: Int = 2,
    @SerializedName("cursor")
    val cursor: String = "0",
)

class Blogs(
    @SerializedName("err_msg")
    val msg: String,
    @SerializedName("data")
    val feeds: List<Feed>?
)

class Feed(
    @SerializedName("article_id")
    val articleId: String,

    @SerializedName("article_info")
    val entry: Entry
)


class Entry(

    @field:SerializedName("user_id")
    val userId: String? = null,

    @field:SerializedName("article_id")
    val articleId: String? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("cover_image")
    val coverImage: String? = null,

    @field:SerializedName("brief_content")
    val briefContent: String? = null,

    @field:SerializedName("ctime")
    val ctime: Long? = null,

    @field:SerializedName("rtime")
    val rtime: Long? = null,

    @field:SerializedName("mtime")
    val mtime: Long? = null,

    @field:SerializedName("is_original")
    val isOriginal: Int? = null,

    @field:SerializedName("comment_count")
    val commentCount: Int? = null,

    @field:SerializedName("collect_count")
    val collectCount: Int? = null,

    @field:SerializedName("digg_count")
    val diggCount: Int? = null,

    @field:SerializedName("display_count")
    val displayCount: Int? = null,

    @field:SerializedName("view_count")
    val viewCount: Int? = null,
)

