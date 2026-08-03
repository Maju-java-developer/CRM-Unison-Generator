package org.example;

import java.util.Random;

public class WFProcessEntityAllocGenerator {

    private static final String CREATED_BY =
            "Majid.hussain";

    // =====================================================
    // EFORM DEFAULTS
    // =====================================================

    private static final String EFORM_PROCESS_CODE =
            "0000000001";

    // =====================================================
    // COMPLAINT DEFAULTS
    // =====================================================

    private static final String COMPLAINT_PROCESS_CODE =
            "0000000003";

    public static void main(String[] args) {

        // =====================================================
        // EFORM
        // =====================================================

        String eformSql =
                generateEFormWFAllocation(
                        "_4_21_2026_ME001",
                        1440,
                        "0000000001"
                );

        System.out.println(
                "================= EFORM ================="
        );

        System.out.println(eformSql);

        // =====================================================
        // COMPLAINT
        // =====================================================

        String complaintSql =
                generateComplaintWFAllocation(
                        "2000000120",
                        3360,
                        "1100000008"
                );

        System.out.println(
                "\n================= COMPLAINT ================="
        );

        System.out.println(complaintSql);
    }

    // =====================================================
    // EFORM GENERATOR
    // =====================================================

    public static String generateEFormWFAllocation(
            String documentEntity,
            int turnaroundTime,
            String escalationStrategyId
    ) {

        return buildWFAllocation(
                documentEntity,
                EFORM_PROCESS_CODE,
                turnaroundTime,
                escalationStrategyId
        );
    }

    // =====================================================
    // COMPLAINT GENERATOR
    // =====================================================

    public static String generateComplaintWFAllocation(
            String documentEntity,
            int turnaroundTime,
            String escalationStrategyId
    ) {

        return buildWFAllocation(
                documentEntity,
                COMPLAINT_PROCESS_CODE,
                turnaroundTime,
                escalationStrategyId
        );
    }

    // =====================================================
    // COMMON BUILDER
    // =====================================================

    private static String buildWFAllocation(
            String documentEntity,
            String processCode,
            int turnaroundTime,
            String escalationStrategyId
    ) {

        long processAllocId =
                generateProcessAllocationId();

        StringBuilder sb =
                new StringBuilder();
        sb.append("\n").
        append("------------------------------------------").append("\n").
        append("------ PROCESS ALLOCATION ID FOR ------").append("\n").
        append("------------------------------------------").append("\n");

        sb.append("INSERT INTO MEEZAN_UNISON.dbo.WF_PROCESS_ENTITY_ALLOC")
                .append("\n");

        sb.append("(")
                .append("PROCESS_ALLOC_ID, ")
                .append("DOCUMENT_ENTITY, ")
                .append("PROCESS_CODE, ")
                .append("TURN_AROUND_TIME, ")
                .append("SELECT_EXPRESSION, ")
                .append("CREATED_ON, ")
                .append("CREATED_BY, ")
                .append("UPDATED_ON, ")
                .append("UPDATED_BY, ")
                .append("ESC_STRATEGY_ID")
                .append(")")
                .append("\n");

        sb.append("VALUES(");

        sb.append(processAllocId)
                .append(", ");

        sb.append("N'")
                .append(documentEntity)
                .append("', ");

        sb.append("N'")
                .append(processCode)
                .append("', ");

        sb.append(turnaroundTime)
                .append(", ");

        sb.append("NULL, ");

        sb.append("GETDATE(), ");

        sb.append("N'")
                .append(CREATED_BY)
                .append("', ");

        sb.append("GETDATE(), ");

        sb.append("N'")
                .append(CREATED_BY)
                .append("', ");

        sb.append("N'")
                .append(escalationStrategyId)
                .append("'");

        sb.append(");");
        System.out.println("ProceccAllocationId query: " +sb.toString());
        return sb.toString();
    }

    // =====================================================
    // RANDOM PROCESS ALLOCATION ID
    // =====================================================

    public static long generateProcessAllocationId() {

        return 100000000L
                + new Random().nextInt(900000000);
    }
}