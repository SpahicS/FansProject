package models.team_data;

/**
 * Created by Spaja on 06-Aug-17.
 */

public class Links {

    private Self self;
    private Fixtures fixtures;
    private Players players;
    private Competition competition;
    private HomeTeam homeTeam;
    private AwayTeam awayTeam;
    private TeamHref team;

    public Competition getCompetition() {
        return competition;
    }

    public HomeTeam getHomeTeam() {
        return homeTeam;
    }

    public AwayTeam getAwayTeam() {
        return awayTeam;
    }

    public TeamHref getTeam() {
        return team;
    }

    public Self getSelf() {
        return self;
    }

    public Fixtures getFixtures() {
        return fixtures;
    }

    public Players getPlayers() {
        return players;
    }
}
