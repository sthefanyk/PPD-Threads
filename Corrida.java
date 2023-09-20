import java.util.Random;

class CorridaThread extends Thread {
    private static final int MAX_PASSOS = 5;
    private static final int DISTANCIA_ALVO = 50;

    private int id;
    private int totalPassos = 0;

    public CorridaThread(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        int posicaoAtual = 0;
        System.out.println("Thread " + id + " - Iniciando");

        while (posicaoAtual < DISTANCIA_ALVO) {
            int passos = gerarNumerosAleatorios();
            totalPassos += passos;
            posicaoAtual += passos;

            System.out.println("Vez da thread " + id);
            System.out.println("Numero sorteado: " + passos);
            System.out.println("Thread " + id + " Andou " + passos + " casas");
            System.out.println("Posição atual da thread " + id + ": " + posicaoAtual);

            esperar(1);
        }

        System.out.println("Thread " + id + " - Chegou à posição " + DISTANCIA_ALVO + "! Total de passos: " + totalPassos);
    }

    private int gerarNumerosAleatorios() {
        Random rand = new Random();
        return rand.nextInt(MAX_PASSOS) + 1;
    }

    private void esperar(int segundos) {
        try {
            Thread.sleep(segundos * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getTotalPassos() {
        return totalPassos;
    }
}

public class Corrida {

    public static void main(String[] args) {
        int numThreads = 2;
        CorridaThread[] threads = new CorridaThread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new CorridaThread(i + 1);
            threads[i].start();
        }

        for (CorridaThread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        int indiceVencedor = encontrarVencedor(threads);
        System.out.println("Thread " + (indiceVencedor + 1) + " venceu com " + threads[indiceVencedor].getTotalPassos() + " passos");
    }

    private static int encontrarVencedor(CorridaThread[] threads) {
        int menorPassos = Integer.MAX_VALUE;
        int indiceVencedor = -1;

        for (int i = 0; i < threads.length; i++) {
            if (threads[i].getTotalPassos() < menorPassos) {
                menorPassos = threads[i].getTotalPassos();
                indiceVencedor = i;
            }
        }

        return indiceVencedor;
    }
}
