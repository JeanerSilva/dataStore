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

import org.junit.runner.manipulation.Filter;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

//      https://coliconwg.appspot.com/listapos?entity=moto
//      http://localhost:8080/poslist?entity=moto
//      http://localhost:8080/poslist
@WebServlet(name = "poslist", urlPatterns = { "/poslist" })
public class PosList extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String entity = request.getParameter("entity") == null ? "unknowtracker" : request.getParameter("entity");
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		TrackerPos tracker = new TrackerPos();
		List<TrackerPos> trackerList = new ArrayList<>();
		
		Query q = new Query(entity);
		PreparedQuery pq = ds.prepare(q);
		for (Entity e : pq.asIterable()) {
			tracker = new TrackerPos();
			tracker.setPos (e.getProperty("pos")  == null ? "" : e.getProperty("pos").toString());
			tracker.setTime(e.getProperty("time") == null ? "" : e.getProperty("time").toString());
			trackerList.add(tracker);
		}
		Comparator<? super TrackerPos> comparator = new Comparator<TrackerPos> () {
			@Override
			public int compare(TrackerPos o1, TrackerPos o2) {				
				return o2.getTime().compareTo(o1.getTime());
			}};
		trackerList.sort(comparator);
		
		PrintWriter out = response.getWriter();
		out.write("<html><body>");
		out.write("<h2>Histórico</h2>");
		out.write("<p>" + entity + "</p>");		
		out.write("<table border=\"1\">");
		out.write("<tr><th>N.</th>");
		out.write("<th>Pos</th>");
		out.write("<th>Time</th></tr>");
		int x = 1;
		for (TrackerPos t:trackerList) {
			out.write("<tr><td>" + x + "</td>");
			out.write("<td>" + t.getPos() + "</td>");
			out.write("<td>" + t.getTime() + "</td></tr>");
		x++;
		};
		out.write("</table>");
		
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		out.write("</body></html>");

	}
}