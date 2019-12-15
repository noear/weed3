package org.noear.weed;

public class WhereQ extends WhereBase<WhereQ> {
    protected WhereQ(DbContext context) {
        super(context);
    }

    protected WhereQ(DbTableQuery query){
        super();
        _context = query._context;
        _builder = query._builder;
    }
}
