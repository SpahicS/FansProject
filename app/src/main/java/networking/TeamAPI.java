package networking;

import models.team_data.Fixtures;
import models.team_data.LeagueTable;
import models.team_data.TeamInfo;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

/**
 * Created by Spaja on 06-Aug-17.
 */

public interface TeamAPI {

    String BASE_URL = "http://api.football-data.org/v1/";

    @GET("teams/{id}")
    Call<TeamInfo> getTeamData(@Path("id") int id, @Header("X-Auth-Token") String apiKey);

    @GET("competitions/456/leagueTable")
    Call<LeagueTable> getLeagueTable(@Header("X-Auth-Token") String apiKey);

    @GET("teams/109/fixtures")
    Call<Fixtures> getTeamFixtures(@Header("X-Auth-Token") String apiKey);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    TeamAPI service = retrofit.create(TeamAPI.class);



}
