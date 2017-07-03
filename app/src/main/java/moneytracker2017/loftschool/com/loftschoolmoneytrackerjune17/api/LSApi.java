package moneytracker2017.loftschool.com.loftschoolmoneytrackerjune17.api;

import java.util.List;

import moneytracker2017.loftschool.com.loftschoolmoneytrackerjune17.Item;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;


/**
 * Created by Admin on 01.07.2017.
 */

public interface LSApi {
    @Headers("Content-Type: application/json")
    @GET("items")
    Call<List<Item>> items(@Query("type") String type);

    @POST("items")
    Call<PostResults> add(@Query("name") String name, @Query("price") int price, @Query("type") String type);

}
