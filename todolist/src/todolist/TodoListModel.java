package todolist;

import java.util.ArrayList;

import javax.swing.AbstractListModel;

public class TodoListModel extends AbstractListModel {

	private static final long serialVersionUID = 1L;

	private ArrayList<Todo> delegate = new ArrayList<Todo>();

	public TodoListModel() {
	}

	@Override
	public int getSize() {
		// TODO 自動生成されたメソッド・スタブ
		return delegate.size();
	}

	@Override
	public Todo getElementAt(int index) {
		// TODO 自動生成されたメソッド・スタブ
		return delegate.get(index);
	}

	public void addElement(Todo obj) {
		int index = delegate.size();
		delegate.add(obj);
		fireIntervalAdded(this, index, index);
	}

	public void removeElementAt(int index) {
		delegate.remove(index);
		fireIntervalRemoved(this, index, index);
	}

	public Object set(int index, Todo element) {
		Object rv = delegate.get(index);
		delegate.set(index, (Todo) element);
		fireContentsChanged(this, index, index);
		return rv;
	}


}
