package demo;

import com.military.oauth2.server.AuthserverApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = AuthserverApplication.class)
//@WebAppConfiguration
//@IntegrationTest("server.port:0")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AuthserverApplication.class,
		// Normally spring.cloud.config.enabled:true is the default but since we have the
		// config
		// server on the classpath we need to set it explicitly
		  webEnvironment = RANDOM_PORT)
public class ApplicationTests {

	@Value("${local.server.port}")
	private int port;

	@Autowired
	private TestRestTemplate template ;

	@Test
	public void homePageProtected() {
		ResponseEntity<String> response = template.getForEntity("http://localhost:"
				+ port + "/uaa/", String.class);
		assertEquals(HttpStatus.FOUND, response.getStatusCode());
	}

	@Test
	public void authorizationRedirects() {
		ResponseEntity<String> response = template.getForEntity("http://localhost:"
				+ port + "/uaa/oauth/authorize", String.class);
		assertEquals(HttpStatus.FOUND, response.getStatusCode());
		String location = response.getHeaders().getFirst("Location");
		assertTrue("Wrong header: " + location,
				location.startsWith("http://localhost:" + port + "/uaa/login"));
	}

	@Test
	public void getCss(){
		ResponseEntity<String> response = template.getForEntity("http://localhost:"
				+ port + "/static/css/wro.css", String.class);
		assertEquals("aaaaaa",response.getBody());

	}

	@Test
	public void loginSucceeds() {
		ResponseEntity<String> response = template.getForEntity("http://localhost:"
				+ port + "/uaa/login", String.class);
		String csrf = getCsrf(response.getBody());
		MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
		form.set("username", "user");
		form.set("password", "password");
		form.set("_csrf", csrf);
		HttpHeaders headers = new HttpHeaders();
		headers.put("COOKIE", response.getHeaders().get("Set-Cookie"));
		RequestEntity<MultiValueMap<String, String>> request = new RequestEntity<MultiValueMap<String, String>>(
				form, headers, HttpMethod.POST, URI.create("http://localhost:" + port
						+ "/uaa/login"));
		ResponseEntity<Void> location = template.exchange(request, Void.class);
		assertEquals("http://localhost:" + port + "/uaa/",
				location.getHeaders().getFirst("Location"));
	}

	private String getCsrf(String soup) {
		Matcher matcher = Pattern.compile("(?s).*name=\"_csrf\".*?value=\"([^\"]+).*")
				.matcher(soup);
		if (matcher.matches()) {
			return matcher.group(1);
		}
		return null;
	}

}
