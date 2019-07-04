package cn.xiaolong.pdfiumpdfviewer;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.xiaolong.pdfiumpdfviewer.download.DownLoadInfo;
import cn.xiaolong.pdfiumpdfviewer.download.DownLoadManager;
import cn.xiaolong.pdfiumpdfviewer.download.HttpProgressOnNextListener;
import cn.xiaolong.pdfiumpdfviewer.pdf.CircleProgressBar;
import cn.xiaolong.pdfiumpdfviewer.pdf.PDFManager;
import cn.xiaolong.pdfiumpdfviewer.pdf.adapter.PdfGuideAdapter;
import cn.xiaolong.pdfiumpdfviewer.pdf.adapter.PdfImageAdapter;
import cn.xiaolong.pdfiumpdfviewer.pdf.utils.FileUtils;

/**
 * Created by yezi on 2019/7/3.
 */

public class ShowSelectedPdfActivity extends AppCompatActivity implements HttpProgressOnNextListener<DownLoadInfo> {

    private static final String TAG="MainActivity";
    private CircleProgressBar cpbLoad;
    private PDFManager mPDFManager;
    private ViewPager mViewpager;
    private ListView mListView;
    private View vGuide;
    private File downLoadPdfFile;
    private List<File> allPdfFile;


    private List<Button> buttons;
    private Drawable drawable;

