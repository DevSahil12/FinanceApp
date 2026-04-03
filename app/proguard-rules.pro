# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keepclassmembers @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao interface *

# MPAndroidChart
-keep class com.github.mikephil.charting.** { *; }

# Models
-keep class com.financeapp.data.model.** { *; }

# ViewBinding
-keep class com.financeapp.databinding.** { *; }
