package mycompany.com.androidapplication;
/**
 * Created by LebyanaWT on 2017/11/27.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.List;



public class ProductListAdapter extends ArrayAdapter<Item> {
    private List<Item> items;
    Button btnCart;

    MainActivity aController = (MainActivity) getContext();

    public ProductListAdapter(Context context, int resource, List<Item> objects) {
        super(context, resource, objects);
        items = objects;
    }
    @Override
    public int getCount() {
        return super.getCount();
    }
    @Override
    public Item getItem(int position) {
        return super.getItem(position);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.product_list_row, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name_text_view_row);
            holder.price = (TextView) convertView.findViewById(R.id.price_text_view_row);
//            holder.btnCart = (Button) convertView.findViewById(R.id.btnCartAdd);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //bind data to views.
        holder.name.setText(getItem(position).getName());
        holder.price.setText( Constants.CURRENCY + String.valueOf(getItem(position).getUnitCost()));
        holder.icon = (ImageView) convertView.findViewById(R.id.icon_image_view_row);
        final String pureBase64Encoded = getItem(position).getImage().substring(getItem(position).getImage().indexOf(",") + 1);
        decodeImageString(pureBase64Encoded, holder.icon);
//        btnCart = (Button) convertView.findViewById(R.id.btnCartAdd);
        return convertView;
    }
    private static class ViewHolder {
        TextView name, price;
        ImageView icon;
        Button btnCart;
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
        protected void onPostExecute(Bitmap bitmap){
            super.onPostExecute(bitmap);
        }
    }
    private void decodeImageString(String imageString, ImageView imageView){
        byte[] decodeString = Base64.decode (imageString, Base64.DEFAULT);
        Bitmap decoded = BitmapFactory.decodeByteArray (decodeString, 0, decodeString.length);
        imageView.setImageBitmap(decoded);
    }
}
