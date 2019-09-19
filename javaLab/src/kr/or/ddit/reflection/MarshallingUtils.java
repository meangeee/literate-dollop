package kr.or.ddit.reflection;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.ClassUtils;

/**
 * Marshalling : 특정 언어로 표현된 데이터(java object)를 범용 표현 방식(json/xml)으로 변환하는 작업
 *
 */
public class MarshallingUtils {
   public String marshalling(Object target) {
      // int(Integer),double(Double),char,String,StringBuffer

      if (ClassUtils.isPrimitiveOrWrapper(target.getClass())
            || CharSequence.class.isAssignableFrom(target.getClass())) {
         throw new IllegalArgumentException("마샬링이 불가능한 데이터");
      }
      return marshallingObjectToJson(target);

   }

   private String marshallingObjectToJson(Object target) {
      if (target == null) {
         return null;
      }
      Class<?> targetType = target.getClass();
      String value = null;
      // String 타입을 잡음
      if (CharSequence.class.isAssignableFrom(targetType) ||
      // char타입을 잡음
            ClassUtils.isAssignable(targetType, Character.class, true)) {
         value = String.format("\"%s\"", target);
      } else if (ClassUtils.isPrimitiveOrWrapper(targetType)) {
         value = target.toString();
      } else if (targetType.isArray()) {
         // 배열요소의 타입을 가져옴
    	  value = marshallingArrayToJson(target);

//    	  Object[] array = (Object[]) target;
      
      } else if (target instanceof Map) {
         Map map = (Map) target;
         value = marshallingMapToJson(map);
      } else {
         StringBuffer json = new StringBuffer("{");
         Field[] fields = targetType.getDeclaredFields();
         for (Field tmp : fields) {
            String name = tmp.getName();
            try {
               PropertyDescriptor pd = new PropertyDescriptor(name, targetType);
               Object propValue = pd.getReadMethod().invoke(target);
               json.append(String.format(PROPPATTERN, name, marshallingObjectToJson(propValue)));
            } catch (IntrospectionException | IllegalAccessException | IllegalArgumentException
                  | InvocationTargetException e) {
               // getter가 없는 경우 다음 요소로 넘어가기
               System.err.println(e.getMessage());
               continue;
            }

         }
         int lastIndex = json.lastIndexOf(",");
         if (lastIndex == json.length() - 1) {
            json.deleteCharAt(lastIndex);
         }
         json.append("}");
         value = json.toString();
      }
      return value;
   }

   private final String PROPPATTERN = "\"%s\":%s,";

   private String marshallingMapToJson(Map map) {
      // 순서가 있는 것 처럼 접근 가능
      Iterator<?> keys = map.keySet().iterator();
      StringBuffer json = new StringBuffer("{");
      while (keys.hasNext()) {
         Object key = (Object) keys.next();
         Object value = map.get(key);
         json.append(String.format(PROPPATTERN, key, marshallingObjectToJson(value)));

      }
      int lastIndex = json.lastIndexOf(",");
      if (lastIndex == json.length() - 1) {
         json.deleteCharAt(lastIndex);
      }
      json.append("}");
      return json.toString();
   }

   public String marshallingArrayToJson(Object array) {
      StringBuffer json = new StringBuffer("[");
      if (array != null) {
    	  int length = Array.getLength(array);
         // 배열에있는 요소 꺼내기
         for (int i = 0; i<length; i++) {
            json.append(marshallingObjectToJson(Array.get(array, i)) + ",");
         }
         int lastIndex = json.lastIndexOf(",");
         if (lastIndex == json.length() - 1) {
            json.deleteCharAt(lastIndex);
         }
      }
      json.append("]");
      return json.toString();
   }

   //검증용
   public static class TestVO{
      private Integer number = new Integer(34);
      private int num = 23;
      private int[] numbers = new int[] {1,2,3};
      private String[] array = new String[] {"a","b"};
      private Map<String,Object> map = new HashMap<String, Object>();
      
      public int[] getNumbers() {
		return numbers;
	}
      
	public void setNumbers(int[] numbers) {
		this.numbers = numbers;
	}
	
	{
         map.put("key1", number);
         map.put("key2", num);
         map.put("key3", array);
         //얘떄문에 무한루프돈다
//         map.put("key4", new TestVO());
      }
      public Integer getNumber() {
         return number;
      }
      public void setNumber(Integer number) {
         this.number = number;
      }
      public int getNum() {
         return num;
      }
      public void setNum(int num) {
         this.num = num;
      }
      public String[] getArray() {
         return array;
      }
      public void setArray(String[] array) {
         this.array = array;
      }
      public Map<String, Object> getMap() {
         return map;
      }
      public void setMap(Map<String, Object> map) {
         this.map = map;
      }
      
      
      
   }
   
   
   public static void main(String[] args) {
      TestVO test = new TestVO();
      String json = new MarshallingUtils().marshalling(test);
      System.out.println(json);
//      Object target = "Text";
//      System.out.println(new MarshallingUtils().marshallingObjectToJson(target));
//      target = 'c';
//      System.out.println(new MarshallingUtils().marshallingObjectToJson(target));
//      target = new Character('a');
//      System.out.println(new MarshallingUtils().marshallingObjectToJson(target));
//      target = new StringBuffer("Text");
//      System.out.println(new MarshallingUtils().marshallingObjectToJson(target));
//      target = 32;
//      System.out.println(new MarshallingUtils().marshallingObjectToJson(target));
//      target = true;
//      System.out.println(new MarshallingUtils().marshallingObjectToJson(target));
//      target = null;
//      System.out.println(new MarshallingUtils().marshallingObjectToJson(target));
//      System.out.println("=====================================================");
//      // new char[]로만 해놓으면 기본 배열형은 잡을 수가 없다.
//      target = new char[] { 'a', 'b' };
//      System.out.println(new MarshallingUtils().marshallingObjectToJson(target));
//      target = new Integer[] { 23, 18 };
//      System.out.println(new MarshallingUtils().marshallingObjectToJson(target));
//      target = new String[] {};
//      System.out.println(new MarshallingUtils().marshallingObjectToJson(target));
//      target = new String[] { "Text" };
//      System.out.println(new MarshallingUtils().marshallingObjectToJson(target));
//      System.out.println("=====================================================");
//
//      Map<String, Object> map = new HashMap<String, Object>();
//      map.put("key1", 34);
//      map.put("key2", "test");
//      map.put("key3", new Character('c'));
//      System.out.println(new MarshallingUtils().marshallingObjectToJson(map));
//      System.out.println("=====================================================");
      
      

   }
}