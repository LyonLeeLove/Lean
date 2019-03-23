package com.lyon.search;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Project：Lean                  <br>
 * Description:                               <br>
 * Time：3/23/2019 3:09 PM                  <br>
 *
 * @author lyon                         <br>
 * @version V0.0.1                           <br>
 */
public abstract class BaseSearchTest {
    protected abstract String getDatabaseName();
    protected abstract void createDB(String path);
    protected abstract void loadData();
    protected abstract void closeDB();
    protected abstract void execSQL(String sql);
    protected abstract void execSQL(String sql, Object[] bindArgs);
    protected abstract void beginTransaction();
    protected abstract void setTransactionSuccessful();
    protected abstract void endTransaction();
    protected abstract long longForQuery(String sql, String[] selectionArgs);
    protected abstract String stringForQuery(String sql, String[] selectionArgs);
    protected abstract Cursor rawQuery(String sql, String[] selectionArgs);

    public void doBefore() {
        Context context = InstrumentationRegistry.getTargetContext();

        File dbFile = context.getDatabasePath(getDatabaseName());
        dbFile.getParentFile().mkdirs();
        dbFile.delete();
        new File(dbFile.getParentFile(), dbFile.getName() + "-journal").delete();
        new File(dbFile.getParentFile(), dbFile.getName() + "-wal").delete();
        createDB(dbFile.getPath());
        loadData();
    }

    public void doAfter() {
        closeDB();
    }

    public void doTest() {

    }

    public Map<String, String> loadDict() {
        final Resources resources = InstrumentationRegistry.getTargetContext().getResources();
        InputStream inputStream = resources.openRawResource(R.raw.dictionary);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        Map<String, String> dict = new HashMap<>();
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] strings = TextUtils.split(line, "-");
                if (strings.length < 2) {
                    continue;
                }
                dict.put(strings[0].trim(), strings[1].trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return dict;
    }
}
