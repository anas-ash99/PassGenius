package com.example.passgenius.common.instances

import com.example.passgenius.data.apis.LogosApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private  var  userRetrofit:Retrofit? = null
    private  var  logoRetrofit:Retrofit? = null
    private const val baseUrl:String = "http://10.0.2.2:5000/";
    private const val baseUrlLogo:String = "https://www.klazify.com/";


    fun logoApi(): LogosApi {
        if (logoRetrofit == null){
            logoRetrofit = Retrofit.Builder()
                .baseUrl(baseUrlLogo)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        return logoRetrofit!!.create(LogosApi::class.java)
    }



}