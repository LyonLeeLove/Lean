package com.lyon.database.table;

/**
 * @author lyon                             <br>
 * @version V0.0.1                          <br>
 * Description:                             <br>
 * Date 9/30/2018 4:25 PM                    <br>
 */
public abstract class AbstractTable {
    protected abstract Class getCls();
    protected abstract boolean isPrimaryKey(String column);
    public String build() {
        StringBuffer buffer = new StringBuffer();
        Class cls = getCls();

        return "";
    }
}
