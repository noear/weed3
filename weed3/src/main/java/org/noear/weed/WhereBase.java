package org.noear.weed;

import org.noear.weed.ext.Fun2;
import org.noear.weed.utils.EntityUtils;
import org.noear.weed.wrap.DbType;

import java.util.Map;

/**
 * Created by noear on 19-12-11.
 */
public abstract class WhereBase<T extends WhereBase> {
    protected DbContext  _context;
    protected SQLBuilder _builder;
    protected StringBuilder _orderBy;

    protected WhereBase() {

    }

    public WhereBase(DbContext context) {
        _context = context;
        _builder = new SQLBuilder();
    }

    protected DbType dbType(){
        return _context.dbType();
    }

    protected String fmtSchema(String name){
        return _context.formater().formatSchema(name);
    }
    protected String fmtObject(String name) {
        return _context.formater().formatTable(name);
    }

    protected String fmtColumn(String name) {
        return _context.formater().formatColumn(name);
    }

    protected String fmtMutColumns(String columns) {
        return _context.formater().formatMultipleColumns(columns);
    }

    protected String fmtCondition(String condition) {
        return _context.formater().formatCondition(condition);
    }


    /**
     * 添加SQL where 语句
     * 可使用?,?...占位符（ ?... 表示数组占位符）
     * <p>
     * 例1: .where("name=?","x");
     * 例2: .where("((name=? or id=?) and sex=0)","x",1)
     * 例3: .where("id IN (?...)",new int[]{1,12,3,6})
     */
    public T where(String code, Object... args) {
        _builder.append(" WHERE ").append(fmtCondition(code), args);
        return (T) this;
    }

    //
    // whereIf //非常危险。。。已对delete(),update()添加限有制
    public T whereIf(boolean condition, String code, Object... args) {
        if (condition) {
            where(code, args);
        }
        return (T) this;
    }


    /**
     * 添加SQL where 关键字
     */
    public T where() {
        _builder.append(" WHERE ");
        return (T) this;
    }

    public T where(MapperWhereQ whereQ) {
        _builder.append(whereQ._builder);
        return (T) this;
    }

    public T whereMap(Map<String, Object> columnMap) {
        return whereEntityIf(columnMap, (k, v) -> v != null);
    }

    public T whereMapIf(Map<String, Object> columnMap, Fun2<Boolean,String,Object> condition) {
        if (columnMap != null && columnMap.size() > 0) {
            where("1=1");
            columnMap.forEach((k, v) -> {
                if (condition.run(k, v)) {
                    andEq(k, v);
                }
            });
        }
        return (T) this;
    }

    public T whereEntity(Object entity) {
        return whereEntityIf(entity, (k, v) -> v != null);
    }

    public T whereEntityIf(Object entity, Fun2<Boolean,String,Object> condition) {
        if (entity != null) {
            where("1=1");
            EntityUtils.fromEntity(entity, (k, v) -> {
                if (condition.run(k, v)) {
                    andEq(k, v);
                }
            });
        }
        return (T) this;
    }

    /**
     * 添加SQL where = 语句
     * <p>
     * 例：.whereEq("name","x");
     */
    public T whereEq(String column, Object val) {
        _builder.append(" WHERE ").append(fmtColumn(column));
        if(val == null){
            _builder.append(" IS NULL ");
        }else{
            _builder.append(" = ? ", val);
        }

        return (T) this;
    }


    public T whereNeq(String column, Object val) {
        _builder.append(" WHERE ").append(fmtColumn(column));
        if(val == null){
            _builder.append(" IS NOT NULL ");
        }else{
            _builder.append(" != ? ", val);
        }

        return (T) this;
    }

    /**
     * 添加SQL where < 语句
     */
    public T whereLt(String column, Object val) {
        _builder.append(" WHERE ").append(fmtColumn(column)).append(" < ? ", val);
        return (T) this;
    }

    /**
     * 添加SQL where <= 语句
     */
    public T whereLte(String column, Object val) {
        _builder.append(" WHERE ").append(fmtColumn(column)).append(" <= ? ", val);
        return (T) this;
    }

    /**
     * 添加SQL where > 语句
     */
    public T whereGt(String column, Object val) {
        _builder.append(" WHERE ").append(fmtColumn(column)).append(" > ? ", val);
        return (T) this;
    }

    /**
     * 添加SQL where >= 语句
     */
    public T whereGte(String column, Object val) {
        _builder.append(" WHERE ").append(fmtColumn(column)).append(" >= ? ", val);
        return (T) this;
    }

    /**
     * 添加SQL where like 语句
     */
    public T whereLk(String column, String val) {
        _builder.append(" WHERE ").append(fmtColumn(column)).append(" LIKE ? ", val);
        return (T) this;
    }

