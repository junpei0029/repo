package io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import util.StringUtil;

public class WriteCSV {

	public static void createCSV(List<Map<String, String>> list) {

		try {
			// final String csfFilePath = PropertyUtil.getCsvfilePath();
			// File csv = new File(csfFilePath); // CSVデータファイル
			File csv = new File("./todolist.csv");
			// 上書きモード
			final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csv, false),
					"Shift_JIS"));

			for (Iterator<Map<String, String>> it = list.iterator(); it.hasNext();) {
				Map<String, String> m = it.next();
				if (StringUtil.isNotBlank(m.get("time"))) {
					bw.write(m.get("kbn") + "," + m.get("content") + "," + m.get("time"));
				} else {
					bw.write(m.get("kbn") + "," + m.get("content") + ",");
				}
				bw.newLine();
			}
			bw.close();

		} catch (FileNotFoundException e) {
			// Fileオブジェクト生成時の例外捕捉
			e.printStackTrace();
		} catch (IOException e) {
			// BufferedWriterオブジェクトのクローズ時の例外捕捉
			e.printStackTrace();
		}
	}

}
