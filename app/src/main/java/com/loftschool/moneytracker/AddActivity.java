package com.loftschool.moneytracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class AddActivity extends AppCompatActivity{

    public static final String EXTRA_TYPE = "type";
    public static final String RESULT_ITEM = "item";
    public static final int RC_ADD_ITEM = 99;

    private String type;

    public static void startForResult(ItemsFragment fragment, String type, int requestCode) {
        Intent intent = new Intent(fragment.getContext(), AddActivity.class);
        intent.putExtra(EXTRA_TYPE, type);
        fragment.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        final EditText name = findViewById(R.id.name);
        final EditText price = findViewById(R.id.price);
        final ImageButton add = findViewById(R.id.addBtn);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.add_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        add.setEnabled(false);

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (name.getText().toString().equals("") || price.getText().toString().equals("")) {
                    add.setEnabled(false);
                } else add.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (name.getText().toString().equals("") || price.getText().toString().equals("")) {
                    add.setEnabled(false);
                } else add.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        type = getIntent().getStringExtra(EXTRA_TYPE);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result = new Intent();
                result.putExtra(RESULT_ITEM, new Item (name.getText().toString(), Integer.parseInt(price.getText().toString()), type));
                setResult(RESULT_OK, result);
                finish();
            }
        });
    }
}
