package org.noear.weed;

import org.noear.weed.ext.DatabaseType;
import org.noear.weed.utils.Property;
import org.noear.weed.wrap.ClassWrap;
import org.noear.weed.wrap.PropertyWrap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by noear on 19-12-11.
 */
public abstract class WhereBase<T extends WhereBase> {
    protected DbContext  _context;
    protected SQLBuilder _builder;
    protected boolean _isSingleTable;

    protected WhereBase() {

    }

    public WhereBase(DbContext context) {
        _context = context;
        _builder = new SQLBuilder();
    }

    protected DatabaseType databaseType(){
        return _context.databaseType();
    }

    protected String formatObject(String name) {
        return _context.formater().formatObject(name);
    }

    protected String formatField(String name) {
        return _context.formater().formatField(name);
    }

    protected String formatColumns(String columns) {
        return _context.formater().formatColumns(columns);
    }

    protected String formatCondition(String condition) {
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
        _builder.append(" WHERE ").append(formatCondition(code), args);
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

    public T where(WhereQ whereQ) {
        _builder.append(whereQ._builder);
        return (T) this;
    }

    public T whereMap(Map<String, Object> columnMap) {
        if (columnMap != null && columnMap.size() > 0) {
            where("1=1");
            columnMap.forEach((k, v) -> {
                andEq(k, v);
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
        _builder.append(" WHERE ").append(formatField(column)).append(" = ? ", val);
        return (T) this;
    }

    public <C> T whereEq(Property<C, ?> property, Object val) {
        return whereEq(getColumnName(property), val);
    }

    public T whereNeq(String column, Object val) {
        _builder.append(" WHERE ").append(formatField(column)).append(" != ? ", val);
        return (T) this;
    }

    public <C> T whereNeq(Property<C, ?> property, Object val) {
        return whereNeq(getColumnName(property), val);
    }

    /**
     * 添加SQL where < 语句
     */
    public T whereLt(String column, Object val) {
        _builder.append(" WHERE ").append(formatField(column)).append(" < ? ", val);
        return (T) this;
    }

    public <C> T whereLt(Property<C, ?> property, Object val) {
        return whereLt(getColumnName(property), val);
    }

    /**
     * 添加SQL where <= 语句
     */
    public T whereLte(String column, Object val) {
        _builder.append(" WHERE ").append(formatField(column)).append(" <= ? ", val);
        return (T) this;
    }

    public <C> T whereLte(Property<C, ?> property, Object val) {
        return whereLte(getColumnName(property), val);
    }

    /**
     * 添加SQL where > 语句
     */
    public T whereGt(String column, Object val) {
        _builder.append(" WHERE ").append(formatField(column)).append(" > ? ", val);
        return (T) this;
    }

    public <C> T whereGt(Property<C, ?> property, Object val) {
        return whereGt(getColumnName(property), val);
    }

    /**
     * 添加SQL where >= 语句
     */
    public T whereGte(String column, Object val) {
        _builder.append(" WHERE ").append(formatField(column)).append(" >= ? ", val);
        return (T) this;
    }

    public <C> T whereGte(Property<C, ?> property, Object val) {
        return whereGte(getColumnName(property), val);
    }

    /**
     * 添加SQL where like 语句
     */
    public T whereLk(String column, String val) {
        _builder.append(" WHERE ").append(formatField(column)).append(" LIKE ? ", val);
        return (T) this;
    }

    public <C> T whereLk(Property<C, ?> property, String val) {
        return whereLk(getColumnName(property), val);
    }

    /**
     * 添加SQL where not like 语句
     */
    public T whereNlk(String column, String val) {
        _builder.append(" WHERE ").append(formatField(column)).append(" NOT LIKE ? ", val);
        return (T) this;
    }

    public <C> T whereNlk(Property<C, ?> property, String val) {
        return whereNlk(getColumnName(property), val);
    }

    /**
     * 添加SQL where between 语句
     */
    public T whereBtw(String column, Object start, Object end) {
        _builder.append(" WHERE ").append(formatField(column)).append(" BETWEEN ? AND ? ", start, end);
        return (T) this;
    }

    public <C> T whereBtw(Property<C, ?> property, Object start, Object end) {
        return whereBtw(getColumnName(property), start, end);
    }

    /**
     * 添加SQL where note between 语句
     */
    public T whereNbtw(String column, Object start, Object end) {
        _builder.append(" WHERE ").append(formatField(column)).append(" NOT BETWEEN ? AND ? ", start, end);
        return (T) this;
    }

    public <C> T whereNbtw(Property<C, ?> property, Object start, Object end) {
        return whereNbtw(getColumnName(property), start, end);
    }


    /**
     * 添加SQL where in(?...) 语句
     */
    public T whereIn(String column, Iterable<Object> ary) {
        _builder.append(" WHERE ").append(formatField(column)).append(" IN (?...) ", ary);
        return (T) this;
    }

    public <C> T whereIn(Property<C, ?> property, Iterable<Object> ary) {
        return whereIn(getColumnName(property), ary);
    }

    /**
     * 添加SQL where not in(?...) 语句
     */
    public T whereNin(String column, Iterable<Object> ary) {
        _builder.append(" WHERE ").append(formatField(column)).append(" NOT IN (?...) ", ary);
        return (T) this;
    }

    public <C> T whereNin(Property<C, ?> property, Iterable<Object> ary) {
        return whereNin(getColumnName(property), ary);
    }


    /**
     * 添加SQL and 语句 //可使用?占位符
     * <p>
     * 例1：.and("name=?","x");
     * 例2: .and("(name=? or id=?)","x",1)
     */
    public T and(String code, Object... args) {
        _builder.append(" AND ").append(formatCondition(code), args);
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
        _builder.append(" AND ").append(formatField(column)).append(" = ? ", val);
        return (T) this;
    }

    public <C> T andEq(Property<C, ?> property, Object val) {
        return andEq(getColumnName(property), val);
    }

    public T andNeq(String column, Object val) {
        _builder.append(" AND ").append(formatField(column)).append(" != ? ", val);
        return (T) this;
    }

    public <C> T andNeq(Property<C, ?> property, Object val) {
        return andNeq(getColumnName(property), val);
    }

    /**
     * 添加SQL and < 语句
     */
    public T andLt(String column, Object val) {
        _builder.append(" AND ").append(formatField(column)).append(" < ? ", val);
        return (T) this;
    }

    public <C> T andLt(Property<C, ?> property, Object val) {
        return andLt(getColumnName(property), val);
    }

    /**
     * 添加SQL and <= 语句
     */
    public T andLte(String column, Object val) {
        _builder.append(" AND ").append(formatField(column)).append(" <= ? ", val);
        return (T) this;
    }

    public <C> T andLte(Property<C, ?> property, Object val) {
        return andLte(getColumnName(property), val);
    }

    /**
     * 添加SQL and > 语句
     */
    public T andGt(String column, Object val) {
        _builder.append(" AND ").append(formatField(column)).append(" > ? ", val);
        return (T) this;
    }

    public <C> T andGt(Property<C, ?> property, Object val) {
        return andGt(getColumnName(property), val);
    }

    /**
     * 添加SQL and >= 语句
     */
    public T andGte(String column, Object val) {
        _builder.append(" AND ").append(formatField(column)).append(" >= ? ", val);
        return (T) this;
    }

    public <C> T andGte(Property<C, ?> property, Object val) {
        return andGte(getColumnName(property), val);
    }

    /**
     * 添加SQL and like 语句
     */
    public T andLk(String column, String val) {
        _builder.append(" AND ").append(formatField(column)).append(" LIKE ? ", val);
        return (T) this;
    }

    public <C> T andLk(Property<C, ?> property, String val) {
        return andLk(getColumnName(property), val);
    }

    /**
     * 添加SQL and not like 语句
     */
    public T andNlk(String column, String val) {
        _builder.append(" AND ").append(formatField(column)).append(" NOT LIKE ? ", val);
        return (T) this;
    }

    public <C> T andNlk(Property<C, ?> property, String val) {
        return andNlk(getColumnName(property), val);
    }

    /**
     * 添加SQL where between 语句
     */
    public T andBtw(String column, Object start, Object end) {
        _builder.append(" AND ").append(formatField(column)).append(" BETWEEN ? AND ? ", start, end);
        return (T) this;
    }

    public <C> T andBtw(Property<C, ?> property, Object start, Object end) {
        return andBtw(getColumnName(property), start, end);
    }

    /**
     * 添加SQL where note between 语句
     */
    public T andNbtw(String column, Object start, Object end) {
        _builder.append(" AND ").append(formatField(column)).append(" NOT BETWEEN ? AND ? ", start, end);
        return (T) this;
    }

    public <C> T andNbtw(Property<C, ?> property, Object start, Object end) {
        return andNbtw(getColumnName(property), start, end);
    }

    /**
     * 添加SQL and in(?...) 语句
     */
    public T andIn(String column, Iterable<Object> ary) {
        _builder.append(" AND ").append(formatField(column)).append(" IN (?...) ", ary);
        return (T) this;
    }

    public <C> T andIn(Property<C, ?> property, Iterable<Object> ary) {
        return andIn(getColumnName(property), ary);
    }

    /**
     * 添加SQL and not in(?...) 语句
     */
    public T andNin(String column, Iterable<Object> ary) {
        _builder.append(" AND ").append(formatField(column)).append(" NOT IN (?...) ", ary);
        return (T) this;
    }

    public <C> T andNin(Property<C, ?> property, Iterable<Object> ary) {
        return andNin(getColumnName(property), ary);
    }


    /**
     * 添加SQL or 语句 //可使用?占位符
     * 例1：.or("name=?","x");
     * 例2: .or("(name=? or id=?)","x",1)
     */
    public T or(String code, Object... args) {
        _builder.append(" OR ").append(formatCondition(code), args);
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
        _builder.append(" OR ").append(formatField(column)).append(" = ? ", val);
        return (T) this;
    }

    public <C> T orEq(Property<C, ?> property, Object val) {
        return orEq(getColumnName(property), val);
    }

    public T orNeq(String column, Object val) {
        _builder.append(" OR ").append(formatField(column)).append(" != ? ", val);
        return (T) this;
    }

    public <C> T orNeq(Property<C, ?> property, Object val) {
        return orNeq(getColumnName(property), val);
    }

    /**
     * 添加SQL or < 语句
     */
    public T orLt(String column, Object val) {
        _builder.append(" OR ").append(formatField(column)).append(" < ? ", val);
        return (T) this;
    }

    public <C> T orLt(Property<C, ?> property, Object val) {
        return orLt(getColumnName(property), val);
    }

    /**
     * 添加SQL or <= 语句
     */
    public T orLte(String column, Object val) {
        _builder.append(" OR ").append(formatField(column)).append(" <= ? ", val);
        return (T) this;
    }

    public <C> T orLte(Property<C, ?> property, Object val) {
        return orLte(getColumnName(property), val);
    }

    /**
     * 添加SQL or > 语句
     */
    public T orGt(String column, Object val) {
        _builder.append(" OR ").append(formatField(column)).append(" > ? ", val);
        return (T) this;
    }

    public <C> T orGt(Property<C, ?> property, Object val) {
        return orGt(getColumnName(property), val);
    }

    /**
     * 添加SQL or >= 语句
     */
    public T orGte(String column, Object val) {
        _builder.append(" OR ").append(formatField(column)).append(" >= ? ", val);
        return (T) this;
    }

    public <C> T orGte(Property<C, ?> property, Object val) {
        return orGte(getColumnName(property), val);
    }

    /**
     * 添加SQL or like 语句
     */
    public T orLk(String column, String val) {
        _builder.append(" OR ").append(formatField(column)).append(" LIKE ? ", val);
        return (T) this;
    }

    public <C> T orLk(Property<C, ?> property, String val) {
        return orLk(getColumnName(property), val);
    }


    /**
     * 添加SQL or not like 语句
     */
    public T orNlk(String column, String val) {
        _builder.append(" OR ").append(formatField(column)).append(" NOT LIKE ? ", val);
        return (T) this;
    }

    public <C> T orNlk(Property<C, ?> property, String val) {
        return orNlk(getColumnName(property), val);
    }


    /**
     * 添加SQL where between 语句
     */
    public T orBtw(String column, Object start, Object end) {
        _builder.append(" OR ").append(formatField(column)).append(" BETWEEN ? AND ? ", start, end);
        return (T) this;
    }

    public <C> T orBtw(Property<C, ?> property, Object start, Object end) {
        return orBtw(getColumnName(property), start, end);
    }

    /**
     * 添加SQL where note between 语句
     */
    public T orNbtw(String column, Object start, Object end) {
        _builder.append(" OR ").append(formatField(column)).append(" NOT BETWEEN ? AND ? ", start, end);
        return (T) this;
    }

    public <C> T orNbtw(Property<C, ?> property, Object start, Object end) {
        return orNbtw(getColumnName(property), start, end);
    }


    /**
     * 添加SQL or in(?...) 语句
     */
    public T orIn(String column, Iterable<Object> ary) {
        _builder.append(" OR ").append(formatField(column)).append(" IN (?...) ", ary);
        return (T) this;
    }

    public <C> T orIn(Property<C, ?> property, Iterable<Object> ary) {
        return orIn(getColumnName(property), ary);
    }

    /**
     * 添加SQL or not in(?...) 语句
     */
    public T orNin(String column, Iterable<Object> ary) {
        _builder.append(" OR ").append(formatField(column)).append(" NOT IN (?...) ", ary);
        return (T) this;
    }

    public <C> T orNin(Property<C, ?> property, Iterable<Object> ary) {
        return orNin(getColumnName(property), ary);
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
        _builder.append(" ( ").append(formatCondition(code), args);
        return (T) this;
    }

    /**
     * 添加右括号
     */
    public T end() {
        _builder.append(" ) ");
        return (T) this;
    }



    protected String _orderBy;
    public T orderBy(String code) {
        _orderBy = formatColumns(code);
        _builder.append(" ORDER BY ").append(_orderBy);
        return (T) this;
    }

    public T orderByAsc(String fileds) {
        orderBy(formatColumns(fileds) + " ASC ");
        return (T) this;
    }
    public <C> T orderByAsc(Property<C,?> property) {
        return orderByAsc(getColumnName(property));
    }


    public T orderByDesc(String fileds) {
        orderBy(formatColumns(fileds) + " DESC ");
        return (T)this;
    }
    public <C> T orderByDesc(Property<C,?> property) {
        return orderByDesc(getColumnName(property));
    }



    public T groupBy(String code) {
        _builder.append(" GROUP BY ").append(formatColumns(code));
        return (T)this;
    }

    public <C> T groupBy(Property<C,?> property) {
        return groupBy(getColumnName(property));
    }

    public T having(String code){
        _builder.append(" HAVING ").append(code);
        return (T)this;
    }








    protected List<ClassWrap> _clzArray;
    protected int addClass(ClassWrap clzWrap) {
        if (_clzArray == null) {
            _clzArray = new ArrayList<>();
        }

        int idx = _clzArray.indexOf(clzWrap);
        if (idx < 0) {
            idx = _clzArray.size();
            _clzArray.add(clzWrap);
        }
        return idx;
    }

    protected String getTableName(Class<?> tableClz) {
        ClassWrap cw = ClassWrap.get(tableClz);
        int idx = addClass(cw);
        return cw.tableName + " t" + idx;
    }

    protected <C> String getColumnName(Property<C, ?> p) {
        if(_isSingleTable){
            return PropertyWrap.get(p).name;
        }else {
            return PropertyWrap.get(p).getColumnName(_clzArray);
        }
    }
}
