package com.lerex.tr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class SignupActivity extends AppCompatActivity {


    private EditText email,password,retypepassword;
    private Button signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//FullScreening The Application

        setContentView(R.layout.activity_signup);

        email = findViewById(R.id.etEmail);
        password=findViewById(R.id.etPass);
        retypepassword=findViewById(R.id.etRepass);
        signup=findViewById(R.id.btnSignup);

    }
}
