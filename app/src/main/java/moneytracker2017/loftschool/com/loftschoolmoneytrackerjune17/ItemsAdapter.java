package moneytracker2017.loftschool.com.loftschoolmoneytrackerjune17;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Admin on 27.06.2017.
 */
class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    final List<Item> items = new ArrayList<>();
    private Resources resources;

    ItemsAdapter() {
        items.add(new Item("Milk", 100));
        items.add(new Item("Nuts", 400));
        items.add(new Item("Butter", 100));
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, null));
    }


    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final Item item = items.get(position);
        holder.name.setText(item.name);
        holder.price.setText(String.valueOf(item.price));
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public Resources getResources() {
        return resources;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView name, price;

        ItemViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            price = (TextView) itemView.findViewById(R.id.price);
        }
    }

}
