package com.androidcourse.shoppinglist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {

	private List<Product> shoppingList;

	public ShoppingListAdapter(List<Product> shoppingList) {
		this.shoppingList = shoppingList;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
		View view = inflater.inflate(android.R.layout.simple_list_item_1, viewGroup, false);
		return new ShoppingListAdapter.ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
		viewHolder.tvProductName.setText(shoppingList.get(viewHolder.getAdapterPosition()).getName());
	}

	@Override
	public int getItemCount() {
		return shoppingList.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {

		private TextView tvProductName;

		public ViewHolder(@NonNull View itemView) {
			super(itemView);
			tvProductName = itemView.findViewById(android.R.id.text1);
		}
	}
}
