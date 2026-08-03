package org.example.ui;

import javax.swing.*;
import java.awt.*;

public class GeneratorNavigationPanel extends JPanel {

    private final JComboBox<String> generatorCombo;

    private final String currentScreen;

    public GeneratorNavigationPanel(
            String currentScreen
    ) {

        this.currentScreen =
                currentScreen;

        setLayout(
                new FlowLayout(
                        FlowLayout.LEFT
                )
        );

        add(
                new JLabel(
                        "Generator:"
                )
        );

        generatorCombo =
                new JComboBox<>(
                        new String[]{
                                "Document Generator",
                                "Enhancement Document",
                                "PickList Creation"
                        }
                );

        // Current screen selected by default
        generatorCombo.setSelectedItem(
                currentScreen
        );

        generatorCombo.addActionListener(
                e -> navigate()
        );

        add(
                generatorCombo
        );
    }

    private void navigate() {

        String selected =
                (String)
                        generatorCombo
                                .getSelectedItem();

        // Don't reopen same screen
        if (
                selected == null
                        ||
                        selected.equals(
                                currentScreen
                        )
        ) {

            return;
        }

        if (
                "Document Generator"
                        .equals(selected)
        ) {

            openDocumentGenerator();

        } else if (
                "Enhancement Document"
                        .equals(selected)
        ) {

            openEnhancementDocument();

        } else if (
                "PickList Creation"
                        .equals(selected)
        ) {

            openPickListGenerator();
        }
    }

    private void openDocumentGenerator() {

        EformComplaintGeneratorUI frame =
                new EformComplaintGeneratorUI();

        frame.setVisible(
                true
        );

        closeCurrentWindow();
    }

    private void openEnhancementDocument() {

        EnhancementDocumentGeneratorUI frame =
                new EnhancementDocumentGeneratorUI();

        frame.setVisible(
                true
        );

        closeCurrentWindow();
    }

    private void openPickListGenerator() {

        PickListGeneratorUI frame =
                new PickListGeneratorUI();

        frame.setVisible(
                true
        );

        closeCurrentWindow();
    }

    private void closeCurrentWindow() {

        Window window =
                SwingUtilities
                        .getWindowAncestor(
                                this
                        );

        if (
                window != null
        ) {

            window.dispose();
        }
    }
}