package kr.or.ddit.designpattern;

import kr.or.ddit.designpattern.templatemethodpattern.BadStudent;
import kr.or.ddit.designpattern.templatemethodpattern.GoodStudent;
import kr.or.ddit.designpattern.templatemethodpattern.Student;

public class DDIT {
	public static void main(String[] args) {
		
		Student[] students = new Student[10];
		
		int i = 0;
		for(;i<5;i++) {
			students[i] = new GoodStudent();
		}
		
		for(;i<students.length;i++) {
			students[i] = new BadStudent(); 
		}
		
		for(Student tmp : students) {
			tmp.lifecycle();
		}
	}
}
