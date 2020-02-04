package GUI;

import javax.imageio.*;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class JarNode extends DefaultMutableTreeNode {
    public enum Type {
        CLASS, JAR, DIR
        //INTERFACE, ATTRIBUTE, CONSTRUCTOR, METHOD, METHODDECLARATION
        }
    private Type type;
    private URL url;


    JarNode(Object userObject, Type type) {
        super(userObject);
        this.type = type;


            switch (type) {
                case CLASS:
                    url = this.getClass().getClassLoader().getResource("class.png");
                    break;

                case JAR:
                    url = this.getClass().getClassLoader().getResource("jar.png");
                    break;

                case DIR:
                    url = this.getClass().getClassLoader().getResource("package.png");
                    break;

                default:
                    url = getClass().getResource("placeholder.png");
                    break;
            }
    }

    public String toString() {
        return userObject.toString();
    }

    URL getIcon() {
        return url;
    }

    Type getType() {
        return type;
    }
}
