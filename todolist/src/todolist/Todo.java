package todolist;

import util.StringUtil;

public class Todo {

	private String todo;

	private String time;

	public Todo(String todo) {
		this(todo, null);
	}

	public Todo(String todo, String time) {
		this.todo = todo;
		this.time = time;
	}

	public String getTodo() {
		return todo;
	}

	public void setTodo(String todo) {
		this.todo = todo;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public String toString() {
		// TODO 自動生成されたメソッド・スタブ
		if (StringUtil.isNotBlank(time)) {
			return todo + " ： " + time;
		}
		return todo;
	}
}
