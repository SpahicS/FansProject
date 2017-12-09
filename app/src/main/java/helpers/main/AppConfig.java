package helpers.main;

import digitalbath.fansproject.BuildConfig;

/**
 * Created by unexpected_err on 27/07/2017.
 */

public enum AppConfig {

    JUVENTUS("Juventus", "Juventus", 109, 456),
    INTERMILAN("Inter Milan", "InterMilan", 108, 456),
    MILAN("Ac Milan", "AcMilan", 98, 456),

    LIVERPOOL("Liverpool", "Liverpool", 64, 445),
    MANUTD("Manchester United", "ManchesterUnited", 66, 445),

    BARCELONA("Barcelona", "Barcelona", 81, 457),
    REALMADRID("Real Madrid", "RealMadrid", 86, 457);

    private String mNewsQuery;
    private String mDatabaseQuery;
    private int mTeamId;
    private int mTeamLeagueId;

    AppConfig(String newsQuery, String databaseQuery, int teamId, int teamLeagueId) {

        mNewsQuery = newsQuery;
        mDatabaseQuery = databaseQuery;
        mTeamId = teamId;
        mTeamLeagueId = teamLeagueId;
    }

    public static String getNewsQuery() {

        return BuildConfig.NEWS_QUERY;
    }

    public static String getDatabaseQuery() {

        return BuildConfig.DB_QUERY;

    }

    public static int getTeamId() {

        return BuildConfig.TEAM_ID;

    }

    public static int getTeamLeagueId() {

        return BuildConfig.LEAGUE_ID;

    }

    public static String getTeamName() {

        return BuildConfig.NEWS_QUERY;

    }
}
