package networking;

import models.ArticleData;
import models.team_data.Fixtures;
import models.team_data.LeagueTable;
import models.team_data.TeamInfo;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Spaja on 06-Aug-17.
 */

public interface ArticleAPI {

    String BASE_URL = "https://mercury.postlight.com/";

    @GET("parser")
    Call<ArticleData> getArticleData(@Query("url") String url, @Header("x-api-key") String apiKey);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    ArticleAPI service = retrofit.create(ArticleAPI.class);

}
