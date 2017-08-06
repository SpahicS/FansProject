package models.team_data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Spaja on 06-Aug-17.
 */

public class Team {

    @SerializedName("_links")
    private Links links;

    private String name;
    private String code;
    private String shortName;
    private String squadMarketValue;
    private String crestUrl;
    private int position;
    private String teamName;
    private String crestURI;
    private int playedGames;
    private int points;
    private int wins;
    private int draws;
    private int loses;
    private Home home;
    private Away away;

    public int getPosition() {
        return position;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getCrestURI() {
        return crestURI;
    }

    public int getPlayedGames() {
        return playedGames;
    }

    public int getPoints() {
        return points;
    }

    public int getWins() {
        return wins;
    }

    public int getDraws() {
        return draws;
    }

    public int getLoses() {
        return loses;
    }

    public Home getHome() {
        return home;
    }

    public Away getAway() {
        return away;
    }

    public Links getLinks() {
        return links;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getShortName() {
        return shortName;
    }

    public String getSquadMarketValue() {
        return squadMarketValue;
    }

    public String getCrestUrl() {
        return crestUrl;
    }
}
