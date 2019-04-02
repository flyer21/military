package sample;

import com.military.config.server.ConfigServerApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

//import org.springframework.boot.test.TestRestTemplate;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = ConfigServerApplication.class)
//@WebAppConfiguration
//@IntegrationTest("server.port=0")

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ConfigServerApplication.class,
		// Normally spring.cloud.config.enabled:true is the default but since we have the
		// config
		// server on the classpath we need to set it explicitly
		properties = { "spring.cloud.config.enabled:true", "",
				"management.security.enabled=false",
				"management.endpoints.web.exposure.include=*" }, webEnvironment = RANDOM_PORT)

public class ApplicationTests {

	@Value("${local.server.port}")
	private int port = 0;

    /**
     * {
     *     "name": "app1",
     *     "profiles": [
     *         "dev"
     *     ],
     *     "label": "master",
     *     "version": "3e00a33ba8ef6aed85f79f387b8a97c8dbc2bf9d",
     *     "state": null,
     *     "propertySources": [
     *         {
     *             "name": "https://github.com/flyer21/military-config-rep.git/application-dev.yml",
     *             "source": {
     *                 "eureka.client.serviceUrl.defaultZone": "http://localhost:8761/eureka/"
     *             }
     *         },
     *         {
     *             "name": "https://github.com/flyer21/military-config-rep.git/application.yml",
     *             "source": {
     *                 "military.server.version": "1.0.0"
     *             }
     *         }
     *     ]
     * }
     */
	@Test
	public void configurationAvailable() {
	    String appName="app1";
	    String profile ="dev";
	    String label ="master";
	    ///{application}/{profile}[/{label}]
		@SuppressWarnings("rawtypes")
		ResponseEntity<Map> entity = new TestRestTemplate().getForEntity(
				"http://localhost:" + port + "/"+appName+"/"+profile+"/"+ label, Map.class);
		assertEquals(HttpStatus.OK, entity.getStatusCode());
		assertEquals(label, entity.getBody().get("label"));

	}

    /**
     * {
     *     "name": "actuator",
     *     "profiles": [
     *         "env"
     *     ],
     *     "label": null,
     *     "version": "3e00a33ba8ef6aed85f79f387b8a97c8dbc2bf9d",
     *     "state": null,
     *     "propertySources": [
     *         {
     *             "name": "https://github.com/flyer21/military-config-rep.git/application.yml",
     *             "source": {
     *                 "military.server.version": "1.0.0"
     *             }
     *         }
     *     ]
     * }
     */
	@Test
	public void envPostAvailable() {
//		MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
		@SuppressWarnings("rawtypes")
		ResponseEntity<Map> entity = new TestRestTemplate().getForEntity(
				"http://localhost:" + port + "/actuator/env/spring.application.name",  Map.class);
		assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertThat(entity.getBody()).containsKey("propertySources");
        Map<String, Object> property = (Map<String, Object>) entity.getBody().get("property");
        assertThat(property).containsEntry("value", "config-server");
	}

}
