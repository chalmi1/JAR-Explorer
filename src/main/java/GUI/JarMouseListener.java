package GUI;

import javassist.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class JarMouseListener extends MouseAdapter {
    private JTree tree;

    JarMouseListener(JTree tree) {
        this.tree = tree;
    }


    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if (SwingUtilities.isRightMouseButton(mouseEvent)) {
            var path = tree.getPathForLocation(mouseEvent.getX(), mouseEvent.getY());
            if (path != null) {
                JarNode node = (JarNode) path.getLastPathComponent();
                try {
                    if (node.getType() == JarNode.Type.CLASS) {
                        JarModel model = (JarModel) tree.getModel();
                        CtClass clazz = model.getPool().get(JarModel.pathToString(path));
                        var menu = new ClassMenu(clazz);
                        menu.show(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent.getY());
                    }
                } catch (NotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
