package models.news;

import com.google.firebase.database.IgnoreExtraProperties;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Spaja on 10-Jun-17.
 */

@IgnoreExtraProperties
public class ArticleItem implements Serializable {

    private String id;
    private HashMap<String, Boolean> likes = new HashMap<>();
    private HashMap<String, Boolean> dislikes = new HashMap<>();
    private HashMap<String, Comment> comments = new HashMap<>();
    private String link;
    private String url;
    private String title;
    private String pubDate;
    private String imageUrl;

    public ArticleItem() {}

    public ArticleItem(Item item) {

        id = String.valueOf(item.getTitle().hashCode());
        link = item.getLink();
        imageUrl = item.getImageUrl();
        pubDate = item.getPubDate();
        title = item.getTitle();

    }

    public String getLink() {
        return link;
    }

    public String getArticleUrl() {
        return url != null ? url : getLink();
    }

    public void setArticleUrl(String url) {
        this.url = url;
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

        Iterator it = comments.entrySet().iterator();

        while (it.hasNext()) {

            Map.Entry pair = (Map.Entry) it.next();

            Comment comment = (Comment) pair.getValue();
            comment.setId((String) pair.getKey());

            arrayComments.add(comment);
        }

        return arrayComments;
    }

    public HashMap<String, Comment> getCommentsMap() {

        return this.comments;
    }

    public void setCommentsMap(HashMap<String, Comment> comments) {

        this.comments = comments;
    }

    public int getCommentsCount() {
        return comments.size();
    }
}
