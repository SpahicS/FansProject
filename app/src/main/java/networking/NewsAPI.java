package networking;


import models.news.ResponseData;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Spaja on 26-Apr-17.
 */

public interface NewsAPI {

    String BASE_URL = "https://news.google.com/";

    @GET("news/rss/search/section/q/{q1}/{q2}")
    Call<ResponseData> getNewsData(@Path("q1") String query1, @Path("q2") String query2, @Query("num") int num,
                                   @Query("hl") String hl, @Query("gl") String gl, @Query("ned") String edition);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build();

    NewsAPI service = retrofit.create(NewsAPI.class);

}
