package com.sessac.travel_agency.helper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TravelAgencyOpenHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, CURRENT_DB_VERSION) {
    companion object {
        const val DB_NAME = "travelAgency.db"
        const val CURRENT_DB_VERSION = 1
        const val TABLE_PACKAGE = "package"
        const val TABLE_LODGING = "lodging"
        const val TABLE_GUIDE = "guide"
        const val TABLE_GUIDE_SCHEDULE = "guide_schedule"
        const val TABLE_SCHEDULE = "schedule"

        const val CREATE_PACKAGE_TABLE = """CREATE TABLE $TABLE_PACKAGE (
           packageId INTEGER PRIMARY KEY AUTOINCREMENT,
           guideInfo INTEGER,
           area TEXT,
           pName TEXT,
           pImage TEXT,
           status INTEGER,
           pStartDate INTEGER,
           pEndDate INTEGER
           );"""

//        const val CREATE_PACKAGE_TABLE = """CREATE TABLE $TABLE_PACKAGE (
//           packageId INTEGER PRIMARY KEY AUTOINCREMENT,
//           guideInfo INTEGER,
//           area TEXT,
//           pName TEXT,
//           pImage TEXT,
//           status INTEGER,
//           pStartDate DATE,
//           pEndDate DATE
//           );"""

        const val CREATE_LODGING_TABLE = """CREATE TABLE $TABLE_LODGING (
          lodgeId INTEGER PRIMARY KEY AUTOINCREMENT,
          area TEXT,
          lName TEXT,
          lImage INTEGER,
          starNum INTEGER 
          );"""

        const val CREATE_GUIDE_TABLE = """CREATE TABLE $TABLE_GUIDE (
          guideId INTEGER PRIMARY KEY AUTOINCREMENT,
          gName TEXT,
          gImage TEXT
          );"""

        const val CREATE_GUIDE_SCHEDULE_TABLE = """CREATE TABLE $TABLE_GUIDE_SCHEDULE (
          guideScheduleId INTEGER PRIMARY KEY AUTOINCREMENT,
          guideInfo GuideItem,
          packageInfo PackageItem
          );"""

        const val CREATE_SCHEDULE_TABLE = """CREATE TABLE $TABLE_SCHEDULE (
          scheduleId INTEGER PRIMARY KEY AUTOINCREMENT,
          lodgingInfo LodgingItem,
          theme TEXT,
          detail TEXT,
          day INTEGER
          );"""
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_PACKAGE_TABLE)
        db.execSQL(CREATE_LODGING_TABLE)
        db.execSQL(CREATE_GUIDE_TABLE)
        db.execSQL(CREATE_GUIDE_SCHEDULE_TABLE)
        db.execSQL(CREATE_SCHEDULE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PACKAGE")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_LODGING")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_GUIDE")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_GUIDE_SCHEDULE")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SCHEDULE")
        onCreate(db)
    }
}
