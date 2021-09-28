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

import org.adr.rulesiot.mqtt.Action;
import org.eclipse.paho.client.mqttv3.MqttTopic;

/**
 *
 * @author adrian
 */
public class EngineReducerAction<StateInfo> implements EngineReducer<StateInfo> {

    private String topicfilter;
    private EngineReducer<StateInfo> f;

    public EngineReducerAction(String topicfilter, EngineReducer<StateInfo> f) {
        this.topicfilter = topicfilter;
        this.f = f;
    }

    @Override
    public EngineState<StateInfo> apply(StateInfo info, Action action) {
        if (MqttTopic.isMatched(topicfilter, action.message.topic)) {
            return f.apply(info, action);
        } else {
            EngineState<StateInfo> state = new EngineState<>();
            state.info = info;
            return state;
        }
    }
}
