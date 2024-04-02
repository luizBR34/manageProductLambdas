package com.ecommerce.aws.lambda.dto;

public class Product {

    private int productId;
    private String productName;
    private String productDescription;
    private double productPrice;
    private int productStock;
    private String productImageUrl;

    public Product() {
    }

    public Product(int productId, String productName, String productDescription, double productPrice, int productStock, String productImageUrl) {
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productStock = productStock;
        this.productImageUrl = productImageUrl;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductStock() {
        return productStock;
    }

    public void setProductStock(int productStock) {
        this.productStock = productStock;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productDescription='" + productDescription + '\'' +
                ", productPrice=" + productPrice +
                ", productStock=" + productStock +
                ", productImageUrl='" + productImageUrl + '\'' +
                '}';
    }
}
