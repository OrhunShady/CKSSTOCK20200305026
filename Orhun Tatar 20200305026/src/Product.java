import java.io.Serializable;

class Product<T extends Number> implements StockOperations<T>, Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private int stock;
    private T price;

    public Product(String name, T price) {
        this.name = name;
        this.price = price;
        this.stock = 0;
    }

    @Override
    public void addStock(int quantity) {
        this.stock += quantity;
        System.out.println("Added " + quantity + " items to " + name + " stock. Current stock: " + stock);
    }

    @Override
    public void sell(int quantity) {
        if (quantity <= this.stock) {
            this.stock -= quantity;
            System.out.println("Sold " + quantity + " items from " + name + " stock. Current stock: " + stock);
        } else {
            System.out.println("Not enough " + name + " stock available.");
        }
    }

    @Override
    public int getStock() {
        return this.stock;
    }

    @Override
    public double getPrice() {
        return this.price.doubleValue();
    }

    @Override
    public void updatePrice(double newPrice) {
        this.price = (T) Double.valueOf(newPrice);
        System.out.println("Price for " + name + " updated to: $" + newPrice);
    }

    public String getName() {
        return name;
    }
}