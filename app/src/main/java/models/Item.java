package models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

/**
 * Created by Spaja on 26-Apr-17.
 */

public class Item {

    @Element(required = false)
    private String title;

    @Attribute(required = false)
    private String isPermaLink;

    @Element(required = false)
    private String link;

    @Element(required = false)
    private Guid guid;

    @Element(required = false)
    private String pubDate;

    @Element(required = false)
    private String description;

    private String imageUrl;

    public String getIsPermaLink() {
        return isPermaLink;
    }

    public String getLink() {
        return link;
    }

    public Guid getGuid() {
        return guid;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
