import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.JComboBox;
import java.awt.Component;

public class ResturantSpinnerFrame extends JFrame {

	private JPanel contentPane;
	private static final long serialVersionUID = 4624654308139813674L;
	private static ArrayList<String> resturantGroupList;
	private static ArrayList<String> resturantStyleList;
	private static ArrayList<Resturant> resturantList;
	private static String selectedGroup;

	/**
	 * Create the frame.
	 */
	public ResturantSpinnerFrame() {
		
		resturantGroupList = new ArrayList<String>();
		resturantStyleList = new ArrayList<String>();
		resturantList = new ArrayList<Resturant>();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		accumulateResturantGroupList();
		accumulateResturantStylelist();
		accumulateResturantList();
		
		JButton btnAdminPanel = new JButton("Admin Panel");
		btnAdminPanel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AdminPasswordFrame adminPassFrame = new AdminPasswordFrame(resturantStyleList.toArray(new String[resturantStyleList.size()]), resturantGroupList.toArray(new String[resturantGroupList.size()]));
				//ResturantSpinnerAdminFrame adminFrame = new ResturantSpinnerAdminFrame(resturantStyleList.toArray(new String[resturantStyleList.size()]), resturantGroupList.toArray(new String[resturantGroupList.size()]));
				adminPassFrame.setVisible(true);
			}
		});
		btnAdminPanel.setBounds(342, 11, 132, 23);
		contentPane.add(btnAdminPanel);
		
		JLabel lblResultLabel = new JLabel("",SwingConstants.CENTER);
		lblResultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblResultLabel.setBounds(132, 171, 193, 59);
		contentPane.add(lblResultLabel);
		
		JButton btnSpin = new JButton("Spin");
		btnSpin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				spinWheel(lblResultLabel);
			}
		});
		btnSpin.setBounds(188, 304, 89, 23);
		contentPane.add(btnSpin);
		
		JLabel lblResturantSpinner = new JLabel("Resturant Spinner");
		lblResturantSpinner.setFont(new Font("CollegiateInsideFLF", Font.PLAIN, 36));
		lblResturantSpinner.setBounds(49, 71, 372, 59);
		contentPane.add(lblResturantSpinner);
		
		DefaultComboBoxModel<String> comboModel= new DefaultComboBoxModel<String>(resturantGroupList.toArray(new String[resturantGroupList.size()]));
		JComboBox comboBoxGroupPicker = new JComboBox(comboModel);
		comboBoxGroupPicker.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent agr0){
				selectedGroup = (String)comboBoxGroupPicker.getSelectedItem();
			}
		});
		comboBoxGroupPicker.setBounds(155, 48, 145, 20);
		contentPane.add(comboBoxGroupPicker);
		selectedGroup = (String)comboBoxGroupPicker.getSelectedItem();
	}
	
	public void spinWheel(JLabel resultLabel) {

		Random r = new Random();
		/**
		for (long stop=System.nanoTime()+TimeUnit.SECONDS.toNanos(2);stop>System.nanoTime();) {
			Color c = new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256));
			resultLabel.setBackground(c);
			String rText = resturantList.get(r.nextInt(resturantList.size())).getName();
			resultLabel.setText(rText);
		}
		*/
		Color c = new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256));
		resultLabel.setBackground(c);
		Resturant rest= resturantList.get(r.nextInt(resturantList.size()));
		if(rest.getGroup().equals(ResturantSpinnerFrame.selectedGroup)){
			resultLabel.setText(rest.getName());
			resultLabel.setOpaque(true);
		}
		else{
			spinWheel(resultLabel);
		}
	}

	private static void accumulateResturantList() {
		String csvFile = "F:\\Documents\\Programming\\Mars\\eclipse\\workspace\\ResturantSpinner\\src\\ResturantsDatabase.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = "\t";

		try {

			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
				String [] splitLine = line.split(cvsSplitBy);
				Resturant newR = new Resturant(splitLine[0], splitLine[1], splitLine[2], splitLine[3], splitLine[4]);
				resturantList.add(newR);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		System.out.println("Done");		
	}

	public static void addResturant(String rName, String rAddress, String rStyle, String rPrice, String rGroup){
		Resturant newResturant = new Resturant(rName, rAddress, rStyle, rPrice, rGroup);
		resturantList.add(newResturant);

		//Physically change the database
		PrintWriter csvWriter;
		try {
			/*1. declare stringBuffer*/
			StringBuffer oneLineStringBuffer = new StringBuffer();

			File file = new File("F:\\Documents\\Programming\\Mars\\eclipse\\workspace\\ResturantSpinner\\src\\ResturantsDatabase.csv");
			csvWriter = new PrintWriter(new FileWriter(file, true));

			/*2. append to stringBuffer*/   
			oneLineStringBuffer.append(rName + '\t' + rAddress + '\t' + rStyle + '\t' + rPrice + '\t' + rGroup);
			oneLineStringBuffer.append("\n");

			/*3. print to csvWriter*/
			csvWriter.print(oneLineStringBuffer);

			csvWriter.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void editResturant(Resturant prevRecord, Integer rIndex, String rName, String rAddress, String rStyle, String rPrice, String rGroup){

		Resturant rEdit = resturantList.get(rIndex);
		rEdit.editResturant(rName, rAddress, rStyle, rPrice, rGroup);

		//remove the previous entry
		PrintWriter csvWriter;
		try{
			//Create a delete the old database and write previous data to it
			File file = new File("F:\\Documents\\Programming\\Mars\\eclipse\\workspace\\ResturantSpinner\\src\\ResturantsDatabase.csv");
			String tempFile = file.toString();
			file.delete();
			file = new File(tempFile);
			csvWriter = new PrintWriter(new FileWriter(file,true));

			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			for (Resturant r : resturantList){
				bw.write(r.asCSVRecord() + '\n');
			}
			bw.close();
		} catch (Exception e){
			e.printStackTrace();
		}

		System.out.println("Updated CSV \n");

		//Update All of our lists
		resturantGroupList = new ArrayList<String>();
		resturantStyleList = new ArrayList<String>();
		resturantList = new ArrayList<Resturant>();
		accumulateResturantGroupList();
		accumulateResturantStylelist();
		accumulateResturantList();

	}

	public static void deleteResturant(Resturant restToDelete) {
		//remove the previous entry
		PrintWriter csvWriter;
		try{
			//Create a delete the old database and write previous data to it
			File file = new File("F:\\Documents\\Programming\\Mars\\eclipse\\workspace\\ResturantSpinner\\src\\ResturantsDatabase.csv");
			String tempFile = file.toString();
			file.delete();
			file = new File(tempFile);
			csvWriter = new PrintWriter(new FileWriter(file,true));

			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			for (Resturant r : resturantList){
				if(r != restToDelete){
					bw.write(r.asCSVRecord() + '\n');
				}
			}
			bw.close();
		} catch (Exception e){
			e.printStackTrace();
		}

		System.out.println("Updated CSV \n");

		//Update All of our lists
		resturantGroupList = new ArrayList<String>();
		resturantStyleList = new ArrayList<String>();
		resturantList = new ArrayList<Resturant>();
		accumulateResturantGroupList();
		accumulateResturantStylelist();
		accumulateResturantList();
	}

	private static void accumulateResturantStylelist() {
		// TODO Auto-generated method stub
		String csvFile = "F:\\Documents\\Programming\\Mars\\eclipse\\workspace\\ResturantSpinner\\src\\ResturantsDatabase.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = "\t";

		try {

			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
				String [] splitLine = line.split(cvsSplitBy);
				if (resturantStyleList.contains(splitLine[2])){

				}
				else{
					String temp = splitLine[2];
					resturantStyleList.add(temp);
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		System.out.println("Done");	
	}

	public static String[] getResturantGroupsList(){
		return resturantGroupList.toArray(new String[resturantGroupList.size()]);
	}

	private static void accumulateResturantGroupList() {

		String csvFile = "F:\\Documents\\Programming\\Mars\\eclipse\\workspace\\ResturantSpinner\\src\\ResturantsDatabase.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = "\t";

		try {

			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
				String [] splitLine = line.split(cvsSplitBy);
				if (resturantGroupList.contains(splitLine[4])){

				}
				else{
					String temp = splitLine[4];
					resturantGroupList.add(temp);
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		System.out.println("Done");
	}

	@SuppressWarnings("null")
	public static String[] getResturantList() {
		ArrayList<String> tempList = new ArrayList<String>();
		String tempGroup = selectedGroup;
		String tempResturant;
		Integer count = 0;

		for (Resturant r : resturantList){
			if(r.getGroup().equals(tempGroup)){
				tempResturant = r.getName();
				tempList.add(tempResturant);
				count++;
			}
		}
		return tempList.toArray(new String[tempList.size()]);
	}

	public static Resturant getResturant(String selectedItem) {
		for (Resturant r : resturantList){
			if(r.getName().equals(selectedItem)){
				return r;
			}
		}
		return null;
	}


	public static String[] getResturantStylesList() {
		return resturantStyleList.toArray(new String[resturantStyleList.size()]);
	}
}