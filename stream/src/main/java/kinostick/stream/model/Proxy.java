package kinostick.stream.model;

import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Component
public class Proxy {

    private String PROXY_PATH = "/etc/nginx/conf.d/include/proxy-%s.conf";

    private String NGINX_PROXY_TEMPLATE = "location %s {\n" +
            "  proxy_pass http://127.0.0.1:%d;\n" +
            "}";

    private Integer port;


    public void createProxy(String name,Integer port, String url) {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(String.format(PROXY_PATH, name)), StandardCharsets.UTF_8))) {
            writer.write(fillTemplate(NGINX_PROXY_TEMPLATE, url, port));
        }
        catch (IOException ex) {
            // Handle me
        }
    }

    public boolean removeProxy(String name) {
        return new File(String.format(PROXY_PATH, name)).delete();
    }

    private String fillTemplate(String template, String url, Integer port) {
        return String.format(template, url, port);
    }



}
