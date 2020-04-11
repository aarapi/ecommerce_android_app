/*
 * Copyright (c) 2017. http://hiteshsahu.com- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * If you use or distribute this project then you MUST ADD A COPY OF LICENCE
 * along with the project.
 *  Written by Hitesh Sahu <hiteshkrsahu@Gmail.com>, 2017.
 */

package com.ecommerce.retailapp.model;

import com.ecommerce.retailapp.model.entities.Product;
import com.ecommerce.retailapp.model.entities.ProductCategoryModel;
import com.ecommerce.retailapp.model.entities.ShopModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CenterRepository {

    private static CenterRepository centerRepository;

    private ArrayList<ProductCategoryModel> listOfCategory;
    private ConcurrentHashMap<String, ArrayList<Product>> mapOfProductsInCategory = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, ArrayList<Product>> mapAllProducts;
    private List<Product> listOfProductsInShoppingList = Collections.synchronizedList(new ArrayList<Product>());
    private ArrayList<ShopModel> listOfShop = new ArrayList<>();
    private ArrayList<ShopModel> shopsOfCategory = new ArrayList<>();
    private List<Product> listOfSearchedProducts = new ArrayList<>();
    private List<Set<String>> listOfItemSetsForDataMining = new ArrayList<>();

    public static CenterRepository getCenterRepository() {

        if (null == centerRepository) {
            centerRepository = new CenterRepository();
        }
        return centerRepository;
    }

    public ArrayList<ShopModel> getListOfShop() {
        return listOfShop;
    }

    public void setListOfShop(ArrayList<ShopModel> listOfShop) {
        this.listOfShop = listOfShop;
    }

    public List<Product> getListOfProductsInShoppingList() {
        return listOfProductsInShoppingList;
    }

    public void setListOfProductsInShoppingList(ArrayList<Product> getShoppingList) {
        this.listOfProductsInShoppingList = getShoppingList;
    }

    public Map<String, ArrayList<Product>> getMapOfProductsInCategory() {

        return mapOfProductsInCategory;
    }

    public void setMapOfProductsInCategory(ConcurrentHashMap<String, ArrayList<Product>> mapOfProductsInCategory) {
        this.mapOfProductsInCategory = mapOfProductsInCategory;
    }

    public ConcurrentHashMap<String, ArrayList<Product>> getMapAllProducts() {
        return mapAllProducts;
    }

    public void setMapAllProducts(ConcurrentHashMap<String, ArrayList<Product>> mapAllProducts) {
        this.mapAllProducts = mapAllProducts;
    }

    public ArrayList<ProductCategoryModel> getListOfCategory() {

        return listOfCategory;
    }

    public void setListOfCategory(ArrayList<ProductCategoryModel> listOfCategory) {
        this.listOfCategory = listOfCategory;
    }

    public List<Set<String>> getItemSetList() {

        return listOfItemSetsForDataMining;
    }

    public void addToItemSetList(HashSet list) {
        listOfItemSetsForDataMining.add(list);
    }

    public List<Product> getListOfSearchedProducts() {
        return listOfSearchedProducts;
    }

    public void setListOfSearchedProducts(List<Product> listOfSearchedProducts) {
        this.listOfSearchedProducts = listOfSearchedProducts;
    }

    public ArrayList<ShopModel> getShopsOfCategory() {
        return shopsOfCategory;
    }

    public void setShopsOfCategory(ArrayList<ShopModel> shopsOfCategory) {
        this.shopsOfCategory = shopsOfCategory;
    }
}
