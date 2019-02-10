package com.androidcourse.shoppinglist;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Product.class}, version = 1, exportSchema = false)
public abstract class ProductRoomDatabase extends RoomDatabase {

	private final static String NAME_DATABASE = "product_database";
	public abstract ProductDao productDao();

	private static volatile ProductRoomDatabase INSTANCE;

	static ProductRoomDatabase getDatabase(final Context context) {
		if (INSTANCE == null) {
			synchronized (ProductRoomDatabase.class) {
				if (INSTANCE == null) {
					// Create database here
					INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
							ProductRoomDatabase.class, NAME_DATABASE)
							.build();
				}
			}
		}
		return INSTANCE;
	}
}

