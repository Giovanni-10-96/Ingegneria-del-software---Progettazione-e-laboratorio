/**
Costituire i gruppi

ciascuno di voi “offrirà” X>=0 pt per ciascun membro
una procedura sw troverà il team massimizzando il valore medio dei 2 gruppi che derivano dal partizionare 
in 2 i partecipanti

(Istruzioni complete alla fine del PDF: "(1) introduzione-al-corso.pdf")
 */
package fileConsegnare;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

public class estrazionegruppi {

	
	public static void main(String args[]) {
		
		//L'utente da in input il Path del file CSV da analizzare
		Scanner sc = new Scanner(System.in);
		
		System.out.print("Path per il file CSV = ");
		String file = sc.nextLine();
		
		
		//Leggere i valori della tabella e i nomi delle persone
		
		BufferedReader reader = null;
		
		String line = null;
		
		
		try {
			
			reader = new BufferedReader(new FileReader(file));
			
			//Leggo la prima riga in modo da ottenere i nomi di tutti i membri da dividere in gruppi
			
			line = reader.readLine();
			String[] row = line.split(";");
			String[] nomiMembri = new String[row.length-1];
			
			for(int i=1; i < row.length; i++) {
				
				nomiMembri[i-1] = row[i];
			}
			
			
			//Scansiono le righe successive
			int[][] valoriMembri = new int[nomiMembri.length][nomiMembri.length];
			
			int riga = 0;
			
			while( (line = reader.readLine()) != null ) {
				
				row = line.split(";");
				
				//Scansiono le righe successive (parto dalla seconda colonna perchè la prima contiene solo i nomi
				for(int colonna=1; colonna < row.length; colonna++) {
					
					if (row[colonna].equals("x") || row[colonna].equals("")) {
						
						//Se il valore all'interno della cella non è un numero oppure è vuota gli dò automaticamente 0
						valoriMembri[riga][colonna-1] = 0;
						
					} else {
						
						//Trasformo in un intero il valore contenuto nella tabella (da Stringa a Intero)
						String s = row[colonna];
						
						int c = Integer.parseInt(s);
						
						valoriMembri[riga][colonna-1] = c;

					}
					
				}
				
				//Passo alla riga successiva
				riga = riga + 1;
				
			}
			
			String gruppi;	//Stringa che conterrà i gruppi finali
			
			if(nomiMembri.length > 1) {
				//Uso la funzione per trovare i 2 gruppi che forniscono il massimo valore medio
				gruppi = partizionamento(valoriMembri, nomiMembri);
			
			} else {
				//Se la tabella contiene 1 sola persona allora ritorno solo il nome di quella persona come gruppo
				gruppi = nomiMembri[0];
			}
			
			
			
			//Stampo i gruppi ottenuti "(gruppo1) / (gruppo2)"
			System.out.println(gruppi);
			
			
		} catch(Exception e) {
			
			e.printStackTrace();
		}
		
		
	}
	
	
	
	//Supponiamo che i nomi siano in ordine ALFABETICO (sia per righe che per colonne)
	/*
				Alfredo Benny Chris Damian
	Alfredo		-		1	  2		4
	Benny		1		-	  5		2
	Chris		3		3	  -		4
	Damian		1		2	  3		-
	
	
	 */
	
	
	
