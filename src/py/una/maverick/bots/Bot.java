
package py.una.maverick.bots;

import py.una.maverick.domain.Client;
import py.una.maverick.domain.Jugador;

public abstract class Bot extends Jugador implements Client {
    
    /** Number of hole cards. */
    protected static final int NO_OF_HOLE_CARDS = 2;
    
}
