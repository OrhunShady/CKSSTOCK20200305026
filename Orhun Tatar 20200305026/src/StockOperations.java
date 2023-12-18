interface StockOperations<T extends Number> {
    void addStock(int quantity);

    void sell(int quantity);

    int getStock();

    double getPrice();

    void updatePrice(double newPrice);
}