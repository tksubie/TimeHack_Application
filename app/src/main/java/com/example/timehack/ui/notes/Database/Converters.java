package com.example.timehack.ui.notes.Database;

import androidx.room.TypeConverter;

import java.util.Date;

//converters for date from Android Studio Documentation for a timestamp entry into database
//https://developer.android.com/training/data-storage/room/referencing-data#java

public class Converters {
        @TypeConverter
        public static Date fromTimestamp(Long value) {
            return value == null ? null : new Date(value);
        }

        @TypeConverter
        public static Long dateToTimestamp(Date date) {
            return date == null ? null : date.getTime();
        }
}
