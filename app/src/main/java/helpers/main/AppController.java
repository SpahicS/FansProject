package helpers.main;

import android.app.Application;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by unexpected_err on 20/05/2017.
 */

public class AppController extends Application {

    public static DatabaseReference mDatabase;
    public static FirebaseUser mUser;

    @Override public void onCreate() {
        super.onCreate();
    }

    public static void initializeFirebaseDatabase() {

        mDatabase = FirebaseDatabase.getInstance()
            .getReference().child(AppConfig.TEAM);

    }

    public static DatabaseReference getFirebaseDatabase() {

        if (mDatabase == null)
            mDatabase = FirebaseDatabase.getInstance()
                .getReference().child(AppConfig.TEAM);

        return mDatabase;

    }

    public static FirebaseUser getUser() {
        return mUser;
    }

    public static void setUser(FirebaseUser mUser) {
        AppController.mUser = mUser;
    }
}
