package py.una.maverick.domain;

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
    
    public void getMano(List<Carta> cartas){
        Collections.sort(cartas);
        contarCartas(cartas);
        contarPalos(cartas);
        
    }
    
    private void contarCartas(List<Carta> cartas){
        for(int i = 0; i < cartas.size(); i++){
            int numero =cartas.get(i).getNumero();
            int cantidad = iguales.getOrDefault(numero, 0);
            cantidad++;
            palos.put(numero, cantidad);
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
    
    private Carta getCartaAlta(List<Carta> cartas){
        if(cartas == null || cartas.isEmpty()){
            return null;
        }
        Collections.sort(cartas);
        return cartas.get(cartas.size()-1);
    }
    
    private List<Carta> getPoker(List<Carta> cartas){
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
                Carta alta = getCartaAlta(restantes);
                if(alta != null){
                    poker.add(alta);
                }
                return poker;
            }
        }
        return null;
    }
    
    private List<Carta> getFull(List<Carta> cartas){
        List<Carta> trio = getTrio(cartas);
        List<Carta> restantes = new ArrayList<>();
        if(trio != null && !trio.isEmpty()){
            for (int i=0; i<cartas.size(); i++) {
                if(!cartas.get(i).getNumero().equals(trio.get(0).getNumero())){
                    restantes.add(cartas.get(i));
                }
            }
        }
        List<Carta> par = getPar(restantes);
        List<Carta> full = new ArrayList();
        if(trio != null && !trio.isEmpty() && par != null && !par.isEmpty()){
            full.addAll(trio);
            full.addAll(par);
        }
        
        return full.isEmpty() ? null : full;
    }
    
    /**
     * 
     * @param cartas
     * @return las cartas que conforman el mejor trio o null
     */
    private List<Carta> getTrio(List<Carta> cartas){
        List<Carta> trio = new ArrayList<>();
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
        return trio.isEmpty() ? null : trio;
    }
    /**
     * 
     * @param cartas
     * @return las cartas que conforman el mejor par o null
     */
    private List<Carta> getPar(List<Carta> cartas){
        List<Carta> par = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : iguales.entrySet()) {
            Integer key = entry.getKey();
            Integer value = entry.getValue();
            if(value.equals(2)){
                if(!par.isEmpty() && key > par.get(0).getNumero()){
                    par= new ArrayList<>();
                }
                if(par.isEmpty()){
                    for(int i=0; i<cartas.size(); i++){
                        if(cartas.get(i).getNumero().equals(key)){
                            par.add(cartas.get(i));
                        }
                    }
                }
            }
        }
        return par.isEmpty() ? null : par;
    }
    
    private List<Carta> getDosPares(List<Carta> cartas){
        List<Carta> par1 = getPar(cartas);
        if(par1 == null || par1.isEmpty()){
            return null;
        }
        List<Carta> restantes = new ArrayList<>();
        for(int i = 0; i < cartas.size(); i++){
            if(!par1.get(0).equals(cartas.get(i))){
                restantes.add(cartas.get(i));
            }
        }
        List<Carta> par2 = getPar(restantes);
        if(par2 == null || par2.isEmpty()){
            return null;
        }
        
        List<Carta> pares = new ArrayList();
        pares.addAll(par1);
        pares.addAll(par2);
        return pares;
    }
    
    private List<Carta> getEscalera(List<Carta> cartas){
        Collections.sort(cartas);
        List<Carta> escalera = new ArrayList<>();
        List<Carta> tmp = new ArrayList<>();
        //la siguiete carta debe ser numero + 1
        for(int i=0; i < cartas.size()-1; i++){
            int next = cartas.get(i+1).getNumero();
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
            return escalera;
        }
    }
    
}
