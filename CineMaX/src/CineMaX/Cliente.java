package CineMaX;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Cliente extends Guest {

  // parametri //

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

  /// questo metodo estrae dal file di prenotazione tutte le prenotazione e me
  /// li ristituisce
  public static ArrayList<Prenotazione> caricaPrenotazioni()  {
    // trova il percorso assoluto del file proiezioni.csv per rendere il metodo
    // indipendente dalla macchina su cui è eseguito
    String percorso = new File("..\\..\\data\\Prenotazioni.csv").getAbsolutePath(); // il doppio punto è per andare
                                                                                    // nella directory padre

    // inizializzo l'array di linkedlist datastruct
    ArrayList<Prenotazione> listaPrenotazioni = new ArrayList<Prenotazione>();

    // lettura del file e scrittura delle colonne come linkedlists nell' array
    // datastruct
    FileReader frd;
    try {
      frd = new FileReader(percorso);
    try (BufferedReader buffread = new BufferedReader(frd)) {
      String riga; // Creo la variabile che contiene ad ogni iterazione la riga successiva del file
      String[] colonne; // inizializzo la variabile che crea l'array di stringhe che contiene i valori
                        // estratti dal file da inserire nelle LinkedList

      // Leggi la prima riga (l'intestazione) a vuoto per saltarla
      if (buffread.readLine() != null) {
        // Usiamo un 'if' per sicurezza, nel caso in cui il file fosse completamente
        // vuoto
      }

      while ((riga = buffread.readLine()) != null) {// leggo il file riga per riga fino a quando la riga non
                                                    // diventa null (dopo l'ultima riga!)
        colonne = riga.split(",");// divido le colonne col separatore decimale , essendo il file di tipo csv
        // aggiungo le stringhe nelle relative LinkedList
        Prenotazione PrenotazioneTemp = new Prenotazione(colonne[0], colonne[1], LocalDateTime.parse(colonne[2]),
            colonne[3],
            Integer.parseInt(colonne[4]), Integer.parseInt(colonne[5]));
        listaPrenotazioni.add(PrenotazioneTemp);
      }
      // chiusura degli stream per evitare memory leaks
      buffread.close();
      frd.close();

    } 
    catch(IOException e){
      e.printStackTrace();
    }
  }catch (FileNotFoundException e) {
    e.printStackTrace();
  }
    return listaPrenotazioni;
  }

  // -visualizzare le propie prenotazioni//
  public void visualizzarePrenotazione() {
    // leggere file prenotazione
    // strarre le prenotazioni a nome di questo cliente
    // mostrarle

  }

  // crea prenotazione//
  public boolean creaPrenotazione(Proiezione proiezioneSelezionata, int fila, int posto) {
    
    int postiLiberiDopoLaPrenotazione = proiezioneSelezionata.calcolaPostiLiberi(posto);

    if (postiLiberiDopoLaPrenotazione >= 0) {
      // prenota
      Prenotazione prenotazione = new Prenotazione(this.getNome(), this.getCognome(),
          proiezioneSelezionata.getDataOra(),
          proiezioneSelezionata.getFilm().getTitolo(), fila, posto);


      return Prenotazione.aggiungiPrenotazioneAlCSV(prenotazione);
      
    } else {
      System.out.println("non si puo efettuare la prenotazione perche non ci sono posti disponibili");
      return false;
    }

  }

  // modifica prenotazione//
  public void modificaPrenotazione(int idPrenotazioneDaModificare, Prenotazione nuovaPrenotazione) {
    // leggere file prenotazione
    // trovare prenotazione con idPrenotazioneDaModificare
    // sostituirla con nuovaPrenotazione
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
                  System.out.println("---PROIEZIONI DISPONIBILI---");
                  System.out.println("");

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
                System.out.println("---VISUAIZZA PRENOTAZIONE---");

                ArrayList<Prenotazione> l = Prenotazione.caricaPrenotazioni();
                ArrayList<Prenotazione> prenotazioniCliente = new ArrayList<Prenotazione>();
                prenotazioniCliente = Prenotazione.TrovaPrenotazioniConNomeECognome(this.getNome(), this.getCognome(),
                    l);
                for (Prenotazione elemento : prenotazioniCliente) {
                  System.out.println(elemento.toString());
                }

                System.out.println("");
                break;

              case 3:
                System.out.println("---MODIFICA PRENOTAZIONE---");
                System.out.println("");
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
