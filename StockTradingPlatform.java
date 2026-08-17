//TASK 1: Stock Trading Platform

//Simulate a basic stock trading system.

//Display market data and implement buy/sell operations.

//Track user portfolio performance.

//Apply OOP concepts to manage users, stocks, and transactions.

//Optional: File I/O or database for data persistence.

import java.io.*;
import java.util.*;
public class StockTradingPlatform {

    // ================= STOCK CLASS =================
    static class Stock {
        private String symbol;
        private String companyName;
        private double price;

        public Stock(String symbol, String companyName, double price) {
            this.symbol = symbol;
            this.companyName = companyName;
            this.price = price;
        }

        public String getSymbol() {
            return symbol;
        }

        public String getCompanyName() {
            return companyName;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public void displayStock() {
            System.out.printf("%-10s %-20s ₹%.2f%n",
                    symbol, companyName, price);
        }
    }

    // ================= TRANSACTION CLASS =================
    static class Transaction {
        private String type;
        private String stockSymbol;
        private int quantity;
        private double price;

        public Transaction(String type, String stockSymbol,
                           int quantity, double price) {
            this.type = type;
            this.stockSymbol = stockSymbol;
            this.quantity = quantity;
            this.price = price;
        }

        @Override
        public String toString() {
            return type + " | " + stockSymbol +
                    " | Quantity: " + quantity +
                    " | Price: ₹" + price;
        }
    }

    // ================= PORTFOLIO CLASS =================
    static class Portfolio {

        private HashMap<String, Integer> holdings = new HashMap<>();
        private ArrayList<Transaction> transactions = new ArrayList<>();

        // Buy stock
        public void buyStock(Stock stock, int quantity) {

            if (quantity <= 0) {
                System.out.println("Invalid quantity!");
                return;
            }

            holdings.put(
                    stock.getSymbol(),
                    holdings.getOrDefault(stock.getSymbol(), 0) + quantity
            );

            transactions.add(
                    new Transaction(
                            "BUY",
                            stock.getSymbol(),
                            quantity,
                            stock.getPrice()
                    )
            );

            System.out.println(
                    "Successfully bought " + quantity +
                    " shares of " + stock.getSymbol()
            );
        }

        // Sell stock
        public void sellStock(Stock stock, int quantity) {

            if (quantity <= 0) {
                System.out.println("Invalid quantity!");
                return;
            }

            int available =
                    holdings.getOrDefault(stock.getSymbol(), 0);

            if (quantity > available) {
                System.out.println(
                        "Not enough shares to sell!"
                );
                return;
            }

            holdings.put(
                    stock.getSymbol(),
                    available - quantity
            );

            transactions.add(
                    new Transaction(
                            "SELL",
                            stock.getSymbol(),
                            quantity,
                            stock.getPrice()
                    )
            );

            System.out.println(
                    "Successfully sold " + quantity +
                    " shares of " + stock.getSymbol()
            );
        }

        // Display portfolio
        public void displayPortfolio(
                HashMap<String, Stock> market) {

            System.out.println("\n========== YOUR PORTFOLIO ==========");

            double totalValue = 0;

            if (holdings.isEmpty()) {
                System.out.println("Portfolio is empty.");
                return;
            }

            for (String symbol : holdings.keySet()) {

                int quantity = holdings.get(symbol);

                Stock stock = market.get(symbol);

                if (stock != null && quantity > 0) {

                    double value =
                            quantity * stock.getPrice();

                    totalValue += value;

                    System.out.printf(
                            "%s : %d shares | Current Value: ₹%.2f%n",
                            symbol,
                            quantity,
                            value
                    );
                }
            }

            System.out.println("------------------------------------");
            System.out.printf(
                    "Total Portfolio Value: ₹%.2f%n",
                    totalValue
            );
        }

        // Display transaction history
        public void displayTransactions() {

            System.out.println(
                    "\n========== TRANSACTION HISTORY =========="
            );

            if (transactions.isEmpty()) {
                System.out.println("No transactions yet.");
                return;
            }

            for (Transaction transaction : transactions) {
                System.out.println(transaction);
            }
        }

        // Save portfolio to file
        public void saveToFile() {

            try {

                FileWriter writer =
                        new FileWriter("portfolio.txt");

                writer.write("========== PORTFOLIO ==========\n");

                for (String symbol : holdings.keySet()) {

                    writer.write(
                            symbol + " : " +
                            holdings.get(symbol) +
                            " shares\n"
                    );
                }

                writer.write("\n========== TRANSACTIONS ==========\n");

                for (Transaction transaction : transactions) {

                    writer.write(
                            transaction.toString() + "\n"
                    );
                }

                writer.close();

                System.out.println(
                        "Portfolio saved successfully!"
                );

            } catch (IOException e) {

                System.out.println(
                        "Error saving portfolio: " +
                        e.getMessage()
                );
            }
        }
    }

    // ================= USER CLASS =================
    static class User {

        private String name;
        private double balance;
        private Portfolio portfolio;

        public User(String name, double balance) {

            this.name = name;
            this.balance = balance;
            this.portfolio = new Portfolio();
        }

