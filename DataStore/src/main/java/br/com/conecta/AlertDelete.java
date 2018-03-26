package br.com.conecta;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

//      https://coliconwg.appspot.com/alertsdelete?verbose=false&entity=alertmoto
//      http://localhost:8080/alertsdelete?verbose=false&entity=alertmoto
@WebServlet(name = "alertsdelete", urlPatterns = { "/alertsdelete" })
public class AlertDelete extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String verbose = request.getParameter("verbose") == null ? "unknowalert" : request.getParameter("verbose");
		String entity = request.getParameter("entity") == null ? "unknowalert" : request.getParameter("entity");
		
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Alert alert = new Alert();
		Query q = new Query(entity);
		PreparedQuery pq = ds.prepare(q);

		PrintWriter out = response.getWriter();
		out.write("<html><body>");
		out.write("<h2>Histórico</h2>");
		out.write("<table border=\"1\">");
		out.write("<tr><th>Pos</th>");
		out.write("<th>Giro</th>");
		out.write("<th>Mov</th>");
		out.write("<th>Time</th></tr>");
		for (Entity e : pq.asIterable()) {
			if (!verbose.equals("false")) {
				alert.setPos(e.getProperty("pos").toString() == null ? "" : e.getProperty("pos").toString());
				alert.setGiro(e.getProperty("giro").toString() == null ? "" : e.getProperty("giro").toString());
				alert.setMov(e.getProperty("mov").toString() == null ? "" : e.getProperty("mov").toString());
				alert.setTime(e.getProperty("time").toString() == null ? "" : e.getProperty("time").toString());

				out.write("<tr><td>" + alert.getPos() + "</td>");
				out.write("<td>" + alert.getGiro() + "</td>");
				out.write("<td>" + alert.getMov() + "</td>");
				out.write("<td>" + alert.getTime() + "</td></tr>");
			}
			ds.delete(e.getKey());
		}
		;
		out.write("<br /><br /><p>Alertas deletados.</p>");

		// Key key = KeyFactory.createKey(entity);
		// ds.delete(e.getKey());

		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");

		out.write("</body></html>");

	}
}