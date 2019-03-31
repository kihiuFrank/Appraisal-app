package com.example.lecevaluation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class ListViewActivity extends AppCompatActivity {

    ListView queryList;
    String[] questions;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        queryList = findViewById(R.id.simpleListView);
        submit = findViewById(R.id.submit);

        //get string array from string.xml file
        questions = getResources().getStringArray(R.array.questions);

        //setting adapter to fill data in list view
         CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), questions);
         queryList.setAdapter(customAdapter);

         submit.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 String message = "";

                 for (int i = 0; i< CustomAdapter.selectedAnswers.size(); i++){
                     message = message + "\n" + (i+1) + "" + CustomAdapter.selectedAnswers.get(i);
                 }
                 Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
             }
         });
    }
    /**
     * @Override
     *     public boolean onCreateOptionsMenu(Menu menu) {
     *         //adds items to action bar
     *         getMenuInflater().inflate(R.menu.menu_main, menu);
     *         return true;
     *    }
     *  @Override
     * public boolean onOptionsItemSelected(MenuItem item) {
     * // Handle action bar item clicks here. The action bar will
     * // automatically handle clicks on the Home/Up button, so long
     * // as you specify a parent activity in AndroidManifest.xml.
     * int id = item.getItemId();
     *
     * //noinspection SimplifiableIfStatement
     * if (id == R.id.action_settings) {
     * return true;
     * }
     *
     * return super.onOptionsItemSelected(item);
     * }
     * */

}
