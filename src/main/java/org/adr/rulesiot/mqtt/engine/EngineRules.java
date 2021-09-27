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
package org.adr.rulesiot.mqtt.engine;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.adr.rulesiot.engine.AppEngine;
import org.adr.rulesiot.mqtt.Action;
import org.adr.rulesiot.mqtt.Message;
import org.adr.rulesiot.mqtt.Result;

/**
 *
 * @author adrian
 */
public final class EngineRules<StateInfo> implements AppEngine<Action, Result, EngineState<StateInfo>> {

    private final static Logger LOGGER = Logger.getLogger(EngineRules.class.getName());

    private final List<BiFunction<StateInfo, Action, EngineState<StateInfo>>> reducers;

    public EngineRules(BiFunction<StateInfo, Action, EngineState<StateInfo>>... reducers) {
        this.reducers = Arrays.asList(reducers);

    }

    public EngineState<StateInfo> reduce(EngineState<StateInfo> state, Action action) {

        LOGGER.log(Level.CONFIG, "Reducing action %s", action);

        EngineState<StateInfo> newstate = new EngineState<>();
        newstate.info = state.info;

        this.reducers.forEach(f -> {
            EngineState<StateInfo> resultState = f.apply(newstate.info, action);

            newstate.info = resultState.info;

            newstate.messages.addAll(resultState.messages);
            newstate.exit = newstate.exit || resultState.exit;
        });

        return newstate;
    }

    public Result template(EngineState state) {

        Result result = new Result();
        List<Message> l = state.messages;
        result.messages = l.toArray(Message[]::new);
        result.exit = state.exit;
        return result;
    }

    public boolean isFinal(Result result) {
        return result.exit;
    }
}
