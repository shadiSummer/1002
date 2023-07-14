import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class Edge {
    int source;
    int destination;
    int bandwidth;
    int delay;

    Edge(int source, int destination, int bandwidth, int delay) {
        this.source = source;
        this.destination = destination;
        this.bandwidth = bandwidth;
        this.delay = delay;
    }
}

class Graph {
    Map<Integer, List<Edge>> adjacencyList;
    int maxVertex;

    Graph() {
        adjacencyList = new HashMap<>();
        maxVertex = 0;
    }

    void addEdge(int source, int destination, int bandwidth, int delay) {
        Edge edge = new Edge(source, destination, bandwidth, delay);
        adjacencyList.computeIfAbsent(source, k -> new ArrayList<>()).add(edge);
        maxVertex = Math.max(maxVertex, Math.max(source, destination));
    }

    List<List<Integer>> findAllPaths(int source, int destination) {
        List<List<Integer>> paths = new ArrayList<>();
        boolean[] visited = new boolean[maxVertex + 1]; // Increase size by 1
        List<Integer> path = new ArrayList<>();
        path.add(source);
        findAllPathsDFS(source, destination, visited, path, paths);
        return paths;
    }

    void findAllPathsDFS(int vertex, int destination, boolean[] visited, List<Integer> path, List<List<Integer>> paths) {
        visited[vertex] = true;

        if (vertex == destination) {
            paths.add(new ArrayList<>(path));
        } else {
            List<Edge> edges = adjacencyList.get(vertex);
            if (edges != null) {
                for (Edge edge : edges) {
                    int nextVertex = edge.destination;
                    if (!visited[nextVertex]) {
                        List<Integer> newPath = new ArrayList<>(path);
                        newPath.add(nextVertex);
                        findAllPathsDFS(nextVertex, destination, visited, newPath, paths);
                    }
                }
            }
        }

        visited[vertex] = false;
    }


    List<List<Integer>> sortPathsByMaxBandwidth(List<List<Integer>> paths) {
        paths.sort(Comparator.comparing(this::calculateMaxBandwidth).reversed());
        return paths;
    }

    int calculateMaxBandwidth(List<Integer> path) {
        int maxBandwidth = Integer.MAX_VALUE;
        for (int i = 0; i < path.size() - 1; i++) {
            int source = path.get(i);
            int destination = path.get(i + 1);
            List<Edge> edges = adjacencyList.get(source);
            if (edges != null) {
                for (Edge edge : edges) {
                    if (edge.destination == destination) {
                        maxBandwidth = Math.min(maxBandwidth, edge.bandwidth);
                    }
                }
            }
        }
        return maxBandwidth;
    }

    int calculateDelay(List<Integer> path) {
        int totalDelay = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            int source = path.get(i);
            int destination = path.get(i + 1);
            List<Edge> edges = adjacencyList.get(source);
            if (edges != null) {
                for (Edge edge : edges) {
                    if (edge.destination == destination) {
                        totalDelay += edge.delay;
                    }
                }
            }
        }
        return totalDelay;
    }


    
}

public class PathFinder {
    public static void main(String[] args) {
        String filename = "graph.txt"; // Provide the name of your input file here

        // Get the source and destination vertices from the user
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the source vertex: ");
        int start = scanner.nextInt();
        System.out.print("Enter the destination vertex: ");
        int end = scanner.nextInt();

        Graph graph = readGraphFromFile(filename);
        List<List<Integer>> paths = graph.findAllPaths(start, end);
        List<List<Integer>> sortedPaths = graph.sortPathsByMaxBandwidth(paths);

        for (List<Integer> path : sortedPaths) {
            System.out.println("Path: " + path);
            System.out.println("Max Bandwidth: " + graph.calculateMaxBandwidth(path));
            System.out.println("Delay: " + graph.calculateDelay(path));
            System.out.println();
        }
    }
    
    private static Graph readGraphFromFile(String filename) {
        Graph graph = new Graph();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] data = line.substring(1, line.length() - 1).split(",");
                if (data.length < 3) {
                    // Handle invalid lines with less than three values
                    System.out.println("Invalid line: " + line);
                    continue;
                }

                int source = Integer.parseInt(data[0].trim());
                int destination = Integer.parseInt(data[1].trim());
                int bandwidth = Integer.parseInt(data[2].trim());

                // Check if the delay value is provided
                int delay = (data.length > 3) ? Integer.parseInt(data[3].trim()) : 0;

                graph.addEdge(source, destination, bandwidth, delay);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return graph;
    }
}
