package org.noear.weed.generator.entity;

import org.noear.weed.DbContext;
import org.noear.weed.generator.entity.block.TableItem;
import org.noear.weed.generator.entity.block.XmlEntityBlock;
import org.noear.weed.generator.entity.block.XmlSourceBlock;
import org.noear.weed.generator.entity.block.XmlTableBlock;
import org.noear.weed.generator.utils.NamingUtils;
import org.noear.weed.generator.utils.StringUtils;
import org.noear.weed.generator.utils.XmlUtils;
import org.noear.weed.wrap.TableWrap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class XmlParser {

    public static XmlSourceBlock getSource(Node n1) throws Exception {
        if (Names.tag_source.equals(n1.getNodeName()) == false) {
            return null;
        }

        XmlSourceBlock source = new XmlSourceBlock();

        source.schema = XmlUtils.attr(n1, Names.att_schema);
        source.url = XmlUtils.attr(n1, Names.att_url);
        source.username = XmlUtils.attr(n1, Names.att_username);
        source.password = XmlUtils.attr(n1, Names.att_password);
        source.driverClassName = XmlUtils.attr(n1, Names.att_driverClassName);
        source.namingStyle = XmlUtils.attr(n1, Names.att_namingStyle);

        NodeList n2l = n1.getChildNodes();
        for (int i = 0, len = n2l.getLength(); i < len; i++) {
            Node n2 = n2l.item(i);

            if (n2.getNodeType() != 1) {
                continue;
            }

            if (Names.tag_entity.equals(n2.getNodeName())) {
                XmlEntityBlock bk = new XmlEntityBlock();
                bk.targetPackage = XmlUtils.attr(n2, Names.att_targetPackage);
                bk.entityName = XmlUtils.attr(n2, Names.att_entityName);
                bk.code = getCode(n2);

                source.entityBlocks.add(bk);
            }

            if (Names.tag_table.equals(n2.getNodeName())) {
                XmlTableBlock bk = new XmlTableBlock();
                bk.tableName = XmlUtils.attr(n2, Names.att_tableName);
                bk.domainName = XmlUtils.attr(n2, Names.att_domainName);

                source.tableBlocks.add(bk);
            }
        }

        return source;
    }

    public static String getCode(Node n2){
        StringBuilder sb = new StringBuilder();

        NodeList nl = n2.getChildNodes();

        for(int i=0,len=nl.getLength(); i<len; i++){
            Node n3 = nl.item(i);
            sb.append(n3.getTextContent());
        }

        return sb.toString();
    }

    public static List<TableItem> getTables(XmlSourceBlock source, DbContext db){
        List<TableItem> tableItems = new ArrayList<>();

        for(XmlTableBlock tb : source.tableBlocks){
            if("*".equals(tb.tableName)){
                for(TableWrap tw : db.dbTables()){
                    TableItem item = new TableItem();
                    item.tableName = tw.getName();
                    item.domainName = NamingUtils.toCamelString(item.tableName,true);
                    item.tableWrap = tw;

                    tableItems.add(item);
                }

                break;
            }else{
                for(TableWrap tw : db.dbTables()){
                    if(tw.getName().equals(tb.tableName)) {
                        TableItem item = new TableItem();
                        item.tableName = tw.getName();
                        if(StringUtils.isEmpty(tb.domainName)) {
                            item.domainName = NamingUtils.toCamelString(item.tableName, true);
                        }else{
                            item.domainName = tb.domainName;
                        }

                        item.tableWrap = tw;

                        tableItems.add(item);
                    }
                }
            }
        }

        return tableItems;
    }
}
