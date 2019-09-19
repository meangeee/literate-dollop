package kr.or.ddit.designpattern.templatemethodpattern;

public class BadStudent extends Student {

	@Override
	protected void work() {
		System.out.println("열라 잠. 그냥 잠. 계속 잠.");
	}

}
