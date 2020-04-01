/*
 * Copyright (c) 2017. http://hiteshsahu.com- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * If you use or distribute this project then you MUST ADD A COPY OF LICENCE
 * along with the project.
 *  Written by Hitesh Sahu <hiteshkrsahu@Gmail.com>, 2017.
 */

package com.ecommerce.retailapp.domain.mock;

import android.content.Context;

import com.example.connectionframework.requestframework.sender.SenderBridge;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ecommerce.retailapp.model.CenterRepository;
import com.ecommerce.retailapp.model.entities.Product;
import com.ecommerce.retailapp.model.entities.ProductCategoryModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/*
 * This class serve as fake server and provides dummy product and category with real Image Urls taken from flipkart
 */
public class FakeWebServer {

    private static FakeWebServer fakeServer;
    private static SenderBridge senderBridge;
    private Gson gson = new Gson();
    private List<Object> data;

    public static FakeWebServer getFakeWebServer() {

        if (null == fakeServer) {
            fakeServer = new FakeWebServer();
        }
        return fakeServer;
    }


    public void addCategory(Context context) {

        Type founderListType = new TypeToken<ArrayList<ProductCategoryModel>>(){}.getType();
        senderBridge = new SenderBridge(context);

        data = senderBridge.sendMessage(RequestFunction.getCategories());

        if (data !=null) {
            ArrayList<ProductCategoryModel> productCategoryModels = gson.fromJson(gson.toJson(data.get(0)),
                    founderListType);
            CenterRepository.getCenterRepository().setListOfCategory(productCategoryModels);
        }else
            CenterRepository.getCenterRepository().setListOfCategory(null);
    }


    public void getAllProductsOfCategory(String categoryName, Context context) {
        Type founderListType = new TypeToken<ConcurrentHashMap<String, ArrayList<Product>>>(){}.getType();

        ConcurrentHashMap<String, ArrayList<Product>> productMap =
                new ConcurrentHashMap<String, ArrayList<Product>>();
        senderBridge = new SenderBridge(context);
        data = senderBridge.sendMessage(RequestFunction.getProductsOfCategory(categoryName));

        if (data != null) {
            productMap = gson.fromJson(gson.toJson(data.get(0)), founderListType);


            CenterRepository.getCenterRepository().setMapOfProductsInCategory(productMap);
        }else
            CenterRepository.getCenterRepository().setMapOfProductsInCategory(null);

    }

    public void getAllProducts(int productCategory, Context context) {
        String categoryName = CenterRepository.getCenterRepository()
                                .getListOfCategory()
                                 .get(productCategory).categoryName;

            getAllProductsOfCategory(categoryName, context);

    }

}
