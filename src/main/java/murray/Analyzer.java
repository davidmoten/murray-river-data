package murray;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.davidmoten.kool.Stream;

import com.github.davidmoten.guavamini.Preconditions;

public final class Analyzer {

    public static void main(String[] args) {

        File file = new File("../murray-river-data-downloads");
        Map<String, List<String>> fileColumnLabels = new HashMap<>();
        Stream //
                .fromArray(file.listFiles()) //
                .filter(f -> f.getName().endsWith(".csv")) //
                .doOnNext(f -> {
                    String line = Stream.lines(f).get(2).get().get();
                    List<String> list = Arrays.asList(line.split(","));
                    fileColumnLabels.put(id(f.getName()), list);
                }) //
                .doOnNext(System.out::println) //
                .go();
        
        fileColumnLabels.entrySet().forEach(System.out::println);
    }

    private static String id(String filename) {
        int i = filename.indexOf("_");
        Preconditions.checkArgument(i >=0);
        return filename.substring(0, i);
    }

}
