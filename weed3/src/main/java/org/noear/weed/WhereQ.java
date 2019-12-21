package org.noear.weed;

import org.noear.weed.wrap.Property;

/**
 * Created by noear on 19-12-11.
 *
 * 仅用于BaseMapper，即用于单表操作
 */
public class WhereQ extends WhereBase<WhereQ> {

    private DbTableQuery _query;

    protected WhereQ(DbTableQuery query) {
        super();

        _query = query;

        //为了不自动创建别名（insert,update,delete；不需要别名）
        _isSingleTable = true;
        _context = _query._context;
        _builder = _query._builder;
    }

    @Override
    public WhereQ orderBy(String code) {
        super.orderBy(code);

        if (_query != null) {
            _query._orderBy = _orderBy;
        }

        return this;
    }


    public <C> WhereQ whereEq(Property<C, ?> property, Object val) {
        return whereEq(getColumnName(property), val);
    }

    public <C> WhereQ whereNeq(Property<C, ?> property, Object val) {
        return whereNeq(getColumnName(property), val);
    }

    public <C> WhereQ whereLt(Property<C, ?> property, Object val) {
        return whereLt(getColumnName(property), val);
    }

    public <C> WhereQ whereLte(Property<C, ?> property, Object val) {
        return whereLte(getColumnName(property), val);
    }

    public <C> WhereQ whereGt(Property<C, ?> property, Object val) {
        return whereGt(getColumnName(property), val);
    }

    public <C> WhereQ whereGte(Property<C, ?> property, Object val) {
        return whereGte(getColumnName(property), val);
    }

    public <C> WhereQ whereLk(Property<C, ?> property, String val) {
        return whereLk(getColumnName(property), val);
    }

    public <C> WhereQ whereNlk(Property<C, ?> property, String val) {
        return whereNlk(getColumnName(property), val);
    }

    public <C> WhereQ whereBtw(Property<C, ?> property, Object start, Object end) {
        return whereBtw(getColumnName(property), start, end);
    }

    public <C> WhereQ whereNbtw(Property<C, ?> property, Object start, Object end) {
        return whereNbtw(getColumnName(property), start, end);
    }

    public <C> WhereQ whereIn(Property<C, ?> property, Iterable<Object> ary) {
        return whereIn(getColumnName(property), ary);
    }
    public <C> WhereQ whereNin(Property<C, ?> property, Iterable<Object> ary) {
        return whereNin(getColumnName(property), ary);
    }


    public <C> WhereQ andEq(Property<C, ?> property, Object val) {
        return andEq(getColumnName(property), val);
    }

    public <C> WhereQ andNeq(Property<C, ?> property, Object val) {
        return andNeq(getColumnName(property), val);
    }

    public <C> WhereQ andLt(Property<C, ?> property, Object val) {
        return andLt(getColumnName(property), val);
    }

    public <C> WhereQ andLte(Property<C, ?> property, Object val) {
        return andLte(getColumnName(property), val);
    }

    public <C> WhereQ andGt(Property<C, ?> property, Object val) {
        return andGt(getColumnName(property), val);
    }

    public <C> WhereQ andGte(Property<C, ?> property, Object val) {
        return andGte(getColumnName(property), val);
    }

    public <C> WhereQ andLk(Property<C, ?> property, String val) {
        return andLk(getColumnName(property), val);
    }

    public <C> WhereQ andNlk(Property<C, ?> property, String val) {
        return andNlk(getColumnName(property), val);
    }

    public <C> WhereQ andBtw(Property<C, ?> property, Object start, Object end) {
        return andBtw(getColumnName(property), start, end);
    }
    public <C> WhereQ andNbtw(Property<C, ?> property, Object start, Object end) {
        return andNbtw(getColumnName(property), start, end);
    }
    public <C> WhereQ andIn(Property<C, ?> property, Iterable<Object> ary) {
        return andIn(getColumnName(property), ary);
    }

    public <C> WhereQ andNin(Property<C, ?> property, Iterable<Object> ary) {
        return andNin(getColumnName(property), ary);
    }

    public <C> WhereQ orEq(Property<C, ?> property, Object val) {
        return orEq(getColumnName(property), val);
    }

    public <C> WhereQ orNeq(Property<C, ?> property, Object val) {
        return orNeq(getColumnName(property), val);
    }

    public <C> WhereQ orLt(Property<C, ?> property, Object val) {
        return orLt(getColumnName(property), val);
    }

    public <C> WhereQ orLte(Property<C, ?> property, Object val) {
        return orLte(getColumnName(property), val);
    }

    public <C> WhereQ orGt(Property<C, ?> property, Object val) {
        return orGt(getColumnName(property), val);
    }

    public <C> WhereQ orGte(Property<C, ?> property, Object val) {
        return orGte(getColumnName(property), val);
    }

    public <C> WhereQ orLk(Property<C, ?> property, String val) {
        return orLk(getColumnName(property), val);
    }

    public <C> WhereQ orNlk(Property<C, ?> property, String val) {
        return orNlk(getColumnName(property), val);
    }

    public <C> WhereQ orBtw(Property<C, ?> property, Object start, Object end) {
        return orBtw(getColumnName(property), start, end);
    }
    public <C> WhereQ orNbtw(Property<C, ?> property, Object start, Object end) {
        return orNbtw(getColumnName(property), start, end);
    }
    public <C> WhereQ orIn(Property<C, ?> property, Iterable<Object> ary) {
        return orIn(getColumnName(property), ary);
    }

    public <C> WhereQ orNin(Property<C, ?> property, Iterable<Object> ary) {
        return orNin(getColumnName(property), ary);
    }


    public <C> WhereQ orderByAsc(Property<C,?> property) {
        return orderByAsc(getColumnName(property));
    }
    public <C> WhereQ orderByDesc(Property<C,?> property) {
        return orderByDesc(getColumnName(property));
    }


    public <C> WhereQ groupBy(Property<C,?> property) {
        return groupBy(getColumnName(property));
    }

}
