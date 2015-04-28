import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Servidor {
	public static void main(String[] args) {
		String palavracorreta = "eclipse"; // palavra a ser descoberta
		String chute; // letra que o jogador chutou
		int chances = 3; // determina quantas vezes o jogador pode errar
		int acertos = 0; // contador de letras acertadas
		
		StringBuffer palavraTemp = trocaLetras(palavracorreta); // palavra temporária
		
		try {
			ServerSocket servidor = new ServerSocket(6100);
			System.out.println("Servidor rodando. Aguardando conexao...");
			Socket socket = servidor.accept();
			System.out.println("Conectado a - " + socket.getInetAddress() + ":" + socket.getPort());
			
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			DataInputStream in = new DataInputStream(socket.getInputStream());
			
			out.writeUTF("Palavra: " + palavraTemp +"\nDigite uma letra:");	// enviando a palavra em asteriscos para o jogador]
			chute = in.readUTF();
			
			do{				
				if (temLetra(palavracorreta, chute)) {
					palavraTemp = colocaLetraCerta(palavracorreta, palavraTemp, chute); 
					acertos++;
					
					if(acertos == (palavracorreta.length()-1)){
						out.writeUTF("Parabens! Voce acertou! Palavra certa: " + palavracorreta + ".");
						break;
					}
					
					out.writeUTF("Palavra: " + palavraTemp + "\nDigite uma letra: ");
				}
				else{
					chances--;
					
					if(chances < 1){
						out.writeUTF("Voce perdeu! A palavra certa era: " + palavracorreta);
						break;
					}
					
					out.writeUTF("A palavra nao tem essa letra! Tente novamente... (chances: " + chances + ")"
								+ "\nPalavra: " + palavraTemp + "\nDigite uma letra:");
				}
				
				chute = in.readUTF(); // lendo o chute do jogador
			}while(true);
			
			socket.close();
			out.writeUTF("Fim do jogo!");
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// troca todas as letras da palavra correta por *
	public static StringBuffer trocaLetras(String palavra){ 
		StringBuffer trocada = new StringBuffer();
		
		for (int i = 0; i < palavra.length(); i++) {
			trocada.insert(i, "*");
		}
		
		return trocada;
	}
	
	// verifica se a palavra correta tem a letra chute
	public static boolean temLetra(String palavra, String chute){ 
		int count = 0;
		
		for (int i = 0; i < palavra.length(); i++) {
			if (chute.equals(String.valueOf(palavra.charAt(i)))) {
				count++;
			}
		}
		
		if (count > 0) {
			return true;
		}
		else{
			return false;
		}
	}
	
	// troca o * pela letra certa na palavra temporária
	public static StringBuffer colocaLetraCerta(String palavracorreta, StringBuffer palavraTemp, String letra){	
		for (int i = 0; i < palavracorreta.length(); i++) {
			if(letra.equals(String.valueOf(palavracorreta.charAt(i)))){
				palavraTemp.deleteCharAt(i);
				palavraTemp.insert(i, letra);
			}
		}
		
		return palavraTemp;
	}
}
