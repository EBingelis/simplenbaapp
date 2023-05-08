package com.github.ebingelis.simplenbaapp.model

import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

data class PlayersData(val firstName: String?, val lastName: String?, val teamFullName: String?, val teamId: Int?)

class Players {

    var maximumPages = 0

    suspend fun getPlayersData(string:String, page: Int = 1): List<PlayersData> {

        val dataList = mutableListOf<PlayersData>()

        val client = OkHttpClient()

        val urlBuilder = "https://www.balldontlie.io/api/v1/players".toHttpUrl().newBuilder()
        urlBuilder.addQueryParameter("page", "$page")

        if(string != ""){
            urlBuilder.addQueryParameter("search", string)
        }

        val url = urlBuilder.build()

        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        val response = client.newCall(request).execute()

        val responseBody = response.body?.string()
        println(responseBody)

        if (!response.isSuccessful) {
            throw Exception("API request failed with code ${response.code}")
        }

        try {
            val responseJSON = JSONObject(responseBody.toString())

            val dataArray = responseJSON.getJSONArray("data")

            for (i in 0 until dataArray.length()) {
                val jsonObject = dataArray.getJSONObject(i)
                val teamObject = jsonObject.getJSONObject("team")
                dataList.add(
                    PlayersData(
                        jsonObject.getString("first_name").toString(),
                        jsonObject.getString("last_name").toString(),
                        teamObject.getString("full_name").toString(),
                        teamObject.getInt("id")
                    )
                )
            }

            maximumPages = responseJSON.getJSONObject("meta").getInt("total_count")

        } catch (e: Exception) {
            println(e)
        }


        return dataList
    }

}