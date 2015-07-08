import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

public class Query1 {

	static Cipher decodeReq = new Cipher(); 

	public static void main(final String[] args) {
		
		// When running on local machine, use values: (8080, "localhost")
		// When running on ec2 instance, use values: (80, ec2-public-dns)
		Undertow server = Undertow.builder()
				.addHttpListener(80, "ec2-54-173-221-79.compute-1.amazonaws.com")
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
							exchange.getResponseSender().send("TeamRockStars,327811142774,617128749441,910842319737\n" + time_val + "\n"+ res + "\n");
						}
					}
				}).build();
		server.start();
	}
}