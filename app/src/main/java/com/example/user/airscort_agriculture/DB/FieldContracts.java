package com.example.user.airscort_agriculture.DB;

import android.provider.BaseColumns;

public class FieldContracts {
    public  FieldContracts(){}

    public static abstract class FieldsEntry implements BaseColumns {
        //fields table
        public static final String FIELDS_TABLE ="Fields";
        public static final String NAME="name";
        public static final String FIELD_ID="field_id";
        public static final String PATH_FRAME_LAT="path_frame_lat";
        public static final String PATH_FRAME_LON="path_frame_lon";
        public static final String DRONE_PATH_LAT="drone_path_lat";
        public static final String DRONE_PATH_LON="drone_path_lon";
        public static final String DISTANCE="distance";

        //history table
        public static final String HISTORY_TABLE="hitory";
        public static final String MISSION_ID="mission_id";
        public static final String DATE="date";
        public static final String FIELDS_LIST="fields_list";

        //scan table
        public static final String SCANNING_TABLE="scanning";
        public static final String SCAN_ID="scan_id";
        public static final String SCAN_DATE="scan_date";
        public static final String FIELD="field";
        public static final String RESOLUTION="resolution";
        public static final String HIGH="high";

        //user table
        public static final String USER_TABLE="user";
        public static final String FIRST_NAME="first_name";
        public static final String LAST_NAME="last_name";
        public static final String EMAIL="email";
        public static final String PASSWORD="password";
        public static final String USER_ID="user_id";
        public static final String HOME_LAT="home_lat";
        public static final String HOME_LON="home_lon";

        //time table
        public static final String TIME_TABLE="time_table";
        public static final String SCANNING_TIME="scanning_time";

    }
}
