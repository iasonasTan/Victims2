package gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;
import java.util.ArrayList;
import java.util.List;

public final class VerticalFlowLayout implements LayoutManager2 {
    private List<Component> components = new ArrayList<>();
    public int vGap = 0; // vertical

    public VerticalFlowLayout () {
    }

    public VerticalFlowLayout (int vGap) {
        this.vGap = vGap;
    }

    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        components.add(comp);
    }

    @Override
    public Dimension maximumLayoutSize(Container target) {
        return null;
    }

    @Override
    public float getLayoutAlignmentX(Container target) {
        return 0;
    }

    @Override
    public float getLayoutAlignmentY(Container target) {
        return 0;
    }

    @Override
    public void invalidateLayout(Container target) {

    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
        components.add(comp);
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        components.remove(comp);
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return null;
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return null;
    }

    @Override
    public void layoutContainer(Container parent) {
        int x = 0;
        int y = vGap;

        Dimension parentSize = parent.getPreferredSize();

        for (Component c: components) {
            if (!c.isVisible()) {
                continue;
            }

            Dimension componentSize = c.getPreferredSize();
            x = parentSize.width/2-componentSize.width/2;
            c.setBounds(x, y, componentSize.width, componentSize.height);
            y+=componentSize.height+vGap;

            if (y+componentSize.height > parentSize.height) {
                y = vGap;
            }

        }

    }
}
