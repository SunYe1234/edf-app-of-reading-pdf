package cn.xiaolong.pdfiumpdfviewer;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
    private String filesPath="/storage/0403-0201/PDFs";


    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();


//        button=(Button)findViewById(R.id.button);
//        button.setOnClickListener(new Button.OnClickListener()
//        {
//
//            @Override
//            public void onClick(View arg0) {
//                // TODO Auto-generated method stub
//                button.setText("after click");
//            }
//
//        });
    }



    private void init()
    {
        //create the file object using the filesPath which is the parent of all the pdfs we want to read
        File file = new File(filesPath);
        //get the number of pdfs under filesPath
        numOfFile=FileInfoUtils.getFileSize(file);
        //get the list of pdf names
        namesOfFiles=FileInfoUtils.getFilesName(file);
        filtrePDF();
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
     * get the LinearLayout where we put all of the buttons
     */
    private void findAllView()
    {
        mButnsLayout=(TableLayout)findViewById(R.id.table);
        mButnsLayout.setStretchAllColumns(true);

        //mButnsLayout.setPadding(20,20,20,20);
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
                Drawable icon=getResources().getDrawable(R.drawable.ic_unfolded);
                icon.setBounds(0, 0, icon.getIntrinsicWidth()*10, icon.getIntrinsicHeight()*10);
                codeBtn.setCompoundDrawables(codeBtn.getCompoundDrawables()[0],icon,codeBtn.getCompoundDrawables()[2],codeBtn.getCompoundDrawables()[0]);
                codeBtn.setOnClickListener( new View.OnClickListener( ) {
                @Override
                public void onClick(View v) {
                    startShowPdfActivity(btnContent);
                }
            });

                index++;
                tableRow.addView(codeBtn);
                //if it's the 3rd button of one row, add this row to the table
                if (indexInRow==2)  mButnsLayout.addView(tableRow);
                indexInRow=(indexInRow+1)%3;

//            }
            //新建的TableRow添加到TableLayout

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
                numOfFile--;
                continue;
            }
            nameOfPdfs.add(namesOfFiles.get(i).split("\\.")[0]);
        }
        namesOfFiles=nameOfPdfs;
    }


    /**
     * set attributes, styles of image buttons
     * @param codeBtn   the Button we need to modify the style
     * @param btnContent    the content of the codeBtn
     * @param id    the id of the codeBtn
     * @param backGroundColor   the backGroundColor of the codeBtn
     * @param textColor     the text color of the codeBtn
     * @param textSize      the size of the text
     */
//    private void setBtnAttribute( Button codeBtn, String btnContent, int id, int backGroundColor, int textColor, int textSize ){
//        if( null == codeBtn ){
//            return;
//        }
//
//
//        //use image ic_unfolded in the res/drawable as the picture of buttons
//        Drawable icon=getResources().getDrawable(R.drawable.ic_unfolded);
//        icon.setBounds(0, 0, icon.getIntrinsicWidth()*10, icon.getIntrinsicHeight()*10);
//        codeBtn.setCompoundDrawables(codeBtn.getCompoundDrawables()[0],icon,codeBtn.getCompoundDrawables()[2],codeBtn.getCompoundDrawables()[0]);
//        codeBtn.setBackgroundColor( ( backGroundColor >= 0 )?backGroundColor:Color.BLUE );
//        codeBtn.setTextColor( ( textColor >= 0 )?textColor: Color.BLACK );
//        codeBtn.setTextSize( ( textSize > 16 )?textSize:24 );
//        codeBtn.setId( id );
//        codeBtn.setText( btnContent );
//        //set the align of the button as LEFT align
//        codeBtn.setGravity( Gravity.LEFT );
//        codeBtn.setOnClickListener( new View.OnClickListener( ) {
//            @Override
//            public void onClick(View v) {
//                // btn click process
//                startShowPdfActivity();
//            }
//        });
//
//        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams( ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT );
//        rlp.addRule( RelativeLayout.ALIGN_PARENT_LEFT );
//
//        codeBtn.setLayoutParams( rlp );
//    }





