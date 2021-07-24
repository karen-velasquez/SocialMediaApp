package com.example.socialmediaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    //views
    EditText mEmailEt, mPasswordEt;
    Button mRegisterBtn;
    ProgressDialog progressDialog;

    //Declarando la instancia de FirebaseAuth
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        //ActionBar poniendo titulo
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Crear Cuenta");
        //permitiendo boton de atras
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        mEmailEt=findViewById(R.id.emailEt);
        mPasswordEt=findViewById(R.id.passwordEt);
        mRegisterBtn=findViewById(R.id.registerBtn);

        //En el OnCreate() metodo, inicializar el FirebaseAuth instance
        mAuth=FirebaseAuth.getInstance();

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Usuario registrado...");

        //Al hacer clik en el boton
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //entrada email,password
                String email=mEmailEt.getText().toString().trim();
                String password=mPasswordEt.getText().toString().trim();
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    //Seleccionar el edittext en caso de error
                    mEmailEt.setError("Invalid Email");
                    mEmailEt.setFocusable(true);
                }else if(password.length()<6){
                    //Seleccionar error en el campo de contrase;a
                    mPasswordEt.setError("El tamanio de la contrasenia debe ser mayor a 6 caracterse");
                    mPasswordEt.setFocusable(true);
                }else{
                    registerUser(email,password);
                }
            }
        });
    }

    private void registerUser(String email, String password) {
        //Si el email y contrasenia son validos mostrar un mensaje al registrar el usuario
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //Sign in success. dismiss dialog and register activity
                            progressDialog.dismiss();
                            FirebaseUser user=mAuth.getCurrentUser();
                            Toast.makeText(RegisterActivity.this,"Registrado...\n",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this,ProfileActivity.class));
                            finish();
                        }else{
                            //Si el ingreso falla, despleagar el mensaje a el usuario
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this,"Autenticacion fallida",Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed(); //Ir a la actividad anterior
        return super.onSupportNavigateUp();
    }


}