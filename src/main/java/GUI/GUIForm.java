package GUI;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class GUIForm extends JFrame implements ActionListener {
    private JPanel rootPanel;
    private JButton openButton;
    //private JButton saveButton;
    private JButton exitButton;
    private JTree tree;
    private JScrollPane scrollPane;
    private JarFile jar;
    private Enumeration<JarEntry> entries;
    private JarNode top = null;
    private JarModel model = new JarModel(top);
    private ArrayList<String> classes;

    public GUIForm() {
        add(rootPanel);
        tree.setModel(model);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("JAR Explorer");
        setSize(800, 600);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        openButton.addActionListener(this);
        //saveButton.addActionListener(this);
        exitButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Object o = actionEvent.getSource();
        if (openButton.equals(o)) {
            JFileChooser fc = new JFileChooser();
            fc.setAcceptAllFileFilterUsed(false);
            FileFilter filter = new FileNameExtensionFilter("JAR File", "jar");
            fc.addChoosableFileFilter(filter);
            int result = fc.showDialog(this, "Open file");
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                try {
                    model.getPool().insertClassPath(file.getPath());
                } catch (NotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    jar = new JarFile(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                entries = jar.entries();
                classes = getClassList(entries);
                top = new JarNode(file.getName().replaceAll("\\.jar", ""), JarNode.Type.JAR);
                tree.setCellRenderer(new Renderer());
                tree.addMouseListener(new JarMouseListener(tree));
                model.buildModel(top, classes);
            }
        }
        /*
        else if (saveButton.equals(o)) {
            if (model.getPool() != null) {
                JFileChooser fc = new JFileChooser();
                int returnVal = fc.showDialog(this, "Save");
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    String path = fc.getSelectedFile().getPath();
                    if (!path.endsWith(".jar")) {
                        path += ".jar";
                    }
                    model.save(path);
                }
            }
            System.out.println("Saved succesfully!");
        }*/
        else if (exitButton.equals(o)) {
            System.exit(0);
        }
    }

    /**
     * Metoda służąca do wyłuskania klas z pliku jar, tak aby ścieżki które nie prowadzą do klas zostały pominięte
     * @param entries lista JarEntries
     * @return lista klas w pliku .jar jako pełna ścieżka (z nazwami paczek) z uciętym rozszerzeniem .class
     */
    private ArrayList<String> getClassList(Enumeration<JarEntry> entries) {
        ArrayList<String> classes = new ArrayList<>();
        while(entries.hasMoreElements()) {
            JarEntry c = entries.nextElement();
            String className = c.getName();
            if (!className.endsWith(".class")) continue;
            className = className.replaceAll("\\.class", "");
            className = className.replaceAll("/", ".");
            classes.add(className);
        }
        return classes;
    }
}