//    private static final String TAG="MainActivity";
//    private CircleProgressBar cpbLoad;
//    private PDFManager mPDFManager;
//    private ViewPager mViewpager;
//    private ListView mListView;
//    private View vGuide;
//    private File downLoadPdfFile;
//    private List<File> allPdfFile;
//
//
//    private List<Button> buttons;
//    private Drawable drawable;
//
//    private List<String> allPdfName;
//    private String intParentDoc="/storage/emulated/0/Download/";
//    private String extParentDoc;
//
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//       // setButtons();
//        init();
//        setListener();
//    }
//
//    protected void init() {
//        cpbLoad = (CircleProgressBar) findViewById(R.id.cpbLoad);
//        vGuide = findViewById(R.id.vGuide);
//        vGuide.setVisibility(View.GONE);
//        extParentDoc=getExternalStoragePath()+"/PDFs/";
//        allPdfName=getFilesAllName(extParentDoc);
//        allPdfFile=new ArrayList<File>();
//        initcpbConfig();
//
//        //downLoadPdfFile = new File(this.getCacheDir(), "test" + ".pdf");
//        //downLoadPdfFile = new File("/storage/emulated/0/Download/china.pdf");
//        downLoadPdfFile = new File(extParentDoc+"china.pdf");
//
//        for (int i=0;i<allPdfName.size();i++)
//        {
//            allPdfFile.add(new File(allPdfName.get(i)));
//        }
//        if (downLoadPdfFile.exists() )
//        {if( FileUtils.getFileSize(downLoadPdfFile) > 0)
//            loadFinish();
//        }
//        else {
//            DownLoadManager.getDownLoadManager().startDown(getDownLoadInfo());
//        }
//    }
//
//    public String getExternalStoragePath() {
//
//        String internalPath = Environment.getExternalStorageDirectory().getAbsolutePath();
//        String[] paths = internalPath.split("/");
//        String parentPath = "/";
//        for (String s : paths) {
//            if (s.trim().length() > 0) {
//                parentPath = parentPath.concat(s);
//                break;
//            }
//        }
//        File parent = new File(parentPath);
//        if (parent.exists()) {
//            File[] files = parent.listFiles();
//            for (File file : files) {
//                String filePath = file.getAbsolutePath();
//                Log.d(TAG, filePath);
//                if (filePath.equals(internalPath)) {
//                    continue;
//                } else if (filePath.toLowerCase().contains("sdcard")) {
//                    return filePath;
//                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    try {
//                        if (Environment.isExternalStorageRemovable(file)) {
//                            return filePath;
//                        }
//                    } catch (RuntimeException e) {
//                        Log.e(TAG, "RuntimeException: " + e);
//                    }
//                }
//            }
//
//        }
//        return null;
//    }

//    private void setButtons()
//    {
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int width = dm.widthPixels;
//        int height = dm.heightPixels;
//
//        buttons=new ArrayList<Button>();
//        Button button1=(Button) findViewById(R.id.photoGraphBtn1);
//        buttons.add(button1);
//        Button button2=(Button) findViewById(R.id.photoGraphBtn1);
//        buttons.add(button2);
//
//        RelativeLayout layout = new RelativeLayout(this);
//
//        //这里创建16个按钮，每行放置4个按钮
//
//        Button Btn[] = new Button[16];
//        int j = -1;
//        for  (int i=0; i<=15; i++) {
//            Btn[i]=new Button(this);
//            Btn[i].setId(2000+i);
//            Btn[i].setText("pdf"+i);
//            RelativeLayout.LayoutParams btParams = new RelativeLayout.LayoutParams ((width-50)/4,40);  //设置按钮的宽度和高度
//            if (i%4 == 0) {
//                j++;
//            }
//            btParams.leftMargin = 10+ ((width-50)/4+10)*(i%4);   //横坐标定位
//            btParams.topMargin = 20 + 55*j;   //纵坐标定位
//            layout.addView(Btn[i],btParams);   //将按钮放入layout组件
//        }
//
//
//        drawable=getResources().getDrawable(R.mipmap.ic_launcher);
//        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
//    }

