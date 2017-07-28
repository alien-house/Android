package com.example.shinji.asgrecyclerview;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.ListItemClickListener  {
    private ArrayList<Recipe> recipeList = new ArrayList<Recipe>();
    private RecyclerView recyclerViewList;
    private RecipeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        recyclerViewList = (RecyclerView) findViewById(R.id.recyclerViewRecipe);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewList.setLayoutManager(layoutManager);
        recyclerViewList.setHasFixedSize(true);

        if(savedInstanceState != null) {
            System.out.println("=============onCreate---------------");
            recipeList = savedInstanceState.getParcelableArrayList("LIST_INSTANCE_STATE");
        }else{
            createRecipeData();
        }

        mAdapter = new RecipeAdapter(recipeList, this);
        recyclerViewList.setAdapter(mAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        System.out.println("------------onSaveInstanceState---------------");
        outState.putParcelableArrayList("LIST_INSTANCE_STATE", recipeList);
        super.onSaveInstanceState(outState);
    }

    public void selectAll(View view){
        for(Recipe m : recipeList){
            m.setSelected(true);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void clearAll(View view){
        for(Recipe m : recipeList){
            m.setSelected(false);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void deleteMovie(View view){
        for(int i = recipeList.size() - 1; i >= 0; i--){
            if(recipeList.get(i).isSelected()){
                recipeList.remove(i);
            }
        }
        mAdapter.notifyDataSetChanged();
    }
    @Override
    public void onListItemClick(int index) {
        System.out.println("norma....l:"+index);
        Intent i = new Intent(MainActivity.this, PageActivity.class);
        Recipe recipe_tmp = recipeList.get(index);
        i.putExtra("RECIPEDATA", recipe_tmp);
        startActivity(i);

        //
//        Uri uri = Uri.parse("https://www.example.com");
//        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//        startActivity(intent);

    }
    @Override
    public void onListItemLongClick(int index) {
        System.out.println("Hello again!!!!!!!!!!!!!"+index);
        int itemPosition = index;
        Recipe recipe_tmp = recipeList.get(itemPosition);
        Recipe obj = null;
        try {
            obj = (Recipe) recipe_tmp.clone();
            recipeList.add(itemPosition+1,obj);
            mAdapter.notifyDataSetChanged();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void createRecipeData(){
        recipeList.add(
                new Recipe(
                        "Chiken Salada",
                        "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.",
                        getImgID("img01"),
                        "http://allrecipes.com/"
                )
        );
        recipeList.add(
                new Recipe(
                        "Colokke",
                        "jagajaga imo imo o isihi na n decency.",
                        getImgID("img02"),
                        "http://allrecipes.com/recipe/222111/healthier-brown-sugar-meatloaf/?internalSource=popular&referringContentType=home%20page&clickId=cardslot%2024"
                )
        );
        recipeList.add(
                new Recipe(
                        "Tomato Salada",
                        "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.",
                        getImgID("img03"),
                        "http://allrecipes.com/recipe/233762/chipotle-beef-tostadas/?internalSource=previously%20viewed&referringContentType=home%20page&clickId=cardslot%2042"
                )
        );
        recipeList.add(
                new Recipe(
                        "minesuterone",
                        "Two imprisoned  through acts of common decency.",
                        getImgID("img04"),
                        "http://allrecipes.com/recipe/233762/chipotle-beef-tostadas/?internalSource=previously%20viewed&referringContentType=home%20page&clickId=cardslot%2042")
        );
        recipeList.add(
                new Recipe(
                        "koresutete-na",
                        "Two imprisoned  through acts of common decency.",
                        getImgID("img05"),
                        "http://allrecipes.com/recipe/233762/chipotle-beef-tostadas/?internalSource=previously%20viewed&referringContentType=home%20page&clickId=cardslot%2042")
        );
        recipeList.add(
                new Recipe(
                        "koreiminaiwa",
                        "Two imprisoned  through acts of common decency.",
                        getImgID("img06"),
                        "http://allrecipes.com/recipe/233762/chipotle-beef-tostadas/?internalSource=previously%20viewed&referringContentType=home%20page&clickId=cardslot%2042")
        );
        recipeList.add(
                new Recipe(
                        "ashitanoimagoro",
                        "through acts of common decency Two imprisoned  .",
                        getImgID("img07"),
                        "http://allrecipes.com/recipe/233762/chipotle-beef-tostadas/?internalSource=previously%20viewed&referringContentType=home%20page&clickId=cardslot%2042")
        );
    }

    public int getImgID(String str){
        int ID = getResources().getIdentifier(str, "drawable", getPackageName());
        return ID;
    }

}
