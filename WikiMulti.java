import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WikiMulti {

    public static String getWikiPageExistence(String wikiPageUrl) {
        try {
            URL url = new URL(wikiPageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                return wikiPageUrl + " - Existe";
            } else if (responseCode == 404) {
                return wikiPageUrl + " - Não existe";
            } else {
                return wikiPageUrl + " - Desconhecido";
            }
        } catch (IOException e) {
            return wikiPageUrl + " - Erro: " + e.getMessage();
        }
    }

    public static void main(String[] args) {
        System.out.println("Executando...");

        int NUM_WORKERS = 4;
        List<String> wikiPageUrls = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            wikiPageUrls.add("https://en.wikipedia.org/wiki/" + (i + 1));
        }

        int chunkSize = wikiPageUrls.size() / NUM_WORKERS;
        List<List<String>> chunks = new ArrayList<>();
        for (int i = 0; i < wikiPageUrls.size(); i += chunkSize) {
            int end = Math.min(i + chunkSize, wikiPageUrls.size());
            chunks.add(wikiPageUrls.subList(i, end));
        }

        List<String> results = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();

        long startTime = System.currentTimeMillis();

        for (List<String> chunk : chunks) {
            Thread thread = new Thread(() -> {
                List<String> chunkResults = new ArrayList<>();
                for (String url : chunk) {
                    String result = getWikiPageExistence(url);
                    chunkResults.add(result);
                }
                synchronized (results) {
                    results.addAll(chunkResults);
                }
            });
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long endTime = System.currentTimeMillis();

        for (String result : results) {
            System.out.println(result);
        }

        double elapsedTime = (endTime - startTime) / 1000.0;
        System.out.println("Tempo total de execução: " + elapsedTime + " segundos");
    }
}
