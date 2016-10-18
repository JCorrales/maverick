
package py.una.maverick.domain;


/**
 *
 * @author Juan Andrés Corrales Duarte
 */
public class Carta {
    private final Integer palo;
    private final Short numero;
    
    public Carta(Integer palo, Short numero){
        this.palo = palo;
        this.numero = numero;
    }

    public Integer getPalo() {
        return palo;
    }

    public Short getNumero() {
        return numero;
    }

}
