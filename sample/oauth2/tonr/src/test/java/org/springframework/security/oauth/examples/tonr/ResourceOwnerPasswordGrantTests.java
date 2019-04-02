package org.springframework.security.oauth.examples.tonr;


import org.junit.Rule;
import org.junit.Test;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * @author Dave Syer
 */
public class ResourceOwnerPasswordGrantTests {

	@Rule
	public ServerRunning serverRunning = ServerRunning.isRunning();

	@Test
	public void testConnectDirectlyToResourceServer() throws Exception {

		ResourceOwnerPasswordResourceDetails resource = new ResourceOwnerPasswordResourceDetails();

		resource.setAccessTokenUri(serverRunning.getUrl("/sparklr2/oauth/token"));
		resource.setClientId("my-trusted-client");
		resource.setId("sparklr");
		resource.setScope(Arrays.asList("trust"));
		resource.setUsername("marissa");
		resource.setPassword("koala");

		OAuth2RestTemplate template = new OAuth2RestTemplate(resource);
		String result = template.getForObject(serverRunning.getUrl("/sparklr2/photos/user/message"), String.class);
		// System.err.println(result);
		assertEquals("Hello, Trusted User marissa", result);

	}

}
