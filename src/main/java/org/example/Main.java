package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        // for entity mapping.
        int sbpProductId = 2;
        int complaintType = 5;
        String metaEntityId = "_2019-01-03_00000100121";
//        String metaViewId = "_2024-02-28_00000000725";
        DocumentType documentType = DocumentType.EFORM;
        // Generate EForm or Complaint also will generate DetailsView
//       metaEntityId = MetaEntityGenerator.generateDocument(
//                "Campaign Van Sub-minor",
//                documentType,
//                3340,
//                "0000000001"
//        );

        // Generate Section
//        String staffAcknowledgementSection = MetaViewGenerator.generateSectionView(
//                metaEntityId,
//                "Debit Card Delivery Eform Info Section"
//        );

//        String debitCardDelivery = MetaViewGenerator.generateSectionView(
//                metaEntityId,
//                "Debit Card Delivery Eform Info Section"
//        );

        // generating metaEntAttribute and it's metaView Attribute.
        MetaAttributeGenerator.generateMetaEntityAttribute(
                getMetaAttributes(
                        "202512192",
                        "191220252_11"
                )
        );

        // generate SBP_ENTITY_MAPPING Against
//        MetaEntityGenerator.generateSBPEntityMapping(sbpProductId, complaintType, metaEntityId);
        MetaEntityGenerator.generateADCFacilityMapping(documentType, "4", metaEntityId);
//        generatePickList();
    }

    public static List<AttributeRequest> getMetaAttributes(String metaEntId, String metaViewId) {
        List<AttributeRequest> attributes = new ArrayList<>();

        // =====================================================
        // Boolean FIELD Object
        // =====================================================
//        attributes.add(new AttributeRequest(metaEntId, metaViewId,
//            "STAFF_CHECKED_BEFORE_REQUEST",
//            "Staff to be checked before initiating the request",
//            "Boolean", "BOOL_VAL4",
//            1, true)
//        );
//
        // =====================================================
        // STRING FIELD Object 
        // =====================================================
        attributes.add(new AttributeRequest(metaEntId, metaViewId, 
            "SBP_circular_violations",
            "SBP circular violations",
            "String","STRING_VAL42",
            1,false)
        );

        // =====================================================
        // PickList FIELD Object 
        // =====================================================
//        attributes.add(new AttributeRequest(metaEntId, metaViewId,
//            "BRANCH_NAME_FOR_DELIVERY",
//            "Branch Name (Where Customer Wants to MDA Card Delivery)",
//            "PickList","STRING_VAL46","0021",
//            2,true)
//        );
        return attributes;
    }

}