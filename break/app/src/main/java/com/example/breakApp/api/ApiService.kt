
import com.example.breakApp.api.model.CreateInBodyDto
import com.example.breakApp.api.model.InBodyResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("inbody/{userId}")
    suspend fun getInBody(@Path("userId") userId: String): Response<List<InBodyResponse>>

    @POST("inbody")
    suspend fun createInBody(@Body inBody: CreateInBodyDto): Response<InBodyResponse>
}
