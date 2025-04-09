import com.example.myapp.data.Instrument
import com.example.myapp.data.MyResponse
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


private const val BASE_URL = "https://wiutmadcw.uz/api/v1/"

private val json = Json {
 ignoreUnknownKeys = true
 coerceInputValues = true
}

private val retrofit = Retrofit.Builder()
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

interface InstrumentApiService{

    @GET("records/all")
    suspend fun getInstruments(
        @Query("student_id") studentId: String
    ): MyResponse<List<Instrument>>


//    @PUT
//
//
//    @POST("records")
//    suspend fun insertNewInstrument(
//        @Query("student_id") studentId: String,
//        @Body instrumentRequest: InstrumentRequest
//    ): MyResponse
//
//
//
//    @DELETE("records/{record_id}")
//    suspend fun deleteInstrumentById(
//        @Query("student_id") studentId: String,
//        @Path("record_id") instrumentId: Int
//    ): MyResponse
}

object InstrumentApi {
    val retrofitService : InstrumentApiService by lazy {
        retrofit.create(InstrumentApiService::class.java)
    }
}
