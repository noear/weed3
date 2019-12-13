package org.noear.weed;

import java.util.Map;

public abstract class WhereBase<T extends WhereBase> {
    protected DbContext _context;
    protected SQLBuilder _builder;

    protected WhereBase(){

    }
    public WhereBase(DbContext context) {
        _context = context;
        _builder = new SQLBuilder();
    }

    protected String formatObject(String name){
        return _context.formater().formatObject(name);
    }

    protected String formatField(String name){
        return _context.formater().formatField(name);
    }

    protected String formatColumns(String columns){
        return _context.formater().formatColumns(columns);
    }

    protected String formatCondition(String condition){
        return _context.formater().formatCondition(condition);
    }



    /** 添加SQL where 语句
     * 可使用?,?...占位符（ ?... 表示数组占位符）
     *
     * 例1: .where("name=?","x");
     * 例2: .where("((name=? or id=?) and sex=0)","x",1)
     * 例3: .where("id IN (?...)",new int[]{1,12,3,6})
     * */
    public T where(String code, Object... args) {
        _builder.append(" WHERE ").append(formatCondition(code), args);
        return (T)this;
    }

    //
    // 不能有 whereIf //太危险了。。。可能会造成更新所有
    //

    /** 添加SQL where 关键字 */
    public T where() {
        _builder.append(" WHERE ");
        return (T)this;
    }

    public T where(WhereQ whereQ) {
        _builder.append(whereQ._builder);
        return (T)this;
    }

    public T whereMap(Map<String,Object> map) {
        if (map != null && map.size() > 0) {
            where("1=1");
            map.forEach((k, v) -> {
                andEq(k, v);
            });
        }
        return (T) this;
    }

    /**
     * 添加SQL where = 语句
     *
     * 例：.whereEq("name","x");
     * */
    public T whereEq(String filed, Object val){
        _builder.append(" WHERE ").append(formatField(filed)).append(" = ? ",val);
        return (T)this;
    }
    /** 添加SQL where < 语句 */
    public T whereLt(String filed, Object val){
        _builder.append(" WHERE ").append(formatField(filed)).append(" < ? ",val);
        return (T)this;
    }
    /** 添加SQL where <= 语句 */
    public T whereLte(String filed, Object val){
        _builder.append(" WHERE ").append(formatField(filed)).append(" <= ? ",val);
        return (T)this;
    }
    /** 添加SQL where > 语句 */
    public T whereGt(String filed, Object val){
        _builder.append(" WHERE ").append(formatField(filed)).append(" > ? ",val);
        return (T)this;
    }
    /** 添加SQL where >= 语句 */
    public T whereGte(String filed, Object val){
        _builder.append(" WHERE ").append(formatField(filed)).append(" >= ? ",val);
        return (T)this;
    }
    /** 添加SQL where like 语句 */
    public T whereLk(String filed, String val){
        _builder.append(" WHERE ").append(formatField(filed)).append(" LIKE ? ",val);
        return (T)this;
    }

    /** 添加SQL where in(?...) 语句 */
    public T whereIn(String filed, Iterable<Object> ary){
        _builder.append(" WHERE ").append(formatField(filed)).append(" IN (?...) ",ary);
        return (T)this;
    }

    /** 添加SQL where not in(?...) 语句 */
    public T whereNin(String filed, Iterable<Object> ary){
        _builder.append(" WHERE ").append(formatField(filed)).append(" NOT IN (?...) ",ary);
        return (T)this;
    }



    /**
     * 添加SQL and 语句 //可使用?占位符
     *
     * 例1：.and("name=?","x");
     * 例2: .and("(name=? or id=?)","x",1)
     * */
    public T and(String code, Object... args) {
        _builder.append(" AND ").append(formatCondition(code), args);
        return (T)this;
    }

    public T andIf(boolean condition, String code, Object... args) {
        if (condition) {
            and(code, args);
        }
        return (T) this;
    }

    /** 添加SQL where 关键字 */
    public T and() {
        _builder.append(" AND ");
        return (T)this;
    }

