/*
 * Copyright (c) 2020. http://hiteshsahu.com- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * If you use or distribute this project then you MUST ADD A COPY OF LICENCE
 * along with the project.
 *  Written by Hitesh Sahu <hiteshkrsahu@Gmail.com>, 2017.
 */

/**
 *
 */
package com.ecommerce.retailapp.model.entities;

import com.ecommerce.retailapp.json.BaseJsonModel;


public class Product extends BaseJsonModel {


    /**
     * The item short desc.
     */
    private String description = "";

    /**
     * The item detail.
     */
    private String longDescription = "";

    /**
     * The mrp.
     */
    private String mrp;

    /**
     * The discount.
     */
    private String discount;

    /**
     * The sell mrp.
     */
    private String salePrice;

    /**
     * The quantity.
     */
    private String orderQty;

    /**
     * The image url.
     */
    private String imageUrl = "";

    /**
     * The item name.
     */
    private String productName = "";

    private String productId = "";

    private String shopName;

    private String subCategoryName;

    private String productImageLocal;


    public String getProductImageLocal() {
        return productImageLocal;
    }

    public void setProductImageLocal(String productImageLocal) {
        this.productImageLocal = productImageLocal;
    }

    public Product() {
    }

    /**
     * @param itemName
     * @param itemShortDesc
     * @param itemDetail
     * @param MRP
     * @param discount
     * @param sellMRP
     * @param quantity
     * @param imageURL
     */

    public Product(String itemName, String itemShortDesc, String itemDetail,
                   String MRP, String discount, String sellMRP, String quantity,
                   String imageURL, String orderId) {
        this.productName = itemName;
        this.description = itemShortDesc;
        this.longDescription = itemDetail;
        this.mrp = MRP;
        this.discount = discount;
        this.salePrice = sellMRP;
        this.orderQty = quantity;
        this.imageUrl = imageURL;
        this.productId = orderId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String __type = getClass().getSimpleName();

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getItemName() {
        return productName;
    }

    public void setItemName(String itemName) {
        this.productName = itemName;
    }

    public String getItemShortDesc() {
        return description;
    }

    public void setItemShortDesc(String itemShortDesc) {
        this.description = itemShortDesc;
    }

    public String getItemDetail() {
        return longDescription;
    }

    public void setItemDetail(String itemDetail) {
        this.longDescription = itemDetail;
    }

    public String getMRP() {
        return this.mrp;
    }

    public void setMRP(String MRP) {
        this.mrp = MRP;
    }

    public String getDiscount() {
        return discount + "%";
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDiscountNumeric() {
        return discount;
    }

    public String getSellMRP() {
        return salePrice;
    }

    public void setSellMRP(String sellMRP) {
        this.salePrice = sellMRP;
    }

    public String getQuantity() {
        return orderQty;
    }

    public void setQuantity(String quantity) {
        this.orderQty = quantity;
    }

    public String getImageURL() {
        return imageUrl;
    }

    public void setImageURL(String imageURL) {
        this.imageUrl = imageURL;
    }

}
