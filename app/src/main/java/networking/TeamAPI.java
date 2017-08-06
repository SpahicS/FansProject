package networking;

import models.news.ResponseData;
import models.team_data.Fixtures;
import models.team_data.LeagueTable;
import models.team_data.Team;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by Spaja on 06-Aug-17.
 */

public interface TeamAPI {

    String BASE_URL = "http://api.football-data.org/v1/";

    @GET("teams/109")
    Call<Team> getTeamData();

    @GET("competitions/456/leagueTable")
    Call<LeagueTable> getLeagueTable();

    @GET("teams/109/fixtures")
    Call<Fixtures> getTeamFixtures();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    TeamAPI service = retrofit.create(TeamAPI.class);



}
