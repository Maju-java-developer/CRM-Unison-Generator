package org.example;

import org.example.utils.CoreUtils;

import java.util.ArrayList;
import java.util.List;

public class PickListGenerator {

    /**
     * Complete SQL Generator (INSERT)
     */
    public static String generateCompletePickListSQL(
            String pickListId,
            String groupId,
            List<String> values
    ) {
        String createdBy = "Majid.hussain";

        StringBuilder sb = new StringBuilder();

        // =========================================================
        // META_PICK_LIST INSERT
        // =========================================================

        sb.append("-- PICK LIST").append("\n");

        sb.append("INSERT INTO MEEZAN_UNISON.dbo.META_PICK_LIST ")
                .append("(PICK_LIST_ID, SYSTEM_NAME, LIST_TYPE, LINK_COLUMN_TYPE, ")
                .append("PARENT_ID, PICK_LIST_NAME, TABLE_NAME, LINK_COLUMN, ")
                .append("DISPLAY_COLUMN_PRM, DISPLAY_COLUMN_SEC, HIERARCY_LEVEL, ")
                .append("SELECT_CRITERIA, CREATED_ON, CREATED_BY, UPDATED_ON, ")
                .append("UPDATED_BY, IS_SYSTEM, IS_EXTERNAL, IS_KEY_REQUIRED)")
                .append("\n");

        sb.append("VALUES(")
                .append("N'").append(pickListId).append("', ")
                .append("N'JDBC.Unison.").append(groupId).append("', ")
                .append("N'Static', ")
                .append("N'String', ")
                .append("NULL, ")
                .append("N'").append(groupId).append("', ")
                .append("N'VALUE_TREE_NODE', ")
                .append("N'TREE_NODE_ID', ")
                .append("N'TREE_NODE_NAME_PRM', ")
                .append("N'TREE_NODE_NAME_SEC', ")
                .append("0, ")
                .append("N'").append(groupId).append(".GROUP_ID=''")
                .append(groupId).append("''', ")
                .append("GETDATE(), ")
                .append("N'").append(createdBy).append("', ")
                .append("GETDATE(), ")
                .append("N'").append(createdBy).append("', ")
                .append("0, 0, 0")
                .append(");")
                .append("\n\n");

        // =========================================================
        // VALUE_TREE_NODE INSERTS
        // =========================================================

        sb.append("-- VALUES").append("\n");

        sb.append("INSERT INTO MEEZAN_UNISON.dbo.VALUE_TREE_NODE ")
                .append("(TREE_NODE_ID, GROUP_ID, TREE_NODE_NAME_PRM, ")
                .append("TREE_NODE_NAME_SEC, DISPLAY_ORDER, SYSTEM_NAME, ")
                .append("CREATED_ON, CREATED_BY, IS_DELETED, UPDATED_BY)")
                .append(" VALUES ")
                .append("\n");

        List<String> insertValues = new ArrayList<>();

        for (int i = 0; i < values.size(); i++) {

            String fieldValue = values.get(i);

            String treeNodeId = generateTreeNodeId(pickListId, i + 1);

            String systemName = groupId + "."
                    + CoreUtils.generateSystemKey(fieldValue);

            String row = "("
                    + "N'" + treeNodeId + "', "
                    + "N'" + groupId + "', "
                    + "N'" + fieldValue + "', "
                    + "N'" + fieldValue + "', "
                    + (i + 1) + ", "
                    + "N'" + systemName + "', "
                    + "GETDATE(), "
                    + "N'" + createdBy + "', "
                    + "0, "
                    + "N'" + createdBy + "' "
                    + ")";

            insertValues.add(row);
        }

        sb.append(String.join(",\n", insertValues));

        sb.append(";");

        return sb.toString();
    }

    /**
     * Revert SQL Generator (DELETE)
     * Deletes records from VALUE_TREE_NODE using TREE_NODE_ID and META_PICK_LIST using PICK_LIST_ID
     */
    public static String generateRevertPickListSQL(
            String pickListId,
            List<String> values
    ) {
        StringBuilder sb = new StringBuilder();

        sb.append("-- REVERT SCRIPT FOR PICKLIST: ").append(pickListId).append("\n\n");

        // 1. VALUE_TREE_NODE Delete Queries
        List<String> treeNodeIds = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            treeNodeIds.add("N'" + generateTreeNodeId(pickListId, i + 1) + "'");
        }

        sb.append("-- DELETE FROM VALUE_TREE_NODE\n");
        sb.append("DELETE FROM MEEZAN_UNISON.dbo.VALUE_TREE_NODE WHERE TREE_NODE_ID IN (")
                .append(String.join(", ", treeNodeIds))
                .append(");\n\n");

        // 2. META_PICK_LIST Delete Query
        sb.append("-- DELETE FROM META_PICK_LIST\n");
        sb.append("DELETE FROM MEEZAN_UNISON.dbo.META_PICK_LIST WHERE PICK_LIST_ID = N'")
                .append(pickListId)
                .append("';");

        return sb.toString();
    }

    /**
     * Generate Tree Node ID
     */
    public static String generateTreeNodeId(
            String pickListId,
            int sequence
    ) {
        return pickListId + String.format("%02d", sequence);
    }
}