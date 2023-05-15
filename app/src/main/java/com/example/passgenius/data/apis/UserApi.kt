package com.example.passgenius.data.apis

import com.example.passgenius.domain.models.ResModel
import retrofit2.Call
import retrofit2.http.GET

interface UserApi {

    @GET("/")
    suspend fun authUser(): ResModel

}