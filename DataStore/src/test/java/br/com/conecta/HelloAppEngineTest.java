package br.com.conecta;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class HelloAppEngineTest {

  @Test
  public void test() throws IOException {
    MockHttpServletResponse response = new MockHttpServletResponse();
    new PosList().doGet(null, response);
    Assert.assertEquals("text/html", response.getContentType());
    Assert.assertEquals("UTF-8", response.getCharacterEncoding());    
  }
}
