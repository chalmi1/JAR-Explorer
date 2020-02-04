package GUI;

import javassist.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

class HierarchyDialog extends JDialog {

    HierarchyDialog(ArrayList<String> lista, ClassPool pool) {
        JList<String> list = new JList<>((String[]) lista.toArray());
        JPanel panel = new JPanel();
        panel.add(list);
        JButton up = new JButton("/\\");
        JButton down = new JButton("\\/");
        JPanel buttonPane = new JPanel();
        buttonPane.add(up);
        buttonPane.add(down);
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.add(panel, BorderLayout.CENTER);
        contentPane.add(buttonPane, BorderLayout.PAGE_END);
        setContentPane(contentPane);
        setTitle("Class hierarchy");
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        up.addActionListener(actionEvent -> {
            String className = list.getSelectedValue();
            int index = list.getSelectedIndex();
            if (index > 0) {
                CtClass selectedClass = null;
                CtClass lowerClass = null;
                CtClass upperupperClass = null;
                CtClass upperClass = null;
                try {
                    if (index - 1 <= lista.toArray().length - 1)
                    upperClass = pool.get(lista.get(index-1));
                    if (index-2 >= 0 && index-2 <= lista.toArray().length-1)
                    upperupperClass = pool.get(lista.get(index-2));
                    if (index+1 >= 0 && index+1 <= lista.toArray().length-1)
                        lowerClass = pool.get(lista.get(index+1));

                    selectedClass = pool.get(className);
                    if (lowerClass != null) {
                        lowerClass.setSuperclass(upperClass);
                    }
                    if (upperClass != null) {
                        upperClass.setSuperclass(selectedClass);
                        if  (upperupperClass != null)
                            selectedClass.setSuperclass(upperupperClass);
                    }

                } catch (NotFoundException | CannotCompileException e) {
                    e.printStackTrace();
                }
                assert selectedClass != null;

                String upper = lista.get(index-1);
                lista.set(index, upper);
                lista.set(index-1, className);
                list.setListData((String[]) lista.toArray());
            }
        });
        down.addActionListener(actionEvent -> {
            String className = list.getSelectedValue();
            int index = list.getSelectedIndex();
            if (index < lista.toArray().length-1 && index >= 0) {
                CtClass selectedClass = null;
                CtClass lowerClass = null;
                CtClass upperClass = null;
                CtClass lowerlowerClass = null;
                try {
                    if (index-1 >= 0 && index-1 <= lista.toArray().length-1)
                        upperClass = pool.get(lista.get(index-1));
                    if (index + 1 <= lista.toArray().length - 1)
                        lowerClass = pool.get(lista.get(index+1));
                    if (index + 2 <= lista.toArray().length - 1)
                        lowerlowerClass = pool.get(lista.get(index+2));

                    selectedClass = pool.get(className);
                    if (lowerClass != null) {
                        selectedClass.setSuperclass(lowerClass);
                        if  (lowerlowerClass != null)
                            lowerlowerClass.setSuperclass(selectedClass);
                    }
                    if (upperClass != null) {
                        assert lowerClass != null;
                        lowerClass.setSuperclass(upperClass);
                    }
                } catch (NotFoundException | CannotCompileException e) {
                    e.printStackTrace();
                }
                assert selectedClass != null;

                String lower = lista.get(index+1);
                lista.set(index, lower);
                lista.set(index+1, className);
                list.setListData((String[]) lista.toArray());
            }
        });
    }


}
