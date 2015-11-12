package com.example.epub.db;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.TextView;

import com.example.EpubProject.parser.ParserAsync;
import com.nivbible.R;

/**
 * To generate .db file
 * Created by neo on 11/10/2015.
 */
public class DatabaseGenerateActivity extends Activity implements ParserAsync.IOnComplete{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(this);
        textView.setText("Generating...");
        setContentView(textView);
        new ParserAsync(this, this).execute();
    }

    @Override
    public void onComplete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Success");
        builder.setMessage("Generate Database successful!");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.create().show();
    }
}
