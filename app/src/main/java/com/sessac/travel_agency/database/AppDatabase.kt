package com.sessac.travel_agency.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sessac.travel_agency.data.GuideItem
import com.sessac.travel_agency.data.GuideScheduleItem
import com.sessac.travel_agency.data.LodgingItem
import com.sessac.travel_agency.data.PackageItem
import com.sessac.travel_agency.data.ScheduleItem

@Database(
    entities = [GuideItem::class, GuideScheduleItem::class, LodgingItem::class, PackageItem::class, ScheduleItem::class],
    version = 2
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun guideDao(): GuideDao
    abstract fun guideScheduleDao(): GuideScheduleDao
    abstract fun lodgingDao(): LodgingDao
    abstract fun packageDao(): PackageDao
    abstract fun scheduleDao(): ScheduleDao

    companion object {
        // tutor pyo lateinit
        private lateinit var instance : AppDatabase

        fun getDatabase(context: Context): AppDatabase {
            if(!::instance.isInitialized){
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "travelAgency.db"
                ).fallbackToDestructiveMigrationFrom(1, 2)
                    .build()
            }
            return instance
        }
    }
}
