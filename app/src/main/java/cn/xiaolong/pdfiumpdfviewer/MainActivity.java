package cn.xiaolong.pdfiumpdfviewer;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import java.io.File;
import java.util.ArrayList;

import cn.xiaolong.pdfiumpdfviewer.util.FileInfoUtils;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

/**
 *
 */
public class MainActivity extends AppCompatActivity {
    private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
    private final int FP = ViewGroup.LayoutParams.FILL_PARENT;


    private Button button;
    private TableLayout mButnsLayout;
    private ArrayList<String> mButnsNames;
    private View.OnClickListener onClickListener;

    private long numOfFile;
    private ArrayList<String> namesOfFiles;
    private String filesPath="/storage/0403-0201/PDFs/";


    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }



    private void init()
    {
        if (getIntent().getStringExtra("path")!=null) {
            Log.d("****received path value",filesPath);
            filesPath = getIntent().getStringExtra("path");
        }
        //create the file object using the filesPath which is the parent of all the pdfs we want to read
        File file = new File(filesPath);
        //get the number of pdfs under filesPath
        //numOfFile=FileInfoUtils.getFileSize(file);
        //get the list of pdf names
        namesOfFiles=FileInfoUtils.getFilesName(file);
        numOfFile=namesOfFiles.size();

        //filtrePDF();
        setButtons();

    }


    /**
     * add buttons dynamically according to the number of files under the attribute "filesPath"
     */
    private void setButtons()
    {

        findAllView( );
       generateBtnList( namesOfFiles );


    }

    /**
     * start ShowSelectedPdfActivity to show pdf
     */
    public void startShowPdfActivity(String pdfName)
    {
        Intent intent=new Intent(this,ShowSelectedPdfActivity.class);
        intent.putExtra("pdf name", pdfName);
        startActivity(intent);


    }
    /**
     * start ShowSelectedFolderActivity to show folders
     */
    public void startShowFolderActivity(String nextPath)
    {
        Intent intent=new Intent(this,ShowFolderActivity.class);
        intent.putExtra("path", nextPath);

        startActivity(intent);


    }

    /**
     * get the LinearLayout where we put all of the buttons
     */
    private void findAllView()
    {
        mButnsLayout=(TableLayout)findViewById(R.id.table);
        mButnsLayout.setStretchAllColumns(true);

        //mButnsLayout.setPadding(20,20,20,20);
    }

    /**
     * if folderName is a folder, return true, otherwise return false
     * @param folderName
     * @return
     */
    private boolean isFolder(String folderName)
    {
        String[] type=folderName.split("\\.");
        if (type.length>1)
            return false;
        return true;
    }
    /**
     * create a list of buttons which represent all of the pdfs under filesPath
     * @param btnContentList    the list of buttons by which we can read all of the pdfs under filesPath
     */
    private void generateBtnList( ArrayList<String> btnContentList ){
        int indexInRow=0;
        int index = 0;
        TableRow tableRow=null;
        for( String btnContent : btnContentList )
        {
                //if it's a new row, create a TableRow
                if (indexInRow==0)  tableRow=new TableRow(this);
                //create the button corresponding with name btnContent
                Button codeBtn = new Button( this );
                codeBtn.setText(btnContent);
                codeBtn.setBackgroundColor(Color.TRANSPARENT);
                 Drawable icon;
                if (isFolder(filesPath+btnContent))
                    icon=getResources().getDrawable(R.drawable.ic_unfolded);
                else
                    icon=getResources().getDrawable(R.drawable.ic_pack);                icon.setBounds(0, 0, icon.getIntrinsicWidth()*10, icon.getIntrinsicHeight()*10);
                codeBtn.setCompoundDrawables(codeBtn.getCompoundDrawables()[0],icon,codeBtn.getCompoundDrawables()[2],codeBtn.getCompoundDrawables()[0]);
                codeBtn.setOnClickListener( new View.OnClickListener( ) {
                @Override
                public void onClick(View v) {
                    if (isFolder(btnContent))
                        startShowFolderActivity(filesPath+btnContent+"/");
                    else
                        startShowPdfActivity(filesPath+btnContent);                }
            });

                index++;
                tableRow.addView(codeBtn);
                //if it's the 3rd button of one row, add this row to the table
                if (indexInRow==2)  mButnsLayout.addView(tableRow);
                indexInRow=(indexInRow+1)%3;

//            }
            //新建的TableRow添加到TableLayout

        }
        if (tableRow != null) {
            ViewGroup parentViewGroup = (ViewGroup) tableRow.getParent();
            if (parentViewGroup != null ) {
                parentViewGroup.removeView(tableRow);
            }
        }
        mButnsLayout.addView(tableRow);

    }


    /**
     *
     */
    private void filtrePDF()
    {
        ArrayList<String> nameOfPdfs=new ArrayList<String>();
        for (int i=0;i<namesOfFiles.size();i++)
        {
            String type=namesOfFiles.get(i).split("\\.")[1];
            if (!type.equals("pdf")) {
                continue;
            }
            nameOfPdfs.add(namesOfFiles.get(i).split("\\.")[0]);
        }
        namesOfFiles=nameOfPdfs;
    }





}
