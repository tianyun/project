package standard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class CompareUtil {

	public static void main(String[] args) {
		List<Student> students = new ArrayList<>();
		students.add(new Student("zt", 12));
		students.add(new Student("lw", 20));
		students.add(new Student("zz", 6));
		for (Student student : students) {
			System.out.println(student.toString());
		}

		Collections.sort(students);
		System.out.println("\n=====ÕıĞò======");
		for (Student student : students) {
			System.out.println(student.toString());
		}
		

		Collections.sort(students, new Comparator<Student>() {

			@Override
			public int compare(Student o1, Student o2) {
				// TODO Auto-generated method stub
				return o1.age > o2.age ? -1 : 1;
			}
			
		});
		
		System.out.println("\n=====µ¹Ğò======");
		for (Student student : students) {
			System.out.println(student.toString());
		}
		
		
		Student[] stuarr =(Student[]) students.toArray();
		for (Student student : stuarr) {
			System.out.println(student);
		}	
		
	}

}

class Student implements Comparable<Student> {

	String name;
	int age;
	
	public Student(String name, int age) {
		super();
		this.name = name;
		this.age = age;
	}


	@Override
	public String toString() {
		return "Student [name=" + name + ", age=" + age + "]";
	}

	@Override
	public int compareTo(Student o) {
		// TODO Auto-generated method stub
		return this.age > o.age ? 1 : -1;
	}

}