    /**
     * 添加SQL where not like 语句
     */
    public T whereNlk(String column, String val) {
        _builder.append(" WHERE ").append(fmtColumn(column)).append(" NOT LIKE ? ", val);
        return (T) this;
    }

    /**
     * 添加SQL where between 语句
     */
    public T whereBtw(String column, Object start, Object end) {
        _builder.append(" WHERE ").append(fmtColumn(column)).append(" BETWEEN ? AND ? ", start, end);
        return (T) this;
    }

    /**
     * 添加SQL where note between 语句
     */
    public T whereNbtw(String column, Object start, Object end) {
        _builder.append(" WHERE ").append(fmtColumn(column)).append(" NOT BETWEEN ? AND ? ", start, end);
        return (T) this;
    }


    /**
     * 添加SQL where in(?...) 语句
     */
    public T whereIn(String column, Iterable<Object> ary) {
        _builder.append(" WHERE ").append(fmtColumn(column)).append(" IN (?...) ", ary);
        return (T) this;
    }


    /**
     * 添加SQL where not in(?...) 语句
     */
    public T whereNin(String column, Iterable<Object> ary) {
        _builder.append(" WHERE ").append(fmtColumn(column)).append(" NOT IN (?...) ", ary);
        return (T) this;
    }



    /**
     * 添加SQL and 语句 //可使用?占位符
     * <p>
     * 例1：.and("name=?","x");
     * 例2: .and("(name=? or id=?)","x",1)
     */
    public T and(String code, Object... args) {
        _builder.append(" AND ").append(fmtCondition(code), args);
        return (T) this;
    }

    public T andIf(boolean condition, String code, Object... args) {
        if (condition) {
            and(code, args);
        }
        return (T) this;
    }

    /**
     * 添加SQL where 关键字
     */
    public T and() {
        _builder.append(" AND ");
        return (T) this;
    }

    /**
     * 添加SQL and = 语句
     */
    public T andEq(String column, Object val) {
        _builder.append(" AND ").append(fmtColumn(column));
        if(val == null){
            _builder.append(" IS NULL ");
        }else{
            _builder.append(" = ? ", val);
        }

        return (T) this;
    }


    public T andNeq(String column, Object val) {
        _builder.append(" AND ").append(fmtColumn(column));
        if(val == null){
            _builder.append(" IS NOT NULL ");
        }else{
            _builder.append(" != ? ", val);
        }

        return (T) this;
    }

    /**
     * 添加SQL and < 语句
     */
    public T andLt(String column, Object val) {
        _builder.append(" AND ").append(fmtColumn(column)).append(" < ? ", val);
        return (T) this;
    }

    /**
     * 添加SQL and <= 语句
     */
    public T andLte(String column, Object val) {
        _builder.append(" AND ").append(fmtColumn(column)).append(" <= ? ", val);
        return (T) this;
    }

    /**
     * 添加SQL and > 语句
     */
    public T andGt(String column, Object val) {
        _builder.append(" AND ").append(fmtColumn(column)).append(" > ? ", val);
        return (T) this;
    }

    /**
     * 添加SQL and >= 语句
     */
    public T andGte(String column, Object val) {
        _builder.append(" AND ").append(fmtColumn(column)).append(" >= ? ", val);
        return (T) this;
    }

    /**
     * 添加SQL and like 语句
     */
    public T andLk(String column, String val) {
        _builder.append(" AND ").append(fmtColumn(column)).append(" LIKE ? ", val);
        return (T) this;
    }

    /**
     * 添加SQL and not like 语句
     */
    public T andNlk(String column, String val) {
        _builder.append(" AND ").append(fmtColumn(column)).append(" NOT LIKE ? ", val);
        return (T) this;
    }

    /**
     * 添加SQL where between 语句
     */
    public T andBtw(String column, Object start, Object end) {
        _builder.append(" AND ").append(fmtColumn(column)).append(" BETWEEN ? AND ? ", start, end);
        return (T) this;
    }


    /**
     * 添加SQL where note between 语句
     */
    public T andNbtw(String column, Object start, Object end) {
        _builder.append(" AND ").append(fmtColumn(column)).append(" NOT BETWEEN ? AND ? ", start, end);
        return (T) this;
    }


    /**
     * 添加SQL and in(?...) 语句
     */
    public T andIn(String column, Iterable<Object> ary) {
        _builder.append(" AND ").append(fmtColumn(column)).append(" IN (?...) ", ary);
        return (T) this;
    }

    /**
     * 添加SQL and not in(?...) 语句
     */
    public T andNin(String column, Iterable<Object> ary) {
        _builder.append(" AND ").append(fmtColumn(column)).append(" NOT IN (?...) ", ary);
        return (T) this;
    }


