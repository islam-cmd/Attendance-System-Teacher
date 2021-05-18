package ses.attendance_system_teacher;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {


    EditText et_email;
    EditText et_password;
    FrameLayout fl_login;
    FirebaseAuth auth;
    TextView tv_forgotPassword;
    FirebaseUser firebaseUser;

//
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        //auto login
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            startActivity(new Intent(LoginActivity.this, SessionListActivity.class));
            finish();
        }

        setContentView(R.layout.activity_login);
        et_email = findViewById(R.id.email);
        et_password = findViewById(R.id.password);
        fl_login = findViewById(R.id.fl_login);
        tv_forgotPassword = findViewById(R.id.forgot_password);
        auth = FirebaseAuth.getInstance();

        // move to ResetPasswordActivity
        tv_forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(  new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });


        // login process
        //1. get data from the input fields
        //2. check if valid (empty, not email, etc)
        //3. check with firebase authentication
        //4. login if the email and password are correct
        fl_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = et_email.getText().toString();
                String txt_password = et_password.getText().toString();
                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(LoginActivity.this, "All Fields are Required", Toast.LENGTH_SHORT).show();
                } else {

//                    lamtienlong9@gmail.com
                    auth.signInWithEmailAndPassword(txt_email, txt_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(LoginActivity.this, FacialRecActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Authentication failed!", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }
        });

    }
}