    /** 添加SQL and = 语句 */
    public T andEq(String filed, Object val){
        _builder.append(" AND ").append(formatField(filed)).append(" = ? ",val);
        return (T)this;
    }
    /** 添加SQL and < 语句 */
    public T andLt(String filed, Object val){
        _builder.append(" AND ").append(formatField(filed)).append(" < ? ",val);
        return (T)this;
    }
    /** 添加SQL and <= 语句 */
    public T andLte(String filed, Object val){
        _builder.append(" AND ").append(formatField(filed)).append(" <= ? ",val);
        return (T)this;
    }
    /** 添加SQL and > 语句 */
    public T andGt(String filed, Object val){
        _builder.append(" AND ").append(formatField(filed)).append(" > ? ",val);
        return (T)this;
    }
    /** 添加SQL and >= 语句 */
    public T andGte(String filed, Object val){
        _builder.append(" AND ").append(formatField(filed)).append(" >= ? ",val);
        return (T)this;
    }
    /** 添加SQL and like 语句 */
    public T andLk(String filed, String val){
        _builder.append(" AND ").append(formatField(filed)).append(" LIKE ? ",val);
        return (T)this;
    }

    /** 添加SQL and in(?...) 语句 */
    public T andIn(String filed, Iterable<Object> ary){
        _builder.append(" AND ").append(formatField(filed)).append(" IN (?...) ",ary);
        return (T)this;
    }

    /** 添加SQL and not in(?...) 语句 */
    public T andNin(String filed, Iterable<Object> ary){
        _builder.append(" AND ").append(formatField(filed)).append(" NOT IN (?...) ",ary);
        return (T)this;
    }




    /**
     * 添加SQL or 语句 //可使用?占位符
     * 例1：.or("name=?","x");
     * 例2: .or("(name=? or id=?)","x",1)
     * */
    public T or(String code, Object... args) {
        _builder.append(" OR ").append(formatCondition(code), args);
        return (T)this;
    }

    public T orIf(boolean condition, String code, Object... args) {
        if (condition) {
            or(code, args);
        }
        return (T) this;
    }

    /** 添加SQL or 关键字 */
    public T or() {
        _builder.append(" OR ");
        return (T)this;
    }

    /** 添加SQL or = 语句 */
    public T orEq(String filed, Object val){
        _builder.append(" OR ").append(formatField(filed)).append(" = ? ",val);
        return (T)this;
    }

    /** 添加SQL or < 语句 */
    public T orLt(String filed, Object val){
        _builder.append(" OR ").append(formatField(filed)).append(" < ? ",val);
        return (T)this;
    }

    /** 添加SQL or <= 语句 */
    public T orLte(String filed, Object val){
        _builder.append(" OR ").append(formatField(filed)).append(" <= ? ",val);
        return (T)this;
    }

    /** 添加SQL or > 语句 */
    public T orGt(String filed, Object val){
        _builder.append(" OR ").append(formatField(filed)).append(" > ? ",val);
        return (T)this;
    }

    /** 添加SQL or >= 语句 */
    public T orGte(String filed, Object val){
        _builder.append(" OR ").append(formatField(filed)).append(" >= ? ",val);
        return (T)this;
    }

    /** 添加SQL or like 语句 */
    public T orLk(String filed, String val){
        _builder.append(" OR ").append(formatField(filed)).append(" LIKE ? ",val);
        return (T)this;
    }

    /** 添加SQL or in(?...) 语句 */
    public T orIn(String filed, Iterable<Object> ary){
        _builder.append(" OR ").append(formatField(filed)).append(" IN (?...) ",ary);
        return (T)this;
    }

    /** 添加SQL or not in(?...) 语句 */
    public T orNin(String filed, Iterable<Object> ary){
        _builder.append(" OR ").append(formatField(filed)).append(" NOT IN (?...) ",ary);
        return (T)this;
    }



    /** 添加左括号 */
    public T begin() {
        _builder.append(" ( ");
        return (T)this;
    }

    /**
     * 添加左括号并附加代码
     * 可使用?,?...占位符（ ?... 表示数组占位符）
     * */
    public T begin(String code, Object... args) {
        _builder.append(" ( ").append(formatCondition(code), args);
        return (T)this;
    }

    /** 添加右括号 */
    public T end() {
        _builder.append(" ) ");
        return (T)this;
    }
}
