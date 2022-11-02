import java.sql.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.function.Function;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class COMPANY {
	static final String url = "jdbc:mysql://localhost:3306/COMPANY?serverTimeZone=UTC";
	static final String user = "root";
	static final String password = "root1234";
	
	static final String searchRange[] = {"전체", "부서", "성별", "연봉", "생일", "부하직원"};
	static final JComboBox searchRangeBox = new JComboBox<String>(searchRange);
	static final JComboBox departmentBox = new JComboBox<String>();
	static final JComboBox sexBox = new JComboBox<String>();
	static final JTextField salaryBox = new JTextField();
	static final Integer bdateRange[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
	static final JComboBox bdateBox = new JComboBox<Integer>(bdateRange);
	static final JTextField subordinateBox = new JTextField();
	static final JLabel selectedEmployeeLabel = new JLabel("선택한 직원:");
	static final JLabel selectedEmployeeCountLabel = new JLabel("인원수:");
	static final DefaultTableModel currentData = new DefaultTableModel();
	static final Map<String, String> selectedEmployee = new HashMap<String, String>();
	
	
	static final ArrayList<JCheckBox> searchItemBoxes = new ArrayList<JCheckBox>();
	static final JTable resultTable = new JTable(new DefaultTableModel());
	
	public static void main(String[] args) {
		setFrame();
	}
	
	public static void setFrame() {
		JFrame frame = new JFrame();
		frame.setSize(1000,800);
		
		Container container = frame.getContentPane();
		
		resultTable.setVisible(false);
		JScrollPane scrollPane = new JScrollPane(resultTable);
		scrollPane.setBounds(50, 65, 900, 550);
		container.add(scrollPane);
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		
		setSearchRanges(panel);
		setSearchItems(panel);
		setSearchButton(panel);
		setResultBar(panel);
		
		panel.setVisible(true);
		container.add(panel);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public static void setSearchRanges(JPanel panel) {
		JLabel searchRangeLabel = new JLabel("검색 범위");
		searchRangeLabel.setBounds(7, 5, 50, 25);
		panel.add(searchRangeLabel);
		
		searchRangeBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String s = (String) searchRangeBox.getSelectedItem();
				departmentBox.setVisible(false);
				sexBox.setVisible(false);
				salaryBox.setText("");
				salaryBox.setVisible(false);
				bdateBox.setVisible(false);
				subordinateBox.setText("");
				subordinateBox.setVisible(false);
				if (s == searchRange[1]) {
					String sql = "Select dname from DEPARTMENT;";
					runSelectSQL(sql, (rs) -> {
						try {
							DefaultComboBoxModel model = new DefaultComboBoxModel<String>();
							while (rs.next()) {
								model.addElement(rs.getString("dname"));
							}
							departmentBox.setModel(model);
							departmentBox.setVisible(true);
						} catch (SQLException e1) {
							System.out.println("select dname error " + e1.getLocalizedMessage());
						}
						return null;
					});
				} else if (s == searchRange[2]) {
					String sql = "Select distinct sex from EMPLOYEE;";
					runSelectSQL(sql, (rs) -> {
						try {
							DefaultComboBoxModel model = new DefaultComboBoxModel<String>();
							while (rs.next()) {
								model.addElement(rs.getString("sex"));
							}
							sexBox.setModel(model);
							sexBox.setVisible(true);
						} catch (SQLException e1) {
							System.out.println("select sex error " + e1.getLocalizedMessage());
						}
						return null;
					});
				} else if (s == searchRange[3]) {
					salaryBox.setVisible(true);
				} else if (s == searchRange[4]) {
					bdateBox.setVisible(true);
				} else if (s == searchRange[5]) {
					subordinateBox.setVisible(true);
				}
			}
		});
		
		searchRangeBox.setBounds(60, 5, 100, 30);
		panel.add(searchRangeBox);
		
		departmentBox.setBounds(170, 5, 200, 30);
		departmentBox.setVisible(false);
		panel.add(departmentBox);
		sexBox.setBounds(170, 5, 200, 30);
		sexBox.setVisible(false);
		panel.add(sexBox);
		salaryBox.setBounds(170, 5, 200, 30);
		salaryBox.setVisible(false);
		panel.add(salaryBox);
		bdateBox.setBounds(170, 5, 200, 30);
		bdateBox.setVisible(false);
		panel.add(bdateBox);
		subordinateBox.setBounds(170, 5, 200, 30);
		subordinateBox.setVisible(false);
		panel.add(subordinateBox);
	}
	
	public static void setSearchItems(JPanel panel) {
		JLabel searchItemLabel = new JLabel("검색 항목");
		searchItemLabel.setBounds(7, 35, 50, 25);
		panel.add(searchItemLabel);
		
		JCheckBox name = new JCheckBox("Name");
		name.setBounds(60, 35, 90, 25);
		name.setSelected(true);
		panel.add(name);
		searchItemBoxes.add(name);
		
		JCheckBox ssn = new JCheckBox("Ssn");
		ssn.setBounds(140, 35, 60, 25);
		ssn.setSelected(true);
		panel.add(ssn);
		searchItemBoxes.add(ssn);
		
		JCheckBox bdate = new JCheckBox("Bdate");
		bdate.setBounds(200, 35, 90, 25);
		bdate.setSelected(true);
		panel.add(bdate);
		searchItemBoxes.add(bdate);
		
		JCheckBox address = new JCheckBox("Address");
		address.setBounds(280, 35, 90, 25);
		address.setSelected(true);
		panel.add(address);
		searchItemBoxes.add(address);
		
		JCheckBox sex = new JCheckBox("Sex");
		sex.setBounds(370, 35, 70, 25);
		sex.setSelected(true);
		panel.add(sex);
		searchItemBoxes.add(sex);
		
		JCheckBox salary = new JCheckBox("Salary");
		salary.setBounds(440, 35, 90, 25);
		salary.setSelected(true);
		panel.add(salary);
		searchItemBoxes.add(salary);
		
		JCheckBox supervisor = new JCheckBox("Supervisor");
		supervisor.setBounds(530, 35, 110, 25);
		supervisor.setSelected(true);
		panel.add(supervisor);
		searchItemBoxes.add(supervisor);
		
		JCheckBox department = new JCheckBox("Department");
		department.setBounds(630, 35, 110, 25);
		department.setSelected(true);
		panel.add(department);
		searchItemBoxes.add(department);
	}
	
	public static void setSearchButton(JPanel panel) {
		JButton searchButton = new JButton("검색");
		searchButton.setBounds(750, 35, 70, 25);
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				search();
			}
		});
		panel.add(searchButton);
	}
	
	public static void setResultBar(JPanel panel) {
		selectedEmployeeLabel.setBounds(7, 650, 800, 30);
		selectedEmployeeLabel.setVisible(true);
		panel.add(selectedEmployeeLabel);
		
		selectedEmployeeCountLabel.setBounds(7, 680, 100, 30);
		selectedEmployeeCountLabel.setVisible(true);
		panel.add(selectedEmployeeCountLabel);
		
		JButton deleteButton = new JButton("선택한 데이터 삭제");
		deleteButton.setBounds(800, 680, 150, 30);
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sql = "Delete from EMPLOYEE where ssn IN (";
				for (String strKey: selectedEmployee.keySet()) {
					sql = sql + "'" + strKey + "',";
				} 
				sql = sql.substring(0, sql.length()-1) + ");";
				runSQL(sql, (rs) -> {
					selectedEmployee.clear();
					selectedEmployeeLabel.setText("선택한 직원: ");
					search();
					return null;
				});
			}
		});
		
		
		panel.add(deleteButton);
	}
	
	public static Void showResult(ArrayList<String> column, ResultSet result) {
		try {
			DefaultTableModel model = new DefaultTableModel(column.toArray(), 0) {
				@Override
				public void setValueAt(Object value, int row, int col) {
					super.setValueAt(value, row, col);
				    if (col == 0) {
				    	String ssn = (String) currentData.getValueAt(row, currentData.getColumnCount()-2);
				    	String name = (String) currentData.getValueAt(row, currentData.getColumnCount()-1);
				    	if ((Boolean) value) {
				    		selectedEmployee.put(ssn, name);
				    	} else {
				    		selectedEmployee.remove(ssn);
				    	}
				    	String str = "선택한 직원: ";
				    	for( String strKey : selectedEmployee.keySet() ){
				    		String val = selectedEmployee.get(strKey);
				    		str = str + val + ", ";
				    	}
				    	selectedEmployeeLabel.setText(str.substring(0, str.length()-2));
				    }
				}
			};
			column.add("pk");
			column.add("pkName");
			currentData.setColumnIdentifiers(column.toArray());
			currentData.setRowCount(0);
			while (result.next()) {
				Object[] data = new Object[column.size() - 2];
				Object[] pkData = new Object[column.size()];
				data[0] = false;
				String fname = result.getString("eFname");
				String minit = result.getString("eMinit");
				String lname = result.getString("eLname");
				String name = fname+" "+minit+" "+lname;
				for(int i = 1; i < column.size()-2; i++) {
					if (column.get(i) == "Name") {
						data[i] = name;
					} else if (column.get(i) == "Supervisor") {
						if (result.getString("sFname") == null) {
							data[i] = "";
						} else {
							String sfname = result.getString("sFname");
							String sminit = result.getString("sMinit");
							String slname = result.getString("sLname");
							data[i] = sfname+" "+sminit+" "+slname;
						}
					} else {
						data[i] = result.getString(column.get(i));				
					}
					pkData[i] = data[i];
				}
				pkData[column.size()-2] = result.getString("Ssn");
				pkData[column.size()-1] = name;
				model.addRow(data);
				currentData.addRow(pkData);
			}
			resultTable.setModel(model);
			
			resultTable.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
				@Override
				public Component getTableCellRendererComponent(JTable table,Object value, boolean isSelected,boolean hasFocus, int row,int column){
					JCheckBox check = new JCheckBox();
					check.setSelected((Boolean) value);
					return check;
				}
			});
			resultTable.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox()));
			resultTable.setVisible(true);
			selectedEmployeeCountLabel.setText("인원수: "+model.getRowCount());
		} catch (SQLException e) {
			System.out.println("showResult error " + e.getLocalizedMessage());
		}
		return null;
	}
	
	public static void runSelectSQL(String sql, Function <ResultSet, Void> completion) {
		try (Connection conn = DriverManager.getConnection(url, user, password)) {
			System.out.println("정상적으로 연결되었습니다");
			try (Statement stmt = conn.createStatement()) {
				System.out.println(sql);
				try (ResultSet rs = stmt.executeQuery(sql)) {
					completion.apply(rs);
				}
			}
		} catch (SQLException e) {
			System.out.println("runSQL error " + e.getLocalizedMessage());
		}
	}
	
	public static void runSQL(String sql, Function <Void, Void> completion) {
		try (Connection conn = DriverManager.getConnection(url, user, password)) {
			System.out.println("정상적으로 연결되었습니다");
			try (Statement stmt = conn.prepareStatement(sql)) {
				System.out.println(sql);
				int rs = stmt.executeUpdate(sql);
				if (rs == 1) {
					completion.apply(null);
				}
			}
		} catch (SQLException e) {
			System.out.println("runSQL error " + e.getLocalizedMessage());
		}
	}
	
	public static void search() {
		String sql = "Select e.Ssn, e.Fname as eFname,e.Minit as eMinit,e.Lname as eLname,";
		ArrayList<String> column = new ArrayList<String>();
		column.add("선택");
		
		for(JCheckBox item: searchItemBoxes) {
			if (item.isSelected() == false) {
				continue;
			} else if (item.getText() == "Ssn" || item.getText() == "Name") {
				column.add(item.getText());
				continue;
			} else if (item.getText() == "Supervisor") {
				sql = sql + "s.Fname as sFname,s.Minit as sMinit,s.Lname as sLname,";
			} else if (item.getText() == "Department") {
				sql = sql + "d.Dname as Department,";
			} else {
				sql = sql + "e."+item.getText() + ",";
			}
			column.add(item.getText());
		}
		
		sql = sql.substring(0, sql.length() - 1) + " from EMPLOYEE e left join EMPLOYEE s ON e.Super_ssn = s.Ssn join DEPARTMENT d ON e.Dno = d.Dnumber ";
		
		Integer searchRangeIdx = searchRangeBox.getSelectedIndex();
		if (searchRangeIdx == 1) {
			sql = sql + "where d.Dname = '" + (String) departmentBox.getSelectedItem() +"'";
		} else if (searchRangeIdx == 2) {
			sql = sql + "where e.Sex = '" + (String) sexBox.getSelectedItem() + "'";
		} else if (searchRangeIdx == 3) {
			try {
				Integer salary = Integer.parseInt(salaryBox.getText());
				sql = sql + "where e.Salary > " + salary;
			} catch (NumberFormatException e1) {
				JOptionPane.showMessageDialog(null, "숫자를 입력해주세요");
			}
		} else if (searchRangeIdx == 4) {
			Integer bmonth = (Integer) bdateBox.getSelectedItem();
			sql = sql + "where MONTH(e.bdate) = " + bmonth;
		} else if (searchRangeIdx == 5) {
			String[] name = ((String) subordinateBox.getText()).split(" ");
			if (name.length == 3) {
				String fname = name[0];
				String minit = name[1];
				String lname = name[2];
				sql = sql + "where s.fname = '" + fname + "' AND s.minit = '" + minit + "' AND s.lname = '" + lname + "'";
			} else {
				JOptionPane.showMessageDialog(null, "이름을 정상적으로 입력해주세요\n(ex Jennifer S Wallace");
			}
		}
		runSelectSQL(sql+";", rs -> showResult(column, rs));
	}
}