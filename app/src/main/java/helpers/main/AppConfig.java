package helpers.main;

import android.content.Context;
import digitalbath.fansproject.R;

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

    private static AppConfig getTeamConfig(Context context) {

        switch (context.getResources().getString(R.string.app_name)) {

            case "Juve Fans":
                return JUVENTUS;
            case "Barca Fans":
                return BARCELONA;
            case "Inter Milan Fans":
                return INTERMILAN;
            case "Milan Fans":
                return MILAN;
            case "Arsenal Fans":
                return ARSENAL;
            case "Liverpool Fans":
                return LIVERPOOL;
            case "Manchester United Fans":
                return MANUTD;
            case "Real Madrid Fans":
                return REALMADRID;
        }

        return JUVENTUS;
    }

    public static String getNewsQuery(Context context) {

        return getTeamConfig(context).mNewsQuery;
    }

    public static String getDatabaseQuery(Context context) {

        return getTeamConfig(context).mDatabaseQuery;

    }

    public static int getTeamId(Context context) {

        return getTeamConfig(context).mTeamId;

    }

    public static int getTeamLeagueId(Context context) {

        return getTeamConfig(context).mTeamLeagueId;

    }
}
