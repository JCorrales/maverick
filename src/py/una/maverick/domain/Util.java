
package py.una.maverick.domain;

/**
 *
 * @author Juan Andrés Corrales Duarte
 */
public class Util {
    /**
     * 
     * @param min minimo inclusive
     * @param max maximo inclusive
     * @return 
     */
    public static int randInt(int min, int max){
        return (int)Math.floor(Math.random()*(max-min+1)+min);
    }
    
}
