package command;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

//@RunWith(SpringJUnit4ClassRunner.class) // 지난 버전에서 사용함
//SpringRunner는 SpringJUnit4ClassRunner를 상속받으며 SpringJUnit4ClassRunner의 alias임
@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration("/spring/spring-*.xml")
// @ContextConfiguration("file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml")
public class NOperationCommandTest {

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		// this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build(); // 아래와 같은 코드
		this.mockMvc = webAppContextSetup(this.wac).alwaysExpect(status().isOk()).alwaysDo(print()).build(); //항상 200 ok가 리턴됨을 검사함, 항상 디버깅 로그를 찍음
	}

	@Test
	public void getOperation_print() throws Exception {
		this.mockMvc.perform(get("/noperation"));
				//.andDo(print()); //표준출력(콘솔) 디버깅 용도 -> 공통으로 변경
				// .andDo(log()) //로그 객체를 이용하여 파일이나 다른 지정한 곳에 로그를 남김
	}
	
	@Test
	public void getOperation_form_submit() throws Exception {
		
		this.mockMvc.perform(
				post("/noperation")
					.param("msg", "아무거나")
					.param("agent", "암호랑이(234gasf)") 
					.param("agent", "호랑이(3rggaef)")) //agent는 checkbox로 다중선택가능함
				.andExpect(forwardedUrl("/WEB-INF/view/noperation.jsp"));
	}

	@Test
	public void getOperation_Return() throws Exception {
		// mvcResult 객체를 통해 결과에 직접 접근할 수 있다. 따라서 검증이 불가한 항목에 대하여 검증이 가능하다
		MvcResult mvcResult = this.mockMvc.perform(get("/noperation")).andReturn();
	}

	/*	@Test
	public void getAccount() throws Exception {
		this.mockMvc.perform(get("/noperation").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(content().contentType("application/json")).andExpect(jsonPath("$.name").value("Lee"));
	}*/
	
}
