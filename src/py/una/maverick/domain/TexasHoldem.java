package py.una.maverick.domain;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * @author Juan Andrés Corrales Duarte
 */
public class TexasHoldem {
    Map<Integer, Integer> iguales = new HashMap<>();
    Map<Integer, Integer> palos = new HashMap<>();
    
    public Mano getMano(List<Carta> cartas){
        iguales.clear();
        palos.clear();
        Collections.sort(cartas);
        contarCartas(cartas);
        contarPalos(cartas);
        Mano mano = null;
        mano = getPoker(cartas);
        if(mano != null){
            return mano;
        }
        mano = getFull(cartas);
        if(mano != null){
            return mano;
        }

        mano = getColor(cartas);
        if(mano != null){
            return mano;
        }
        mano = getEscalera(cartas);
        if(mano != null){
            return mano;
        }
        mano = getTrio(cartas);
        if(mano != null){
            return mano;
        }
        mano = getDosPares(cartas);
        if(mano != null){
            return mano;
        }

        mano = getPar(cartas);
        if(mano != null){
            return mano;
        }
        mano = getCartaAlta(cartas);
        return mano;
    }
    
    private void contarCartas(List<Carta> cartas){
        for(int i = 0; i < cartas.size(); i++){
            Integer numero =cartas.get(i).getNumero();
            int cantidad = iguales.getOrDefault(numero, 0);
            cantidad++;
            iguales.put(numero, cantidad);
        }
    }
    
    private void contarPalos(List<Carta> cartas){
        for(int i = 0; i < cartas.size(); i++){
            int palo =cartas.get(i).getPalo();
            int cantidad = palos.getOrDefault(palo, 0);
            cantidad++;
            palos.put(palo, cantidad);
        }
    }
    
    private Mano getCartaAlta(List<Carta> cartas){
        if(cartas == null || cartas.isEmpty()){
            return null;
        }
        Collections.sort(cartas);
        List<Carta> mano = new ArrayList<>();
        mano.add(cartas.get(cartas.size()-1));
        return new Mano(C.CARTA_ALTA, mano, null);
    }
    
