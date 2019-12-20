package org.noear.weed;

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
}
