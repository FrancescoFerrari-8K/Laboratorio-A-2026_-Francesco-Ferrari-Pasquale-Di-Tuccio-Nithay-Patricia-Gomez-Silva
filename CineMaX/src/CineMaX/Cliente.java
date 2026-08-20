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
  public void visualizzarePrenotazione() {
    // legge il file prenotazione
    // strae le prenotazioni a nome di questo cliente
    // mostrarle

  }

  // crea prenotazione//
  public boolean creaPrenotazione(Proiezione proiezioneSelezionata, int posti) {

    boolean haPostiDisponibili = proiezioneSelezionata.haPostiDisponibili(posti);

    if (haPostiDisponibili == true) {
      // prenota
      Prenotazione prenotazione = new Prenotazione(this.getIDUtente(), this.getNome(), this.getCognome(),
          proiezioneSelezionata.getDataOra(),
          proiezioneSelezionata.getFilm().getTitolo(), posti, proiezioneSelezionata.getPrezzoBiglietto() );

      return Prenotazione.aggiungiPrenotazioneAlCSV(prenotazione);

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
                  sceltaCosto = sc.nextInt();
                  // cercaProiezione(sceltaCosto)e restituscimi la proiezione scelta dal utente
                  break;
                case 5:
                  // BISOGNA INSERIRE IL MENU CON LE PROIEZIONI E LUTENTE DEBE SELEZIONARE LA
                  // PROIEZIONE CHE VUOLE PRENOTARE!!
                  // chiamare il creaPrenotazione per creare la prenotazione, il risultato di
                  // questo metodo è booleano
                  LocalDate data = LocalDate.parse("2027-12-28");
                  LocalTime orario = LocalTime.parse("15:30");
                  LocalDateTime dataOra = data.atTime(orario); // Combina data e ora
                  Proiezione proiezioneTest = new Proiezione(
                      new Film("Blue Velvet",
                          "Zombie", "NitReg", 2020, 3, 2),
                      LocalDateTime.now(),
                      10);
                  this.creaPrenotazione(proiezioneTest, 3);
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
              sceltaPrenotazioni = sc.nextInt();

              switch (sceltaPrenotazioni) {
                case 1:
                  System.out.println("---VISUALIZZA PRENOTAZIONI---");

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
                    scletaModificaPrenotazione = sc.nextInt();
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
                        sceltaDiModifica = sc.nextInt();

                        switch (sceltaDiModifica) {
                          case 1:

                              System.out.println("scrivi quanti posti vuoi");
                              numeroDiPosti = sc.nextInt();
                              // TODO: chiamo un metodo che mi ristituisce la proiezione corrente
                              Proiezione temp = null;
                              boolean hapostidisponibili = temp.haPostiDisponibili(numeroDiPosti);
                              if (hapostidisponibili == true) {

                                Prenotazione nuovaPrenotazione = new Prenotazione(elemento.getIDPrenotazione(),
                                    elemento.getIDUtente(), elemento.getNome(), elemento.getCognome(),
                                    elemento.getProiezione_Data(), elemento.getProiezione_Titolo(), numeroDiPosti, elemento.getPrezzoBiglietto());
                                this.modificaPrenotazione(scletaModificaPrenotazione, nuovaPrenotazione);

                              } else {
                                System.out.println("non ci sono posti disponibili");
                              }
                            break;


                            case 2: 
                            //ricerca proiezione quella che ci deve restituire la proiezione
                            //l'utente sceglie la proiezione
                              Proiezione proiezioneSelezionata = null; 
                              System.out.println("scrivi quanti posti vuoi");
                              numeroDiPosti = sc.nextInt();
                              Proiezione proiezionetemp = null;
                              boolean haposti = proiezionetemp.haPostiDisponibili(numeroDiPosti);
                              if (haposti == true) {

                                Prenotazione nuovaPrenotazione = new Prenotazione(elemento.getIDPrenotazione(),
                                    elemento.getIDUtente(), elemento.getNome(), elemento.getCognome(),
                                    elemento.getProiezione_Data(), elemento.getProiezione_Titolo(), numeroDiPosti, elemento.getPrezzoBiglietto());
                                this.modificaPrenotazione(scletaModificaPrenotazione, nuovaPrenotazione);

                              } else {
                                System.out.println("non ci sono posti disponibili");
                              }
                              
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
                  sceltaPrenotazioni = sc.nextInt();
                  break;
                // TODO: cuando el cliente escoja un id se tiene que preguntar si esta seguro,
                // en caso de si eliminar, en caso de no, volver a atras

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

}
