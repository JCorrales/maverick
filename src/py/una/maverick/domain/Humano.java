package py.una.maverick.domain;

import java.util.List;
import py.una.maverick.domain.gui.Main;

/**
 *
 * @author Juan Andrés Corrales Duarte
 */
public class Humano extends Jugador{
    private Main main;
    
    public Humano(Main main) {
        this.main = main;
        super.setNombre("humano");
    }
    
    

    @Override
    public void setTurno() {
        main.playerUpdated(getRival());
        main.playerUpdated(this);
        main.boardUpdated(getMesa().getComunitarias(), getMesa().getBB(), getMesa().getPozo());
        List<Integer> acciones = this.accionesPosibles();
        if(getMesa().getEstadoRonda().equals(C.SHOWDOWN)){
            acciones.clear();
            acciones.add(C.PASAR);
        }
        if(acciones.isEmpty()){
            this.pasar();
        }
        Integer accion = main.act(getMesa().getBB(), getMesa().getBB(), acciones);
        switch(accion){
            case C.SUBIR:
                //this.subir(10);
                break;
            case C.PASAR:
                if(getMesa().getEstadoRonda().equals(C.SHOWDOWN)){
                    getMesa().iniciarRonda();
                }
                this.pasar();
                break;
            case C.IGUALAR:
                this.igualar();
                break;
            default:
                this.retirarse();
                break;    
        }
    }

    @Override
    public void messageReceived(String message) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void joinedTable(int bigBlind, List<Jugador> players) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void handStarted(Jugador dealer) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void actorRotated(Jugador actor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void playerUpdated(Jugador player) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void boardUpdated(List<Carta> cards, int bet, int pot) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void playerActed(Jugador player) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer act(int minBet, int currentBet, List<Integer> allowedActions) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
