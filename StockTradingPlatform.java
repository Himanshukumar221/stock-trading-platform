
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

class Stock {
    String symbol;
    String name;
    double price;

    Stock(String symbol, String name, double price) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
    }

    void updatePrice(double newPrice) {
        this.price = newPrice;
    }
}

class Portfolio {
    Map<String, Integer> holdings = new HashMap<>();
    double cashBalance = 10000.0;

    void buyStock(String symbol, int quantity, double price) {
        double totalCost = quantity * price;
        if (totalCost > cashBalance) {
            System.out.println(" Insufficient balance!");
            return;
        }
        holdings.put(symbol, holdings.getOrDefault(symbol, 0) + quantity);
        cashBalance -= totalCost;
        System.out.println(" Bought " + quantity + " of " + symbol);
    }

    void sellStock(String symbol, int quantity, double price) {
        if (!holdings.containsKey(symbol) || holdings.get(symbol) < quantity) {
            System.out.println(" Not enough stock to sell!");
            return;
        }
        holdings.put(symbol, holdings.get(symbol) - quantity);
        if (holdings.get(symbol) == 0) holdings.remove(symbol);
        cashBalance += quantity * price;
        System.out.println(" Sold " + quantity + " of " + symbol);
    }

    void displayPortfolio(Map<String, Stock> market) {
        System.out.println("\n Portfolio Summary:");
        double totalValue = cashBalance;
        for (String symbol : holdings.keySet()) {
            int qty = holdings.get(symbol);
            double price = market.get(symbol).price;
            System.out.println(symbol + ": " + qty + " shares @ $" + price + " = $" + (qty * price));
            totalValue += qty * price;
        }
        System.out.printf("Cash: $%.2f\n", cashBalance);
        System.out.printf("Total Portfolio Value: $%.2f\n", totalValue);
    }

    void saveToFile(String filename) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        writer.write("Cash:" + cashBalance + "\n");
        for (String symbol : holdings.keySet()) {
            writer.write(symbol + ":" + holdings.get(symbol) + "\n");
        }
        writer.close();
    }

    void loadFromFile(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        holdings.clear();
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("Cash:")) {
                cashBalance = Double.parseDouble(line.split(":")[1]);
            } else {
                String[] parts = line.split(":");
                holdings.put(parts[0], Integer.parseInt(parts[1]));
            }
        }
        reader.close();
    }
}

public class StockTradingPlatform {
    static Map<String, Stock> market = new HashMap<>();
    static Portfolio portfolio = new Portfolio();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        loadSampleStocks();

        while (true) {
            System.out.println("\n===== STOCK TRADING PLATFORM =====");
            System.out.println("1. View Market");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. Save Portfolio");
            System.out.println("6. Load Portfolio");
            System.out.println("7. Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> displayMarket();
                case 2 -> buy();
                case 3 -> sell();
                case 4 -> portfolio.displayPortfolio(market);
                case 5 -> savePortfolio();
                case 6 -> loadPortfolio();
                case 7 -> System.exit(0);
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    static void loadSampleStocks() {
        market.put("AAPL", new Stock("AAPL", "Apple Inc.", 150.0));
        market.put("GOOG", new Stock("GOOG", "Alphabet Inc.", 2700.0));
        market.put("TSLA", new Stock("TSLA", "Tesla Inc.", 700.0));
        market.put("AMZN", new Stock("AMZN", "Amazon.com Inc.", 3200.0));
    }

    static void displayMarket() {
        System.out.println("\n Market Data:");
        for (Stock s : market.values()) {
            System.out.printf("%s (%s): $%.2f\n", s.name, s.symbol, s.price);
        }
    }

    static void buy() {
        System.out.print("Enter Stock Symbol: ");
        String symbol = sc.next().toUpperCase();
        if (!market.containsKey(symbol)) {
            System.out.println(" Stock not found!");
            return;
        }
        System.out.print("Enter quantity to buy: ");
        int qty = sc.nextInt();
        portfolio.buyStock(symbol, qty, market.get(symbol).price);
    }

    static void sell() {
        System.out.print("Enter Stock Symbol: ");
        String symbol = sc.next().toUpperCase();
        if (!market.containsKey(symbol)) {
            System.out.println(" Stock not found!");
            return;
        }
        System.out.print("Enter quantity to sell: ");
        int qty = sc.nextInt();
        portfolio.sellStock(symbol, qty, market.get(symbol).price);
    }

    static void savePortfolio() {
        try {
            portfolio.saveToFile("portfolio.txt");
            System.out.println(" Portfolio saved!");
        } catch (IOException e) {
            System.out.println(" Error saving portfolio.");
        }
    }

    static void loadPortfolio() {
        try {
            portfolio.loadFromFile("portfolio.txt");
            System.out.println(" Portfolio loaded!");
        } catch (IOException e) {
            System.out.println(" Error loading portfolio.");
        }
    }
}

    

