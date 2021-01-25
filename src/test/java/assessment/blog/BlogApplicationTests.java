package assessment.blog;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.server.ResponseStatusException;
import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BlogApplicationTests {

		private static final Logger log = LoggerFactory.getLogger(UserController.class);
		private MockMvc mockMvc;
		private MockHttpSession session;
		private SessionFilter sessionFilter;
		@Autowired
		private UserController controller;
		@Autowired
		private UserRepository users;
		@Autowired
		private BlogRepository blogs;
		@Autowired
		private WebApplicationContext wac;
		@Autowired
		private FilterChainProxy springSecurityFilterChain;
		@LocalServerPort
		private int port;

		@Before
		public void setup() {
			RestAssured.port = port;
			this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilters(this.springSecurityFilterChain).build();
		}

		@Test
		public void contextLoads() throws Exception {
			assertThat(controller).isNotNull();
		}

		@Test
		public void registerUser() throws Exception {
				String testEmail = "test@example.com";
				String password = "pass";
				given().header("Content-Type", "application/json").body("{\"email\": \"test1@example.com\", \"password\": \"p\"}").
					when().post("http://localhost:"+String.valueOf(port)+"/register").then().statusCode(200);
		}

		@Test
		public void registerUserBadEmail() throws Exception {
			String testEmail = "test@example.com";
			String password = "pass";
			given().header("Content-Type", "application/json").body("{\"email\": \"test1wrongmail\", \"password\": \"p\"}").
					when().post("http://localhost:"+String.valueOf(port)+"/register").then().statusCode(422);
		}


		@Test
		public void returnBlogs() throws Exception {
			this.sessionFilter = new SessionFilter();
			given().auth().form("user1@example.com", "p").filter(this.sessionFilter).when().
					get("http://localhost:"+String.valueOf(port)+"/login").then().statusCode(200);
			given().filter(this.sessionFilter).when().
					get("http://localhost:"+String.valueOf(port)+"/blogs").then().statusCode(200);

		}

		@Test
		public void createAndDeleteBlog() throws Exception {
			this.sessionFilter = new SessionFilter();
			given().auth().form("user1@example.com", "p").filter(this.sessionFilter).when().
					get("http://localhost:"+String.valueOf(port)+"/login").then().statusCode(200);
			given().filter(this.sessionFilter).when().
					header("Content-Type", "application/json").body("{\"title\": \"newBlog\", \"content\": \"text\"}").
					when().post("http://localhost:"+String.valueOf(port)+"/blogs").then().statusCode(200);
			given().filter(this.sessionFilter).when().
					get("http://localhost:"+String.valueOf(port)+"/blogs").then().statusCode(200);
			given().filter(this.sessionFilter).when().
					when().delete("http://localhost:"+String.valueOf(port)+"/blogs/1").then().statusCode(200);
		}



}



