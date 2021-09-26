//    HelloIoT is a dashboard creator for MQTT
//    Copyright (C) 2021 Adrián Romero Corchado.
//
//    This file is part of RulesIoT.
//
//    HelloIot is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    HelloIot is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with HelloIot.  If not, see <http://www.gnu.org/licenses/>.
//
package org.adr.rulesiot.engine;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adrian
 */
public class AppRuntime {

    private final static Logger LOGGER = Logger.getLogger(AppRuntime.class.getName());

    public <Action, Result, State> Result loop(AppEngine<Action, Result, State> engine, IOQueue<Action, Result> io, State initial) throws InterruptedException {

        LOGGER.info("Starting event loop...");

        State state = initial;
        Result result = null;

        LOGGER.log(Level.CONFIG, "Persist initial state: %s ", state);

        do {
            Action action = io.take(); // This blocks until new action available;
            LOGGER.log(Level.CONFIG, "Persist action: %s ", action);
            state = engine.reduce(state, action);
            LOGGER.log(Level.CONFIG, "Persist state: %s ", state);
            result = engine.template(state);
            LOGGER.log(Level.CONFIG, "Persist result: %s ", result);
            io.put(result);
        } while (!engine.isFinal(result));

        LOGGER.info("Exiting event loop...");
        return result;
    }
}
