package com.zrmiller.gui;

import javax.swing.*;
import java.awt.*;

public abstract class ComponentLimitPanel extends JPanel {

    private final int COMPONENT_LIMIT = 0;
    private int componentCount;

    public ComponentLimitPanel(int limit) {
//        COMPONENT_LIMIT = limit;
    }

    @Override
    public Component add(Component comp) {
        super.add(comp);
        componentCount++;
        System.out.println("Cool:" + componentCount);
        if (componentCount > COMPONENT_LIMIT) {
            System.err.println("[ComponentLimitPanel]");
            System.err.println(getClass().getSimpleName());
        }
        return comp;
    }
}
