package moneytracker2017.loftschool.com.loftschoolmoneytrackerjune17;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import moneytracker2017.loftschool.com.loftschoolmoneytrackerjune17.api.BalanceResult;

/**
 * Created by Admin on 30.06.2017.
 */

public class BalanceFragment extends Fragment {

    private TextView income;
    private TextView balance;
    private TextView expense;
    private DiagramView diagram;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.balance_tab, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        income = (TextView) view.findViewById(R.id.income);
        balance = (TextView) view.findViewById(R.id.balance);
        expense = (TextView) view.findViewById(R.id.expense);
        diagram = (DiagramView) view.findViewById(R.id.diagram);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && isResumed()) {
            updateData();
        }
    }

    private void updateData() {
        Log.d("BalanceFragment", "updateData");
        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<BalanceResult>() {
            @Override
            public Loader<BalanceResult> onCreateLoader(int id, Bundle args) {
                return new AsyncTaskLoader<BalanceResult>(getContext()) {
                    @Override
                    public BalanceResult loadInBackground() {
                        try {
                            return ((LSApp) getActivity().getApplicationContext()).api().balance().execute().body();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                };

            }

            @Override
            public void onLoadFinished(Loader<BalanceResult> loader, BalanceResult data) {
                if (data != null && data.isSuccess()) {
                    balance.setText(getString(R.string.count, data.MoneyIncome - data.MoneyExpense));
                    expense.setText(getString(R.string.count, data.MoneyExpense));
                    income.setText(getString(R.string.count, data.MoneyIncome));
                    diagram.update(data.MoneyExpense, data.MoneyIncome);
                } else {
                    Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onLoaderReset(Loader<BalanceResult> loader) {

            }
        }).forceLoad();
    }
}