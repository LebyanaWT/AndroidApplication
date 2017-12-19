package mycompany.com.androidapplication;
/**
 * Created by LebyanaWT on 2017/11/27.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements ProductListFragment.ProductListControllerCallback<Item>
        ,ProductDetailsFragment.ProductDetailsControllerCallback {

    private static final String TAG = "Order Data";
    private ProductListFragment productListFragment;
    private ArrayList<Item> myProducts = new ArrayList<Item>();
    private ShoppingCart myCart;
    private ProductDetailsFragment productDetailsFragment;
    private CartListFragment cartListFragment;
    private Button cartButton,checkoutBtn,cntinueShopBtn;
    private double checkOutPrice = 0.0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final List<Item> items = (List<Item>) getLastCustomNonConfigurationInstance();
        myCart = new ShoppingCart();
        if (items != null) {
            myCart.setItems(items);
//            Singleton.getInstance().myCartItems.setItems(items);
        } else {
            myCart.setItems(new ArrayList<Item>());
        }
        FragmentManager manager = getSupportFragmentManager();
        if (manager.findFragmentByTag("pjf") != null) {
            productListFragment = (ProductListFragment) manager.findFragmentByTag("plf");
        } else {
            productListFragment = new ProductListFragment();
        }
        if (savedInstanceState == null) {
            manager.beginTransaction().add(R.id.main_content, productListFragment, "plf").commit();
        }
        if (manager.findFragmentByTag("pdf") != null) {
            productDetailsFragment = (ProductDetailsFragment) manager.findFragmentByTag("pdf");
        } else {
            productDetailsFragment = new ProductDetailsFragment();
        }
        if (manager.findFragmentByTag("clf") != null) {
            cartListFragment = (CartListFragment) manager.findFragmentByTag("clf");
        } else {
            cartListFragment = new CartListFragment();
        }
        cartListFragment.setShoppingCart(myCart);
        cartButton = (Button) findViewById(R.id.cart_item_btn);
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewCartItemOnClick();
            }
        });
    }
    @Override
    public void onItemClicked(Item item) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("item", item);
        productDetailsFragment.setArguments(bundle);
        //Swap out the listFragment for the detials fragment, saving the transaction to the back stack.
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("pdf")
                .replace(R.id.main_content, productDetailsFragment, "pdf")
                .commit();
    }
    @Override
    public void addToCart(Item item) {
        myCart.addItem(item);
        cartButton = (Button) findViewById(R.id.cart_item_btn);
        checkOutPrice += item.getUnitCost() ;
        Singleton.getInstance().checkOutAmount = checkOutPrice;
        //update the cart button text.
        cartButton.setText("Items: " + " " + myCart.getSize());
//        TextView subTotal = (TextView)findViewById(R.id.sub_total);
//        subTotal.setText((int) checkOutPrice);

    }
    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return myCart.getAllItems();
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //update the cart button text.
    }
    public void viewCartItemOnClick(){
        if(!cartListFragment.isVisible()){
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack("clf")
                    .replace(R.id.main_content, cartListFragment, "clf")
                    .commit();
        }

    }
}