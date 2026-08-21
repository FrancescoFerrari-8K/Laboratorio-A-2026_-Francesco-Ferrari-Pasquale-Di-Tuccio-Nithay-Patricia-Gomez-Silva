package CineMaX;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Cliente extends Guest {

  // parametri //
  private String Nome;

  private String Cognome;

  private String IDutente;

  // costruttore//

  public Cliente(String nome, String cognome, String idutente) {

    this.Nome = nome;
    this.Cognome = cognome;
    this.IDutente = idutente;
  }

  public String getNome() {
    return this.Nome;
  }

  public void setNome(String nome) {
    this.Nome = nome;
  }

  public String getCognome() {
    return this.Cognome;
  }

  public void setCognome(String cognome) {
    this.Cognome = cognome;
  }

  public String getIDUtente() {
    return this.IDutente;
  }

  public void setIDUtente(String IDutente) {
    this.IDutente = IDutente;
  }
  // metodi//

  // cerca proiezione (implementato da guest)//
  // visulazza proiezione (implementato da guest)//

  // metodo che messo in input l'ID del cliente posso restituire un objetto del
  // tipo cliente
  public static Cliente getClienteDaFile(int IdCliente) {

    return null;
  }

  // -visualizzare le propie prenotazioni//
  public void visualizzaLeMiePrenotazioni() {
    // legge il file prenotazione
    // strae le prenotazioni a nome di questo cliente
    // mostrarle
    ArrayList<Prenotazione> l = Prenotazione.caricaPrenotazioni();
    ArrayList<Prenotazione> prenotazioniCliente = new ArrayList<Prenotazione>();
    System.out.println("");
    System.out.println("Ricerca per:" + this.getNome() + " " + this.getCognome());
    prenotazioniCliente = Prenotazione.TrovaPrenotazioniConNomeECognome(this.getNome(), this.getCognome(),
        l);
    System.out.println("");
    System.out.println("PrenotazioniTrovate:" + prenotazioniCliente.size());
    System.out.println("");
    for (Prenotazione elemento : prenotazioniCliente) {
      System.out.println(elemento.toString(false));
    }

    System.out.println("");

  }

  // crea prenotazione//
  public boolean creaPrenotazione(Proiezione proiezioneSelezionata, int posti) {

    Scanner sc = new Scanner(System.in);

    boolean haPostiDisponibili = proiezioneSelezionata.haPostiDisponibili(posti);

    if (haPostiDisponibili == true) {
      // prenota
      String sceltaPrenota;
      System.out
          .println("il prezzo totale delle tue prenotazione è: " + proiezioneSelezionata.getPrezzoBiglietto() * posti);
      System.out.println("");
      System.out.println("sei sicuro/a che vuoi prenotare? (S/N): ");
      sceltaPrenota = sc.nextLine();

      if (sceltaPrenota.toUpperCase().charAt(0) == 'S') {

        Prenotazione prenotazione = new Prenotazione(this.getIDUtente(), this.getNome(), this.getCognome(),
            proiezioneSelezionata.getDataOra(),
            proiezioneSelezionata.getFilm().getTitolo(), posti, proiezioneSelezionata.getPrezzoBiglietto());

        return Prenotazione.aggiungiPrenotazioneAlCSV(prenotazione);
      } else {
        System.out.println("prenotazione annulata");
        return false;
      }

    } else {
      System.out.println("non si puo efettuare la prenotazione perche non ci sono posti disponibili");
      return false;
    }

  }

  // questo metodo serve per modificare le prenotazioni del CSV//
  public boolean modificaPrenotazione(int idPrenotazione, Prenotazione nuovaPrenotazione) {
    boolean prenotazioneModificata = Prenotazione.modificaPrenotazioneNelCSV(idPrenotazione, nuovaPrenotazione);

    if (prenotazioneModificata == true) {
      System.out.println("tua prenotazione è stata modificata con successo");
    } else {

      System.out.println("Non è stato possibile modificare la tua prenotazione.");
    }

    return prenotazioneModificata;
    // legge il file prenotazione
    // trova la prenotazione con idPrenotazioneDaEliminare
    // elimina
    // salva file prenotazione
  }

  // questo metodo serve per modificare le prenotazioni del CSV//
  public boolean eliminaPrenotazione(int idPrenotazioneDaEliminare) {

    return Prenotazione.eliminaPrenotazioneDalCSV(idPrenotazioneDaEliminare);

    // legge il file prenotazione
    // trova le prenotazione con idPrenotazioneDaEliminare
    // elimina la prenotazione
    // salva il file prenotazione
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
        System.out.println("Benvenuto/a: " + this.getNome() + " " + this.getCognome());
        System.out.println("");
        System.out.println("-Scegli un numero (1,2,3) per continuare-");
        System.out.println("");
        System.out.println("1- Cerca proiezione e prenota");
        System.out.println("2- Visualizzare le mie prenotazione");
        System.out.println("3- Logout");
        System.out.println("");
        System.out.print("Scelta: ");
        sceltaMenucliente = this.leggiInt("numero non valido");

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
              sceltaProiezione = this.leggiInt("numero non valido");



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
                  sceltaGenere = this.leggiInt("numero non valido");
                  break;
                // cercaProiezione(sceltaGenere)e restituscimi la proiezione scelta dal utente
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
                // cercaProiezione(sceltaTitolo)e restituscimi la proiezione scelta dal utente

                case 3:
                  // interfaccia di data//
                  String sceltaData;
                  System.out.println("---DATA---");
                  System.out.println("");
                  System.out.println(
                      "Inserisce data (GG/MM/YYYY) della proiezione che vuoi vedere oppure 0 per tornare indietro: ");
                  sc.nextLine();
                  sceltaData = sc.nextLine();
                  System.out.println("");
                  // TODO: controllare se la data inserita e in un formatto corretto e se e una
                  // data futura//
                  if (Prenotazione.FormatoDiDataCorretto(sceltaData)) {

                    LocalDate data = LocalDate.parse(sceltaData);
                    LocalTime orario = LocalTime.now(); // se usa para saber el orario del di, asi el utente no puede
                                                        // escoger un film que ya paso
                    // cercaProiezione(data,orario); e restituscimi la proiezione scelta dal utente

                    // ////
                  } else {
                    System.out.println("Errore: La data non è valida");
                  }
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
                  sceltaCosto = this.leggiInt("numero non valido");
                  // cercaProiezione(sceltaCosto)e restituscimi la proiezione scelta dal utente
                  break;
                case 5:
                  // BISOGNA INSERIRE IL MENU CON LE PROIEZIONI E LUTENTE DEBE SELEZIONARE LA
                  // PROIEZIONE CHE VUOLE PRENOTARE!!
                  // chiamare il creaPrenotazione per creare la prenotazione, il risultato di
                  // questo metodo è booleano
                  Proiezione[] risultatoRicerca = this.cercaProiezione();
                  Proiezione proiezioneSelezionata = Guest.selezionaProiezDaRicerca(risultatoRicerca);

                  int numeroPostiRichiesti = this.chiediNumeroDiPosti(proiezioneSelezionata);

                  if(numeroPostiRichiesti< 0){
                    System.out.println("posti non disponibili");

                  } else {

                    this.creaPrenotazione(proiezioneSelezionata, numeroPostiRichiesti);
                  }
                  // LocalDate data = LocalDate.parse("2027-12-28");
                  // LocalTime orario = LocalTime.parse("15:30");
                  // LocalDateTime dataOra = data.atTime(orario); // Combina data e ora
                  // Proiezione proiezioneTest = new Proiezione(
                  // new Film("Blue Velvet",
                  // "Zombie", "NitReg", 2020, 3, 2),
                  // LocalDateTime.now(),
                  // 10);

                  break;
              }

            } while (sceltaProiezione != 0);

            break;

          case 2:
            int sceltaPrenotazioni;
            do {
              // interfaccia di prenotazione//
              System.out.println("---LE MIE PRENOTAZIONI---");
              System.out.println("");
              System.out.println("-Scegli un numero per continuare-");
              System.out.println("");
              System.out.println("1- Visualizzare prenotazioni");
              System.out.println("2- Modificare prenotazione");
              System.out.println("3- Eliminare prenotazione");
              System.out.println("0- INDIETRO");
              System.out.println("");
              System.out.println("Scelta");
              sceltaPrenotazioni = this.leggiInt("numero non valido");

              switch (sceltaPrenotazioni) {
                case 1:
                  System.out.println("---VISUALIZZA PRENOTAZIONI---");

                  this.visualizzaLeMiePrenotazioni();
                  break;

                case 2:
                  int scletaModificaPrenotazione;
                  do {
                    System.out.println("---MODIFICA PRENOTAZIONE---");
                    ArrayList<Prenotazione> prenotazioneC = Prenotazione.caricaPrenotazioni();
                    ArrayList<Prenotazione> prenotazioneCliente = new ArrayList<Prenotazione>();
                    prenotazioneCliente = Prenotazione.TrovaPrenotazioniConNomeECognome(this.getNome(),
                        this.getCognome(), prenotazioneC);
                    System.out.println("");
                    System.out.println(
                        "inserisci l'ID della prenotazione che vuoi modificare oppure scrivi 0 per tornare indietro ");
                    System.out.println("");
                    for (Prenotazione elemento : prenotazioneCliente) {
                      System.out.println(elemento.toString(true));
                    }
                    System.out.println("");
                    System.out.print("Inserisci il numero di l'ID oppure scrivi 0 per torare indietro: ");
                    scletaModificaPrenotazione = this.leggiInt("ID non valido");
                    if (scletaModificaPrenotazione == 0) {
                      break;
                    }
                    // TODO:
                    // 1) mostrare al utente la prenotazione scelta o l'errore in caso l'ID non
                    // esista.
                    int numeroDiPosti;
                    for (Prenotazione elemento : prenotazioneCliente) {
                      if (scletaModificaPrenotazione == elemento.getIDPrenotazione()) {
                        int sceltaDiModifica;

                        System.out.println("la prenotazione che vuoi modificare è: " + elemento.toString());
                        System.out.println("");
                        System.out.println("cosa vuoi modificare?: ");
                        System.out.println("");
                        System.out.println("1- Numero di posti");
                        System.out.println("2- La proiezione che vuoi vedere");
                        System.out.println("");
                        System.out.println("Scrive 1 oppure 2 per scegliere: ");
                        sceltaDiModifica = this.leggiInt("numero non valido");

                        switch (sceltaDiModifica) {
                          case 1:
                            //ho bisogno di estrarre la proiezione inserendo solo la data e il titolo della proiezione (elemento)
                            

                            Proiezione proiezioneAttuale = null;
                            this.chiediQuantiPostiPrenotareEmodifica(proiezioneAttuale, elemento);
                            break;

                          case 2:
                            // ricerca proiezione quella che ci deve restituire la proiezione
                            // l'utente sceglie la proiezione
                            Proiezione proiezioneSelezionata = null;




                            this.chiediQuantiPostiPrenotareEmodifica(proiezioneSelezionata, elemento);
                            break;
                          default:
                            System.out.println("opzione non riconosciuta, riprova");
                            break;
                        }
                      } else {
                        System.out.println("l'ID inserito non e valido");
                      }
                    }
                  } while (scletaModificaPrenotazione != 0);
                  // 3)chiamare il modifica
                  // se viene modifiacta la proiezione l'utente deve poter vedere la lista di
                  // proiezioni e orari. sulla base di quello io modifico il file di prenotazioni
                  // se invece l'utente puo modificare solo il numero di posti prenotati, ho
                  // bisogno di un metodo che mi modifichi il totale di posti per la proiezione
                  // (NUMPOSTIDISPONIBILI)

                  break;

                case 3:
                  System.out.println("---ELIMINA PRENOTAZIONE---");
                  System.out.println("");
                  ArrayList<Prenotazione> prenotazioniC = Prenotazione.caricaPrenotazioni();
                  ArrayList<Prenotazione> prenotazioniClienti = new ArrayList<Prenotazione>();
                  prenotazioniClienti = Prenotazione.TrovaPrenotazioniConNomeECognome(this.getNome(), this.getCognome(),
                      prenotazioniC);
                  System.out.println("");
                  System.out.println("Que prenotazione vuoi eliminare? " + "inserisci ID: ");
                  System.out.println("");
                  for (Prenotazione elemento : prenotazioniClienti) {
                    System.out.println(elemento.toString(true));
                  }
                  System.out.println("");
                  System.out.print("Inserisci: ");
                  sceltaPrenotazioni = this.leggiInt("ID non valido");

                  boolean esiste = false;
                  String sceltaEliminaPrenotazione;

                  for (Prenotazione elemento : prenotazioniClienti) {

                    if (sceltaPrenotazioni == elemento.getIDPrenotazione()) {
                      // ho incontrato l'id, quindi esco e mi ricordo che l'id esiste
                      esiste = true;
                    }

                  }

                  if (esiste == true) {
                    System.out.println("sei sicuro/a che vuoi eliminare questa prenotazione? (S/N): ");
                    sceltaEliminaPrenotazione = sc.nextLine();

                    if (sceltaEliminaPrenotazione.toUpperCase().charAt(0) == 'S') {
                      this.eliminaPrenotazione(sceltaPrenotazioni);
                    }
                  } else {
                    System.out.println("eliminazione annulata");
                  }

                  // en caso de si eliminar, en caso de no, volver a atras
                  break;

                default:
                  System.out.println("opzione non valida");
                  break;
              }
            } while (sceltaPrenotazioni != 0);

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

  private void chiediQuantiPostiPrenotareEmodifica(Proiezione proiezioneSelezionata, Prenotazione prenotazioneSelezionata) {
    int numeroDiPosti = this.chiediNumeroDiPosti(proiezioneSelezionata);
    if (numeroDiPosti != -1) {

      Prenotazione nuovaPrenotazione = new Prenotazione(prenotazioneSelezionata.getIDPrenotazione(),
      prenotazioneSelezionata.getIDUtente(), prenotazioneSelezionata.getNome(), prenotazioneSelezionata.getCognome(),
      prenotazioneSelezionata.getProiezione_Data(), prenotazioneSelezionata.getProiezione_Titolo(), numeroDiPosti,
      prenotazioneSelezionata.getPrezzoBiglietto());
      this.modificaPrenotazione(prenotazioneSelezionata.getIDPrenotazione(), nuovaPrenotazione);

    } else {
      System.out.println("non ci sono posti disponibili");
    }

  }


  private int chiediNumeroDiPosti(Proiezione proiezioneSelezionata){
  
    Scanner sc = new Scanner(System.in);
    System.out.println("scrivi quanti posti vuoi");
    int numeroDiPosti = this.leggiInt("numero di posti non valido");
    System.out.println("posti scritti:" + numeroDiPosti);

    boolean hapostidisponibili = proiezioneSelezionata.haPostiDisponibili(numeroDiPosti);
    if(hapostidisponibili == false){
      numeroDiPosti = -1;
    }

    return numeroDiPosti;
  }

  private int leggiInt (String errorMessage){
    Scanner sc = new Scanner(System.in);
    boolean inputStringintOk;
    String inputStringint;
    int variabileint = 0;

    do {
			inputStringintOk=true;
			try {
				inputStringint = sc.nextLine();
				variabileint = Integer.parseInt(inputStringint);
			
			} catch (NumberFormatException e) {
				inputStringintOk = false;
				System.out.println(errorMessage);
			}
		} while (inputStringintOk == false);

    return variabileint;
  }
}


  


