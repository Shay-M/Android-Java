package com.silverhorse.freegames.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.silverhorse.freegames.R;
import com.silverhorse.freegames.adapters.GameAdapter;
import com.silverhorse.freegames.adapters.OnGameListener;
import com.silverhorse.freegames.viewModel.ListGameViewModel;

public class ListGameFragment extends Fragment implements OnGameListener {
    private ListGameViewModel mlistGameViewModel;

    private RecyclerView mRecyclerViewGames;
    private GameAdapter mGameAdapter;

    public ListGameFragment() {
        // Required empty public constructor
    }

    // top bar
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {


        // contact
        inflater.inflate(R.menu.menu_game, menu);

        //// @ Search
        MenuItem searchItem = menu.findItem(R.id.app_bar_search);

        SearchView searchView = (SearchView) searchItem.getActionView();

        // Keyboard to show button Done
        // searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mGameAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);

    }

    // for arrange by date
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_sort_by_name) {
            mlistGameViewModel.sortByName();

        } else if (item.getItemId() == R.id.action_sort_by_name2) {
            Log.d("TAG", "onOptionsItemSelected: " + "action_sort_by_name2");

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true); // need in fragment for top bar


        // create instance when 'onCreate' call if we have get it if not make new
        mlistGameViewModel = new ViewModelProvider(this)
                .get(ListGameViewModel.class);
        // androidx.lifecycle.ViewModelProvider
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerViewGames = view.findViewById(R.id.recycler_view_games);
        mRecyclerViewGames.setHasFixedSize(true);

        mGameAdapter = new GameAdapter(this);

        mRecyclerViewGames.setAdapter(mGameAdapter);
        mRecyclerViewGames.setLayoutManager(new LinearLayoutManager(getActivity()));

        mlistGameViewModel.getGamesList()
                .observe(getViewLifecycleOwner(), gamesList -> {
                    if (gamesList != null) {
                        mGameAdapter.setGameList(gamesList);
                    }
                });
    }

    @Override
    public void onGameClick(int position, View itemView) {
        Log.d("ListGameFragment", "onGameClick: ");

        ListGameFragmentDirections.ActionListGameFragmentToGameInfoFragment action =
                ListGameFragmentDirections.actionListGameFragmentToGameInfoFragment(mGameAdapter.getSelectedGame(position));
        Navigation.findNavController(mRecyclerViewGames).navigate(action);
//        Navigation.findNavController(itemView).navigate(R.id.action_listGameFragment_to_gameInfoFragment);
    }
}