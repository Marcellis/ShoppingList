package com.androidcourse.shoppinglist;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;

@Dao
public interface ProductDao {

	@Insert
	void insert(Product product);

	@Delete
	void delete(Product product);

}