    private Mano getPoker(List<Carta> cartas){
        List<Carta> mano = new ArrayList<>();
        List<Carta> kickers = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : iguales.entrySet()) {
            Integer key = entry.getKey();
            Integer value = entry.getValue();
            if(value.equals(4)){
                List<Carta> poker = new ArrayList<>();
                List<Carta> restantes = new ArrayList<>();
                for(int i=0; i<cartas.size(); i++){
                    if(cartas.get(i).getNumero().equals(key)){
                        poker.add(cartas.get(i));
                    }else{
                        restantes.add(cartas.get(i));
                    }
                }
                Carta alta = getCartaAlta(restantes).getMano().get(0);
                if(alta != null){
                    //poker.add(alta);
                    kickers.add(alta);
                }
                return new Mano(C.POKER, mano, kickers);
            }
        }
        return null;
    }
    
    private Mano getFull(List<Carta> cartas){
        Mano trioMano = getTrio(cartas);
        if(trioMano == null){
            return null;
        }
        List<Carta> trio = trioMano.getMano();
        List<Carta> restantes = new ArrayList<>();
        Mano mano = null;
        for (int i=0; i<cartas.size(); i++) {
            if(!cartas.get(i).getNumero().equals(trio.get(0).getNumero())){
                restantes.add(cartas.get(i));
            }
        }
        Mano parMano = getPar(restantes);
        if(parMano == null){
            return null;
        }
        List<Carta> par = parMano.getMano();
        List<Carta> full = new ArrayList();
        full.addAll(trio);
        full.addAll(par);
        mano = new Mano(C.FULL, full, null);
        
        return mano;
    }
    
    /**
     * 
     * @param cartas
     * @return las cartas que conforman el mejor trio o null
     */
    private Mano getTrio(List<Carta> cartas){
        List<Carta> trio = new ArrayList<>();
        Mano mano = null;
        for (Map.Entry<Integer, Integer> entry : iguales.entrySet()) {
            Integer key = entry.getKey();
            Integer value = entry.getValue();
            if(value.equals(3)){
                if(!trio.isEmpty() && key > trio.get(0).getNumero()){
                    trio = new ArrayList<>();
                }
                if(trio.isEmpty()){
                    for(int i=0; i<cartas.size(); i++){
                        if(cartas.get(i).getNumero().equals(key)){
                            trio.add(cartas.get(i));
                        }
                    }
                }
            }
        }
        if(trio.isEmpty()){
            return null;
        }
        List<Carta> restantes = minus(cartas, trio);
        List<Carta> kickers = new ArrayList();
        Carta alta1 = getCartaAlta(restantes).getMano().get(0);
        if(alta1 != null){
            restantes.remove(alta1);//FIXME: Carta.compareTo no consistente con equals
            kickers.add(alta1);
            Carta alta2 = getCartaAlta(restantes).getMano().get(0);
            if(alta2 != null){
                kickers.add(alta2);
            }
        }
        
        return new Mano(C.TRIO, trio, kickers);
    }
    
    private List<Carta> minus(List<Carta> mayor, List<Carta> menor){
        if(mayor == null || mayor.isEmpty()){
            return new ArrayList<>();
        }
        if(menor == null || menor.isEmpty()){
            return new ArrayList<>();
        }
        List<Carta> restantes = new ArrayList<>();
        for(int i = 0; i < mayor.size(); i++){
            boolean esta = false;
            for(int j = 0; j < menor.size(); j++){
                if(mayor.get(i).getNumero().equals(menor.get(j).getNumero())){
                    if(mayor.get(i).getPalo().equals(menor.get(j).getPalo())){
                        esta = true;
                    }
                }
            }
            if(!esta){
                restantes.add(mayor.get(i));
            }
        }
        return restantes;
    }
    /**
     * 
     * @param cartas
     * @return las cartas que conforman el mejor par o null
     */
    private Mano getPar(List<Carta> cartas){
        List<Carta> par = new ArrayList<>();
        int mejorPar = -1;
        for (Map.Entry<Integer, Integer> entry : iguales.entrySet()) {
            Integer key = entry.getKey();
            Integer value = entry.getValue();
            if(value.equals(2)){
                if(key.intValue() > mejorPar){
                    mejorPar = key.intValue();
                }
            }
        }
        for(Carta carta : cartas){
            if(carta.getNumero().intValue() == mejorPar){
                par.add(carta);
            }
        }
        if(par.isEmpty()){
            return null;
        }
        List<Carta> kickers = minus(cartas, par);
        return new Mano(C.PAR, par, kickers);
    }
    
    private Mano getDosPares(List<Carta> cartas){
        /*Mano parMano = getPar(cartas);
        if(parMano == null){
            return null;
        }
        List<Carta> par1 = parMano.getMano();
        List<Carta> restantes = minus(cartas, par1);
        Mano parMano2 = getPar(restantes);
        if(parMano2 == null){
            return null;
        }
        List<Carta> par2 = parMano2.getMano();
        List<Carta> pares = new ArrayList();
        pares.addAll(par1);
        pares.addAll(par2);
        restantes = minus(cartas, pares);
        Mano altaMano = getCartaAlta(restantes);
        if(altaMano == null){
            return new Mano(C.DOS_PARES, pares, null);
        }
        Carta alta = altaMano.getMano().get(0);
        List<Carta> kickers = new ArrayList<>();
        if(alta != null){
            kickers.add(alta);
        }*/
        List<Carta> par1 = new ArrayList<>();
        int mejorPar = -1;
        for (Map.Entry<Integer, Integer> entry : iguales.entrySet()) {
            Integer key = entry.getKey();
            Integer value = entry.getValue();
            if(value.equals(2)){
                if(key.intValue() > mejorPar){
                    mejorPar = key.intValue();
                }
            }
        }
        for(Carta carta : cartas){
            if(carta.getNumero().intValue() == mejorPar){
                par1.add(carta);
            }
        }
        if(par1.isEmpty()){
            return null;
        }
        
        List<Carta> par2 = new ArrayList<>();
        int mejorPar2 = -1;
        for (Map.Entry<Integer, Integer> entry : iguales.entrySet()) {
            Integer key = entry.getKey();
            Integer value = entry.getValue();
            if(value.equals(2)){
                if(key.intValue() > mejorPar2 && key != mejorPar){
                    mejorPar2 = key.intValue();
                }
            }
        }
        for(Carta carta : cartas){
            if(carta.getNumero().intValue() == mejorPar2){
                par2.add(carta);
            }
        }
        if(par2.isEmpty()){
            return null;
        }
        List<Carta> pares = new ArrayList<>();
        pares.addAll(par1);
        pares.addAll(par2);
        Collections.sort(pares);
        
        return new Mano(C.DOS_PARES, pares, minus(cartas, pares));
    }
    
    private Mano getEscalera(List<Carta> cartas){
        Collections.sort(cartas);
        List<Carta> escalera = new ArrayList<>();
        List<Carta> tmp = new ArrayList<>();
        //la siguiete carta debe ser numero + 1
        for(int i=0; i < cartas.size()-1; i++){
            int next = cartas.get(i+1).getNumero();
            if(cartas.get(i).getNumero().equals(14)){
                if(next == 2){
                    tmp.add(cartas.get(i));
                    continue;
                }
            }
            if(cartas.get(i).getNumero()+1 == next || cartas.get(i).getNumero() == next){
                if(cartas.get(i).getNumero()+1 == next){
                    tmp.add(cartas.get(i));
                }else{
                    //TO DO: decidir color
                }
            }else{//opa escalera
                if(tmp.size() > escalera.size()){
                    escalera = tmp;
                }
                tmp = new ArrayList<>();
            }
        }
        if(tmp.size() > escalera.size()){
            escalera = tmp;
        }
        Collections.sort(escalera);
        if(escalera.size() == 6){
            escalera.remove(0);
        }else if(escalera.size() == 7){
            escalera.remove(0);
            escalera.remove(1);
        }
        if(escalera.size() < 5){
            return null;
        }else{
            return new Mano(C.ESCALERA, escalera, null);
        }
    }
    
    private Mano getColor(List<Carta> cartas){
        for (Map.Entry<Integer, Integer> entry : palos.entrySet()) {
            Integer key = entry.getKey();
            Integer value = entry.getValue();
            if(value.equals(5)){
                List<Carta> color = new ArrayList<>();
                List<Carta> restantes = new ArrayList<>();
                for(int i=0; i<cartas.size(); i++){
                    if(cartas.get(i).getPalo().equals(key)){
                        color.add(cartas.get(i));
                    }
                }
                return new Mano(C.COLOR, color, null);
            }
        }
        return null;
    }
}
