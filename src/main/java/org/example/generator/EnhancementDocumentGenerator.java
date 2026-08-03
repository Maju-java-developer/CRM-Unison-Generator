//package org.example.generator;
//
//import org.example.AttributeRequest;
//import org.example.MetaAttributeGenerator;
//import org.example.MetaViewGenerator;
//import org.example.config.*;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//
//public class EnhancementDocumentGenerator {
//
//    public static void main(String[] args) throws Exception {
//
//        EnhancementConfiguration config =
//                ConfigurationLoader.enhancementDocumentLoaderConfig();
//
//        String metaEntityId = config.getMetaEntityId();
//
//        for (ViewConfiguration view : config.getViews()) {
//            if (view.getViewId() == null &&
//                    (view.getViewName() == null || view.getViewName().trim().isEmpty())) {
//
//                throw new IllegalArgumentException(
//                        "Either viewId or viewName is required"
//                );
//            }
//
//            // create New view
//            String metaViewId = null;
//            if (Objects.isNull(view.getViewId())) {
//                 metaViewId =
//                        MetaViewGenerator.generateSectionView(
//                                metaEntityId,
//                                view.getViewName()
//                        );
//            }
//            // add into existing section
//            else {
//                metaViewId = view.getViewId();
//            }
//
//            List<AttributeRequest> requests = new ArrayList<>();
//
//            for (int i = 0; i < view.getAttributes().size(); i++) {
//
//                AttributeConfiguration attr = view.getAttributes().get(i);
//
//                requests.add(
//                        new AttributeRequest(
//                                metaEntityId, metaViewId, attr.getSystemName(),
//                                attr.getAttributeName(), attr.getAttributeType(), attr.getTableColumn(),
//                                attr.getAttributeType().equalsIgnoreCase("PickList") ? attr.getPickListId() : null,
//                                i + 1, attr.isMandatory()
//                        )
//                );
//            }
//
//            MetaAttributeGenerator.generateMetaEntityAttribute(requests);
//        }
//    }
//}