package com.afdevelopment.behaviours;

import com.afdevelopment.agentes.Vendedor;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * Este comportamiento se encarga de gestionar las órdenes de compra que recibe el agente vendedor.
 * Es cíclico, por lo que siempre está esperando recibir mensajes de propuestas aceptadas para gestionar una venta.
 */
public class PurchaseOrderServer extends CyclicBehaviour {

    // Referencia al agente vendedor asociado a este comportamiento.
    private Vendedor bsAgent;

    /**
     * Constructor de PurchaseOrderServer.
     * @param a El agente vendedor al que se asociará este comportamiento.
     */
    public PurchaseOrderServer(Vendedor a) {
        this.bsAgent = a;
    }

    /**
     * Define las acciones que realiza el comportamiento.
     */
    @Override
    public void action() {
        // Define un patrón para recibir solo los mensajes que tengan una propuesta aceptada.
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
        ACLMessage msg = bsAgent.receive(mt);

        // Si se recibe un mensaje...
        if(msg != null) {
            String title = msg.getContent();  // Obtiene el título del libro del contenido del mensaje.
            ACLMessage reply = msg.createReply();  // Crea una respuesta al mensaje.

            // Intenta remover el libro del catálogo del vendedor y obtener su precio.
            // Si el libro no está disponible, price será null.
            Integer price = (Integer) bsAgent.getCatalogue().remove(title);

            // Si el libro está disponible (su precio no es null)...
            if(price != null) {
                // Establece que la respuesta es de tipo INFORM para notificar que la transacción se realizó.
                reply.setPerformative(ACLMessage.INFORM);
                System.out.println(title + " sold to agent " + msg.getSender().getName());
            } else {
                // Si el libro no está disponible, envía un mensaje de fallo.
                reply.setPerformative(ACLMessage.FAILURE);
                reply.setContent("not-available");
            }
            bsAgent.send(reply);  // Envía la respuesta al agente comprador.
        } else {
            block();  // Si no se recibe un mensaje, pone el comportamiento en espera hasta que llegue uno.
        }
    }
}
