package mycompany.com.androidapplication;

import java.util.List;

/**
 * Created by LebyanaWT on 2017/12/06.
 */

public class ShoppingCart<T> {

    private List<T> items;
    private Item item;

    public ShoppingCart() {
    }

    public List<T> getAllItems() {
        return items;
    }

    public void addItem(T item) {
        items.add(item);
    }

    public void removeItem(T item) {
        items.remove(item);
    }

    public int getSize() {
        return items.size();
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

}
