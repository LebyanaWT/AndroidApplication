package mycompany.com.androidapplication;


import java.util.List;

/**
 * Created by LebyanaWT on 2017/12/11.
 */

public class Singleton {

    public Double checkOutAmount = null ;
    public List<Item> myCart  = null;

    private static Singleton singletonInstance = null ;

    private Singleton(){
    }
    public static Singleton getInstance(){
        if(singletonInstance == null){
            singletonInstance = new Singleton() ;
        }
        return singletonInstance ;
    }


}
