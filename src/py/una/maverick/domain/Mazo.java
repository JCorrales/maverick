
package py.una.maverick.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Juan Andrés Corrales Duarte
 */
public class Mazo {
    private final List<Carta> cartas = new ArrayList<>();
    public final Map<Integer, String> palos = new HashMap<>();
    //public final Map<Integer, String> nombres = new HashMap<>();
    
    public Mazo(){
        palos.put(0, "Treboles");
        palos.put(1, "Corazones");
        palos.put(2, "Espadas");
        palos.put(3, "Diamantes");        
    }
    
    public void mesclar(){
        for (Integer palo : palos.keySet()) {
            for(short i=2; i<=14; i++){
                cartas.add( new Carta(palo, i));
            }
        }
        
        if(cartas.size() != 52){
            throw new RuntimeException("52 cartas");
        }
    }

    public Carta getCarta() {
        int index = Util.randInt(0, cartas.size()-1);
        Carta carta = cartas.get(index);
        cartas.remove(index);
        return carta;
    }
    
}