    private List<String> allPdfName;
    private String intParentDoc="/storage/emulated/0/Download/";
    private String extParentDoc;    //the common SD card absolute path for SAMSUNG is /storage/0403-0201/
    private String selectedPdf;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_pdf);
        // setButtons();
        init();
        setListener();
    }

    protected void init() {
        cpbLoad = (CircleProgressBar) findViewById(R.id.cpbLoad);
        vGuide = findViewById(R.id.vGuide);
        vGuide.setVisibility(View.GONE);
        extParentDoc=getExternalStoragePath()+"/PDFs/";
        selectedPdf=getIntent().getStringExtra("pdf name");
        //allPdfName=getFilesAllName(extParentDoc);
        //allPdfFile=new ArrayList<File>();
        initcpbConfig();

        //downLoadPdfFile = new File(this.getCacheDir(), "test" + ".pdf");
        //downLoadPdfFile = new File("/storage/emulated/0/Download/china.pdf");
        //downLoadPdfFile = new File(extParentDoc+selectedPdf);
        downLoadPdfFile=new File(getIntent().getStringExtra("pdf name"));

//        for (int i=0;i<allPdfName.size();i++)
//        {
//            allPdfFile.add(new File(allPdfName.get(i)));
//        }
        if (downLoadPdfFile.exists() )
        {if( FileUtils.getFileSize(downLoadPdfFile) > 0)
            loadFinish();
        }
        else {
            DownLoadManager.getDownLoadManager().startDown(getDownLoadInfo());
        }
    }

    public String getExternalStoragePath() {

        String internalPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String[] paths = internalPath.split("/");
        String parentPath = "/";
        for (String s : paths) {
            if (s.trim().length() > 0) {
                parentPath = parentPath.concat(s);
                break;
            }
        }
        File parent = new File(parentPath);
        if (parent.exists()) {
            File[] files = parent.listFiles();
            for (File file : files) {
                String filePath = file.getAbsolutePath();
                Log.d(TAG, filePath);
                if (filePath.equals(internalPath)) {
                    continue;
                } else if (filePath.toLowerCase().contains("sdcard")) {
                    return filePath;
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    try {
                        if (Environment.isExternalStorageRemovable(file)) {
                            return filePath;
                        }
                    } catch (RuntimeException e) {
                        Log.e(TAG, "RuntimeException: " + e);
                    }
                }
            }

        }
        return null;
    }

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

    private void initcpbConfig() {
        cpbLoad.setValue(0);
        cpbLoad.setTextColor(getResources().getColor(R.color.main_normal_black_color));
        cpbLoad.setProdressWidth(cpbLoad.dp2px(8));
        cpbLoad.setProgress(getResources().getColor(R.color.main_blue_color));
        cpbLoad.setCircleBackgroud(Color.WHITE);
        cpbLoad.setPreProgress(Color.WHITE);
    }

    private void loadFinish() {
        mPDFManager = new PDFManager.Builder(this)
                .pdfFromFile(downLoadPdfFile)
                .setOnOpenErrorListener(t -> {
                    cpbLoad.setValue(0);
                    DownLoadManager.getDownLoadManager().startDown(getDownLoadInfo());
                })
                .setOnOpenSuccessListener(() -> {
                    cpbLoad.setVisibility(View.GONE);
                    vGuide.setVisibility(View.VISIBLE);
                })
                .build();
        vGuide.setVisibility(View.VISIBLE);
        initListView();
        initViewPager();
    }

    private void initViewPager() {
        mViewpager = (ViewPager) findViewById(R.id.viewpager);
        mViewpager.setAdapter(new PdfImageAdapter(this, mPDFManager));

        final TextView textView = (TextView) findViewById(R.id.tvPosition);
        textView.setVisibility(View.VISIBLE);
        textView.setText(1 + "/" + mPDFManager.pageCount());
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                textView.setText(position + 1 + "/" + mPDFManager.pageCount());
                ((PdfGuideAdapter) mListView.getAdapter()).setStatePosition(position);
                mListView.smoothScrollToPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initListView() {
        mListView = (ListView) findViewById(R.id.lvGuide);
        mListView.setVisibility(View.GONE);
        PdfGuideAdapter pdfGuideAdapter = new PdfGuideAdapter(this, mPDFManager);
        pdfGuideAdapter.setOnItemClickListener(v -> mViewpager.setCurrentItem((int) v.getTag()));
        mListView.setAdapter(pdfGuideAdapter);
    }

    private DownLoadInfo getDownLoadInfo() {
        DownLoadInfo mDownLoadInfo = new DownLoadInfo();
          /*下载回调*/
        mDownLoadInfo.listener = this;
        mDownLoadInfo.savePath = downLoadPdfFile.getAbsolutePath();
        mDownLoadInfo.url = "https://d11.baidupcs.com/file/d10b1184525be1fa2185cbd1b17f244e?bkt=p3-1400d10b1184525be1fa2185cbd1b17f244e538a176a0000001d530b&xcode=d99bdf2146c60fedd1163b4354f9afd7c85d2bd05878864c0b2977702d3e6764&fid=1695803216-250528-664972808641582&time=1502263894&sign=FDTAXGERLBHS-DCb740ccc5511e5e8fedcff06b081203-8jadaXCWaHHtT4Uj%2F1bjxk9al74%3D&to=d11&size=1921803&sta_dx=1921803&sta_cs=283546&sta_ft=pdf&sta_ct=7&sta_mt=7&fm2=MH,Yangquan,Netizen-anywhere,,fujian,ct&newver=1&newfm=1&secfm=1&flow_ver=3&pkey=1400d10b1184525be1fa2185cbd1b17f244e538a176a0000001d530b&sl=76480590&expires=8h&rt=sh&r=723264417&mlogid=5117425091165905134&vuk=1695803216&vbdid=1114076982&fin=%E8%BD%AF%E4%BB%B6%E5%BC%80%E5%8F%91%E5%B8%B8%E7%94%A8%E8%AF%8D%E6%B1%87%28%E5%8C%97%E4%BA%AC%E5%B0%9A%E5%AD%A6%E5%A0%82%E5%8F%91%E5%B8%83%29.pdf&fn=%E8%BD%AF%E4%BB%B6%E5%BC%80%E5%8F%91%E5%B8%B8%E7%94%A8%E8%AF%8D%E6%B1%87%28%E5%8C%97%E4%BA%AC%E5%B0%9A%E5%AD%A6%E5%A0%82%E5%8F%91%E5%B8%83%29.pdf&rtype=1&iv=0&dp-logid=5117425091165905134&dp-callid=0.1.1&hps=1&csl=80&csign=WryUYKeHbb5ItV4jNvcrPWowU%2Bo%3D&so=0&ut=6&uter=4&serv=0&by=themis";
        return mDownLoadInfo;
    }

    protected void setListener() {
        vGuide.setOnClickListener(v -> {
            if (!vGuide.isSelected()) {
                mListView.setVisibility(View.VISIBLE);
                vGuide.setSelected(true);
            } else {
                mListView.setVisibility(View.GONE);
                vGuide.setSelected(false);
            }
        });
    }

    public static List<String> getFilesAllName(String path) {
        File file=new File(path);
        File[] files=file.listFiles();
        if (files == null){
            Log.e("error","空目录");return null;}
        List<String> s = new ArrayList<>();
        for(int i =0;i<files.length;i++){
            String[] type=files[i].getName().split("\\.");

            if (type[1].equals("pdf"))
                s.add(files[i].getAbsolutePath());
        }
        return s;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        DownLoadManager.getDownLoadManager().stopAllDown();
        if (mPDFManager != null) {
            mPDFManager.recycle();
        }
    }


    @Override
    public void onNext(DownLoadInfo downLoadInfo) {
        Toast.makeText(this, downLoadInfo.savePath, Toast.LENGTH_LONG).show();
        loadFinish();
    }

    @Override
    public void onLoadStart() {
        cpbLoad.setValue(0);
        cpbLoad.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadComplete() {
        cpbLoad.setVisibility(View.GONE);
    }

    @Override
    public void updateProgress(long readLength, long countLength) {
        cpbLoad.setValue((int) (readLength * 100 / countLength));
    }

    @Override
    public void onLoadError(Throwable e) {
        e.printStackTrace();
        Toast.makeText(this, "下载发生异常错误。", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLoadPause() {
        Toast.makeText(this, "暂停下载", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLoadStop() {
        Toast.makeText(this, "停止下载", Toast.LENGTH_LONG).show();
    }

}


