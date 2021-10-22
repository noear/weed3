package org.noear.weed.elasticsearch;

import org.noear.snack.ONode;

/**
 * @author noear 2021/10/23 created
 */
public class EsRange {
    protected ONode oNode = new ONode();
    public EsRange gt(Object value){
        oNode.set("gt",value);
        return this;
    }
    public EsRange gte(Object value){
        oNode.set("gte",value);
        return this;
    }

    public EsRange lt(Object value){
        oNode.set("lt",value);
        return this;
    }

    public EsRange lte(Object value){
        oNode.set("lte",value);
        return this;
    }
}
