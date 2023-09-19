package com.afdevelopment.agentes;

import jade.core.AID;
import jade.core.Agent;
import com.afdevelopment.behaviours.RequestPerformer;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import com.afdevelopment.gui.BookBuyerGui;

public class Comprador extends Agent {
    private String titulo;  // Almacena el título del libro que se quiere comprar.
    private AID[] sellerAgents;  // Lista de agentes vendedores.
    private final int ticker_timer = 10000;  // Intervalo en el que intentará comprar el libro.
    private final Comprador this_agent = this;  // Referencia al propio agente.
    private BookBuyerGui gui;  // La interfaz gráfica asociada a este agente.

    // Método inicial del agente.
    protected void setup() {
        System.out.println("Soy el agente comprador " + getAID().getName());

        // Inicializa y muestra la GUI al iniciar el agente.
        gui = new BookBuyerGui(this);
        gui.showGui();
    }

    // Método para intentar comprar un libro. Se llama desde la GUI.
    public void buyBook(String title) {
        this.titulo = title;
        System.out.println("Vamos a comprar: " + titulo);

        // Comportamiento periódico para intentar comprar el libro cada "ticker_timer" milisegundos.
        addBehaviour(new TickerBehaviour(this, ticker_timer) {
            @Override
            protected void onTick() {
                System.out.println("Intentando comprar " + titulo);
                gui.showMessage("Intentando comprar " + titulo);  // Actualiza la GUI con información.

                // Crea una descripción de agente para buscar agentes vendedores en el directorio.
                DFAgentDescription template = new DFAgentDescription();
                ServiceDescription sd = new ServiceDescription();
                sd.setType("book-selling");
                template.addServices(sd);

                try {
                    // Busca los agentes vendedores en el directorio.
                    DFAgentDescription[] result = DFService.search(myAgent, template);
                    sellerAgents = new AID[result.length];

                    // Almacena y muestra en la GUI los agentes vendedores encontrados.
                    for (int i = 0; i < result.length; i++) {
                        sellerAgents[i] = result[i].getName();
                        gui.showMessage("Vendedor encontrado: " + sellerAgents[i].getName());
                        System.out.println(sellerAgents[i].getName());
                    }
                } catch (FIPAException e) {
                    e.printStackTrace();
                }

                // Agrega un comportamiento para realizar la solicitud de compra.
                myAgent.addBehaviour(new RequestPerformer(this_agent));
            }
        });
    }

    // Método para finalizar el agente.
    protected void takeDown() {
        System.out.println("Finalizando el agente comprador " + getAID().getName());
        gui.dispose();  // Cierra la GUI cuando se finaliza el agente.
    }

    // Retorna la lista de agentes vendedores.
    public AID[] getSellerAgents() {
        return sellerAgents;
    }

    // Retorna el título del libro que se quiere comprar.
    public String getBookTitle() {
        return titulo;
    }
}
