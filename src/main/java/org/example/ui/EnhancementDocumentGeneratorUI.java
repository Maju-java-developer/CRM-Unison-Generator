package org.example.ui;

import org.example.AttributeRequest;
import org.example.MetaAttributeGenerator;
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

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EnhancementDocumentGeneratorUI extends JFrame {

    private JTextField metaEntityIdField;

    private JPanel viewsContainer;

    private JTextArea sqlOutput;

    private final List<ViewPanel> viewPanels =
            new ArrayList<>();

    public EnhancementDocumentGeneratorUI() {

        setTitle("Enhancement Document Generator");

        setSize(1200, 800);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setLocationRelativeTo(null);

        initUI();
    }

    private void initUI() {

        JPanel mainPanel =
                new JPanel(new BorderLayout(10, 10));

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );

        // =====================================================
        // TOP
        // =====================================================

        JPanel topPanel =
                new JPanel(new FlowLayout(
                        FlowLayout.LEFT
                ));

        topPanel.add(
                new JLabel("Meta Entity ID:")
        );

        metaEntityIdField =
                new JTextField(30);

        topPanel.add(
                metaEntityIdField
        );
        topPanel.add(
                new GeneratorNavigationPanel(
                        "Enhancement Document"
                ),
                BorderLayout.NORTH
        );
        mainPanel.add(
                topPanel,
                BorderLayout.NORTH
        );

        // =====================================================
        // VIEWS
        // =====================================================

        viewsContainer =
                new JPanel();

        viewsContainer.setLayout(
                new BoxLayout(
                        viewsContainer,
                        BoxLayout.Y_AXIS
                )
        );

        JScrollPane viewsScrollPane =
                new JScrollPane(
                        viewsContainer
                );

        mainPanel.add(
                viewsScrollPane,
                BorderLayout.CENTER
        );

        // =====================================================
        // BOTTOM
        // =====================================================

        JPanel bottomPanel =
                new JPanel(
                        new BorderLayout(
                                5,
                                5
                        )
                );

        JPanel buttonsPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        JButton addViewButton =
                new JButton(
                        "+ Add View"
                );

        addViewButton.addActionListener(
                e -> addView()
        );

        JButton generateButton =
                new JButton(
                        "Generate SQL"
                );

        JButton saveToFileButton =
                new JButton("Save To File");

        saveToFileButton.addActionListener(
                e -> saveSQLToFile()
        );

        generateButton.addActionListener(
                e -> generateSQL()
        );

        buttonsPanel.add(
                addViewButton
        );

        buttonsPanel.add(
                generateButton
        );

        buttonsPanel.add(
                saveToFileButton
        );

        bottomPanel.add(
                buttonsPanel,
                BorderLayout.NORTH
        );

        sqlOutput =
                new JTextArea(
                        12,
                        100
                );

        sqlOutput.setFont(
                new Font(
                        Font.MONOSPACED,
                        Font.PLAIN,
                        13
                )
        );

        sqlOutput.setEditable(
                false
        );

        bottomPanel.add(
                new JScrollPane(
                        sqlOutput
                ),
                BorderLayout.CENTER
        );

        mainPanel.add(
                bottomPanel,
                BorderLayout.SOUTH
        );

        add(
                mainPanel
        );

        // Start with one view
        addView();
    }

    // =========================================================
    // ADD VIEW
    // =========================================================
    private void saveSQLToFile() {

        String sql =
                sqlOutput.getText();

        // Check if TextArea is empty
        if (
                sql == null
                        ||
                        sql.trim().isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "There is no SQL to save.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        JFileChooser fileChooser =
                new JFileChooser();

        fileChooser.setDialogTitle(
                "Save SQL Script"
        );

        // Default file name
        fileChooser.setSelectedFile(
                new File(
                        "generated_script.sql"
                )
        );

        int result =
                fileChooser.showSaveDialog(
                        this
                );

        if (
                result !=
                        JFileChooser.APPROVE_OPTION
        ) {

            return;
        }

        File selectedFile =
                fileChooser.getSelectedFile();

        String filePath =
                selectedFile.getAbsolutePath();

        // Automatically add .sql extension
        if (
                !filePath
                        .toLowerCase()
                        .endsWith(".sql")
        ) {

            filePath += ".sql";
        }

        try (
                BufferedWriter writer =
                        new BufferedWriter(
                                new FileWriter(
                                        filePath
                                )
                        )
        ) {

            writer.write(sql);

            JOptionPane.showMessageDialog(
                    this,
                    "SQL file saved successfully.\n\n"
                            + filePath,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (
                IOException ex
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to save SQL file:\n"
                            + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
    private void addView() {

        ViewPanel viewPanel =
                new ViewPanel(
                        viewPanels.size() + 1
                );

        viewPanels.add(
                viewPanel
        );

        viewsContainer.add(
                viewPanel
        );

        viewsContainer.add(
                Box.createVerticalStrut(10)
        );

        refreshUI();
    }

    // =========================================================
    // REMOVE VIEW
    // =========================================================

    private void removeView(
            ViewPanel viewPanel
    ) {

        viewPanels.remove(
                viewPanel
        );

        viewsContainer.remove(
                viewPanel
        );

        refreshUI();
    }

    // =========================================================
    // GENERATE SQL
    // =========================================================

    private void generateSQL() {

        try {

            String metaEntityId =
                    metaEntityIdField
                            .getText()
                            .trim();

            if (metaEntityId.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Meta Entity ID is required."
                );

                return;
            }

            if (viewPanels.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "At least one View is required."
                );

                return;
            }

            StringBuilder finalSQL =
                    new StringBuilder();

            for (
                    ViewPanel viewPanel :
                    viewPanels
            ) {

                // =================================================
                // VIEW ID
                // =================================================

                String metaViewId;

                if (
                        viewPanel
                                .existingViewCheckBox
                                .isSelected()
                ) {

                    metaViewId =
                            viewPanel
                                    .viewIdField
                                    .getText()
                                    .trim();

                    if (metaViewId.isEmpty()) {

                        throw new IllegalArgumentException(
                                "View ID is required."
                        );
                    }

                } else {

                    String viewName =
                            viewPanel
                                    .viewNameField
                                    .getText()
                                    .trim();

                    if (viewName.isEmpty()) {

                        throw new IllegalArgumentException(
                                "View Name is required."
                        );
                    }

                    finalSQL.append(
                            "-- ===============================\n"
                    );

                    finalSQL.append(
                            "-- NEW VIEW\n"
                    );

                    finalSQL.append(
                            "-- ===============================\n"
                    );

                    MetaViewResult metaViewResult=
                            MetaViewGenerator
                                    .generateSectionView(
                                            metaEntityId,
                                            viewName
                                    );

                    finalSQL.append(
                            metaViewResult.getSql()
                    );
                    metaViewId = metaViewResult.getMetaViewId();
                    finalSQL.append(
                            "\n\n"
                    );
                }

                // =================================================
                // ATTRIBUTES
                // =================================================

                List<AttributeRequest>
                        requests =
                        new ArrayList<>();

                for (
                        int i = 0;
                        i <
                                viewPanel
                                        .attributePanels
                                        .size();
                        i++
                ) {

                    AttributePanel attr =
                            viewPanel
                                    .attributePanels
                                    .get(i);

                    AttributeRequest request =
                            attr.toAttributeRequest(
                                    metaEntityId,
                                    metaViewId,
                                    i + 1
                            );

                    requests.add(
                            request
                    );
                }

                if (
                        !requests.isEmpty()
                ) {

//                    String attributeSQL = MetaAttributeGenerator
////                            .generateMetaEntityAttribute(
////                                    requests
////                            );
//                    finalSQL.append(attributeSQL);

                    finalSQL.append(
                            "\n\n"
                    );
                }
            }

            sqlOutput.setText(
                    finalSQL.toString()
            );

            JOptionPane.showMessageDialog(
                    this,
                    "SQL generated successfully."
            );

        } catch (
                Exception ex
        ) {

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

    private class ViewPanel
            extends JPanel {

        private final JCheckBox
                existingViewCheckBox;

        private final JTextField
                viewIdField;

        private final JTextField
                viewNameField;

        private final JPanel
                attributesContainer;

        private final List<AttributePanel>
                attributePanels =
                new ArrayList<>();

        public ViewPanel(
                int viewNumber
        ) {

            setLayout(
                    new BorderLayout(
                            5,
                            5
                    )
            );

            setBorder(
                    BorderFactory
                            .createTitledBorder(
                                    "View "
                                            + viewNumber
                            )
            );

            // =================================================
            // VIEW HEADER
            // =================================================

            JPanel viewHeader =
                    new JPanel(
                            new FlowLayout(
                                    FlowLayout.LEFT
                            )
                    );

            existingViewCheckBox =
                    new JCheckBox(
                            "Existing View"
                    );

            viewHeader.add(
                    existingViewCheckBox
            );

            viewHeader.add(
                    new JLabel(
                            "View ID:"
                    )
            );

            viewIdField =
                    new JTextField(
                            25
                    );

            viewHeader.add(
                    viewIdField
            );

            viewHeader.add(
                    new JLabel(
                            "New View Name:"
                    )
            );

            viewNameField =
                    new JTextField(
                            25
                    );

            viewHeader.add(
                    viewNameField
            );

            JButton removeViewButton =
                    new JButton(
                            "Remove View"
                    );

            removeViewButton
                    .addActionListener(
                            e ->
                                    removeView(
                                            this
                                    )
                    );

            viewHeader.add(
                    removeViewButton
            );

            add(
                    viewHeader,
                    BorderLayout.NORTH
            );

            // =================================================
            // ATTRIBUTE CONTAINER
            // =================================================

            attributesContainer =
                    new JPanel();

            attributesContainer.setLayout(
                    new BoxLayout(
                            attributesContainer,
                            BoxLayout.Y_AXIS
                    )
            );

            add(
                    new JScrollPane(
                            attributesContainer
                    ),
                    BorderLayout.CENTER
            );

            // =================================================
            // BUTTONS
            // =================================================

            JPanel attributeButtons =
                    new JPanel(
                            new FlowLayout(
                                    FlowLayout.LEFT
                            )
                    );

            JButton addAttributeButton =
                    new JButton(
                            "+ Add Attribute"
                    );

            addAttributeButton
                    .addActionListener(
                            e ->
                                    addAttribute()
                    );

            attributeButtons.add(
                    addAttributeButton
            );

            add(
                    attributeButtons,
                    BorderLayout.SOUTH
            );

            // =================================================
            // VIEW TYPE TOGGLE
            // =================================================

            existingViewCheckBox
                    .addActionListener(
                            e ->
                                    updateViewMode()
                    );

            updateViewMode();

            // Start with one attribute
            addAttribute();
        }

        private void updateViewMode() {

            boolean existing =
                    existingViewCheckBox
                            .isSelected();

            viewIdField.setEnabled(
                    existing
            );

            viewNameField.setEnabled(
                    !existing
            );
        }

        private void addAttribute() {

            AttributePanel attributePanel =
                    new AttributePanel(
                            attributePanels.size() + 1
                    );

            attributePanels.add(
                    attributePanel
            );

            attributesContainer.add(
                    attributePanel
            );

            attributesContainer.add(
                    Box.createVerticalStrut(
                            5
                    )
            );

            refreshUI();
        }

        private void removeAttribute(
                AttributePanel panel
        ) {

            attributePanels.remove(
                    panel
            );

            attributesContainer.remove(
                    panel
            );

            refreshUI();
        }
    }

    // =========================================================
    // ATTRIBUTE PANEL
    // =========================================================

    private class AttributePanel
            extends JPanel {

        private final JTextField
                attributeNameField;

        private final JComboBox<String>
                attributeTypeCombo;

        private final JComboBox<String> tableColumnField;

        private final JTextField
                pickListIdField;

        private final JCheckBox
                mandatoryCheckBox;

        public AttributePanel(
                int attributeNumber
        ) {

            setLayout(
                    new FlowLayout(
                            FlowLayout.LEFT
                    )
            );

            setBorder(
                    BorderFactory
                            .createLineBorder(
                                    Color.LIGHT_GRAY
                            )
            );

            add(
                    new JLabel(
                            "Attribute "
                                    + attributeNumber
                                    + ":"
                    )
            );

            // =================================================
            // SYSTEM NAME
            // =================================================

            // =================================================
            // ATTRIBUTE NAME
            // =================================================

            add(
                    new JLabel(
                            "Attribute Name:"
                    )
            );

            attributeNameField =
                    new JTextField(
                            20
                    );

            add(
                    attributeNameField
            );

            // =================================================
            // ATTRIBUTE TYPE
            // =================================================

            add(
                    new JLabel(
                            "Type:"
                    )
            );

            attributeTypeCombo =
                    new JComboBox<>(
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

            add(
                    attributeTypeCombo
            );

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

            add(
                    new JLabel(
                            "PickList ID:"
                    )
            );

            pickListIdField =
                    new JTextField(
                            10
                    );

            add(
                    pickListIdField
            );

            // =================================================
            // MANDATORY
            // =================================================

            mandatoryCheckBox =
                    new JCheckBox(
                            "Mandatory"
                    );

            add(
                    mandatoryCheckBox
            );

            // =================================================
            // REMOVE
            // =================================================

            JButton removeButton =
                    new JButton(
                            "Remove"
                    );

            removeButton
                    .addActionListener(
                            e ->
                                    removeAttributePanel(
                                            this
                                    )
                    );

            add(
                    removeButton
            );

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
            boolean isPickList ="PickList".equalsIgnoreCase((String)attributeTypeCombo.getSelectedItem());
            pickListIdField.setEnabled(
                    isPickList
            );

            if (!isPickList) {pickListIdField.setText("");}
        }

        private void removeAttributePanel(AttributePanel panel) {
            for (ViewPanel viewPanel :viewPanels) {
                if (viewPanel.attributePanels.contains(panel)) {
                    viewPanel.removeAttribute(panel);
                    break;
                }
            }
        }

        // =====================================================
        // CREATE ATTRIBUTE REQUEST
        // =====================================================

        public AttributeRequest
        toAttributeRequest(
                String metaEntityId,
                String metaViewId,
                int displayOrder
        ) {

            String systemName = CoreUtils.generateSystemKey(attributeNameField
                            .getText()
                            .trim());

            String attributeName =
                    attributeNameField
                            .getText()
                            .trim();

            String attributeType =
                    (String)
                            attributeTypeCombo
                                    .getSelectedItem();

            String tableColumn =tableColumnField.getSelectedItem().toString();

            String pickListId = null;

            if (
                    "PickList"
                            .equalsIgnoreCase(
                                    attributeType
                            )
            ) {

                pickListId =
                        pickListIdField
                                .getText()
                                .trim();

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

    public static void main(
            String[] args
    ) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(EnhancementDocumentGeneratorUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EnhancementDocumentGeneratorUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EnhancementDocumentGeneratorUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EnhancementDocumentGeneratorUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        SwingUtilities.invokeLater(
                () -> {

                    EnhancementDocumentGeneratorUI
                            frame =
                            new EnhancementDocumentGeneratorUI();

                    frame.setVisible(
                            true
                    );
                }
        );
    }
}