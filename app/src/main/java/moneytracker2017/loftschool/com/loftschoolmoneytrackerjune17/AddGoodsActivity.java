package moneytracker2017.loftschool.com.loftschoolmoneytrackerjune17;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class AddGoodsActivity extends AppCompatActivity {
    public static final String EXTRA_TYPE = "type";
    public static final String RESULT_ITEM = "item";
    public static final String RESULT_PRICE = "price";
    public static final int RC_ADD_ITEM = 99;
    private String type;
    private String price;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        type = getIntent().getStringExtra(EXTRA_TYPE);
        price = getIntent().getStringExtra(RESULT_PRICE);

        final TextView add = (TextView) findViewById(R.id.id_button);
        final EditText name_hint = (EditText) findViewById(R.id.id_name_hint);
        final EditText money_count = (EditText) findViewById(R.id.id_money_count);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(getActivity(), AddGoodsActivity.class);
                intent.putExtra(AddGoodsActivity.EXTRA_TYPE, type);
                startActivityForResult(intent, RC_ADD_ITEM);
            }
        });

        name_hint.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                add.setEnabled(!TextUtils.isEmpty(s));

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        money_count.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                add.setEnabled(!TextUtils.isEmpty(s));

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

}
