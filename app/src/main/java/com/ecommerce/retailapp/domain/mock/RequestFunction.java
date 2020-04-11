/*
 * Copyright (c) 2017. http://hiteshsahu.com- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * If you use or distribute this project then you MUST ADD A COPY OF LICENCE
 * along with the project.
 *  Written by Hitesh Sahu <hiteshkrsahu@Gmail.com>, 2017.
 */

package com.ecommerce.retailapp.domain.mock;


import com.ecommerce.retailapp.model.entities.Product;
import com.example.connectionframework.requestframework.sender.Request;
import com.example.connectionframework.requestframework.sender.RequestFunctions;

import java.util.ArrayList;
import java.util.List;

public class RequestFunction {

    public static Request getAllProductsOffline(){
        List<Object> params = new ArrayList<>();
        return RequestFunctions.createRequest(CheckSetup.ServerActions.ECOMMERCE_PRODUCTS_OFFLINE, params);
    }

    public static Request getCategories(){
        List<Object> params = new ArrayList<>();
        return RequestFunctions.createRequest(CheckSetup.ServerActions.ECOMMERCE_GET_CATEGORIES, params);
    }
    public static Request getShopList(String categoryName){
        List<Object> params = new ArrayList<>();
        params.add(categoryName);
        return RequestFunctions.createRequest(CheckSetup.ServerActions.ECOMMERCE_LIST_OF_SHOPS, params);
    }
    public static Request getSearchedProducts(String searchString){
        List<Object> params = new ArrayList<>();
        params.add(searchString);
        return RequestFunctions.createRequest(CheckSetup.ServerActions.ECOMMERCE_LIST_OF_SEARCHED_PRODUCTS, params);
    }
    public static Request getProductsOfCategory(String categoryName){
        List<Object> params = new ArrayList<>();
        params.add(categoryName);
        return RequestFunctions.createRequest(CheckSetup.ServerActions.ECOMMERCE_GET_ALL_PRODUCTS_OF_CATEGORY, params);
    }

    public static Request makeAnOrder(List<Product> productList, String... userInfo){
        List<Object> params = new ArrayList<>();
        params.add(productList);
        params.add(userInfo);

        return RequestFunctions.createRequest(CheckSetup.ServerActions.ECOMMERCE_MAKE_AN_ORDER, params);
    }



}
