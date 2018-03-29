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

//      https://coliconwg.appspot.com/alertslist?entity=motoalert
//      http://localhost:8080/alertslist?&entity=motoalert
//		http://localhost:8080/alertslist
@WebServlet(name = "alertslist", urlPatterns = { "/alertslist" })
public class AlertList extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String entity = request.getParameter("entity") == null ? "unknowalert" : request.getParameter("entity");
		System.out.println("entity: " + entity);
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Alert alert = new Alert();
		List<Alert> alertsList = new ArrayList<>();
		
		
		Query q = new Query(entity);

		PreparedQuery pq = ds.prepare(q);
		for (Entity e : pq.asIterable()) {
			alert = new Alert();
			alert.setPos (e.getProperty("pos") == null ? "" : e.getProperty("pos").toString());
			alert.setFont(e.getProperty("font") == null ? "" : e.getProperty("font").toString());
			alert.setTime(e.getProperty("time") == null ? "" : e.getProperty("time").toString());
			alertsList.add(alert);
		}
		Comparator<? super Alert> comparator = new Comparator<Alert> () {
			@Override
			public int compare(Alert o1, Alert o2) {				
				return o2.getTime().compareTo(o1.getTime());
			}};
		alertsList.sort(comparator);
		
		PrintWriter out = response.getWriter();
		out.write("<html><body>");
		out.write("<h2>Histórico</h2>");
		out.write("<p>" + entity + "</p>");		
		out.write("<table border=\"1\">");
		out.write("<tr><th>N.</th>");
		out.write("<th>Pos</th>");
		out.write("<th>Font</th>");
		out.write("<th>Time</th></tr>");
		System.out.println(alertsList);
		int x = 1;
		for (Alert a:alertsList) {
			out.write("<tr><td>" + x + "</td>");
			out.write("<td>" + a.getPos() + "</td>");
			out.write("<td>" + a.getFont() + "</td>");
			out.write("<td>" + a.getTime() + "</td></tr>");
		x++;
		};
		out.write("</table>");
		
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		out.write("</body></html>");

	}
}