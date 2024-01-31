package com.example.cypherassignment.network

import com.example.cypherassignment.model.RegisterDetails
import com.example.cypherassignment.model.ResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface Api {
    @POST("/register")
    suspend fun register(@Body registerDetails: RegisterDetails): Response<ResponseModel>
}