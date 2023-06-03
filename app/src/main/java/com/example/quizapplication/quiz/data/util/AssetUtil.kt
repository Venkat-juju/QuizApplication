package com.example.quizapplication.quiz.data.util

import android.content.Context
import org.json.JSONObject
import java.io.InputStream

internal fun Context.readJSONFromAsset(vararg fileName: String?): ArrayList<JSONObject>? {
    val resultJson = ArrayList<JSONObject>()
    for (i in fileName.indices) {
        this.readJSONFromAsset(fileName[i])?.let {
            resultJson.add(it)
        }
    }
    return resultJson.takeIf { it.isNotEmpty() }
}

private fun Context.readJSONFromAsset(fileName: String?): JSONObject? {
    return try {
        val inputStream: InputStream = assets.open(fileName?:return null)
        JSONObject(inputStream.bufferedReader().use{it.readText()})
    } catch (ex: Exception) {
        ex.printStackTrace()
        null
    }
}