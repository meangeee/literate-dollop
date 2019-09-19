package kr.or.ddit.exception;

import java.io.IOException;

/**
 * 예외란? (Throwable) : 어플리케이션 내에서 발생하는 비정상 상황의 포괄 개념. 
 * 	1. Error : 처리하지 않는/ 할 수 없는 비정상 상황 
 *  2. Exception : 처리할 수 있는 상황 
 *  	1) Exception (Checked exception) 
 *  		: 발생 가능한 상황에서는 반드시 처리를 해야 하는 예외 IOException, SQLException 
 * 		2) RuntimeException (UnChecked exception) 
 * 			: 처리하지 않더라도 VM에게 제어권이 전달됨. 
 * 			NullPointerException,IllegalArgumentException
 * 
 * ** 예외 발생 방법 * throw new 예외타입의 생성자();
 * 
 * ** 예외 처리 방법 1. 적극적 : try~catch~finally 
 * 				2. 소극적 : throws
 * 
 * 
 */
public class ExceptionDesc {
	   public static void main(String[] args) throws IOException {
//	      String message = test();
//	      System.out.println(message);
		  try {
//			login("b001","java");
			login("a001","sql");
			boolean result = login("a001", "sql");
			if(result) {
				System.out.println("로그인 성공");
			}else {
				System.out.println("아이디나 비밀번호가 틀렸음");
			}
		} catch (UserNotFoundException e) {
			//예외 메시지를 받는다
			System.err.println(e.getMessage());
		}catch(NotAuthenticatedException e) {
			System.out.println(e.getMessage());
		}
	  }
	   public static String test() {
	      try {
	         String result = "결과";
	         if(1==1) {
	            throw new IOException("강제 발생 예외, checked 형태");
	         }
	         return result;
	      } catch (IOException e) {
//	          throw new RuntimeException(e);
	    	  return "짜가";
	      } 
	   }
	   
	   public static final String ID = "a001";
	   public static final String PASSWORD = "java";
	   
	   public static boolean login(String id, String pass){
		   if(!ID.equals(id)) { //없다고 가정
//			   throw new UserNotFoundException(id + "에 해당 하는 사람 없음.");
//			   return false;
		   }
		   
		   if(!PASSWORD.equals(pass)) {	//아이디는 맞고 비번이 틀렸을 때
			   throw new NotAuthenticatedException("비번오류");
//			   return false;
			   
		   }
		   return true;
	   }
	}