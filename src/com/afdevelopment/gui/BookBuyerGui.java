package com.afdevelopment.gui;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import com.afdevelopment.agentes.Comprador;

/**
 * GUI para interactuar con el agente comprador y comprar libros.
 */
public class BookBuyerGui extends JFrame {
    private Comprador myAgent;  // Referencia al agente comprador.
    private JTextField titleField;  // Campo para introducir el título del libro.
    private JButton buyButton;  // Botón para iniciar la compra.
    private JTextArea outputArea;  // Área para mostrar mensajes y actualizaciones.

    // Constructor: inicializa y configura los componentes de la GUI.
    public BookBuyerGui(Comprador a) {
        super(a.getLocalName());  // Nombre de la ventana es el nombre del agente.

        myAgent = a;  // Asigna el agente asociado a esta GUI.

        JPanel p = new JPanel();
        p.setLayout(new GridLayout(2, 2));

        // Agrega el campo de texto para el título del libro.
        p.add(new JLabel("Título del libro:"));
        titleField = new JTextField(15);
        p.add(titleField);
        getContentPane().add(p, BorderLayout.NORTH);

        // Configura el área de texto para mostrar mensajes.
        outputArea = new JTextArea(20, 40);
        outputArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(outputArea);
        getContentPane().add(scroll, BorderLayout.CENTER);

        // Configura el botón de compra.
        buyButton = new JButton("Comprar");
        buyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                String title = titleField.getText().trim();
                if (!title.isEmpty()) {
                    outputArea.setText("");
                    myAgent.buyBook(title);
                } else {
                    JOptionPane.showMessageDialog(BookBuyerGui.this, "Por favor, ingrese un título", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        p = new JPanel();
        p.add(buyButton);
        getContentPane().add(p, BorderLayout.SOUTH);

        // Evento para finalizar el agente cuando se cierra la GUI.
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                myAgent.doDelete();
            }
        });

        setResizable(false);
    }

    // Método para agregar mensajes a la GUI.
    public void showMessage(String message) {
        outputArea.append(message + "\n");
    }

    // Método para mostrar la GUI.
    public void showGui() {
        pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (int)screenSize.getWidth() / 2;
        int centerY = (int)screenSize.getHeight() / 2;
        setLocation(centerX - getWidth() / 2, centerY - getHeight() / 2);
        super.setVisible(true);
    }
}
