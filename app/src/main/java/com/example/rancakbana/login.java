package com.example.rancakbana;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by RancakBana on 10/10/2018.
 */

public class login extends AppCompatActivity {
    EditText email, pswd;
    ProgressDialog pd;
    FirebaseAuth mAuth;
    Button Facebook;
    FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        email = (EditText)findViewById(R.id.email);
        pswd = (EditText)findViewById(R.id.pswd);

        //firebase authentication instance
        mAuth = FirebaseAuth.getInstance();

        //progress dialog context
        pd = new ProgressDialog(this);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //checking user presence
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if ( user != null){
                    Intent home = new Intent(login.this, Home.class);
                    home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(home);
                    finish();
                }

            }
        };
        mAuth.addAuthStateListener(mAuthListener);
    }

    //Method ketika akctivity berakhir
    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    //method ketika activity dimulai
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    //method onclick untuk login
    public void masuk(View view) {
        pd.setMessage("Loging in");

        //membaca inputan user
        String inemail = email.getText().toString();
        String inpswd = pswd.getText().toString();
        //jika sudah benar
        if(!TextUtils.isEmpty(inemail)|| !TextUtils.isEmpty(inpswd)){
            //untuk menampilkan dialog
            pd.show();

            //Login dengan email dan password yang diinputkan user
            mAuth.signInWithEmailAndPassword(inemail, inpswd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //ketika login berhasil
                    if (task.isSuccessful()){
                        Intent move = new Intent( login.this, Home.class);
                        move.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(move);
                        finish();
                        //saat login gagal
                    }else{
                        Toast.makeText(login.this, "Login Failed!",Toast.LENGTH_SHORT).show();
                        int errorDatabase = Log.w("ErrorDatabase", task.getException());
                    }
                    //menutup dialog saat login berhasil atau gagal
                    pd.dismiss();
                }
            });
            //jika user tidak menginputkan apa apa
        }else{
            //untuk memunculkan required sign
            email.setError("Fill in the blank!");
            pswd.setError("Fill in the blank");
            //memunculkan toast field tidak boleh kosong
            Toast.makeText(login.this, " Field Tidak boleh kosong!", Toast.LENGTH_SHORT).show();
        }

    }

    //onclick ketika memencet tombol signup
    public void signup(View view) {
        //Untuk langsung berpindah ke halaman sign up
        startActivity(new Intent(login.this, Singup.class));
    }
}