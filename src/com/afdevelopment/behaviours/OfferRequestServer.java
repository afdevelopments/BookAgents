package com.afdevelopment.behaviours;

import com.afdevelopment.agentes.Vendedor;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * Este comportamiento se encarga de gestionar las solicitudes de oferta que recibe el agente vendedor.
 * Es cíclico, por lo que siempre está esperando recibir mensajes que soliciten el precio de un libro.
 */
public class OfferRequestServer extends CyclicBehaviour {

    // Referencia al agente vendedor asociado a este comportamiento.
    private Vendedor bsAgent;

    /**
     * Constructor de OfferRequestServer.
     * @param a El agente vendedor al que se asociará este comportamiento.
     */
    public OfferRequestServer(Vendedor a) {
        this.bsAgent = a;
    }

    /**
     * Define las acciones que realiza el comportamiento.
     */
    @Override
    public void action() {
        // Define un patrón para recibir solo los mensajes que solicitan una propuesta (CFP = Call For Proposal).
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
        ACLMessage msg = bsAgent.receive(mt);

        // Si se recibe un mensaje...
        if(msg != null) {
            String title = msg.getContent();  // Obtiene el título del libro del contenido del mensaje.
            ACLMessage reply = msg.createReply();  // Crea una respuesta al mensaje.

            // Consulta el precio del libro solicitado en el catálogo del vendedor.
            Integer price = (Integer) bsAgent.getCatalogue().get(title);

            // Si el libro está disponible (su precio no es null)...
            if(price != null) {
                // Establece que la respuesta es una propuesta con el precio del libro.
                reply.setPerformative(ACLMessage.PROPOSE);
                reply.setContent(String.valueOf(price.intValue()));
            } else {
                // Si el libro no está disponible, envía un mensaje rechazando la solicitud.
                reply.setPerformative(ACLMessage.REFUSE);
                reply.setContent("not-available");
            }

            bsAgent.send(reply);  // Envía la respuesta al agente comprador.
        } else {
            block();  // Si no se recibe un mensaje, pone el comportamiento en espera hasta que llegue uno.
        }
    }
}
