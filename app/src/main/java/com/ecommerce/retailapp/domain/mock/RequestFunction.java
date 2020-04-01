/*
 * Copyright (c) 2017. http://hiteshsahu.com- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * If you use or distribute this project then you MUST ADD A COPY OF LICENCE
 * along with the project.
 *  Written by Hitesh Sahu <hiteshkrsahu@Gmail.com>, 2017.
 */

package com.ecommerce.retailapp.domain.mock;


import com.example.connectionframework.requestframework.sender.Request;
import com.example.connectionframework.requestframework.sender.RequestFunctions;

import java.util.ArrayList;
import java.util.List;

public class RequestFunction {


    public static Request getCategories(){
        List<Object> params = new ArrayList<>();
        return RequestFunctions.createRequest(CheckSetup.ServerActions.ECOMMERCE_GET_CATEGORIES, params);
    }
    public static Request getProductsOfCategory(String categoryName){
        List<Object> params = new ArrayList<>();
        params.add(categoryName);
        return RequestFunctions.createRequest(CheckSetup.ServerActions.ECOMMERCE_GET_ALL_PRODUCTS_OF_CATEGORY, params);
    }

    public static Request getPostData(int activityId){
        List<Object> params = new ArrayList<>();
        return RequestFunctions.createRequest(activityId, CheckSetup.ServerActions.ANNOYING_PROJECTS_HOME_DATA, params);
    }
    public static Request getUserProfileData(int activityId){
        List<Object> params = new ArrayList<>();
        return RequestFunctions.createRequest(activityId, CheckSetup.ServerActions.ANNOYING_PROJECTS_USER_PROFILE, params);
    }

    public static Request getDashboardData(int activityId) {
        List<Object> params = new ArrayList<>();
        return RequestFunctions.createRequest(activityId, CheckSetup.ServerActions.ANNOYING_PROJECTS_DASHBOARD_ACTIVITY, params);
    }

    public static Request getPersonalData(int activityId) {
        List<Object> params = new ArrayList<>();
        return RequestFunctions.createRequest(activityId, CheckSetup.ServerActions.ANNOYING_PROJECTS_DASHBOARD_ACTIVITY, params);
    }

    public static Request createNewPost(int activityId, List<String> postData){
        List<Object> params = new ArrayList<>();
        params.add(postData);
        return RequestFunctions.createRequest(activityId, CheckSetup.ServerActions.ANNOYING_PROJECTS_CREATE_NEW_POST, params);

    }
}
