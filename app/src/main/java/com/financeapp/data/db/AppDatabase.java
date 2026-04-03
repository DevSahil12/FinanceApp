package com.financeapp.data.db;

import android.content.Context;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.financeapp.data.model.Goal;
import com.financeapp.data.model.Transaction;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Transaction.class, Goal.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;
    public static final ExecutorService DB_EXECUTOR = Executors.newFixedThreadPool(4);

    public abstract TransactionDao transactionDao();
    public abstract GoalDao goalDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "finance_db"
                    )
                    .addCallback(new RoomDatabase.Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            DB_EXECUTOR.execute(() -> seedSampleData(INSTANCE));
                        }
                    })
                    .build();
                }
            }
        }
        return INSTANCE;
    }

    private static void seedSampleData(AppDatabase db) {
        TransactionDao dao = db.transactionDao();
        long now = System.currentTimeMillis();
        long day = 86400000L;

        // Sample income
        dao.insert(new Transaction(45000, "INCOME", "Salary", "💼", Color.parseColor("#4CAF50"), now - day, "Monthly salary"));
        dao.insert(new Transaction(5000, "INCOME", "Freelance", "💻", Color.parseColor("#2196F3"), now - 5 * day, "Website project"));
        dao.insert(new Transaction(2000, "INCOME", "Other", "💰", Color.parseColor("#9C27B0"), now - 10 * day, "Bonus"));

        // Sample expenses
        dao.insert(new Transaction(3200, "EXPENSE", "Food", "🍔", Color.parseColor("#FF5722"), now - day, "Groceries"));
        dao.insert(new Transaction(800, "EXPENSE", "Transport", "🚌", Color.parseColor("#FF9800"), now - 2 * day, "Metro pass"));
        dao.insert(new Transaction(1500, "EXPENSE", "Shopping", "🛍️", Color.parseColor("#E91E63"), now - 3 * day, "Clothes"));
        dao.insert(new Transaction(2000, "EXPENSE", "Bills", "📄", Color.parseColor("#607D8B"), now - 4 * day, "Electricity + internet"));
        dao.insert(new Transaction(500, "EXPENSE", "Entertainment", "🎬", Color.parseColor("#9C27B0"), now - 5 * day, "Movie + dinner"));
        dao.insert(new Transaction(300, "EXPENSE", "Health", "💊", Color.parseColor("#00BCD4"), now - 7 * day, "Medicines"));
        dao.insert(new Transaction(1200, "EXPENSE", "Food", "🍔", Color.parseColor("#FF5722"), now - 8 * day, "Restaurant dinner"));
        dao.insert(new Transaction(600, "EXPENSE", "Transport", "🚌", Color.parseColor("#FF9800"), now - 9 * day, "Cab rides"));
        dao.insert(new Transaction(900, "EXPENSE", "Entertainment", "🎬", Color.parseColor("#9C27B0"), now - 12 * day, "OTT subscriptions"));
        dao.insert(new Transaction(2500, "EXPENSE", "Shopping", "🛍️", Color.parseColor("#E91E63"), now - 15 * day, "Electronics"));
        dao.insert(new Transaction(400, "EXPENSE", "Health", "💊", Color.parseColor("#00BCD4"), now - 16 * day, "Doctor visit"));
        dao.insert(new Transaction(1800, "EXPENSE", "Bills", "📄", Color.parseColor("#607D8B"), now - 20 * day, "Rent partial"));
        dao.insert(new Transaction(700, "EXPENSE", "Food", "🍔", Color.parseColor("#FF5722"), now - 22 * day, "Takeout orders"));
        dao.insert(new Transaction(42000, "INCOME", "Salary", "💼", Color.parseColor("#4CAF50"), now - 30 * day, "Previous month salary"));
        dao.insert(new Transaction(3100, "EXPENSE", "Food", "🍔", Color.parseColor("#FF5722"), now - 32 * day, "Monthly groceries"));
        dao.insert(new Transaction(850, "EXPENSE", "Transport", "🚌", Color.parseColor("#FF9800"), now - 33 * day, "Transport"));
        dao.insert(new Transaction(2200, "EXPENSE", "Bills", "📄", Color.parseColor("#607D8B"), now - 35 * day, "Utilities"));
        dao.insert(new Transaction(1100, "EXPENSE", "Entertainment", "🎬", Color.parseColor("#9C27B0"), now - 38 * day, "Weekend outing"));
        dao.insert(new Transaction(3500, "EXPENSE", "Shopping", "🛍️", Color.parseColor("#E91E63"), now - 40 * day, "Monthly shopping"));

        // Sample goal
        Goal vacationGoal = new Goal("Vacation Fund", 50000, 0, "✈️", Color.parseColor("#3F51B5"));
        vacationGoal.savedAmount = 18500;
        db.goalDao().insert(vacationGoal);

        Goal emergencyGoal = new Goal("Emergency Fund", 100000, 0, "🏦", Color.parseColor("#009688"));
        emergencyGoal.savedAmount = 35000;
        db.goalDao().insert(emergencyGoal);
    }
}
