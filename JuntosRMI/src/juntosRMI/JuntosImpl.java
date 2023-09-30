package juntosRMI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JuntosImpl extends UnicastRemoteObject implements JuntosInterface {

    private JuntosClientInterface jogador1;
    private JuntosClientInterface jogador2;
    private char letraAtual;
    private String palavra1;
    private String palavra2;
    private ExecutorService executorService;
    private CountDownLatch latch;

    public JuntosImpl() throws RemoteException {
        super();
        jogador1 = null;
        jogador2 = null;
        executorService = Executors.newFixedThreadPool(2);
    }

    @Override
    public void conectar(JuntosClientInterface cliente) throws RemoteException {
        try {
            cliente.pegarNome();
            if (jogador1 == null) {
                jogador1 = cliente;
                enviarMensagem("Você é o jogador 1. Aguardando o jogador 2...", jogador1);
            } else if (jogador2 == null) {
                jogador2 = cliente;
                enviarMensagem("Você é o jogador 2.", jogador2);
                enviarMensagemTodos("Iniciando partida...");
                iniciarJogo();
            } else {
                cliente.receberMensagem("O jogo já está em andamento. Aguarde sua vez.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void desconectar(JuntosClientInterface cliente) throws RemoteException {
        if (cliente.equals(jogador1)) {
            jogador1 = null;
        } else if (cliente.equals(jogador2)) {
            jogador2 = null;
        }

        try {
            enviarMensagem("Você foi desconectado.", cliente);

            if (jogador1 == null || jogador2 == null) {
                enviarMensagemTodos("Um jogador desconectou. O jogo foi encerrado.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void enviarPalavra(String palavra, JuntosClientInterface cliente) throws RemoteException {
        try {
            System.out.println("Recebi a palavra " + palavra + " de " + cliente.enviarNome());
            if (palavra1 == null) {
                palavra1 = palavra;
                enviarMensagem("Aguardando o outro jogador...", cliente);
            } else {
                palavra2 = palavra;
                gerarResultado();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void iniciarJogo() throws RemoteException {
        gerarLetra();
        System.out.println("Gerei a letra " + letraAtual);
        iniciarClienteAsync(jogador1);
        iniciarClienteAsync(jogador2);
    }

    private void iniciarClienteAsync(JuntosClientInterface cliente) throws RemoteException {
        executorService.execute(() -> {
            try {
                enviarMensagem("O jogo começou!", cliente);
                enviarLetra(cliente);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    private void gerarLetra() throws RemoteException {
        Random random = new Random();
        char letraAleatoria = (char) (random.nextInt(26) + 'A');
        letraAtual = letraAleatoria;
    }

    private void enviarLetra(JuntosClientInterface cliente) throws RemoteException {
        try {
            cliente.receberLetra(letraAtual);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void gerarResultado() throws RemoteException {
        String corVermelha = "\u001B[31m";
        String resetarCor = "\u001B[0m";
        String corVerde = "\u001B[32m";

        if (palavra1.equals(palavra2)) {
            try {
                enviarMensagemTodos("Parabéns, vocês venceram! A palavra é: " + palavra1);
                enviarMensagemTodos("Obrigado por jogar!");
                System.out.println(corVerde + "Parabéns, vocês venceram! A palavra é: " + palavra1 + resetarCor);
                System.out.println("Game Over!");
            } catch (Exception e) {
                e.printStackTrace();
            }
            desconectar(jogador1);
            desconectar(jogador2);
        } else {
            try {
                enviarMensagemTodos("Vocês perderam! As palavras não coincidem!");
                System.out.println(corVermelha + "Vocês perderam! As palavras não coincidem!" + resetarCor);
                palavra1 = null;
                palavra2 = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            iniciarJogo();
        }
    }

    private void enviarMensagem(String mensagem, JuntosClientInterface cliente) throws RemoteException {
        try {
            cliente.receberMensagem(mensagem);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void enviarMensagemTodos(String mensagem) throws RemoteException {
        try {
            if (jogador1 != null) {
                jogador1.receberMensagem(mensagem);
            }
            if (jogador2 != null) {
                jogador2.receberMensagem(mensagem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
