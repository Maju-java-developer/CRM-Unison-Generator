package org.example;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

public class MetaAttributeGenerator {

    private static final String CREATED_BY =
            "Majid.hussain";

    // =========================================================
    // COMPLETE ATTRIBUTE GENERATOR
    // =========================================================
//    public static void generateMetaEntityAttribute(List<AttributeRequest> attributes) {
//        for (AttributeRequest attribute : attributes) {
//            System.out.println(generateAttributeSQL(attribute));
//            System.out.println("\n--====================================================\n");
//        }
//    }

    public static String generateMetaEntityAttribute(
            List<AttributeRequest> requests
    ) {

        StringBuilder finalSQL =
                new StringBuilder();

        for (
                AttributeRequest request :
                requests
        ) {

            finalSQL.append(
                    generateAttributeSQL(
                            request
                    )
            );

            finalSQL.append(
                    "\n"
            );
        }

        return finalSQL.toString();
    }

    private static String generateAttributeSQL(
            AttributeRequest request
    ) {
    
        // this will generate 
        String metaEntitymetaEntityAttributeId =generatemetaEntityAttributeId();

        String metaViewmetaEntityAttributeId = generateViewmetaEntityAttributeId();

        StringBuilder sb =
                new StringBuilder();

        // =====================================================
        // META_ENTITY_ATTRIB
        // =====================================================

        sb.append("-- META ENTITY ATTRIBUTE FOR: " + request.getAttributeName())
                .append("\n");

        sb.append(generateMetaEntityAttribute(
                request,
                metaEntitymetaEntityAttributeId
        ));

        sb.append("\n\n");

        // =====================================================
        // META_VIEW_ATTRIB
        // =====================================================

        sb.append("--META VIEW ATTRIBUTE MAPPING FOR: " + request.getAttributeName())
                .append("\n");

        sb.append(generateMetaViewAttribute(
                request,
                metaEntitymetaEntityAttributeId,
                metaViewmetaEntityAttributeId
        ));

        sb.append("\n\n");

        return sb.toString();
    }

    // =========================================================
    // META ENTITY ATTRIBUTE
    // =========================================================

    private static String generateMetaEntityAttribute(
            AttributeRequest request,
            String metaEntityAttributeId
    ) {

        StringBuilder sb =
                new StringBuilder();

        sb.append("INSERT INTO MEEZAN_UNISON.dbo.META_ENTITY_ATTRIB")
                .append("\n");

        sb.append("(")
                .append("ATTRIBUTE_ID, SYSTEM_NAME, META_ENT_ID, ")
                .append("ATTRIBUTE_NAME, TABLE_COLUMN, ATTRIBUTE_TYPE, ");

        if ("PickList".equalsIgnoreCase(
                request.getAttributeType())) {

            sb.append("PICK_LIST, ");
        }

        sb.append("IS_MANDATORY, IS_SEARCH, IS_AUDIT, ")
                .append("TEMPLATE_ENABLED, CREATED_ON, ")
                .append("CREATED_BY, UPDATED_ON, UPDATED_BY, ")
                .append("ATTRIBUTE_SCOPE, OPERATOR_TYPE, ")
                .append("IS_SERIALIZED, IS_EXTERNAL_ATTRIBUTE")
                .append(")")
                .append("\n");

        sb.append("VALUES (");

        sb.append("N'")
                .append(metaEntityAttributeId)
                .append("', ");

        sb.append("N'")
                .append(request.getSystemName())
                .append("', ");

        sb.append("N'")
                .append(request.getMetaEntityId())
                .append("', ");

        sb.append("N'")
                .append(request.getAttributeName())
                .append("', ");

        sb.append("N'")
                .append(request.getTableColumn())
                .append("', ");

        sb.append("N'")
                .append(request.getAttributeType())
                .append("', ");

        // PICKLIST
        if ("PickList".equalsIgnoreCase(
                request.getAttributeType())) {

            sb.append("N'")
                    .append(request.getPickList())
                    .append("', ");
        }

        // MANDATORY
        sb.append(request.getMandatory() ? 1 : 0)
                .append(", ");

        sb.append("1, "); // IS_SEARCH

        sb.append("1, "); // IS_AUDIT

        sb.append("0, "); // TEMPLATE_ENABLED

        sb.append("GETDATE(), ");

        sb.append("N'")
                .append(CREATED_BY)
                .append("', ");

        sb.append("GETDATE(), ");

        sb.append("N'")
                .append(CREATED_BY)
                .append("', ");

        sb.append("'None', ");

        sb.append("'Like', ");

        sb.append("0, ");

        sb.append("0");

        sb.append(");");

        return sb.toString();
    }

    // =========================================================
    // META VIEW ATTRIBUTE
    // =========================================================

