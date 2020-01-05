package whut.com.myapp.UI;


import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.utils.Constant;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import me.rosuh.filepicker.config.FilePickerManager;
import whut.com.myapp.R;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoreFragment extends Fragment implements View.OnClickListener {
    MoreFragment mMoreFragment;


    // 扫描二维码
    private EditText et_origin_text;
    private MaterialButton btn_genQR_ok;
    private RelativeLayout rlt_showImg;
    private ImageView iv_showImg;

    // 背景图片更换
    CardView cv_toScanner_QRcode;
    LinearLayout ll_morefragment_bkg;

    // 汇率转换
    private MaterialButton btn_money1;
    private MaterialButton btn_money2;
    private EditText et_money_text;
    private ImageView iv_swap_money;
    private TextView tv_money_result;
    private MaterialButton btn_money_ok;
    private int moneyId1 = 0;
    private int moneyId2 = 1;
    private List<String> moneyName = Arrays.asList("人民币", "美元", "日元", "欧元", "英镑");
    private List<Double> moneyRate = new ArrayList<>();

    // 相机
    private ZXingScannerView scanner_view;
    private ZXingScannerView.ResultHandler mResultHandler = null;
    private RelativeLayout rlt_camera;
    private MaterialButton btn_camera_continue;
    private MaterialButton btn_camera_cancel;
    private MaterialButton btn_camera_file;
    private int REQUESTCODE_FROM_ACTIVITY = 1000;


    public MoreFragment() {
        // Required empty public constructor
        mMoreFragment = this;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        initView(view);

        return view;
    }

    public void initView(View view) {
        et_origin_text = (EditText) view.findViewById(R.id.et_origin_text);
        btn_genQR_ok = (MaterialButton) view.findViewById(R.id.btn_genQR_ok);
        btn_genQR_ok.setOnClickListener(this);
        iv_showImg = (ImageView) view.findViewById(R.id.iv_showImg);
        rlt_showImg = (RelativeLayout) view.findViewById(R.id.rlt_showImg);
        rlt_showImg.setOnClickListener(this);

        cv_toScanner_QRcode = (CardView) view.findViewById(R.id.cv_toScanner_QRcode);
        cv_toScanner_QRcode.setOnClickListener(this);
        ll_morefragment_bkg = (LinearLayout) view.findViewById(R.id.ll_morefragment_bkg);

        btn_money1 = (MaterialButton) view.findViewById(R.id.btn_money1);
        btn_money2 = (MaterialButton) view.findViewById(R.id.btn_money2);
        et_money_text = (EditText) view.findViewById(R.id.et_money_text);
        iv_swap_money = (ImageView) view.findViewById(R.id.iv_swap_money);
        tv_money_result = (TextView) view.findViewById(R.id.tv_money_result);
        btn_money_ok = (MaterialButton) view.findViewById(R.id.btn_money_ok);
        btn_money_ok.setOnClickListener(this);
        btn_money1.setOnClickListener(this);
        btn_money2.setOnClickListener(this);
        iv_swap_money.setOnClickListener(this);
        moneyRate.add(1.0);
        moneyRate.add(6.9649);
        moneyRate.add(0.06444);
        moneyRate.add(7.7714);
        moneyRate.add(9.1143);
        btn_money1.setText(moneyName.get(moneyId1));
        btn_money2.setText(moneyName.get(moneyId2));

//        scanner_view = new ZXingScannerView(getContext());
        scanner_view = (ZXingScannerView) view.findViewById(R.id.scanner_view);
        rlt_camera = (RelativeLayout) view.findViewById(R.id.rlt_camera);
        btn_camera_continue = (MaterialButton) view.findViewById(R.id.btn_camera_continue);
        btn_camera_cancel = (MaterialButton) view.findViewById(R.id.btn_camera_cancel);
        btn_camera_file = (MaterialButton) view.findViewById(R.id.btn_camera_file);
        btn_camera_cancel.setOnClickListener(this);
        btn_camera_continue.setOnClickListener(this);
        btn_camera_file.setOnClickListener(this);
        btn_camera_continue.setClickable(false);

        mResultHandler = new ZXingScannerView.ResultHandler() {
            @Override
            public void handleResult(Result result) {
//                scanner_view.resumeCameraPreview(mResultHandler); //重新进入扫描二维码
                btn_camera_continue.setClickable(true);
//                Toast.makeText(getContext(), "内容=" + result.getText() + ",格式=" + result.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), "已经复制到粘贴板中", Toast.LENGTH_SHORT).show();

                // 复制到系统粘贴板中
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(getActivity().CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("QRcode", result.getText().toString());
                clipboard.setPrimaryClip(clip);
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_genQR_ok:
                String text = et_origin_text.getText().toString();
                if (text == null || text.length() == 0) {
                    Toast.makeText(getContext(), "内容为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                QRCodeWriter qrCodeWriter = new QRCodeWriter();
                try {
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    text = new String(text.getBytes("UTF-8"), "iso-8859-1");
                    Bitmap bitmap = barcodeEncoder.encodeBitmap(text, BarcodeFormat.QR_CODE, 400, 400);
                    String path = Environment.getExternalStorageDirectory().toString();
                    ;
                    File file = new File(path, "MyQrImage.png");
                    FileOutputStream out = new FileOutputStream(file);

                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] bytes = baos.toByteArray();
                    Glide.with(getContext()).load(bytes).into(iv_showImg);
                    rlt_showImg.setVisibility(View.VISIBLE);
                    // 缩起软键盘
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);


                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.rlt_showImg:
                rlt_showImg.setVisibility(View.GONE);
                break;

            case R.id.cv_toScanner_QRcode:

                scannerQRcode();
                break;
            case R.id.btn_money1:
                ArrayAdapter adapter = new ArrayAdapter(getContext(),
                        R.layout.dialog_simple_itme1, moneyName);
                DialogPlus dialog = DialogPlus.newDialog(getActivity())
                        .setAdapter(adapter)
                        .setContentBackgroundResource(R.color.white)
                        .setGravity(Gravity.BOTTOM)
                        .setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                                moneyId1 = position;
                                btn_money1.setText(moneyName.get(position));
                                dialog.dismiss();
                            }
                        })
                        .setExpanded(true)  // This will enable the expand feature, (similar to android L share dialog)
                        .create();
                dialog.show();
                break;

            case R.id.btn_money2:
                ArrayAdapter adapter2 = new ArrayAdapter(getContext(),
                        R.layout.dialog_simple_itme1, moneyName);
                DialogPlus dialog2 = DialogPlus.newDialog(getActivity())
                        .setAdapter(adapter2)
                        .setContentBackgroundResource(R.color.white)
                        .setGravity(Gravity.BOTTOM)
                        .setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                                moneyId2 = position;
                                btn_money2.setText(moneyName.get(position));
                                dialog.dismiss();
                            }
                        })
                        .setExpanded(true)  // This will enable the expand feature, (similar to android L share dialog)
                        .create();
                dialog2.show();
                break;

            case R.id.iv_swap_money:
                int t = moneyId1;
                moneyId1 = moneyId2;
                moneyId2 = t;
                btn_money1.setText(moneyName.get(moneyId1));
                btn_money2.setText(moneyName.get(moneyId2));
                break;

            case R.id.btn_money_ok:
                String text2 = et_money_text.getText().toString();
                if (text2 == null || text2.length() == 0) {
                    Toast.makeText(getContext(), "不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                Double moneyNum = Double.parseDouble(text2);
                moneyNum = moneyNum * moneyRate.get(moneyId1) / moneyRate.get(moneyId2);
                DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.CHINA));
                df.setMaximumFractionDigits(340); // 340 = DecimalFormat.DOUBLE_FRACTION_DIGITS
                tv_money_result.setText(df.format(moneyNum));

                // 收起软键盘
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);

                break;
            case R.id.btn_camera_continue:
                btn_camera_continue.setClickable(false);
                scanner_view.resumeCameraPreview(mResultHandler);
                break;
            case R.id.btn_camera_cancel:
                scanner_view.stopCamera();
                rlt_camera.setVisibility(View.GONE);
                break;
            case R.id.btn_camera_file:

