package org.example.generator;

import org.example.*;
import org.example.config.*;

import java.util.ArrayList;
import java.util.List;

public class DynamicDocumentGenerator {

    public static void main(String[] args) throws Exception {

        DocumentConfiguration config =
                ConfigurationLoader.load();

        MappingConfiguration mapping = config.getMapping();

        DocumentType documentType =
                DocumentType.valueOf(config.getDocumentType());

         MetaEntityResult metaEntityResult =
                MetaEntityGenerator.generateDocument(
                        config.getDocumentName(),
                        documentType,
                        mapping.getTurnAroundTime(),
                        mapping.getEscalationStrategyId()
                );

        String metaEntityId = metaEntityResult.getMetaEntityId();

        for (ViewConfiguration view : config.getViews()) {

            MetaViewResult metaViewResult =
                    MetaViewGenerator.generateSectionView(
                            metaEntityId,
                            view.getViewName()
                    );

            List<AttributeRequest> requests = new ArrayList<>();

            for (int i = 0; i < view.getAttributes().size(); i++) {

                AttributeConfiguration attr = view.getAttributes().get(i);

                requests.add(
                        new AttributeRequest(
                                metaEntityId,
                                metaViewResult.getMetaViewId(),
                                attr.getSystemName(),
                                attr.getAttributeName(),
                                attr.getAttributeType(),
                                attr.getTableColumn(),
                                attr.getAttributeType().equalsIgnoreCase("PickList") ? attr.getPickListId() : null,
                                i + 1, // displayOrder auto generated
                                attr.isMandatory()
                        )
                );
            }

            String attributeSQL = MetaAttributeGenerator.generateMetaEntityAttribute(requests);
        }

        String sbpEntityMappingSQL = MetaEntityGenerator.generateSBPEntityMapping(
                mapping.getSbpProductId(),
                mapping.getComplaintType(),
                metaEntityId
        );

        String generateADCFacilityMappingSQL = MetaEntityGenerator.generateADCFacilityMapping(
                documentType,
                mapping.getAdcCode(),
                metaEntityId
        );

        String metaEntityCategorySQL = MetaEntityCategoryQueryBuilder.buildInsertQuery(metaEntityId, config.getValueTreeNodeId());

    }
}