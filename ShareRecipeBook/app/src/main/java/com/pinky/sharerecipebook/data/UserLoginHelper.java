package com.pinky.sharerecipebook.data;/* Created by Shay Mualem 03/10/2021 */

import com.pinky.sharerecipebook.models.User;

public class UserLoginHelper {

    private static UserLoginHelper instead;
    //    private RecipeDetailsViewModel recipeDetailsViewModel;
    private Boolean UserIsLogin = false;
    private User loginUser = new User();

    public static UserLoginHelper getInstance() {
        if (instead == null)
            instead = new UserLoginHelper();
        return instead;
    }


    public void setLoginUser(User loginUser) {
        this.loginUser = loginUser;
        UserIsLogin = true;
    }

    public void setLogoutUser() {
        this.loginUser = null;
        loginUser = new User();
        UserIsLogin = false;
    }

    public Boolean getIsLoggedIn() {
        return UserIsLogin;
    }


}
