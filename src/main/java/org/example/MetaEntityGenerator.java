package org.example;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Random;

public class MetaEntityGenerator {

    /**
     * CONSTANTS
     */
    private static final String CREATED_BY = "Majid.hussain";

    /**
     * Parent Entity Constants
     */
    private static final String COMPLAINT_PARENT_ENTITY = "0000000075";

    private static final String EFORM_PARENT_ENTITY = "2000000149";

    // =========================================================
    // COMPLAINT GENERATOR
    // =========================================================

    public static MetaEntityResult generateDocument(String entityName, DocumentType documentType, int turnAroundTime, String escalationStrategyId) {
        MetaEntityResult metaEntityResult = new MetaEntityResult();

        String metaEntityId = "";
        if (Objects.requireNonNull(documentType) == DocumentType.EFORM) {
            metaEntityResult = generateEFormSQL(entityName);
            String processAllocationSQL = WFProcessEntityAllocGenerator.generateEFormWFAllocation(metaEntityId, turnAroundTime, escalationStrategyId);
            metaEntityResult.setProcessAllocationSQL(processAllocationSQL);
        } else if (documentType == DocumentType.COMPLAINT) {
            metaEntityResult = generateComplaintSQL(entityName);
            String processAllocationSQL = WFProcessEntityAllocGenerator.generateComplaintWFAllocation(metaEntityId, turnAroundTime, escalationStrategyId);
            metaEntityResult.setProcessAllocationSQL(processAllocationSQL);
        } else {
            throw new IllegalStateException("Unexpected value: " + documentType);
        }
        return metaEntityResult;
    }

    private static MetaEntityResult generateComplaintSQL(String entityName) {
        MetaEntityResult metaEntityResult = new MetaEntityResult();

        String metaEntityId = generateMetaEntityId();

        String systemName = buildSystemName(
                "Unison.Document.Complaint",
                entityName
        );

        String metaEntityAndDetailViewGeneratedQuery = buildMetaEntityInsert(
                metaEntityId,
                systemName,
                COMPLAINT_PARENT_ENTITY,
                entityName,
                "COMPLAINT",
                "Complaint",
                1
        );
        metaEntityResult.setMetaEntityId(metaEntityId);
        metaEntityResult.setMetaEntityAndDetailViewSQL(metaEntityAndDetailViewGeneratedQuery);

        return metaEntityResult;
    }

    // =========================================================
    // EFORM GENERATOR
    // =========================================================

    private static MetaEntityResult generateEFormSQL(String entityName) {
        MetaEntityResult metaEntityResult = new MetaEntityResult();

        String metaEntityId = generateMetaEntityId();

        String systemName = buildSystemName(
                "Unison.Document.EForm",
                entityName
        );
        String metaEntityAndDetailViewGeneratedQuery = buildMetaEntityInsert(
                metaEntityId,
                systemName,
                EFORM_PARENT_ENTITY,
                entityName,
                "EFORM",
                "Eform",
                0
        );

        metaEntityResult.setMetaEntityId(metaEntityId);
        metaEntityResult.setMetaEntityAndDetailViewSQL(metaEntityAndDetailViewGeneratedQuery);

        return metaEntityResult;
    }

    // =========================================================
    // COMMON INSERT BUILDER
    // =========================================================

    private static String buildMetaEntityInsert(
            String metaEntityId,
            String systemName,
            String parentEntity,
            String entityName,
            String tableName,
            String majorType,
            int isActive
    ) {

        StringBuilder sb = new StringBuilder();
        sb.append("------------- META ENTITY -------------\n");
        sb.append("INSERT INTO MEEZAN_UNISON.dbo.META_ENTITY").append("\n");

        sb.append("(")
                .append("META_ENT_ID, SYSTEM_NAME, PARENT_ENTITY, ENTITY_NAME, ")
                .append("TABLE_NAME, IS_ABSTRACT, IS_SYSTEM, TEMPLATE_ENABLED, ")
                .append("ACTION_LOG_TABLE, ACTIVITY_LOG_TABLE, MAJOR_TYPE, ")
                .append("DESCRIPTION, META_ENT_NAME_PRM, META_ENT_NAME_SEC, ")
                .append("DISCRIMINATOR_COLUMN, DISCRIMINATOR_VALUE, CACHE_TIME, ")
                .append("CREATED_ON, CREATED_BY, UPDATED_ON, UPDATED_BY, ")
                .append("ROOT_NODE_ID, IS_ACTIVE, IS_VISIBLE_TO_CHANNELS, IS_DIRTY")
                .append(")")
                .append("\n");

        sb.append("VALUES(").append("\n");

        sb.append("    N'").append(metaEntityId).append("',").append("\n");

        sb.append("    N'").append(systemName).append("',").append("\n");

        sb.append("    N'").append(parentEntity).append("',").append("\n");

        sb.append("    N'").append(entityName).append("',").append("\n");

        sb.append("    N'").append(tableName).append("',").append("\n");

        sb.append("    0,").append("\n");

        sb.append("    0,").append("\n");

        sb.append("    1,").append("\n");

        sb.append("    NULL,").append("\n");

        sb.append("    NULL,").append("\n");

        sb.append("    N'").append(majorType).append("',").append("\n");

        sb.append("    N'").append(entityName).append("',").append("\n");

        sb.append("    N'").append(entityName).append("',").append("\n");

        sb.append("    N'").append(entityName).append("',").append("\n");

        sb.append("    NULL,").append("\n");

        sb.append("    N'").append(metaEntityId).append("',").append("\n");

        sb.append("    0,").append("\n");

        sb.append("    GETDATE(),").append("\n");

        sb.append("    N'").append(CREATED_BY).append("',").append("\n");

        sb.append("    GETDATE(),").append("\n");

        sb.append("    N'").append(CREATED_BY).append("',").append("\n");

        sb.append("    NULL,").append("\n");

        sb.append("    ").append(isActive).append(",").append("\n");

        sb.append("    1,").append("\n");

        sb.append("    NULL").append("\n");

        sb.append(");");
        sb.append("\n------------- META ENTITY END-------------").append("\n\n")
            .append("---------- META VIEW DETAIL ---------------").append("\n");
        sb.append(MetaViewGenerator.generateDetailView(metaEntityId, entityName, majorType)).append("\n");
        System.out.println("FINAL QUERY : " + sb.toString());
        return sb.toString();
    }

