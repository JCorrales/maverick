
package py.una.maverick.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Juan Andrés Corrales Duarte
 */
public class Mesa {
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

    public void finTurno() {
        turno++;
        if((turno + 1) > jugadores.size()){
            turno = 0;
        }
        jugadores.get(turno).setTurno();
    }
    
    public void startGame(){
        // fichas iniciales
        for(int i = 0; i <= this.getJugadores().size(); i++){
            this.getJugadores().get(i).setFichas(BB*30);
        }

    }
    
    public void iniciarRonda(){
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
   
    //usado para apostar e igualar
    public void apostar(String jugador, Integer cantidad){
        apuestas.put(jugador, apuestas.get(jugador)+cantidad);
        pozo = pozo + cantidad;
    }
    
    public void finRonda(Jugador jugador){
        for(int i=0; i<jugadores.size(); i++){
            if(!jugadores.get(i).getNombre().equals(jugador.getNombre())){
                jugadores.get(i).setCartas(null);
                jugadores.get(i).setMano(null);
                jugadores.get(i).setFichas(jugadores.get(i).getFichas()
                        +pozo);
            }
        }
        apuestas.clear();
        pozo = 0;
        comunitarias = null;
    }
    
}
