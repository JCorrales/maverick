
package py.una.maverick.domain;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Juan Andrés Corrales Duarte
 */
public abstract class Jugador {
    public final int PASAR = 0;
    public final int IGUALAR = 1;
    public final int SUBIR = 2;
    public final int RETIRARSE = 3;
    private String nombre;
    private Integer fichas;
    private Carta[] cartas = null;
    private Carta[] mano = null;
    private Mesa mesa;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }

    public Integer getFichas() {
        return fichas;
    }

    public void setFichas(Integer fichas) {
        this.fichas = fichas;
    }

    public Carta[] getCartas() {
        return cartas;
    }

    public void setCartas(Carta[] cartas) {
        this.cartas = cartas;
    }

    public Carta[] getMano() {
        return mano;
    }

    public void setMano(Carta[] mano) {
        this.mano = mano;
    }

    public abstract void setTurno();
      
    public void subir(Integer cantidad){
        if(cantidad > fichas){
            throw new RuntimeException("no hay suficientes fichas");
        }
        fichas = fichas - cantidad;
        
        mesa.apostar(nombre, cantidad);
        mesa.finTurno();
    }
    
    //igualar si es posible, all in en caso contrario
    public void igualar(){
        Integer mayor = 0;
        for (Integer apuesta : mesa.getApuestas().values()) {
            if(apuesta > mayor){
                mayor = apuesta;
            }
        }
        int cantidad;
        if(fichas >= mayor){
            cantidad = mayor;
            fichas = fichas - mayor;
        }else{
            cantidad = fichas;
            fichas = 0;
        }
        
        mesa.apostar(nombre, cantidad);
        mesa.finTurno();
    }
    
    public void pasar(){
        mesa.finTurno();
    }
    
    public void retirarse(){
        mesa.finTurno();
        mesa.finRonda(this);
    }

    public List<Integer> accionesPosibles(){
        int mayor = 0;
        for (String jugador : mesa.getApuestas().keySet()) {
            if(!jugador.equals(this.getNombre())){
                if(mesa.getApuestas().get(jugador) > mayor){
                    mayor = mesa.getApuestas().get(jugador);
                }
            }
        }
        List<Integer> acciones = new ArrayList<>();
        acciones.add(IGUALAR);//igualar o all in
        acciones.add(RETIRARSE);//retirarse
        if(new Integer(mayor).equals(mesa.getApuestas().get(this.getNombre()))){
            acciones.add(PASAR);
        }
        if(this.getFichas() > mayor){
            acciones.add(SUBIR);
        }
        return acciones;
    }
        
}
