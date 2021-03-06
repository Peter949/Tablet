package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignIn extends AppCompatActivity
{
    private TextView errorText, change;
    private EditText email, password;
    private Button submit, submitError;
    private ConstraintLayout errorLayout;
    private API api;
    private Retrofit retrofit;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl("http://cinema.areas.su/").build();
        api = retrofit.create(API.class);
        email = findViewById(R.id.emailAuth);
        password = findViewById(R.id.passwordAuth);
        errorText = findViewById(R.id.errorText);
        submit = findViewById(R.id.submitAuth);
        submitError = findViewById(R.id.submitError1);
        errorLayout = findViewById(R.id.error1);
        change = findViewById(R.id.changeAuth);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignIn.this, SignUp.class);
                startActivity(intent);
            }
        });
        submitError.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                errorLayout.setVisibility(View.GONE);
                errorText.setText("");
            }
        });
        submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                in();
            }
        });
    }
    private boolean emailCheck(String email)
    {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private void in()
    {
        if(email.getText().toString().equals("") || password.getText().toString().equals(""))
        {
            errorLayout.setVisibility(View.VISIBLE);
            errorText.setText("?????????????????? ?????? ???????? ??????????!");
        }
        else if(!emailCheck(email.getText().toString()))
        {
            errorLayout.setVisibility(View.VISIBLE);
            errorText.setText("?????????????? ???????????????????? ??????????!");
        }
        else
        {
            ParamSignIn paramSignIn = new ParamSignIn();
            paramSignIn.setEmail(email.getText().toString());
            paramSignIn.setPassword(password.getText().toString());
            Call<ParamSignIn> call = api.doSignIn(paramSignIn);
            call.enqueue(new Callback<ParamSignIn>()
            {
                @Override
                public void onResponse(Call<ParamSignIn> call, Response<ParamSignIn> response)
                {
                    if(response.isSuccessful())
                    {
                        Toast.makeText(getApplicationContext(), "?????????????????????? ???????????? ??????????????", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignIn.this, MainActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        errorLayout.setVisibility(View.VISIBLE);
                        errorText.setText("???????????????????????? ?????????? ?????? ????????????!");
                    }
                }
                @Override
                public void onFailure(Call<ParamSignIn> call, Throwable t)
                {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}