    private static String generateMetaViewAttribute(
            AttributeRequest request,
            String metaEntityAttributeId,
            String viewmetaEntityAttributeId
    ) {

        String componentType = AttributeType.getComponentTypeByName(request.getAttributeType());

        String constraint =
                request.getMandatory()
                        ? "IsReq()"
                        : null;

        StringBuilder sb =
                new StringBuilder();

        sb.append("INSERT INTO MEEZAN_UNISON.dbo.META_VIEW_ATTRIB")
                .append("\n");

        sb.append("(")
                .append("VIEW_ATTRIB_ID, META_VIEW_ID, ")
                .append("ATTRIBUTE_ID, META_ENT_ID, ")
                .append("SYSTEM_NAME, IS_READ_ONLY, ")
                .append("DISPLAY_ORDER, STYLE_CLASS, ")
                .append("IS_AGGREGATED, COMPONENT_TYPE, ")
                .append("DISPLAY_NAME_PRM, DISPLAY_NAME_SEC, ")
                .append("FORMAT_EXPRESSION, ")
                .append("FORMAT_EXPRESSION_SEC, ")
                .append("IS_FOREIGN_VAL_DISPLAY, ")
                .append("FOREIGN_DISPLAY_ATTRIB_ID, ")
                .append("LINK_PARAMS, CONSTRAINT_EXP, ")
                .append("IS_POSTBACK, POSTBACK_EXP, ")
                .append("LINK_URL, CREATED_ON, ")
                .append("CREATED_BY, UPDATED_ON, ")
                .append("UPDATED_BY, RENDER_AS_POPUP, ")
                .append("SELECT_EXP, CLIENT_SCRIPT_EXP, ")
                .append("FACTORY_CLASS, EDIT_EXPRESSION, ")
                .append("EVENTS, EVENT_ACTION_CLASSES, ")
                .append("EVENT_RERENDER_COMPONENTS, ")
                .append("CASCADED_ATTRIB_ID, ")
                .append("CASCADED_COLUMN, ")
                .append("EVENT_ACTION_METHOD, ")
                .append("FORMAT_EXPRESSION_FEL_ID, ")
                .append("CONFIG, ")
                .append("IS_CHECKBOX_ENABLED, ")
                .append("IS_HEADER_CHECKBOX_ENABLED")
                .append(")")
                .append("\n");

        sb.append("VALUES")
                .append("\n");

        sb.append("(");

        sb.append("N'")
                .append(viewmetaEntityAttributeId)
                .append("', ");

        sb.append("N'")
                .append(request.getMetaViewId())
                .append("', ");

        sb.append("N'")
                .append(metaEntityAttributeId)
                .append("', ");

        sb.append("N'")
                .append(request.getMetaEntityId())
                .append("', ");

        sb.append("N'")
                .append(request.getSystemName())
                .append("', ");

        sb.append("0, ");

        sb.append(request.getDisplayOrder())
                .append(", ");

        sb.append("NULL, ");

        sb.append("0, ");

        sb.append("N'")
                .append(componentType)
                .append("', ");

        sb.append("N'")
                .append(request.getAttributeName())
                .append("', ");

        sb.append("N'")
                .append(request.getAttributeName())
                .append("', ");

        sb.append("NULL, ");

        sb.append("NULL, ");

        sb.append("0, ");

        sb.append("NULL, ");

        sb.append("NULL, ");

        // MANDATORY
        if (constraint != null) {

            sb.append("N'")
                    .append(constraint)
                    .append("', ");

        } else {

            sb.append("NULL, ");
        }

        sb.append("0, ");

        sb.append("NULL, ");

        sb.append("NULL, ");

        sb.append("GETDATE(), ");

        sb.append("N'")
                .append(CREATED_BY)
                .append("', ");

        sb.append("GETDATE(), ");

        sb.append("N'")
                .append(CREATED_BY)
                .append("', ");

        sb.append("0, ");

        sb.append("NULL, ");

        sb.append("NULL, ");

        sb.append("NULL, ");

        sb.append("NULL, ");

        sb.append("NULL, ");

        sb.append("NULL, ");

        sb.append("NULL, ");

        sb.append("NULL, ");

        sb.append("NULL, ");

        sb.append("NULL, ");

        sb.append("NULL, ");

        sb.append("NULL, ");

        sb.append("0, ");

        sb.append("0");

        sb.append(");");

        return sb.toString();
    }

    // =========================================================
    // ATTRIBUTE ID
    // =========================================================

    private static String generatemetaEntityAttributeId() {

        LocalDate currentDate =
                LocalDate.now();

        String date =
                currentDate.format(
                        DateTimeFormatter.ofPattern(
                                "dd_MM_yyyy"
                        )
                );

        int random =
                1000 + new Random().nextInt(9000);

        return "_" + date + "_A" + random;
    }

    // =========================================================
    // VIEW ATTRIBUTE ID
    // =========================================================

    private static String generateViewmetaEntityAttributeId() {

        LocalDate currentDate =
                LocalDate.now();

        String date =
                currentDate.format(
                        DateTimeFormatter.ofPattern(
                                "dd_MM_yyyy"
                        )
                );

        int random =
                1000 + new Random().nextInt(9000);

        return "_" + date + "_VA" + random;
    }

    // =========================================================
    // DTO
    // =========================================================

}