package models.team_data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Spaja on 06-Aug-17.
 */

public class LeagueTable {

    @SerializedName("_links")
    private Links links;

    private String leagueCaption;
    private int matchDay;
    private ArrayList<Team> standing;

    public Links getLinks() {
        return links;
    }

    public String getLeagueCaption() {
        return leagueCaption;
    }

    public int getMatchDay() {
        return matchDay;
    }

    public ArrayList<Team> getStanding() {
        return standing;
    }
}
