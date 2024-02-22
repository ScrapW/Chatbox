package com.scrapw.chatbox

import android.util.Log
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubReleaseService {
    @GET("/repos/{owner}/{repo}/releases/latest")
    suspend fun getLatestRelease(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Response<GitHubRelease>

    data class GitHubRelease(
        @SerializedName("tag_name")
        val tagName: String,
        val assets: List<GitHubAsset>
    )

    data class GitHubAsset(
        @SerializedName("browser_download_url")
        val browserDownloadUrl: String
    )
}

enum class UpdateStatus {
    NOT_CHECKED,
    FAILED,
    UP_TO_DATE,
    AVAILABLE
}

data class UpdateInfo(
    val status: UpdateStatus,
    val version: String? = null,
    val downloadUrl: String? = null
)

suspend fun checkUpdate(owner: String, repo: String): UpdateInfo {

//    if (BuildConfig.DEBUG) {
//        return UpdateInfo(
//            UpdateStatus.AVAILABLE,
//            "v1.0.0 (Example version for debug)",
//            "https://example.com"
//        )
//    }

    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val releaseService = retrofit.create(GitHubReleaseService::class.java)

    try {
        lateinit var response: Response<GitHubReleaseService.GitHubRelease>

        withContext(Dispatchers.IO) {
            response = releaseService.getLatestRelease(owner, repo)
        }

        if (response.isSuccessful) {
            val data = response.body()

            if (data != null && data.assets.isNotEmpty()) {
                if (BuildConfig.VERSION_NAME == data.tagName) {
                    Log.d("Update", "Up to date.")
                    return UpdateInfo(
                        UpdateStatus.UP_TO_DATE,
                        data.tagName,
                        data.assets[0].browserDownloadUrl
                    )
                } else {
                    Log.d("Update", "New version: ${data.tagName}")
                    return UpdateInfo(
                        UpdateStatus.AVAILABLE,
                        data.tagName,
                        data.assets[0].browserDownloadUrl
                    )
                }
            }
        } else {
            Log.d("Update", "Response failed.")
        }
    } catch (e: Exception) {
        Log.d("Update", "Failed with exception.")
        e.printStackTrace()
    }

    return UpdateInfo(
        UpdateStatus.FAILED
    )
}
