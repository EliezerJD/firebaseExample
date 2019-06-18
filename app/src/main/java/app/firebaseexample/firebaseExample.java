package app.firebaseexample;

import com.google.firebase.database.FirebaseDatabase;

public class firebaseExample extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
