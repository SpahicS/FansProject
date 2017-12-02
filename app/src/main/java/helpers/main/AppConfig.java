package helpers.main;

import digitalbath.fansproject.BuildConfig;

/**
 * Created by unexpected_err on 27/07/2017.
 */

public enum AppConfig {

    JUVENTUS("Juventus", "Juventus", 109, 456),
    INTERMILAN("Inter Milan", "InterMilan", 108, 456),
    MILAN("Ac Milan", "AcMilan", 98, 456),

    ARSENAL("Arsenal", "Arsenal", 57, 445),
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

    private static AppConfig getTeamConfig() {

        switch (BuildConfig.APP_CONFIG) {

            case "Juventus":
                return JUVENTUS;
            case "Barcelona":
                return BARCELONA;
            case "InterMilan":
                return INTERMILAN;
            case "Milan":
                return MILAN;
            case "Arsenal":
                return ARSENAL;
            case "Liverpool":
                return LIVERPOOL;
            case "ManUtd":
                return MANUTD;
            case "RealMadrid":
                return REALMADRID;
        }

        return JUVENTUS;
    }

    public static String getNewsQuery() {

        return getTeamConfig().mNewsQuery;
    }

    public static String getDatabaseQuery() {

        return getTeamConfig().mDatabaseQuery;

    }

    public static int getTeamId() {

        return getTeamConfig().mTeamId;

    }

    public static int getTeamLeagueId() {

        return getTeamConfig().mTeamLeagueId;

    }

    public static String getTeamName(int teamId) {
        switch (teamId) {
            case 109:
                return "Juventus";
            case 108:
                return "Inter";
            case 98:
                return "Milan";
            case 57:
                return "Arsenal";
            case 64:
                return "Liverpool";
            case 66:
                return "Manchester";
            case 81:
                return "Barcelona";
            case 86:
                return "Real";
            default:
                return "Juventus";
        }
    }

    public static String getLeagueName(int leagueId) {
        switch (leagueId) {
            case 456:
                return "Serie A";
            case 445:
                return "Premiere League";
            case 457:
                return "Primera Division";
            default:
                return "Unknown League";
        }
    }
}
