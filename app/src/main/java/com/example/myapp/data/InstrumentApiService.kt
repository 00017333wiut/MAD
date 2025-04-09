import com.example.myapp.data.Instrument
import com.example.myapp.data.MyResponse
import com.example.myapp.data.PostResponse
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Retrofit
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.http.*
import retrofit2.Response



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

    //get all
    @GET("records/all")
    suspend fun getInstruments(
        @Query("student_id") studentId: String
    ): MyResponse<List<Instrument>>


    //add
    @POST("records")
    suspend fun insertNewInstrument(
        @Query("student_id") studentId: String,
        @Body instrumentRequest: Instrument
    ):PostResponse


    //update
    @PUT("records/{record_id}")
    suspend fun updateInstrument(
        @Path("record_id") instrumentId: Int?,
        @Query("student_id") studentId: String,
        @Body instrumentRequest: Instrument
    ):PostResponse


    @DELETE("records/{record_id}")
    suspend fun deleteInstrumentById(
        @Path("record_id") instrumentId: Int?,
        @Query("student_id") studentId: String
    ):PostResponse

}

object InstrumentApi {
    val retrofitService: InstrumentApiService by lazy {
        retrofit.create(InstrumentApiService::class.java)
    }
}

