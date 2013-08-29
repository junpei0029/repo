package todolist;

import java.util.Stack;

public class UserOperationStack {

	private Stack<UserOperation> stack = new Stack<UserOperation>();
	private static UserOperationStack userOperationStack = new UserOperationStack();

	private UserOperationStack() {

	}

	public UserOperation popUserOperation() {
		if (stack.isEmpty()) {
			return null;
		}
		return stack.pop();
	}

	public void pushUserOperation(UserOperation item) {
		stack.push(item);
	}

	/**
	 * @return userOperationStack
	 */
	public static UserOperationStack getUserOperationStack() {
		return userOperationStack;
	}
}
