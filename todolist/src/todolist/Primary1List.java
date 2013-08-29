package todolist;

import java.awt.Color;

import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.border.TitledBorder;

public class Primary1List implements PrimaryList {

	private static final StatePrimary PRIMARY_LIST = new Primary1List();

	private JList toDoList;
	private TodoListModel toDoListModel;

	private Primary1List() {
		toDoListModel = new TodoListModel();
		toDoList = new JList(toDoListModel);
	}

	@Override
	public TitledBorder getTitle() {
		return new TitledBorder("重要度高　緊急度高");
	}

	@Override
	public Color getColor() {
		return Color.red;
	}

	@Override
	public void addListElement(Todo todo) {
		toDoListModel.addElement(todo);
	}

	@Override
	public void editListElement(int index, Todo todo) {
		// TODO 自動生成されたメソッド・スタブ
		toDoListModel.set(index, todo);

	}

	@Override
	public void deleteListElement(int index) {
		// TODO 自動生成されたメソッド・スタブ
		toDoListModel.removeElementAt(index);
	}

	public static StatePrimary getInstance() {
		return PRIMARY_LIST;
	}

	@Override
	public String getKbn() {
		// TODO 自動生成されたメソッド・スタブ
		return "01";
	}

	@Override
	public JList getToDoList() {
		// TODO 自動生成されたメソッド・スタブ
		return toDoList;
	}

	@Override
	public ListModel getToDoListModel() {
		// TODO 自動生成されたメソッド・スタブ
		return toDoListModel;
	}

}
