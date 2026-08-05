package org.example.ui;

import org.example.AttributeRequest;
import org.example.MetaAttributeGenerator;
import org.example.MetaEntAttribAndViewAttribResultSQL;
import org.example.MetaViewGenerator;
import org.example.MetaViewResult;
import org.example.utils.CoreUtils;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EnhancementDocumentGeneratorUI extends JFrame {

    private JTextField metaEntityIdField;

    private JPanel viewsContainer;

    // Split SQL output into Insert and Revert TextAreas
    private JTextArea insertSqlOutput;
    private JTextArea revertSqlOutput;

    private final List<ViewPanel> viewPanels = new ArrayList<>();

    public EnhancementDocumentGeneratorUI() {

        setTitle("Enhancement Document Generator");

        setSize(1200, 800);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        initUI();
    }

    private void initUI() {

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        // =====================================================
        // TOP
        // =====================================================

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        topPanel.add(new JLabel("Meta Entity ID:"));

        metaEntityIdField = new JTextField(30);

        topPanel.add(metaEntityIdField);
        topPanel.add(
                new GeneratorNavigationPanel("Enhancement Document"),
                BorderLayout.NORTH
        );
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // =====================================================
        // VIEWS
        // =====================================================

        viewsContainer = new JPanel();

        viewsContainer.setLayout(
                new BoxLayout(viewsContainer, BoxLayout.Y_AXIS)
        );

        JScrollPane viewsScrollPane = new JScrollPane(viewsContainer);

        mainPanel.add(viewsScrollPane, BorderLayout.CENTER);

        // =====================================================
        // BOTTOM (Generated SQL Tabs & Action Buttons)
        // =====================================================

        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton addViewButton = new JButton("+ Add View");
        addViewButton.addActionListener(e -> addView());

        JButton generateButton = new JButton("Generate SQL");
        generateButton.addActionListener(e -> generateSQL());

        JButton saveToFileButton = new JButton("Save To File");
        saveToFileButton.addActionListener(e -> saveSQLToFile());

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> {
            insertSqlOutput.setText("");
            revertSqlOutput.setText("");
        });

        buttonsPanel.add(addViewButton);
        buttonsPanel.add(generateButton);
        buttonsPanel.add(saveToFileButton);
        buttonsPanel.add(clearButton);

        bottomPanel.add(buttonsPanel, BorderLayout.NORTH);

        // Tabbed Pane for Insert & Revert SQL
        insertSqlOutput = new JTextArea(12, 100);
        insertSqlOutput.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));

        revertSqlOutput = new JTextArea(12, 100);
        revertSqlOutput.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));

        JTabbedPane sqlTabbedPane = new JTabbedPane();
        sqlTabbedPane.addTab("Insert SQL", new JScrollPane(insertSqlOutput));
        sqlTabbedPane.addTab("Revert SQL", new JScrollPane(revertSqlOutput));

        bottomPanel.add(sqlTabbedPane, BorderLayout.CENTER);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Start with one view
        addView();
    }

    // =========================================================
    // SAVE TO FILE
    // =========================================================
    private void saveSQLToFile() {

        String insertSql = insertSqlOutput.getText();
        String revertSql = revertSqlOutput.getText();

        if ((insertSql == null || insertSql.trim().isEmpty()) &&
                (revertSql == null || revertSql.trim().isEmpty())) {

            JOptionPane.showMessageDialog(
                    this,
                    "There is no SQL to save.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String metaEntityId = metaEntityIdField.getText().trim();
        if (metaEntityId.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Meta Entity ID is required to save SQL files.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Directory to Save SQL Files");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int result = fileChooser.showSaveDialog(this);

        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File baseDir = fileChooser.getSelectedFile();

        // Folder structure: current_date/metaEntityId/
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        File targetDir = new File(baseDir, currentDate + File.separator + metaEntityId);

        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }

        // Output File paths
        File insertFile = new File(targetDir, metaEntityId + "_Insert.sql");
        File revertFile = new File(targetDir, metaEntityId + "_Revert.sql");

        try {
            // Write Insert SQL
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(insertFile))) {
                writer.write(insertSql);
            }

            // Write Revert SQL
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(revertFile))) {
                writer.write(revertSql);
            }

            JOptionPane.showMessageDialog(
                    this,
                    "SQL files saved successfully!\n\nFolder Path:\n" + targetDir.getAbsolutePath(),
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

    // =========================================================
    // ADD VIEW
    // =========================================================
    private void addView() {

        ViewPanel viewPanel = new ViewPanel(viewPanels.size() + 1);

        viewPanels.add(viewPanel);

        viewsContainer.add(viewPanel);

        viewsContainer.add(Box.createVerticalStrut(10));

        refreshUI();
    }

    // =========================================================
    // REMOVE VIEW
    // =========================================================

    private void removeView(ViewPanel viewPanel) {

        viewPanels.remove(viewPanel);

        viewsContainer.remove(viewPanel);

        refreshUI();
    }

    // =========================================================
    // GENERATE SQL
    // =========================================================

    private void generateSQL() {

        try {

            String metaEntityId = metaEntityIdField.getText().trim();

            if (metaEntityId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Meta Entity ID is required.");
                return;
            }

            if (viewPanels.isEmpty()) {
                JOptionPane.showMessageDialog(this, "At least one View is required.");
                return;
            }

            StringBuilder finalInsertSQL = new StringBuilder();
            StringBuilder finalRevertSQL = new StringBuilder();

            for (ViewPanel viewPanel : viewPanels) {

                // =================================================
                // VIEW ID
                // =================================================

                String metaViewId;

                if (viewPanel.existingViewCheckBox.isSelected()) {

                    metaViewId = viewPanel.viewIdField.getText().trim();

                    if (metaViewId.isEmpty()) {
                        throw new IllegalArgumentException("View ID is required.");
                    }

                } else {

                    String viewName = viewPanel.viewNameField.getText().trim();

                    if (viewName.isEmpty()) {
                        throw new IllegalArgumentException("View Name is required.");
                    }

                    finalInsertSQL.append("-- ===============================\n");
                    finalInsertSQL.append("-- NEW VIEW\n");
                    finalInsertSQL.append("-- ===============================\n");

                    MetaViewResult metaViewResult = MetaViewGenerator.generateSectionView(metaEntityId, viewName);

                    finalInsertSQL.append(metaViewResult.getSql());
                    metaViewId = metaViewResult.getMetaViewId();
                    finalInsertSQL.append("\n\n");
                    finalRevertSQL.append("\n-------- REVERT META VIEW ------------ \n").
                            append("DELETE FROM MEEZAN_UNISON.dbo.META_VIEW WHERE META_VIEW_ID =").
                            append("N'").append(metaViewResult.getMetaViewId()).append("';").append("\n");
                }

                // =================================================
                // ATTRIBUTES
                // =================================================

                List<AttributeRequest> requests = new ArrayList<>();

                for (int i = 0; i < viewPanel.attributePanels.size(); i++) {

                    AttributePanel attr = viewPanel.attributePanels.get(i);

                    AttributeRequest request = attr.toAttributeRequest(
                            metaEntityId,
                            metaViewId,
                            i + 1
                    );

                    requests.add(request);
                }

                if (!requests.isEmpty()) {

                    MetaEntAttribAndViewAttribResultSQL attributeSQLResult =
                            MetaAttributeGenerator.generateMetaEntityAttribute(requests);

                    finalInsertSQL.append(attributeSQLResult.getAttributeSQl()).append("\n\n");
                    finalRevertSQL.append(attributeSQLResult.getRevertAttributeSQl()).append("\n\n");
                }
            }

            insertSqlOutput.setText(finalInsertSQL.toString());
            revertSqlOutput.setText(finalRevertSQL.toString());

            JOptionPane.showMessageDialog(
                    this,
                    "SQL generated successfully."
            );

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // REFRESH UI
    // =========================================================

    private void refreshUI() {

        viewsContainer.revalidate();

        viewsContainer.repaint();
    }

    // =========================================================
    // VIEW PANEL
    // =========================================================

    private class ViewPanel extends JPanel {

        private final JCheckBox existingViewCheckBox;

        private final JTextField viewIdField;

        private final JTextField viewNameField;

        private final JPanel attributesContainer;

        private final List<AttributePanel> attributePanels = new ArrayList<>();

        public ViewPanel(int viewNumber) {

            setLayout(new BorderLayout(5, 5));

            setBorder(BorderFactory.createTitledBorder("View " + viewNumber));

            // =================================================
            // VIEW HEADER
            // =================================================

            JPanel viewHeader = new JPanel(new FlowLayout(FlowLayout.LEFT));

            existingViewCheckBox = new JCheckBox("Existing View");

            viewHeader.add(existingViewCheckBox);

            viewHeader.add(new JLabel("View ID:"));

            viewIdField = new JTextField(25);

            viewHeader.add(viewIdField);

            viewHeader.add(new JLabel("New View Name:"));

            viewNameField = new JTextField(25);

            viewHeader.add(viewNameField);

            JButton removeViewButton = new JButton("Remove View");

            removeViewButton.addActionListener(e -> removeView(this));

            viewHeader.add(removeViewButton);

            add(viewHeader, BorderLayout.NORTH);

            // =================================================
            // ATTRIBUTE CONTAINER
            // =================================================

            attributesContainer = new JPanel();

            attributesContainer.setLayout(
                    new BoxLayout(attributesContainer, BoxLayout.Y_AXIS)
            );

            add(new JScrollPane(attributesContainer), BorderLayout.CENTER);

            // =================================================
            // BUTTONS
            // =================================================

            JPanel attributeButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));

            JButton addAttributeButton = new JButton("+ Add Attribute");

            addAttributeButton.addActionListener(e -> addAttribute());

            attributeButtons.add(addAttributeButton);

            add(attributeButtons, BorderLayout.SOUTH);

            // =================================================
            // VIEW TYPE TOGGLE
            // =================================================

            existingViewCheckBox.addActionListener(e -> updateViewMode());

            updateViewMode();

            // Start with one attribute
            addAttribute();
        }

        private void updateViewMode() {

            boolean existing = existingViewCheckBox.isSelected();

            viewIdField.setEnabled(existing);

            viewNameField.setEnabled(!existing);
        }

        private void addAttribute() {

            AttributePanel attributePanel = new AttributePanel(attributePanels.size() + 1);

            attributePanels.add(attributePanel);

            attributesContainer.add(attributePanel);

            attributesContainer.add(Box.createVerticalStrut(5));

            refreshUI();
        }

        private void removeAttribute(AttributePanel panel) {

            attributePanels.remove(panel);

            attributesContainer.remove(panel);

            refreshUI();
        }
    }

    // =========================================================
    // ATTRIBUTE PANEL
    // =========================================================

    private class AttributePanel extends JPanel {

        private final JTextField attributeNameField;

        private final JComboBox<String> attributeTypeCombo;

        private final JComboBox<String> tableColumnField;

        private final JTextField pickListIdField;

        private final JCheckBox mandatoryCheckBox;

        public AttributePanel(int attributeNumber) {

            setLayout(new FlowLayout(FlowLayout.LEFT));

            setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

            add(new JLabel("Attribute " + attributeNumber + ":"));

            // =================================================
            // ATTRIBUTE NAME
            // =================================================

            add(new JLabel("Attribute Name:"));

            attributeNameField = new JTextField(20);

            add(attributeNameField);

            // =================================================
            // ATTRIBUTE TYPE
            // =================================================

            add(new JLabel("Type:"));

            attributeTypeCombo = new JComboBox<>(
                    new String[]{
                            "String",
                            "PickList",
                            "DateTime",
                            "Boolean",
                            "Long",
                            "Double",
                            "Integer"
                    }
            );

            add(attributeTypeCombo);

            // =================================================
            // TABLE COLUMN
            // =================================================

            add(new JLabel("Column"));
            tableColumnField = new JComboBox<>();
            tableColumnField.setPreferredSize(new Dimension(130, 24));
            add(tableColumnField);

            // =================================================
            // PICKLIST ID
            // =================================================

            add(new JLabel("PickList ID:"));

            pickListIdField = new JTextField(10);

            add(pickListIdField);

            // =================================================
            // MANDATORY
            // =================================================

            mandatoryCheckBox = new JCheckBox("Mandatory");

            add(mandatoryCheckBox);

            // =================================================
            // REMOVE
            // =================================================

            JButton removeButton = new JButton("Remove");

            removeButton.addActionListener(e -> removeAttributePanel(this));

            add(removeButton);

            // =================================================
            // TYPE CHANGE
            // =================================================

            attributeTypeCombo.addActionListener(e -> {
                        updatePickListField();
                        updatetableColumnFields();
                    }
            );
            updatePickListField();
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

        private void updatePickListField() {
            boolean isPickList = "PickList".equalsIgnoreCase((String) attributeTypeCombo.getSelectedItem());
            pickListIdField.setEnabled(isPickList);

            if (!isPickList) {
                pickListIdField.setText("");
            }
        }

        private void removeAttributePanel(AttributePanel panel) {
            for (ViewPanel viewPanel : viewPanels) {
                if (viewPanel.attributePanels.contains(panel)) {
                    viewPanel.removeAttribute(panel);
                    break;
                }
            }
        }

        // =====================================================
        // CREATE ATTRIBUTE REQUEST
        // =====================================================

        public AttributeRequest toAttributeRequest(
                String metaEntityId,
                String metaViewId,
                int displayOrder
        ) {

            String systemName = CoreUtils.generateSystemKey(attributeNameField.getText().trim());

            String attributeName = attributeNameField.getText().trim();

            String attributeType = (String) attributeTypeCombo.getSelectedItem();

            String tableColumn = Objects.requireNonNull(tableColumnField.getSelectedItem()).toString();

            String pickListId = null;

            if ("PickList".equalsIgnoreCase(attributeType)) {

                pickListId = pickListIdField.getText().trim();

                if (pickListId.isEmpty()) {
                    throw new IllegalArgumentException(
                            "PickList ID is required for PickList attribute."
                    );
                }
            }
            if (attributeName.isEmpty()) {
                throw new IllegalArgumentException("Attribute Name is required.");
            }
            if (systemName.isEmpty()) {
                throw new IllegalArgumentException("System Name is required.");
            }
            if (tableColumn.isEmpty()) {
                throw new IllegalArgumentException("Table Column is required.");
            }
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

    // =========================================================
    // MAIN
    // =========================================================

    public static void main(String[] args) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(EnhancementDocumentGeneratorUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        SwingUtilities.invokeLater(() -> {
            EnhancementDocumentGeneratorUI frame = new EnhancementDocumentGeneratorUI();
            frame.setVisible(true);
        });
    }
}