package CineMaX;

public class Cliente extends Guest {

    //parametri //

    // private String IDCliente;
    //costruttore//

    public Cliente (String nome, String cognome, String password, int id ){
        super(nome, cognome, "C",password, id);
    }


      //metodi//
    // public String getIDCliente(){
    //     return this.IDCliente;
    // }
    //il set no si implementa perche non si deve mai modificare l'ID dalla creazione//
    
      //-inserire una prenotazione//

      //- cerca proiezione (implementato da guest)//
      // CercaFilm()

      //- visulazza proiezione (implementato da guest)//
      //DettagliFilm()

      //-visualizzare le propie prenotazioni//
      //crea prenotazione//
      //modifica prenotazione//
      //elimina prenotazione//

      //-modificare e cancellare le proprie prenotazioni//
      //-logout//


}
