// This file is part of the 'texasholdem' project, an open source
// Texas Hold'em poker application written in Java.
//
// Copyright 2009 Oscar Stigter
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package py.una.maverick.bots;

import java.util.List;
import py.una.maverick.domain.C;
import py.una.maverick.domain.Carta;
import py.una.maverick.domain.Jugador;


/**
 * Dummy Texas Hold'em poker bot that always just checks or calls. <br />
 * <br />
 * 
 * This bot allowed for perfectly predictable behavior.
 * 
 * @author Oscar Stigter
 */
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
