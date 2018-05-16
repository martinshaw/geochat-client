package co.martinshaw.apps.android.geochat;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by martin on 13/05/2018.
 */

public interface GeochatAPIService {
//    @GET("users/{user}/repos")
//    Call<List<Repo>> listRepos(@Path("user") String user);

//    @GET("status")
//    Call<GeochatAPIResponse> getStatus(@Header("Geochat-Session-Key") String session_key);

    @GET("api/v0.1/users")
    Call<GeochatAPIResponse<User>> getAllUsers(@Header("Geochat-Session-Key") String session_key);

    @GET("api/v0.1/")
    Call<GeochatAPIResponse<UserSession>> signInUserAccount(@Query("email_address") String email_address, @Query("password") String password);
}