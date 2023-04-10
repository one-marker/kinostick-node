package kinostick.stream.model;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class Memory {

    private Map<String, Movie> idMovieMap = new HashMap<>();
    private Map<String, Movie> magnetMovieMap = new HashMap<>();

    public String getMagnetMovieMap(String magnet) {
        return magnetMovieMap.containsKey(magnet) ? magnetMovieMap.get(magnet).getUuid() : null;
    }

    public String putMovie(Movie movie) {

        idMovieMap.put(movie.getUuid(), movie);
        magnetMovieMap.put(movie.getMagnet(), movie);

        return movie.getUuid();
    }

    public String getUrlById(String uuid) {
        return idMovieMap.get(uuid) == null ? null : idMovieMap.get(uuid).getUrl();
    }

    public Movie getMovieById(String uuid) {
        return idMovieMap.get(uuid);
    }

    public String removeMovieById(String uuid) {
        Movie movie = idMovieMap.get(uuid);
        if (movie == null) {
            return "Фильм не найден!";
        }
        movie.getProcess().destroy();
        magnetMovieMap.remove(movie.getMagnet());
        idMovieMap.remove(uuid);
        return "Фильм удален!";
    }

    public Map<String, Movie> getIdMovieMap() {
        return idMovieMap;
    }

    public Map<String, Movie> getMagnetMovixeMap() {
        return magnetMovieMap;
    }
}
