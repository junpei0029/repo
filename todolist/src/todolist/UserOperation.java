package todolist;

public class UserOperation {

	private PrimaryList primary;

	private int userOpe;// {1:Add,2:Edit,3:Delete}

	private int selectedIndex;

	private Todo todo;

	public UserOperation(PrimaryList primary, int userOpe, int selectedIndex, Todo todo) {
		this.primary = primary;
		this.userOpe = userOpe;
		this.selectedIndex = selectedIndex;
		this.todo = todo;
	}

	/**
	 * @return primary
	 */
	public PrimaryList getPrimary() {
		return primary;
	}

	/**
	 * @return userOpe
	 */
	public int getUserOpe() {
		return userOpe;
	}

	/**
	 * @param primary セットする primary
	 */
	public void setPrimary(PrimaryList primary) {
		this.primary = primary;
	}

	/**
	 * @param userOpe セットする userOpe
	 */
	public void setUserOpe(int userOpe) {
		this.userOpe = userOpe;
	}

	/**
	 * @return selectedIndex
	 */
	public int getSelectedIndex() {
		return selectedIndex;
	}

	/**
	 * @param selectedIndex セットする selectedIndex
	 */
	public void setSelectedIndex(int selectedIndex) {
		this.selectedIndex = selectedIndex;
	}

	/**
	 * @return todo
	 */
	public Todo getTodo() {
		return todo;
	}

	/**
	 * @param todo セットする todo
	 */
	public void setTodo(Todo todo) {
		this.todo = todo;
	}

}
