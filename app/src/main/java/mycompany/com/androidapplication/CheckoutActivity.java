package mycompany.com.androidapplication;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;




import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mycompany.com.androidapplication.helpers.MySharedPreference;
import mycompany.com.androidapplication.helpers.SimpleDividerItemDecoration;

public class CheckoutActivity extends AppCompatActivity {

    private static final String TAG = CheckoutActivity.class.getSimpleName();

    private RecyclerView checkRecyclerView;

    private TextView subTotal;

    private double mSubTotal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTitle("Over Cart");

        subTotal = (TextView )findViewById(R.id.sub_total);

        checkRecyclerView = (RecyclerView)findViewById(R.id.checkout_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CheckoutActivity.this);
        checkRecyclerView.setLayoutManager(linearLayoutManager);
        checkRecyclerView.setHasFixedSize(true);
        checkRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(CheckoutActivity.this));

        // get content of cart
        MySharedPreference mShared = new MySharedPreference(CheckoutActivity.this);

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

       Item addCartProducts = gson.fromJson(mShared.retrieveProductFromCart(), Item.class);
        List<Item> productList = convertObjectArrayToListObject(addCartProducts);

        CheckRecyclerViewAdapter mAdapter = new CheckRecyclerViewAdapter(CheckoutActivity.this, Singleton.getInstance().myCart);
        checkRecyclerView.setAdapter(mAdapter);

        mSubTotal = 12;
        subTotal.setText("Subtotal excluding tax and shipping: " + String.valueOf(mSubTotal) + " $");


        Button shoppingButton = (Button)findViewById(R.id.shopping);
        assert shoppingButton != null;
        shoppingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shoppingIntent = new Intent(CheckoutActivity.this, MainActivity.class);
                startActivity(shoppingIntent);
            }
        });

        Button checkButton = (Button)findViewById(R.id.checkout);
        assert checkButton != null;
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent paymentIntent = new Intent(CheckoutActivity.this, MainActivity.class);
                paymentIntent.putExtra("TOTAL_PRICE", mSubTotal);
                startActivity(paymentIntent);
            }
        });
    }

    private List<Item> convertObjectArrayToListObject(Item allProducts){
        List<Item> mProduct = new ArrayList<Item>();
        Collections.addAll(mProduct, allProducts);
        return mProduct;
    }

    private int returnQuantityByProductName(String productName, List<Item> mProducts){
        int quantityCount = 0;
        for(int i = 0; i < mProducts.size(); i++){
            Item pObject = mProducts.get(i);
            if(pObject.getName().trim().equals(productName.trim())){
                quantityCount++;
            }
        }
        return quantityCount;
    }

    private double getTotalPrice(List<Item> mProducts){
        double totalCost = 0;
        for(int i = 0; i < mProducts.size(); i++){
            Item pObject = mProducts.get(i);
            totalCost = totalCost + pObject.getUnitCost();
        }
        return totalCost;
    }
}
