package com.danielogbuti.eatit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.danielogbuti.eatit.Interface.ItemClickListener;
import com.danielogbuti.eatit.ViewHolder.FoodViewHolder;
import com.danielogbuti.eatit.model.Food;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FoodList extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference foodList;


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    String categoryId ="";

    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;

    //search functionality
    FirebaseRecyclerAdapter<Food, FoodViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        //init firebase and recyclerView
        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Foods");



        //get the id of the recyclerView to be used
        recyclerView = (RecyclerView)findViewById(R.id.recycler_food);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        //get intent extras
        if (getIntent()!=null){
            categoryId = getIntent().getStringExtra("category");
        }
        if (!categoryId.isEmpty() && categoryId != null){
            loadFoodList(categoryId);
        }

        //search
        materialSearchBar = (MaterialSearchBar)findViewById(R.id.searchBar);
        materialSearchBar.setHint("Enter your food");
        loadSuggest();
        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //when user tyoes their text we will change suggest list
                List<String> suggest = new ArrayList<>();
                for (String search : suggestList){
                    //if thke text in the search bar is the same as in the suggestList, add it to suggest
                    if (search.toLowerCase().contains(materialSearchBar.getText().toLowerCase())){
                        suggest.add(search);

                        materialSearchBar.setLastSuggestions(suggest);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                //when search bar is closed
                //restore adapter
                if (!enabled)
                    searchAdapter.stopListening();
                    adapter.startListening();
                    recyclerView.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                //when search is finised
                if (text!=null) {
                    adapter.stopListening();
                    StartSearch(text);
                    searchAdapter.startListening();
                }
            }

            @Override
            public void onButtonClicked(int buttonCode) {


            }
        });
    }

    private void StartSearch(CharSequence text) {
        FirebaseRecyclerOptions<Food> options = new FirebaseRecyclerOptions.Builder<Food>()
                //get the text from the search bar and ahowcase its view holder
                .setQuery(foodList.orderByChild("Name").equalTo(text.toString()), Food.class)
                .build();

        searchAdapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder holder, int position, @NonNull Food model) {
                holder.foodName.setText(model.getName());
                //use Picasso to get the image
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(holder.foodImage);
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intent =  new Intent(FoodList.this,FoodDetail.class);
                        intent.putExtra("foodDetail",searchAdapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                return new FoodViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.food_item,viewGroup,false));
            }
        };
        recyclerView.setAdapter(searchAdapter);

    }

    private void loadSuggest() {
        //if name entered is equal to the Name in our database, get that particular class and get the name
        foodList.orderByChild("MenuId").equalTo(categoryId).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                           Log.e("Tag",postSnapshot.getKey());
                            Food item = postSnapshot.getValue(Food.class);
                            suggestList.add(item.getName());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void loadFoodList(String categoryId) {
        //create the FireBase Options to be used
        FirebaseRecyclerOptions<Food> options = new FirebaseRecyclerOptions.Builder<Food>()
                //set query to select only foodItems whose menuId is equal to the categoryId
                .setQuery(foodList.orderByChild("MenuId").equalTo(categoryId), Food.class)
                .build();

        //set the adapter
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder holder, int position, @NonNull Food model) {
                holder.foodName.setText(model.getName());
                //use Picasso to get the image
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(holder.foodImage);
                final Food item = model;
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intent =  new Intent(FoodList.this,FoodDetail.class);
                        intent.putExtra("foodDetail",adapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                //inflate the layout
                return new FoodViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.food_item,viewGroup,false));
            }
        };

        //set the adapter
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
