package py.una.maverick.domain;

import java.util.List;

/**
 *
 * @author Juan Andrés Corrales Duarte
 */
public class Mano {
    private Integer valor;
    private List<Carta> mano;
    private List<Carta> kickers;

    public Mano(Integer valor, List<Carta> mano, List<Carta> kickers) {
        this.valor = valor;
        this.mano = mano;
        this.kickers = kickers;
    }

    public List<Carta> getKickers() {
        return kickers;
    }

    public void setKickers(List<Carta> kickers) {
        this.kickers = kickers;
    }

    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }

    public List<Carta> getMano() {
        return mano;
    }

    public void setMano(List<Carta> mano) {
        this.mano = mano;
    }
    
    
}
