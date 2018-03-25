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
		PreparedQuery pq = ds.prepare(q);
		
		PrintWriter out = response.getWriter();
		out.write("<html><body>");
		out.write("<h2>Posições</h2>");
		out.write("<p>Veículo: " + entity + "</p>");
		out.write("<table border=\"1\">");
		out.write("<tr><th>Posição</th>");
		out.write("<th>Time</th></tr>");
		for (Entity e : pq.asIterable()) {
			trackerPos.setPos(e.getProperty("pos").toString());
			trackerPos.setTime(e.getProperty("time").toString());
			out.write("<tr><td>" + trackerPos.getPos() + "</td>");
			out.write("<td>" + trackerPos.getTime() + "</td></tr>");
		};
		out.write("</table>");
		
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		out.write("</body></html>");

	}
}