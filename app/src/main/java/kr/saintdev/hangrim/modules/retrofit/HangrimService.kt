package kr.saintdev.hangrim.modules.retrofit

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface HangrimService {
    @GET("words/random/{category}/{except}")
    fun requestRandomWord(@Path("category") category: String, @Path("except") exceptArr: String) : Call<HangrimWord>

    /**
     * @Date 01.01 2019
     * Request all word category
     */
    @GET("words/all/{category}")
    fun requestAllWords(@Path("category") category: String) : Call<List<HangrimWord>>
}

object Retrofit {
    private var retrofit: Retrofit? = null

    fun getRetrofit() : Retrofit {
        if(retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(HTTPConst.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        return retrofit!!
    }
}