package ccf;

import java.util.Scanner;

public class Sauce {
	public static void main(String args[]) {
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt() / 10;
		int result;
		int a = n / 5;
		int b = n % 5 / 3;
		result = a * 7 + b * 4 + n % 5 % 3;
		System.out.println(result);
	}
}