    /**
     * 添加SQL or 语句 //可使用?占位符
     * 例1：.or("name=?","x");
     * 例2: .or("(name=? or id=?)","x",1)
     */
    public T or(String code, Object... args) {
        _builder.append(" OR ").append(fmtCondition(code), args);
        return (T) this;
    }

    public T orIf(boolean condition, String code, Object... args) {
        if (condition) {
            or(code, args);
        }
        return (T) this;
    }

    /**
     * 添加SQL or 关键字
     */
    public T or() {
        _builder.append(" OR ");
        return (T) this;
    }

    /**
     * 添加SQL or = 语句
     */
    public T orEq(String column, Object val) {
        _builder.append(" OR ").append(fmtColumn(column));
        if(val == null){
            _builder.append(" IS NULL ");
        }else{
            _builder.append(" = ? ", val);
        }

        return (T) this;
    }


    public T orNeq(String column, Object val) {
        _builder.append(" OR ").append(fmtColumn(column));
        if(val == null){
            _builder.append(" IS NOT NULL ");
        }else{
            _builder.append(" != ? ", val);
        }

        return (T) this;
    }

    /**
     * 添加SQL or < 语句
     */
    public T orLt(String column, Object val) {
        _builder.append(" OR ").append(fmtColumn(column)).append(" < ? ", val);
        return (T) this;
    }

    /**
     * 添加SQL or <= 语句
     */
    public T orLte(String column, Object val) {
        _builder.append(" OR ").append(fmtColumn(column)).append(" <= ? ", val);
        return (T) this;
    }

    /**
     * 添加SQL or > 语句
     */
    public T orGt(String column, Object val) {
        _builder.append(" OR ").append(fmtColumn(column)).append(" > ? ", val);
        return (T) this;
    }

    /**
     * 添加SQL or >= 语句
     */
    public T orGte(String column, Object val) {
        _builder.append(" OR ").append(fmtColumn(column)).append(" >= ? ", val);
        return (T) this;
    }

    /**
     * 添加SQL or like 语句
     */
    public T orLk(String column, String val) {
        _builder.append(" OR ").append(fmtColumn(column)).append(" LIKE ? ", val);
        return (T) this;
    }


    /**
     * 添加SQL or not like 语句
     */
    public T orNlk(String column, String val) {
        _builder.append(" OR ").append(fmtColumn(column)).append(" NOT LIKE ? ", val);
        return (T) this;
    }


    /**
     * 添加SQL where between 语句
     */
    public T orBtw(String column, Object start, Object end) {
        _builder.append(" OR ").append(fmtColumn(column)).append(" BETWEEN ? AND ? ", start, end);
        return (T) this;
    }


    /**
     * 添加SQL where note between 语句
     */
    public T orNbtw(String column, Object start, Object end) {
        _builder.append(" OR ").append(fmtColumn(column)).append(" NOT BETWEEN ? AND ? ", start, end);
        return (T) this;
    }



    /**
     * 添加SQL or in(?...) 语句
     */
    public T orIn(String column, Iterable<Object> ary) {
        _builder.append(" OR ").append(fmtColumn(column)).append(" IN (?...) ", ary);
        return (T) this;
    }

    /**
     * 添加SQL or not in(?...) 语句
     */
    public T orNin(String column, Iterable<Object> ary) {
        _builder.append(" OR ").append(fmtColumn(column)).append(" NOT IN (?...) ", ary);
        return (T) this;
    }


    /**
     * 添加左括号
     */
    public T begin() {
        _builder.append(" ( ");
        return (T) this;
    }

    /**
     * 添加左括号并附加代码
     * 可使用?,?...占位符（ ?... 表示数组占位符）
     */
    public T begin(String code, Object... args) {
        _builder.append(" ( ").append(fmtCondition(code), args);
        return (T) this;
    }

    /**
     * 添加右括号
     */
    public T end() {
        _builder.append(" ) ");
        return (T) this;
    }



    public T orderBy(String code) {
        if(_orderBy == null){
            _orderBy = new StringBuilder(" ORDER BY ");
        }else{
            _orderBy.append(", ");
        }

        _orderBy.append(fmtMutColumns(code));

        return (T) this;
    }

    public T orderByAsc(String fileds) {
        orderBy(fmtMutColumns(fileds) + " ASC ");
        return (T) this;
    }


    public T orderByDesc(String fileds) {
        orderBy(fmtMutColumns(fileds) + " DESC ");
        return (T)this;
    }



    public T groupBy(String code) {
        _builder.append(" GROUP BY ").append(fmtMutColumns(code));
        return (T)this;
    }

    public T having(String code){
        _builder.append(" HAVING ").append(code);
        return (T)this;
    }
}
