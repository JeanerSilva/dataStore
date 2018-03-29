package br.com.conecta;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gson.Gson;

//      https://coliconwg.appspot.com/config?entity=motoconfig&giro=on&gps=on

//	https://coliconwg.appspot.com/config?action=publish&entity=motoconfig&status=on&gpstime=1000&gpsdist=1&timertransmit=2000
//	https://coliconwg.appspot.com/config?action=publish&entity=motoconfig&status=off&gpstime=1000&gpsdist=1
//	https://coliconwg.appspot.com/config?action=publish&entity=motoconfig&status=on&gpstime=1000&gpsdist=1
//	https://coliconwg.appspot.com/config?action=publish&entity=motoconfig&status=off&gpstime=1000&gpsdist=1
//	https://coliconwg.appspot.com/config?action=pull&entity=motoconfig
//	https://coliconwg.appspot.com/config?action=pull&entity=motoconfig
//	https://coliconwg.appspot.com/config?action=list&entity=motoconfig
//	https://coliconwg.appspot.com/config?action=list&entity=motoconfig

//		http://localhost:8080/config?action=publish&entity=motoconfig&status=on&gpstime=2000&gpsdist=2&girosense=2.0f&timertransmit=2000
//		http://localhost:8080/config?action=pull&entity=motoconfig
//		http://localhost:8080/config?action=list&entity=motoconfig

@WebServlet(name = "config", urlPatterns = { "/config" })
public class ConfigBean extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Config config = new Config();
		
		config.setGiroStatus(request.getParameter("girostatus") == null ? "on" :request.getParameter("girostatus").toString());
		config.setGpsStatus(request.getParameter("gpsstatus") == null ? "on" :request.getParameter("gpsstatus").toString());
		config.setGpsTime(request.getParameter("gpstime") == null ? "1000" : request.getParameter("gpstime").toString());
		config.setGpsDist(request.getParameter("gpsdist") == null ? "1" : request.getParameter("gpsdist").toString());
		config.setGiroSense(request.getParameter("girosense") == null ? "3.0f" : request.getParameter("girosense").toString());
		config.setTimerTransmit(request.getParameter("timertransmit") == null ? "3000" : request.getParameter("timertransmit").toString());
		System.out.println(config);
		String entity = request.getParameter("entity").toString();
		String action = request.getParameter("action").toString();
		int id = 1;
		String result = "";
		LocalDateTime time = LocalDateTime.now();
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		
		
		if (entity != null) {

			Entity configEntity = new Entity(entity, id);
			List<String> getConfig = new ArrayList<>();

			if (action != null) {
				if (action.equals("publish")) {					
					if (config.getGiroStatus() != null && config.getGpsStatus() != null) {
					configEntity.setProperty("girostatus", config.getGiroStatus());
					configEntity.setProperty("gpsstatus", config.getGpsStatus());
					configEntity.setProperty("gpstime", config.getGpsTime());
					configEntity.setProperty("gpsdist", config.getGpsDist());
					configEntity.setProperty("girosense", config.getGiroSense());
					configEntity.setProperty("time", time.toString());
					configEntity.setProperty("timertransmit", config.getTimerTransmit());
					ds.put(configEntity);
					result = "<p> A entity '" + entity + "' teve o gpsstatus setado para '" 
							+ config.getGpsStatus() + " o girostatos setado para " + config.getGiroStatus()
							+ "' em " + time + ", com os parâmetros gpstime = " +config.getGpsTime()
							+ " e gpsdist = " + config.getGpsDist() + " girosense = " + config.getGiroSense() 
							+ " timertransmit = " + config.getTimerTransmit() + "</p>";
					} else {
						result = "<p> Status null </p>";
					}
					
					responseHtml(response, result);
				}

				if (action.equals("pull") || action.equals("list")) {
					Key key = KeyFactory.createKey(entity, id);
					//System.out.println("key: " + key);
					Entity getConfigEntity = null;
					try {
						getConfigEntity = ds.get(key);
						//System.out.println(getConfigEntity);
					} catch (EntityNotFoundException e1) {
						result = "<p>" + e1.getMessage() + "</p>";
						responseHtml(response, result);
						e1.printStackTrace();
					}

					String getTime = "zero";

					config.setGpsStatus(getConfigEntity.getProperty("gpsstatus") == null ? "nullo"
							: getConfigEntity.getProperty("gpsstatus").toString());
					config.setGiroStatus(getConfigEntity.getProperty("girostatus") == null ? "nullo"
							: getConfigEntity.getProperty("girostatus").toString());
					config.setTime(getConfigEntity.getProperty("time") == null ? "nullo"
							: getConfigEntity.getProperty("time").toString());
					config.setGpsTime(getConfigEntity.getProperty("gpstime") == null ? "nullo"
							: getConfigEntity.getProperty("gpstime").toString());
					config.setGpsDist(getConfigEntity.getProperty("gpsdist") == null ? "nullo"
							: getConfigEntity.getProperty("gpsdist").toString());
					config.setGiroSense(getConfigEntity.getProperty("girosense") == null ? "nullo"
							: getConfigEntity.getProperty("girosense").toString());
					config.setTimerTransmit(getConfigEntity.getProperty("timertransmit") == null ? "nullo"
							: getConfigEntity.getProperty("timertransmit").toString());

					if (action.equals("pull")) {
						Gson gson = new Gson();
						String configJson = gson.toJson(config);
						response.setContentType("application/json");
						response.setCharacterEncoding("UTF-8");
						response.getWriter().print(configJson);
					}

					if (action.equals("list")) {
						result = "<p> A entity '" + entity + "' teve o gpsstatus setado para '" 
								+ config.getGpsStatus() + " o girostatos setado para " + config.getGiroStatus()
								+ "' em " + time + ", com os parâmetros time = " +config.getGpsTime()
								+ " e dist = " + config.getGpsDist() + " girosense = " + config.getGiroSense() 
								+ " timertransmit = " + config.getTimerTransmit() + "</p>";
						responseHtml(response, result);
					}
				}

			} else {
				result = "<p> Action null </p>";
				responseHtml(response, result);
			}
		} else {
			result = "<p> Entity null </p>";
			responseHtml(response, result);
		}

	}

	public void responseHtml(HttpServletResponse response, String result) throws IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.write("<html><body>");
		out.write("<h2>Config</h2>");
		out.write(result);
		out.write("</body></html>");
	}

}