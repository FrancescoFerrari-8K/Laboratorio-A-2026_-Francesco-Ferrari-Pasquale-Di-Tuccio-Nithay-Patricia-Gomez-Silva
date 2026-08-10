package CineMaX;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.io.BufferedReader;

public class Film { // Questa classe costruisce oggetti di tipo Film
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
	// Campi
	private LocalDate Data;
	private LocalTime Orario;
    private String Titolo;
	private String Genere;
	private String Regista;
	private int Anno;
	private int Durata; // In minuti	
	private int Età; // Età minima
	private double Prezzo; //Costo biglietto proiezione
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
	// Costruttori
	public Film(LocalDate Data,LocalTime ora,String titolo, String genere, String regista, int anno, int durata, int età, double prezzo) {
		this.Data=Data;
		this.Orario=ora;
		this.Titolo=titolo;
		this.Genere=genere;
		this.Regista=regista;
		this.Anno=anno;
		this.Durata=durata;
		this.Età=età;
		this.Prezzo=prezzo;
	}
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
	// Metodi
	// Getter
	public LocalDate getData(){
		return Data;
	}
	
	public LocalTime getOra(){
		return Orario;
	}
	
	public String getTitolo(){
		return Titolo;
	}
	
	public String getGenere(){
		return Genere;
	}
	
	public String getRegista(){
		return Regista;
	}
	
	public String getAnno(){
		return Anno;
	}
	
	public String getDurata(){
		return Durata;
	}
	
	public String getEtà(){
		return Età;
	}
	
	public String getPrezzo(){
		return Prezzo;
	}
	
