package moneytracker2017.loftschool.com.loftschoolmoneytrackerjune17;

import java.io.Serializable;

/**
 * Created by Admin on 25.06.2017.
 */

public class Item implements Serializable {
    public static final String TYPE_EXPENSE = "expense";
    public static final String TYPE_INCOME = "income";

    final String name;
    final int price;
    int id;
    final String type;

    public Item(String name, int price, String type) {
        this.name = name;
        this.price = price;
        this.type = type;
    }
}
