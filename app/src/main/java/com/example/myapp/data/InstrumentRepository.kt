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
            Log.d(
                "Repository",
                "Success: ${response.status}, got ${response.data.size} instruments"
            )
            response.data
        } catch (e: Exception) {
            Log.e("Repository", "Error fetching instruments: ${e.message}")
            emptyList()
        }
    }

    //insert
    suspend fun insertNewInstrument(instrumentRequest: Instrument): PostResponse {
        return try {
            val myResponse = InstrumentApi.retrofitService.insertNewInstrument(
                STUDENT_ID,
                instrumentRequest
            )
            Log.d("Repository", "Success: ${myResponse.status}, message: ${myResponse.message}")
            myResponse
        } catch (e: Exception) {
            Log.e("Repository", "Error adding instrument: ${e.message}")
            throw e
        }
    }


    //update
    suspend fun updateInstrument(instrumentId: Int?, instrumentRequest: Instrument): PostResponse {
        val response = InstrumentApi.retrofitService.updateInstrument(
            instrumentId,
            STUDENT_ID,
            instrumentRequest
        )
        Log.d("myResponse", response.message)
        return PostResponse(
            code = response.code,
            status = response.status,
            message = response.message
        )
    }


    //delete
    suspend fun deleteInstrumentById(instrumentId: Int?): PostResponse {
        return try {
            val response = InstrumentApi.retrofitService.deleteInstrumentById(
                instrumentId,
                STUDENT_ID
            )
            Log.d("myResponse", response.message)

            PostResponse(
                code = response.code,
                status = response.status,
                message = response.message
            )
        } catch (e: Exception) {
            Log.e("Repository", "Error deleting: ${e.message}")
            throw e
        }
    }
}