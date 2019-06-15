package edu;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

class Osoba {
	private String ime;
	private String prezime;

	public Osoba(String ime, String prezime) {
		this.ime = ime;
		this.prezime = prezime;
	}

	public String toString() {
		return ime + " " + prezime;
	}
}

class Prozor extends JFrame {
	private JButton mail = new JButton("Stigao mail");
	private JButton podeli = new JButton("Podeli nagradu");
	private JTextField ime = new JTextField(10);
	private JTextField prezime = new JTextField(10);
	private JCheckBox nagrada = new JCheckBox("zeli nagradu");
	private JTextField dobitnik = new JTextField(20);
	private JTextField preostalo = new JTextField(3);
	private int broj = 0;
	private Stack<Osoba> kandidati = new Stack<Osoba>();
	private PrintWriter izl;

	public Prozor(String title) {
		setTitle(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
// fajl iz koga citamo inicijalni broj nagrada
		Scanner sc = null;
		try {
			sc = new Scanner(new File("src/nagrade.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Nema fajla nagrade.txt");
			System.exit(1);
		}
		if (sc.hasNextInt())
			broj = sc.nextInt();
		sc.close();
		preostalo.setText(broj + "");
// fajl u koji upisujemo dobitnike i kandidate koji nisu dobili nagrade
		try {
			izl = new PrintWriter(new FileWriter("src/dobitnici.txt"));
		} catch (IOException IOe) {
			System.out.println("Neuspela operacija pripreme za upis u fajl dobitnici.txt");
			System.exit(1);
		}
// Kreiranje GUI-ja
		Container content = getContentPane();
		content.setLayout(new GridLayout(0, 1));
		JPanel podaci = new JPanel(new GridLayout(0, 1));
		content.add(podaci);
		podaci.setBorder(BorderFactory.createTitledBorder(new EtchedBorder(Color.CYAN, Color.BLUE), "Podaci",
				TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION));
		JPanel imePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		imePanel.add(new JLabel("Ime: "));
		imePanel.add(ime);
		podaci.add(imePanel);
		JPanel prezimePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		prezimePanel.add(new JLabel("Prezime: "));
		prezimePanel.add(prezime);
		podaci.add(prezimePanel);
		podaci.add(nagrada);
		JPanel mailPanel = new JPanel(new FlowLayout());
		mailPanel.add(mail);
		mail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (nagrada.isSelected()) {
					String imeS = ime.getText();
					String prezimeS = prezime.getText();
					if (imeS.equals("")) {
						ime.setText("POPUNITI POLJE!");
						return;
					} else if (prezimeS.equals("")) {
						prezime.setText("POPUNITI POLJE!");
						return;
					} else
						kandidati.push(new Osoba(imeS, prezimeS));
				}
				ime.setText("");
				prezime.setText("");
				nagrada.setSelected(false);
			}
		});
		podaci.add(mailPanel);
		JPanel podeliNagraduPanel = new JPanel(new GridLayout(0, 1));
		JPanel podeliPanel = new JPanel();
		podeliPanel.add(podeli);
		podeli.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (broj == 0) {
					podeli.setEnabled(false);
					dobitnik.setText("Nema vise nagrada");
					izl.println("\n\nNisu dobili nagrade: ");
					while (!kandidati.isEmpty())
						izl.println(kandidati.pop().toString());
					izl.close();
				} else if (!kandidati.isEmpty()) {
					broj--;
					preostalo.setText(broj + "");
					Osoba osoba = kandidati.pop();
					dobitnik.setText(osoba.toString());
					izl.println(osoba);
				} else
					dobitnik.setText("Trenutno nema prijavljenih kandidata");
			}
		});
		podeliNagraduPanel.add(podeliPanel);
		JPanel dobitnikPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		dobitnikPanel.add(new JLabel("Poslednji dobitnik: "));
		dobitnik.setEditable(false);
		dobitnikPanel.add(dobitnik);
		podeliNagraduPanel.add(dobitnikPanel);
		JPanel preostaloPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		preostaloPanel.add(new JLabel("Ostalo jos: "));
		preostalo.setEditable(false);
		preostaloPanel.add(preostalo);
		podeliNagraduPanel.add(preostaloPanel);
		content.add(podeliNagraduPanel);
	}
}

public class Nagrade {
	private static Prozor window;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createGUI();
			}
		});
	}

	static void createGUI() {
		window = new Prozor("Nagrade");
		Toolkit theKit = window.getToolkit();
		Dimension wndSize = theKit.getScreenSize();
		window.setBounds(wndSize.width / 4, wndSize.height / 4, wndSize.width / 2, wndSize.height / 2);
		window.pack();
		window.setVisible(true);
	}
}
