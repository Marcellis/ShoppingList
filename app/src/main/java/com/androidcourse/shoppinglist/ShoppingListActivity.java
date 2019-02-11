package com.androidcourse.shoppinglist;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ShoppingListActivity extends AppCompatActivity implements RecyclerView.OnItemTouchListener {

	private RecyclerView rvShoppingList;
	private EditText etInput;
	private ShoppingListAdapter shoppingListAdapter;
	private List<Product> shoppingList = new ArrayList<>();
	private ProductRoomDatabase db;

	private GestureDetector gestureDetector;
	private Executor executor = Executors.newSingleThreadExecutor();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopping_list);
		etInput = findViewById(R.id.et_input);
		db = ProductRoomDatabase.getDatabase(this);

		initToolbar();
		initRecyclerView();
		initFloatingActionButton();
	}

	/**
	 * Setup the Add Button.
	 */
	private void initFloatingActionButton() {
		FloatingActionButton fab = findViewById(R.id.fab_add);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String input = etInput.getText().toString();
				final Product product = new Product(input);
				insertProduct(product);
			}
		});
	}

	/**
	 * Add the toolbar to the activity and set the title.
	 */
	private void initToolbar() {
		Toolbar toolbar = findViewById(R.id.toolbar);
		toolbar.setTitle("Shopping List");
		setSupportActionBar(toolbar);
	}

	/**
	 * Initialize the shopping list recycler view with the ShoppingListAdapter and fill the
	 * list with the products stored in the ProductsRoomDatabase.
	 */
	private void initRecyclerView() {
		shoppingListAdapter = new ShoppingListAdapter(shoppingList);
		rvShoppingList = findViewById(R.id.rv_shopping_list);
		rvShoppingList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
		rvShoppingList.setAdapter(shoppingListAdapter);
		rvShoppingList.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

		// Delete an item from the shopping list on long press.
		gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
			@Override
			public void onLongPress(MotionEvent e) {
				super.onLongPress(e);
				View child = rvShoppingList.findChildViewUnder(e.getX(), e.getY());
				if (child != null) {
					int adapterPosition = rvShoppingList.getChildAdapterPosition(child);
					deleteProduct(shoppingList.get(adapterPosition));
				}
			}
		});
		rvShoppingList.addOnItemTouchListener(this);
		getAllProducts();
	}

	/**
	 * Update the shopping list
	 */
	private void updateUI(List<Product> products) {
		shoppingList.clear();
		shoppingList.addAll(products);
		shoppingListAdapter.notifyDataSetChanged();
	}

	/**
	 * Get all products from the database and update the uit with these products.
	 */
	private void getAllProducts() {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				final List<Product> products = db.productDao().getAllProducts();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						updateUI(products);
					}
				});
			}
		});
	}

	/**
	 * Insert a product into the database and update the UI.
	 * @param product Product to be inserted
	 */
	private void insertProduct(final Product product) {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				db.productDao().insert(product);
				getAllProducts();
				runOnUiThread(new Runnable() { // Optionally clear the text from the input field
					@Override
					public void run() {
						etInput.setText("");
					}
				});
			}
		});
	}

	/**
	 * Delete a product from the database and update the UI.
	 * @param product Product to be deleted
	 */
	private void deleteProduct(final Product product) {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				db.productDao().delete(product);
				getAllProducts();
			}
		});
	}

	/**
	 * Delete all products from the database and update the UI.
	 * @param products List<Product> to be deleted.
	 */
	private void deleteAllProducts(final List<Product> products) {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				db.productDao().delete(products);
				getAllProducts();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_shopping_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Checking the item id of our menu item.
		if (item.getItemId() == R.id.action_delete_item) {
			// Deleting all items and notifying our list adapter of the changes.
			deleteAllProducts(shoppingList);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
		gestureDetector.onTouchEvent(motionEvent);
		return false;
	}

	@Override
	public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

	}

	@Override
	public void onRequestDisallowInterceptTouchEvent(boolean b) {

	}
}