	/**

	Funzione che dato in input:
		- un array contenente i nomi (stringhe) di persone/membri da dividere in 2 gruppi
	 	- una matrice/tabella con un gruppo di valori interi (contenente i punti che ciascun membro da ad un altro)
	 
	 Ritorna il team migliore (cioè i 2 gruppi di persone col valore medio dei punteggi più alto tra tutti i possibili
	 gruppi)
	 
	*/
	public static String partizionamento(int[][] valoriMembri, String[] nomiMembri) {
		
		//Divido i membri in 2 gruppi distinti
		int nMembri = nomiMembri.length;
		
		int[] gruppo1;
		int[] gruppo2;
		
		if(nMembri % 2 == 0) {
			
			//Se il numero dei membri è PARI allora i due gruppi avranno lo stesso numero di persone
			
			gruppo1 = new int[nMembri / 2];
			gruppo2 = new int[nMembri / 2];

		}else{
			
			//Se il numero dei membri è DISPARI allora uno dei due gruppi avrà una persona in più
			
			int metaMembri = nMembri / 2;
			
			gruppo1 = new int[metaMembri + 1];
			gruppo2 = new int[metaMembri];
		}
		
		
		//Calcolare la media massima ottenibile e il relativo partizionamento con cui la si ottiene
		double media = 0;
		double mediaMassima = 0;
		
		String partizionamentoMigliore = "";
		
		//Uso il for per scegliere il primo membro da selezionare per creare il gruppo1 
		for(int i=0; i < nMembri; i++) {
			
			int ultimoN = 0;	//variabile per capire qual è l'ultimo membro che è stato aggiunto nel gruppo1
			
			//Creazione del GRUPPO 1 (decido quali membri faranno parte del gruppo)
			for(int conta=0; conta < gruppo1.length; conta++) {
					
				gruppo1[conta] = (i+conta) % nomiMembri.length;
				ultimoN = i+conta;
				
			}
			
			ultimoN = (ultimoN + 1) % nomiMembri.length;	//la variabile conterrà il membro da cui partire per creare il gruppo2
			
			
			//Creazione del GRUPPO 2 (decido quali membri faranno parte del gruppo)
			for(int conta=0; conta < gruppo2.length; conta++) {
				
				gruppo2[conta] = (ultimoN+conta) % nomiMembri.length;
				
			}
			
			
			//Chiamo la funzione per calcolare il valore medio dei due gruppi appena creati
			media = calcoloMedia(valoriMembri, gruppo1, gruppo2);
			
			
			//Se la media appena ottenuta è MAGGIORE della media massima calcolata finora la sostituisco con questa
			if(media > mediaMassima) {
				
				mediaMassima = media;
				
				
				partizionamentoMigliore = "";	//Stringa che conterrà i 2 gruppi migliori (quelli col valore medio più alto)
				
				//Salviamo nella stringa i membri del GRUPPO 1
				for(int k=0; k < gruppo1.length; k++) {
					
					partizionamentoMigliore = partizionamentoMigliore.concat( nomiMembri[ gruppo1[k] ] );
					//ALTERNATIVA (per separare i nomi): partizionamentoMigliore = partizionamentoMigliore.concat( " " );
				}
				
				partizionamentoMigliore = partizionamentoMigliore.concat( " / " );	//Usiamo il carattere "/" per separare i 2 gruppi
				//ALTERNATIVA (da usare se si usano le altre ALTERNATIVE per separare i nomi): partizionamentoMigliore = partizionamentoMigliore.concat( "/ " );
				
				//Salviamo nella stringa i membri del GRUPPO 2
				for(int k=0; k < gruppo2.length; k++) {
					
					partizionamentoMigliore = partizionamentoMigliore.concat( nomiMembri[ gruppo2[k] ] );
					//ALTERNATIVA (per separare i nomi): partizionamentoMigliore = partizionamentoMigliore.concat( " " );
				}
			}
			
			
			
		}
		
		//Ritorniamo il MIGLIORE partizionamento dei 2 gruppi ottenuto (quello che ci ha dato il valore medio più alto)
		return partizionamentoMigliore;
	}
	

	
	/**
	Funzione che dato in input:
		- la tabella dei valori
		- 2 gruppi contenenti gli identificativi dei membri (ogni membro è identificato da un numero (es. 0 = ALfonso, 1 = Mateo, ...) ) 
		  che sono stati divisi in due gruppi
	
	Ritorna il valore medio dei punti dati dai membri dei due gruppi
	*/
	public static double calcoloMedia(int[][] valoriMembri, int[] gruppo1, int[] gruppo2) {
		
		double media;
		
		int totPuntiGruppo1 = 0;
		int totPuntiGruppo2 = 0;
		
		
		//Calcolo la somma di tutti i punti del GRUPPO 1
		for(int i=0; i < gruppo1.length; i++) {
			for(int j=0; j < gruppo1.length; j++) {
				
				//Solo se i!=j perché uno non può dare punti a sè stesso
				if(i != j) {
					
					//Aggiungo alla somma il punteggio che il membro "i" del GRUPPO 1 ha dato al membro "j"
					totPuntiGruppo1 = totPuntiGruppo1 + valoriMembri[ gruppo1[i] ][ gruppo1[j] ];

				}
			}
		}
		

		//Calcolo la somma di tutti i punti del GRUPPO 2
		for(int i=0; i < gruppo2.length; i++) {
			for(int j=0; j < gruppo2.length; j++) {
				
				//Solo se i!=j perché uno non può dare punti a sè stesso
				if(i != j) {
					
					//Aggiungo alla somma il punteggio che il membro "i" del GRUPPO 2 ha dato al membro "j"
					totPuntiGruppo2 = totPuntiGruppo2 + valoriMembri[ gruppo2[i] ][ gruppo2[j] ];
					
				}
			}
		}
		
		//Calcolo le medie dei due gruppi
		double mediaGruppo1 = (double) totPuntiGruppo1 / gruppo1.length;
		double mediaGruppo2 = (double) totPuntiGruppo2 / gruppo2.length;
		
		
		//Calcoliamo la media ottenuta dalle due medie appena calcolate e le restituisco come output
		media = ( mediaGruppo1 + mediaGruppo2 ) / 2;
		
		return media;
		
	}

}
