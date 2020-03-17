package murray;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.davidmoten.kool.Stream;

import com.github.davidmoten.guavamini.Preconditions;

public final class Analyzer {

    public static void main(String[] args) throws FileNotFoundException {

        File file = new File("../murray-river-data-downloads");
        Map<String, List<String>> fileColumnLabels = new HashMap<>();
        Stream<File> files = Stream //
                .fromArray(file.listFiles())//
                .filter(f -> f.getName().endsWith(".csv"));

        files //
                .doOnNext(f -> {
                    String line = Stream.lines(f).get(2).get().get();
                    List<String> list = Arrays.asList(line.split(","));
                    fileColumnLabels.put(id(f.getName()), list);
                }) //
                .go();

        Stream<String> output = files //
                .flatMap(f -> Stream //
                        .lines(f) //
                        .skip(3) //
                        .flatMap(line -> {
                            String id = id(f.getName());
                            String[] items = line.split(",");
                            String time = items[0];
                            String s = id + "," + time;
                            List<String> list = new ArrayList<>();
                            for (int i = 1; i < items.length; i++) {
                                String key = fileColumnLabels.get(id).get(i);
                                String value = items[i].trim();
                                if (!value.equals("\"\"") && value.length() > 0) {
                                    list.add(s + "," + key + "," + value);
                                }
                            }
                            return Stream.from(list);
                        }));

        output.takeLast(100) //
                .doOnNext(System.out::println) //
                .go();
        
        try (PrintStream out = new PrintStream(new File("target/all.csv"))) {
            output.forEach(line -> out.println(line));
        };
    }

    private static String id(String filename) {
        int i = filename.indexOf("_");
        Preconditions.checkArgument(i >= 0);
        return filename.substring(0, i);
    }

}
