package mycompany.com.androidapplication;

import java.util.Collection;
import java.util.Date;

/**
 * Created by LebyanaWT on 2017/12/07.
 */

public class Order {
    private int id;
    private int quantity;
    private double price;
    private String status;
    private Date ordered_date;
    private int custId;
    private int paymentId;
    private Collection prodsInCart;

    public Order(int id, int quantity, double price, String status, Date ordered_date, int custId, int paymentId, Collection prodsInCart) {
        this.id = id;
        this.quantity = quantity;
        this.price = price;
        this.status = status;
        this.ordered_date = ordered_date;
        this.custId = custId;
        this.paymentId = paymentId;
        this.prodsInCart = prodsInCart;
    }


}
