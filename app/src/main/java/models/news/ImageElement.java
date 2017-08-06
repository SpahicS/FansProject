package models.news;

import org.simpleframework.xml.Element;

/**
 * Created by Spaja on 26-Apr-17.
 */

class ImageElement {

    @Element (name = "title")
    private String imageTitle;

    @Element (name = "link")
    private String imageLink;

    @Element (name = "url")
    private String imageUrl;

    public String getImageTitle() {
        return imageTitle;
    }

    public String getImageLink() {
        return imageLink;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
