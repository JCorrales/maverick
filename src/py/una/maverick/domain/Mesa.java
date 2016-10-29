
package py.una.maverick.domain;

import java.util.ArrayList;
import java.util.HashMap;
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
    private List<Carta> comunitarias = new ArrayList<>();
    private Integer pozo = 0;
    private final Integer BB = 10;
    private Map<String, Integer> apuestas = new HashMap<>();
    private TexasHoldem reglas = new TexasHoldem();
    private Jugador dealer;
    private Integer currentBet = 0;

    public Integer getCurrentBet() {
        return currentBet;
    }

    public void setCurrentBet(Integer currentBet) {
        this.currentBet = currentBet;
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

    public List<Carta> getComunitarias() {
        return comunitarias;
    }

    public void setComunitarias(List<Carta> comunitarias) {
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

    public Mesa() {
        
        this.mazo = new Mazo();
    }
    
    public void startGame(){
        // fichas iniciales
        for(int i = 0; i < this.getJugadores().size(); i++){
            this.getJugadores().get(i).setFichas(BB*30);
            this.getJugadores().get(i).setMesa(this);
        }
        this.getJugadores().get(0).setRival(this.getJugadores().get(1));
        this.getJugadores().get(1).setRival(this.getJugadores().get(0));
        dealer = getJugadores().get(0);
        iniciarRonda();
    }
    
    public void iniciarRonda(){
        resetApuestas();
        currentBet = 0;
        pozo = 0;
        comunitarias = null;
        mazo = new Mazo();
        dealer = dealer.getRival();
        for(int i=0; i<jugadores.size(); i++){
            jugadores.get(i).setCartas(null);
            jugadores.get(i).setMano(null);
        }
        estadoRonda = C.PREFLOP;
        mazo.mezclar();
        repartir();
        setCiegas();
        jugadores.get(turno).setTurno();
    }
    
    public void setCiegas(){
        dealer.pagarCiegas(BB/2);
        dealer.getRival().pagarCiegas(BB);
        int index = jugadores.indexOf(dealer);
        turno = index;
    }
    
    public void repartir(){
        for(int i = 0; i < this.getJugadores().size(); i++){
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
    
    public void resetApuestas() {
        for (Map.Entry<String, Integer> entrySet : apuestas.entrySet()) {
            String key = entrySet.getKey();
            Integer value = entrySet.getValue();
            this.apuestas.put(key, 0);
        }
    }
   
    //usado para subir e igualar
    public void apostar(String jugador, Integer cantidad){
        apuestas.put(jugador, apuestas.getOrDefault(jugador, 0)+cantidad);
        pozo = pozo + cantidad;
    }
    
    public void setGanador(Jugador jugador){
        jugador.setFichas(jugador.getFichas()+pozo);
        if(estadoRonda.equals(C.SHOWDOWN)){
            jugadores.get(turno).setTurno();//esperar para ver las cartas
            estadoRonda = C.PREFLOP;
        }
        iniciarRonda();
    }
    
    public void setEmpate(){
        System.out.println("Empate!");
        Jugador jugador0 = jugadores.get(0);
        jugador0.setFichas(
                jugador0.getFichas()+apuestas.get(jugador0.getNombre()));
        Jugador jugador1 = jugadores.get(1);
        jugador1.setFichas(
                jugador1.getFichas()+apuestas.get(jugador1.getNombre()));
        if(estadoRonda.equals(C.SHOWDOWN)){
            jugadores.get(turno).setTurno();//esperar para ver las cartas
            estadoRonda = C.PREFLOP;
        }
        iniciarRonda();
    }

    public Integer getEstadoRonda() {
        return estadoRonda;
    }
    
    public void setEstadoRonda(Integer estado) {
        this.estadoRonda = estado;
    }
    
    
    
    public void finRonda(){
        porHablar = jugadores.size();
        estadoRonda++;
        resetApuestas();
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
    
    public void showDown(){
        Map<Jugador, Mano> manos = new HashMap<>();
        for(int i =0; i< jugadores.size(); i++){
            Carta[] cartas = jugadores.get(i).getCartas();
            List<Carta> todas = new ArrayList();
            todas.addAll(comunitarias);
            todas.add(cartas[0]);
            todas.add(cartas[1]);
            Mano mano = reglas.getMano(todas);
            System.out.println("mano size: "+mano.getMano().size());
            manos.put(jugadores.get(i), mano);
        }
        Jugador ganador = null;
        int resultado = manos.get(jugadores.get(0)).compareTo(manos.get(jugadores.get(1)));
        if(resultado == 1){
            ganador = jugadores.get(0);
            System.out.println("Ganador: "+jugadores.get(0).getNombre());
            for(Carta carta : manos.get(jugadores.get(0)).getMano()){
                System.out.println(carta.getNumero()+" "+carta.getPalo());
            }
            System.out.println();
        }else if(resultado == -1){
            System.out.println("Ganador: "+jugadores.get(1).getNombre());
            for(Carta carta : manos.get(jugadores.get(1)).getMano()){
                System.out.print(carta.getNumero()+" ");
            }
            System.out.println();
            ganador = jugadores.get(1);
        }else{
            setEmpate();
            return;
        }
        setGanador(ganador);
    }
    
    private void flop(){
        comunitarias = new ArrayList<>();
        comunitarias.add(mazo.getCarta());
        comunitarias.add(mazo.getCarta());
        comunitarias.add(mazo.getCarta());
        jugadores.get(turno).setTurno();
    }
    
    private void turn(){
        comunitarias.add(mazo.getCarta());
        jugadores.get(turno).setTurno();
    }
    
    private void river(){
        comunitarias.add(mazo.getCarta());
        jugadores.get(turno).setTurno();
    }

    public Integer getBB() {
        return BB;
    }

    public Integer getTurno() {
        return turno;
    }
    
    
        
}
