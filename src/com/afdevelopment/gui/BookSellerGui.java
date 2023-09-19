package com.afdevelopment.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

import com.afdevelopment.agentes.Vendedor;

/**
 * GUI para interactuar con el agente vendedor y actualizar su catálogo de libros.
 */
public class BookSellerGui extends JFrame {
    private Vendedor myAgent;
    private JTextField titleField, priceField;

    public BookSellerGui(Vendedor a) {
        super(a.getLocalName());  // Se establece el nombre del agente como título del marco.

        myAgent = a;

        // Configurar panel principal para ingreso de detalles del libro.
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(2, 2));
        p.add(new JLabel("Título:"));
        titleField = new JTextField(15);
        p.add(titleField);
        p.add(new JLabel("Precio:"));
        priceField = new JTextField(15);
        p.add(priceField);
        getContentPane().add(p, BorderLayout.CENTER);

        // Botón para agregar libros al catálogo.
        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                try {
                    String title = titleField.getText().trim();
                    String price = priceField.getText().trim();
                    myAgent.updateCatalogue(title, Integer.parseInt(price));
                    titleField.setText("");
                    priceField.setText("");
                } catch(Exception e) {
                    JOptionPane.showMessageDialog(BookSellerGui.this, "Invalid values", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Panel para el botón.
        p = new JPanel();
        p.add(addButton);
        getContentPane().add(p, BorderLayout.SOUTH);

        // Evento para manejar el cierre de la ventana.
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                myAgent.doDelete();
            }
        });

        setResizable(false);
    }

    /**
     * Mostrar el GUI.
     */
    public void showGui() {
        pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (int)screenSize.getWidth() / 2;
        int centerY = (int)screenSize.getHeight() / 2;

        setLocation(centerX - getWidth() / 2, centerY - getHeight() / 2);
        super.setVisible(true);
    }
}
