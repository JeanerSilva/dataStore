package br.com.conecta;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

//@Produces("application/json")
@WebServlet(
    name = "HelloAppEngine",
    urlPatterns = {"/hello"}
)
public class HelloAppEngine extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) 
      throws IOException {
     
	DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
	
	
	//create a new entity each time
	/*
	Entity e = new Entity("traker");
	e.setProperty("pos", "1");
	e.setProperty("time", "23-03-2018 14:45");
	ds.put(e);
	*/

	//lista de entidades
	/*
	Entity e1 = new Entity("entidade1");
	Entity e2 = new Entity("entidade2");
	Entity e3 = new Entity("entidade3");
	List<Entity> lista = new ArrayList<>();
	lista.add(e1);lista.add(e2);lista.add(e3);
	*/
	
	//key para alterar uma entidade existente
	/*
	Entity userkey = new Entity("user");
	Key key = KeyFactory.createKey("user", "user_id");
	Entity address = new Entity("Address", user.getKey());
	Key k = new KeyFactory.Builder("user", "greatgrandpa")
			.addChild("user", "grandpa")
			.addChild("user", "dad")
			.getKey();
	*/

	//create a unique entity each time
	Entity trackerPos = new Entity("trackerPos", 1);
	trackerPos.setProperty("position", "1");
	LocalDateTime agora = LocalDateTime.now();
	trackerPos.setProperty("time", agora.toString());
	ds.put(trackerPos);
	

	Query q = new Query("trackerPos");
	PreparedQuery pq = ds.prepare(q);
	for (Entity e : pq.asIterable()) {
		String pos = e.getProperty("position").toString();
		String time = e.getProperty("time").toString();
		response.getWriter().print("{\"position\":\"" + pos + "\"," + "\"time\":\"" + time + "\"}") ;
	}
	
	
   //response.setContentType("application/json");
	response.setContentType("text/plain");
    response.setCharacterEncoding("UTF-8");

    /*
   try {
		response.getWriter().print(ds.get(trackerPos.getKey()));		
	} catch (EntityNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	*/

  }
}