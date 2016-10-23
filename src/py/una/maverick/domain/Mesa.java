
package py.una.maverick.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Juan Andrés Corrales Duarte
 */
public class Mesa {
    public Integer porHablar = 2;
    private Integer estadoRonda = 0;
    private Integer turno = 0;
    private List<Jugador> jugadores = new ArrayList<>();
    //el tiempo en segundos que tiene el jugador para accionar
    private Integer tiempo = 8;
    private Mazo mazo;
    private Carta[] comunitarias;
    private Integer pozo;
    private final Integer BB = 10;
    private Map<String, Integer> apuestas;

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public void setJugadores(List<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    public Integer getTiempo() {
        return tiempo;
    }

    public void setTiempo(Integer tiempo) {
        this.tiempo = tiempo;
    }

    public Carta[] getComunitarias() {
        return comunitarias;
    }

    public void setComunitarias(Carta[] comunitarias) {
        this.comunitarias = comunitarias;
    }

    public Integer getPozo() {
        return pozo;
    }

    public void setPozo(Integer pozo) {
        this.pozo = pozo;
    }

    public void finTurno(boolean haSubido) {
        turno++;
        if((turno + 1) > jugadores.size()){
            turno = 0;
        }
        if(!haSubido){
            porHablar--;
            if(porHablar == 0){
                finRonda();
            }
        }
        if(porHablar > 0){
            jugadores.get(turno).setTurno();
        }
    }
    
    public void startGame(){
        // fichas iniciales
        for(int i = 0; i <= this.getJugadores().size(); i++){
            this.getJugadores().get(i).setFichas(BB*30);
        }
        this.getJugadores().get(0).setRival(this.getJugadores().get(1));
        this.getJugadores().get(1).setRival(this.getJugadores().get(0));

    }
    
    public void iniciarRonda(){
        estadoRonda = C.PREFLOP;
        mazo.mesclar();
        repartir();
        setCiegas();
        jugadores.get(turno).setTurno();
    }
    
    public void setCiegas(){
        jugadores.get(turno).subir(BB/2);
        jugadores.get(turno > jugadores.size()-1 ? 0 : turno).subir(BB);
        
    }
    
    public void repartir(){
        for(int i = 0; i <= this.getJugadores().size(); i++){
            Carta[] cartas = new Carta[]{mazo.getCarta(), mazo.getCarta()};
            this.getJugadores().get(i).setCartas(cartas);
        }
    }

    public Map<String, Integer> getApuestas() {
        return apuestas;
    }

    public void setApuestas(Map<String, Integer> apuestas) {
        this.apuestas = apuestas;
    }
   
    //usado para subir e igualar
    public void apostar(String jugador, Integer cantidad){
        apuestas.put(jugador, apuestas.get(jugador)+cantidad);
        pozo = pozo + cantidad;
    }
    
    public void setGanador(Jugador jugador){
        for(int i=0; i<jugadores.size(); i++){
            jugadores.get(i).setCartas(null);
            jugadores.get(i).setMano(null);
        }
        jugador.setFichas(jugador.getFichas()+pozo);
        apuestas.clear();
        pozo = 0;
        comunitarias = null;
    }
    
    public void finRonda(){
        porHablar = jugadores.size();
        estadoRonda++;
        switch(estadoRonda){
            case C.FLOP:
                flop();
            case C.TURN:
                turn();
            case C.RIVER:
                river();
            case C.SHOWDOWN:
                showDown();
        }
    }
    
    public String showDown(){
        
        return "ganador";
    }
    
    private void flop(){
        comunitarias = new Carta[5];
        for(int i = 0; i < 5; i++){
            comunitarias[i] = null;
        }
        comunitarias[0] = mazo.getCarta();
        comunitarias[1] = mazo.getCarta();
        comunitarias[2] = mazo.getCarta();
        jugadores.get(turno).setTurno();
    }
    
    private void turn(){
        comunitarias[3] = mazo.getCarta();
        jugadores.get(turno).setTurno();
    }
    
    private void river(){
        comunitarias[4] = mazo.getCarta();
        jugadores.get(turno).setTurno();
    }
        
}
