package br.com.conecta;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
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
//      localhost:8080/listapos?entity=moto
@WebServlet(name = "listapos", urlPatterns = { "/listapos" })
public class ListaPos extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		String entity = request.getParameter("entity") == null ? "unknow" : request.getParameter("entity");
		TrackerPos trackerPos = new TrackerPos();
		
		Query q = new Query(entity);
				//.addSort("time", Query.SortDirection.DESCENDING);
		PreparedQuery pq = ds.prepare(q);
		
		PrintWriter out = response.getWriter();
		out.write("<html><body>");
		out.write("<h2>Posições</h2>");
		out.write("<p>Veículo: " + entity + "</p>");
		out.write("<table border=\"1\">");
		out.write("<tr><th>N.</th>");
		out.write("<th>Posição</th>");
		out.write("<th>Time</th></tr>");
		int x = 1;
		for (Entity e : pq.asIterable()) {
			trackerPos.setPos(e.getProperty("pos").toString());
			String positions [] = trackerPos.getPos().split(",");
			String linkPos = getFormattedLocationInDegree(Double.parseDouble(positions[0]), Double.parseDouble(positions[1]));
			
			trackerPos.setTime(e.getProperty("time").toString());
			out.write("<tr><td>" + x + "</td>");
			out.write("<td><a href=https://www.google.com.br/maps/place/" + linkPos + ">"+ linkPos + "</a></td>");
			out.write("<td>" + trackerPos.getTime() + "</td></tr>");
			x++;
		};
		out.write("</table>");
		
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		out.write("</body></html>");

	}
	
	public static String getFormattedLocationInDegree(double latitude, double longitude) {
		try {
			int latSeconds = (int) Math.round(latitude * 3600);
			int latDegrees = latSeconds / 3600;
			latSeconds = Math.abs(latSeconds % 3600);
			int latMinutes = latSeconds / 60;
			latSeconds %= 60;

			int longSeconds = (int) Math.round(longitude * 3600);
			int longDegrees = longSeconds / 3600;
			longSeconds = Math.abs(longSeconds % 3600);
			int longMinutes = longSeconds / 60;
			longSeconds %= 60;
			String latDegree = latDegrees >= 0 ? "N" : "S";
			String lonDegrees = longDegrees >= 0 ? "E" : "W";

			return Math.abs(latDegrees) + "°" + latMinutes + "'" + latSeconds + "\"" + latDegree 
					+ Math.abs(longDegrees) + "°" + longMinutes + "'" + longSeconds + "\"" + lonDegrees;
		} catch (Exception e) {
			return "" + String.format("%8.5f", latitude) + String.format("%8.5f", longitude);
		}
	}
	
}