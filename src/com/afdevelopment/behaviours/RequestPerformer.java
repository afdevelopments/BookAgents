package com.afdevelopment.behaviours;

import com.afdevelopment.agentes.Comprador;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

// Este comportamiento es utilizado por el agente comprador para realizar solicitudes de compra de libros.
public class RequestPerformer extends Behaviour{

    // AID del vendedor con el mejor precio.
    private AID bestSeller;
    // El mejor precio encontrado.
    private int bestPrice;
    // Contador para las respuestas de los vendedores.
    private int repliesCount = 0;
    // Plantilla de mensajes para filtrar respuestas.
    private MessageTemplate mt;
    // Paso actual del proceso de compra.
    private int step = 0;
    // Referencia al agente comprador que utiliza este comportamiento.
    private Comprador bbAgent;
    // Título del libro que el agente comprador desea comprar.
    private String bookTitle;

    // Constructor del comportamiento.
    public RequestPerformer(Comprador a) {
        bbAgent = a;
        bookTitle = a.getBookTitle();
    }

    // Define las acciones que el agente realizará cuando este comportamiento esté activo.
    public void action() {
        ACLMessage reply;
        switch(step) {
            case 0:
                // Paso 0: Envía un CFP (Call For Proposal) a todos los agentes vendedores.
                ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
                for(int i = 0; i < bbAgent.getSellerAgents().length; i++) {
                    cfp.addReceiver(bbAgent.getSellerAgents()[i]);
                }
                cfp.setContent(bookTitle);
                cfp.setConversationId("book-trade");
                cfp.setReplyWith("cfp" + System.currentTimeMillis());
                myAgent.send(cfp);

                // Prepara la plantilla de mensajes para las respuestas de los vendedores.
                mt = MessageTemplate.and(MessageTemplate.MatchConversationId("book-trade"),
                        MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
                step = 1;
                break;

            case 1:
                // Paso 1: Recibe propuestas de vendedores y encuentra la mejor oferta.
                reply = bbAgent.receive(mt);
                if(reply != null) {
                    if(reply.getPerformative() == ACLMessage.PROPOSE) {
                        int price = Integer.parseInt(reply.getContent());
                        if(bestSeller == null || price < bestPrice) {
                            bestPrice = price;
                            bestSeller = reply.getSender();
                        }
                    }
                    repliesCount++;
                    if(repliesCount >= bbAgent.getSellerAgents().length) {
                        step = 2;
                    }
                } else {
                    block();
                }
                break;

            case 2:
                // Paso 2: Envía una orden de aceptación de propuesta al vendedor con el mejor precio.
                ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                order.addReceiver(bestSeller);
                order.setContent(bookTitle);
                order.setConversationId("book-trade");
                order.setReplyWith("order" + System.currentTimeMillis());
                bbAgent.send(order);

                // Prepara la plantilla de mensajes para la confirmación del vendedor.
                mt = MessageTemplate.and(MessageTemplate.MatchConversationId("book-trade"),
                        MessageTemplate.MatchInReplyTo(order.getReplyWith()));
                step = 3;
                break;

            case 3:
                // Paso 3: Recibe la confirmación de la venta del vendedor.
                reply = myAgent.receive(mt);
                if (reply != null) {
                    if (reply.getPerformative() == ACLMessage.INFORM) {
                        System.out.println(bookTitle+" successfully purchased from agent "+reply.getSender().getName());
                        System.out.println("Price = "+bestPrice);
                        myAgent.doDelete();
                    } else {
                        System.out.println("Attempt failed: requested book already sold.");
                    }
                    step = 4;
                } else {
                    block();
                }
                break;
        }
    }

    // Método que indica cuándo este comportamiento ha terminado.
    public boolean done() {
        if (step == 2 && bestSeller == null) {
            System.out.println("Attempt failed: "+bookTitle+" not available for sale");
        }
        return ((step == 2 && bestSeller == null) || step == 4);
    }
}
