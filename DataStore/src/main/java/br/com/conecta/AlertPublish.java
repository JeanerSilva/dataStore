package br.com.conecta;


import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

//      https://coliconwg.appspot.com/alertpublish?pos=-15.1,-48.30&giro=true&mov=true&entity=alertmoto
//      http://localhost:8080/alertpublish?pos=-15.1,-48.30&giro=true&mov=true&entity=alertmoto
//		http://localhost:8080/alertpublish
//      http://localhost:8080/alertpublish?pos=-15.1,-48.30&mov=true&entity=alertmoto
@WebServlet(name = "alertpublish", urlPatterns = { "/alertpublish" })
public class AlertPublish extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String entity = request.getParameter("entity") == null ? "unknowalert" : request.getParameter("entity");
		LocalDateTime time = LocalDateTime.now();
				
		Alert alert = new Alert(request.getParameter("pos"),
								request.getParameter("giro"),
								request.getParameter("mov"),
								time.toString());
		
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Entity alertEntity = new Entity(entity);
		if (alert.getGiro() == null) alert.setGiro("vazio");
		if (alert.getPos() == null) alert.setPos("vazio");
		if (alert.getMov() == null) alert.setMov("vazio");
		if (alert.getTime() == null) alert.setTime("vazio");
		
		alertEntity.setProperty("pos", alert.getPos());
		alertEntity.setProperty("giro", alert.getGiro());
		alertEntity.setProperty("mov", alert.getMov());
		alertEntity.setProperty("time", alert.getTime());		
		ds.put(alertEntity);

		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.write("<html><body>");
		out.write("<h2>Novo alerta!</h2>");
		//cabeçalho
		out.write("<table>");
		out.write("<tr><th>Pos</th>");
			out.write("<th>Giro</th>");
			out.write("<th>Mov</th>");
			out.write("<th>Time</th></tr>");
		//corpo
		

		
		out.write("<tr><td>" + alert.getPos() + "</td>");
			out.write("<td>" + alert.getGiro() + "</td>");
			out.write("<td>" + alert.getMov() + "</td>");
			out.write("<td>" + alert.getTime() + "</td></tr>");
		
		out.write("</table>");
		out.write("</body></html>");

	}
}