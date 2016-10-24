
package py.una.maverick.domain;

import java.util.List;
import java.util.Set;


public interface Client {
    
    void messageReceived(String message);

    void joinedTable(int bigBlind, List<Jugador> players);
    
    void handStarted(Jugador dealer);
    
    void actorRotated(Jugador actor);
    
    void playerUpdated(Jugador player);
    

    void boardUpdated(List<Carta> cards, int bet, int pot);
    
    void playerActed(Jugador player);

    Integer act(int minBet, int currentBet, List<Integer> allowedActions);

}
