package com.afdevelopment.agentes;

import com.afdevelopment.behaviours.OfferRequestServer;
import com.afdevelopment.behaviours.PurchaseOrderServer;
import com.afdevelopment.gui.BookSellerGui;
import jade.core.Agent;
import java.util.Hashtable;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

/**
 * Agente Vendedor que gestiona un catálogo de libros y responde a solicitudes de compradores.
 */
public class Vendedor extends Agent {
    // Catálogo para mantener libros y sus precios.
    private Hashtable<String, Integer> catalogo;
    // GUI para interactuar con el agente vendedor.
    private BookSellerGui miGUI;

    /**
     * Configuración inicial del agente.
     */
    protected void setup() {
        System.out.println("Hola, soy el agente vendedor: " + getAID().getName());

        // Inicializar el catálogo y la GUI.
        catalogo = new Hashtable<String, Integer>();
        miGUI = new BookSellerGui(this);
        miGUI.showGui();

        // Descripción del agente para registrarlo en el directorio.
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("book-selling");
        sd.setName("book-trading");
        dfd.addServices(sd);

        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        // Agregar comportamientos para gestionar solicitudes y órdenes de compra.
        addBehaviour(new OfferRequestServer(this));
        addBehaviour(new PurchaseOrderServer(this));
    }

    /**
     * Acciones a realizar antes de que el agente sea terminado.
     */
    protected void takedown() {
        // Desregistrar el agente del directorio.
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        miGUI.dispose();
        System.out.println("Finalizando el agente vendedor " + getAID().getName());
    }

    /**
     * Actualizar el catálogo con un nuevo libro o cambiar el precio de uno existente.
     * @param titulo Titulo del libro.
     * @param precio Precio del libro.
     */
    public void updateCatalogue(final String titulo, final int precio) {
        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                catalogo.put(titulo, precio);
                System.out.println(titulo + " ha sido insertado en el catálogo con un precio de $" + precio);
            }
        });
    }

    /**
     * Obtener el catálogo de libros.
     * @return Catálogo de libros.
     */
    public Hashtable<String, Integer> getCatalogue() {
        return catalogo;
    }
}
