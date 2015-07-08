/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2014 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.undertow.Undertow;
import io.undertow.examples.UndertowExample;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

@UndertowExample("Hello World")
public class query1 {

	static Cipher decodeReq = new Cipher(); 

	public static void main(final String[] args) {
		Undertow server = Undertow.builder()
				.addHttpListener(8080, "localhost")
				.setHandler(new HttpHandler() {
					@Override
					public void handleRequest(final HttpServerExchange exchange) throws Exception {
						String request = exchange.getQueryString();
						if(request.length() != 0)
						{
							String res = decodeReq.parseHttpReq(request);
							Date date= Calendar.getInstance().getTime();
							Timestamp  ts = new Timestamp(date.getTime());
							String time = ts.toString();
							SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							Date currentDate;
							currentDate = date_format.parse(time);
							String time_val = date_format.format(currentDate);
							
							exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
							exchange.getResponseSender().send("TeamRockStars,327811142774,617128749441,910842319737\n" + time_val + "\n"+ res);
						}
					}
				}).build();
		server.start();
	}
}