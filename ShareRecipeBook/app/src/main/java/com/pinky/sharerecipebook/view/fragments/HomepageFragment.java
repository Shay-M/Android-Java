package com.pinky.sharerecipebook.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.astritveliu.boom.Boom;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.pinky.sharerecipebook.R;
import com.pinky.sharerecipebook.adapters.RecipeAdapter;
import com.pinky.sharerecipebook.models.Recipe;
import com.pinky.sharerecipebook.models.User;
import com.pinky.sharerecipebook.repositories.AuthRepository;
import com.pinky.sharerecipebook.utils.HidesKeyboard;
import com.pinky.sharerecipebook.viewmodels.HomeViewModel;
import com.pinky.sharerecipebook.viewmodels.LogOutViewModel;

import java.util.ArrayList;
import java.util.Comparator;


public class HomepageFragment extends Fragment implements RecipeAdapter.RecyclerViewListener {

    User loginUser = new User();
    private Boolean UserIsLogin;
    private ArrayList<Recipe> recipeArrayList;
    private RecipeAdapter recipeAdapter;
    private FloatingActionButton floatingAddButton;
    private ActionBar actionBar;// = ((AppCompatActivity) getActivity()).getSupportActionBar();
    private View rootView;
    private Menu menu;

    private HomeViewModel homeViewModel;
    private LogOutViewModel logOutViewModel;

    //
    /*private String expanduserId ;
    private String expanduserMakeName ;*/


    // top bar
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if (actionBar != null) {
            actionBar.show();
            actionBar.setTitle("");
        }

        //MainActivity mA = ((MainActivity)getActivity());
        //mA.setActionBarColor(Color.parseColor("#FFA2C13E"));
        //actionBar.setTitle("Hello, " + loginUser.getName());
        this.menu = menu;


        inflater.inflate(R.menu.top_bar, menu);

