package org.noear.weed.elasticsearch;

import org.noear.snack.ONode;
import org.noear.weed.WeedException;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * ElasticSearch 条件构建器
 *
 * @author noear 2021/10/22 created
 */
public class EsCondition {
    protected final ONode oNode = new ONode();
    ONode oNodeArray;

    /**
     * bool/must
     * */
    public EsCondition must() {
        oNodeArray = oNode.getOrNew("bool").getOrNew("must").asArray();
        return this;
    }

    /**
     * bool/should
     * */
    public EsCondition should() {
        oNodeArray = oNode.getOrNew("bool").getOrNew("should").asArray();
        return this;
    }

    /**
     * bool/mustNot
     * */
    public EsCondition mustNot() {
        oNodeArray = oNode.getOrNew("bool").getOrNew("must_not").asArray();
        return this;
    }

    /**
     * match
     * */
    public EsCondition match(String field, Object value) {
        if (oNodeArray == null) {
            oNode.getOrNew("match").set(field, value);
        } else {
            oNodeArray.add(new ONode().build(n -> n.getOrNew("match").set(field, value)));
        }
        return this;
    }


    /**
     * match_phrase_prefix
     * */
    public EsCondition matchPrefix(String field, Object value) {
        if (oNodeArray == null) {
            oNode.getOrNew("match_phrase_prefix").set(field, value);
        } else {
            oNodeArray.add(new ONode().build(n -> n.getOrNew("match_phrase_prefix").set(field, value)));
        }
        return this;
    }


    /**
     * ids
     * */
    public EsCondition ids(String field, Object... values) {
        if (oNodeArray == null) {
            oNode.getOrNew("ids").getOrNew(field).addAll(Arrays.asList(values));
        } else {
            oNodeArray.add(new ONode().build(n -> n.getOrNew("ids").getOrNew(field).addAll(Arrays.asList(values))));
        }
        return this;
    }


    /**
     * term
     * */
    public EsCondition term(String field, Object value) {
        if (oNodeArray == null) {
            oNode.getOrNew("term").set(field, value);
        } else {
            oNodeArray.add(new ONode().build(n -> n.getOrNew("term").set(field, value)));
        }
        return this;
    }


    /**
     * terms
     * */
    public EsCondition termsIn(String field, Object... values) {
        if (oNodeArray == null) {
            oNode.getOrNew("terms").getOrNew(field).addAll(Arrays.asList(values));
        } else {
            oNodeArray.add(new ONode().build(n -> n.getOrNew("terms").getOrNew(field).addAll(Arrays.asList(values))));
        }
        return this;
    }

    /**
     * constant_score/filter/terms
     * */
    public EsCondition termsLike(String field, Object... values) {
        if (oNodeArray == null) {
            oNode.getOrNew("constant_score").getOrNew("filter").getOrNew("terms").getOrNew(field).addAll(Arrays.asList(values));
        } else {
            oNodeArray.add(new ONode().build(n -> n.getOrNew("constant_score").getOrNew("filter").getOrNew("terms").getOrNew(field).addAll(Arrays.asList(values))));
        }
        return this;
    }

    /**
     * constant_score/filter/range
     * */
    public EsCondition range(String field, Consumer<EsRange> range) {
        EsRange r = new EsRange();
        range.accept(r);

        if (oNodeArray == null) {
            oNode.getOrNew("constant_score").getOrNew("filter").getOrNew("range").set(field, r.oNode);
        } else {
            oNodeArray.add(new ONode().build(n -> n.getOrNew("constant_score").getOrNew("filter").getOrNew("range").set(field, r.oNode)));
        }
        return this;
    }


    /**
     * prefix
     * */
    public EsCondition prefix(String field, String value) {
        if (oNodeArray == null) {
            oNode.getOrNew("prefix").set(field, value);
        } else {
            oNodeArray.add(new ONode().build(n -> n.getOrNew("prefix").set(field, value)));
        }
        return this;
    }

    /**
     * wildcard
     *
     * @param value *表示任意字符，?表示任意单个字符(
     * */
    public EsCondition wildcard(String field, String value) {
        if (oNodeArray == null) {
            oNode.getOrNew("wildcard").set(field, value);
        } else {
            oNodeArray.add(new ONode().build(n -> n.getOrNew("wildcard").set(field, value)));
        }
        return this;
    }

    /**
     * regexp
     * */
    public EsCondition regexp(String field, String value) {
        if (oNodeArray == null) {
            oNode.getOrNew("regexp").set(field, value);
        } else {
            oNodeArray.add(new ONode().build(n -> n.getOrNew("regexp").set(field, value)));
        }
        return this;
    }
    //todo: https://www.cnblogs.com/juncaoit/p/12664109.html
    //todo: https://www.jianshu.com/p/2abd2e344dcb




    public EsCondition add(Consumer<EsCondition> condition) {
        EsCondition c = new EsCondition();
        condition.accept(c);

        if (oNodeArray != null) {
            oNodeArray.add(c.oNode);
        } else {
            throw new WeedException("Conditions lack combination types");
        }

        return this;
    }
}


/**
 * filter:过滤，不参与打分
 * must:如果有多个条件，这些条件都必须满足 and与
 * should:如果有多个条件，满足一个或多个即可 or或
 * must_not:和must相反，必须都不满足条件才可以匹配到 ！非
 * */
