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
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.gson.Gson;

//      https://coliconwg.appspot.com/alertpull
//      localhost:8080/alertpull
@WebServlet(name = "alertpull", urlPatterns = { "/alertpull" })
public class AlertPull extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String entity = "alert";
		Alert alert = new Alert();
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		List<Alert> alertList = new ArrayList<>();

		Query q = new Query(entity)
				.addSort("data", Query.SortDirection.DESCENDING)
				.addSort("time", Query.SortDirection.DESCENDING);
		PreparedQuery pq = ds.prepare(q);
		for (Entity e : pq.asIterable()) {
			alert.setPos(e.getProperty("pos").toString());
			alert.setMov(e.getProperty("mov").toString());
			alert.setGiro(e.getProperty("giro").toString());
			alert.setData(e.getProperty("data").toString());
			alert.setTime(e.getProperty("time").toString());
			alertList.add(alert);
		}

		Gson gson = new Gson();
		String listJson = gson.toJson(alertList);
		
		response.getWriter().print(listJson);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
	}
}