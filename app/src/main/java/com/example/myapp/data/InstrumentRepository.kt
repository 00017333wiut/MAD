package com.example.myapp.data

import AppApi
import android.util.Log
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

class InstrumentRepository {


    suspend fun deleteInstrumentById(instrumentId: Int) : MyResponse{
        val myResponse = AppApi.retrofitService.deleteInstrumentById(
            instrumentId,
            "00017333"
        )

        Log.d("myResponse", myResponse.message)
        return myResponse
    }
}