package command;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

//@RunWith(SpringJUnit4ClassRunner.class) // 지난 버전에서 사용함
//SpringRunner는 SpringJUnit4ClassRunner를 상속받으며 SpringJUnit4ClassRunner의 alias임
@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration("/spring/spring-*.xml")
public class NOperationCommandTest {

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	public void getAccount() throws Exception {
		this.mockMvc.perform(get("/noperation").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(status().isOk()).andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$.name").value("Lee"));
	}

	@Test
	public void getOperation() throws Exception {
		this.mockMvc.perform(get("/noperation"))
		//.andDo(print())
		//.andDo(log())
		.andExpect(status().isOk())
		.andReturn()
		;
	}
	
	@Test
	public void getOperationReturn() throws Exception {
		//mvcResult 객체를 통해 결과에 직접 접근할 수 있다. 따라서 검증이 불가한 항목에 대하여 검증이 가능하다
		MvcResult mvcResult = this.mockMvc.perform(get("/noperation"))
		.andExpect(status().isOk())
		.andReturn();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
