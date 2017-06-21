package models;

import java.util.HashMap;

/**
 * Created by Spaja on 10-Jun-17.
 */

public class NewsItem {

    private String id;
    private HashMap<String, Boolean> likes;
    private HashMap<String, Boolean> dislikes;

    public NewsItem() {
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

    public void addLike(String userId) {

        if (likes == null) {
            likes = new HashMap<>();
        }

        likes.put(userId, true);

    }

    public void removeLike(String userId) {

        if (likes != null) {
            likes.remove(userId);
        }
    }
}