    // =========================================================
    // META ENTITY ID GENERATOR
    // FORMAT:
    //
    // _2026_05_13_4839201
    // =========================================================

    private static String generateMetaEntityId() {

        LocalDate currentDate = LocalDate.now();

        String formattedDate = currentDate.format(
                DateTimeFormatter.ofPattern("yyyy_MM_dd")
        );

        int randomNumber = 1000000 + new Random().nextInt(9000000);

        return "_" + formattedDate + "_" + randomNumber;
    }

    // =========================================================
    // SYSTEM NAME BUILDER
    //
    // Example:
    //
    // Campaign Van Sub-minor
    //
    // =>
    //
    // Unison.Document.Complaint.Campaign_Van_Sub_minor
    // =========================================================

    private static String buildSystemName(
            String prefix,
            String entityName
    ) {

        String formattedName = entityName
                .trim()
                .replaceAll("[^a-zA-Z0-9]+", "_")
                .replaceAll("_+", "_")
                .replaceAll("^_|_$", "");

        return prefix + "." + formattedName;
    }

    public static String generateSBPEntityMapping(
            int productId,
            int complaintType,
            String metaEntityId
    ) {

        StringBuilder sb =
                new StringBuilder();
        sb.append("\n").
            append("-------------------------------------------").append("\n").
            append("----------- SBP ENTITY MAPPING---------------").append("\n").
            append("-------------------------------------------\n");
        sb.append("INSERT INTO MEEZAN_UNISON.dbo.SBP_ENTITY_MAPPING")
                .append("\n");

        sb.append("(PRODUCT_ID, COMPLAINT_TYPE, META_ENT_ID)")
                .append("\n");

        sb.append("VALUES(");

        sb.append(productId)
                .append(", ");

        sb.append(complaintType)
                .append(", ");

        sb.append("N'")
                .append(metaEntityId)
                .append("'");

        sb.append(");");
        System.out.println("-----------------------------------");
        System.out.println("----- SBP ENTITY MAPPING ------");
        System.out.println("-----------------------------------");
        return sb.toString();
    }

    public static String generateADCFacilityMapping(
            DocumentType documentType,
            String adcCode,
            String metaEntityId
    ) {

        // ONLY FOR EFORM
        if (documentType != DocumentType.EFORM) {
            return "";
        }

        StringBuilder sb =
                new StringBuilder();
        sb.append("\n").
                append("-------------------------------------------").
                append("- ADC FACILITY EFORM TYPE MAPPING ----").
                append("-------------------------------------------\n");

        sb.append("INSERT INTO MEEZAN_UNISON.dbo.ADC_FACILITY_EFORM_TYPE_MAPPING")
                .append("\n");

        sb.append("(")
                .append("ADC_CODE, ")
                .append("META_ENT_ID, ")
                .append("CREATED_ON, ")
                .append("CREATED_BY, ")
                .append("UPDATED_ON, ")
                .append("UPDATED_BY")
                .append(")")
                .append("\n");

        sb.append("VALUES(");
        sb.append("N'")
                .append(adcCode)
                .append("', ");

        sb.append("N'")
                .append(metaEntityId)
                .append("', ");

        sb.append("GETDATE(), ");

        sb.append("N'Majid.hussain', ");

        sb.append("GETDATE(), ");

        sb.append("N'Majid.hussain'");

        sb.append(");");

        System.out.println("-----------------------------------");
        System.out.println("----- ADC_FACILITY_MAPPING ------");
        System.out.println("-----------------------------------");
        System.out.println(sb.toString());
        return sb.toString();
    }
}
