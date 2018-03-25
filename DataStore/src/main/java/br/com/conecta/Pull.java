package br.com.conecta;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.repackaged.com.google.gson.Gson;

//@Produces("application/json")
@WebServlet(name = "pull", urlPatterns = { "/pull" })
public class Pull extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String entity = request.getParameter("entity") == null ? "trackerPos" : request.getParameter("entity");

		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		TrackerPosList trackerPosList = new TrackerPosList();

		Query q = new Query(entity);
		PreparedQuery pq = ds.prepare(q);
		for (Entity e : pq.asIterable()) {
			String pos = e.getProperty("pos").toString();
			String time = e.getProperty("time").toString();
			trackerPosList.getTrackerPosList().add(new TrackerPos(pos, time));
		}

		Gson gson = new Gson();
		String listJson = gson.toJson(trackerPosList);
		
		response.getWriter().print(listJson);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		
	}
}