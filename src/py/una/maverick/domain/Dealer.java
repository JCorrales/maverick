
package py.una.maverick.domain;

/**
 *
 * @author Juan Andrés Corrales Duarte
 */
public class Dealer {
    private Mesa mesa;
    private Mazo mazo;
    
    public Dealer(Mesa mesa){
        this.mesa = mesa;
        mazo = new Mazo();
    }
    
    public void startGame(){
        if(mesa.getJugadores().size()<2){
            throw new RuntimeException("Debe haber al menos dos jugadores");
        }
        mazo.mesclar();
        repartir();
        // fichas iniciales
        for(int i = 0; i <= mesa.getJugadores().size(); i++){
            mesa.getJugadores().get(i).setFichas(mesa.getBigBlind()*30);
        }
        
    }
    
    public void repartir(){
        for(int i = 0; i <= mesa.getJugadores().size(); i++){
            Carta[] cartas = new Carta[]{mazo.getCarta(), mazo.getCarta()};
            mesa.getJugadores().get(i).setCartas(cartas);
        }
    }
    
    
}
