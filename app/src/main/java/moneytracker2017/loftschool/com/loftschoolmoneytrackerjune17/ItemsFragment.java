package moneytracker2017.loftschool.com.loftschoolmoneytrackerjune17;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
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
    private ActionMode actionMode;

    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.items, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_remove:
                    new AlertDialog.Builder(getContext())
                            .setTitle(R.string.app_name)
                            .setMessage(R.string.confirm_remove)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    for (Integer selectedItemId : adapter.getSelectedItems())
                                        removeItem(selectedItemId);
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, null)
                            .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    actionMode.finish();
                                }
                            })
                            .show();
                    return true;
            }
            return false;
        }


        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
            adapter.clearSelections();
            add.setVisibility(View.VISIBLE);
        }
    };

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
        final GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                if (actionMode == null) {
                    actionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(actionModeCallback);
                    adapter.toggleSelection(items.getChildLayoutPosition(items.findChildViewUnder(e.getX(), e.getY())));
                    add.setVisibility(View.GONE);
                }
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                toggleSelection(e, items);
                return super.onSingleTapConfirmed(e);
            }

        });
        items.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

        final SwipeRefreshLayout refresh = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadItems();
                refresh.setRefreshing(false);

            }
        });

        type = getArguments().getString(ARG_TYPE);
        api = ((LSApp) getActivity().getApplication()).api();
        loadItems();

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

    private void toggleSelection(MotionEvent e, RecyclerView items) {
        adapter.toggleSelection(items.getChildLayoutPosition(items.findChildViewUnder(e.getX(), e.getY())));
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
            postItems(item);
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
                    item.id = data.id;
                    adapter.add(item);
                }
                getLoaderManager().destroyLoader(LOADER_ADD);
            }

            @Override
            public void onLoaderReset(Loader<PostResults> loader) {
            }
        }).forceLoad();

    }

    private void removeItem(final int item_id) {
        getLoaderManager().initLoader(LOADER_REMOVE, null, new LoaderManager.LoaderCallbacks<AddGoodsActivity>() {

            @Override
            public Loader<AddGoodsActivity> onCreateLoader(int id, Bundle args) {

                return new AsyncTaskLoader<AddGoodsActivity>(getContext()) {
                    @Override
                    public AddGoodsActivity loadInBackground() {
                        try {
                            return api.remove(item_id).execute().body();
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                };
            }

            @Override
            public void onLoadFinished(Loader<AddGoodsActivity> loader, AddGoodsActivity data) {
                if (data == null) {
                    Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
                } else {
                    adapter.remove(data.id);
                    Toast.makeText(getContext(), R.string.Remove, Toast.LENGTH_SHORT).show();
                }
                getLoaderManager().destroyLoader(LOADER_REMOVE);
            }

            @Override
            public void onLoaderReset(Loader<AddGoodsActivity> loader) {
            }
        }).forceLoad();

    }
}