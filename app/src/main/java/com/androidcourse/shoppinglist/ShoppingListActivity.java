package com.androidcourse.shoppinglist;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListActivity extends AppCompatActivity {

	private RecyclerView rvShoppingList;
	private ShoppingListAdapter shoppingListAdapter;
	private List<Product> shoppingList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopping_list);

		initToolbar();
		initRecyclerView();
		initFloatingActionButton();
	}

	private void initFloatingActionButton() {
		FloatingActionButton fab = findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();
			}
		});
	}

	private void initToolbar() {
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
	}

	private void initRecyclerView() {
		shoppingListAdapter = new ShoppingListAdapter(shoppingList);
		rvShoppingList = findViewById(R.id.rv_shopping_list);
		rvShoppingList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
		rvShoppingList.setAdapter(shoppingListAdapter);
		rvShoppingList.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

		shoppingList.add(new Product("Bread"));
		shoppingList.add(new Product("Milk"));
		updateUI();
	}

	private void updateUI() {
		shoppingListAdapter.notifyDataSetChanged();
	}

}
