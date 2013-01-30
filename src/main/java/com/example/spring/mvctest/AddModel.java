package com.example.spring.mvctest;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class AddModel {
	@NotNull
	@Min(0)
	@Max(9_999)
	private Integer num1;
	@NotNull
	@Min(0)
	@Max(9_999)
	private Integer num2;
	private Integer answer;

	public void calc() {
		answer = num1 + num2;
	}

	public Integer getNum1() {
		return num1;
	}

	public void setNum1(Integer num1) {
		this.num1 = num1;
	}

	public Integer getNum2() {
		return num2;
	}

	public void setNum2(Integer num2) {
		this.num2 = num2;
	}

	public Integer getAnswer() {
		return answer;
	}

	public void setAnswer(Integer answer) {
		this.answer = answer;
	}
}
