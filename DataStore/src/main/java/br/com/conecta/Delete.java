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

//      https://coliconwg.appspot.com/publish?entity=moto&pos=1234
//      localhost:8080/publish?entity=moto&pos=1234
@WebServlet(name = "delete", urlPatterns = { "/delete" })
public class Delete extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		String entity = request.getParameter("entity") == null ? "moto" : request.getParameter("entity");
		Entity e = new Entity(entity);
		Query q = new Query(entity);
		PreparedQuery pq = ds.prepare(q);
		
		PrintWriter out = response.getWriter();
		out.write("<html><body>");
		out.write("<h2>Posições</h2>");
		out.write("<p>Veículo: " + entity + "</p>");
		for (Entity efor : pq.asIterable()) {
			String posEntity = efor.getProperty("pos").toString();
			String timeEntity = efor.getProperty("time").toString();
			out.write("<p>Pos: " + posEntity + "</p>");
			out.write("<p>Time: " + timeEntity + "</p>");
			ds.delete(efor.getKey());
		};
		out.write("<br /><br /><p>Entidade " + entity + " deletada.</p>");
		
		//Key key = KeyFactory.createKey(entity);
		//ds.delete(e.getKey());
		
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");


		out.write("</body></html>");

	}
}