package models.team_data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Spaja on 06-Aug-17.
 */

public class TeamInfo {

    @SerializedName("_links")
    private Links links;

    private String name;
    private String code;
    private String shortName;
    private String squadMarketValue;
    private String crestUrl;

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
