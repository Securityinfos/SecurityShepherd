package com.mobshep.poorauthentication2;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteStatement;


import java.io.File;
import java.sql.PreparedStatement;

/**
 * This file is part of the Security Shepherd Project.
 *
 * The Security Shepherd project is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.<br/>
 *
 * The Security Shepherd project is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.<br/>
 *
 * You should have received a copy of the GNU General Public License
 * along with the Security Shepherd project.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @author Sean Duggan
 */

public class DBHelper {

    protected void createKeyTable(Context context) {
        SQLiteDatabase.loadLibs(context);

        String dbPath = context.getDatabasePath("Users.db").getPath();
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbPath,
                "", null);
    }

    public void insertData(Context context, String Username, String Password) {
        SQLiteDatabase.loadLibs(context);

        String dbPath = context.getDatabasePath("Users.db").getPath();
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbPath,
                "", null);

        SQLiteStatement stmt = db.compileStatement("INSERT INTO Users (username,password,verified) VALUES(?,?,'False')");
        stmt.bindString(1, Username);
        stmt.bindString(2, Password);
        stmt.execute();

        db.close();

    }


    public void insertKey(Context context, String key) {
        SQLiteDatabase.loadLibs(context);

        String dbPath = context.getDatabasePath("Key.db").getPath();
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbPath,
                "AHardCodedPassword", null);

        SQLiteStatement stmt = db.compileStatement("INSERT INTO Key (key) VALUES(?)");
        stmt.bindString(1, key);
        stmt.execute();

        db.close();
    }

    public boolean queryData(Context context, String Username, String Password) {

        SQLiteDatabase.loadLibs(context);

        String dbPath = context.getDatabasePath("Users.db").getPath();
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbPath,
                "", null);


        // SQLiteStatement query = db.compileStatement("SELECT * FROM Users WHERE username = ? AND password = ?");
        // query.bindString(1, Username);
        // query.bindString(2, Password);
        // query.execute();

        String query = ("SELECT * FROM Users WHERE username=? AND password=? AND verified != 'False'");

        Cursor cursor = db.rawQuery(query, new String[]{Username, Password});

        if (cursor != null) {
            if (cursor.getCount() <= 0) {
                return false;
            }
        }

        db.close();

        return true;

    }


    public void populateTable(Context context, String password) {
        try {
            try {
                SQLiteDatabase.loadLibs(context);

                String dbPath = context.getDatabasePath("Users.db").getPath();

                File dbPathFile = new File(dbPath);
                if (!dbPathFile.exists())
                    dbPathFile.getParentFile().mkdirs();

                SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbPath,
                        "", null);

                db.execSQL("DROP TABLE IF EXISTS Users");
                db.execSQL("CREATE TABLE Users(memID INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password VARCHAR, verified Text)");


            } catch (Exception e) {
                // TODO Auto-generated catch block
                Log.d("DBHelper", "The following error occurred:" + e.getMessage());

            }

        } catch (SQLiteException e) {
            Log.i ("", "An database error occurred.");
        }
    }

    public void populateAnotherTable(Context context, String password) {
        try {
            try {
                SQLiteDatabase.loadLibs(context);

                String dbPath = context.getDatabasePath("Key.db").getPath();

                File dbPathFile = new File(dbPath);
                if (!dbPathFile.exists())
                    dbPathFile.getParentFile().mkdirs();

                SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbPath,
                        "", null);

                db.execSQL("DROP TABLE IF EXISTS Key");
                db.execSQL("CREATE TABLE Key( VARCHAR Key)");


            } catch (Exception e) {
                // TODO Auto-generated catch block
                Log.d("DBHelper", "The following error occurred:" + e.getMessage());

            }

        } catch (SQLiteException e) {
            Log.i ("", "An database error occurred.");
        }
    }

    public String outputKey(Context context, String password) {
        SQLiteDatabase.loadLibs(context);

        String dbPath = context.getDatabasePath("key.db").getPath();

        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbPath, password,
                null);

        String query = ("SELECT * FROM key;");

        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {

            try {
                if (cursor.moveToFirst())
                    return cursor.getString(0);
            } finally {
                cursor.close();
            }
        }
        return dbPath;
    }


}
