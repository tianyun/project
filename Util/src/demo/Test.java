package demo;

import java.util.Arrays;

public class Test {
	public static void main(String[] args) {
		String str = "10";
				int i = Integer.parseInt(str,16); //转换成十进程整形
		System.out.println(i);

		
		person p = new person();
		p.age=22;
		p.name="zz";
		
		System.out.println(p.toString());
	
		gets(p);
		System.out.println(p.toString());

	}
	
	public static void gets(person pp) {
		pp.age=1;
	}

}

class person{
	int age;
	String name;
	@Override
	public String toString() {
		return "person [age=" + age + ", name=" + name + "]";
	}
	
	
}