
package py.una.maverick.domain;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Juan Andrés Corrales Duarte
 */
public abstract class Jugador {
    private String nombre;
    private Integer fichas;
    private Carta[] cartas = null;
    private Carta[] mano = null;
    private Mesa mesa;
    private Jugador rival;

    public Jugador getRival() {
        return rival;
    }

    public void setRival(Jugador rival) {
        this.rival = rival;
    }

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
        mesa.finTurno(true);
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
        mesa.finTurno(false);
    }
    
    public void pasar(){
        mesa.finTurno(false);
    }
    
    public void retirarse(){
        mesa.finTurno(false);
        mesa.setGanador(rival);
    }

    public List<Integer> accionesPosibles(){
        List<Integer> acciones = new ArrayList<>();
        acciones.add(Const.RETIRARSE);
        if(fichas == 0 || rival.getFichas() == 0){
            return acciones;
        }
        int mayor = 0;
        for (String jugador : mesa.getApuestas().keySet()) {
            if(!jugador.equals(this.getNombre())){
                if(mesa.getApuestas().get(jugador) > mayor){
                    mayor = mesa.getApuestas().get(jugador);
                }
            }
        }
        
        acciones.add(Const.IGUALAR);//igualar o all in
        if(new Integer(mayor).equals(mesa.getApuestas().get(this.getNombre()))){
            acciones.add(Const.PASAR);
        }
        if(this.getFichas() > mayor){
            acciones.add(Const.SUBIR);
        }
        return acciones;
    }

    @Override
    public boolean equals(Object obj) {
        return this.getNombre().equals(((Jugador) obj).getNombre());
    }
    
    
}
