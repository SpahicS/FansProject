package models.team_data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Spaja on 06-Aug-17.
 */

public class Fixtures extends Href {

    @SerializedName("_links")
    private Links links;

    private int count;
    private ArrayList<Fixture> fixtures;

    public Links getLinks() {
        return links;
    }

    public int getCount() {
        return count;
    }

    public ArrayList<Fixture> getFixtures() {
        return fixtures;
    }
}
