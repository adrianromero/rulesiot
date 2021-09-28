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
package org.adr.rulesiot;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.adr.rulesiot.engine.AppRuntime;
import org.adr.rulesiot.mqtt.MQTTConnector;
import org.adr.rulesiot.mqtt.MQTTConnectorConfig;
import org.adr.rulesiot.mqtt.Message;
import org.adr.rulesiot.mqtt.TopicInfo;
import org.adr.rulesiot.mqtt.engine.EngineReducer;
import org.adr.rulesiot.mqtt.engine.EngineRules;
import org.adr.rulesiot.mqtt.engine.EngineState;
import org.adr.rulesiot.mqtt.engine.EngineReducerAction;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 *
 * @author adrian
 */
public class Main {

    private final static Logger LOGGER = Logger.getLogger(Main.class.getName());

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        EngineState<MyAppStateInfo> initial = new EngineState<>();
        initial.info = new MyAppStateInfo();

        EngineReducer<MyAppStateInfo> reducer1
                = new EngineReducerAction<>("myhelloiot/exit", (info, action) -> {
                    EngineState<MyAppStateInfo> newstate = new EngineState<>();
                    newstate.info = info;
                    newstate.exit = "1".equals(new String(action.message.payload));
                    return newstate;
                });

        EngineReducer<MyAppStateInfo> reducer2
                = new EngineReducerAction<>("myhelloiot/alarm", (info, action) -> {
                    EngineState<MyAppStateInfo> newstate = new EngineState<>();
                    newstate.info = info;

                    newstate.messages.add(new Message("myhelloiot/modal", "0"));

                    if ("1234".equals(new String(action.message.payload))) {
                        newstate.messages.add(new Message("myhelloiot/disconnect", "1234"));
                    }

                    return newstate;
                });

        EngineRules<MyAppStateInfo> engine = new EngineRules<>(reducer1, reducer2);

        MQTTConnectorConfig config = new MQTTConnectorConfig();
        config.url = "tcp://localhost:1883";
        config.clientid = "rulesiot_2341237";

        try {
            MQTTConnector c = new MQTTConnector(config, new TopicInfo[]{new TopicInfo("myhelloiot/#", 0)});
            c.connect();

            AppRuntime runtime = new AppRuntime();
            runtime.loop(engine, c.createIOQueue(), initial);

            c.disconnect();

        } catch (InterruptedException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        } catch (MqttException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
}
