package com.example.android.newsapp.Data;

import android.provider.BaseColumns;

/**
 * Created by ishpr on 7/26/2017.
 */
//Contract class created for the SQL Data base for the application
public class NewsItemContract {


    public static final class NewsItemEntry implements BaseColumns{

        public static final String TABLE_NAME="NewsEntry";
        public static final String COLUMN_TITLE="newsTitle";
        public static final String COLUMN_DESC="newsDesc";
        public static final String COLUMN_URL_TO_IMAGE="newsUrlImage";
        public static final String COLUMN_PUBLISHED_AT="newsDate";
        public static final String COLUMN__URL_TO="newsUrlTo";


    }

}
