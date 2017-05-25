package models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * Created by Spaja on 26-Apr-17.
 */

public class Channel {

    @Element(required = false)
    private String pubDate;

    @Element(required = false)
    private String description;

    @Element(required = false)
    private String link;

    @Element(required = false)
    private String lastBuildDate;

    @ElementList(inline = true)
    private List<Item> item;

    @Element(required = false)
    private String generator;

    @Element(required = false)
    private String language;

    @Element(required = false)
    private String copyright;

    @Element(required = false)
    private String webMaster;

    @Element(name = "title")
    private String title;

    @Element(name = "image")
    private ImageElement image;


    public String getTitle() {
        return title;
    }

    public ImageElement getImage() {
        return image;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    public String getLastBuildDate() {
        return lastBuildDate;
    }

    public List<Item> getNewsList() {
        return item;
    }

    public String getGenerator() {
        return generator;
    }

    public String getLanguage() {
        return language;
    }

    public String getCopyright() {
        return copyright;
    }

    public String getWebMaster() {
        return webMaster;
    }
}
