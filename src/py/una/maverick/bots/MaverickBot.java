package py.una.maverick.bots;

import java.util.ArrayList;
import java.util.List;
import py.una.maverick.domain.C;
import py.una.maverick.domain.Carta;
import py.una.maverick.domain.Jugador;
import py.una.maverick.domain.Mano;
import py.una.maverick.domain.TexasHoldem;
import py.una.maverick.domain.Util;

/**
 *
 * @author Juan Andrés Corrales Duarte
 */
public class MaverickBot extends Bot{
    
    Integer[][] preflop = new Integer[15][15];
    Integer q;
    Integer agresion;
    Integer valorFactor = 2;
    TexasHoldem reglas = new TexasHoldem();

    public MaverickBot(Integer q, Integer agresion) {
        super.setNombre("Maverick Bot");
        this.q = q;
        this.agresion = agresion;
        setPreflop();
        
    }
    
    @Override
    public void setTurno() {
        List<Integer> allowedActions = accionesPosibles();
        if(allowedActions.isEmpty()){
            pasar();
        }
        if(getMano() == null){
            igualar();
        }
        int win = 50;
        if(getMesa().getEstadoRonda() == C.PREFLOP){
            win = preflop[getMano()[0].getNumero()][getMano()[1].getNumero()];
        }
        
        
        boolean probabilistico = Util.randInt(0, 100) <= q ? true : false;
        
        if(getMesa().getEstadoRonda() == C.FLOP){
            Carta[] cartas = getCartas();
            List<Carta> todas = new ArrayList();
            todas.addAll(getMesa().getComunitarias());
            todas.add(cartas[0]);
            todas.add(cartas[1]);
            Mano mano = reglas.getMano(todas);
            win = win + mano.getValor()*valorFactor;
        }
        if(probabilistico){
            if(win > 70 && allowedActions.contains(C.SUBIR)){
                subir(getMesa().getBB()*3);
            }else if(win > 50 && allowedActions.contains(C.IGUALAR)){
                igualar();
            }else if(win < 50 && allowedActions.contains(C.PASAR)){
                pasar();
            }else if(win < 40){
                retirarse();
            }
            
        }
        
        if(allowedActions.contains(C.PASAR)){
            pasar();
        }
        pasar();
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

    private void setPreflop() {
        preflop[14][14] = 85;
        preflop[13][13] = 82;
        preflop[12][12] = 80;
        preflop[11][11] = 77;
        preflop[10][10] = 75;
        preflop[9][9] = 72;
        preflop[8][8] = 69;
        preflop[7][7] = 66;
        preflop[6][6] = 63;
        preflop[5][5] = 60;
        preflop[4][4] = 57;
        preflop[3][3] = 54;
        preflop[2][2] = 50;
        preflop[14][13] = 67;
        preflop[14][12] = 66;
        preflop[14][11] = 65;
        preflop[14][10] = 64;
        preflop[13][12] = 63;
        preflop[14][9] = 62;
        preflop[13][11] = 62;
        preflop[14][8] = 60;
        preflop[12][11] = 60;
        preflop[12][10] = 59;
        preflop[12][9] = 57;
        preflop[11][10] = 57;
        preflop[11][9] = 56;
        preflop[10][9] = 54;
        preflop[10][8] = 51;
        preflop[9][8] = 48;
        preflop[8][7] = 48;
        preflop[7][6] = 45;
        preflop[6][5] = 43;
        preflop[5][4] = 41;
        preflop[4][3] = 39;
        preflop[3][2] = 36;
        preflop[2][14] = 57;
        for(int i = 0; i < 15; i++) {
            for(int j = 0; j < 15; j++){
                if(preflop[i][j] == null){
                    preflop[i][j] = 20;
                    if(i == 14){
                        preflop[i][j] = preflop[i][j]+40;
                    }
                    if(i == 13){
                        preflop[i][j] = preflop[i][j]+32;
                    }
                    if(i == 12){
                        preflop[i][j] = preflop[i][j]+25;
                    }
                    if(i == 11){
                        preflop[i][j] = preflop[i][j]+22;
                    }
                }
                if(preflop[i][j] != null){
                    preflop[j][i] = preflop[i][j];
                }
            }
        }
    }
    
}
