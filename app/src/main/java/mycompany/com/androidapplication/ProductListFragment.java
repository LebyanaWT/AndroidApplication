package mycompany.com.androidapplication;
/**
 * Created by LebyanaWT on 2017/11/27.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class ProductListFragment extends ListFragment {

    private ProductListControllerCallback callback;
    private ProductListAdapter listAdapter;
    private final static String URL = "http://192.168.2.59:8081/products/all";
    Button btnCart;

    public interface ProductListControllerCallback<T> {
        void onItemClicked(T item);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (ProductListControllerCallback) getActivity();
        } catch (ClassCastException e) {
            throw new IllegalStateException("Hosts must implement ProductListControllerCa" + "0llback");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listAdapter = new ProductListAdapter(getActivity(), R.layout.product_list_row, new ArrayList<Item>());
        setListAdapter(listAdapter);
        fetchProducts();
    }
    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
        callback.onItemClicked(this.getListAdapter().getItem(position));
    }
    private void fetchProducts() {
        RequestQueue requestque = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("onResponse", response);
                        try {
                            List<Item> temp = createListFromData(response);
                            listAdapter.addAll(temp);
                            listAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley error", error.getMessage());
                    }
                }
        );
        requestque.add(request);
    }
    private List<Item> createListFromData(String data) throws JSONException {
        List<Item> items = new ArrayList();
        JSONArray mainArray = new JSONArray(data);
        int length = mainArray.length();
        for (int i = 0; i < length; i++) {
            //pull out every object in the array and add it to the list.
            JSONObject obj = mainArray.optJSONObject(i);
            Item item = new Item();
            item.setName(obj.optString("name"));
            item.setDescription(obj.optString("description"));
            item.setUnitCost(Double.parseDouble(obj.optString("unitCost")));
            item.setImage(obj.optString("image"));
            items.add(item);
        }
        return items;
    }
    public class ImageRequestAsk extends AsyncTask<String , Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            try{
                InputStream inputStream = new java.net.URL(params[0]).openStream() ;
                return BitmapFactory.decodeStream(inputStream) ;
            }catch (Exception e){
                return null ;
            }
        }
        @Override
        protected void onPostExecute(Bitmap bitmap){super.onPostExecute(bitmap);}
    }
}