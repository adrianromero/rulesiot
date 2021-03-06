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
package org.adr.rulesiot.mqtt;

import java.util.Arrays;

/**
 *
 * @author adrian
 */
public class Message {

    public String topic;

    public byte[] payload = new byte[0];
    public int qos = 0;
    public boolean retained = false;

    public Message() {
    }

    public Message(String topic, byte[] payload) {
        this.topic = topic;
        this.payload = payload;
    }

    public Message(String topic, String payload) {
        this.topic = topic;
        this.payload = payload.getBytes();
    }

    @Override
    public String toString() {
        return "Message{" + "topic=" + topic + ", payload=" + Arrays.toString(payload) + ", qos=" + qos + ", retained=" + retained + '}';
    }
}
