package todolist;

import static org.junit.Assert.assertSame;

import org.junit.Test;

public class TodoTest {

	@Test
	public void test() {
		Todo todo = new Todo("test", "130");
		assertSame("test", todo.getTodo());
		assertSame("130", todo.getTime());
		// assertSame(todo.getTodo() + " ： " + todo.getTime(), todo.toString());

	}
}
