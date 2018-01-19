package models.news;

import com.google.firebase.database.IgnoreExtraProperties;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by unexpected_err on 20/05/2017.
 */

@IgnoreExtraProperties
public class FeedItem {

    private String id;
    private String username;
    private String userId;
    private String message;
    private String imageUrl;
    private String videoUrl;
    private String date;
    private String avatar;

    private Map<String, ArticleItem> article = new HashMap<>();

    private HashMap<String, Boolean> likes = new HashMap<>();
    private HashMap<String, Boolean> dislikes = new HashMap<>();
    private HashMap<String, Comment> comments = new HashMap<>();

    public FeedItem() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public HashMap<String, Boolean> getLikes() {
        return likes;
    }

    public HashMap<String, Boolean> getDislikes() {
        return dislikes;
    }

    public ArticleItem getArticle() {
        return article.get("article");
    }

    public void setArticle(ArticleItem articleItem) {
        this.article.put("article", articleItem);
    }

    public ArrayList<Comment> getComments() {

        ArrayList<Comment> arrayComments = new ArrayList<>();

        Iterator it = comments.entrySet().iterator();

        while (it.hasNext()) {

            Map.Entry pair = (Map.Entry) it.next();

            Comment comment = (Comment) pair.getValue();
            comment.setId((String) pair.getKey());

            arrayComments.add(comment);
        }

        return arrayComments;
    }

    public int getCommentsCount() {
        return comments.size();
    }

}
