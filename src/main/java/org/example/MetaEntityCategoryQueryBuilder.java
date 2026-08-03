package org.example;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class MetaEntityCategoryQueryBuilder {
    public static String buildInsertQuery(String metaEntityId, String treeNodeId) {

        String engCategoryId = generateCategoryId();
        StringBuilder sb = new StringBuilder();
        String query = "INSERT INTO MEEZAN_UNISON.dbo.META_ENTITY_CATEGORY "
                + "(ENT_CATEGORY_ID, META_ENT_ID, TREE_NODE_ID, CREATED_ON, CREATED_BY, "
                + "UPDATED_ON, UPDATED_BY, ACTION_CLASS, IS_ACTIVE) "
                + "VALUES("
                + "N'" + engCategoryId + "', "
                + "N'" + metaEntityId + "', "
                + "N'" + treeNodeId + "', "
                + "GETDATE(), "
                + "N'majid.hussain', "
                + "GETDATE(), "
                + "N'majid.hussain', "
                + "NULL, "
                + "1);";

        System.out.println();
        sb.append("\n\n-----------------------------------");
        sb.append("\n----- META CATEGORY ------");
        sb.append("\n-----------------------------------\n");
        sb.append(query);
        return sb.toString();
    }

    private static String generateCategoryId() {
        String currentDate = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        int random = 1000 + new Random().nextInt(9000); // 4-digit random
        return currentDate + "_" + random;
    }
}
