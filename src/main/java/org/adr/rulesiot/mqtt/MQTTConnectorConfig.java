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

import java.util.Properties;

/**
 *
 * @author adrian
 */
public class MQTTConnectorConfig {

    public String url;
    public Properties sslproperties = null;
    public String clientid;

    public String username = null;
    public String password = null;

    public int timeout = 30000;
    public int keepalive = 60;
    public int maxinflight = 60;
    public Message lastwill = null;

    @Override
    public String toString() {
        return "MQTTConnectorConfig{" + "url=" + url + ", sslproperties=" + sslproperties + ", clientid=" + clientid + ", username=" + username + ", password=" + password + ", timeout=" + timeout + ", keepalive=" + keepalive + ", maxinflight=" + maxinflight + ", lastwill=" + lastwill + '}';
    }
}
