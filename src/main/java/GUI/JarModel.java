package GUI;

import javassist.ClassPool;
import javassist.Loader;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

class JarModel extends DefaultTreeModel {

    private ClassPool pool;

    JarModel(TreeNode root) {
        super(root);
        pool = ClassPool.getDefault();
    }

    void buildModel(JarNode root, ArrayList<String> classes) {
        setRoot(root);
        for (String s : classes) {
            String[] strings = s.split("\\.");
            JarNode parent = root;
            for (String i : strings) {
                if (parent.getChildCount() != 0 &&
                        parent.getLastChild().toString().equals(i))
                {

                    parent = (JarNode) parent.getLastChild();
                    continue;
                }
                JarNode node;
                if (i.equals(strings[strings.length-1]))
                    node = new JarNode(i, JarNode.Type.CLASS);
                else
                    node = new JarNode(i, JarNode.Type.DIR);
                if (!parent.isNodeChild(node)) {
                    parent.add(node);
                }
                parent = node;
            }

        }
    }

    ClassPool getPool() {
        return pool;
    }

    static String pathToString(TreePath path) {
        StringBuilder s = new StringBuilder();
        for (Object i : path.getPath()) {
            JarNode node = (JarNode) i;
            if (node.getType() == JarNode.Type.JAR) continue;
            s.append(i.toString());

            if (!i.equals(path.getLastPathComponent()))
            s.append(".");
        }

        return s.toString();
    }

}
