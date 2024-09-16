package com.example.terminal.data

import retrofit2.http.GET

interface ApiService {

    @GET("v2/aggs/ticker/AAPL/range/1/hour/2023-09-16/2024-09-16?adjusted=true&sort=asc&limit=50000&apiKey=yQI2Yyniqe_xaZppzsoPr7nlPhPqqVmK")
    suspend fun loadBars(): Result
}