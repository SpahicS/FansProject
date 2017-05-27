package helpers.main;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by Spaja on 25-Apr-17.
 */

public class GetMetaDataFromUrl extends AsyncTask<String, Void, Void> {

    private Activity mActivity;
    private Elements description, image;
    private ImageView newsImage;

    public GetMetaDataFromUrl(Activity activity, ImageView newsImage) {

        this.mActivity = activity;
        this.newsImage = newsImage;

    }

    @Override
    public Void doInBackground(String... params) {

        try {

            Document document = Jsoup.connect(params[0]).get();
//            description = document.select("meta[name=description]");
            image = document.select("meta[property=og:image]");
//            author = document.select("meta[name=author]");


        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onPostExecute(Void aVoid) {

        if (image != null && !mActivity.isDestroyed()) {
            Glide.with(mActivity)
                    .load(image.attr("content"))
                    .into(newsImage);
        } else if (mActivity.isDestroyed()) {
            Glide.clear(newsImage);
        }
    }
}
