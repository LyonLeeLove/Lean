package com.lyon.search.dict;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.lyon.search.BaseSearchTest;

import org.junit.Assert;

import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Project：Lean                  <br>
 * Description:                               <br>
 * Time：3/23/2019 3:12 PM                  <br>
 *
 * @author lyon                         <br>
 * @version V0.0.1                           <br>
 */
public class BaseDictionaryTest extends BaseSearchTest {
    protected static final String TAG = "NORMAL BENCHMARK";
    private static final String DATABASE_NAME = "dictionary_test.db";
    protected SQLiteDatabase database;

    @Override
    @Before
    public void doBefore() {
        super.doBefore();
    }

    @Override
    @After
    public void doAfter() {
        super.doAfter();
    }

    @Override
    @Test
    public void doTest() {
        super.doTest();

        int count = (int) longForQuery("SELECT count(*) FROM custom_dictionary", null);

        Assert.assertEquals(count, 1000);
    }

    @Override
    protected String getDatabaseName() {
        return DATABASE_NAME;
    }

    @Override
    protected void loadData() {
        Map<String, String> data = loadDict();
        Set<Map.Entry<String, String>> entrySet = data.entrySet();

        long i = 1L;
        beginTransaction();
        for (Map.Entry<String, String> entry : entrySet) {
            execSQL("INSERT INTO custom_dictionary (id, key_word, description_content) VALUES (?, ?, ?);",
                    new Object[] {i, entry.getKey(), entry.getValue()});
            i = i + 1;
        }
        setTransactionSuccessful();
        endTransaction();
    }

    @Override
    protected void createDB(String path) {
        database = SQLiteDatabase.openOrCreateDatabase(path, null);
        execSQL("CREATE TABLE custom_dictionary (id INTEGER PRIMARY KEY, key_word TEXT, description_content TEXT);");
    }

    @Override
    protected void closeDB() {
        database.close();
        database = null;
    }

    @Override
    protected void execSQL(String sql) {
        database.execSQL(sql);
    }

    @Override
    protected void execSQL(String sql, Object[] bindArgs) {
        database.execSQL(sql, bindArgs);
    }

    @Override
    protected void beginTransaction() {
        database.beginTransaction();
    }

    @Override
    protected void setTransactionSuccessful() {
        database.setTransactionSuccessful();
    }

    @Override
    protected void endTransaction() {
        database.endTransaction();
    }

    @Override
    protected long longForQuery(String sql, String[] selectionArgs) {
        return DatabaseUtils.longForQuery(database, sql, selectionArgs);
    }

    @Override
    protected String stringForQuery(String sql, String[] selectionArgs) {
        return DatabaseUtils.stringForQuery(database, sql, selectionArgs);
    }

    @Override
    protected Cursor rawQuery(String sql, String[] selectionArgs) {
        return database.rawQuery(sql, selectionArgs);
    }
}
