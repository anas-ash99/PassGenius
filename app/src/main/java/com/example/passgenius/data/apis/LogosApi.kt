package com.example.passgenius.data.apis

import com.example.passgenius.domain.models.logosApi.ErrorMessage
import com.example.passgenius.domain.models.logosApi.LogoApiModel
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface LogosApi {

    @POST("/api/categorize")
    fun getCompanyLogo(@Query("url") url:String, @Header("Authorization") api_key:String ) : Call<LogoApiModel>


}