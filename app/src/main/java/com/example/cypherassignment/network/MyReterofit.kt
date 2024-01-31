package com.example.cypherassignment.network

import android.net.Uri
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.UnsupportedEncodingException


object RetrofitBuilder {
    var retrofit: Retrofit? = null
        get() {
            val gson = GsonBuilder().setLenient().create()
            if (field == null) {
                field = Retrofit.Builder()
                    .baseUrl(getBaseAddress().toString()+"/")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
            }
            return field
        }

    private fun getBaseAddress(): Uri.Builder {
        var builder = Uri.Builder()

        builder.scheme("https")
            .authority("cloud.cypherx.in")
        try {
            builder = Uri.parse(builder.toString()).buildUpon()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return builder
    }
}