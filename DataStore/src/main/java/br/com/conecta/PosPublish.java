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

//      https://coliconwg.appspot.com/pospublish?entity=moto&pos=1.2,3.4
//      http://localhost:8080/pospublish?entity=moto&pos=1.2,3.4
@WebServlet(name = "pospublish", urlPatterns = { "/pospublish" })
public class PosPublish extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String pos = request.getParameter("pos") == null ? "1.1,1.1" : request.getParameter("pos") ;
		String entity = request.getParameter("entity") == null ? "unknowtracker" : request.getParameter("entity");

		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Entity trackerPos = new Entity(entity);
		trackerPos.setProperty("pos", pos);
		LocalDateTime time = LocalDateTime.now();
		trackerPos.setProperty("time", time.toString());
		ds.put(trackerPos);

		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.write("<html><body>");
		out.write("<h2>Nova posição incluída</h2>");
		out.write("<p>Veículo: " + entity + "</p>");
		out.write("<p>Pos: " + pos + "</p>");
		out.write("<p>Time: " + time + "</p>");
		out.write("</body></html>");

	}
}