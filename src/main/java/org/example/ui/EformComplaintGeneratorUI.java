package org.example.ui;

import org.example.*;
import org.example.utils.CoreUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EformComplaintGeneratorUI extends JFrame {

    //=========================================================
    // FIELDS & COMPONENTS
    //=========================================================
    private JTextField documentNameField;
    private JComboBox<String> documentTypeCombo;
    private final List<ViewPanel> viewPanels = new ArrayList<>();

    private JPanel complaintPanel;
    private JPanel eformPanel;

    // =====================================================
    // COMPLAINT ONLY
    // =====================================================
    private JTextField sbpProductIdField;
    private JTextField complaintTypeField;

    // =====================================================
    // COMMON FIELDS - Complaint + EForm
    // =====================================================
    private JTextField valueTreeNodeField;
    private JTextField turnAroundTimeField;
    private JTextField escalationStrategyField;

    // =====================================================
    // EFORM ONLY
    // =====================================================
    private JTextField adcCodeField;

    private JPanel viewsContainer;

    // TWO SQL AREAS: INSERT & REVERT
    private JTextArea insertSqlArea;
    private JTextArea revertSqlArea;

    public EformComplaintGeneratorUI() {
        setTitle("Complaint / EForm Generator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 900);
        setLocationRelativeTo(null);

        initUI();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(new EmptyBorder(10, 10, 10, 10));

        //----------------------------------------------------
        // CENTER CONTAINER
        //----------------------------------------------------
        JPanel centerContainer = new JPanel();
        centerContainer.setLayout(new BoxLayout(centerContainer, BoxLayout.Y_AXIS));

        centerContainer.add(createDocumentPanel());
        centerContainer.add(Box.createVerticalStrut(10));
        centerContainer.add(createMappingPanel());
        centerContainer.add(Box.createVerticalStrut(10));
        centerContainer.add(createViewContainer());

        JScrollPane centerScrollPane = new JScrollPane(centerContainer);
        centerScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        //----------------------------------------------------
        // BOTTOM CONTAINER (Generated SQL Area - INSERT & REVERT TABS)
        //----------------------------------------------------
        JPanel bottomContainer = new JPanel(new BorderLayout(5, 5));
        bottomContainer.setBorder(BorderFactory.createTitledBorder("Generated SQL Scripts"));

        insertSqlArea = new JTextArea(8, 50);
        insertSqlArea.setFont(new Font("Consolas", Font.PLAIN, 13));

        revertSqlArea = new JTextArea(8, 50);
        revertSqlArea.setFont(new Font("Consolas", Font.PLAIN, 13));

        JTabbedPane sqlTabbedPane = new JTabbedPane();
        sqlTabbedPane.addTab("Insert SQL", new JScrollPane(insertSqlArea));
        sqlTabbedPane.addTab("Revert SQL", new JScrollPane(revertSqlArea));

        bottomContainer.add(sqlTabbedPane, BorderLayout.CENTER);

        // Buttons Bar
        JPanel buttonBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 5));
        JButton addViewBtn = new JButton("+ Add View");
        addViewBtn.addActionListener(e -> addView());

        JButton generateBtn = new JButton("Generate SQL");
        generateBtn.addActionListener(e -> generateSQL());

        JButton saveBtn = new JButton("Save SQL Files");
        saveBtn.addActionListener(e -> saveSQLToFile());

        JButton clearBtn = new JButton("Clear");
        clearBtn.addActionListener(e -> {
            insertSqlArea.setText("");
            revertSqlArea.setText("");
        });

        buttonBar.add(addViewBtn);
        buttonBar.add(generateBtn);
        buttonBar.add(saveBtn);
        buttonBar.add(clearBtn);

        bottomContainer.add(buttonBar, BorderLayout.SOUTH);

        //----------------------------------------------------
        // SPLIT PANE
        //----------------------------------------------------
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                centerScrollPane,
                bottomContainer
        );
        splitPane.setResizeWeight(0.60);

        root.add(splitPane, BorderLayout.CENTER);
        add(root);

        // Default initializations
        addView();
        updateDocumentTypeUI();
    }

    private void addView() {
        ViewPanel panel = new ViewPanel(viewPanels.size() + 1);
        viewPanels.add(panel);
        viewsContainer.add(panel);
        viewsContainer.add(Box.createVerticalStrut(8));
        refreshViews();
    }

    private void removeView(ViewPanel panel) {
        viewPanels.remove(panel);
        viewsContainer.remove(panel);
        refreshViews();
    }

    private void refreshViews() {
        viewsContainer.revalidate();
        viewsContainer.repaint();
    }

    //=========================================================
    // DOCUMENT PANEL
    //=========================================================
    private JPanel createDocumentPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Document Information"));

        panel.add(new JLabel("Document Name:"));
        documentNameField = new JTextField(20);
        panel.add(documentNameField);

        panel.add(new JLabel("Document Type:"));
        documentTypeCombo = new JComboBox<>(new String[]{"COMPLAINT", "EFORM"});
        documentTypeCombo.setPreferredSize(new Dimension(150, 25));
        documentTypeCombo.addActionListener(e -> updateDocumentTypeUI());
        panel.add(documentTypeCombo);

        panel.add(
                new GeneratorNavigationPanel("Document Generator"),
                BorderLayout.NORTH
        );
        return panel;
    }

    //=========================================================
    // MAPPING PANEL
    //=========================================================
    private JPanel createMappingPanel() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBorder(BorderFactory.createTitledBorder("Document Mapping"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 12);
        gbc.anchor = GridBagConstraints.WEST;

        // COMMON FIELDS
        JPanel commonPanel = new JPanel(new GridBagLayout());
        valueTreeNodeField = new JTextField(15);
        turnAroundTimeField = new JTextField(15);
        escalationStrategyField = new JTextField(15);

        addGridCell(commonPanel, "Value Tree Node:", valueTreeNodeField, gbc, 0, 0);
        addGridCell(commonPanel, "Turn Around Time:", turnAroundTimeField, gbc, 0, 1);
        addGridCell(commonPanel, "Escalation Strategy:", escalationStrategyField, gbc, 0, 2);

        // COMPLAINT FIELDS
        complaintPanel = new JPanel(new GridBagLayout());
        sbpProductIdField = new JTextField(15);
        complaintTypeField = new JTextField(15);

        addGridCell(complaintPanel, "SBP Product Id:", sbpProductIdField, gbc, 0, 0);
        addGridCell(complaintPanel, "Complaint Type:", complaintTypeField, gbc, 0, 1);

        // EFORM FIELDS
        eformPanel = new JPanel(new GridBagLayout());
        adcCodeField = new JTextField(15);

        addGridCell(eformPanel, "ADC Code:", adcCodeField, gbc, 0, 0);

        wrapper.add(commonPanel, BorderLayout.NORTH);
        wrapper.add(complaintPanel, BorderLayout.CENTER);
        wrapper.add(eformPanel, BorderLayout.SOUTH);

        return wrapper;
    }

    private void addGridCell(JPanel panel, String label, JTextField field, GridBagConstraints gbc, int row, int col) {
        gbc.gridy = row;
        gbc.gridx = col * 2;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = (col * 2) + 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(field, gbc);
    }

    //=========================================================
    // VIEWS CONTAINER
    //=========================================================
    private JPanel createViewContainer() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Views"));

        viewsContainer = new JPanel();
        viewsContainer.setLayout(new BoxLayout(viewsContainer, BoxLayout.Y_AXIS));

        panel.add(viewsContainer, BorderLayout.CENTER);
        return panel;
    }

    private void updateDocumentTypeUI() {
        boolean complaint = documentTypeCombo.getSelectedItem().toString().equalsIgnoreCase("COMPLAINT");
        complaintPanel.setVisible(complaint);
        eformPanel.setVisible(!complaint);

        revalidate();
        repaint();
    }

    //=========================================================
    // INNER CLASSES: VIEW & ATTRIBUTE PANELS
    //=========================================================
    private class ViewPanel extends JPanel {
        private JTextField viewNameField;
        private JPanel attributeContainer;
        private final List<AttributePanel> attributePanels = new ArrayList<>();

        public ViewPanel(int no) {
            setLayout(new BorderLayout(5, 5));
            setBorder(BorderFactory.createTitledBorder("View " + no));

            JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 2));
            top.add(new JLabel("View Name:"));
            viewNameField = new JTextField(20);
            top.add(viewNameField);

            JButton removeBtn = new JButton("Remove View");
            removeBtn.addActionListener(e -> removeView(this));
            top.add(removeBtn);

            add(top, BorderLayout.NORTH);

            attributeContainer = new JPanel();
            attributeContainer.setLayout(new BoxLayout(attributeContainer, BoxLayout.Y_AXIS));
            add(attributeContainer, BorderLayout.CENTER);

            JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
            JButton addAttributeBtn = new JButton("+ Attribute");
            addAttributeBtn.addActionListener(e -> addAttribute());
            bottom.add(addAttributeBtn);

            add(bottom, BorderLayout.SOUTH);

            addAttribute();
        }

        private void addAttribute() {
            AttributePanel panel = new AttributePanel(attributePanels.size() + 1, this);
            attributePanels.add(panel);
            attributeContainer.add(panel);
            attributeContainer.add(Box.createVerticalStrut(4));
            attributeContainer.revalidate();
            attributeContainer.repaint();
        }

        private void removeAttribute(AttributePanel panel) {
            attributePanels.remove(panel);
            attributeContainer.remove(panel);
            attributeContainer.revalidate();
            attributeContainer.repaint();
        }
    }

    private class AttributePanel extends JPanel {
        private JTextField attributeNameField;
        private JComboBox<String> attributeTypeCombo;
        private JComboBox<String> tableColumnField;
        private JTextField pickListIdField;
        private JCheckBox mandatoryCheckBox;

        public AttributePanel(int no, ViewPanel parent) {
            setLayout(new FlowLayout(FlowLayout.LEFT));
            setBorder(BorderFactory.createEtchedBorder());

            add(new JLabel("Attr " + no + ":"));

            add(new JLabel("Name"));
            attributeNameField = new JTextField(12);
            add(attributeNameField);

            add(new JLabel("Type:"));
            attributeTypeCombo = new JComboBox<>(new String[]{
                    "String", "pickList", "DateTime", "Boolean", "Long", "Double", "Integer"
            });
            add(attributeTypeCombo);

            add(new JLabel("Column"));
            tableColumnField = new JComboBox<>();
            tableColumnField.setPreferredSize(new Dimension(130, 24));
            add(tableColumnField);

            add(new JLabel("pickListId"));
            pickListIdField = new JTextField(8);
            add(pickListIdField);

            mandatoryCheckBox = new JCheckBox("Mandatory");
            add(mandatoryCheckBox);

            JButton removeBtn = new JButton("X");
            removeBtn.setMargin(new Insets(2, 6, 2, 6));
            removeBtn.addActionListener(e -> parent.removeAttribute(this));
            add(removeBtn);

            attributeTypeCombo.addActionListener(e -> {
                updatepickListIdField();
                updatetableColumnFields();
            });

            updatepickListIdField();
            updatetableColumnFields();
        }

        private void updatetableColumnFields() {
            tableColumnField.removeAllItems();
            String type = (String) attributeTypeCombo.getSelectedItem();

            if ("String".equalsIgnoreCase(type) || "pickList".equalsIgnoreCase(type)) {
                int count = "pickList".equalsIgnoreCase(type) ? 80 : 60;
                for (int i = 1; i <= count; i++) {
                    tableColumnField.addItem(String.format("STRING_VAL%02d", i));
                }
            } else if ("DateTime".equalsIgnoreCase(type)) {
                for (int i = 1; i <= 20; i++) tableColumnField.addItem("DATE_VAL" + i);
            } else if ("Boolean".equalsIgnoreCase(type)) {
                for (int i = 1; i <= 20; i++) tableColumnField.addItem("BOOLEAN_VAL" + i);
            } else if ("Long".equalsIgnoreCase(type)) {
                for (int i = 1; i <= 20; i++) tableColumnField.addItem("LONG_VAL" + i);
            } else if ("Integer".equalsIgnoreCase(type)) {
                for (int i = 1; i <= 20; i++) tableColumnField.addItem("INTEGER_VAL" + i);
            } else if ("Double".equalsIgnoreCase(type)) {
                for (int i = 1; i <= 20; i++) tableColumnField.addItem("DOUBLE_VAL" + i);
            }
        }

        private void updatepickListIdField() {
            boolean enable = "pickList".equalsIgnoreCase(attributeTypeCombo.getSelectedItem().toString());
            pickListIdField.setEnabled(enable);
            if (!enable) pickListIdField.setText("");
        }

        public AttributeRequest toAttributeRequest(String metaEntityId, String metaViewId, int displayOrder) {
            String systemName = CoreUtils.generateSystemKey(attributeNameField.getText().trim());
            String attributeName = attributeNameField.getText().trim();
            String attributeType = (String) attributeTypeCombo.getSelectedItem();
            String tableColumn = tableColumnField.getSelectedItem().toString();
            String pickListId = null;

            if ("PickList".equalsIgnoreCase(attributeType)) {
                pickListId = pickListIdField.getText().trim();
                if (pickListId.isEmpty()) {
                    throw new IllegalArgumentException("PickList ID is required for PickList attribute.");
                }
            }

            if (attributeName.isEmpty()) throw new IllegalArgumentException("Attribute Name is required.");
            if (systemName.isEmpty()) throw new IllegalArgumentException("System Name is required.");
            if (tableColumn.isEmpty()) throw new IllegalArgumentException("Table Column is required.");

            return new AttributeRequest(
                    metaEntityId,
                    metaViewId,
                    systemName,
                    attributeName,
                    attributeType,
                    tableColumn,
                    pickListId,
                    displayOrder,
                    mandatoryCheckBox.isSelected()
            );
        }
    }

    //=========================================================
    // GENERATE SQL (UPDATED FOR INSERT + REVERT)
    //=========================================================
    private void generateSQL() {
        StringBuilder insertSQL = new StringBuilder();
        StringBuilder revertSQL = new StringBuilder();

        try {
            String metaEntityName = documentNameField.getText().trim();
            String sbpProductId = sbpProductIdField.getText().trim();
            String complaintType = complaintTypeField.getText().trim();
            String adcCode = adcCodeField.getText().trim();
            String TAT = turnAroundTimeField.getText().trim();
            String escalationStrategyId = escalationStrategyField.getText().trim();
            String documentType = documentTypeCombo.getSelectedItem().toString();
            DocumentType documentTypeEnum = DocumentType.valueOf(documentType);

            if (documentTypeEnum.equals(DocumentType.EFORM)) {
                if (adcCode.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "adcCode is required.");
                    return;
                }
            } else {
                if (sbpProductId.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "sbpProductId is required.");
                    return;
                }
                if (complaintType.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "complaintType is required.");
                    return;
                }
            }

            if (metaEntityName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "metaEntityName is required."); return;
            } if (TAT.isEmpty()) {
                JOptionPane.showMessageDialog(this, "TAT is required."); return;
            } if (escalationStrategyId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "escalationStrategyId is required."); return;
            }

            // Generate Base Entity SQL
            MetaEntityResult metaEntityResult = MetaEntityGenerator.generateDocument(
                    metaEntityName,
                    documentTypeEnum,
                    Integer.parseInt(TAT),
                    escalationStrategyId
            );
            String metaEntityId = metaEntityResult.getMetaEntityId();

            insertSQL.append(metaEntityResult.getMetaEntityAndDetailViewSQL())
                    .append(metaEntityResult.getProcessAllocationSQL());

            // Loop Views and Attributes
            for (ViewPanel viewPanel : viewPanels) {
                String viewName = viewPanel.viewNameField.getText().trim();
                if (viewName.isEmpty()) {
                    throw new IllegalArgumentException("View Name is required.");
                }

                insertSQL.append("\n\n");

                MetaViewResult metaViewResult = MetaViewGenerator.generateSectionView(metaEntityId, viewName);
                insertSQL.append(metaViewResult.getSql()).append("\n\n");

                List<AttributeRequest> requests = getAttributeRequests(viewPanel, metaViewResult, metaEntityId);

                if (!requests.isEmpty()) {
                    // Call updated MetaAttributeGenerator returning both Insert & Revert SQLs
                    MetaEntAttribAndViewAttribResultSQL attributeSQLResult =
                            MetaAttributeGenerator.generateMetaEntityAttribute(requests);

                    insertSQL.append(attributeSQLResult.getAttributeSQl()).append("\n\n");
                    revertSQL.append(attributeSQLResult.getRevertAttributeSQl()).append("\n\n");
                }
            }

            if (documentTypeEnum.equals(DocumentType.COMPLAINT)) {
                String sbpEntityMappingSQL = MetaEntityGenerator.generateSBPEntityMapping(
                        Integer.parseInt(sbpProductId),
                        Integer.parseInt(complaintType),
                        metaEntityId
                );
                insertSQL.append(sbpEntityMappingSQL);
            } else {
                String generateADCFacilityMappingSQL = MetaEntityGenerator.generateADCFacilityMapping(
                        documentTypeEnum,
                        adcCode,
                        metaEntityId
                );
                insertSQL.append(generateADCFacilityMappingSQL);
            }

            String metaEntityCategorySQL = MetaEntityCategoryQueryBuilder.buildInsertQuery(
                    metaEntityId, valueTreeNodeField.getText().trim()
            );
            insertSQL.append(metaEntityCategorySQL);

            // Populate both text areas
            insertSqlArea.setText(insertSQL.toString());
            revertSqlArea.setText(revertMetaEntity(documentTypeEnum, metaEntityId, revertSQL.toString()));
