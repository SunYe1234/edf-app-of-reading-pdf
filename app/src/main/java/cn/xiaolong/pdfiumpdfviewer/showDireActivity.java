package cn.xiaolong.pdfiumpdfviewer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;


import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;

import cn.xiaolong.pdfiumpdfviewer.loginSignup.DashboardActivity;
import cn.xiaolong.pdfiumpdfviewer.loginSignup.RegisterActivity;
import cn.xiaolong.pdfiumpdfviewer.loginSignup.SQLiteHelper;
import cn.xiaolong.pdfiumpdfviewer.util.FileInfoUtils;


public class showDireActivity extends AppCompatActivity {
    private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
    private final int FP = ViewGroup.LayoutParams.FILL_PARENT;

    SQLiteDatabase sqLiteDatabaseObj;
    String SQLiteDataBaseQueryHolder ;
    SQLiteHelper sqLiteHelper;
    Cursor cursor;

    private Button button;
    protected TableLayout mButnsLayout;
    protected ArrayList<String> mButnsNames;
    private View.OnClickListener onClickListener;

    private long numOfFile;
    private ArrayList<String> namesOfFiles;
    private String filesPath="/storage/0403-0201/PDFs/";
    private String userName;
    //private static boolean firstTime=true;


    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.show_dire);
        init();
        getOverflowMenu();
    }

    private void getOverflowMenu() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);//得到一个已经设置好设备的显示密度的对象
            Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");//反射获取其中的方法sHasPermanentMenuKey()，他的作用是报告设备的菜单是否对用户可用，如果不可用可强制可视化。
            if (menuKeyField != null) {
                //强制设置参数,让其重绘三个点
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // 填充menu的main.xml文件; 给action bar添加条目
        getMenuInflater().inflate(R.menu.main, menu);
        menu.add(0, 0, 1, "Log Out");// 相当于在res/menu/main.xml文件中，给menu增加一个新的条目item，这个条目会显示title标签的文字（如备注1）
        menu.add(0, 1, 2, "Delete Account");//第二个参数代表唯一的item ID.
//        menu.add(0, 2, 3, "备注3");
//        menu.add(0, 3, 4, "备注4");

        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 0:
                Toast.makeText(showDireActivity.this,"User log out", Toast.LENGTH_LONG).show();

                Intent intent_0 = new Intent(this, MainActivity.class);

                // Sending Email to Dashboard Activity using intent.
//                intent_0.putExtra("userName",userName );
//                intent_0.setClass(this,DashboardActivity.class);
//                intent_0.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_0);
                break;
            case 1:
                SQLiteDataBaseBuild();
               DeleteDataInSQLiteDatabase();
               goBackToLoginPage();
                break;

            default:
                break;
        }
        return true;
    }
    private void goBackToLoginPage()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // SQLite database build method.
    public void SQLiteDataBaseBuild(){

        sqLiteDatabaseObj = openOrCreateDatabase("UserDataBase", Context.MODE_PRIVATE, null);

    }

    // delete data in the SQLite database .
    public  void DeleteDataInSQLiteDatabase(){


        // SQLite query to insert data into table.
        SQLiteDataBaseQueryHolder = "DELETE FROM "+SQLiteHelper.TABLE_NAME+" WHERE "+SQLiteHelper.Table_Column_1_Name+" = '"+userName+"';";

        // Executing query.
        sqLiteDatabaseObj.execSQL(SQLiteDataBaseQueryHolder);

        // Closing SQLite database object.
        sqLiteDatabaseObj.close();

        // Printing toast message after done inserting.
        Toast.makeText(showDireActivity.this,"User deleted Successfully", Toast.LENGTH_LONG).show();



    }

    private void init()
    {
        //if it's not the first time to start showDireActivity, than the filesPath is not the initial filesPath,
        //set it to the value received
        if (getIntent().getStringExtra("path")!=null) {
            Log.d("****received path value",filesPath);
            filesPath = getIntent().getStringExtra("path");
        }
        if (getIntent().getStringExtra("userName")!=null) {
            Log.d("****received path value",filesPath);
            userName = getIntent().getStringExtra("userName");
        }
        //create the file object using the filesPath which is the parent of all the pdfs we want to read
        File file = new File(filesPath);
        //get the number of pdfs under filesPath
        //numOfFile=FileInfoUtils.getFileSize(file);
        //get the list of pdf names
        File flist[] = file.listFiles();

        namesOfFiles=FileInfoUtils.getFilesName(file);
        numOfFile=namesOfFiles.size();

        //filtrePDF();
        setButtons();

    }


    /**
     * add buttons dynamically according to the number of files under the attribute "filesPath"
     */
    protected void setButtons()
    {

        findAllView( );
       generateBtnList( namesOfFiles );


    }

    /**
     * start ShowSelectedPdfActivity to show pdf
     */
    protected void startShowPdfActivity(String pdfName)
    {
        Intent intent=new Intent(this,ShowSelectedPdfActivity.class);
        intent.putExtra("pdf name", pdfName);
        intent.putExtra("userName", userName);

        startActivity(intent);


    }
    /**
     * start ShowSelectedFolderActivity to show folders
     */
    protected void startShowFolderActivity(String nextPath)
    {
        Intent intent=new Intent(this, showDireActivity.class);
        intent.putExtra("path", nextPath);

        startActivity(intent);


    }

    /**
     * get the LinearLayout where we put all of the buttons
     */
    protected void findAllView()
    {
        mButnsLayout=(TableLayout)findViewById(R.id.table);
        mButnsLayout.setStretchAllColumns(true);

        //mButnsLayout.setPadding(20,20,20,20);
    }

    /**
     * if folderName is a folder, return true, otherwise return false
     * @param folderName    folderName=filesPath+name of the folder
     * @return
     */
    protected boolean isFolder(String folderName)
    {
        File file=new File(folderName);
        if (file.isDirectory())
            return true;
        else
            return false;
    }
    /**
     * create a list of buttons which represent all of the pdfs under filesPath
     * @param btnContentList    the list of buttons by which we can read all of the pdfs under filesPath
     */
    protected void generateBtnList( ArrayList<String> btnContentList ){
        int indexInRow=0;
        int index = 0;
        TableRow tableRow=null;
        for( String btnContent : btnContentList )
        {
                //if it's a new row, create a TableRow
                if (indexInRow==0)  tableRow=new TableRow(this);
                //create the button corresponding with name btnContent
                // and set its text and background color which will be transparent
                Button codeBtn = new Button( this );
                codeBtn.setText(btnContent);
                codeBtn.setBackgroundColor(Color.TRANSPARENT);
                //set the animation of a button when it's clicked
                codeBtn.setOnTouchListener(new View.OnTouchListener() {
                                               @Override
                                               public boolean onTouch(View v, MotionEvent event) {
                                                   switch (event.getAction()) {
                                                       case MotionEvent.ACTION_DOWN:
                                                           Animation animation = AnimationUtils.loadAnimation(showDireActivity.this, R.anim.nomal_to_large);
                                                           v.startAnimation(animation);
                                                           break;
                                                   }
                                                   return false;
                                               }

                                           });
                 Drawable icon;
                //use different images for directory and pdf
                if (isFolder(filesPath+btnContent))
                    icon=getResources().getDrawable(R.drawable.folder);
                else
                    icon=getResources().getDrawable(R.drawable.pdf);
                //set bound of icons, otherwise it won't be displayed
                icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
                codeBtn.setCompoundDrawables(codeBtn.getCompoundDrawables()[0],icon,codeBtn.getCompoundDrawables()[2],codeBtn.getCompoundDrawables()[0]);
                codeBtn.setOnClickListener( new View.OnClickListener( ) {
                @Override
                public void onClick(View v) {
                    if (isFolder(filesPath+btnContent))
                        startShowFolderActivity(filesPath+btnContent+"/");
                    else
                        startShowPdfActivity(filesPath+btnContent);                }
            });

                index++;
                tableRow.addView(codeBtn);
                //if it's the 3rd button of one row, add this row to the table
                if (indexInRow==2)  mButnsLayout.addView(tableRow);
                indexInRow=(indexInRow+1)%3;
        }
        //add the row which has less than 3 buttons to the table
        //before adding it, remove its parent view if it already has one
        if (tableRow != null) {
            ViewGroup parentViewGroup = (ViewGroup) tableRow.getParent();
            if (parentViewGroup != null ) {
                parentViewGroup.removeView(tableRow);
            }
        }
        mButnsLayout.addView(tableRow);

    }


//    /**
//     *
//     */
//    private void filtrePDF()
//    {
//        ArrayList<String> nameOfPdfs=new ArrayList<String>();
//        for (int i=0;i<namesOfFiles.size();i++)
//        {
//            String type=namesOfFiles.get(i).split("\\.")[1];
//            if (!type.equals("pdf")) {
//                continue;
//            }
//            nameOfPdfs.add(namesOfFiles.get(i).split("\\.")[0]);
//        }
//        namesOfFiles=nameOfPdfs;
//    }





}
