package CineMaX;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
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
	public Film(LocalDate data,LocalTime ora,String titolo, String genere, String regista, int anno, int durata, int età, double prezzo) {
		Data=data;
		Orario=ora;
		Titolo=titolo;
		Genere=genere;
		Regista=regista;
		Anno=anno;
		Durata=durata;
		Età=età;
		Prezzo=prezzo;
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
	
	public int getAnno(){
		return Anno;
	}
	
	public int getDurata(){
		return Durata;
	}
	
	public int getEtà(){
		return Età;
	}
	
	public double getPrezzo(){
		return Prezzo;
	}
	
	//Setter
	public void setData(LocalDate data){
		Data=data;
	}
	
	public void setOra(LocalTime ora){
		Orario=ora;
	}
	
	public void setTitolo(String titolo){
		Titolo=titolo;
	}
	
	public void setGenere(String genere){
		Genere=genere;
	}
	
	public void setRegista(String regista){
		Regista=regista;
	}
	
	public void setAnno(int anno){
		Anno=anno;
	}
	
	public void setDurata(int durata){
		Durata=durata;
	}
	
	public void setEtà(int età){
		Età=età;
	}
	
	public void setPrezzo(double prezzo){
		Prezzo=prezzo;
	}
}
	
