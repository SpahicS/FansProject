package models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by unexpected_err on 16/08/2017.
 */

@IgnoreExtraProperties
public class ArticleData {

    private String content;

    public String getContent() {
        return content;
    }
}
