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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.adr.rulesiot.engine.IOQueue;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 *
 * @author adrian
 */
public class MQTTConnector implements MqttCallback, IOQueue<Action, Result> {

    // MQTT
    private MqttClient mqttClient = null;
    private BlockingQueue<Action> messagesqueue;

    @Override
    public void connectionLost(Throwable arg0) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttmessage) throws Exception {
        Message message = new Message();
        message.topic = topic;
        message.payload = mqttmessage.getPayload();
        message.qos = mqttmessage.getQos();
        message.retained = mqttmessage.isRetained();
        Action action = new Action();
        action.message = message;
        messagesqueue.add(action);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken arg0) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Action take() throws InterruptedException {
        return messagesqueue.take();
    }

    @Override
    public void put(Result result) throws InterruptedException {
        try {
            for (int i = 0; i < result.messages.length; i++) {
                Message msg = result.messages[i];
                MqttMessage mm = new MqttMessage(msg.payload);
                mm.setQos(msg.qos);
                mm.setRetained(msg.retained);
                mqttClient.publish(msg.topic, mm);
            }

        } catch (MqttException ex) {
            // TODO: Review in case of paho exception too much publications              
        }
    }

    public void connect(MQTTConnectorConfig config) throws MqttException {

        String[] listtopics = new String[]{"myhelloiot/#"};
        int[] listqos = new int[]{0};

        messagesqueue = new LinkedBlockingQueue<Action>();
        mqttClient = new MqttClient(config.url, config.clientid, new MemoryPersistence());
        MqttConnectOptions options = new MqttConnectOptions();
        if (config.username != null && !config.username.isEmpty()) {
            options.setUserName(config.username);
            options.setPassword(config.password.toCharArray());
        }
        options.setConnectionTimeout(config.timeout);
        options.setKeepAliveInterval(config.keepalive);
        options.setCleanSession(true);
        options.setAutomaticReconnect(true);
        options.setMaxInflight(config.maxinflight);
        options.setSSLProperties(config.sslproperties);
        if (config.lastwill != null) {
            options.setWill(config.lastwill.topic, config.lastwill.payload, config.lastwill.qos, config.lastwill.retained);
        }
        mqttClient.connect(options);
        mqttClient.setCallback(this);
        if (listtopics.length > 0) {
            mqttClient.subscribe(listtopics, listqos);
        }
    }

    public void disconnect() throws MqttException {
        // To be invoked by executor thread
        if (mqttClient.isConnected()) {
            mqttClient.setCallback(null);
            String[] listtopics = new String[]{"myhelloiot/#"};
            mqttClient.unsubscribe(listtopics);
            mqttClient.disconnect();
            mqttClient.close();
        }
        mqttClient = null;
        messagesqueue = null;
    }
}