package com.lyon.search.dict;

import android.content.res.Resources;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import com.lyon.search.R;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Project：Lean                  <br>
 * Description:                               <br>
 * Time：4/13/2019 3:52 PM                  <br>
 *
 * @author lyon                         <br>
 * @version V0.0.1                           <br>
 */
public class CommonDictionaryTest extends BaseDictionaryTest {
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

        testNormalLike();
    }
    @Override
    protected void createDB(String path) {
        super.createDB(path);

        execSQL("CREATE TABLE custom_dictionary (id INTEGER PRIMARY KEY, key_word TEXT, description_content TEXT);");
    }

    @Override
    protected void loadData() {
        loadNormal();
    }

    private void testNormalLike() {
        int count = (int) longForQuery("SELECT count(*) FROM custom_dictionary", null);

//        Assert.assertEquals(count, 777826);

        long time2 = System.currentTimeMillis();
        Cursor cursor = rawQuery("SELECT * FROM custom_dictionary WHERE description_content LIKE '%embellishment%'", null);
        Log.i(TAG, "BENCHMARK | NORMAL QUERY LIKE | COUNT " + cursor.getCount());
        Assert.assertTrue(cursor.getCount() > 0);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String exceptedWord = cursor.getString(1);
            String exceptedDes = cursor.getString(2);

            Assert.assertTrue(id > 0);
            Assert.assertTrue(exceptedWord.length() > 0);
        }

        Log.i(TAG, "BENCHMARK | NORMAL QUERY LIKE | " + (System.currentTimeMillis() - time2));
    }

    private void loadNormal() {
        final Resources words = InstrumentationRegistry.getTargetContext().getResources();
        InputStream wordInputStream = words.openRawResource(R.raw.dictionary);
        BufferedReader wordReader = new BufferedReader(new InputStreamReader(wordInputStream));
        long time1 = System.currentTimeMillis();
        beginTransaction();
        try {
            String wordLine;
            long i = 1L;
            while ((wordLine = wordReader.readLine()) != null) {
                execSQL("INSERT INTO custom_dictionary (id, key_word, description_content) VALUES (?, ?, ?);",
                        new Object[] {i, wordLine.substring(0, 1).trim(), wordLine.trim()});
                i = i + 1;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            setTransactionSuccessful();
            endTransaction();

            Log.i(TAG, "BENCHMARK | NORMAL INSERT | " + (System.currentTimeMillis() - time1));
            try {
                wordReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
