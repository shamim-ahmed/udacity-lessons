/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.sunshine.app.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;
import android.util.Log;

/**
 * Defines table and column names for the weather database.
 */
public class WeatherContract {
    public static final String CONTENT_AUTHORITY = "com.example.android.sunshine.app";

    // To make it easy to query for the exact date, we normalize all dates that go into
    // the database to the start of the the Julian day at UTC.
    public static long normalizeDate(long startDate) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    /*
        Inner class that defines the table contents of the location table
        Students: This is where you will add the strings.  (Similar to what has been
        done for WeatherEntry)
     */
    public static final class LocationEntry implements BaseColumns {
        public static final String TABLE_NAME = "location";
        public static final String COLUMN_CITY_NAME = "city";
        public static final String COLUMN_COORD_LAT = "lat" ;
        public static final String COLUMN_COORD_LONG = "long";
        public static final String COLUMN_LOCATION_SETTING = "loc_setting";
        public static final String CONTENT_PATH = "location";
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(CONTENT_PATH).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + CONTENT_PATH;

        public static Uri buildLocationrUri(long id) {
            return CONTENT_URI.buildUpon().appendPath(Long.toString(id)).build();
        }
    }

    /* Inner class that defines the table contents of the weather table */
    public static final class WeatherEntry implements BaseColumns {

        public static final String TABLE_NAME = "weather";

        // Column with the foreign key into the location table.
        public static final String COLUMN_LOC_KEY = "location_id";
        // Date, stored as long in milliseconds since the epoch
        public static final String COLUMN_DATE = "date";
        // Weather id as returned by API, to identify the icon to be used
        public static final String COLUMN_WEATHER_ID = "weather_id";

        // Short description and long description of the weather, as provided by API.
        // e.g "clear" vs "sky is clear".
        public static final String COLUMN_SHORT_DESC = "short_desc";

        // Min and max temperatures for the day (stored as floats)
        public static final String COLUMN_MIN_TEMP = "min";
        public static final String COLUMN_MAX_TEMP = "max";

        // Humidity is stored as a float representing percentage
        public static final String COLUMN_HUMIDITY = "humidity";

        // Humidity is stored as a float representing percentage
        public static final String COLUMN_PRESSURE = "pressure";

        // Windspeed is stored as a float representing windspeed  mph
        public static final String COLUMN_WIND_SPEED = "wind";

        // Degrees are meteorological degrees (e.g, 0 is north, 180 is south).  Stored as floats.
        public static final String COLUMN_DEGREES = "degrees";
        public static final String CONTENT_PATH = "weather";
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + WeatherEntry.CONTENT_PATH;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + WeatherEntry.CONTENT_PATH + "/" + LocationEntry.CONTENT_PATH + "/date";


        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(CONTENT_PATH).build();

        public static String getLocationSettingFromUri(Uri uri) {
            return uri.getLastPathSegment();
        }

        public static long getDateFromUri(Uri uri) {
            long result = 0;
            String dateValue = uri.getLastPathSegment();

            try {
                result = Long.parseLong(dateValue);
            } catch (Exception ex) {
                Log.w(WeatherEntry.class.getSimpleName(), "error while parsing date from uri");
            }

            return result;
        }

        public static long getStartDateFromUri(Uri uri) {
            long result = 0;
            String startDateValue = uri.getQueryParameter("date");

            if (startDateValue != null) {
                result = Long.parseLong(startDateValue);
            }

            return result;
        }

        public static Uri buildWeatherUri(long id) {
            return CONTENT_URI.buildUpon().appendPath(Long.toString(id)).build();
        }

        public static Uri buildWeatherLocation(String locationQuery) {
            return CONTENT_URI.buildUpon().appendPath(locationQuery).build();
        }

        public static Uri buildWeatherLocationWithDate(String locationQuery, long testDate) {
            return CONTENT_URI.buildUpon().appendPath(locationQuery).appendPath(Long.toString(normalizeDate(testDate))).build();
        }

        public static Uri buildWeatherLocationWithStartDate(String locationSetting, long l) {
            return CONTENT_URI.buildUpon()
                    .appendPath(locationSetting)
                    .appendQueryParameter("date", Long.toString(normalizeDate(l)))
                    .build();
        }
    }
}
