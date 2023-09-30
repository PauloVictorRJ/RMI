package juntosRMI;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class JuntosServer {

    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);
            JuntosImpl juntosImpl = new JuntosImpl();
            Naming.rebind("Juntos", juntosImpl);
            bemVindo();
        } catch (RemoteException e) {
            System.err.println("Erro ao iniciar o servidor: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    static public void bemVindo() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("              ,---------------------------,");
        System.out.println("              |  /---------------------\\  |");
        System.out.println("              | |    Seja bem vindo!    | |");
        System.out.println("              | |          by           | |");
        System.out.println("              | |    Jonatan Torres     | |");
        System.out.println("              | |   Paulo Rodrigues     | |");
        System.out.println("              | |     Paulo Victor      | |");
        System.out.println("              |  \\_____________________/  |");
        System.out.println("              |___________________________|");
        System.out.println("            ,---\\_____     []     _______/------,");
        System.out.println("          /         /______________\\           /|");
        System.out.println("        /___________________________________ /  | ___");
        System.out.println("        |                                   |   |    )");
        System.out.println("        |  _ _ _                 [-------]  |   |   (");
        System.out.println("        |  o o o                 [-------]  |  /    )_");
        System.out.println("        |__________________________________ |/     /  /");
        System.out.println("    /-------------------------------------/|      ( )/");
        System.out.println("  /-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/ /");
        System.out.println("-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/ /");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("");
        System.out.println("Servidor do Jogo 'Juntos' está ativo.");
    }
}
