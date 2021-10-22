package org.noear.weed.elasticsearch;

import org.noear.snack.ONode;
import org.noear.weed.WeedException;

import java.util.function.Consumer;

/**
 * ElasticSearch 条件构建器
 *
 * @author noear 2021/10/22 created
 */
public class EsCondition {
    protected final ONode oNode = new ONode();
    ONode oNodeArray;

    public EsCondition must() {
        oNodeArray = oNode.getOrNew("bool").getOrNew("must").asArray();
        return this;
    }

    public EsCondition should() {
        oNodeArray = oNode.getOrNew("bool").getOrNew("should").asArray();
        return this;
    }

    public EsCondition mustNot() {
        oNodeArray = oNode.getOrNew("bool").getOrNew("must_not").asArray();
        return this;
    }

    public EsCondition match(String field, Object value) {
        if (oNodeArray == null) {
            oNode.getOrNew("match").set(field, value);
        } else {
            oNodeArray.add(new ONode().build(n -> n.getOrNew("match").set(field, value)));
        }
        return this;
    }

    public EsCondition matchPrefix(String field, Object value) {
        if (oNodeArray == null) {
            oNode.getOrNew("match_phrase_prefix").set(field, value);
        } else {
            oNodeArray.add(new ONode().build(n -> n.getOrNew("match_phrase_prefix").set(field, value)));
        }
        return this;
    }

    public EsCondition term(String field, Object value) {
        if (oNodeArray == null) {
            oNode.getOrNew("term").set(field, value);
        } else {
            oNodeArray.add(new ONode().build(n -> n.getOrNew("term").set(field, value)));
        }
        return this;
    }

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
