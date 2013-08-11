package util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil {

	private static final String TODO_LIST_PROPERTY = "todolist.properties";

	public static Properties getProp() {
		final Properties prop = new Properties();
		InputStream inStream;
		try {
			inStream = PropertyUtil.class.getClassLoader().getResourceAsStream(TODO_LIST_PROPERTY);

			if (inStream == null) {
				return null;
			}

			prop.load(inStream);
		} catch (FileNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		return prop;
	}

	public static String getCsvfilePath() {
		final Properties prop = getProp();
		if (prop == null) {
			return "";
		}
		final String csfFilePath = prop.getProperty("csvFilePath");
		return csfFilePath;
	}

}
