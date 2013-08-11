package util;

public class StringUtil {


	public static boolean isBlank(String str) {
		if (str == null || str.length() <= 0) {
			return true;
		}
		if (str != null && str.trim().length() > 0) {
			return false;
		}
		return true;
	}



	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}

	/**
	 * 文字列が等しいか判断します。</br>
	 * 
	 * @param str 文字列
	 * @param anObject 比較オブジェクト
	 * @return 文字列が等しい場合はtrue
	 */
	public static boolean equals(String str, Object anObject) {
		if (str == null) {
			return str == anObject;
		} else {
			return str.equals(anObject);
		}
	}

}
