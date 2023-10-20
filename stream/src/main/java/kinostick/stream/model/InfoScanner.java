package kinostick.stream.model;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

@Service
public class InfoScanner {

    public List<TorrentFile> scan(String magnet) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<List<TorrentFile>> future = executor.submit(new InfoThread(magnet));

        try {
            System.out.println("Started..");
            return future.get(3, TimeUnit.SECONDS);

        } catch (TimeoutException e) {
            future.cancel(true);
            System.out.println("Terminated!");
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        executor.shutdownNow();
        return Collections.emptyList();
    }
}
