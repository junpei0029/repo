package todolist;

import io.ReadCSV;
import io.WriteCSV;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import util.StringUtil;

public class TodoList extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final TodoList TODO_LIST = new TodoList();

	private JTextField toDoInputField;
	private JTextField requiredTime;
	private JComboBox toDoComboBox;
	private JButton addButton;
	private JButton modifyButton;
	private JButton removeButton;
	private static final String TITLE = "TODOリスト";

	/**********************************************
	 * コンストラクタ
	 **********************************************/
	private TodoList() {

		final JFrame f = new JFrame(TITLE);
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.setSize(640, 640);
		f.setLocationRelativeTo(null);
		f.addWindowListener(new WindowHandler());


		JPanel inputPanel = getInputPanel();
		f.add(inputPanel, BorderLayout.NORTH);

		JPanel outputPanel = getOutputPanel();
		f.add(outputPanel, BorderLayout.CENTER);

		f.setVisible(true);

	}

	/**********************************************
	 * インプットパネル
	 **********************************************/
	private JPanel getInputPanel() {

		JPanel panel = new JPanel();
		final String[] COMBO_NAMES = { "重要：緊急", "重要：非緊急", "非重要：緊急", "非重要：非緊急" };
		// ToDo追加用テキストフィールドの生成
		toDoInputField = new JTextField();
		toDoInputField.addKeyListener(new inputKeyAdapter());
		toDoInputField.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				toDoInputField.getInputContext().setCompositionEnabled(false);
			}

			@Override
			public void focusGained(FocusEvent e) {
				toDoInputField.getInputContext().setCompositionEnabled(true);
			}
		});

		// 所要時間用テキストフィールド
		requiredTime = new JTextField();
		requiredTime.addKeyListener(new inputKeyAdapter());

		toDoComboBox = new JComboBox(COMBO_NAMES);
		toDoComboBox.addKeyListener(new inputKeyAdapter());

		requiredTime.setSize(5, toDoInputField.getHeight());

		// 各ボタンの生成
		JPanel buttonPanel = new JPanel();
		addButton = new JButton("追加");
		modifyButton = new JButton("編集");
		removeButton = new JButton("削除");

		// ボタンにリスナを設定
		addButton.addActionListener(new AddActionHandler());
		modifyButton.addActionListener(new ModifyActionHandler());
		removeButton.addActionListener(new RemoveActionHandler());

		addButton.addKeyListener(new ExamKeyAdapter());
		modifyButton.addKeyListener(new ExamKeyAdapter());
		removeButton.addKeyListener(new ExamKeyAdapter());

		buttonPanel.add(addButton);
		buttonPanel.add(modifyButton);
		buttonPanel.add(removeButton);

		panel.add(toDoInputField);
		panel.add(requiredTime);
		panel.add(toDoComboBox);
		panel.add(buttonPanel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));

		return panel;
	}

	/**********************************************
	 * アウトプットパネル
	 **********************************************/
	private JPanel getOutputPanel() {

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 2));

		// インスタンス生成
		StatePrimary s1 = Primary1List.getInstance();
		StatePrimary s2 = Primary2List.getInstance();
		StatePrimary s3 = Primary3List.getInstance();
		StatePrimary s4 = Primary4List.getInstance();

		// ハンドラセット
		setListHandler();

		// 各リスト処理
		setList(s1);
		setList(s2);
		setList(s3);
		setList(s4);

		JScrollPane listScrollPane1 = new JScrollPane(s1.getToDoList());
		JScrollPane listScrollPane2 = new JScrollPane(s2.getToDoList());
		JScrollPane listScrollPane3 = new JScrollPane(s3.getToDoList());
		JScrollPane listScrollPane4 = new JScrollPane(s4.getToDoList());

		panel.add(listScrollPane1);
		panel.add(listScrollPane2);
		panel.add(listScrollPane3);
		panel.add(listScrollPane4);

		return panel;
	}

	/**********************************************
	 * アクションハンドラ
	 **********************************************/
	/**
	 * 追加ボタンアクションのハンドラ
	 */
	private class AddActionHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			addListAction();
		}
	}

	/**
	 * 編集ボタンアクションのハンドラ
	 */
	private class ModifyActionHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			editListAction();
		}
	}

	/**
	 * 削除ボタンアクションのハンドラ
	 */
	private class RemoveActionHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			deleteListAction();
		}
	}

	/**
	 * TODOリスト選択アクションのハンドラ
	 */
	private class ToDoListSelectionHandler implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent arg0) {
			selectListAction();
		}
	}

	/**
	 * 入力欄用キーハンドラ
	 */
	private class inputKeyAdapter extends KeyAdapter {
		public void keyPressed(KeyEvent ke) {
			if (ke.getKeyCode() == KeyEvent.VK_E) {
				int mod = ke.getModifiersEx();
				if ((mod & InputEvent.CTRL_DOWN_MASK) != 0) {
					editListAction();
				}
			} else if (ke.getKeyCode() == KeyEvent.VK_A) {
				int mod = ke.getModifiersEx();
				if ((mod & InputEvent.CTRL_DOWN_MASK) != 0) {
					addListAction();
				}
			}
			/**
			 * CTRL + 数字(1,2,3,4) のハンドラ(フォーカス移動用)
			 */
			else if (ke.getKeyCode() == KeyEvent.VK_1) {
				int mod = ke.getModifiersEx();
				if ((mod & InputEvent.CTRL_DOWN_MASK) != 0) {
					moveListAction(Primary1List.getInstance());
				}
			} else if (ke.getKeyCode() == KeyEvent.VK_2) {
				int mod = ke.getModifiersEx();
				if ((mod & InputEvent.CTRL_DOWN_MASK) != 0) {
					moveListAction(Primary2List.getInstance());
				}
			} else if (ke.getKeyCode() == KeyEvent.VK_3) {
				int mod = ke.getModifiersEx();
				if ((mod & InputEvent.CTRL_DOWN_MASK) != 0) {
					moveListAction(Primary3List.getInstance());
				}
			} else if (ke.getKeyCode() == KeyEvent.VK_4) {
				int mod = ke.getModifiersEx();
				if ((mod & InputEvent.CTRL_DOWN_MASK) != 0) {
					moveListAction(Primary4List.getInstance());
				}
			}

		}
	}

	/**
	 * ボタン用キーハンドラ
	 */
	private class ExamKeyAdapter extends KeyAdapter {
		public void keyPressed(KeyEvent ke) {
			enterKeyAction(ke);
		}
	}

	/**
	 * リスト用キーハンドラ
	 */
	private class ListKeyAdapter extends KeyAdapter {
		public void keyPressed(KeyEvent ke) {
			/**
			 * jk のハンドラ(リスト内移動用)
			 * ctrl + jk ハンドラ(リスト上下移動用)
			 */
			if (ke.getKeyCode() == KeyEvent.VK_J) {
				int mod = ke.getModifiersEx();
				if ((mod & InputEvent.CTRL_DOWN_MASK) != 0) {
					moveListAction(getMoveTargetUD(false));
				} else {
					moveFocusUpDownAction(getFocusList(), false);
				}
			} else if (ke.getKeyCode() == KeyEvent.VK_K) {
				int mod = ke.getModifiersEx();
				if ((mod & InputEvent.CTRL_DOWN_MASK) != 0) {
					moveListAction(getMoveTargetUD(true));
				} else {
					moveFocusUpDownAction(getFocusList(), true);
				}
			}
			/**
			 * hl のハンドラ(リスト間左右移動用)
			 */
			else if (ke.getKeyCode() == KeyEvent.VK_H) {
				moveListAction(getMoveTargetRL(false));
			} else if (ke.getKeyCode() == KeyEvent.VK_L) {
				moveListAction(getMoveTargetRL(true));
			}
			/**
			 * デリートキーのハンドラ（項目削除用）
			 */
			else if (ke.getKeyCode() == KeyEvent.VK_DELETE) {
				deleteListAction();
			}
			/**
			 * エスケープキーのハンドラ（選択解除用）
			 */
			else if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
				escapeListAction();
			}
			/**
			 * iキーのハンドラ（フォーカス移動用）
			 */
			else if (ke.getKeyCode() == KeyEvent.VK_I) {
				moveFocusAction();
			}
			/**
			 * CTRL + 矢印 のハンドラ(リスト項目移動用)
			 */
			else if (ke.getKeyCode() == KeyEvent.VK_LEFT) {
				int mod = ke.getModifiersEx();
				if ((mod & InputEvent.CTRL_DOWN_MASK) != 0) {
					moveToLeftAction();
				}
			} else if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
				int mod = ke.getModifiersEx();
				if ((mod & InputEvent.CTRL_DOWN_MASK) != 0) {
					moveToRightAction();
				}
			} else if (ke.getKeyCode() == KeyEvent.VK_UP) {
				int mod = ke.getModifiersEx();
				if ((mod & InputEvent.CTRL_DOWN_MASK) != 0) {
					moveToUpAction();
				}
			} else if (ke.getKeyCode() == KeyEvent.VK_DOWN) {
				int mod = ke.getModifiersEx();
				if ((mod & InputEvent.CTRL_DOWN_MASK) != 0) {
					moveToDownAction();
				}
			}
			/**
			 * CTRL + 数字(1,2,3,4) のハンドラ(フォーカス移動用)
			 */
			else if (ke.getKeyCode() == KeyEvent.VK_1) {
				int mod = ke.getModifiersEx();
				if ((mod & InputEvent.CTRL_DOWN_MASK) != 0) {
					moveListAction(Primary1List.getInstance());
				}
			} else if (ke.getKeyCode() == KeyEvent.VK_2) {
				int mod = ke.getModifiersEx();
				if ((mod & InputEvent.CTRL_DOWN_MASK) != 0) {
					moveListAction(Primary2List.getInstance());
				}
			} else if (ke.getKeyCode() == KeyEvent.VK_3) {
				int mod = ke.getModifiersEx();
				if ((mod & InputEvent.CTRL_DOWN_MASK) != 0) {
					moveListAction(Primary3List.getInstance());
				}
			} else if (ke.getKeyCode() == KeyEvent.VK_4) {
				int mod = ke.getModifiersEx();
				if ((mod & InputEvent.CTRL_DOWN_MASK) != 0) {
					moveListAction(Primary4List.getInstance());
				}
			}
			/**
			 * CTRL + S のハンドラ(保存用)
			 */
			else if (ke.getKeyCode() == KeyEvent.VK_S) {
				int mod = ke.getModifiersEx();
				if ((mod & InputEvent.CTRL_DOWN_MASK) != 0) {
					saveListAction();
				}
			}
		}
	}

	/**
	 * ウィンドウハンドラ
	 */
	private class WindowHandler implements WindowListener {

		@Override
		public void windowOpened(WindowEvent e) {
			loadListAction();
		}

		@Override
		public void windowClosing(WindowEvent e) {
			// 同期保存
			save();
			System.exit(1);
		}

		@Override
		public void windowClosed(WindowEvent e) {
		}

		@Override
		public void windowIconified(WindowEvent e) {

		}

		@Override
		public void windowDeiconified(WindowEvent e) {

		}

		@Override
		public void windowActivated(WindowEvent e) {

		}

		@Override
		public void windowDeactivated(WindowEvent e) {
		}
	}

	/**********************************************
	 * アクション
	 **********************************************/

	/**
	 * 追加ボタンアクション
	 */
	private void addListAction() {
		int i = toDoComboBox.getSelectedIndex();
		StatePrimary s = judgePrimary(i);
		addListText(s);
	}

	/**
	 * 編集ボタンアクション
	 */
	private void editListAction() {
		StatePrimary s1 = Primary1List.getInstance();
		StatePrimary s2 = Primary2List.getInstance();
		StatePrimary s3 = Primary3List.getInstance();
		StatePrimary s4 = Primary4List.getInstance();

		if (s1.getToDoList().getSelectedIndices().length + s2.getToDoList().getSelectedIndices().length
				+ s3.getToDoList().getSelectedIndices().length + s4.getToDoList().getSelectedIndices().length != 1) {
			return;
		}
		if (s1.getToDoList().getSelectedIndices().length == 1) {
			editListText(s1);
		} else if (s2.getToDoList().getSelectedIndices().length == 1) {
			editListText(s2);
		} else if (s3.getToDoList().getSelectedIndices().length == 1) {
			editListText(s3);
		} else if (s4.getToDoList().getSelectedIndices().length == 1) {
			editListText(s4);
		}
	}

	/**
	 * 削除ボタンアクション
	 */
	private void deleteListAction() {
		StatePrimary s1 = Primary1List.getInstance();
		StatePrimary s2 = Primary2List.getInstance();
		StatePrimary s3 = Primary3List.getInstance();
		StatePrimary s4 = Primary4List.getInstance();

		if (s1.getToDoList().getSelectedIndices().length >= 1) {
			deleteListElement(s1);
		}
		if (s2.getToDoList().getSelectedIndices().length >= 1) {
			deleteListElement(s2);
		}
		if (s3.getToDoList().getSelectedIndices().length >= 1) {
			deleteListElement(s3);
		}
		if (s4.getToDoList().getSelectedIndices().length >= 1) {
			deleteListElement(s4);
		}
	}

	/**
	 * エンターキーアクション
	 */
	private void enterKeyAction(KeyEvent ke) {
		if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
			if (ke.getSource() == addButton) {
				addListAction();
			}
			if (ke.getSource() == modifyButton) {
				editListAction();
			}
			if (ke.getSource() == removeButton) {
				deleteListAction();
			}
		}
	}

	/**
	 * リスト選択アクション
	 */
	private void selectListAction() {
		StatePrimary s1 = Primary1List.getInstance();
		StatePrimary s2 = Primary2List.getInstance();
		StatePrimary s3 = Primary3List.getInstance();
		StatePrimary s4 = Primary4List.getInstance();

		if (s4.getToDoList().getSelectedIndices().length == 1) {
			updateText(s4);
		}
		if (s3.getToDoList().getSelectedIndices().length == 1) {
			updateText(s3);
		}
		if (s2.getToDoList().getSelectedIndices().length == 1) {
			updateText(s2);
		}
		if (s1.getToDoList().getSelectedIndices().length == 1) {
			updateText(s1);
		}
	}

	/**
	 * リスト選択解除アクション
	 */
	private void escapeListAction() {
		StatePrimary s1 = Primary1List.getInstance();
		StatePrimary s2 = Primary2List.getInstance();
		StatePrimary s3 = Primary3List.getInstance();
		StatePrimary s4 = Primary4List.getInstance();

		s1.getToDoList().setSelectedIndices(new int[0]);
		s2.getToDoList().setSelectedIndices(new int[0]);
		s3.getToDoList().setSelectedIndices(new int[0]);
		s4.getToDoList().setSelectedIndices(new int[0]);

	}

	/**
	 * ロードアクション
	 */
	private void loadListAction() {
		StatePrimary s1 = Primary1List.getInstance();
		StatePrimary s2 = Primary2List.getInstance();
		StatePrimary s3 = Primary3List.getInstance();
		StatePrimary s4 = Primary4List.getInstance();

		ReadCSV.readCSV(s1);
		ReadCSV.readCSV(s2);
		ReadCSV.readCSV(s3);
		ReadCSV.readCSV(s4);
	}

	/**
	 * 保存アクション
	 */
	private void saveListAction() {

		// Executorオブジェクトの生成
		Executor executor = Executors.newSingleThreadExecutor();

		// タスクの実行
		executor.execute(new AsyncSave());
	}

	/**
	 * 保存処理
	 */
	private void save() {
		List<Map<String, String>> args = new ArrayList<Map<String, String>>();
		setList(Primary1List.getInstance(), args);
		setList(Primary2List.getInstance(), args);
		setList(Primary3List.getInstance(), args);
		setList(Primary4List.getInstance(), args);

		WriteCSV.createCSV(args);
	}

	/**
	 * 非同期保存用クラス
	 */
	class AsyncSave implements Runnable {
		@Override
		public void run() {

			// 保存処理
			save();
			try {
				Thread.sleep(500);
				JLabel label = new JLabel("保存完了");
				JOptionPane.showMessageDialog(label, label);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 移動アクション(右から左へ)
	 */
	private void moveToLeftAction() {
		StatePrimary s1 = Primary1List.getInstance();
		StatePrimary s2 = Primary2List.getInstance();
		StatePrimary s3 = Primary3List.getInstance();
		StatePrimary s4 = Primary4List.getInstance();

		if (s2.getToDoList().getSelectedIndices().length == 1) {
			Todo o = (Todo) s2.getToDoListModel().getElementAt(s2.getToDoList().getSelectedIndex());
			Todo todo = new Todo(o.getTodo(), o.getTime());
			deleteListElement(s2);
			s1.addListElement(todo);
		}
		if (s4.getToDoList().getSelectedIndices().length == 1) {
			Todo o = (Todo) s4.getToDoListModel().getElementAt(s4.getToDoList().getSelectedIndex());
			Todo todo = new Todo(o.getTodo(), o.getTime());
			deleteListElement(s4);
			s3.addListElement(todo);

		}
	}

	/**
	 * 移動アクション(左から右へ)
	 */
	private void moveToRightAction() {
		StatePrimary s1 = Primary1List.getInstance();
		StatePrimary s2 = Primary2List.getInstance();
		StatePrimary s3 = Primary3List.getInstance();
		StatePrimary s4 = Primary4List.getInstance();

		if (s1.getToDoList().getSelectedIndices().length == 1) {
			Todo o = (Todo) s1.getToDoListModel().getElementAt(s1.getToDoList().getSelectedIndex());
			Todo todo = new Todo(o.getTodo(), o.getTime());
			deleteListElement(s1);
			s2.addListElement(todo);
		}
		if (s3.getToDoList().getSelectedIndices().length == 1) {
			Todo o = (Todo) s3.getToDoListModel().getElementAt(s3.getToDoList().getSelectedIndex());
			Todo todo = new Todo(o.getTodo(), o.getTime());
			deleteListElement(s3);
			s4.addListElement(todo);

		}
	}

	/**
	 * 移動アクション(上から下へ)
	 */
	private void moveToDownAction() {
		StatePrimary s1 = Primary1List.getInstance();
		StatePrimary s2 = Primary2List.getInstance();
		StatePrimary s3 = Primary3List.getInstance();
		StatePrimary s4 = Primary4List.getInstance();

		if (s1.getToDoList().getSelectedIndices().length == 1) {
			Todo o = (Todo) s1.getToDoListModel().getElementAt(s1.getToDoList().getSelectedIndex());
			Todo todo = new Todo(o.getTodo(), o.getTime());
			deleteListElement(s1);
			s3.addListElement(todo);
		}
		if (s2.getToDoList().getSelectedIndices().length == 1) {
			Todo o = (Todo) s2.getToDoListModel().getElementAt(s2.getToDoList().getSelectedIndex());
			Todo todo = new Todo(o.getTodo(), o.getTime());
			deleteListElement(s2);
			s4.addListElement(todo);
		}
	}

	/**
	 * 移動アクション(下から上へ)
	 */
	private void moveToUpAction() {
		StatePrimary s1 = Primary1List.getInstance();
		StatePrimary s2 = Primary2List.getInstance();
		StatePrimary s3 = Primary3List.getInstance();
		StatePrimary s4 = Primary4List.getInstance();

		if (s3.getToDoList().getSelectedIndices().length == 1) {
			Todo o = (Todo) s3.getToDoListModel().getElementAt(s3.getToDoList().getSelectedIndex());
			Todo todo = new Todo(o.getTodo(), o.getTime());
			deleteListElement(s3);
			s1.addListElement(todo);
		}
		if (s4.getToDoList().getSelectedIndices().length == 1) {
			Todo o = (Todo) s4.getToDoListModel().getElementAt(s4.getToDoList().getSelectedIndex());
			Todo todo = new Todo(o.getTodo(), o.getTime());
			deleteListElement(s4);
			s2.addListElement(todo);
		}
	}

	/**
	 * フォーカス移動アクション(入力欄にフォーカス移動)
	 */
	private void moveFocusAction() {
		toDoInputField.requestFocusInWindow();
	}

	/**
	 * リスト移動アクション
	 */
	private void moveListAction(StatePrimary s) {
		if (s == null) {
			return;
		}
		Primary1List.getInstance().getToDoList().setSelectedIndices(new int[0]);
		Primary2List.getInstance().getToDoList().setSelectedIndices(new int[0]);
		Primary3List.getInstance().getToDoList().setSelectedIndices(new int[0]);
		Primary4List.getInstance().getToDoList().setSelectedIndices(new int[0]);

		s.getToDoList().setSelectedIndex(0);
		s.getToDoList().requestFocusInWindow();
	}

	/**
	 * フォーカス左右移動アクション
	 */
	private StatePrimary getMoveTargetRL(boolean toRightFlag) {

		StatePrimary s = getFocusList();
		if (s == null) {
			return null;
		}
		StatePrimary target = null;

		if (toRightFlag) {
			if (StringUtil.equals(s.getKbn(), "01")) {
				target = Primary2List.getInstance();
			} else if (StringUtil.equals(s.getKbn(), "03")) {
				target = Primary4List.getInstance();
			}
		} else {
			if (StringUtil.equals(s.getKbn(), "02")) {
				target = Primary1List.getInstance();
			} else if (StringUtil.equals(s.getKbn(), "04")) {
				target = Primary3List.getInstance();
			}

		}

		return target;
	}

	/**
	 * フォーカス上下移動アクション
	 */
	private StatePrimary getMoveTargetUD(boolean toupFlag) {

		StatePrimary s = getFocusList();
		if (s == null) {
			return null;
		}
		StatePrimary target = null;

		if (toupFlag) {
			if (StringUtil.equals(s.getKbn(), "03")) {
				target = Primary1List.getInstance();
			} else if (StringUtil.equals(s.getKbn(), "04")) {
				target = Primary2List.getInstance();
			}
		} else {
			if (StringUtil.equals(s.getKbn(), "01")) {
				target = Primary3List.getInstance();
			} else if (StringUtil.equals(s.getKbn(), "02")) {
				target = Primary4List.getInstance();
			}
		}

		return target;
	}


	/**
	 * フォーカス移動アクション(リスト内フォーカス移動)
	 */
	private void moveFocusUpDownAction(StatePrimary s, boolean upDownFlag) {

		if (s == null) {
			return;
		}

		if (s.getToDoList().hasFocus()) {
			int nowFocusIdx = s.getToDoList().getSelectedIndex();
			if (upDownFlag && nowFocusIdx != 0) {
				s.getToDoList().setSelectedIndex(nowFocusIdx - 1);
			} else if (upDownFlag == false) {
				s.getToDoList().setSelectedIndex(nowFocusIdx + 1);
			}
			s.getToDoList().requestFocusInWindow();
		}
	}

	/**
	 * フォーカスが存在するリストを取得
	 */
	private StatePrimary getFocusList() {

		StatePrimary s1 = Primary1List.getInstance();
		StatePrimary s2 = Primary2List.getInstance();
		StatePrimary s3 = Primary3List.getInstance();
		StatePrimary s4 = Primary4List.getInstance();

		StatePrimary ret = null;

		if (s1.getToDoList().hasFocus()) {
			ret = s1;
		} else if (s2.getToDoList().hasFocus()) {
			ret = s2;
		} else if (s3.getToDoList().hasFocus()) {
			ret = s3;
		} else if (s4.getToDoList().hasFocus()) {
			ret = s4;
		}
		return ret;
	}


	/**********************************************
	 * 補助メソッド
	 **********************************************/
	private void setList(StatePrimary s) {
		JList ret = s.getToDoList();
		ret.setBorder(s.getTitle());
		// ret.setBackground(s.getColor());
	}

	private void setListHandler() {

		JList s1l = Primary1List.getInstance().getToDoList();
		JList s2l = Primary2List.getInstance().getToDoList();
		JList s3l = Primary3List.getInstance().getToDoList();
		JList s4l = Primary4List.getInstance().getToDoList();

		s1l.addListSelectionListener(new ToDoListSelectionHandler());
		s2l.addListSelectionListener(new ToDoListSelectionHandler());
		s3l.addListSelectionListener(new ToDoListSelectionHandler());
		s4l.addListSelectionListener(new ToDoListSelectionHandler());

		s1l.addKeyListener(new ListKeyAdapter());
		s2l.addKeyListener(new ListKeyAdapter());
		s3l.addKeyListener(new ListKeyAdapter());
		s4l.addKeyListener(new ListKeyAdapter());

	}

	private StatePrimary judgePrimary(int selectNum) {
		StatePrimary s = null;
		if (selectNum == 0) {
			s = Primary1List.getInstance();
		} else if (selectNum == 1) {
			s = Primary2List.getInstance();
		} else if (selectNum == 2) {
			s = Primary3List.getInstance();
		} else if (selectNum == 3) {
			s = Primary4List.getInstance();
		}

		return s;
	}

	public static TodoList getInstance() {
		return TODO_LIST;
	}

	private List<Map<String, String>> setList(StatePrimary s, List<Map<String, String>> mList) {

		ListModel list = s.getToDoListModel();
		for (int i = 0; i < list.getSize(); i++) {
			Map<String, String> m = new HashMap<String, String>();
			m.put("kbn", s.getKbn());
			m.put("content", ((Todo) list.getElementAt(i)).getTodo());
			m.put("time", ((Todo) list.getElementAt(i)).getTime());
			mList.add(m);
		}
		return mList;
	}

	/**********************************************
	 * 委譲メソッド
	 **********************************************/

	private void updateText(StatePrimary s) {
		toDoInputField.setText(((Todo) s.getToDoList().getSelectedValue()).getTodo());
		requiredTime.setText(((Todo) s.getToDoList().getSelectedValue()).getTime());
	}

	private void addListText(StatePrimary s) {
		Todo todo = new Todo(toDoInputField.getText(), requiredTime.getText());
		s.addListElement(todo);
	}

	private void editListText(StatePrimary s) {
		Todo todo = new Todo(toDoInputField.getText(), requiredTime.getText());
		s.editListElement(s.getToDoList().getSelectedIndex(), todo);
	}

	private void deleteListElement(StatePrimary s) {
		s.deleteListElement(s.getToDoList().getSelectedIndex());
	}

}
