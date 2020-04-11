/*
 * Copyright (c) 2017. http://hiteshsahu.com- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * If you use or distribute this project then you MUST ADD A COPY OF LICENCE
 * along with the project.
 *  Written by Hitesh Sahu <hiteshkrsahu@Gmail.com>, 2017.
 */

package com.ecommerce.retailapp.domain.mock;


import java.util.HashMap;
import java.util.Map;

public class CheckSetup {

    public static class ServerActions {
        public static int ECOMMERCE_GET_CATEGORIES = 0;
        public static int ECOMMERCE_GET_ALL_PRODUCTS_OF_CATEGORY = 1;
        public static int ECOMMERCE_MAKE_AN_ORDER = 2;
        public static final int ECOMMERCE_LIST_OF_SHOPS = 3;
        public static final int ECOMMERCE_LIST_OF_SEARCHED_PRODUCTS = 4;
        public static final int ECOMMERCE_PRODUCTS_OFFLINE = 5;

    }

    public static class Activities {
        public static int LOG_IN_ACTIVITY = 0;
        public static int DASHBOARD_ACTIVITY = 1;
        public static int COURSE_EXAMPLE_ACTIVITY = 2;
        public static int HOME_ACTIVITY = 3;
        public static int SINGLE_POST_ACTIVITY = 4;
        public static int MESSAGES_ACTIVITY = 5;
    }



}
