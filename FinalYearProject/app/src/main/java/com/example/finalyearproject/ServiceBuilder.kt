package com.example.finalyearproject

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {
        private val client = OkHttpClient.Builder().build()
        private val retrofit = Retrofit.Builder()
            .baseUrl("http://172.17.87.33:8000") //172.17.80.1
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        fun<T> buildService(service: Class<T>): T {
            return retrofit.create(service)
        }
}
