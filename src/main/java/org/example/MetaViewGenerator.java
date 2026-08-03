package org.example;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class MetaViewGenerator {

    private static final String CREATED_BY = "Majid.hussain";

    // =========================================================
    // DETAIL VIEW GENERATOR
    //
    // ALWAYS REQUIRED
    // =========================================================

    public static String generateDetailView(
            String metaEntityId,
            String entityName,
            String documentType
    ) {
        if (documentType.equalsIgnoreCase("Eform")) {
            documentType = "EForm";
        } else {
            documentType = "Complaint";
        }
        String metaViewId = generateMetaViewId();
        String systemName =
                documentType + ".View"+"."
                        + normalizeName(entityName)
                        + ".Detail";
        System.out.println("DocumentType From Details View: " + documentType);
        StringBuilder sb = new StringBuilder();

        sb.append("INSERT INTO MEEZAN_UNISON.dbo.META_VIEW").append("\n");

        sb.append("(")
                .append("META_VIEW_ID, VIEW_NAME_PRM, SYSTEM_NAME, ")
                .append("VIEW_NAME_SEC, VIEW_TYPE, META_ENT_ID, ")
                .append("DIMENSION, PLACEHOLDER_POSITION, IS_MAIN, ")
                .append("IS_DEFAULT, IS_READ_ONLY, IS_PRIMARY_LINK, ")
                .append("DISPLAY_ORDER, FACTORY_CLASS, SELECT_CRITERIA, ")
                .append("ASSOCIATED_SEARCH, PAGE_SIZE, CREATED_ON, ")
                .append("AGGREGATED_ATTRIB_ID, CREATED_BY, UPDATED_BY, ")
                .append("UPDATED_ON, ORDER_BY_CLAUSE, DATA_PROCESSOR_CLASS, ")
                .append("IFRAME_URL, IS_COLLAPSABLE_VIEW, ")
                .append("IS_COLLAPSED_STATE, IS_CONFIRMATION_ACTION, ")
                .append("stay_on_save, RESTRICTED_ACCESS, ")
                .append("VIEW_PERMISSION_ID, IS_EDITABLE")
                .append(")")
                .append("\n");

        sb.append("VALUES(");

        sb.append("N'").append(metaViewId).append("', ");

        sb.append("N'").append(entityName).append("', ");

        sb.append("N'").append(systemName).append("', ");

        sb.append("N'").append(entityName).append("', ");

        sb.append("N'Detail', ");

        sb.append("N'").append(metaEntityId).append("', ");

        sb.append("N'0', ");

        sb.append("NULL, ");

        sb.append("1, ");

        sb.append("1, ");

        sb.append("0, ");

        sb.append("1, ");

        sb.append("1, ");

        sb.append("NULL, ");

        sb.append("NULL, ");

        sb.append("NULL, ");

        sb.append("0, ");

        sb.append("GETDATE(), ");

        sb.append("NULL, ");

        sb.append("N'").append(CREATED_BY).append("', ");

        sb.append("N'").append(CREATED_BY).append("', ");

        sb.append("GETDATE(), ");

        sb.append("NULL, ");

        sb.append("NULL, ");

        sb.append("NULL, ");

        sb.append("0, ");

        sb.append("0, ");

        sb.append("0, ");

        sb.append("0, ");

        sb.append("NULL, ");

        sb.append("NULL, ");

        sb.append("1");

        sb.append(");");

        return sb.toString();
    }

    // =========================================================
    // SECTION VIEW GENERATOR
    //
    // OPTIONAL
    // =========================================================

    public static MetaViewResult generateSectionView(
            String metaEntityId,
            String sectionName
    ) {

        String metaViewId = generateMetaViewId();

        StringBuilder sb = new StringBuilder();

        sb.append("INSERT INTO MEEZAN_UNISON.dbo.META_VIEW").append("\n");

        sb.append("(")
                .append("META_VIEW_ID, VIEW_NAME_PRM, SYSTEM_NAME, ")
                .append("VIEW_NAME_SEC, VIEW_TYPE, META_ENT_ID, ")
                .append("DIMENSION, PLACEHOLDER_POSITION, IS_MAIN, ")
                .append("IS_DEFAULT, IS_READ_ONLY, IS_PRIMARY_LINK, ")
                .append("DISPLAY_ORDER, FACTORY_CLASS, SELECT_CRITERIA, ")
                .append("ASSOCIATED_SEARCH, PAGE_SIZE, CREATED_ON, ")
                .append("AGGREGATED_ATTRIB_ID, CREATED_BY, UPDATED_BY, ")
                .append("UPDATED_ON, ORDER_BY_CLAUSE, DATA_PROCESSOR_CLASS, ")
                .append("IFRAME_URL, IS_COLLAPSABLE_VIEW, ")
                .append("IS_COLLAPSED_STATE, IS_CONFIRMATION_ACTION, ")
                .append("stay_on_save, RESTRICTED_ACCESS, ")
                .append("VIEW_PERMISSION_ID, IS_EDITABLE")
                .append(")")
                .append("\n");

        sb.append("VALUES(");

        sb.append("N'").append(metaViewId).append("', ");

        sb.append("N'").append(sectionName).append("', ");

        sb.append("N'").append(sectionName).append("', ");

        sb.append("N'").append(sectionName).append("', ");

        sb.append("N'Section', ");

        sb.append("N'").append(metaEntityId).append("', ");

        sb.append("N'0x2', ");

        sb.append("NULL, ");

        sb.append("0, ");

        sb.append("0, ");

        sb.append("0, ");

        sb.append("1, ");

        sb.append("2, ");

        sb.append("NULL, ");

        sb.append("N'ifelse(isEmpty(getMapValue(getProperty(@currentObj,''values''),''CURRENT_STATE'')),''EDITABLE'',''READ_ONLY'')', ");

        sb.append("NULL, ");

        sb.append("0, ");

        sb.append("GETDATE(), ");

        sb.append("NULL, ");

        sb.append("N'").append(CREATED_BY).append("', ");

        sb.append("N'").append(CREATED_BY).append("', ");

        sb.append("GETDATE(), ");

        sb.append("NULL, ");

        sb.append("NULL, ");

        sb.append("NULL, ");

        sb.append("0, ");

        sb.append("0, ");

        sb.append("0, ");

        sb.append("0, ");

        sb.append("NULL, ");

        sb.append("NULL, ");

        sb.append("0");

        sb.append(");");

        System.out.println("");
        System.out.println(sb.toString());
        System.out.println("--------------- META VIEW SECTION END-----------");
        System.out.println("");
        return new MetaViewResult(metaViewId, sb.toString());
    }

    // =========================================================
    // META VIEW ID GENERATOR
    //
    // FORMAT:
    //
    // _2026_05_13_4839201
    // =========================================================

    public static String generateMetaViewId() {

        LocalDate currentDate = LocalDate.now();

        String formattedDate = currentDate.format(
                DateTimeFormatter.ofPattern("yyyy_MM_dd")
        );

        int randomNumber =
                1000000 + new Random().nextInt(9000000);

        return "_" + formattedDate + "_" + randomNumber;
    }

    // =========================================================
    // NORMALIZE NAME
    //
    // Example:
    //
    // Meezan To Meezan HVT
    //
    // =>
    //
    // MeezanToMeezanHVT
    // =========================================================

    public static String normalizeName(String value) {

        String[] parts = value
                .replaceAll("[^a-zA-Z0-9 ]", "")
                .trim()
                .split("\\s+");

        StringBuilder sb = new StringBuilder();

        for (String part : parts) {
            sb.append(part);
        }

        return sb.toString();
    }
}