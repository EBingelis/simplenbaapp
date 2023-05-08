package com.github.ebingelis.simplenbaapp.model

import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

data class TeamsData(val fullName: String?, val city: String?, val conference: String?, val id: Int?)

class Teams {

    suspend fun getTeamsData(): List<TeamsData> {

        val dataList = mutableListOf<TeamsData>()

        val client = OkHttpClient()

        val urlBuilder = "https://www.balldontlie.io/api/v1/teams".toHttpUrl().newBuilder()

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
                dataList.add(
                    TeamsData(
                        jsonObject.getString("full_name").toString(),
                        jsonObject.getString("city").toString(),
                        jsonObject.getString("conference").toString(),
                        jsonObject.getInt("id")
                    )
                )
            }

        } catch (e: Exception) {
            println(e)
        }


        return dataList
    }

}