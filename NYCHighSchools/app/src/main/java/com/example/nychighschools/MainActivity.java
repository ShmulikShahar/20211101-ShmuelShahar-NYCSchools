package com.example.nychighschools;

import android.content.DialogInterface;
import android.os.Bundle;

import com.android.volley.VolleyError;
import com.example.nychighschools.server.NYCSchoolSAT;
import com.example.nychighschools.server.NYCSchools;
import com.example.nychighschools.server.NYCSchoolsServerEvents;
import com.example.nychighschools.server.ServerCtrl;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.nychighschools.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements NYCSchoolsServerEvents {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private AlertDialog dialog;

    private void show(String title, String message)
    {
        dialog = new AlertDialog.Builder(this) // Pass a reference to your main activity here
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        dialog.cancel();
                    }
                })
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        ServerCtrl srv = new ServerCtrl();
        srv.Init(this);//Set Server Events for error handling
        srv.setNYCSchoolsServerEvents(this);//Set NYC Server Events
        srv.SetContext(getApplicationContext());
        srv.getNYCSchools();

        ListView listView = (ListView) findViewById(R.id.listSchools);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                srv.getNYCSchoolSAT(selectedItem);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void fire_GetNYCSchoolsResponse(NYCSchools NYCSchoolsObj) {
        ListView listView = (ListView) findViewById(R.id.listSchools);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,NYCSchoolsObj.Schools());
        listView.setAdapter(adapter);
    }

    @Override
    public void fire_GetNYCSchoolSATResponse(NYCSchoolSAT NYCSchoolSATObj) {
        show(""+ NYCSchoolSATObj.get_SchoolName(),
                "Takers: "+ NYCSchoolSATObj.get_Takers() +
        ", SAT Reading: "+ NYCSchoolSATObj.get_SATReading() +
                        ", SAT Math: "+ NYCSchoolSATObj.get_SATMath() +
                        ", SAT Writing: "+ NYCSchoolSATObj.get_SATWriting());
    }

    @Override
    public void fire_ExceptionResponse() {
        show("Exception!", "Error in the Application");
    }

    @Override
    public void fire_ErrorResponse(VolleyError error, int StatusCode) {
        show("Communication Exception", error.getMessage());
    }

    @Override
    public void fire_HttpErrorResponse(int StatusCode) {
        show("Error Code", String.valueOf(StatusCode));
    }
}