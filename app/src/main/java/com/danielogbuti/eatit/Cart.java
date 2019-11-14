package com.danielogbuti.eatit;

import android.content.DialogInterface;
import android.location.Location;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.danielogbuti.eatit.Common.Common;
import com.danielogbuti.eatit.ViewHolder.CartAdapter;
import com.danielogbuti.eatit.database.Database;
import com.danielogbuti.eatit.model.Order;
import com.danielogbuti.eatit.model.Request;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    TextView textPrice;
    Button placeOrder;

    FirebaseDatabase database;
    DatabaseReference request;

    List<Order> cart;
    CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = (RecyclerView)findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        database = FirebaseDatabase.getInstance();
        request = database.getReference("Request");

        textPrice = (TextView)findViewById(R.id.total);
        placeOrder = (Button)findViewById(R.id.placeOrder);

        loadListFood();


        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this)
                .setTitle("One More Step!")
                .setMessage("Enter your address");

        final EditText editAddress = new EditText(Cart.this);
        //set the parameters for the edittext
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        editAddress.setLayoutParams(layoutParams);
        alertDialog.setView(editAddress);//add the editText to the alertDialog
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        //set the alet dialog to perform an action when affirmative
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //on click get the required info in the request class
                Request requests = new Request(
                        Common.currentUser.getPhone(),
                        Common.currentUser.getName(),
                        editAddress.getText().toString(),
                        textPrice.getText().toString(),
                        cart
                );
                //add the request to firebase using the current time as key
                request.child(String.valueOf(System.currentTimeMillis()))
                        .setValue(requests);
                //erase the sqlDatabase when done
                new Database(getBaseContext()).cleanCart();
                Toast.makeText(Cart.this,"Thank You, Order Placed",Toast.LENGTH_SHORT).show();
                finish();

            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();




    }

    private void loadListFood() {
        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart,this);
        recyclerView.setAdapter(adapter);

        int total = 0;
        for (Order order: cart){
            //go through all the items in the list and add the prices together
            total += (Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuantity()));
            Locale locale = new Locale("en","US");
            NumberFormat format = NumberFormat.getCurrencyInstance(locale);

            textPrice.setText(format.format(total));
        }

    }
}
