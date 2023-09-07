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

public class Vendedor extends Agent{
    private Hashtable<String, Integer> catalogo;
    private BookSellerGui miGUI;
    protected void setup(){
        System.out.println("Hola, soy el agente vendedor: "+getAID().getName());
        catalogo = new Hashtable<String, Integer> ();
        miGUI = new BookSellerGui(this);
        miGUI.showGui();
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("book-selling");
        sd.setName("book-trading");
        dfd.addServices(sd);
        try{
            DFService.register(this, dfd);
        }catch(FIPAException e){
            e.printStackTrace();
        }
        addBehaviour(new OfferRequestServer(this));
        addBehaviour(new PurchaseOrderServer(this));

    }
    protected void takedown(){
        try{
            DFService.deregister(this);
        }catch(FIPAException fe){
            fe.printStackTrace();
        }
        miGUI.dispose();
        System.out.println("Finalizando el agente vendedor "+getAID().getName());
    }
    public void updateCatalogue(final String titulo, final int precio){
        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                catalogo.put(titulo, precio);
                System.out.println(titulo+" ha sido insertado en el catálogo con un precio de $"+precio);

            }
        });
    }
    public Hashtable getCatalogue() {
        return catalogo;
    }
}
