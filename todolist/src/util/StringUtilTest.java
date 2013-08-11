package util;

import static org.junit.Assert.assertSame;

import org.junit.Test;

public class StringUtilTest {

	@Test
	public void testIsBlank() {
		assertSame(true, StringUtil.isBlank(null));
		assertSame(true, StringUtil.isBlank(""));
		assertSame(true, StringUtil.isBlank("    "));
		assertSame(false, StringUtil.isBlank("　　　"));
		assertSame(false, StringUtil.isBlank("abc"));
		assertSame(false, StringUtil.isBlank(" abc "));
		assertSame(false, StringUtil.isBlank("null"));
		assertSame(false, StringUtil.isBlank("　null　"));

	}
}
