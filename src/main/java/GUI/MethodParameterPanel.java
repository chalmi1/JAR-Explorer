package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class MethodParameterPanel extends JPanel {
    private Object[] parametry;
    private Class[] attributes;

    MethodParameterPanel(Class[] attributes) {
        super(new BorderLayout());
        this.attributes = attributes;
        JPanel parameters = new JPanel();
        JTextField[] field = new JTextField[attributes.length];
        int iter = 0;
        for (Class i : attributes) {
            field[iter] = new JTextField();
            field[iter].setPreferredSize(new Dimension(150, 50));
            JLabel parameterLabel = new JLabel(i.getName());
            parameters.add(parameterLabel);
            parameters.add(field[iter]);
            iter++;
        }
        add(parameters, BorderLayout.CENTER);
        JButton ok = new JButton("Submit parameters");
        add(ok, BorderLayout.PAGE_END);
        parametry = new Object[attributes.length];
        int a = (int) 5.4;
        ok.addActionListener(actionEvent -> updateParametry(field));
    }

    Object[] getParameters() {
        return parametry;
    }

    private void updateParametry(JTextField[] field) {
        for (int i = 0; i < attributes.length; i++) {
            parametry[i] = toObject(attributes[i], field[i].getText());
        }
    }

    private static Object toObject(Class clazz, String value) {
        if( Boolean.class == clazz ) return Boolean.parseBoolean( value );
        if( Byte.class == clazz ) return Byte.parseByte( value );
        if( Short.class == clazz ) return Short.parseShort( value );
        if( Integer.class == clazz || int.class == clazz) return Integer.parseInt( value );
        if( Long.class == clazz ) return Long.parseLong( value );
        if( Float.class == clazz ) return Float.parseFloat( value );
        if( Double.class == clazz ) return Double.parseDouble( value );
        return value;
    }
}
