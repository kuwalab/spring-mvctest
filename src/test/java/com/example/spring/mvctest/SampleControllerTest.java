package com.example.spring.mvctest;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/beans-webmvc.xml" })
public class SampleControllerTest {
	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		mockMvc = webAppContextSetup(wac).build();
	}

	@Test
	public void slashへのGET() throws Exception {
		mockMvc.perform(get("/")).andExpect(status().isOk())
				.andExpect(view().name("index"))
				.andExpect(model().hasNoErrors());
	}

	@Test
	public void slashへのPOST_許可されていないメソッド() throws Exception {
		mockMvc.perform(post("/")).andExpect(status().isMethodNotAllowed());
	}

	@Test
	public void slash_addへのGET() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get("/add"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("addModel")).andReturn();

		// Modelの中身までテストする場合には、MvcResultからModelを取得しないといけない
		ModelMap modelMap = mvcResult.getModelAndView().getModelMap();
		Object object = modelMap.get("addModel");
		assertThat(object, is(not(nullValue())));
		assertThat(object, is(instanceOf(AddModel.class)));
		AddModel addModel = (AddModel) object;
		assertThat(addModel.getNum1(), is(nullValue()));
		assertThat(addModel.getNum2(), is(nullValue()));
		assertThat(addModel.getAnswer(), is(nullValue()));
	}

	@Test
	public void slash_calcへのPOST() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(post("/calc").param("num1", "1").param("num2", "2"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("addModel")).andReturn();

		// Modelの中身までテストする場合には、MvcResultからModelを取得しないといけない
		ModelMap modelMap = mvcResult.getModelAndView().getModelMap();
		Object object = modelMap.get("addModel");
		assertThat(object, is(not(nullValue())));
		assertThat(object, is(instanceOf(AddModel.class)));
		AddModel addModel = (AddModel) object;
		assertThat(addModel.getNum1(), is(1));
		assertThat(addModel.getNum2(), is(2));
		assertThat(addModel.getAnswer(), is(3));
	}

	@Test
	public void slash_add2へのGET() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get("/add2"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("addModel")).andReturn();

		// Modelの中身までテストする場合には、MvcResultからModelを取得しないといけない
		ModelMap modelMap = mvcResult.getModelAndView().getModelMap();
		Object object = modelMap.get("addModel");
		assertThat(object, is(not(nullValue())));
		assertThat(object, is(instanceOf(AddModel.class)));
		AddModel addModel = (AddModel) object;
		assertThat(addModel.getNum1(), is(nullValue()));
		assertThat(addModel.getNum2(), is(nullValue()));
		assertThat(addModel.getAnswer(), is(nullValue()));
	}

	@Test
	public void slash_calc2へのPOST() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(post("/calc2").param("num1", "1").param("num2", "2"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("addModel")).andReturn();

		// Modelの中身までテストする場合には、MvcResultからModelを取得しないといけない
		ModelMap modelMap = mvcResult.getModelAndView().getModelMap();
		Object object = modelMap.get("addModel");
		assertThat(object, is(not(nullValue())));
		assertThat(object, is(instanceOf(AddModel.class)));
		AddModel addModel = (AddModel) object;
		assertThat(addModel.getNum1(), is(1));
		assertThat(addModel.getNum2(), is(2));
		assertThat(addModel.getAnswer(), is(3));
	}

	@Test
	public void slash_calc2へのPOST_validatorのテスト() throws Exception {
		mockMvc.perform(post("/calc2").param("num1", "").param("num2", ""))
				.andExpect(status().isOk())
				.andExpect(view().name("add2"))
				.andExpect(model().attributeExists("addModel"))
				.andExpect(model().hasErrors())
				.andExpect(model().errorCount(2))
				.andExpect(
						model().attributeHasFieldErrors("addModel", "num1",
								"num2"));
		mockMvc.perform(post("/calc2").param("num1", "9999").param("num2", ""))
				.andExpect(status().isOk()).andExpect(view().name("add2"))
				.andExpect(model().attributeExists("addModel"))
				.andExpect(model().hasErrors())
				.andExpect(model().errorCount(1))
				.andExpect(model().attributeHasFieldErrors("addModel", "num2"));
		mockMvc.perform(post("/calc2").param("num1", "").param("num2", "9999"))
				.andExpect(status().isOk()).andExpect(view().name("add2"))
				.andExpect(model().attributeExists("addModel"))
				.andExpect(model().hasErrors())
				.andExpect(model().errorCount(1))
				.andExpect(model().attributeHasFieldErrors("addModel", "num1"));
		mockMvc.perform(
				post("/calc2").param("num1", "100000").param("num2", "100000"))
				.andExpect(view().name("add2"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("addModel"))
				.andExpect(model().hasErrors())
				.andExpect(model().errorCount(2))
				.andExpect(
						model().attributeHasFieldErrors("addModel", "num1",
								"num2"));
	}

	@Test
	public void slash_calc2へのPOST_validatorのメッセージのテスト() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(post("/calc2").param("num1", "").param("num2", ""))
				.andExpect(status().isOk())
				.andExpect(view().name("add2"))
				.andExpect(model().attributeExists("addModel"))
				.andExpect(model().hasErrors())
				.andExpect(model().errorCount(2))
				.andExpect(
						model().attributeHasFieldErrors("addModel", "num1",
								"num2")).andDo(print()).andReturn();
		ModelAndView mav = mvcResult.getModelAndView();
		Map<String, Object> map = mav.getModel();
		for (String key : map.keySet()) {
			System.out.println(key);
		}

		// エラーメッセージは、「org.springframework.validation.BindingResult.モデル名」に格納される。
		Object object = mav.getModel().get(
				"org.springframework.validation.BindingResult.addModel");
		assertThat(object, is(not(nullValue())));
		assertThat(object, is(instanceOf(BindingResult.class)));
		BindingResult bindingResult = (BindingResult) object;

		// num1のエラーを取り出しテスト
		List<FieldError> list = bindingResult.getFieldErrors("num1");
		assertThat(list, is(not(nullValue())));
		assertThat(list.size(), is(1));
		FieldError fieldError = list.get(0);
		assertThat(fieldError.getCode(), is("NotNull"));
		Object[] args = fieldError.getArguments();
		assertThat(args.length, is(1));
		assertThat(args[0],
				is(instanceOf(DefaultMessageSourceResolvable.class)));
		DefaultMessageSourceResolvable dmr = (DefaultMessageSourceResolvable) args[0];
		assertThat(dmr.getCode(), is("num1"));
	}

	@Test
	public void slash_csv_へのGETのテスト() throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("名前,年齢\r\n");
		sb.append("山田太郎,28\r\n");
		sb.append("鈴木四郎,44\r\n");
		sb.append("田中花子,19\r\n");

		mockMvc.perform(get("/csv"))
				.andExpect(
						content().contentType(
								"application/octet-stream;charset=windows-31j"))
				.andExpect(content().string(sb.toString()));
	}

	@Test
	public void slash_uploadへのPOST() throws Exception {
		byte[] fileImage = null;
		Path path = Paths.get("src/test/resources/kappa.jpg");
		if (Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
			fileImage = Files.readAllBytes(path);
		}

		// ローカルのファイル名もエミュレーションできる。
		String fileName = "かっぱ.jpg";
		MockMultipartFile file = new MockMultipartFile("uploadFile", fileName,
				null, fileImage);
		mockMvc.perform(fileUpload("/upload").file(file))
				.andExpect(redirectedUrl("/uploadForm"))
				.andExpect(
						flash().attribute("saveLocation", endsWith("に保管しました。")));
		File actualFile = new File(System.getProperty("java.io.tmpdir"),
				fileName);

		// 画像保管されていることを確認する
		assertThat(actualFile.exists(), is(true));
		byte[] actualImage = Files.readAllBytes(Paths.get(actualFile.toURI()));
		assertThat(actualImage, is(equalTo(fileImage)));
	}
}
