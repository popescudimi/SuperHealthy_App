package com.example.superhealthyapp.fragments.Food;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.superhealthyapp.adapters.FoodAdapter;
import com.example.superhealthyapp.R;
import com.example.superhealthyapp.activities.MainActivity;
import com.example.superhealthyapp.adapters.ApplicationViewModel;
import com.example.superhealthyapp.fragments.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class FoodFragment extends BaseFragment implements
        View.OnClickListener, SearchView.OnQueryTextListener {
    private EditText editTextManual;
    private FoodAdapter foodAdapter;
    private int preferenceCurrent;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_food, container, false);

        if(getActivity() != null) {
            ((MainActivity) getActivity()).updateToolbarTitle(getString(R.string.title_food));
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeRecyclerView(view);
        initializeSearchView(view);
        editTextManual = view.findViewById(R.id.editTextAddManually);
        Button buttonAddManually = view.findViewById(R.id.buttonAddCaloryManually);
        buttonAddManually.setOnClickListener(this);
        editTextManual = view.findViewById(R.id.editTextAddManually);

        //initializeEditText(view);

    }
    private void initializeSearchView(View view){
        SearchView searchView = view.findViewById(R.id.searchViewFood);
        searchView.setActivated(true);
        searchView.setQueryHint(getString(R.string.search));
        searchView.onActionViewExpanded();
        searchView.setIconified(false);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(this);
    }

    private void initializeRecyclerView(View view){
        final List<ApplicationViewModel> foodList = new ArrayList<>();
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewFoodList);
        foodAdapter = new FoodAdapter(getContext(), ApplicationViewModel.foodItems(foodList), new FoodAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ApplicationViewModel food) {
                switchToFoodDetailFragment(food);
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(foodAdapter);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.buttonAddCaloryManually:
                addPreferenceValues();
            }
    }

    private void switchToFoodDetailFragment(ApplicationViewModel food) {
        if(getActivity() != null) {
            Fragment fragment = new FoodDetailFragment();
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.content_frame, fragment);
            getActivity().getIntent().putExtra("foodName", food.getFoodName());
            getActivity().getIntent().putExtra("foodDetail",food.getFoodDescription());
            getActivity().getIntent().putExtra("foodCalory",food.getCaloriesNumber());
            getActivity().getIntent().putExtra("foodURL",food.getFoodDownload());
            transaction.commit();
        }
    }
    private void addPreferenceValues(){
        int additionalFoodAmount;
        try {
            additionalFoodAmount = Integer.parseInt((editTextManual.getText().toString()));
        }catch (Exception x){
            x.getMessage();
            additionalFoodAmount = 0;
            Toast.makeText(getContext(),getString(R.string.out_of_range),Toast.LENGTH_LONG).show();
        }
        preferenceCurrent = additionalFoodAmount;
        putSharedPreferencesData();
        editTextManual.setText("");
        editTextManual.clearFocus();
        hideKeyboard();
    }
    @Override
    public boolean onQueryTextSubmit(String s) {
        foodAdapter.getFilter().filter(s);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        foodAdapter.getFilter().filter(s);
        return false;
    }
    private void putSharedPreferencesData(){
        if(getActivity() != null){
            SharedPreferences.Editor sharedPreferences =
                    getActivity().getSharedPreferences("calory",Context.MODE_PRIVATE).edit();
            int dailyCalory = getActivity().getSharedPreferences("calory",Context.MODE_PRIVATE)
                    .getInt("totalcalory",0);
            dailyCalory += preferenceCurrent;
            sharedPreferences.putInt("totalcalory", dailyCalory);
            sharedPreferences.apply();
            Toast.makeText(getContext(),R.string.food_added_calory,Toast.LENGTH_LONG).show();
        }
    }
    private void hideKeyboard(){
        if (getActivity() != null) {
            if (getActivity().getCurrentFocus() != null) {
                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
}