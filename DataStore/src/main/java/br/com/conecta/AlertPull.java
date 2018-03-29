package br.com.conecta;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
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

//      https://coliconwg.appspot.com/alertpull?entity=motoalert
//      http://localhost:8080/alertpull?entity=motoalert
@WebServlet(name = "alertpull", urlPatterns = { "/alertpull" })
public class AlertPull extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String entity = request.getParameter("entity") == null ? "unknowalert" : request.getParameter("entity");
		Alert alert = new Alert();
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		List<Alert> alertList = new ArrayList<>();

		Query q = new Query(entity);

		PreparedQuery pq = ds.prepare(q);
		for (Entity e : pq.asIterable()) {
			alert = new Alert();
			alert.setPos(e.getProperty("pos") == null ? "" : e.getProperty("pos").toString());
			alert.setFont(e.getProperty("font") == null ? "" : e.getProperty("font").toString());
			alert.setTime(e.getProperty("time") == null ? "" : e.getProperty("time").toString());
			alertList.add(alert);
		}
		
		Comparator<? super Alert> comparator = new Comparator<Alert> () {
			@Override
			public int compare(Alert o1, Alert o2) {				
				return o2.getTime().compareTo(o1.getTime());
			}};
		alertList.sort(comparator);

		Gson gson = new Gson();
		String listJson = gson.toJson(alertList);
		
		response.getWriter().print(listJson);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
	}
}