package com.github.ebingelis.simplenbaapp.model

import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

data class SelectedTeamData(
    val homeTeamFullName: String?,
    val homeTeamScore: String?,
    val visitorTeamFullName: String?,
    val visitorTeamScore: String?
)

class SelectedTeam {

    var maximumPages = 0

    suspend fun getTeamData(id: Int, page: Int = 1): List<SelectedTeamData> {

        val dataList = mutableListOf<SelectedTeamData>()

        val client = OkHttpClient()

        val urlBuilder =
            "https://www.balldontlie.io/api/v1/games?&team_ids[]=$id".toHttpUrl().newBuilder()
        urlBuilder.addQueryParameter("page", "$page")

        val url = urlBuilder.build()

        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        val response = client.newCall(request).execute()

        val responseBody = response.body?.string()

        if (!response.isSuccessful) {
            throw Exception("API request failed with code ${response.code}")
        }

        try {
            val responseJSON = JSONObject(responseBody.toString())

            val dataArray = responseJSON.getJSONArray("data")

            for (i in 0 until dataArray.length()) {
                val jsonObject = dataArray.getJSONObject(i)
                val homeTeamJSON = jsonObject.getJSONObject("home_team")
                val visitorTeamJSON = jsonObject.getJSONObject("visitor_team")
                dataList.add(
                    SelectedTeamData(
                        homeTeamJSON.getString("full_name").toString(),
                        jsonObject.getInt("home_team_score").toString(),
                        visitorTeamJSON.getString("full_name").toString(),
                        jsonObject.getInt("visitor_team_score").toString()
                    )
                )
            }

            maximumPages = responseJSON.getJSONObject("meta").getInt("total_pages")

        } catch (e: Exception) {
            println(e)
        }

        return dataList
    }

}