//    private void initcpbConfig() {
//        cpbLoad.setValue(0);
//        cpbLoad.setTextColor(getResources().getColor(R.color.main_normal_black_color));
//        cpbLoad.setProdressWidth(cpbLoad.dp2px(8));
//        cpbLoad.setProgress(getResources().getColor(R.color.main_blue_color));
//        cpbLoad.setCircleBackgroud(Color.WHITE);
//        cpbLoad.setPreProgress(Color.WHITE);
//    }
//
//    private void loadFinish() {
//        mPDFManager = new PDFManager.Builder(this)
//                .pdfFromFile(downLoadPdfFile)
//                .setOnOpenErrorListener(t -> {
//                    cpbLoad.setValue(0);
//                    DownLoadManager.getDownLoadManager().startDown(getDownLoadInfo());
//                })
//                .setOnOpenSuccessListener(() -> {
//                    cpbLoad.setVisibility(View.GONE);
//                    vGuide.setVisibility(View.VISIBLE);
//                })
//                .build();
//        vGuide.setVisibility(View.VISIBLE);
//        initListView();
//        initViewPager();
//    }
//
//    private void initViewPager() {
//        mViewpager = (ViewPager) findViewById(R.id.viewpager);
//        mViewpager.setAdapter(new PdfImageAdapter(this, mPDFManager));
//
//        final TextView textView = (TextView) findViewById(R.id.tvPosition);
//        textView.setVisibility(View.VISIBLE);
//        textView.setText(1 + "/" + mPDFManager.pageCount());
//        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                textView.setText(position + 1 + "/" + mPDFManager.pageCount());
//                ((PdfGuideAdapter) mListView.getAdapter()).setStatePosition(position);
//                mListView.smoothScrollToPosition(position);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//    }
//
//    private void initListView() {
//        mListView = (ListView) findViewById(R.id.lvGuide);
//        mListView.setVisibility(View.GONE);
//        PdfGuideAdapter pdfGuideAdapter = new PdfGuideAdapter(this, mPDFManager);
//        pdfGuideAdapter.setOnItemClickListener(v -> mViewpager.setCurrentItem((int) v.getTag()));
//        mListView.setAdapter(pdfGuideAdapter);
//    }
//
//    private DownLoadInfo getDownLoadInfo() {
//        DownLoadInfo mDownLoadInfo = new DownLoadInfo();
//          /*下载回调*/
//        mDownLoadInfo.listener = this;
//        mDownLoadInfo.savePath = downLoadPdfFile.getAbsolutePath();
//        mDownLoadInfo.url = "https://d11.baidupcs.com/file/d10b1184525be1fa2185cbd1b17f244e?bkt=p3-1400d10b1184525be1fa2185cbd1b17f244e538a176a0000001d530b&xcode=d99bdf2146c60fedd1163b4354f9afd7c85d2bd05878864c0b2977702d3e6764&fid=1695803216-250528-664972808641582&time=1502263894&sign=FDTAXGERLBHS-DCb740ccc5511e5e8fedcff06b081203-8jadaXCWaHHtT4Uj%2F1bjxk9al74%3D&to=d11&size=1921803&sta_dx=1921803&sta_cs=283546&sta_ft=pdf&sta_ct=7&sta_mt=7&fm2=MH,Yangquan,Netizen-anywhere,,fujian,ct&newver=1&newfm=1&secfm=1&flow_ver=3&pkey=1400d10b1184525be1fa2185cbd1b17f244e538a176a0000001d530b&sl=76480590&expires=8h&rt=sh&r=723264417&mlogid=5117425091165905134&vuk=1695803216&vbdid=1114076982&fin=%E8%BD%AF%E4%BB%B6%E5%BC%80%E5%8F%91%E5%B8%B8%E7%94%A8%E8%AF%8D%E6%B1%87%28%E5%8C%97%E4%BA%AC%E5%B0%9A%E5%AD%A6%E5%A0%82%E5%8F%91%E5%B8%83%29.pdf&fn=%E8%BD%AF%E4%BB%B6%E5%BC%80%E5%8F%91%E5%B8%B8%E7%94%A8%E8%AF%8D%E6%B1%87%28%E5%8C%97%E4%BA%AC%E5%B0%9A%E5%AD%A6%E5%A0%82%E5%8F%91%E5%B8%83%29.pdf&rtype=1&iv=0&dp-logid=5117425091165905134&dp-callid=0.1.1&hps=1&csl=80&csign=WryUYKeHbb5ItV4jNvcrPWowU%2Bo%3D&so=0&ut=6&uter=4&serv=0&by=themis";
//        return mDownLoadInfo;
//    }
//
//    protected void setListener() {
//        vGuide.setOnClickListener(v -> {
//            if (!vGuide.isSelected()) {
//                mListView.setVisibility(View.VISIBLE);
//                vGuide.setSelected(true);
//            } else {
//                mListView.setVisibility(View.GONE);
//                vGuide.setSelected(false);
//            }
//        });
//    }
//
//    public static List<String> getFilesAllName(String path) {
//        File file=new File(path);
//        File[] files=file.listFiles();
//        if (files == null){
//            Log.e("error","空目录");return null;}
//        List<String> s = new ArrayList<>();
//        for(int i =0;i<files.length;i++){
//            String[] type=files[i].getName().split("\\.");
//
//            if (type[1].equals("pdf"))
//            s.add(files[i].getAbsolutePath());
//        }
//        return s;
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        DownLoadManager.getDownLoadManager().stopAllDown();
//        if (mPDFManager != null) {
//            mPDFManager.recycle();
//        }
//    }
//
//
//    @Override
//    public void onNext(DownLoadInfo downLoadInfo) {
//        Toast.makeText(this, downLoadInfo.savePath, Toast.LENGTH_LONG).show();
//        loadFinish();
//    }
//
//    @Override
//    public void onLoadStart() {
//        cpbLoad.setValue(0);
//        cpbLoad.setVisibility(View.VISIBLE);
//    }
//
//    @Override
//    public void onLoadComplete() {
//        cpbLoad.setVisibility(View.GONE);
//    }
//
//    @Override
//    public void updateProgress(long readLength, long countLength) {
//        cpbLoad.setValue((int) (readLength * 100 / countLength));
//    }
//
//    @Override
//    public void onLoadError(Throwable e) {
//        e.printStackTrace();
//        Toast.makeText(this, "下载发生异常错误。", Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    public void onLoadPause() {
//        Toast.makeText(this, "暂停下载", Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    public void onLoadStop() {
//        Toast.makeText(this, "停止下载", Toast.LENGTH_LONG).show();
//    }

}
