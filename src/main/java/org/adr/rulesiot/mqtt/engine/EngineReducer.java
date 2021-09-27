/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.adr.rulesiot.mqtt.engine;

import java.util.function.BiFunction;
import org.adr.rulesiot.mqtt.Action;

/**
 *
 * @author adrian
 */
public interface EngineReducer<StateInfo> extends BiFunction<StateInfo, Action, EngineState<StateInfo>> {

}
