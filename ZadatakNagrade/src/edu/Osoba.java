package edu;

public class Osoba {
	
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

