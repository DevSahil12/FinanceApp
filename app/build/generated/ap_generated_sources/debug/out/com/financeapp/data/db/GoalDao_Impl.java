package com.financeapp.data.db;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.financeapp.data.model.Goal;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class GoalDao_Impl implements GoalDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Goal> __insertionAdapterOfGoal;

  private final EntityDeletionOrUpdateAdapter<Goal> __deletionAdapterOfGoal;

  private final EntityDeletionOrUpdateAdapter<Goal> __updateAdapterOfGoal;

  private final SharedSQLiteStatement __preparedStmtOfUpdateSavedAmount;

  private final SharedSQLiteStatement __preparedStmtOfUpdateStreak;

  private final SharedSQLiteStatement __preparedStmtOfMarkCompleted;

  public GoalDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfGoal = new EntityInsertionAdapter<Goal>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `goals` (`id`,`title`,`target_amount`,`saved_amount`,`deadline`,`icon`,`color`,`is_completed`,`created_at`,`is_budget_challenge`,`budget_category`,`budget_month`,`current_streak`,`longest_streak`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Goal entity) {
        statement.bindLong(1, entity.id);
        if (entity.title == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.title);
        }
        statement.bindDouble(3, entity.targetAmount);
        statement.bindDouble(4, entity.savedAmount);
        statement.bindLong(5, entity.deadline);
        if (entity.icon == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.icon);
        }
        statement.bindLong(7, entity.color);
        final int _tmp = entity.isCompleted ? 1 : 0;
        statement.bindLong(8, _tmp);
        statement.bindLong(9, entity.createdAt);
        final int _tmp_1 = entity.isBudgetChallenge ? 1 : 0;
        statement.bindLong(10, _tmp_1);
        if (entity.budgetCategory == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.budgetCategory);
        }
        if (entity.budgetMonth == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.budgetMonth);
        }
        statement.bindLong(13, entity.currentStreak);
        statement.bindLong(14, entity.longestStreak);
      }
    };
    this.__deletionAdapterOfGoal = new EntityDeletionOrUpdateAdapter<Goal>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `goals` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Goal entity) {
        statement.bindLong(1, entity.id);
      }
    };
    this.__updateAdapterOfGoal = new EntityDeletionOrUpdateAdapter<Goal>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `goals` SET `id` = ?,`title` = ?,`target_amount` = ?,`saved_amount` = ?,`deadline` = ?,`icon` = ?,`color` = ?,`is_completed` = ?,`created_at` = ?,`is_budget_challenge` = ?,`budget_category` = ?,`budget_month` = ?,`current_streak` = ?,`longest_streak` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Goal entity) {
        statement.bindLong(1, entity.id);
        if (entity.title == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.title);
        }
        statement.bindDouble(3, entity.targetAmount);
        statement.bindDouble(4, entity.savedAmount);
        statement.bindLong(5, entity.deadline);
        if (entity.icon == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.icon);
        }
        statement.bindLong(7, entity.color);
        final int _tmp = entity.isCompleted ? 1 : 0;
        statement.bindLong(8, _tmp);
        statement.bindLong(9, entity.createdAt);
        final int _tmp_1 = entity.isBudgetChallenge ? 1 : 0;
        statement.bindLong(10, _tmp_1);
        if (entity.budgetCategory == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.budgetCategory);
        }
        if (entity.budgetMonth == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.budgetMonth);
        }
        statement.bindLong(13, entity.currentStreak);
        statement.bindLong(14, entity.longestStreak);
        statement.bindLong(15, entity.id);
      }
    };
    this.__preparedStmtOfUpdateSavedAmount = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE goals SET saved_amount = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateStreak = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE goals SET current_streak = ?, longest_streak = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfMarkCompleted = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE goals SET is_completed = 1 WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public long insert(final Goal goal) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfGoal.insertAndReturnId(goal);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final Goal goal) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfGoal.handle(goal);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final Goal goal) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfGoal.handle(goal);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateSavedAmount(final int id, final double amount) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateSavedAmount.acquire();
    int _argIndex = 1;
    _stmt.bindDouble(_argIndex, amount);
    _argIndex = 2;
    _stmt.bindLong(_argIndex, id);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfUpdateSavedAmount.release(_stmt);
    }
  }

  @Override
  public void updateStreak(final int id, final int streak, final int longest) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateStreak.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, streak);
    _argIndex = 2;
    _stmt.bindLong(_argIndex, longest);
    _argIndex = 3;
    _stmt.bindLong(_argIndex, id);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfUpdateStreak.release(_stmt);
    }
  }

  @Override
  public void markCompleted(final int id) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfMarkCompleted.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, id);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfMarkCompleted.release(_stmt);
    }
  }

  @Override
  public LiveData<List<Goal>> getAllGoals() {
    final String _sql = "SELECT * FROM goals ORDER BY created_at DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"goals"}, false, new Callable<List<Goal>>() {
      @Override
      @Nullable
      public List<Goal> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfTargetAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "target_amount");
          final int _cursorIndexOfSavedAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "saved_amount");
          final int _cursorIndexOfDeadline = CursorUtil.getColumnIndexOrThrow(_cursor, "deadline");
          final int _cursorIndexOfIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "icon");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "is_completed");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfIsBudgetChallenge = CursorUtil.getColumnIndexOrThrow(_cursor, "is_budget_challenge");
          final int _cursorIndexOfBudgetCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "budget_category");
          final int _cursorIndexOfBudgetMonth = CursorUtil.getColumnIndexOrThrow(_cursor, "budget_month");
          final int _cursorIndexOfCurrentStreak = CursorUtil.getColumnIndexOrThrow(_cursor, "current_streak");
          final int _cursorIndexOfLongestStreak = CursorUtil.getColumnIndexOrThrow(_cursor, "longest_streak");
          final List<Goal> _result = new ArrayList<Goal>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Goal _item;
            _item = new Goal();
            _item.id = _cursor.getInt(_cursorIndexOfId);
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _item.title = null;
            } else {
              _item.title = _cursor.getString(_cursorIndexOfTitle);
            }
            _item.targetAmount = _cursor.getDouble(_cursorIndexOfTargetAmount);
            _item.savedAmount = _cursor.getDouble(_cursorIndexOfSavedAmount);
            _item.deadline = _cursor.getLong(_cursorIndexOfDeadline);
            if (_cursor.isNull(_cursorIndexOfIcon)) {
              _item.icon = null;
            } else {
              _item.icon = _cursor.getString(_cursorIndexOfIcon);
            }
            _item.color = _cursor.getInt(_cursorIndexOfColor);
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCompleted);
            _item.isCompleted = _tmp != 0;
            _item.createdAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsBudgetChallenge);
            _item.isBudgetChallenge = _tmp_1 != 0;
            if (_cursor.isNull(_cursorIndexOfBudgetCategory)) {
              _item.budgetCategory = null;
            } else {
              _item.budgetCategory = _cursor.getString(_cursorIndexOfBudgetCategory);
            }
            if (_cursor.isNull(_cursorIndexOfBudgetMonth)) {
              _item.budgetMonth = null;
            } else {
              _item.budgetMonth = _cursor.getString(_cursorIndexOfBudgetMonth);
            }
            _item.currentStreak = _cursor.getInt(_cursorIndexOfCurrentStreak);
            _item.longestStreak = _cursor.getInt(_cursorIndexOfLongestStreak);
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
  public LiveData<List<Goal>> getSavingsGoals() {
    final String _sql = "SELECT * FROM goals WHERE is_budget_challenge = 0 ORDER BY created_at DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"goals"}, false, new Callable<List<Goal>>() {
      @Override
      @Nullable
      public List<Goal> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfTargetAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "target_amount");
          final int _cursorIndexOfSavedAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "saved_amount");
          final int _cursorIndexOfDeadline = CursorUtil.getColumnIndexOrThrow(_cursor, "deadline");
          final int _cursorIndexOfIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "icon");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "is_completed");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfIsBudgetChallenge = CursorUtil.getColumnIndexOrThrow(_cursor, "is_budget_challenge");
          final int _cursorIndexOfBudgetCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "budget_category");
          final int _cursorIndexOfBudgetMonth = CursorUtil.getColumnIndexOrThrow(_cursor, "budget_month");
          final int _cursorIndexOfCurrentStreak = CursorUtil.getColumnIndexOrThrow(_cursor, "current_streak");
          final int _cursorIndexOfLongestStreak = CursorUtil.getColumnIndexOrThrow(_cursor, "longest_streak");
          final List<Goal> _result = new ArrayList<Goal>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Goal _item;
            _item = new Goal();
            _item.id = _cursor.getInt(_cursorIndexOfId);
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _item.title = null;
            } else {
              _item.title = _cursor.getString(_cursorIndexOfTitle);
            }
            _item.targetAmount = _cursor.getDouble(_cursorIndexOfTargetAmount);
            _item.savedAmount = _cursor.getDouble(_cursorIndexOfSavedAmount);
            _item.deadline = _cursor.getLong(_cursorIndexOfDeadline);
            if (_cursor.isNull(_cursorIndexOfIcon)) {
              _item.icon = null;
            } else {
              _item.icon = _cursor.getString(_cursorIndexOfIcon);
            }
            _item.color = _cursor.getInt(_cursorIndexOfColor);
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCompleted);
            _item.isCompleted = _tmp != 0;
            _item.createdAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsBudgetChallenge);
            _item.isBudgetChallenge = _tmp_1 != 0;
            if (_cursor.isNull(_cursorIndexOfBudgetCategory)) {
              _item.budgetCategory = null;
            } else {
              _item.budgetCategory = _cursor.getString(_cursorIndexOfBudgetCategory);
            }
            if (_cursor.isNull(_cursorIndexOfBudgetMonth)) {
              _item.budgetMonth = null;
            } else {
              _item.budgetMonth = _cursor.getString(_cursorIndexOfBudgetMonth);
            }
            _item.currentStreak = _cursor.getInt(_cursorIndexOfCurrentStreak);
            _item.longestStreak = _cursor.getInt(_cursorIndexOfLongestStreak);
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
  public LiveData<List<Goal>> getBudgetChallenges() {
    final String _sql = "SELECT * FROM goals WHERE is_budget_challenge = 1 ORDER BY created_at DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"goals"}, false, new Callable<List<Goal>>() {
      @Override
      @Nullable
      public List<Goal> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfTargetAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "target_amount");
          final int _cursorIndexOfSavedAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "saved_amount");
          final int _cursorIndexOfDeadline = CursorUtil.getColumnIndexOrThrow(_cursor, "deadline");
          final int _cursorIndexOfIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "icon");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "is_completed");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfIsBudgetChallenge = CursorUtil.getColumnIndexOrThrow(_cursor, "is_budget_challenge");
          final int _cursorIndexOfBudgetCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "budget_category");
          final int _cursorIndexOfBudgetMonth = CursorUtil.getColumnIndexOrThrow(_cursor, "budget_month");
          final int _cursorIndexOfCurrentStreak = CursorUtil.getColumnIndexOrThrow(_cursor, "current_streak");
          final int _cursorIndexOfLongestStreak = CursorUtil.getColumnIndexOrThrow(_cursor, "longest_streak");
          final List<Goal> _result = new ArrayList<Goal>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Goal _item;
            _item = new Goal();
            _item.id = _cursor.getInt(_cursorIndexOfId);
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _item.title = null;
            } else {
              _item.title = _cursor.getString(_cursorIndexOfTitle);
            }
            _item.targetAmount = _cursor.getDouble(_cursorIndexOfTargetAmount);
            _item.savedAmount = _cursor.getDouble(_cursorIndexOfSavedAmount);
            _item.deadline = _cursor.getLong(_cursorIndexOfDeadline);
            if (_cursor.isNull(_cursorIndexOfIcon)) {
              _item.icon = null;
            } else {
              _item.icon = _cursor.getString(_cursorIndexOfIcon);
            }
            _item.color = _cursor.getInt(_cursorIndexOfColor);
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCompleted);
            _item.isCompleted = _tmp != 0;
            _item.createdAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsBudgetChallenge);
            _item.isBudgetChallenge = _tmp_1 != 0;
            if (_cursor.isNull(_cursorIndexOfBudgetCategory)) {
              _item.budgetCategory = null;
            } else {
              _item.budgetCategory = _cursor.getString(_cursorIndexOfBudgetCategory);
            }
            if (_cursor.isNull(_cursorIndexOfBudgetMonth)) {
              _item.budgetMonth = null;
            } else {
              _item.budgetMonth = _cursor.getString(_cursorIndexOfBudgetMonth);
            }
            _item.currentStreak = _cursor.getInt(_cursorIndexOfCurrentStreak);
            _item.longestStreak = _cursor.getInt(_cursorIndexOfLongestStreak);
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
  public Goal getGoalByIdSync(final int id) {
    final String _sql = "SELECT * FROM goals WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfTargetAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "target_amount");
      final int _cursorIndexOfSavedAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "saved_amount");
      final int _cursorIndexOfDeadline = CursorUtil.getColumnIndexOrThrow(_cursor, "deadline");
      final int _cursorIndexOfIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "icon");
      final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
      final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "is_completed");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
      final int _cursorIndexOfIsBudgetChallenge = CursorUtil.getColumnIndexOrThrow(_cursor, "is_budget_challenge");
      final int _cursorIndexOfBudgetCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "budget_category");
      final int _cursorIndexOfBudgetMonth = CursorUtil.getColumnIndexOrThrow(_cursor, "budget_month");
      final int _cursorIndexOfCurrentStreak = CursorUtil.getColumnIndexOrThrow(_cursor, "current_streak");
      final int _cursorIndexOfLongestStreak = CursorUtil.getColumnIndexOrThrow(_cursor, "longest_streak");
      final Goal _result;
      if (_cursor.moveToFirst()) {
        _result = new Goal();
        _result.id = _cursor.getInt(_cursorIndexOfId);
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _result.title = null;
        } else {
          _result.title = _cursor.getString(_cursorIndexOfTitle);
        }
        _result.targetAmount = _cursor.getDouble(_cursorIndexOfTargetAmount);
        _result.savedAmount = _cursor.getDouble(_cursorIndexOfSavedAmount);
        _result.deadline = _cursor.getLong(_cursorIndexOfDeadline);
        if (_cursor.isNull(_cursorIndexOfIcon)) {
          _result.icon = null;
        } else {
          _result.icon = _cursor.getString(_cursorIndexOfIcon);
        }
        _result.color = _cursor.getInt(_cursorIndexOfColor);
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsCompleted);
        _result.isCompleted = _tmp != 0;
        _result.createdAt = _cursor.getLong(_cursorIndexOfCreatedAt);
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfIsBudgetChallenge);
        _result.isBudgetChallenge = _tmp_1 != 0;
        if (_cursor.isNull(_cursorIndexOfBudgetCategory)) {
          _result.budgetCategory = null;
        } else {
          _result.budgetCategory = _cursor.getString(_cursorIndexOfBudgetCategory);
        }
        if (_cursor.isNull(_cursorIndexOfBudgetMonth)) {
          _result.budgetMonth = null;
        } else {
          _result.budgetMonth = _cursor.getString(_cursorIndexOfBudgetMonth);
        }
        _result.currentStreak = _cursor.getInt(_cursorIndexOfCurrentStreak);
        _result.longestStreak = _cursor.getInt(_cursorIndexOfLongestStreak);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Goal> getChallengesForMonth(final String month) {
    final String _sql = "SELECT * FROM goals WHERE is_budget_challenge = 1 AND budget_month = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (month == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, month);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfTargetAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "target_amount");
      final int _cursorIndexOfSavedAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "saved_amount");
      final int _cursorIndexOfDeadline = CursorUtil.getColumnIndexOrThrow(_cursor, "deadline");
      final int _cursorIndexOfIcon = CursorUtil.getColumnIndexOrThrow(_cursor, "icon");
      final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
      final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "is_completed");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
      final int _cursorIndexOfIsBudgetChallenge = CursorUtil.getColumnIndexOrThrow(_cursor, "is_budget_challenge");
      final int _cursorIndexOfBudgetCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "budget_category");
      final int _cursorIndexOfBudgetMonth = CursorUtil.getColumnIndexOrThrow(_cursor, "budget_month");
      final int _cursorIndexOfCurrentStreak = CursorUtil.getColumnIndexOrThrow(_cursor, "current_streak");
      final int _cursorIndexOfLongestStreak = CursorUtil.getColumnIndexOrThrow(_cursor, "longest_streak");
      final List<Goal> _result = new ArrayList<Goal>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Goal _item;
        _item = new Goal();
        _item.id = _cursor.getInt(_cursorIndexOfId);
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _item.title = null;
        } else {
          _item.title = _cursor.getString(_cursorIndexOfTitle);
        }
        _item.targetAmount = _cursor.getDouble(_cursorIndexOfTargetAmount);
        _item.savedAmount = _cursor.getDouble(_cursorIndexOfSavedAmount);
        _item.deadline = _cursor.getLong(_cursorIndexOfDeadline);
        if (_cursor.isNull(_cursorIndexOfIcon)) {
          _item.icon = null;
        } else {
          _item.icon = _cursor.getString(_cursorIndexOfIcon);
        }
        _item.color = _cursor.getInt(_cursorIndexOfColor);
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsCompleted);
        _item.isCompleted = _tmp != 0;
        _item.createdAt = _cursor.getLong(_cursorIndexOfCreatedAt);
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfIsBudgetChallenge);
        _item.isBudgetChallenge = _tmp_1 != 0;
        if (_cursor.isNull(_cursorIndexOfBudgetCategory)) {
          _item.budgetCategory = null;
        } else {
          _item.budgetCategory = _cursor.getString(_cursorIndexOfBudgetCategory);
        }
        if (_cursor.isNull(_cursorIndexOfBudgetMonth)) {
          _item.budgetMonth = null;
        } else {
          _item.budgetMonth = _cursor.getString(_cursorIndexOfBudgetMonth);
        }
        _item.currentStreak = _cursor.getInt(_cursorIndexOfCurrentStreak);
        _item.longestStreak = _cursor.getInt(_cursorIndexOfLongestStreak);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
