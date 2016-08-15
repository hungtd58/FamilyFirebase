package uet.tdh.familylocation;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import uet.tdh.familylocation.Model.User;

public class MainActivity extends AppCompatActivity {

    public static String TAG = "Family Firebase";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;

    private EditText mEditEmail, mEditPassword, mEditName;
    private Button mBtnSignUp, mBtnSignIn;
    //private Button mBtnFacebook, mBtnGoogle;
    private TextView mTvChangeState;

    private ProgressDialog mProgress;

    //private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getIdAndSetUp();

        mBtnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkToSignIn();
            }
        });

        mBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkToSignUp();
            }
        });

        mTvChangeState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeState();
            }
        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void getIdAndSetUp() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mEditEmail = (EditText) findViewById(R.id.et_email);
        mEditPassword = (EditText) findViewById(R.id.et_password);
        mEditName = (EditText) findViewById(R.id.et_name);
        mBtnSignIn = (Button) findViewById(R.id.btn_sign_in);
        mBtnSignUp = (Button) findViewById(R.id.btn_sign_up);
        mProgress = new ProgressDialog(this);
        mProgress.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        mBtnFacebook = (Button) findViewById(R.id.btn_facebook);
//        mBtnGoogle = (Button) findViewById(R.id.btn_google);
        mTvChangeState = (TextView) findViewById(R.id.tv_register);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    private void checkToSignIn() {
        if ( mEditEmail.getText().toString().equals("") ) {
            Toast.makeText(MainActivity.this, "Bạn chưa nhập email", Toast.LENGTH_SHORT).show();
            mEditEmail.requestFocus();
        } else if ( mEditPassword.getText().toString().equals("") ) {
            Toast.makeText(MainActivity.this, "Bạn chưa nhập mật khẩu", Toast.LENGTH_SHORT).show();
            mEditPassword.requestFocus();
        } else {
            signIn();
        }
    }

    private void signIn() {
        String email = mEditEmail.getText().toString();
        String password = mEditPassword.getText().toString();

        mProgress.setCancelable(false);
        mProgress.setMessage("Vui lòng chờ ...");
        mProgress.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            // dang nhap loi
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(MainActivity.this,
                                    "Thông tin tài khoản hoặc mật khẩu không chính xác",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // dang nhap thanh cong
                            goToHome();
                        }

                        mProgress.dismiss();
                        // ...
                    }
                });
    }



    private void checkToSignUp() {
        if ( mEditEmail.getText().toString().equals("") ) {
            Toast.makeText(MainActivity.this, "Bạn chưa nhập email", Toast.LENGTH_SHORT).show();
            mEditEmail.requestFocus();
        } else if ( mEditName.getText().toString().equals("") ) {
            Toast.makeText(MainActivity.this, "Bạn chưa nhập tên", Toast.LENGTH_SHORT).show();
            mEditName.requestFocus();
        } else if ( mEditPassword.getText().toString().equals("") ) {
            Toast.makeText(MainActivity.this, "Bạn chưa nhập mật khẩu", Toast.LENGTH_SHORT).show();
            mEditPassword.requestFocus();
        } else {
            signUp();
        }
    }

    private void signUp() {
        final String email = mEditEmail.getText().toString();
        final String name = mEditName.getText().toString();
        final String password = mEditPassword.getText().toString();

        mProgress.setCancelable(false);
        mProgress.setMessage("Vui lòng chờ ...");
        mProgress.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.

                        if (!task.isSuccessful()) {
                            // loi dang ki
                            Toast.makeText(MainActivity.this,
                                    "Có lỗi xảy, vui lòng thử lại hoặc nhập một email khác",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // dang ki thanh cong
                            DatabaseReference myRef = mDatabase.child("user").child(task.getResult().getUser().getUid());

                            User newUser = new User(email, name);

                            myRef.setValue(newUser);
                            goToHome();
                        }

                        mProgress.dismiss();
                    }
                });
    }

    private void goToHome() {
        Intent goToHome = new Intent(MainActivity.this, Main2Activity.class);
        startActivity(goToHome);
    }

    private void changeState() {
        if (mTvChangeState.getText() == "Đăng kí") {
            if (mEditName.getVisibility() == View.GONE) {
                mEditName.setVisibility(View.VISIBLE);
            }

            if (mBtnSignIn.getVisibility() == View.VISIBLE) {
                mBtnSignIn.setVisibility(View.GONE);
            }

            if (mBtnSignUp.getVisibility() == View.GONE) {
                mBtnSignUp.setVisibility(View.VISIBLE);
            }
            mTvChangeState.setText("Đã có tài khoản");
        } else {
            if (mEditName.getVisibility() == View.VISIBLE) {
                mEditName.setVisibility(View.GONE);
            }

            if (mBtnSignIn.getVisibility() == View.GONE) {
                mBtnSignIn.setVisibility(View.VISIBLE);
            }

            if (mBtnSignUp.getVisibility() == View.VISIBLE) {
                mBtnSignUp.setVisibility(View.GONE);
            }
            mTvChangeState.setText("Đăng kí");
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