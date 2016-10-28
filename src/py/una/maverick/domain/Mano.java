package py.una.maverick.domain;

import java.util.Collections;
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
    
    public int compareTo(Mano otra){
        System.out.println("this: "+ C.RANK_NAMES[this.valor]);
        System.out.println("other: "+ C.RANK_NAMES[otra.getValor().intValue()]);
        if(this.valor > otra.getValor().intValue()){
            return 1;
        }
        if(this.valor < otra.getValor().intValue()){
            return -1;
        }
        
        if(valor == C.PAR){
            if(mano.get(0).getNumero() > otra.getMano().get(0).getNumero()){
                return 1;
            }else if(mano.get(0).getNumero() < otra.getMano().get(0).getNumero()){
                return -1;
            }
        }
        
        if(valor == C.DOS_PARES){
            if(mano.get(0).getNumero() > otra.getMano().get(0).getNumero()){
                return 1;
            }else if(mano.get(0).getNumero() < otra.getMano().get(0).getNumero()){
                return -1;
            }
            if(mano.get(2).getNumero() > otra.getMano().get(2).getNumero()){
                return 1;
            }else if(mano.get(2).getNumero() < otra.getMano().get(2).getNumero()){
                return -1;
            }
        }
        
        if(valor == C.TRIO){
            if(mano.get(0).getNumero() > otra.getMano().get(0).getNumero()){
                return 1;
            }else if(mano.get(0).getNumero() < otra.getMano().get(0).getNumero()){
                return -1;
            }
        }
        
        if(valor == C.ESCALERA){
            if(mano.get(4).getNumero() > otra.getMano().get(4).getNumero()){
                return 1;
            }else if(mano.get(4).getNumero() < otra.getMano().get(4).getNumero()){
                return -1;
            }
        }
        if(valor == C.FULL){
            if(mano.get(0).getNumero() > otra.getMano().get(0).getNumero()){
                return 1;
            }else if(mano.get(0).getNumero() < otra.getMano().get(0).getNumero()){
                return -1;
            }
            if(mano.get(3).getNumero() > otra.getMano().get(3).getNumero()){
                return 1;
            }else if(mano.get(3).getNumero() < otra.getMano().get(3).getNumero()){
                return -1;
            }
        }
        
        if(valor == C.POKER){
            if(mano.get(0).getNumero() > otra.getMano().get(0).getNumero()){
                return 1;
            }else if(mano.get(0).getNumero() < otra.getMano().get(0).getNumero()){
                return -1;
            }
        }
        
        //si this tiene kicker asumo que la otra mano tambien
        if(kickers == null || kickers.isEmpty()){
            return 0;
        }
        Collections.sort(kickers);
        Collections.sort(otra.getKickers());
        int i = 0;
        for(Carta carta : kickers){
            if(carta.getNumero() > otra.getKickers().get(i).getNumero()){
                return 1;
            }else if(carta.getNumero() < otra.getKickers().get(i).getNumero()){
                return -1;
            }
            i++;
        }
        return 0;
        
    }
}
