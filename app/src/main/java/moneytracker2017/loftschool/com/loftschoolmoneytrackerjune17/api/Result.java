package moneytracker2017.loftschool.com.loftschoolmoneytrackerjune17.api;

import android.text.TextUtils;

/**
 * Created by Admin on 11.07.2017.
 */

public class Result {
    String status;

    public boolean isSuccess() {
        return TextUtils.equals(status, "success");
    }
}
