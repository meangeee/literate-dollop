package kr.or.ddit.designpattern.templatemethodpattern;

public abstract class Student {
	// template method 
	// 서블릿에선 service를 의미함
	public final void lifecycle() {
		inchecking();
		work();
		outchecking();
	}
	
	private void inchecking() {
		System.out.println("지문 인체크");
	}
	
	// hook method
	protected abstract void work();
	
	
	private void outchecking() {
		System.out.println("지문 아웃체크");
	}
}
