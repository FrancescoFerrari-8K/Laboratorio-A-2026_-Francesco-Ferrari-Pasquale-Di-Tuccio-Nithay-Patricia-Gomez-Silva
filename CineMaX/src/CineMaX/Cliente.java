package CineMaX;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Cliente extends Guest {

  // parametri //
	private String Nome;
	
	private String Cognome;
	
	private int IDutente;

  // costruttore//

  public Cliente(String nome, String cognome, String password, int id) {
    super(nome, cognome, "C", password, id);
  }

  // metodi//

  // - cerca proiezione (implementato da guest)//
  // - visulazza proiezione (implementato da guest)//

  // metodo che messo in input l'ID del cliente posso restituire un objetto del
  // tipo cliente
  public static Cliente getClienteDaFile(int IdCliente) {

    return null;
  }


  // -visualizzare le propie prenotazioni//
  public void visualizzarePrenotazione() {
    // leggere file prenotazione
    // strarre le prenotazioni a nome di questo cliente
    // mostrarle

  }

  // crea prenotazione//
  public boolean creaPrenotazione(Proiezione proiezioneSelezionata, int posti) {
    
    int postiLiberiDopoLaPrenotazione = proiezioneSelezionata.calcolaPostiLiberi(posti);

    if (postiLiberiDopoLaPrenotazione >= 0) {
      // prenota
      Prenotazione prenotazione = new Prenotazione(this.getNome(), this.getCognome(),
          proiezioneSelezionata.getDataOra(),
          proiezioneSelezionata.getFilm().getTitolo(), posti);


      return Prenotazione.aggiungiPrenotazioneAlCSV(prenotazione);

    } else {
      System.out.println("non si puo efettuare la prenotazione perche non ci sono posti disponibili");
      return false;
    }

  }

  public boolean modificaPrenotazione(int idPrenotazione, Prenotazione nuovaPrenotazione) {

    return Prenotazione.modificaPrenotazioneNelCSV(idPrenotazione, nuovaPrenotazione);


    // leggere file prenotazione
    // trovare prenotazione con idPrenotazioneDaEliminare
    // eliminarla
    // salvare file prenotazione
  }

  // elimina prenotazione//
  public void eliminaPrenotazione(int idPrenotazioneDaEliminare) {

    // leggere file prenotazione
    // trovare prenotazione con idPrenotazioneDaEliminare
    // eliminarla
    // salvare file prenotazione
  }

  // -logout//
  public void logout() {

  }

  // mettere test true mostra l'interfaccia del menu cliente//
  public void mostraMenuCliente(boolean test) throws FileNotFoundException, IOException {

    if (test == true) {
      int sceltaMenucliente = 0;
      Scanner sc = new Scanner(System.in);

      do {
        // interfaccia di menuCliente//
        System.out.println("---MENU CLIENTE---");
        System.out.println("");
        System.out.println("Benvenuto/a: "+ this.getNome() + " " + this.getCognome());
        System.out.println("");
        System.out.println("-Scegli un numero (1,2,3) per continuare-");
        System.out.println("");
        System.out.println("1- Cerca proiezione e prenota");
        System.out.println("2- Visualizzare le mie prenotazione");
        System.out.println("3- Logout");
        System.out.println("");
        System.out.print("Scelta: ");
        sceltaMenucliente = sc.nextInt();

        switch (sceltaMenucliente) {
          case 1:
            int sceltaProiezione;

            do {
              // interfaccia di cerca proiezione//
              System.out.println("---CERCA PROIEZIONE---");
              System.out.println("");
              System.out.println("-Scegli un numero per continuare-");
              System.out.println("");
              System.out.println("1- Genere");
              System.out.println("2- Titolo");
              System.out.println("3- Data");
              System.out.println("4- Costo");
              // TODO: fare proiezioni disponibili//
              System.out.println("5- Mostra proiezioni disponibili");
              System.out.println("0- INDIETRO");
              System.out.println("");
              System.out.print("Scelta: ");
              sceltaProiezione = sc.nextInt();

              switch (sceltaProiezione) {

                // interfaccia di genere//
                case 1:
                  int sceltaGenere;

                  System.out.println("---GENERE---");
                  System.out.println("");
                  System.out.println("-Scegli un numero per continuare-");
                  System.out.println("");
                  System.out.println("1- Adventure");
                  System.out.println("2- Action");
                  System.out.println("3- Animation");
                  System.out.println("4- Biography");
                  System.out.println("5- Comedy");
                  System.out.println("6- Crime");
                  System.out.println("7- Drama");
                  System.out.println("8- Film-Noir");
                  System.out.println("9- Horror");
                  System.out.println("0- INDIETRO");
                  System.out.println("");
                  System.out.print("Scelta: ");
                  sceltaGenere = sc.nextInt();
                  break;

                case 2:
                  // interfaccia di titolo//
                  String sceltaTitolo;
                  System.out.println("---TITOLO---");
                  System.out.println("");
                  System.out.println(
                      "Inserisce il titolo del film che vuoi vedere oppure 0 per tornare indieto: ");
                  sc.nextLine();
                  sceltaTitolo = sc.nextLine();
                  break;

                case 3:
                  // interfaccia di data//
                  String sceltaData;
                  System.out.println("---DATA---");
                  System.out.println("");
                  // TODO: controllare se la data inserita e in un formatto corretto e se e una
                  // data futura//
                  System.out.println(
                      "Inserisce data (GG/MM/YYYY) della proiezione che vuoi vedere oppure 0 per tornare indietro: ");
                  sc.nextLine();
                  sceltaData = sc.nextLine();
                  System.out.println("");
                  break;

                case 4:
                  // interfaccia di costo//
                  int sceltaCosto;
                  System.out.println("---COSTO---");
                  System.out.println("");
                  System.out.println("-Scegli un numero per continuare-");
                  System.out.println("");
                  System.out.println("1- Minore di 5 Euro");
                  System.out.println("2- Maggiore di 5 Euro");
                  System.out.println("3- Tra 5£ e 15 Euro");
                  System.out.println("0-INDIETRO");
                  System.out.println("");
                  System.out.println("Scelta: ");
                  sceltaCosto = sc.nextInt();
                  break;
                case 5:
                    //BISOGNA INSERIRE IL MENU CON LE PROIEZIONI E L?UTENTE DEBE SELEZIONARE LA PROIEZIONE CHE VUOLE PRENOTARE!!
                    //chiamare il creaPrenotazione per creare la prenotazione, il risultato di questo metodo è booleano
                    
                    Proiezione proiezioneTest = new Proiezione(
                      new Film("2027-12-28T15:30:00", "Blue Velvet", "Zombie","NitReg","2020","3","3","2"),
                       LocalDateTime.now(),
                        10);
                      this.creaPrenotazione(proiezioneTest, 3);
                      break;

              }

            } while (sceltaProiezione != 0);

            break;

          case 2:
            // interfaccia di prenotazione//
            int sceltaPrenotazioni;
            System.out.println("---LE MIE PRENOTAZIONI---");
            System.out.println("");
            System.out.println("-Scegli un numero per continuare-");
            System.out.println("");
            System.out.println("1- Prenota posto");
            System.out.println("2- Visualizzare prenotazioni");
            System.out.println("3- Modificare prenotazione");
            System.out.println("4- Eliminare prenotazione");
            System.out.println("0- INDIETRO");
            System.out.println("");
            System.out.println("Scelta");
            sceltaPrenotazioni = sc.nextInt();

            switch (sceltaPrenotazioni) {
              case 1:
                // interfaccia di prenotazione posto (manca finire)//
                System.out.println("---PRENOTA POSTO---");
                System.out.println("");
                break;

              case 2:
                System.out.println("---VISUALIZZA PRENOTAZIONI---");

                ArrayList<Prenotazione> l = Prenotazione.caricaPrenotazioni();
                ArrayList<Prenotazione> prenotazioniCliente = new ArrayList<Prenotazione>();

                System.out.println("");
                System.out.println("Ricerca per:" + this.getNome() + " " + this.getCognome());
                prenotazioniCliente = Prenotazione.TrovaPrenotazioniConNomeECognome(this.getNome(), this.getCognome(), l);
                System.out.println("");
                System.out.println("PrenotazioniTrovate:" + prenotazioniCliente.size());
                System.out.println("");
                for (Prenotazione elemento : prenotazioniCliente) {
                  System.out.println(elemento.toString(false));
                }

                System.out.println("");
                break;

              case 3:
                System.out.println("---MODIFICA PRENOTAZIONE---");
                ArrayList<Prenotazione> prenotazioneC = Prenotazione.caricaPrenotazioni();
                ArrayList<Prenotazione> prenotazioneCliente = new ArrayList<Prenotazione>();
                prenotazioneCliente = Prenotazione.TrovaPrenotazioniConNomeECognome(this.getNome(), this.getCognome(),prenotazioneC );
                System.out.println("");
                System.out.println("Que prenotazione vuoi modificare? "+ "inserisci ID: ");
                System.out.println("");
                for (Prenotazione elemento : prenotazioneCliente) {
                  System.out.println(elemento.toString(true));
                }
                System.out.println("");
                System.out.print("Inserisci: ");
                sceltaPrenotazioni = sc.nextInt();
                //TODO: 1) mostrare al utente la prenotazione scelta o l'errore in caso l'ID non esista. 2) mostrare al utente i dati da modificare 3)chiamare il modifica
                //il modifica prenotazione, cosa deve modificare? (la proiezione scelta oppure solo il numero di posti prenotati?)
                //se viene modifiacta la proiezione l'utente deve poter vedere la lista di proiezioni e orari. sulla base di quello io modifico il file di prenotazioni
                //se invece l'utente puo modificare solo il numero di posti prenotati, ho bisogno di un metodo che mi modifichi il totale di posti per la proiezione (NUMPOSTIDISPONIBILI)

                break;

              case 4:
                System.out.println("---ELIMINA PRENOTAZIONE---");
                System.out.println("");
                break;

            }

            break;
          case 3:
            System.out.println("stai per efettuare il logout...");
            break;
          default:
            System.out.println("opzione non riconosciuta, riprova");
            break;

        }

      } while (sceltaMenucliente != 3);

    }

  }

}
