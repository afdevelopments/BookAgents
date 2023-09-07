package com.afdevelopment.agentes;
import jade.core.AID;
import jade.core.Agent;
import com.afdevelopment.behaviours.RequestPerformer;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

public class Comprador extends Agent{
    private String titulo;
    private AID[] sellerAgents;
    private final int ticker_timer = 10000;
    private final Comprador this_agent = this;
    
    protected void setup(){
        System.out.println("Soy el agente comprador "+ getAID().getName());
        Object[] args = getArguments();
        if(args != null && args.length>0){
            titulo = (String) args[0];
            System.out.println("Vamos a comprar: " + titulo);
            addBehaviour(new TickerBehaviour(this, ticker_timer) {
                @Override
                protected void onTick() {
                    System.out.println("Intentando comprar "+titulo);
                    DFAgentDescription template = new DFAgentDescription();
                    ServiceDescription sd = new ServiceDescription();
                    sd.setType("book-selling");
                    template.addServices(sd);
                    try {
                        DFAgentDescription[] result = DFService.search(myAgent, template);
                        System.out.println("Vendedores encontrados:");
                        sellerAgents = new AID[result.length];
                        for(int i = 0; i < result.length; i++){
                            sellerAgents[i] = result[i].getName();
                            System.out.println(sellerAgents[i].getName());
                        }
                    }catch (FIPAException e){
                        e.printStackTrace();
                    }
                    myAgent.addBehaviour(new RequestPerformer(this_agent));
                }
            });
        }else{
            System.out.println("No se especificó un titulo para comprar.");
            doDelete();
        }
    }
    protected void takeDown(){
        System.out.println("Finalizando el agente comprador "+ getAID().getName());
    }
    public AID[] getSellerAgents() {
        return sellerAgents;
    }

    public String getBookTitle() {
        return titulo;
    }
}
