import java.util.HashMap;
import java.util.Map;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.io.PrintWriter;
import java.util.stream.Stream;



class Node {
    String label;
    Map<Character, Node> children;

    public Node(String label) {
        this.label = label;
        this.children = new HashMap<>();
    }
}

class SuffixTree {
    Node root;

    public SuffixTree(String string) {
        root = new Node("");
        buildTree(string);
    }

    private void buildTree(String string) {
        for (int i = 0; i < string.length(); i++) {
            Node node = root;
            for (int j = i; j < string.length(); j++) {
                char c = string.charAt(j);
                if (!node.children.containsKey(c)) {
                    node.children.put(c, new Node(String.valueOf(c)));
                }
                node = node.children.get(c);
            }
        }
    }

    public boolean search(String substring) {
        Node node = root;
        for (char c : substring.toCharArray()) {
            if (!node.children.containsKey(c)) {
                return false;
            }
            node = node.children.get(c);
        }
        return true;
    }

    public static void main(String[] args) throws IOException {
        Path inputFile = Paths.get("input.txt");
        List<String> lines = Files.readAllLines(inputFile);
        // System.out.println(lines);
        String comparisonValue = lines.get(1).split(":")[1];
        String folderPath = lines.get(0).split(":")[1];
        Path folder = Paths.get(folderPath);
        try (Stream<Path> files = Files.walk(folder)) {
            files.forEach(file -> {
                if (Files.isRegularFile(file)) {
                    try {
                        String line = Files.readString(file);
                        SuffixTree suffixTree = new SuffixTree(line);
                        if (suffixTree.search(comparisonValue)) {
                            try (PrintWriter writer = new PrintWriter(new FileWriter("result.csv", true))) {
                                writer.println(file.toAbsolutePath());
                            }
                        }
                    } catch (IOException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
            });
        }

        
    }
}