package br.com.conecta;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.gson.Gson;

//      https://coliconwg.appspot.com/config?entity=motoconfig&giro=on&gps=on

//https://coliconwg.appspot.com/config?action=publish&entity=motoconfiggiro&status=on
//	https://coliconwg.appspot.com/config?action=publish&entity=motoconfiggiro&status=off
//	https://coliconwg.appspot.com/config?action=publish&entity=motoconfiggps&status=on
//https://coliconwg.appspot.com/config?action=publish&entity=motoconfiggps&status=off

//	https://coliconwg.appspot.com/config?action=pull&entity=motoconfiggps
//	https://coliconwg.appspot.com/config?action=pull&entity=motoconfiggiro

//	https://coliconwg.appspot.com/config?action=list&entity=motoconfiggps
//	https://coliconwg.appspot.com/config?action=list&entity=motoconfiggiro

//      http://localhost:8080/config?action=publish&entity=motoconfiggiro&status=on
// 		http://localhost:8080/config?action=publish&entity=motoconfiggiro&status=off
//		http://localhost:8080/config?action=publish&entity=motoconfiggps&status=on
//      http://localhost:8080/config?action=publish&entity=motoconfiggps&status=off

//		http://localhost:8080/config?action=pull&entity=motoconfiggps
//		http://localhost:8080/config?action=pull&entity=motoconfiggiro

//		http://localhost:8080/config?action=list&entity=motoconfiggps
//		http://localhost:8080/config?action=list&entity=motoconfiggiro

@WebServlet(name = "config", urlPatterns = { "/config" })
public class Config extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String status = request.getParameter("status");
		String entity = request.getParameter("entity");
		String action = request.getParameter("action");
		int id = 1;
		String result = "";
		LocalDateTime time = LocalDateTime.now();
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		if (entity != null) {

			Entity configEntity = new Entity(entity, id);
			List<String> getConfig = new ArrayList<>();

			if (action != null) {
				if (action.equals("publish")) {
					
					if (status != null) {
					configEntity.setProperty("status", status);
					configEntity.setProperty("time", time.toString());
					ds.put(configEntity);
					result = "<p> O status da entity '" + entity + "' foi setado para '" + status
							+ "' em " + time + "</p>";
					} else {
						result = "<p> property null </p>";
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
					String getstatus = "zero";
					String getTime = "zero";

					getstatus = getConfigEntity.getProperty("status") == null ? "nullo"
							: getConfigEntity.getProperty("status").toString();
					getTime = getConfigEntity.getProperty("time") == null ? "nullo"
							: getConfigEntity.getProperty("time").toString();
					getConfig.add(getstatus);
					getConfig.add(getTime);

					if (action.equals("pull")) {
						Gson gson = new Gson();
						String configJson = gson.toJson(getConfig);
						response.setContentType("application/json");
						response.setCharacterEncoding("UTF-8");
						response.getWriter().print(configJson);
					}

					if (action.equals("list")) {
						result = "<p> O status da entity '" + entity + "' é '" + getstatus
								+ "' e foi setado em " + getTime + "</p>";
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