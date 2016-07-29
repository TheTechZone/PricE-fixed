package com.pentalog.backendboyz.price;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddItemForm extends AppCompatActivity {

    private Context mContext;
    private EditText mNameField,mStoreField,mDescriptionField,mPriceField;
    private Button mInsertButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_form);

        mNameField = (EditText) findViewById(R.id.nameField);
        mStoreField = (EditText) findViewById(R.id.storeField);
        mDescriptionField = (EditText) findViewById(R.id.descriptionField);
        mPriceField = (EditText) findViewById(R.id.priceField);

        mInsertButton = (Button) findViewById(R.id.insertItem);

        mInsertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = "add";
                String name = mNameField.getText().toString();
                String store = mStoreField.getText().toString();
                String price = mPriceField.getText().toString();
                String description = mDescriptionField.getText().toString();

                new AddWorker(mContext).execute(type,name,description,store,price);
            }
        });
    }
}
