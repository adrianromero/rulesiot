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

import org.adr.rulesiot.mqtt.Action;
import org.adr.rulesiot.mqtt.MQTTEngine;
import org.adr.rulesiot.mqtt.Result;
import org.adr.rulesiot.mqtt.Message;

/**
 *
 * @author adrian
 */
public class MyAppEngine implements MQTTEngine<MyAppState> {

    public MyAppState reduce(MyAppState state, Action action) {

        System.out.println(action.message.topic);

        MyAppState newstate = new MyAppState();
        if (action.message.topic.equals("myhelloiot/final")) {
            newstate.value = 1;
        }

        return newstate;
    }

    public Result template(MyAppState state) {

        Result result = new Result();
        result.messages = new Message[]{};
        if (state.value == 1) {
            result.exit = true;
        }

        return result;
    }

    public boolean isFinal(Result result) {
        return result.exit;
    }
}
