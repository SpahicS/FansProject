package helpers.main;

import digitalbath.fansproject.BuildConfig;

/**
 * Created by unexpected_err on 27/07/2017.
 */

public class AppConfig {

    private AppConfig() {
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
