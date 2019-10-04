package it.simonerenzo.docebotest.client.service

import io.reactivex.Maybe
import it.simonerenzo.docebotest.client.model.DoceboModels
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface DoceboService {

    @GET("/catalog")
    fun getCatalog(@Query("type") courseType: String,
                   @Query("search_text") itemName: String) : Maybe<DoceboModels.APIResponse>

    companion object {
        fun create() : DoceboService {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://demomobile.docebosaas.com/learn/v1")
                .build()

            return retrofit.create(DoceboService::class.java)
        }
    }

}