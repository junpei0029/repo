package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import todolist.StatePrimary;
import todolist.Todo;

public class ReadCSV {

	public static void readCSV(StatePrimary s) {

		try {

			// final String csfFilePath = PropertyUtil.getCsvfilePath();
			// File csv = new File(csfFilePath); // CSVデータファイル
			File csv = new File("./todolist.csv");

			final BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(csv), "Shift_JIS"));

			String line = "";
			while ((line = br.readLine()) != null) {

				StringTokenizer st = new StringTokenizer(line, ",");
				if (st.nextToken().equals(s.getKbn())) {
					String todoNm = st.nextToken();
					Todo todo;
					if (st.hasMoreTokens()) {
						String time = st.nextToken();
						todo = new Todo(todoNm, time);
					} else {
						todo = new Todo(todoNm);
					}
					s.addListElement(todo);
				}
			}
			br.close();

		} catch (FileNotFoundException e) {
			// Fileオブジェクト生成時の例外捕捉
			e.printStackTrace();
		} catch (IOException e) {
			// BufferedWriterオブジェクトのクローズ時の例外捕捉
			e.printStackTrace();
		}
	}
}
