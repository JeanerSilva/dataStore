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

//      https://coliconwg.appspot.com/publish?entity=moto&pos=1234
//      localhost:8080/publish?entity=moto&pos=1234
@WebServlet(name = "hello", urlPatterns = { "/hello" })
public class Main extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		String entity = "moto";
		Query q = new Query(entity);
		PreparedQuery pq = ds.prepare(q);
		
		PrintWriter out = response.getWriter();
		out.write("<html><body>");
		out.write("<h2>Posições</h2>");
		out.write("<p>Veículo: " + entity + "</p>");
		for (Entity e : pq.asIterable()) {
			String posEntity = e.getProperty("pos").toString();
			String timeEntity = e.getProperty("time").toString();
			out.write("<p>Pos: " + posEntity + "</p>");
			out.write("<p>Time: " + timeEntity + "</p>");
		};

		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");


		out.write("</body></html>");

	}
}