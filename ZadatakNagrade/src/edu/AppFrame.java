package edu;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class AppFrame extends JFrame {

	private JPanel contentPane;
	private JTextField txtIme;
	private JTextField txtPrezime;
	private JTextField txtDobitnik;
	private JTextField txtPreostalo;
	private JCheckBox chbxNagrada;
	private JButton btnMail;
	private JButton btnPodeli;
	private int preostaloNagrada = 0;
	private Stack<Osoba> kandidati = new Stack<Osoba>();
	private PrintWriter izlaz = null;;
	private BufferedReader ulaz = null;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AppFrame frame = new AppFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 *  
	 */
	public AppFrame() {
		
		try {
			ulaz = new BufferedReader(new FileReader("src/nagrade.txt"));
			preostaloNagrada = Integer.parseInt(ulaz.readLine());
		} catch (FileNotFoundException e) {
			System.out.println("Fajl nagrade nije pronadjen!");
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			izlaz = new PrintWriter(new FileWriter("src/dobitnici.txt"));
		} catch (IOException e) {
			System.out.println("Neuspela operacija pripreme za upis u fajl dobitnici.txt");
			System.exit(1);
		}
	
		
		setTitle("Nagrade");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 389, 361);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(0, 1, 0, 0));
		
		JPanel pnlPodaci = new JPanel();
		pnlPodaci.setBackground(UIManager.getColor("Button.background"));
		pnlPodaci.setToolTipText("");
		pnlPodaci.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 255)), "Podaci", TitledBorder.CENTER, TitledBorder.TOP, null, Color.BLACK));
		contentPane.add(pnlPodaci);
		pnlPodaci.setLayout(new GridLayout(0, 1, 0, 0));
		
		JPanel pnlIme = new JPanel();
		FlowLayout flowLayout = (FlowLayout) pnlIme.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		pnlPodaci.add(pnlIme);
		
		JLabel lblIme = new JLabel("Ime:");
		pnlIme.add(lblIme);
		
		txtIme = new JTextField();
		txtIme.setHorizontalAlignment(SwingConstants.LEFT);
		pnlIme.add(txtIme);
		txtIme.setColumns(10);
		
		JPanel pnlPrezime = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) pnlPrezime.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		pnlPodaci.add(pnlPrezime);
		
		JLabel lblLblprezime = new JLabel("Prezime: ");
		pnlPrezime.add(lblLblprezime);
		
		txtPrezime = new JTextField();
		txtPrezime.setHorizontalAlignment(SwingConstants.LEFT);
		pnlPrezime.add(txtPrezime);
		txtPrezime.setColumns(10);
		
		
		
		chbxNagrada = new JCheckBox("Zeli nagradu");
		pnlPodaci.add(chbxNagrada);
		JPanel pnlMail = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) pnlMail.getLayout();
		pnlPodaci.add(pnlMail);
		
		 btnMail = new JButton("Stigao Mail");
		btnMail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
					String ime = txtIme.getText();
					String prezime = txtPrezime.getText();
					if(ime.equals("") || prezime.equals("")) {
						JOptionPane.showMessageDialog(contentPane,"Popuniti oba polja!","Alert",JOptionPane.WARNING_MESSAGE);
						txtIme.setText("");
						txtPrezime.setText("");
						chbxNagrada.setSelected(false);
						return;
				    }
					if (chbxNagrada.isSelected()) {
				    	kandidati.push(new Osoba(ime, prezime));
						
				}
				txtIme.setText("");
				txtPrezime.setText("");
				chbxNagrada.setSelected(false);
			}
		});
		btnMail.setVerticalAlignment(SwingConstants.TOP);
		pnlMail.add(btnMail);
		
		JPanel pnPodeliNagradu = new JPanel();
		contentPane.add(pnPodeliNagradu);
		pnPodeliNagradu.setLayout(new GridLayout(0, 1, 0, 0));
		
		JPanel pnlPodeli = new JPanel();
		pnPodeliNagradu.add(pnlPodeli);
		
		btnPodeli = new JButton("Podeli nagradu");
		btnPodeli.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(preostaloNagrada == 0) {
					btnPodeli.setEnabled(false);
					txtDobitnik.setText("Nema vise Nagrada!");
					izlaz.println("\n\nNisu dobili nagrade");
					
					while(!kandidati.isEmpty()) {
						izlaz.println(kandidati.pop().toString());
						izlaz.close();
					}
				} else if (!kandidati.isEmpty()) {
					preostaloNagrada--;
					txtPreostalo.setText(preostaloNagrada + "");
					Osoba osoba = kandidati.pop();
					txtDobitnik.setText(osoba.toString());
					izlaz.println(osoba);
				} else
					txtDobitnik.setText("Trenutno nema prijavljenih kandidata");
			}
		});
		pnlPodeli.add(btnPodeli);
		
		JPanel pnlDobitnik = new JPanel();
		pnPodeliNagradu.add(pnlDobitnik);
		pnlDobitnik.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel lblDobitnik = new JLabel("Poslednji dobitnik:");
		lblDobitnik.setVerticalAlignment(SwingConstants.TOP);
		pnlDobitnik.add(lblDobitnik);
		
		txtDobitnik = new JTextField();
		txtDobitnik.setEditable(false);
		pnlDobitnik.add(txtDobitnik);
		txtDobitnik.setColumns(10);
		
		JPanel pnlPreostalo = new JPanel();
		FlowLayout flowLayout_3 = (FlowLayout) pnlPreostalo.getLayout();
		flowLayout_3.setAlignment(FlowLayout.LEFT);
		pnPodeliNagradu.add(pnlPreostalo);
		
		JLabel lblPreostalo = new JLabel("Ostalo jos:");
		pnlPreostalo.add(lblPreostalo);
		
		txtPreostalo = new JTextField();
		txtPreostalo.setEditable(false);
		pnlPreostalo.add(txtPreostalo);
		txtPreostalo.setColumns(10);
		
	}
}
