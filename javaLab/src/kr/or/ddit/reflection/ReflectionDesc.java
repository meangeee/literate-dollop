package kr.or.ddit.reflection;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

import kr.or.ddit.reflect.ReflectionTest;

public class ReflectionDesc {
	public static void main(String[] args) {
		// marshalling : java, c, javacscript -> xml, json
		Test t = new Test();
		t.setNumber(42);
		t.setStr("test");
//		{"number" :42, "str" : "test"}
		String propPattern = "\"%s\":%s,";
		StringBuffer json = new StringBuffer();
		try {
			json.append("{");
			Class<Test> tType = Test.class;
			Field[] fields = tType.getDeclaredFields();
			for (Field tmp : fields) {
				String name = tmp.getName();
				tmp.setAccessible(true);
				Object retVal;
				retVal = tmp.get(t);
				Class<?> fldType = tmp.getType();
				String formatValue = null;
				if (retVal != null && fldType.equals(String.class)) {
					formatValue = "\"" + retVal + "\"";
				} else {
					formatValue = Objects.toString(retVal);
				}
				json.append(String.format(propPattern, name, formatValue));
			}//for end
			if(json.lastIndexOf(",")==json.length()-1) {
				json.deleteCharAt(json.lastIndexOf(","));
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		json.append("}");
		System.out.println(json);
	}

	public static void reflectionTest() {
		Test t = new Test();
		t.setNumber(42);
		t.setStr("test");
		System.out.println(t.merge());

		Object instance = ReflectionTest.getObject();
		// 실제 타입은 무엇인지 모름
		Class<?> instanceType = instance.getClass(); // instancType을 이용해 맛도보고 어쩌고저쩌고
		System.out.println(instanceType.getName());
		// 이미 결정되어있는 붕어빵의 특성을 찾는 것. get이 많음 Field = 전역변수
		Field[] fields = instanceType.getDeclaredFields();
		for (Field fld : fields) {
			String name = fld.getName(); // 전역변수명
			Class<?> fldType = fld.getType();
			System.out.printf("%s %s;\n", fldType.getSimpleName(), name);
			try {
				PropertyDescriptor pd = new PropertyDescriptor(name, instanceType);
				Object retVal = pd.getReadMethod().invoke(instance);
				System.err.printf("%s 호출반환 : %s\n", pd.getReadMethod().getName(), retVal);
			} catch (IntrospectionException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String getter = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
			// 행동 꺼내기
			try {
				// declare는 public 상관없이 데려옴
				Method method = instanceType.getDeclaredMethod(getter);
//				method.getMem_id(); 밑에와 같은 코드 다만 밑에는 거꾸로 찾아가고 있는 과정
				Object retValue = method.invoke(instance);
				System.out.printf("%s 호출 반환 : %s \n", method.getName(), retValue);
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static class Test {
		private String str;
		private Integer number;

		public String merge() {
			return str + number;
		}

		public String getStr() {
			return str;
		}

		public void setStr(String str) {
			this.str = str;
		}

		public Integer getNumber() {
			return number;
		}

		public void setNumber(Integer number) {
			this.number = number;
		}

	}
}
