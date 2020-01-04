package whut.com.myapp.UI;


import android.app.Dialog;
import android.graphics.Bitmap;
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

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.ViewTarget;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnItemClickListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Array;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import whut.com.myapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoreFragment extends Fragment implements View.OnClickListener {
    // 扫描二维码
    private EditText et_origin_text;
    private MaterialButton btn_genQR_ok;
    private RelativeLayout  rlt_showImg;
    private ImageView iv_showImg;

    // 背景图片更换
    CardView cv_change_background;
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
    List<String> moneyName = Arrays.asList("人民币","美元","日元","欧元","英镑");
    List<Double> moneyRate = new ArrayList<>();



    public MoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        initView(view);

        return view;
    }

    public void initView(View view){
        et_origin_text = (EditText)view.findViewById(R.id.et_origin_text);
        btn_genQR_ok = (MaterialButton)view.findViewById(R.id.btn_genQR_ok);
        btn_genQR_ok.setOnClickListener(this);
        iv_showImg = (ImageView)view.findViewById(R.id.iv_showImg);
        rlt_showImg = (RelativeLayout)view.findViewById(R.id.rlt_showImg);
        rlt_showImg.setOnClickListener(this);

        cv_change_background = (CardView)view.findViewById(R.id.cv_change_background);
        cv_change_background.setOnClickListener(this);
        ll_morefragment_bkg = (LinearLayout)view.findViewById(R.id.ll_morefragment_bkg);

        btn_money1 = (MaterialButton)view.findViewById(R.id.btn_money1);
        btn_money2 = (MaterialButton)view.findViewById(R.id.btn_money2);
        et_money_text = (EditText)view.findViewById(R.id.et_money_text);
        iv_swap_money = (ImageView)view.findViewById(R.id.iv_swap_money);
        tv_money_result = (TextView)view.findViewById(R.id.tv_money_result);
        btn_money_ok = (MaterialButton)view.findViewById(R.id.btn_money_ok);
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


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_genQR_ok:
                String text = et_origin_text.getText().toString();
                if(text == null || text.length() == 0){
                    Toast.makeText(getContext(),"内容为空",Toast.LENGTH_SHORT).show();
                    return;
                }

                QRCodeWriter qrCodeWriter = new QRCodeWriter();
                try {
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    text = new String(text.getBytes("UTF-8"),"iso-8859-1");
                    Bitmap bitmap = barcodeEncoder.encodeBitmap(text, BarcodeFormat.QR_CODE, 400, 400);
                    String path = Environment.getExternalStorageDirectory().toString();;
                    File file = new File(path,"MyQrImage.png");
                    FileOutputStream out = new FileOutputStream(file);

                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
                    byte []bytes = baos.toByteArray();
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

            // 下面这个代码没有什么大作用
            case R.id.cv_change_background:
                int a = 1;
                Glide.with(getContext())
                        .load("https://uploadbeta.com/api/pictures/random/?key=%E6%8E%A8%E5%A5%B3%E9%83%8E")
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                                Drawable drawable = new BitmapDrawable(bitmap);
                                ll_morefragment_bkg.setBackground(drawable);
                            }
                        });
                 break;
            case R.id.btn_money1:
                ArrayAdapter adapter = new ArrayAdapter(getContext(),
                        android.R.layout.simple_expandable_list_item_1,moneyName);
                DialogPlus dialog = DialogPlus.newDialog(getActivity())
                        .setAdapter(adapter)
                        .setContentBackgroundResource(R.color.touming_hei)
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
                        android.R.layout.simple_expandable_list_item_1,moneyName);
                DialogPlus dialog2 = DialogPlus.newDialog(getActivity())
                        .setAdapter(adapter2)
                        .setContentBackgroundResource(R.color.touming_hei)
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
                if(text2 == null || text2.length()==0){
                    Toast.makeText(getContext(),"不能为空",Toast.LENGTH_SHORT).show();
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
        }

    }
}
