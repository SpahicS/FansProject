package networking;


import models.ResponseData;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Spaja on 26-Apr-17.
 */

public interface NewsAPI {

    String BASE_URL = "https://news.google.com/";

    @GET("news")
    Call<ResponseData> getNewsData(@Query("q") String query, @Query("num") int num,
        @Query("output") String output, @Query("ned") String edition);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build();

    NewsAPI service = retrofit.create(NewsAPI.class);

}
