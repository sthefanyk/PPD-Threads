import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class WikiMonoThreaded {

    public static CompletableFuture<String> getWikiPageExistence(String wikiPageUrl) {
        return CompletableFuture.supplyAsync(() -> {
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
        });
    }

    public static void main(String[] args) {
        System.out.println("Executando...");

        List<CompletableFuture<String>> futures = new ArrayList<>();

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 50; i++) {
            String wikiPageUrl = "https://en.wikipedia.org/wiki/" + (i + 1);
            CompletableFuture<String> future = getWikiPageExistence(wikiPageUrl);
            futures.add(future);
        }

        try {
            List<String> results = futures.stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList());

            results.forEach(System.out::println);

            long endTime = System.currentTimeMillis();
            double elapsedTime = (endTime - startTime) / 1000.0;
            System.out.println("Tempo total de execução: " + elapsedTime + " segundos");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
