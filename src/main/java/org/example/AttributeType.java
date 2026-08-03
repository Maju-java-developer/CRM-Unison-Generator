package org.example;

import java.util.Arrays;

public enum AttributeType {
    STRING("String", "InputText"),
    PICK_LIST("PickList", "ComboBox"),
    DATE_TIME("DateTime", "InputDateTime"),
    NUMBER("Number", "OutputText"),
    BOOLEAN("Boolean", "RadioButton");

    private final String name;
    private final String componentType;

    AttributeType(String name, String componentType) {
        this.name = name;
        this.componentType = componentType;
    }

    public String getComponentType() {
        return componentType;
    }

    // String input se Component Type dhoondne ke liye method
    public static String getComponentTypeByName(String inputType) {
        return Arrays.stream(values())
                .filter(type -> type.name.equalsIgnoreCase(inputType))
                .findFirst()
                .map(AttributeType::getComponentType)
                .orElse("InputText"); // Default value
    }
}