        public String getName() {
            return name;
        }

        public double getBalance() {
            return balance;
        }

        public Portfolio getPortfolio() {
            return portfolio;
        }

        // Add money
        public void addMoney(double amount) {

            if (amount > 0) {
                balance += amount;
                System.out.println(
                        "₹" + amount +
                        " added successfully."
                );
            }
        }

        // Buy stock with balance checking
        public void buyStock(Stock stock, int quantity) {

            double cost =
                    stock.getPrice() * quantity;

            if (cost > balance) {

                System.out.println(
                        "Insufficient balance!"
                );
                return;
            }

            balance -= cost;

            portfolio.buyStock(
                    stock,
                    quantity
            );
        }

        // Sell stock and receive money
        public void sellStock(Stock stock, int quantity) {

            int currentQuantity =
                    portfolio.holdings.getOrDefault(
                            stock.getSymbol(),
                            0
                    );

            if (quantity > currentQuantity) {

                System.out.println(
                        "You don't have enough shares!"
                );
                return;
            }

            double amount =
                    stock.getPrice() * quantity;

            balance += amount;

            portfolio.sellStock(
                    stock,
                    quantity
            );
        }

        public void displayAccount() {

            System.out.println(
                    "\n========== ACCOUNT =========="
            );

            System.out.println(
                    "User: " + name
            );

            System.out.printf(
                    "Available Balance: ₹%.2f%n",
                    balance
            );
        }
    }

    // ================= MAIN METHOD =================
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // Market stocks
        HashMap<String, Stock> market =
                new HashMap<>();

        market.put(
                "TCS",
                new Stock(
                        "TCS",
                        "Tata Consultancy Services",
                        3500
                )
        );

        market.put(
                "INFY",
                new Stock(
                        "INFY",
                        "Infosys",
                        1800
                )
        );

        market.put(
                "RELIANCE",
                new Stock(
                        "RELIANCE",
                        "Reliance Industries",
                        2900
                )
        );

        market.put(
                "HDFC",
                new Stock(
                        "HDFC",
                        "HDFC Bank",
                        1700
                )
        );

        // Create user
        System.out.print("Enter your name: ");
        String name = sc.nextLine();

        User user =
                new User(name, 100000);

        int choice;

        do {

            System.out.println(
                    "\n===================================="
            );

            System.out.println(
                    "       STOCK TRADING PLATFORM"
            );

            System.out.println(
                    "===================================="
            );

            System.out.println("1. Display Market Data");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. View Transactions");
            System.out.println("6. View Account");
            System.out.println("7. Add Money");
            System.out.println("8. Save Portfolio");
            System.out.println("9. Exit");

            System.out.print("Enter your choice: ");

            choice = sc.nextInt();

            switch (choice) {

                // Market Data
                case 1:

                    System.out.println(
                            "\n========== MARKET DATA =========="
                    );

                    System.out.printf(
                            "%-10s %-20s %s%n",
                            "Symbol",
                            "Company",
                            "Price"
                    );

                    for (Stock stock : market.values()) {
                        stock.displayStock();
                    }

                    break;

                // Buy
                case 2:

                    System.out.print(
                            "Enter stock symbol: "
                    );

                    String buySymbol =
                            sc.next().toUpperCase();

                    Stock buyStock =
                            market.get(buySymbol);

                    if (buyStock == null) {

                        System.out.println(
                                "Stock not found!"
                        );

                        break;
                    }

                    System.out.print(
                            "Enter quantity: "
                    );

                    int buyQuantity =
                            sc.nextInt();

                    user.buyStock(
                            buyStock,
                            buyQuantity
                    );

                    break;

                // Sell
                case 3:

                    System.out.print(
                            "Enter stock symbol: "
                    );

                    String sellSymbol =
                            sc.next().toUpperCase();

                    Stock sellStock =
                            market.get(sellSymbol);

                    if (sellStock == null) {

                        System.out.println(
                                "Stock not found!"
                        );

                        break;
                    }

                    System.out.print(
                            "Enter quantity: "
                    );

                    int sellQuantity =
                            sc.nextInt();

                    user.sellStock(
                            sellStock,
                            sellQuantity
                    );

                    break;

                // Portfolio
                case 4:

                    user.getPortfolio()
                            .displayPortfolio(market);

                    break;

                // Transactions
                case 5:

                    user.getPortfolio()
                            .displayTransactions();

                    break;

                // Account
                case 6:

                    user.displayAccount();

                    break;

                // Add money
                case 7:

                    System.out.print(
                            "Enter amount: ₹"
                    );

                    double amount =
                            sc.nextDouble();

                    user.addMoney(amount);

                    break;

                // File I/O
                case 8:

                    user.getPortfolio()
                            .saveToFile();

                    break;

                // Exit
                case 9:

                    System.out.println(
                            "Thank you for using Stock Trading Platform!"
                    );

                    break;

                default:

                    System.out.println(
                            "Invalid choice!"
                    );
            }

        } while (choice != 9);

        sc.close();
    }
}