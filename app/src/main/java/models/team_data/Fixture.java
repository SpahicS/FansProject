package models.team_data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Spaja on 06-Aug-17.
 */

public class Fixture {

    @SerializedName("_links")
    private Links links;

    private String date;
    private String status;
    private int matchDay;
    private String homeTeamName;
    private String awayTeamName;
    private Result result;
    //private String odds;

//    public String getOdds() {
//        return odds;
//    }

    public Links getLinks() {
        return links;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public int getMatchDay() {
        return matchDay;
    }

    public String getHomeTeamName() {
        return homeTeamName;
    }

    public String getAwayTeamName() {
        return awayTeamName;
    }

    public Result getResult() {
        return result;
    }
}
