package persistance;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import helpers.main.AppController;
import models.NewsItem;

/**
 * Created by Spaja on 22-Jun-17.
 */

public class NewsItemDataService {

    private final DatabaseReference mNewsRef;


    public NewsItemDataService() {
        mNewsRef = AppController.getFirebaseDatabase().child("news");
    }

    public void saveLike(String id, String uid) {
        Map<String, Object> map = new HashMap<>(1);
        map.put(uid, true);
        mNewsRef.child(id).child("likes").updateChildren(map);
    }

    public void removeLike(String id, String uid) {
        mNewsRef.child(id).child("likes").child(uid).removeValue();
    }

    public void saveDislike(String id, String uid) {
        Map<String, Object> map = new HashMap<>(1);
        map.put(uid, true);
        mNewsRef.child(id).child("dislikes").updateChildren(map);
    }

    public void removeDislike(String id, String uid) {
        mNewsRef.child(id).child("dislikes").child(uid).removeValue();
    }
}