package cn.wjx.placetest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.Scanner;

public class JDBCUtilGenerator {

	@SuppressWarnings("resource")
	public void generateJDBCUtil() {
		File file = new File("source/JDBCUtil.txt");
		System.out.println("You are reading from : " + file.getName());
		System.out.println();
		PrintWriter pWriter = null;

		Properties prop = System.getProperties();
		String filePath = "";
		String osType = prop.getProperty("os.name");
		if (osType.equals("Mac OS X")) {
			String path = this.getClass().getResource("/").getFile().toString();
			String p = path.split("/bin/")[0];
			filePath = p + "/gen/JDBCUtil.java";
		} else {
			String path = this.getClass().getResource("\\").getFile()
					.toString();
			String p = path.split("\\bin\\")[0];
			filePath = p + "\\gen\\JDBCUtil.java";
		}
		try {
			pWriter = new PrintWriter(filePath);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		try {

			Scanner s = new Scanner(file);
			int i = 0;

			while (s.hasNextLine()) {
				String ss = s.nextLine();
				if (ss.endsWith("}")) {
					i = i - 6;
					pWriter.print("\r\n");
					for (int j = 0; j < i; j++) {
						pWriter.print(" ");
					}

					pWriter.print(ss);
				} else if (ss.endsWith("{")) {
					pWriter.print("\r\n");
					for (int j = 0; j < i; j++) {
						pWriter.print(" ");
					}
					pWriter.print(ss);
					i = i + 3;
				} else if (ss.endsWith(";")) {
					pWriter.print("\r\n");
					for (int j = 0; j < i; j++) {
						pWriter.print(" ");
					}
					pWriter.print(ss);
				}

			}

			pWriter.flush();
			System.out.println("The JDBCUtil class is generated successfully!");
			System.out.println("The generated JDBCUtil.java file path is : "
					+ filePath);
			System.out.println();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
