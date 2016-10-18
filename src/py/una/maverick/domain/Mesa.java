
package py.una.maverick.domain;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Juan Andrés Corrales Duarte
 */
public class Mesa {
    private Integer boton = 0;
    private List<Jugador> jugadores = new ArrayList<>();
    //el tiempo en segundos que tiene el jugador para accionar
    private Integer tiempo = 8;
    private Carta[] comunitarias;
    private Integer pozo;
    private Integer bigBlind;
    
    public void apostar(Jugador jugador, Integer cuanto){
        
    }

    public Integer getBoton() {
        return boton;
    }

    public void setBoton(Integer boton) {
        this.boton = boton;
    }

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

    public Integer getBigBlind() {
        return bigBlind;
    }

    public void setBigBlind(Integer bigBlind) {
        this.bigBlind = bigBlind;
    }
}
