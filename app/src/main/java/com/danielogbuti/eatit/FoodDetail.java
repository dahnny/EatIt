package com.danielogbuti.eatit;

import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.danielogbuti.eatit.database.Database;
import com.danielogbuti.eatit.model.Food;
import com.danielogbuti.eatit.model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FoodDetail extends AppCompatActivity {

    TextView foodName,foodPrice,foodDescription;
    ImageView foodImage;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton buttonCart;
    ElegantNumberButton numberButton;

    String foodId = "";

    Food food;

    FirebaseDatabase database;
    DatabaseReference foods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        database = FirebaseDatabase.getInstance();
        foods = database.getReference("Foods");

        foodName = (TextView)findViewById(R.id.food_name);
        foodPrice = (TextView)findViewById(R.id.food_price);
        foodImage = (ImageView)findViewById(R.id.img_food);
        foodDescription = (TextView)findViewById(R.id.food_description);

        buttonCart = (FloatingActionButton)findViewById(R.id.buttonCart);
        buttonCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).addToCart(new Order(foodId,food.getName(),
                        numberButton.getNumber(),food.getPrice(),food.getDiscount()));

                Toast.makeText(FoodDetail.this,"Added to cart",Toast.LENGTH_SHORT).show();
            }
        });

        numberButton = (ElegantNumberButton)findViewById(R.id.numberButton);

        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);


        if (getIntent()!= null){
            foodId = getIntent().getStringExtra("foodDetail");
        }
        if(!foodId.isEmpty()){
            getFoodDetail(foodId);
        }
    }

    private void getFoodDetail(String foodId) {
        foods.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                food = dataSnapshot.getValue(Food.class);

                Picasso.with(getBaseContext()).load(food.getImage())
                        .into(foodImage);

                collapsingToolbarLayout.setTitle(food.getName());

                foodPrice.setText(food.getPrice());
                foodDescription.setText(food.getDescription());
                foodName.setText(food.getName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FoodDetail.this,"Error Occurred",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
