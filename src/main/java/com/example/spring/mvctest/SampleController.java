package com.example.spring.mvctest;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import javax.mail.internet.MimeUtility;
import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SampleController {
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index() {
		return "index";
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model) {
		model.addAttribute("addModel", new AddModel());
		return "add";
	}

	@RequestMapping(value = "/calc", method = RequestMethod.POST)
	public String calc(@ModelAttribute("addModel") AddModel addModel,
			Model model) {
		addModel.calc();
		model.addAttribute("addModel", addModel);

		return "calc";
	}

	@RequestMapping(value = "/add2", method = RequestMethod.GET)
	public String add2(Model model) {
		model.addAttribute("addModel", new AddModel());
		return "add2";
	}

	@RequestMapping(value = "/calc2", method = RequestMethod.POST)
	public String calc2(@Valid @ModelAttribute("addModel") AddModel addModel,
			Errors errors, Model model) {
		if (errors.hasErrors()) {
			return "add2";
		}
		addModel.calc();
		model.addAttribute("addModel", addModel);

		return "calc2";
	}

	private static final MediaType MEDIA_TYPE_CSV = new MediaType(
			"application", "octet-stream", Charset.forName("Windows-31J"));
	private static final String CRLF = "\r\n";

	private String encodeFileName(String fileName) {
		try {
			return MimeUtility.encodeWord(fileName, "ISO-2022-JP", "B");
		} catch (UnsupportedEncodingException e) {
			// 来ないはず。
			return "csv.txt";
		}
	}

	@RequestMapping(value = "/csv", method = RequestMethod.GET)
	public ResponseEntity<String> csv() {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentDispositionFormData("attatchment",
				encodeFileName("顧客名簿.csv"));
		httpHeaders.setContentType(MEDIA_TYPE_CSV);

		StringBuilder sb = new StringBuilder();
		sb.append("名前,年齢").append(CRLF);
		sb.append("山田太郎,28").append(CRLF);
		sb.append("鈴木四郎,44").append(CRLF);
		sb.append("田中花子,19").append(CRLF);

		return new ResponseEntity<String>(sb.toString(), httpHeaders,
				HttpStatus.OK);
	}

	@RequestMapping(value = "/uploadForm", method = RequestMethod.GET)
	public String uploadForm() {
		return "uploadForm";
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	public String upload(@RequestParam("uploadFile") MultipartFile uploadFile,
			RedirectAttributes redirectAttributes) {
		try {
			File file = new File(System.getProperty("java.io.tmpdir"),
					uploadFile.getOriginalFilename());
			uploadFile.transferTo(file);
			redirectAttributes.addFlashAttribute("saveLocation",
					file.getCanonicalPath() + "に保管しました。");
		} catch (IOException e) {
			return "uploadForm";
		}

		return "redirect:/uploadForm";
	}
}
