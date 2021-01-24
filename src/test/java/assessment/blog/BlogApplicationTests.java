package assessment.blog;

import org.junit.Before;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class BlogApplicationTests {

		private MockMvc mockMvc;
		private MockHttpSession session;
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

		@Before
		public void setup() {
			this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilters(this.springSecurityFilterChain).build();
			MockHttpSession session = new MockHttpSession();
			User user = users.findById(1l).get();
			session.setAttribute("user", user);
		}

		@Test
		public void contextLoads() throws Exception {
			assertThat(controller).isNotNull();
		}

		@Test
		public void registerUser() throws Exception {
				String testEmail = "test@example.com";
				String password = "pass";
				controller.newRegistration(new UserForm(testEmail, password));
				assertThat(users.getUserByEmail(testEmail).getEmail().equals(testEmail));
		}

		@Test
		public void returnBlogs() throws Exception {
			try {
				List<Blog> recv = controller.allBlogs();
				fail("Returned blogs without auth");
			}
			catch (ResponseStatusException e){

			}
		}

		@Test
		public void createBlog() throws Exception {
			try {
				Blog blog = controller.newBlogEntry(new BlogForm("title", "content"));
				fail("Created blog without auth");
			}
			catch (ResponseStatusException e){
			}
		}

		@Test
		public void deleteBlog() throws Exception {
			try {
				controller.deleteBlog(1l);
				fail("Created blog without auth");
			}
			catch (ResponseStatusException e){
			}
		}


}



