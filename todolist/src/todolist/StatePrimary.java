package todolist;

import java.awt.Color;

import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.border.TitledBorder;


public interface StatePrimary {

	JList getToDoList();

	ListModel getToDoListModel();

	TitledBorder getTitle();

	Color getColor();

	String getKbn();

	void addListElement(Todo todo);

	void editListElement(int index, Todo todo);

	void deleteListElement(int index);

}
