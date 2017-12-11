package mycompany.com.androidapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LebyanaWT on 2017/12/06.
 */

public class CartListFragment extends android.support.v4.app.Fragment  {

    private static final String TAG = "EVENT";
    private CartListControllerCallback callback;
    private ShoppingCart myCart;
    private ProductListAdapter listAdapter;
    private Order custOrder;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String currentDate = sdf.format(new Date());
    private Order order;
    Item item = new Item();




    public interface CartListControllerCallback {
        List<Item> cartItems();
    }
    public void setShoppingCart(ShoppingCart myCart){
        this.myCart = myCart;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cart_products_layout, container, false);
        GridView cartList = (GridView) view.findViewById(R.id.listItems_cart);

        cartList.setAdapter( new ProductListAdapter(getActivity(), R.layout.cart_list_row, myCart.getAllItems()));

        final Button checkOutbtn = (Button) view.findViewById(R.id.checkBtn);
        checkOutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                volleyOrderRequestService();
            }
        });
        return view;
    }

    public void volleyOrderRequestService(){
        RequestQueue requestQ = Volley.newRequestQueue(getContext());
        String serviceUrl = "http://192.168.2.59:8081/placeOrder";
        double checkInPriceTotal;
        int quantity = 1;
        double  totalAmount;

        /**
         * Not Working Yet
         *  double unitCost = item.getUnitCost()
         * double  totalAmount = unitCost * quantity;
         * */

        /**
         * Working so far with manually inserted Values for Order
         * **/
        totalAmount = 147.85;
        Map<String,Object> orderParams = new HashMap<>();
        orderParams.put("quantity",1);
        orderParams.put("price", totalAmount);
        orderParams.put("status","suceessfully" );
        orderParams.put("ordered_date", currentDate);
        orderParams.put("custId", 9);
        orderParams.put("paymentId", 8);
        orderParams.put("prodsInCart", myCart.getAllItems());

        JsonObjectRequest reqRequest = new JsonObjectRequest(Request.Method.POST,serviceUrl, new JSONObject(orderParams)
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String msg = "success";
                Log.d(TAG , "Json" +response.toString());
                try{
                    msg = (String)response.get("msg");
                } catch (JSONException e){
                    e.printStackTrace();
                }
                if(msg.equals("success")){
                    Toast.makeText(getContext(), "Order Placed", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getContext(), "Order Denied!!!", Toast.LENGTH_LONG).show();
                    Log.d(TAG , "Json" + response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError Error) {
                Log.d(TAG , "Error" + Error);
            }
        });
        requestQ.add(reqRequest);
        Log.d(TAG , "Json" + new JSONObject(orderParams));
    }
}
