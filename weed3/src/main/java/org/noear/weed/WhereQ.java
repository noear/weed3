package org.noear.weed;

public class WhereQ extends WhereBase<WhereQ> {
    public WhereQ(DbContext context) {
        super(context);
    }

    public WhereQ(DbTableQuery query){
        super();
        _context = query._context;
        _builder = query._builder;
    }
}
