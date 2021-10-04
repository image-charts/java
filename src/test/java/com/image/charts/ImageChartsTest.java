package com.image.charts;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ImageChartsTest {

  @Test
  @DisplayName("toURL - works")
  void toUrlWorks() throws MalformedURLException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
    String url = new ImageCharts().cht("p").chd("t:1,2,3").toURL();
    assertEquals("https://image-charts.com:443/chart?cht=p&chd=t%3A1%2C2%2C3", url);
  }

  @Test
  @DisplayName("toURL - adds a signature when icac and secrets are defined")
  void toUrlAddSignature() throws MalformedURLException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
    String url = new ImageCharts("plop").cht("p")
      .chd("t:1,2,3")
      .chs("100x100")
      .icac("test_fixture")
      .toURL();
    assertEquals("https://image-charts.com:443/chart?cht=p&chd=t%3A1%2C2%2C3&chs=100x100&icac=test_fixture&ichm=71bd93758b49ed28fdabd23a0ff366fe7bf877296ea888b9aaf4ede7978bdc8d", url);
  }

  @Test
  @DisplayName("toURL - exposes parameters and use them")
  void toUrlExposesParametersUseThem() throws InvocationTargetException, IllegalAccessException, MalformedURLException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
    ImageCharts imageCharts = new ImageCharts();

    StringBuilder query = new StringBuilder();
    for (Method m : imageCharts.getClass().getMethods()) {
      if (m.getName().startsWith("c") || m.getName().startsWith("id")) {
        m.invoke(imageCharts, "plop");
        query.append("&" + m.getName() + "=plop");
      }
    }

    String url = imageCharts.toURL();
    // Use substring to delete first "&" character
    String assertQuery = "https://image-charts.com:443/chart?" + query.substring(1);

    assertEquals(assertQuery, url);
  }

  @Test
  @DisplayName("toBuffer - rejects if a chs is not defined")
  void toBufferRejectsIfChsNotDefined() {
    ImageChartsException exception = assertThrows(ImageChartsException.class, new Executable() {
      @Override
      public void execute() throws Throwable {
        new ImageCharts().cht("p").chd("t:1,2,3").toBuffer();
      }
    });

    String expectedMessage = "\"\\\"chs\\\" is required\"";

    assertEquals(expectedMessage, exception.getMessage());
  }

  @Test
  @DisplayName("toBuffer - rejects if a icac is defined without ichm")
  void toBufferRejectsIfIcacWithoutIchm() {
    ImageChartsException exception = assertThrows(ImageChartsException.class, new Executable() {
      @Override
      public void execute() throws Throwable {
        new ImageCharts().cht("p").chd("t:1,2,3").chs("100x100").icac("test_fixture").toBuffer();
      }
    });

    String expectedMessage = "\"The `icac` (ACCOUNT_ID) and `ichm` (HMAC-SHA256 request signature) query parameters must both be defined if specified. [Learn more](https://bit.ly/HMACENT)\"";

    assertEquals(expectedMessage, exception.getMessage());
  }

  @Test
  @DisplayName("toBuffer - rejects if timeout is reached")
  void toBufferRejectsIfTimeoutReached() {
    assertThrows(SocketTimeoutException.class, new Executable() {
      @Override
      public void execute() throws Throwable {
        new ImageCharts(null, null, null, null, null, 1)
          .cht("p").chd("t:1,2,3").chs("100x100").toBuffer();
      }
    });
  }

  @Test
  @DisplayName("toBuffer - works")
  void toBufferWorks() throws NoSuchAlgorithmException, InvalidKeyException, IOException {
    new ImageCharts()
      .cht("p").chd("t:1,2,3").chs("100x100").toBuffer();
  }

  @Test
  @DisplayName("toDataURI - rejects if a chs is not defined")
  void toDataURIRejectsIfChsNotDefined() {
    ImageChartsException exception = assertThrows(ImageChartsException.class, new Executable() {
      @Override
      public void execute() throws Throwable {
        new ImageCharts().cht("p").chd("t:1,2,3").toDataURI();
      }
    });

    String expectedMessage = "\"\\\"chs\\\" is required\"";

    assertEquals(expectedMessage, exception.getMessage());
  }

  @Test
  @DisplayName("toDataURI - works")
  void toDataURIWorks() throws NoSuchAlgorithmException, InvalidKeyException, IOException {
    String dataURI = new ImageCharts().cht("p").chd("t:1,2,3").chs("2x2").toDataURI();

    assertEquals("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAIAAAACAQAAAABazTCJAAAADElEQVR42mM4wHAAAAMEAYEq5W5a\nAAAAAElFTkSuQmCC", dataURI);
  }

  @Test
  @DisplayName("toDataURI - support gifs")
  void toDataURISupportGifs() throws NoSuchAlgorithmException, InvalidKeyException, IOException {
    String dataURI = new ImageCharts().cht("p").chd("t:1,2,3").chan("100").chs("2x2").toDataURI();

    assertEquals("data:image/gif;base64,iVBORw0K", dataURI.substring(0, 30));
  }

  @Test
  @DisplayName("toFile - rejects if there was an error")
  void toFileRejectsIfError() {
    ImageChartsException exception = assertThrows(ImageChartsException.class, new Executable() {
      @Override
      public void execute() throws Throwable {
        new ImageCharts().cht("p").chd("t:1,2,3").toFile("/tmp/chart.png");
      }
    });

    String expectedMessage = "\"\\\"chs\\\" is required\"";

    assertEquals(expectedMessage, exception.getMessage());
  }

  @Test
  @DisplayName("toFile - rejects when the path is invalid")
  void toFileRejectsWhenInvalidPath() {
    ImageChartsException exception = assertThrows(ImageChartsException.class, new Executable() {
      @Override
      public void execute() throws Throwable {
        new ImageCharts().cht("p").chd("t:1,2,3").toFile("/__invalid_path/chart.png");
      }
    });

    String expectedMessage = "\"\\\"chs\\\" is required\"";

    assertEquals(expectedMessage, exception.getMessage());
  }

  @Test
  @DisplayName("toFile - works")
  void toFileWorks() throws NoSuchAlgorithmException, InvalidKeyException, IOException {
    String filePath = "/app/.cache/plop.png";
    new ImageCharts().cht("p").chd("t:1,2,3").chan("100").chs("2x2").toFile(filePath);

    assertTrue(new File(filePath).exists());
  }

  @Test
  @DisplayName("protocol - expose the protocol")
  void protocolExposeProtocol() throws NoSuchFieldException, IllegalAccessException {
    ImageCharts builder = new ImageCharts(null, null, null, null, null, null);

    Field protocolField = builder.getClass().getDeclaredField("protocol");
    protocolField.setAccessible(true);
    assertEquals("https", protocolField.get(builder).toString());
  }

  @Test
  @DisplayName("protocol - let protocol to be user-defined")
  void protocolUserDefined() throws NoSuchFieldException, IllegalAccessException {
    ImageCharts builder = new ImageCharts("http", null, null, null, null, null);

    Field protocolField = builder.getClass().getDeclaredField("protocol");
    protocolField.setAccessible(true);
    assertEquals("http", protocolField.get(builder).toString());
  }

  @Test
  @DisplayName("host - expose the host")
  void hostExposeHost() throws NoSuchFieldException, IllegalAccessException {
    ImageCharts builder = new ImageCharts(null, null, null, null, null, null);

    Field protocolField = builder.getClass().getDeclaredField("host");
    protocolField.setAccessible(true);
    assertEquals("image-charts.com", protocolField.get(builder).toString());
  }

  @Test
  @DisplayName("host - let host to be user-defined")
  void hostUserDefined() throws NoSuchFieldException, IllegalAccessException {
    ImageCharts builder = new ImageCharts(null, "on-premise-image-charts.com", null, null, null, null);

    Field protocolField = builder.getClass().getDeclaredField("host");
    protocolField.setAccessible(true);
    assertEquals("on-premise-image-charts.com", protocolField.get(builder).toString());
  }

  @Test
  @DisplayName("pathname - expose the pathname")
  void pathnameExposePathname() throws NoSuchFieldException, IllegalAccessException {
    ImageCharts builder = new ImageCharts(null, null, null, null, null, null);

    Field protocolField = builder.getClass().getDeclaredField("pathname");
    protocolField.setAccessible(true);
    assertEquals("/chart", protocolField.get(builder).toString());
  }

  @Test
  @DisplayName("pathname - let pathname to be user-defined")
  void pathnameUserDefined() throws NoSuchFieldException, IllegalAccessException {
    ImageCharts builder = new ImageCharts(null, null, null, "/my-charts", null, null);

    Field protocolField = builder.getClass().getDeclaredField("pathname");
    protocolField.setAccessible(true);
    assertEquals("/my-charts", protocolField.get(builder).toString());
  }

  @Test
  @DisplayName("port - expose the port")
  void portExposePort() throws NoSuchFieldException, IllegalAccessException {
    ImageCharts builder = new ImageCharts(null, null, null, null, null, null);

    Field protocolField = builder.getClass().getDeclaredField("port");
    protocolField.setAccessible(true);
    assertEquals(443, protocolField.get(builder));
  }

  @Test
  @DisplayName("port - let port to be user-defined")
  void portUserDefined() throws NoSuchFieldException, IllegalAccessException {
    ImageCharts builder = new ImageCharts(null, null, 8080, null, null, null);

    Field protocolField = builder.getClass().getDeclaredField("port");
    protocolField.setAccessible(true);
    assertEquals(8080, protocolField.get(builder));
  }

  @Test
  @DisplayName("query - expose the query")
  void queryExposeQuery() throws NoSuchFieldException, IllegalAccessException {
    ImageCharts builder = new ImageCharts();

    Field protocolField = builder.getClass().getDeclaredField("query");
    protocolField.setAccessible(true);
    Map<String, Object> query = (Map<String, Object>) protocolField.get(builder);
    assertTrue(query.isEmpty());
  }

  @Test
  @DisplayName("query - let query to be user-defined")
  void queryUserDefined() throws NoSuchFieldException, IllegalAccessException {
    ImageCharts builder = new ImageCharts().cht("p").chd("t:1,2,3").icac("plop");

    Field protocolField = builder.getClass().getDeclaredField("query");
    protocolField.setAccessible(true);
    Map<String, Object> query = (Map<String, Object>) protocolField.get(builder);
    assertEquals("p", query.get("cht"));
    assertEquals("t:1,2,3", query.get("chd"));
    assertEquals("plop", query.get("icac"));
  }
}
