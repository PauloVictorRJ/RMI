package juntosRMI;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class JuntosClient extends UnicastRemoteObject implements JuntosClientInterface {

    static JuntosInterface servidor;
    static JuntosClient cliente;
    static char letraAtual;
    static String nome;

    protected JuntosClient() throws RemoteException {
    }

    @Override
    public void receberMensagem(String mensagem) throws RemoteException {
        System.out.println(mensagem);
    }

    @Override
    public void receberLetra(char letra) throws RemoteException {
        String corVermelha = "\u001B[31m";
        String resetarCor = "\u001B[0m";
        System.out.println("A letra é: " + corVermelha + letra + resetarCor);
        letraAtual = letra;
        this.enviarPalavra();
    }

    @Override
    public void pegarNome() throws RemoteException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Informe seu nome:");
        if (sc.hasNext()) {
            nome = sc.next();
        } else {
            nome = "Nome Inválido";
        }
        System.out.println("Seja bem vindo " + nome + " !");
    }

    @Override
    public String enviarNome() throws RemoteException {
        return nome;
    }

    public static void main(String[] args) {
        bemVindo();
        try {
            servidor = (JuntosInterface) Naming.lookup("rmi://localhost/Juntos");
            cliente = new JuntosClient();
            servidor.conectar(cliente);
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void enviarPalavra() throws RemoteException {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("Informe a palavra:");
            String palavra = sc.next().toUpperCase();
            if (palavra.startsWith(String.valueOf(letraAtual))) {
                try {
                    servidor.enviarPalavra(palavra, cliente);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            System.out.println("Erro! A palavra deve iniciar com a letra " + letraAtual);
        }

    }

    static public void bemVindo() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
