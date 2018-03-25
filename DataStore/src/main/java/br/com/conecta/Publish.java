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

//      https://coliconwg.appspot.com/publish?entity=moto&pos=1234
//      localhost:8080/publish?entity=moto&pos=1234
@WebServlet(name = "publish", urlPatterns = { "/publish" })
public class Publish extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String pos = request.getParameter("pos") == null ? "0" : request.getParameter("pos");
		String entity = request.getParameter("entity") == null ? "unknow" : request.getParameter("entity");

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