//            revertSqlArea.setText(revertSQL.toString());

            JOptionPane.showMessageDialog(this, "SQL generated successfully.");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static String revertMetaEntity(DocumentType documentType, String metaEntityId, String attributeRevert) {
        StringBuilder revertSQL = new StringBuilder();

        revertSQL.append("\n-------- REVERT FOR TREE NODE ------------ \n").
                append("DELETE FROM MEEZAN_UNISON.dbo.META_ENTITY_CATEGORY WHERE META_ENT_ID =").
                append("N'").append(metaEntityId).append("';").append("\n");

        if (documentType.equals(DocumentType.COMPLAINT)) {
            revertSQL.append("\n-------- REVERT SBP MAPPING ------------ \n").
                    append("DELETE FROM MEEZAN_UNISON.dbo.SBP_ENTITY_MAPPING WHERE META_ENT_ID =").
                    append("N'").append(metaEntityId).append("';").append("\n");
        } else {
            revertSQL.append("\n------ REVERT FOR ADC FACILITY MAPPING -------- \n").
                    append("DELETE FROM MEEZAN_UNISON.dbo.ADC_FACILITY_EFORM_TYPE_MAPPING WHERE META_ENT_ID =").
                    append("N'").append(metaEntityId).append("';").append("\n");
        }
        revertSQL.append(attributeRevert);

        revertSQL.append("\n-------- REVERT WF_PROCESS_ENTITY_ALLOC ------------ \n").
                append("DELETE FROM MEEZAN_UNISON.dbo.WF_PROCESS_ENTITY_ALLOC WHERE DOCUMENT_ENTITY =").
                append("N'").append(metaEntityId).append("';").append("\n");

        revertSQL.append("\n-------- REVERT META VIEW ------------ \n").
                append("DELETE FROM MEEZAN_UNISON.dbo.META_VIEW WHERE META_ENT_ID =").
                append("N'").append(metaEntityId).append("';").append("\n");

        revertSQL.append("\n-------- REVERT META ENTITY ------------ \n").
                append("DELETE FROM MEEZAN_UNISON.dbo.META_ENTITY WHERE META_ENT_ID =").
                append("N'").append(metaEntityId).append("';").append("\n");

        return revertSQL.toString();
    }

    private static List<AttributeRequest> getAttributeRequests(ViewPanel viewPanel, MetaViewResult metaViewResult, String metaEntityId) {
        String metaViewId = metaViewResult.getMetaViewId();
        List<AttributeRequest> requests = new ArrayList<>();

        for (int i = 0; i < viewPanel.attributePanels.size(); i++) {
            AttributePanel attr = viewPanel.attributePanels.get(i);
            AttributeRequest request = attr.toAttributeRequest(metaEntityId, metaViewId, i + 1);
            requests.add(request);
        }
        return requests;
    }

    //=========================================================
    // SAVE SQL FILES (UPDATED TO DIRECTORY STRUCTURE)
    //=========================================================
    private void saveSQLToFile() {
        String insertSql = insertSqlArea.getText();
        String revertSql = revertSqlArea.getText();

        if ((insertSql == null || insertSql.trim().isEmpty()) &&
                (revertSql == null || revertSql.trim().isEmpty())) {
            JOptionPane.showMessageDialog(this, "There is no SQL to save.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String documentName = documentNameField.getText().trim();
        String documentType = documentTypeCombo.getSelectedItem().toString();

        if (documentName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Document Name is required to save files.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Root Directory to Save SQL Files");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int result = fileChooser.showSaveDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File baseDir = fileChooser.getSelectedFile();

        // 1. Current Date Folder (e.g. 2026-08-04)
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // 2. Folder Name: <DocumentType>_<DocumentName>
        String folderName = documentType + "_" + documentName;

        File targetDirectory = new File(baseDir, currentDate + File.separator + folderName);

        if (!targetDirectory.exists()) {
            targetDirectory.mkdirs();
        }

        // File names
        File insertFile = new File(targetDirectory, documentName + "_Insert.sql");
        File revertFile = new File(targetDirectory, documentName + "_Revert.sql");

        try {
            // Write Insert SQL File
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(insertFile))) {
                writer.write(insertSql);
            }

            // Write Revert SQL File
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(revertFile))) {
                writer.write(revertSql);
            }

            JOptionPane.showMessageDialog(
                    this,
                    "SQL files saved successfully!\n\nLocation:\n" + targetDirectory.getAbsolutePath(),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to save SQL files:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(EformComplaintGeneratorUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            new EformComplaintGeneratorUI().setVisible(true);
        });
    }
}