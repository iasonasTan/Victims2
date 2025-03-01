package gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;
import java.util.ArrayList;

import javax.swing.JComponent;

public class VerticalFlowLayout implements LayoutManager2 {
    final private ArrayList<Component> components = new ArrayList<>();
    private int hgap = 1;
    private int vgap = 1;
    private JComponent parent;
    private Dimension pd;

    public VerticalFlowLayout (JComponent c) {
    	parent = c;
    	pd = parent.getPreferredSize();
    }
    
    public void setHGap (int hgap) { this.hgap = hgap; }
    public void setVGap (int vgap) { this.vgap = vgap; }
    
    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        this.components.add(comp);
    }
    
    @Override
    public void addLayoutComponent(String name, Component comp) {
        this.components.add(comp);
    }
    
    @Override
    public void layoutContainer(Container parent) {
        int x = 0;
        int y = 0;
        int columnWidth = 0;
        for (Component c : this.components) {
            if (!c.isVisible())
            	continue;
            
            Dimension d = c.getPreferredSize();
            
            columnWidth = Math.max(columnWidth, d.width);
            if (y+d.height > parent.getHeight())
            {
               x += columnWidth + this.hgap;
               y = 0;
            }
            c.setBounds(x, y, d.width, d.height);
            y += d.height + this.vgap;
        }       
    }
    
    @Override 
    public Dimension minimumLayoutSize(Container parent) {
    	return preferredLayoutSize(parent);
    }
    
    @Override
    public Dimension preferredLayoutSize(Container parent) {
    	int width = 0;
    	int height = pd.height;
    	
    	for (int i = 0; i < components.size(); i++) {
    		final Component c = components.get(i);
    		Dimension d = c.getPreferredSize();
    		width += d.width;
    		height = Math.max(height, d.height);
    	}
    	
        return new Dimension(width, height);
    }
    
    @Override
    public Dimension maximumLayoutSize(Container target) {
        return new Dimension(600,600);
    }
    
    @Override
    public void removeLayoutComponent(Component comp) {
        this.components.remove(comp);
    }
    
    @Override public void invalidateLayout(Container target) {}
    @Override public float getLayoutAlignmentX(Container target) { return 0; }
    @Override public float getLayoutAlignmentY(Container target) { return 0; }
    
}