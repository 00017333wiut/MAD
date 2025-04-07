import com.example.myapp.data.Instrument
import com.example.myapp.data.InstrumentRequest
import com.example.myapp.data.MyResponse
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


private const val BASE_URL = "https://wiutmadcw.uz/api/v1/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

interface AppApiService{

    @POST("records")
    suspend fun insertNewInstrument(
        @Query("student_id") studentId: String,
        @Body instrumentRequest: InstrumentRequest
    ): MyResponse


    @GET ("00017333")
    suspend fun getInstruments() : List<Instrument>

    @DELETE("records/{record_id}")
    suspend fun deleteInstrumentById(
        @Path("record_id") instrumentId: Int,
        @Query("student_id") studentId: String
    ): MyResponse
}

object AppApi {
    val retrofitService : AppApiService by lazy {
        retrofit.create(AppApiService::class.java)
    }
}
