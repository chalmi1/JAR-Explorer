package GUI;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

public class Renderer implements TreeCellRenderer {
    private JLabel label = new JLabel();

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
                                                  boolean leaf, int row, boolean hasFocus) {
            JarNode node = (JarNode) value;
            label.setIcon(new ImageIcon(node.getIcon()));
            label.setText(node.toString());

        return label;
    }
}
