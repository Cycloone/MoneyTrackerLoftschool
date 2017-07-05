package moneytracker2017.loftschool.com.loftschoolmoneytrackerjune17;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import moneytracker2017.loftschool.com.loftschoolmoneytrackerjune17.api.LSApi;
import moneytracker2017.loftschool.com.loftschoolmoneytrackerjune17.api.PostResults;

import static android.app.Activity.RESULT_OK;
import static moneytracker2017.loftschool.com.loftschoolmoneytrackerjune17.AddGoodsActivity.RC_ADD_ITEM;


public class ItemsFragment extends Fragment {
    private static final int LOADER_ITEMS = 0;
    private static final int LOADER_ADD = 1;
    private static final int LOADER_REMOVE = 2;
    private View add;
    private SwipeRefreshLayout refresh;


    public static final String ARG_TYPE = "type";
    private String type;
    private LSApi api;
    private ItemsAdapter adapter = new ItemsAdapter();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.items, null);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final RecyclerView items = (RecyclerView) view.findViewById(R.id.items);
        items.setAdapter(adapter);
        type = getArguments().getString(ARG_TYPE);
        api = ((LSApp) getActivity().getApplication()).api();
        loadItems();
        refresh = (SwipeRefreshLayout) view.
                findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadItems();
            }
        });

        add = view.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddGoodsActivity.class);
                intent.putExtra(AddGoodsActivity.EXTRA_TYPE, type);
                startActivityForResult(intent, RC_ADD_ITEM);
            }

        });
    }


    private void loadItems() {
        getLoaderManager().initLoader(LOADER_ITEMS, null, new LoaderManager.LoaderCallbacks<List<Item>>() {
            @Override
            public Loader<List<Item>> onCreateLoader(int id, Bundle args) {
                return new AsyncTaskLoader<List<Item>>(getContext()) {
                    @Override
                    public List<Item> loadInBackground() {
                        try {
                            return api.items(type).execute().body();

                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                };
            }

            @Override
            public void onLoadFinished(Loader<List<Item>> loader, List<Item> data) {
                if (data == null) {
                    Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
                } else {
                    adapter.clear();
                    adapter.addAll(data);
                }
            }

            @Override
            public void onLoaderReset(Loader<List<Item>> loader) {
            }
        }).forceLoad();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AddGoodsActivity.RC_ADD_ITEM && resultCode == RESULT_OK) {
            Item item = (Item) data.getSerializableExtra(AddGoodsActivity.RESULT_ITEM);
            Toast toast = Toast.makeText(getContext(), item.name, Toast.LENGTH_LONG);
            toast.show();
        }
    }


    private void postItems(final Item item) {
        getLoaderManager().initLoader(LOADER_ADD, null, new LoaderManager.LoaderCallbacks<PostResults>() {
            @Override
            public Loader<PostResults> onCreateLoader(int id, Bundle args) {
                return new AsyncTaskLoader<PostResults>(getContext()) {
                    @Override
                    public PostResults loadInBackground() {
                        try {
                            return api.add(item.name, item.price, item.type).execute().body();

                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                };
            }

            @Override
            public void onLoadFinished(Loader<PostResults> loader, PostResults data) {
                if (data == null) {
                    Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
                } else {
                    adapter.clear();
                }
            }

            @Override
            public void onLoaderReset(Loader<PostResults> loader) {
            }
        }).forceLoad();

    }

}