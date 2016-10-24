
package py.una.maverick.bots;

import java.util.List;
import py.una.maverick.domain.C;
import py.una.maverick.domain.Carta;
import py.una.maverick.domain.Jugador;

public class DummyBot extends Bot {

    public DummyBot() {
        super.setNombre("Dummy Bot");
    }
    
    

    /** {@inheritDoc} */
    @Override
    public void messageReceived(String message) {
        // Not implemented.
    }

    /** {@inheritDoc} */
    @Override
    public void joinedTable(int bigBlind, List<Jugador> players) {
        // Not implemented.
    }

    /** {@inheritDoc} */
    @Override
    public void handStarted(Jugador dealer) {
        // Not implemented.
    }

    /** {@inheritDoc} */
    @Override
    public void actorRotated(Jugador actor) {
        // Not implemented.
    }

    /** {@inheritDoc} */
    @Override
    public void playerUpdated(Jugador player) {
        // Not implemented.
    }

    /** {@inheritDoc} */
    @Override
    public void boardUpdated(List<Carta> cards, int bet, int pot) {
        // Not implemented.
    }

    /** {@inheritDoc} */
    @Override
    public void playerActed(Jugador player) {
        // Not implemented.
    }

    /** {@inheritDoc} */
    @Override
    public Integer act(int minBet, int currentBet, List<Integer> allowedActions) {
        if (allowedActions.contains(C.PASAR)) {
            return C.PASAR;
        } else {
            return C.IGUALAR;
        }
    }

    @Override
    public void setTurno() {
        if (accionesPosibles().contains(C.PASAR)) {
            pasar();
        } else {
            igualar();
        }
    }
    
}
