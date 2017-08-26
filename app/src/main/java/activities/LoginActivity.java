package activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.wang.avi.AVLoadingIndicatorView;

import digitalbath.fansproject.R;
import helpers.main.AppController;
import helpers.main.AppHelper;

/**
 * Created by unexpected_err on 20/05/2017.
 */

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 99;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeMainView();

        initializeFirebaseAuthentication();

        initializeStatusBar();
    }

    private void initializeMainView() {

        ((TextView) findViewById(R.id.app_name))
                .setText(getResources().getString(R.string.app_name));
    }

    private void initializeFirebaseAuthentication() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        final GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        int sd = 0;
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        Button signInButton = (Button) findViewById(R.id.sign_in_button);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);

                LinearLayout loginCont = (LinearLayout) findViewById(R.id.login_cont);
                loginCont.setVisibility(View.INVISIBLE);
                loginCont.startAnimation(AppHelper.getAnimationDown(LoginActivity.this));

                AVLoadingIndicatorView progressBar = (AVLoadingIndicatorView) findViewById(R.id.progress_bar);
                progressBar.setVisibility(View.VISIBLE);
                progressBar.startAnimation(AppHelper.getAnimationUp(LoginActivity.this));
            }
        });

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser mUser = firebaseAuth.getCurrentUser();

                if (mUser != null) {

                    AppController.setUser(mUser);
                    AppController.initializeFirebaseDatabase(LoginActivity.this);

                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);

                } else {

                    LinearLayout loginCont = (LinearLayout) findViewById(R.id.login_cont);
                    loginCont.setVisibility(View.VISIBLE);
                    loginCont.startAnimation(AppHelper.getAnimationUp(LoginActivity.this));

                    AVLoadingIndicatorView progressBar = (AVLoadingIndicatorView) findViewById(R.id.progress_bar);
                    progressBar.setVisibility(View.INVISIBLE);
                    progressBar.startAnimation(AppHelper.getAnimationDown(LoginActivity.this));

                }
            }
        };
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {

                            LinearLayout loginCont = (LinearLayout) findViewById(R.id.login_cont);
                            loginCont.setVisibility(View.VISIBLE);
                            loginCont.startAnimation(AppHelper.getAnimationDown(LoginActivity.this));

                            AVLoadingIndicatorView progressBar = (AVLoadingIndicatorView) findViewById(R.id.progress_bar);
                            progressBar.setVisibility(View.INVISIBLE);
                            progressBar.startAnimation(AppHelper.getAnimationUp(LoginActivity.this));

                            AppHelper.showToast(LoginActivity.this, "Something went wrong");
                        }

                    }
                });
    }

    private void initializeStatusBar() {

        getWindow().getDecorView()
                .setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;

        winParams.flags &= ~bits;

        win.setAttributes(winParams);

        getWindow().setStatusBarColor(Color.parseColor("#1A000000"));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (result.isSuccess()) {

                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);

            } else {

            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
