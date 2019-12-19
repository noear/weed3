package org.noear.weed.wrap;

import java.util.ArrayList;
import java.util.List;

public class TableWrap {
    public final String name;
    public final String remarks;

    private String pk1;
    private List<String> pks = new ArrayList<>();
    private List<ColumnWrap> columns = new ArrayList<>();

    public TableWrap(String name, String remarks) {
        this.name = name;
        this.remarks = remarks;
    }

    public List<String> getPks() {
        return pks;
    }


    public String getPk1() {
        if (pk1 != null) {
            return pk1;
        } else {
            if (columns.size() > 0) {
                return columns.get(0).name;
            } else {
                return null;
            }
        }
    }


    public List<ColumnWrap> getColumns() {
        return columns;
    }

    public void addPk(String name){
        if(pk1==null){
            pk1 = name;
        }
        pks.add(name);
    }

    public void addColumn(ColumnWrap col){
        columns.add(col);
    }

}
