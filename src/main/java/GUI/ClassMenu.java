package GUI;

import javassist.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Vector;

class ClassMenu extends JPopupMenu {
    private JMenuItem addMethod;
    private JMenuItem callMethod;
    private JMenuItem changeInheritanceHierarchy;

    ClassMenu(CtClass clazz) {
        addMethod = new JMenuItem("Add method");
        callMethod = new JMenuItem("Call method");
        changeInheritanceHierarchy = new JMenuItem("Change inheritance hierarchy");

        add(addMethod);
        add(callMethod);
        add(changeInheritanceHierarchy);

        addMethod.addActionListener(actionEvent -> {
            JTextArea textArea = new JTextArea();
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(700, 700));
            int selection = JOptionPane.showConfirmDialog(null, scrollPane,
                    "Add method", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (selection == JOptionPane.OK_OPTION) {
                try {
                    clazz.defrost();
                    CtMethod method = CtNewMethod.make(textArea.getText(), clazz);
                    clazz.addMethod(method);
                } catch (CannotCompileException e) {
                    e.printStackTrace();
                }
            }
        });
        callMethod.addActionListener(actionEvent -> {
            Vector<String> columnNames = new Vector<>();
            columnNames.add("Name");
            columnNames.add("No. of arguments");
            Vector<Vector<String>> data = new Vector<>();
            long beforeUsedMeth = 0, afterUsedMeth = 0;

            Loader ld = new Loader(clazz.getClassPool());
            Class<?> javaClass = null;
            Object point = null;
            try {
                javaClass = clazz.toClass(ld, null);
                point = javaClass.getDeclaredConstructor().newInstance();
            } catch (CannotCompileException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }


            assert javaClass != null;
            for (Method m : javaClass.getMethods()) {
                Vector<String> rowData = new Vector<>();
                rowData.add(m.getName());
                int len = m.getParameterTypes().length;
                rowData.add(Integer.toString(len));
                data.add(rowData);
            }
            JTable table = new JTable();
            DefaultTableModel model = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            table.setModel(model);
            JScrollPane scrollPane = new JScrollPane(table);
            boolean loop = false;

            do {
                int selection = JOptionPane.showConfirmDialog(null, scrollPane,
                        "Call method", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (selection == JOptionPane.OK_OPTION) {
                    if (table.getSelectedRow() != -1) {
                        if (loop)
                            loop = false;
                        try {
                            int row = table.getSelectedRow();
                            //CtMethod method = clazz.getMethods()[row];

                            clazz.defrost();

                            System.out.println("Chosen method: "+javaClass.getMethods()[row]);
                            beforeUsedMeth = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                            Method method = javaClass.getMethods()[row];
                            if (method.getParameterTypes().length > 0) {
                                MethodParameterPanel content = new MethodParameterPanel(method.getParameterTypes());
                                int parameterSelection = JOptionPane.showConfirmDialog(null, content,
                                        "Call method", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                                if (parameterSelection == JOptionPane.OK_OPTION) {
                                    Object[] parameters = content.getParameters();
                                    Object o = method.invoke(point, parameters);
                                    System.out.println("Method returned: "+o.toString());
                                }
                            }
                            else {
                                Object o = method.invoke(point, (Object[]) null);
                                if (o != null)
                                System.out.println("Method returned: "+o.toString());
                            }

                            afterUsedMeth = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();

                        } catch (IllegalAccessException | InvocationTargetException | NullPointerException e) {
                            e.printStackTrace();
                        }

                        JOptionPane.showMessageDialog(null, "Method call used " + (afterUsedMeth-beforeUsedMeth) + " bytes",
                                "Method memory usage", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else {
                    loop = true;
                    }
            }
            else if (selection == JOptionPane.CANCEL_OPTION) {
                if (loop)
                    loop = false;
            }
            } while (loop);
            }
        );
        changeInheritanceHierarchy.addActionListener(actionEvent -> {
            ArrayList<String> classes = new ArrayList<>();
            CtClass newclass = clazz;
            while (true) {
                classes.add(newclass.getName());
                try {
                    if (newclass.getSuperclass() != null)
                    newclass = newclass.getSuperclass();
                    else
                        break;
                } catch (NotFoundException e) {
                    e.printStackTrace();
                }

            }
            ArrayList<String> reversed = new ArrayList<>();
            for (int i = classes.toArray().length-1; i >= 0; i--) {
                reversed.add(classes.get(i));
            }
            new HierarchyDialog(reversed, clazz.getClassPool());


        });
    }
}