        // search
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recipeAdapter.getFilter().filter(newText);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);

        if (UserIsLogin) {
            menu.findItem(R.id.action_logout).setTitle(getString(R.string.Logout));
        } else {
            menu.findItem(R.id.action_logout).setTitle(getString(R.string.Login));
        }

    }

    //call back user select item
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Log.d("action", "item: " + item);
        Bundle bundle;
        HidesKeyboard.hideKeyboard(this.getActivity());


        switch (item.getItemId()) {
            case R.id.action_settings:
                //bundle = new Bundle();
                //bundle.putSerializable("expandLoginUser", loginUser);
                actionBar.setTitle(R.string.settings);
                Navigation.findNavController(rootView).navigate(R.id.action_homepageFragment_to_settingsFragment);

                return true;

            case R.id.action_properties:
                bundle = new Bundle();
                bundle.putSerializable("expandLoginUser", loginUser);
                Navigation.findNavController(rootView).navigate(R.id.action_homepageFragment_to_userProfileFragment, bundle);

                return true;

            case R.id.action_favorites:
                // User chose the "Favorite" action, mark the current item
                if (UserIsLogin) {

                    if (item.isChecked()) {
                        recipeAdapter.getFilter().filter(null);
                        item.setIcon(R.drawable.ic_twotone_favorite_48);
                        actionBar.setTitle("");
                        item.setChecked(false);
                    } else {
                        Log.d("TAG", "onOptionsItemSelected: " + loginUser.getFavoriteRecipe());

                        recipeAdapter.setFavoriteRecipeList(loginUser.getFavoriteRecipe());
                        recipeAdapter.getFilter().filter("#fav");
                        item.setIcon(R.drawable.ic_baseline_favorite_48);
                        actionBar.setTitle("Your Favorite Recipes");
                        item.setChecked(true);
                    }
                } else {
                    bundle = new Bundle();
                    bundle.putSerializable("expandLoginUser", loginUser);
                    bundle.putSerializable("cameFromAddRecipeButton", false);
//                    bundle.putBoolean("cameFromAddRecipeButton", false);
                    Navigation.findNavController(rootView).navigate(R.id.action_homepageFragment_to_loginPageFragment, bundle);
                }


                return true;

            case R.id.action_logout:
                if (UserIsLogin) {
                    Log.d("action_logout", "onOptionsItemSelected: ");
                    Snackbar.make(this.getView(), R.string.msg_logout, BaseTransientBottomBar.LENGTH_LONG).show();
                    logOutViewModel.logOut();
                    loginUser = null;
                    loginUser = new User();
                    UserIsLogin = false;
                    item.setTitle(R.string.title_bar_login);

                } else {
//                    Snackbar.make(this.getView(), R.string.msg_needLogin, BaseTransientBottomBar.LENGTH_LONG).show();
                    bundle = new Bundle();
                    bundle.putSerializable("expandLoginUser", loginUser);
                    bundle.putSerializable("cameFromAddRecipeButton", false);
                    Navigation.findNavController(rootView).navigate(R.id.action_homepageFragment_to_loginPageFragment, bundle);
                }

                return true;

            case R.id.action_my_recipes:
                if (UserIsLogin) {
                    if (item.isChecked()) {
                        recipeAdapter.getFilter().filter(null);
                        actionBar.setTitle("");
                        item.setChecked(false);
                    } else {
                        recipeAdapter.getFilter().filter("#my" + loginUser.getFirebaseUserId());
                        actionBar.setTitle("Your Recipe");
                        item.setChecked(true);
                    }
                } else
                    //Navigation.findNavController(rootView).navigate(R.id.action_homepageFragment_to_loginPageFragment);
                    Snackbar.make(this.getView(), R.string.msg_needLogin, BaseTransientBottomBar.LENGTH_LONG).show();

                return true;

            default:
                recipeAdapter.getFilter().filter(null);
                return super.onOptionsItemSelected(item);

        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //MainActivity main = (MainActivity) getContext();


        recipeArrayList = new ArrayList<>();

        UserIsLogin = false;

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        homeViewModel.init();
        recipeArrayList = homeViewModel.getRecipeLiveData().getValue();

        homeViewModel.initUserLogin();
        loginUser = homeViewModel.getUserLiveData().getValue();

        ////

        logOutViewModel = new ViewModelProvider(this).get(LogOutViewModel.class);

        //logOutViewModel.init();
        //recipeArrayList = homeViewModel.getRecipeLiveData().getValue();

        //homeViewModel.initUserLogin();
        //loginUser = homeViewModel.getUserLiveData().getValue();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rootView = view;
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_of_recipe);
        recyclerView.setHasFixedSize(true);

        recipeAdapter = new RecipeAdapter(recipeArrayList, getActivity());
        recipeAdapter.setClicksListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recipeAdapter);

        if (menu != null && menu.findItem(R.id.action_logout) != null) {
            if (UserIsLogin) {
                menu.findItem(R.id.action_logout).setTitle(getString(R.string.Logout));
            } else {
                menu.findItem(R.id.action_logout).setTitle(getString(R.string.Login));
            }
        }

        // update
        homeViewModel.getRecipeLiveData().observe(getViewLifecycleOwner(),
                new Observer<ArrayList<Recipe>>() {
                    @Override
                    public void onChanged(ArrayList<Recipe> recipes) {
                        recipeAdapter.notifyDataSetChanged();
                        Log.d("loadRecipeViewModel", "onChanged: !!" + recipes);
                        recipeArrayList = recipes;
                        recipeArrayList.sort(Comparator.comparing(Recipe::getRank).reversed());
                    }
                });

        // get login user
        //if (AuthRepository.getInstance().getCurrentUser() != null) {
        homeViewModel.userDbLoginliveData.observe(getViewLifecycleOwner(),
                new Observer<User>() {
                    @Override
                    public void onChanged(User user) {
                        if (AuthRepository.getInstance().getCurrentUser() != null) {

                            //Log.d("if-loginUser", "onChanged: " + user.getName());
                            //Log.d("if-loginUser", "homeViewModel.getUserLiveData(): " + homeViewModel.getUserLiveData());
                            loginUser = homeViewModel.getUserLiveData().getValue();
                            UserIsLogin = true;
                        } else {
                            UserIsLogin = false;
                        }
                    }
                });

        // }


        // floating Add Button
        floatingAddButton = view.findViewById(R.id.floating_add_fragment);
        new Boom(floatingAddButton);

        floatingAddButton.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            HidesKeyboard.hideKeyboard(this.getActivity());
            if (AuthRepository.getInstance().getCurrentUser() != null) {
                bundle.putSerializable("expandLoginUser", loginUser);
                Navigation.findNavController(v).navigate(R.id.action_homepageFragment_to_addNewRecipeFragment, bundle);
            } else {
                bundle.putSerializable("expandLoginUser", loginUser);
                bundle.putSerializable("cameFromAddRecipeButton", true);
                Navigation.findNavController(v).navigate(R.id.action_homepageFragment_to_loginPageFragment, bundle);
            }
        });


        /*String expanduserId = (String) requireArguments().getSerializable("expanduserId");
        String expanduserMakeName = (String) requireArguments().getSerializable("expanduserMakeName");*/

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_page, container, false);
        setHasOptionsMenu(true);
        loginUser = homeViewModel.getUserLiveData().getValue();
        return rootView;
    }

    // https://www.geeksforgeeks.org/shared-element-transition-in-android-with-example/ // todo

    // Click on a item
    @Override
    public void onItemClick(int position, View view) {

        Recipe recipe = recipeArrayList.get(position);

        Bundle bundle = new Bundle();
        bundle.putSerializable("expandRecipe", recipe);
        //if (UserIsLogin)  loginUser = new User();
        bundle.putSerializable("expandLoginUser", loginUser);

        /*ImageView temp = (ImageView) view.findViewById(R.id.recipe_item_img);
        temp.setTransitionName("recipe_tran");

        FragmentNavigator.Extras extras = new FragmentNavigator
                .Extras
                .Builder()
                .addSharedElement(temp, "recipe_tran")
                .build();*/

//        Navigation.findNavController(view).navigate(R.id.action_homepageFragment_to_recipeDetailsFragment, bundle,null,extras);
        HidesKeyboard.hideKeyboard(this.getActivity());
        Navigation.findNavController(view).navigate(R.id.action_homepageFragment_to_recipeDetailsFragment, bundle);
    }

    // Click on a rating in item
    @Override
    public void onImgRatingClick(int position, View view) {
        Log.d("TAG", "onImgRatingClick: ");

    }

    /*public void showRecipeByUserId(String userId, String userName) {

        recipeAdapter.getFilter().filter("#my" + userId);
        actionBar.setTitle("Your Recipe");
        actionBar.setTitle("Recipes of " + userName);

    }*/


}
