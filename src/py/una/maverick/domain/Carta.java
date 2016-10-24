
package py.una.maverick.domain;


/**
 *
 * @author Juan Andrés Corrales Duarte
 */
public class Carta implements Comparable<Carta>{
    private final Integer palo;
    private final Integer numero;
    
    public Carta(Integer palo, Integer numero){
        this.palo = palo;
        this.numero = numero;
    }

    public Integer getPalo() {
        return palo;
    }

    public Integer getNumero() {
        return numero;
    }

    @Override
    public int compareTo(Carta o) {
        //FIXME: Carta.compareTo no consistente con equals
        return this.numero.compareTo(o.getNumero());
    }

    @Override
    public boolean equals(Object obj) {
        Carta objeto = null;
        try{
            objeto = (Carta) obj;
        }catch(Exception ex){
            return false;
        }
        
        if(!getNumero().equals(objeto.getNumero())){
            return false;
        }
        if(!getPalo().equals(objeto.getPalo())){
            return false;
        }
        return true;
    }

    
}
