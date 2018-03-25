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

//      https://coliconwg.appspot.com/listaalertas?entity=moto
//      localhost:8080/listaalertas?entity=moto
@WebServlet(name = "listaalertas", urlPatterns = { "/listaalertas" })
public class ListaAlerta extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		String entity = request.getParameter("entity") == null ? "alert" : request.getParameter("entity");
		Alert alert = new Alert();
		
		Query q = new Query(entity);
		PreparedQuery pq = ds.prepare(q);
		
		PrintWriter out = response.getWriter();
		out.write("<html><body>");
		out.write("<h2>Histórico</h2>");
		out.write("<p>Veículo: " + entity + "</p>");
		
		out.write("<table border=\"1\">");
		out.write("<tr><th>Pos</th>");
		out.write("<th>Giro</th>");
		out.write("<th>Mov</th>");
		out.write("<th>Data</th>");
		out.write("<th>Time</th></tr>");
		for (Entity e : pq.asIterable()) {
			alert.setPos(e.getProperty("pos").toString() == null ? "" : e.getProperty("pos").toString());
			alert.setGiro(e.getProperty("giro").toString() == null ? "" : e.getProperty("giro").toString());
			alert.setMov(e.getProperty("mov").toString() == null ? "" : e.getProperty("mov").toString());
			alert.setTime(e.getProperty("time").toString() == null ? "" : e.getProperty("time").toString());
			alert.setData(e.getProperty("data").toString() == null ? "" : e.getProperty("data").toString());
			
			out.write("<tr><td>" + alert.getPos() + "</td>");
			out.write("<td>" + alert.getGiro() + "</td>");
			out.write("<td>" + alert.getMov() + "</td>");
			out.write("<td>" + alert.getData() + "</td>");
			out.write("<td>" + alert.getTime() + "</td></tr>");
		};
		out.write("</table>");
		
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		out.write("</body></html>");

	}
}