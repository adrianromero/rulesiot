/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.adr.rulesiot.mqtt.engine;

import java.util.function.BiFunction;
import org.adr.rulesiot.mqtt.Action;
import org.eclipse.paho.client.mqttv3.MqttTopic;

/**
 *
 * @author adrian
 */
public class ReducerAction<StateInfo> implements BiFunction<StateInfo, Action, EngineState<StateInfo>> {

    private String topicfilter;
    private BiFunction<StateInfo, Action, EngineState<StateInfo>> f;

    public ReducerAction(String topicfilter, BiFunction<StateInfo, Action, EngineState<StateInfo>> f) {
        this.topicfilter = topicfilter;
        this.f = f;
    }

    @Override
    public EngineState<StateInfo> apply(StateInfo info, Action action) {
        if (MqttTopic.isMatched(topicfilter, action.message.topic)) {
            return f.apply(info, action);
        } else {
            EngineState state = new EngineState();
            state.info = info;
            return state;
        }
    }
}
