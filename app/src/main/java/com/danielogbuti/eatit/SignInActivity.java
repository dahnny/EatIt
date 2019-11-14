package com.danielogbuti.eatit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.danielogbuti.eatit.Common.Common;
import com.danielogbuti.eatit.model.Model;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignInActivity extends AppCompatActivity {

    Button signIn;
    MaterialEditText editPassword, editPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        editPassword = (MaterialEditText)findViewById(R.id.editPassword);
        editPhone = (MaterialEditText)findViewById(R.id.editPhone);
        signIn = (Button)findViewById(R.id.buttonSignIn);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = firebaseDatabase.getReference("User");


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog dialog = new ProgressDialog(SignInActivity.this);
                dialog.setMessage("Please Wait");
                dialog.show();
                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.i("Tag",dataSnapshot.getChildrenCount()+"");
                        //check if the number put in exists in the database
                        if (dataSnapshot.child(editPhone.getText().toString()).exists()) {
                            dialog.dismiss();
                            //get the value of the children of the key of the model database
                            Model model = dataSnapshot.child(editPhone.getText().toString()).getValue(Model.class);
                            model.setPhone(editPhone.getText().toString());//set the phoneNumber to the model class
                            //check if the password field of the key is the same with the password inputed
                            if (model.getPassword().equals(editPassword.getText().toString())) {
                            //if same open the home activity
                                Intent intent = new Intent(SignInActivity.this,Home.class);
                                //save the current class to a model class
                                Common.currentUser = model;
                                startActivity(intent);
                                finish();
                            }else {
                                dialog.dismiss();
                                Toast.makeText(SignInActivity.this,"SignIn Failed",Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            dialog.dismiss();
                            Toast.makeText(SignInActivity.this,"User does not exist in database",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
