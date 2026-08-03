package org.example.ui;

import org.example.PickListGenerator;
import org.example.config.PickListConfig;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PickListGeneratorUI extends JFrame {

    private JPanel pickListsContainer;

    private JTextArea insertSqlOutput;
    private JTextArea revertSqlOutput;
    private JTabbedPane sqlTabbedPane;

    private final List<PickListPanel> pickListPanels = new ArrayList<>();

    public PickListGeneratorUI() {

        setTitle("PickList Generator");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
    }

    private void initUI() {

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // =====================================================
        // TOP BUTTON PANEL
        // =====================================================

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton addPickListButton = new JButton("+ Add PickList");
        addPickListButton.addActionListener(e -> addPickList());

        JButton generateButton = new JButton("Generate SQL");
        generateButton.addActionListener(e -> generateSQL());

        JButton saveToFileButton = new JButton("Save To File");
        saveToFileButton.addActionListener(e -> saveBothSQLFiles());

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> {
            insertSqlOutput.setText("");
            revertSqlOutput.setText("");
        });

        topPanel.add(addPickListButton);
        topPanel.add(generateButton);
        topPanel.add(saveToFileButton);
        topPanel.add(clearButton);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // =====================================================
        // PICKLIST CONTAINER
        // =====================================================

        pickListsContainer = new JPanel();
        pickListsContainer.setLayout(new BoxLayout(pickListsContainer, BoxLayout.Y_AXIS));

        JScrollPane pickListScrollPane = new JScrollPane(pickListsContainer);
        pickListScrollPane.setBorder(BorderFactory.createTitledBorder("PickLists"));

        // =====================================================
        // SQL OUTPUT TABS (Insert & Revert)
        // =====================================================

        insertSqlOutput = createSqlTextArea();
        revertSqlOutput = createSqlTextArea();

        sqlTabbedPane = new JTabbedPane();
        sqlTabbedPane.addTab("Insert SQL", new JScrollPane(insertSqlOutput));
        sqlTabbedPane.addTab("Revert SQL", new JScrollPane(revertSqlOutput));

        // =====================================================
        // SPLIT PANE
        // =====================================================

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                pickListScrollPane,
                sqlTabbedPane
        );

        splitPane.setDividerLocation(400);

        mainPanel.add(splitPane, BorderLayout.CENTER);

        add(mainPanel);

        // Default PickList Panel
        addPickList();
    }

    private JTextArea createSqlTextArea() {
        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        textArea.setLineWrap(false);
        textArea.setEditable(false);
        return textArea;
    }

    /**
     * Saves SQL files in the structured format:
     * YYYY-MM-DD/PickList Creation/PickList_Insert.sql
     * YYYY-MM-DD/PickList Creation/PickList_Revert.sql
     */
    private void saveBothSQLFiles() {

        String insertSql = insertSqlOutput.getText();
        String revertSql = revertSqlOutput.getText();

        if (insertSql == null || insertSql.trim().isEmpty() || revertSql == null || revertSql.trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "There is no SQL to save. Please click 'Generate SQL' first.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Target directory select karne ke liye file chooser
        JFileChooser folderChooser = new JFileChooser();
        folderChooser.setDialogTitle("Select Target Directory");
        folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int result = folderChooser.showSaveDialog(this);

        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File baseDirectory = folderChooser.getSelectedFile();

        // 1. Format date folder: YYYY-MM-DD
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateFolderName = dateFormat.format(new Date());

        // 2. Build folder hierarchy: <SelectedDirectory>/2026-08-03/PickList Creation/
        File dateFolder = new File(baseDirectory, dateFolderName);
        File pickListCreationFolder = new File(dateFolder, "PickList Creation");

        // Hierarchy create karein agar exist nahi karti
        if (!pickListCreationFolder.exists()) {
            boolean created = pickListCreationFolder.mkdirs();
            if (!created) {
                JOptionPane.showMessageDialog(
                        this,
                        "Failed to create directory structure.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
        }

        // 3. Define Insert & Revert files
        File insertFile = new File(pickListCreationFolder, "PickList_Insert.sql");
        File revertFile = new File(pickListCreationFolder, "PickList_Revert.sql");

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
                    "Files saved successfully!\n\n"
                            + "Folder Path: " + pickListCreationFolder.getAbsolutePath() + "\n\n"
                            + "• " + insertFile.getName() + "\n"
                            + "• " + revertFile.getName(),
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
    private void addPickList() {
        PickListPanel panel = new PickListPanel(pickListPanels.size() + 1);
        pickListPanels.add(panel);
        pickListsContainer.add(panel);
        pickListsContainer.add(Box.createVerticalStrut(10));

        refreshUI();
    }

    private void removePickList(PickListPanel panel) {
        pickListPanels.remove(panel);
        pickListsContainer.remove(panel);
        refreshUI();
    }

    private void generateSQL() {

        try {
            if (pickListPanels.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please add at least one PickList.",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            StringBuilder finalInsertSQL = new StringBuilder();
            StringBuilder finalRevertSQL = new StringBuilder();

            for (PickListPanel panel : pickListPanels) {

                PickListConfig config = panel.toPickListConfig();

                String insertSql = PickListGenerator.generateCompletePickListSQL(
                        config.getPickListId(),
                        config.getGroupId(),
                        config.getValues()
                );

                String revertSql = PickListGenerator.generateRevertPickListSQL(
                        config.getPickListId(),
                        config.getValues()
                );

                finalInsertSQL.append(insertSql).append("\n\n");
                finalRevertSQL.append(revertSql).append("\n\n");
            }

            insertSqlOutput.setText(finalInsertSQL.toString());
            insertSqlOutput.setCaretPosition(0);

            revertSqlOutput.setText(finalRevertSQL.toString());
            revertSqlOutput.setCaretPosition(0);

            JOptionPane.showMessageDialog(
                    this,
                    "PickList Insert and Revert SQL generated successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
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

    private void refreshUI() {
        pickListsContainer.revalidate();
        pickListsContainer.repaint();
    }

    // =========================================================
    // INNER CLASSES (PickListPanel & ValuePanel)
    // =========================================================

    private class PickListPanel extends JPanel {

        private final JTextField pickListIdField;
        private final JTextField groupIdField;
        private final JPanel valuesContainer;
        private final List<ValuePanel> valuePanels = new ArrayList<>();

        public PickListPanel(int pickListNumber) {

            setLayout(new BorderLayout(5, 5));
            setBorder(BorderFactory.createTitledBorder("PickList " + pickListNumber));

            JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
            header.add(new JLabel("PickList ID:"));
            pickListIdField = new JTextField(15);
            header.add(pickListIdField);

            header.add(new JLabel("Group ID:"));
            groupIdField = new JTextField(20);
            header.add(groupIdField);

            JButton removePickList = new JButton("Remove PickList");
            removePickList.addActionListener(e -> removePickList(this));
            header.add(removePickList);

            add(header, BorderLayout.NORTH);

            valuesContainer = new JPanel();
            valuesContainer.setLayout(new BoxLayout(valuesContainer, BoxLayout.Y_AXIS));

            add(new JScrollPane(valuesContainer), BorderLayout.CENTER);

            JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JButton addValueButton = new JButton("+ Add Value");
            addValueButton.addActionListener(e -> addValue());
            bottom.add(addValueButton);

            add(bottom, BorderLayout.SOUTH);

            addValue();
        }

        private void addValue() {
            ValuePanel valuePanel = new ValuePanel(valuePanels.size() + 1);
            valuePanels.add(valuePanel);
            valuesContainer.add(valuePanel);
            valuesContainer.add(Box.createVerticalStrut(5));
            valuesContainer.revalidate();
            valuesContainer.repaint();
        }

        private void removeValue(ValuePanel panel) {
            valuePanels.remove(panel);
            valuesContainer.remove(panel);
            valuesContainer.revalidate();
            valuesContainer.repaint();
        }

        private PickListConfig toPickListConfig() {

            String pickListId = pickListIdField.getText().trim();
            String groupId = groupIdField.getText().trim();

            if (pickListId.isEmpty()) {
                throw new IllegalArgumentException("PickList ID is required.");
            }

            if (groupId.isEmpty()) {
                throw new IllegalArgumentException("Group ID is required.");
            }

            if (valuePanels.isEmpty()) {
                throw new IllegalArgumentException("At least one PickList value is required.");
            }

            List<String> values = new ArrayList<>();

            for (ValuePanel valuePanel : valuePanels) {
                String value = valuePanel.getValue();
                if (value.isEmpty()) {
                    throw new IllegalArgumentException("PickList value cannot be empty.");
                }
                values.add(value);
            }

            return new PickListConfig(pickListId, groupId, values);
        }
    }

    private class ValuePanel extends JPanel {

        private final JTextField valueField;

        public ValuePanel(int valueNumber) {

            setLayout(new FlowLayout(FlowLayout.LEFT));

            add(new JLabel("Value " + valueNumber + ":"));
            valueField = new JTextField(40);
            add(valueField);

            JButton removeButton = new JButton("Remove");
            removeButton.addActionListener(e -> {
                for (PickListPanel panel : pickListPanels) {
                    if (panel.valuePanels.contains(this)) {
                        panel.removeValue(this);
                        break;
                    }
                }
            });

            add(removeButton);
        }

        private String getValue() {
            return valueField.getText().trim();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(PickListGeneratorUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            PickListGeneratorUI frame = new PickListGeneratorUI();
            frame.setVisible(true);
        });
    }
}