package models.team_data;

/**
 * Created by Spaja on 07-Aug-17.
 */

public class TeamStanding {

    private int position;
    private String teamName;
    private String crestURI;
    private int playedGames;
    private int points;
    private int wins;
    private int draws;
    private int losses;
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

    public int getLosses() {
        return losses;
    }

    public Home getHome() {
        return home;
    }

    public Away getAway() {
        return away;
    }
}
