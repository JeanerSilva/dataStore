package br.com.conecta;

import java.io.IOException;
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

// https://coliconwg.appspot.com/localhost:8080/pospull?entity=moto
// http://localhost:8080/pospull?entity=moto
@WebServlet(name = "pospull", urlPatterns = { "/pospull" })
public class PosPull extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String entity = request.getParameter("entity") == null ? "unknowtracker" : request.getParameter("entity");
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		List<TrackerPos> trackerPosList = new ArrayList<>();

		Query q = new Query(entity);
		PreparedQuery pq = ds.prepare(q);
		for (Entity e : pq.asIterable()) {
			String posEntity = e.getProperty("pos").toString();
			String timeEntity = e.getProperty("time").toString();
			trackerPosList.add(new TrackerPos(posEntity, timeEntity));
		}

		Gson gson = new Gson();
		String listJson = gson.toJson(trackerPosList);
		
		response.getWriter().print(listJson);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		
	}
}