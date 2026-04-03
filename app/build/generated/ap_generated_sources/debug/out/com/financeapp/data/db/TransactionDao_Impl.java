package com.financeapp.data.db;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.financeapp.data.model.CategoryTotal;
import com.financeapp.data.model.MonthlyTotal;
import com.financeapp.data.model.Transaction;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class TransactionDao_Impl implements TransactionDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Transaction> __insertionAdapterOfTransaction;

  private final EntityDeletionOrUpdateAdapter<Transaction> __deletionAdapterOfTransaction;

  private final EntityDeletionOrUpdateAdapter<Transaction> __updateAdapterOfTransaction;

  public TransactionDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTransaction = new EntityInsertionAdapter<Transaction>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `transactions` (`id`,`amount`,`type`,`category`,`category_icon`,`category_color`,`date`,`note`,`created_at`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final Transaction entity) {
        statement.bindLong(1, entity.id);
        statement.bindDouble(2, entity.amount);
        if (entity.type == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.type);
        }
        if (entity.category == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.category);
        }
        if (entity.categoryIcon == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.categoryIcon);
        }
        statement.bindLong(6, entity.categoryColor);
        statement.bindLong(7, entity.date);
        if (entity.note == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.note);
        }
        statement.bindLong(9, entity.createdAt);
      }
    };
    this.__deletionAdapterOfTransaction = new EntityDeletionOrUpdateAdapter<Transaction>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `transactions` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final Transaction entity) {
        statement.bindLong(1, entity.id);
      }
    };
    this.__updateAdapterOfTransaction = new EntityDeletionOrUpdateAdapter<Transaction>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `transactions` SET `id` = ?,`amount` = ?,`type` = ?,`category` = ?,`category_icon` = ?,`category_color` = ?,`date` = ?,`note` = ?,`created_at` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final Transaction entity) {
        statement.bindLong(1, entity.id);
        statement.bindDouble(2, entity.amount);
        if (entity.type == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.type);
        }
        if (entity.category == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.category);
        }
        if (entity.categoryIcon == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.categoryIcon);
        }
        statement.bindLong(6, entity.categoryColor);
        statement.bindLong(7, entity.date);
        if (entity.note == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.note);
        }
        statement.bindLong(9, entity.createdAt);
        statement.bindLong(10, entity.id);
      }
    };
  }

  @Override
  public long insert(final Transaction transaction) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfTransaction.insertAndReturnId(transaction);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final Transaction transaction) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfTransaction.handle(transaction);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final Transaction transaction) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfTransaction.handle(transaction);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public LiveData<List<Transaction>> getAllTransactions() {
    final String _sql = "SELECT * FROM transactions ORDER BY date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"transactions"}, false, new Callable<List<Transaction>>() {
      @Override
      @Nullable
      public List<Transaction> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfCategoryIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "category_icon");
          final int _cursorIndexOfCategoryColor = CursorUtil.getColumnIndexOrThrow(_cursor, "category_color");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final List<Transaction> _result = new ArrayList<Transaction>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Transaction _item;
            _item = new Transaction();
            _item.id = _cursor.getInt(_cursorIndexOfId);
            _item.amount = _cursor.getDouble(_cursorIndexOfAmount);
            if (_cursor.isNull(_cursorIndexOfType)) {
              _item.type = null;
            } else {
              _item.type = _cursor.getString(_cursorIndexOfType);
            }
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _item.category = null;
            } else {
              _item.category = _cursor.getString(_cursorIndexOfCategory);
            }
            if (_cursor.isNull(_cursorIndexOfCategoryIcon)) {
              _item.categoryIcon = null;
            } else {
              _item.categoryIcon = _cursor.getString(_cursorIndexOfCategoryIcon);
            }
            _item.categoryColor = _cursor.getInt(_cursorIndexOfCategoryColor);
            _item.date = _cursor.getLong(_cursorIndexOfDate);
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _item.note = null;
            } else {
              _item.note = _cursor.getString(_cursorIndexOfNote);
            }
            _item.createdAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<List<Transaction>> getRecentTransactions(final int limit) {
    final String _sql = "SELECT * FROM transactions ORDER BY date DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    return __db.getInvalidationTracker().createLiveData(new String[] {"transactions"}, false, new Callable<List<Transaction>>() {
      @Override
      @Nullable
      public List<Transaction> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfCategoryIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "category_icon");
          final int _cursorIndexOfCategoryColor = CursorUtil.getColumnIndexOrThrow(_cursor, "category_color");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final List<Transaction> _result = new ArrayList<Transaction>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Transaction _item;
            _item = new Transaction();
            _item.id = _cursor.getInt(_cursorIndexOfId);
            _item.amount = _cursor.getDouble(_cursorIndexOfAmount);
            if (_cursor.isNull(_cursorIndexOfType)) {
              _item.type = null;
            } else {
              _item.type = _cursor.getString(_cursorIndexOfType);
            }
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _item.category = null;
            } else {
              _item.category = _cursor.getString(_cursorIndexOfCategory);
            }
            if (_cursor.isNull(_cursorIndexOfCategoryIcon)) {
              _item.categoryIcon = null;
            } else {
              _item.categoryIcon = _cursor.getString(_cursorIndexOfCategoryIcon);
            }
            _item.categoryColor = _cursor.getInt(_cursorIndexOfCategoryColor);
            _item.date = _cursor.getLong(_cursorIndexOfDate);
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _item.note = null;
            } else {
              _item.note = _cursor.getString(_cursorIndexOfNote);
            }
            _item.createdAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<List<Transaction>> getByType(final String type) {
    final String _sql = "SELECT * FROM transactions WHERE type = ? ORDER BY date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (type == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, type);
    }
    return __db.getInvalidationTracker().createLiveData(new String[] {"transactions"}, false, new Callable<List<Transaction>>() {
      @Override
      @Nullable
      public List<Transaction> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfCategoryIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "category_icon");
          final int _cursorIndexOfCategoryColor = CursorUtil.getColumnIndexOrThrow(_cursor, "category_color");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final List<Transaction> _result = new ArrayList<Transaction>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Transaction _item;
            _item = new Transaction();
            _item.id = _cursor.getInt(_cursorIndexOfId);
            _item.amount = _cursor.getDouble(_cursorIndexOfAmount);
            if (_cursor.isNull(_cursorIndexOfType)) {
              _item.type = null;
            } else {
              _item.type = _cursor.getString(_cursorIndexOfType);
            }
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _item.category = null;
            } else {
              _item.category = _cursor.getString(_cursorIndexOfCategory);
            }
            if (_cursor.isNull(_cursorIndexOfCategoryIcon)) {
              _item.categoryIcon = null;
            } else {
              _item.categoryIcon = _cursor.getString(_cursorIndexOfCategoryIcon);
            }
            _item.categoryColor = _cursor.getInt(_cursorIndexOfCategoryColor);
            _item.date = _cursor.getLong(_cursorIndexOfDate);
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _item.note = null;
            } else {
              _item.note = _cursor.getString(_cursorIndexOfNote);
            }
            _item.createdAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<List<Transaction>> getByCategory(final String category) {
    final String _sql = "SELECT * FROM transactions WHERE category = ? ORDER BY date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (category == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, category);
    }
    return __db.getInvalidationTracker().createLiveData(new String[] {"transactions"}, false, new Callable<List<Transaction>>() {
      @Override
      @Nullable
      public List<Transaction> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfCategoryIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "category_icon");
          final int _cursorIndexOfCategoryColor = CursorUtil.getColumnIndexOrThrow(_cursor, "category_color");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final List<Transaction> _result = new ArrayList<Transaction>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Transaction _item;
            _item = new Transaction();
            _item.id = _cursor.getInt(_cursorIndexOfId);
            _item.amount = _cursor.getDouble(_cursorIndexOfAmount);
            if (_cursor.isNull(_cursorIndexOfType)) {
              _item.type = null;
            } else {
              _item.type = _cursor.getString(_cursorIndexOfType);
            }
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _item.category = null;
            } else {
              _item.category = _cursor.getString(_cursorIndexOfCategory);
            }
            if (_cursor.isNull(_cursorIndexOfCategoryIcon)) {
              _item.categoryIcon = null;
            } else {
              _item.categoryIcon = _cursor.getString(_cursorIndexOfCategoryIcon);
            }
            _item.categoryColor = _cursor.getInt(_cursorIndexOfCategoryColor);
            _item.date = _cursor.getLong(_cursorIndexOfDate);
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _item.note = null;
            } else {
              _item.note = _cursor.getString(_cursorIndexOfNote);
            }
            _item.createdAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<List<Transaction>> getByDateRange(final long startDate, final long endDate) {
    final String _sql = "SELECT * FROM transactions WHERE date BETWEEN ? AND ? ORDER BY date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startDate);
    _argIndex = 2;
    _statement.bindLong(_argIndex, endDate);
    return __db.getInvalidationTracker().createLiveData(new String[] {"transactions"}, false, new Callable<List<Transaction>>() {
      @Override
      @Nullable
      public List<Transaction> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfCategoryIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "category_icon");
          final int _cursorIndexOfCategoryColor = CursorUtil.getColumnIndexOrThrow(_cursor, "category_color");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final List<Transaction> _result = new ArrayList<Transaction>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Transaction _item;
            _item = new Transaction();
            _item.id = _cursor.getInt(_cursorIndexOfId);
            _item.amount = _cursor.getDouble(_cursorIndexOfAmount);
            if (_cursor.isNull(_cursorIndexOfType)) {
              _item.type = null;
            } else {
              _item.type = _cursor.getString(_cursorIndexOfType);
            }
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _item.category = null;
            } else {
              _item.category = _cursor.getString(_cursorIndexOfCategory);
            }
            if (_cursor.isNull(_cursorIndexOfCategoryIcon)) {
              _item.categoryIcon = null;
            } else {
              _item.categoryIcon = _cursor.getString(_cursorIndexOfCategoryIcon);
            }
            _item.categoryColor = _cursor.getInt(_cursorIndexOfCategoryColor);
            _item.date = _cursor.getLong(_cursorIndexOfDate);
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _item.note = null;
            } else {
              _item.note = _cursor.getString(_cursorIndexOfNote);
            }
            _item.createdAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<List<Transaction>> search(final String query) {
    final String _sql = "SELECT * FROM transactions WHERE (note LIKE '%' || ? || '%' OR category LIKE '%' || ? || '%') ORDER BY date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (query == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, query);
    }
    _argIndex = 2;
    if (query == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, query);
    }
    return __db.getInvalidationTracker().createLiveData(new String[] {"transactions"}, false, new Callable<List<Transaction>>() {
      @Override
      @Nullable
      public List<Transaction> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfCategoryIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "category_icon");
          final int _cursorIndexOfCategoryColor = CursorUtil.getColumnIndexOrThrow(_cursor, "category_color");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final List<Transaction> _result = new ArrayList<Transaction>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Transaction _item;
            _item = new Transaction();
            _item.id = _cursor.getInt(_cursorIndexOfId);
            _item.amount = _cursor.getDouble(_cursorIndexOfAmount);
            if (_cursor.isNull(_cursorIndexOfType)) {
              _item.type = null;
            } else {
              _item.type = _cursor.getString(_cursorIndexOfType);
            }
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _item.category = null;
            } else {
              _item.category = _cursor.getString(_cursorIndexOfCategory);
            }
            if (_cursor.isNull(_cursorIndexOfCategoryIcon)) {
              _item.categoryIcon = null;
            } else {
              _item.categoryIcon = _cursor.getString(_cursorIndexOfCategoryIcon);
            }
            _item.categoryColor = _cursor.getInt(_cursorIndexOfCategoryColor);
            _item.date = _cursor.getLong(_cursorIndexOfDate);
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _item.note = null;
            } else {
              _item.note = _cursor.getString(_cursorIndexOfNote);
            }
            _item.createdAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<Double> getTotalIncome() {
    final String _sql = "SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE type = 'INCOME'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"transactions"}, false, new Callable<Double>() {
      @Override
      @Nullable
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final Double _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getDouble(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<Double> getTotalExpense() {
    final String _sql = "SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE type = 'EXPENSE'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"transactions"}, false, new Callable<Double>() {
      @Override
      @Nullable
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final Double _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getDouble(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<Double> getBalance() {
    final String _sql = "SELECT COALESCE(SUM(CASE WHEN type='INCOME' THEN amount ELSE -amount END), 0) FROM transactions";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"transactions"}, false, new Callable<Double>() {
      @Override
      @Nullable
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final Double _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getDouble(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public double getMonthIncome(final long start, final long end) {
    final String _sql = "SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE type = 'INCOME' AND date BETWEEN ? AND ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, start);
    _argIndex = 2;
    _statement.bindLong(_argIndex, end);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final double _result;
      if (_cursor.moveToFirst()) {
        _result = _cursor.getDouble(0);
      } else {
        _result = 0.0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public double getMonthExpense(final long start, final long end) {
    final String _sql = "SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE type = 'EXPENSE' AND date BETWEEN ? AND ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, start);
    _argIndex = 2;
    _statement.bindLong(_argIndex, end);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final double _result;
      if (_cursor.moveToFirst()) {
        _result = _cursor.getDouble(0);
      } else {
        _result = 0.0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public LiveData<List<CategoryTotal>> getExpenseCategoryTotals() {
    final String _sql = "SELECT category, category_icon, category_color, SUM(amount) as total, COUNT(*) as transaction_count FROM transactions WHERE type = 'EXPENSE' GROUP BY category ORDER BY total DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"transactions"}, false, new Callable<List<CategoryTotal>>() {
      @Override
      @Nullable
      public List<CategoryTotal> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfCategory = 0;
          final int _cursorIndexOfCategoryIcon = 1;
          final int _cursorIndexOfCategoryColor = 2;
          final int _cursorIndexOfTotal = 3;
          final int _cursorIndexOfTransactionCount = 4;
          final List<CategoryTotal> _result = new ArrayList<CategoryTotal>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CategoryTotal _item;
            _item = new CategoryTotal();
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _item.category = null;
            } else {
              _item.category = _cursor.getString(_cursorIndexOfCategory);
            }
            if (_cursor.isNull(_cursorIndexOfCategoryIcon)) {
              _item.categoryIcon = null;
            } else {
              _item.categoryIcon = _cursor.getString(_cursorIndexOfCategoryIcon);
            }
            _item.categoryColor = _cursor.getInt(_cursorIndexOfCategoryColor);
            _item.total = _cursor.getDouble(_cursorIndexOfTotal);
            _item.transactionCount = _cursor.getInt(_cursorIndexOfTransactionCount);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<List<CategoryTotal>> getMonthCategoryTotals(final long start, final long end) {
    final String _sql = "SELECT category, category_icon, category_color, SUM(amount) as total, COUNT(*) as transaction_count FROM transactions WHERE type = 'EXPENSE' AND date BETWEEN ? AND ? GROUP BY category ORDER BY total DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, start);
    _argIndex = 2;
    _statement.bindLong(_argIndex, end);
    return __db.getInvalidationTracker().createLiveData(new String[] {"transactions"}, false, new Callable<List<CategoryTotal>>() {
      @Override
      @Nullable
      public List<CategoryTotal> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfCategory = 0;
          final int _cursorIndexOfCategoryIcon = 1;
          final int _cursorIndexOfCategoryColor = 2;
          final int _cursorIndexOfTotal = 3;
          final int _cursorIndexOfTransactionCount = 4;
          final List<CategoryTotal> _result = new ArrayList<CategoryTotal>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CategoryTotal _item;
            _item = new CategoryTotal();
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _item.category = null;
            } else {
              _item.category = _cursor.getString(_cursorIndexOfCategory);
            }
            if (_cursor.isNull(_cursorIndexOfCategoryIcon)) {
              _item.categoryIcon = null;
            } else {
              _item.categoryIcon = _cursor.getString(_cursorIndexOfCategoryIcon);
            }
            _item.categoryColor = _cursor.getInt(_cursorIndexOfCategoryColor);
            _item.total = _cursor.getDouble(_cursorIndexOfTotal);
            _item.transactionCount = _cursor.getInt(_cursorIndexOfTransactionCount);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public List<CategoryTotal> getExpenseCategoryTotalsSync() {
    final String _sql = "SELECT category, category_icon, category_color, SUM(amount) as total, COUNT(*) as transaction_count FROM transactions WHERE type = 'EXPENSE' GROUP BY category ORDER BY total DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfCategory = 0;
      final int _cursorIndexOfCategoryIcon = 1;
      final int _cursorIndexOfCategoryColor = 2;
      final int _cursorIndexOfTotal = 3;
      final int _cursorIndexOfTransactionCount = 4;
      final List<CategoryTotal> _result = new ArrayList<CategoryTotal>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final CategoryTotal _item;
        _item = new CategoryTotal();
        if (_cursor.isNull(_cursorIndexOfCategory)) {
          _item.category = null;
        } else {
          _item.category = _cursor.getString(_cursorIndexOfCategory);
        }
        if (_cursor.isNull(_cursorIndexOfCategoryIcon)) {
          _item.categoryIcon = null;
        } else {
          _item.categoryIcon = _cursor.getString(_cursorIndexOfCategoryIcon);
        }
        _item.categoryColor = _cursor.getInt(_cursorIndexOfCategoryColor);
        _item.total = _cursor.getDouble(_cursorIndexOfTotal);
        _item.transactionCount = _cursor.getInt(_cursorIndexOfTransactionCount);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public LiveData<List<MonthlyTotal>> getMonthlyTotals() {
    final String _sql = "SELECT   strftime('%Y-%m', date/1000, 'unixepoch') as month_key,   strftime('%m', date/1000, 'unixepoch') as month_label,   SUM(CASE WHEN type='INCOME'  THEN amount ELSE 0 END) as income,   SUM(CASE WHEN type='EXPENSE' THEN amount ELSE 0 END) as expense FROM transactions GROUP BY month_key ORDER BY month_key DESC LIMIT 6";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"transactions"}, false, new Callable<List<MonthlyTotal>>() {
      @Override
      @Nullable
      public List<MonthlyTotal> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfMonthKey = 0;
          final int _cursorIndexOfMonthLabel = 1;
          final int _cursorIndexOfIncome = 2;
          final int _cursorIndexOfExpense = 3;
          final List<MonthlyTotal> _result = new ArrayList<MonthlyTotal>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MonthlyTotal _item;
            _item = new MonthlyTotal();
            if (_cursor.isNull(_cursorIndexOfMonthKey)) {
              _item.monthKey = null;
            } else {
              _item.monthKey = _cursor.getString(_cursorIndexOfMonthKey);
            }
            if (_cursor.isNull(_cursorIndexOfMonthLabel)) {
              _item.monthLabel = null;
            } else {
              _item.monthLabel = _cursor.getString(_cursorIndexOfMonthLabel);
            }
            _item.income = _cursor.getDouble(_cursorIndexOfIncome);
            _item.expense = _cursor.getDouble(_cursorIndexOfExpense);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public double getCategorySpend(final String category, final long start, final long end) {
    final String _sql = "SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE type = 'EXPENSE' AND category = ? AND date BETWEEN ? AND ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    if (category == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, category);
    }
    _argIndex = 2;
    _statement.bindLong(_argIndex, start);
    _argIndex = 3;
    _statement.bindLong(_argIndex, end);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final double _result;
      if (_cursor.moveToFirst()) {
        _result = _cursor.getDouble(0);
      } else {
        _result = 0.0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public double getTotalSpend(final long start, final long end) {
    final String _sql = "SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE type = 'EXPENSE' AND date BETWEEN ? AND ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, start);
    _argIndex = 2;
    _statement.bindLong(_argIndex, end);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final double _result;
      if (_cursor.moveToFirst()) {
        _result = _cursor.getDouble(0);
      } else {
        _result = 0.0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public LiveData<Double> getWeekExpense(final long start, final long end) {
    final String _sql = "SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE type = 'EXPENSE' AND date BETWEEN ? AND ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, start);
    _argIndex = 2;
    _statement.bindLong(_argIndex, end);
    return __db.getInvalidationTracker().createLiveData(new String[] {"transactions"}, false, new Callable<Double>() {
      @Override
      @Nullable
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final Double _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getDouble(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