//                new LFilePicker()
//                        .withActivity(getActivity())
//                        .withSupportFragment(mMoreFragment)
//                        .withRequestCode(REQUESTCODE_FROM_ACTIVITY)
//                        .withStartPath("/storage/emulated/0/")
//                        .withIsGreater(false)
//                        .withFileSize(500 * 1024)
//                        .start();
                FilePickerManager.INSTANCE
                        .from(this)
                        .forResult(FilePickerManager.REQUEST_CODE);
                scanner_view.stopCamera();
                rlt_camera.setVisibility(View.GONE);

                break;
        }

    }

    public void scannerQRcode() {
        rlt_camera.setVisibility(View.VISIBLE);
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                        scanner_view.setResultHandler(mResultHandler);
                        scanner_view.startCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                })
                .check();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        scanner_view.stopCamera();
        rlt_camera.setVisibility(View.GONE);
    }

    @Override
    public void onStop() {
        super.onStop();
        scanner_view.stopCamera();
        rlt_camera.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();
        scanner_view.stopCamera();
        rlt_camera.setVisibility(View.GONE);
    }

    // 这个是文件选择的方法
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FilePickerManager.INSTANCE.REQUEST_CODE) {
            List<String> list = FilePickerManager.INSTANCE.obtainData();
            Toast.makeText(getActivity(), "选中了" + list.size() + "个文件", Toast.LENGTH_SHORT).show();
            if(list.size() == 0) {
                return;
            }

            FilePickerManager.INSTANCE.saveData(new ArrayList<String>());

            FileInputStream fis = null;
            com.google.zxing.Result result = null;
            Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
            hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
            hints.put(DecodeHintType.POSSIBLE_FORMATS, BarcodeFormat.QR_CODE);
            hints.put(DecodeHintType.CHARACTER_SET, "utf-8");

            try {
                fis = new FileInputStream(list.get(0));
                Bitmap bitmap  = BitmapFactory.decodeStream(fis);
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                int []pixels = new int[width*height];
                bitmap.getPixels(pixels,0,width,0,0,width,height);
                // 新建一个RGBLuminanceSource对象
                RGBLuminanceSource source = new RGBLuminanceSource(width,height,pixels);
                // 将图片转换成二进制图片
                BinaryBitmap binaryBitmap = new BinaryBitmap(new GlobalHistogramBinarizer(source));
                QRCodeReader reader = new QRCodeReader();
                result = reader.decode(binaryBitmap, hints);// 开始解析
//                Toast.makeText(getActivity(),result.getText(),Toast.LENGTH_SHORT).show();

                // 复制到系统粘贴板中
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(getActivity().CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("QRcode", result.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getActivity(),"已经复制到粘贴板",Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(),"解析失败",Toast.LENGTH_SHORT).show();
            }

        }
    }



}
