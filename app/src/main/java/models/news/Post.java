package models.news;

/**
 * Created by Spaja on 14-Aug-17.
 */

public class Post {

    private String userId;
    private String postId;
    private String userName;

    public Post() {
    }

    public Post(String userId, String postId, String userName) {
        this.userId = userId;
        this.postId = postId;
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
