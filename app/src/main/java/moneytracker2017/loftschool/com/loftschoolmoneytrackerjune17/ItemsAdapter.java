package moneytracker2017.loftschool.com.loftschoolmoneytrackerjune17;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
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
    private SparseBooleanArray selectedItems = new SparseBooleanArray();

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final Item item = items.get(position);
        holder.name.setText(item.name);
        holder.price.setText(String.valueOf(item.price));
        holder.container.setActivated(selectedItems.get(position, false));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void clear() {
        items.clear();
    }

    public void addAll(List<Item> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void add(Item item) {
        this.items.add(item);
        notifyDataSetChanged();
    }

    public void remove(int id) {
        for (Item item : this.items)
            if (item.id == id) {
                this.items.remove(item);
                notifyDataSetChanged();
                break;
            }
    }

    public void toggleSelection(int pos) {
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
        } else {
            selectedItems.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            int index = selectedItems.keyAt(i);
            items.add(this.items.get(index).id);
        }
        return items;
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView name, price;
        private final View container;

        ItemViewHolder(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.item_container);
            name = (TextView) itemView.findViewById(R.id.name);
            price = (TextView) itemView.findViewById(R.id.price);
        }
    }

}