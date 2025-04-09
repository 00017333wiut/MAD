package com.example.myapp.data

import InstrumentApi
import android.util.Log
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

private const val STUDENT_ID = "00017333"

class InstrumentRepository {

    //get
    suspend fun getInstruments(): List<Instrument> {
        return try {
            val response = InstrumentApi.retrofitService.getInstruments(STUDENT_ID)
            Log.d("Repository", "Success: ${response.status}, got ${response.data.size} instruments")
            response.data
        } catch (e: Exception) {
            Log.e("Repository", "Error fetching instruments: ${e.message}")
            emptyList()
        }
    }




    //update
//    suspend fun updateInstrument(instrumentId: Int, instrumentRequest: InstrumentRequest): MyResponse {
//        val myResponse = InstrumentApi.retrofitService.updateInstrument(
//            STUDENT_ID,
//            instrumentId,
//            instrumentRequest
//        )
//        Log.d("myResponse", myResponse.message)
//        return myResponse
//    }

//    //insert
//    suspend fun insertNewInstrument(instrumentRequest: InstrumentRequest): MyResponse {
//        val myResponse = InstrumentApi.retrofitService.insertNewInstrument(
//            STUDENT_ID,
//            instrumentRequest
//        )
//        Log.d("myResponse", myResponse.message)
//        return myResponse
//    }
//
//    //delete
//    suspend fun deleteInstrumentById(instrumentId: Int) : MyResponse{
//        val myResponse = InstrumentApi.retrofitService.deleteInstrumentById(
//            STUDENT_ID,
//            instrumentId
//        )
//        Log.d("myResponse", myResponse.message)
//        return myResponse
//    }
}