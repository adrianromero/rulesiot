/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
