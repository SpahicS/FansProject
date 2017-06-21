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
    private String guid;

    @Element(required = false)
    private String pubDate;

    @Element(required = false)
    private String description;

    private String imageUrl;

    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getIsPermaLink() {
        return isPermaLink;
    }

    public String getLink() {
        return link;
    }

    public String getGuid() {
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
