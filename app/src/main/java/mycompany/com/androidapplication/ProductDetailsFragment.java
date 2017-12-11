package mycompany.com.androidapplication;

/**
 * Created by MANDELACOMP3 on 2017/11/27.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.InputStream;


public class ProductDetailsFragment extends android.support.v4.app.Fragment {
    private ProductDetailsControllerCallback callback;

    public interface ProductDetailsControllerCallback {
        void addToCart(Item item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (ProductDetailsControllerCallback) getActivity();
        } catch (ClassCastException e) {
            throw new IllegalStateException("Hosts must implements ProductDetailsCallback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        callback = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.product_details_layout, container, false);
        //create and set ui components.
        ImageView iconView = (ImageView) view.findViewById(R.id.icon_image_view_details);
        TextView nameView = (TextView) view.findViewById(R.id.name_text_view_details);
        TextView priceView = (TextView) view.findViewById(R.id.price_text_view_details);
        TextView descriptionView = (TextView) view.findViewById(R.id.description_text_view_details);
        //Get the item for this detail screen.
        final Item item = getArguments().getParcelable("item");
        //iconView.setImageDrawable(item.getImage());
        nameView.setText(item.getName());
        priceView.setText( Constants.CURRENCY  + String.valueOf(item.getUnitCost()));

        descriptionView.setText(item.getDescription());
      //  final String pureBase64Encoded = prodImageString.substring(prodImageString.indexOf(",") + 1);
        final String pureBase64Encoded = item.getImage().substring(item.getImage().indexOf(",") + 1);
        decodeImageString(pureBase64Encoded, iconView);
        final Button addButton = (Button) view.findViewById(R.id.add_button_details);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.addToCart(item);
                addButton.setText("Added");
            }
        });
        return view;
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
    private void decodeImageString(String imageString, ImageView imageView){
        byte[] decodeString = Base64.decode (imageString, Base64.DEFAULT);
        Bitmap decoded = BitmapFactory.decodeByteArray (decodeString, 0, decodeString.length);
        imageView.setImageBitmap(decoded);
    }

}
