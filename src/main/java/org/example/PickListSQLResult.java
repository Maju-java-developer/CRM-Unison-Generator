package org.example;

public class PickListSQLResult {

    private final String insertSQL;
    private final String revertSQL;

    public PickListSQLResult(
            String insertSQL,
            String revertSQL
    ) {
        this.insertSQL = insertSQL;
        this.revertSQL = revertSQL;
    }

    public String getInsertSQL() {
        return insertSQL;
    }

    public String getRevertSQL() {
        return revertSQL;
    }

    /**
     * Returns complete SQL:
     *
     * 1. INSERT SQL
     * 2. REVERT SQL
     */
    public String getCompleteSQL() {

        return insertSQL
                + "\n\n"
                + "-- =====================================================\n"
                + "-- REVERT SQL\n"
                + "-- =====================================================\n\n"
                + revertSQL;
    }
}

