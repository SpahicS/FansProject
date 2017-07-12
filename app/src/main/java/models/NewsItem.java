package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Spaja on 10-Jun-17.
 */

public class NewsItem {

    private String id;
    private HashMap<String, Boolean> likes = new HashMap<>();
    private HashMap<String, Boolean> dislikes = new HashMap<>();
    private HashMap<String, Comment> comments = new HashMap<>();
    private String link;
    private String title;
    private String pubDate;
    private String imageUrl;

    public NewsItem() {
    }

    public static NewsItem getInstance(Item item) {

        NewsItem newsItem = new NewsItem();
        newsItem.setId(item.getHashCode());
        newsItem.setLink(item.getLink());
        newsItem.setImageUrl(item.getImageUrl());
        newsItem.setPubDate(item.getPubDate());
        newsItem.setTitle(item.getTitle());
        newsItem.setLink(item.getLink());

        return newsItem;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public HashMap<String, Boolean> getLikes() {
        return likes;
    }

    public void setLikes(HashMap<String, Boolean> likes) {
        this.likes = likes;
    }

    public HashMap<String, Boolean> getDislikes() {
        return dislikes;
    }

    public void setDislikes(HashMap<String, Boolean> dislikes) {
        this.dislikes = dislikes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Comment> getComments() {

        ArrayList<Comment> arrayComments = new ArrayList<>();

        for (Object o : comments.entrySet()) {

            Map.Entry pair = (Map.Entry) o;

            Comment comment = (Comment) pair.getValue();
            comment.setId((String) pair.getKey());

            arrayComments.add(comment);
        }

        return arrayComments;
    }

    public HashMap<String, Comment> setComments(ArrayList<Comment> comments) {

        for (Comment comment : comments) {
            this.comments.put(comment.getId(), comment);
        }

        return this.comments;
    }
}
