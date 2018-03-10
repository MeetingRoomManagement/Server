package ccf;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class Key {
	public void addNode()
	public static void main(String arg[]) {
		Scanner scanner = new Scanner(System.in);
		int keyN = scanner.nextInt();
		int key[] = new int[keyN];
		int teaN = scanner.nextInt();
		Teacher teacherEarly;
		int n = 0;
		ArrayList<Teacher> teachers = new ArrayList<Teacher>();
		while (scanner.hasNext()) {
			Teacher teacher = new Teacher(scanner.nextInt(), scanner.nextInt(), scanner.nextInt());		
			teachers.set(n, teacher);
		}
		for(int i = 0; i < keyN; i ++) {
			key[i] = i + 1;
		}
		for(int i = 0; i <keyN; i ++)
			for(int j = 0; j < keyN; j ++) {
				
			}
	}
	
}

class Node {
	public int key;
	public int start;
	public int time;
	public Node next;
	public Node(int key,int start,int time) {
		this.key = key;
		this.time = time;
		this.start = start;
	}
}
