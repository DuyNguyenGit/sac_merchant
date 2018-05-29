package com.sacombank.merchants.view.impl;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.sacombank.merchants.R;
import com.sacombank.merchants.SacombankApp;
import com.sacombank.merchants.api.ApiManager;
import com.sacombank.merchants.injection.AppComponent;
import com.sacombank.merchants.injection.DaggerQrcodeDynamicGenerateViewComponent;
import com.sacombank.merchants.injection.QrcodeDynamicGenerateViewModule;
import com.sacombank.merchants.model.jsonobject.QRCodeSharing;
import com.sacombank.merchants.presenter.QrcodeDynamicGeneratePresenter;
import com.sacombank.merchants.presenter.loader.PresenterFactory;
import com.sacombank.merchants.util.Utils;
import com.sacombank.merchants.view.QrcodeDynamicGenerateView;
import com.sacombank.merchants.widgets.dialog.DialogFactory;
import com.stb.api.bo.AppSessionHandshake;
import com.stb.api.bo.MerchantAccountLogin;
import com.stb.api.bo.MerchantQRGeneration;
import com.stb.api.bo.MerchantQRShare;
import com.stb.api.listeners.ApiResponse;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.inject.Inject;

public final class QRCodeDynamicGenerateFragment extends BaseFragment<QrcodeDynamicGeneratePresenter, QrcodeDynamicGenerateView> implements QrcodeDynamicGenerateView, View.OnClickListener {
    private static final String TAG = QRCodeDynamicGenerateFragment.class.getSimpleName();
    @Inject
    PresenterFactory<QrcodeDynamicGeneratePresenter> mPresenterFactory;
    private ImageView imgQR;
    private Button btnShare,btnDownload, btnTaoMoi;
    public static final String KEY = "key";
    private int key = 0;
    String qrBase64;
    private final int NOTIFICATION_ID=1989;
    private String mEmail;
    // Your presenter is available using the mPresenter variable

    public QRCodeDynamicGenerateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            key = bundle.getInt(KEY, 0);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_qrcode_dynamic_generate, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnShare = (Button) view.findViewById(R.id.btnShare);
        btnShare.setOnClickListener(this);
        btnDownload= (Button) view.findViewById(R.id.btnDownload);
        btnDownload.setOnClickListener(this);
        imgQR = (ImageView) view.findViewById(R.id.imgQRCodeStatic);

        if (key == 0) {
            MerchantAccountLogin userInfo = new Gson().fromJson(((SacombankApp) getActivity().getApplication()).userAccountPref.getValue(),
                    MerchantAccountLogin.class);
            qrBase64 = userInfo.StaticQRData;
        } else {
            MerchantQRGeneration qrCodeInfo = new Gson().fromJson(((SacombankApp) getActivity().getApplication()).qrCodeDynamicPref.getValue(),
                    MerchantQRGeneration.class);
            qrBase64 = qrCodeInfo.QRStream;
        }
        Log.e(TAG, "onViewCreated: >>>" + qrBase64);
        if (qrBase64 != null && !TextUtils.isEmpty(qrBase64)) {
            imgQR.setImageBitmap(Utils.convertBase64StringToBitmap(qrBase64));
        } else {
            Toast.makeText(getActivity(), "Dữ liệu trả về không có giá trị", Toast.LENGTH_LONG).show();
        }

        // Your code here
        // Do not call mPresenter from here, it will be null! Wait for onStart
    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerQrcodeDynamicGenerateViewComponent.builder()
                .appComponent(parentComponent)
                .qrcodeDynamicGenerateViewModule(new QrcodeDynamicGenerateViewModule())
                .build()
                .inject(this);
    }

    @Override
    protected void handleCallApi() {
        if (mPresenter != null) {
            ((BottomTabbarActivity) getActivity()).showHideCover(true);
            mPresenter.shareQRCode(new QRCodeSharing(mEmail, qrBase64, "note"));
        }
    }

    @Override
    public void onBackPressed() {
    }

    @NonNull
    @Override
    protected PresenterFactory<QrcodeDynamicGeneratePresenter> getPresenterFactory() {
        return mPresenterFactory;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnShare:
                DialogFactory.createEmailInputDialog(getActivity(), getString(R.string.dialog_message_email),
                        new DialogFactory.DialogListener() {
                            @Override
                            public void setEmail(String email) {
                                mEmail = email;
                                handleCallApi();
                            }
                        });

                break;
            case R.id.btnDownload:
                if (qrBase64 != null && !TextUtils.isEmpty(qrBase64)) {
                    //----------------
                    saveBitmap(Utils.convertBase64StringToBitmap(qrBase64));
                } else {
                    Toast.makeText(getActivity(), "Dữ liệu trả về không có giá trị", Toast.LENGTH_LONG).show();
                }
        }
    }

    @Override
    public <T> void sharedSuccess(T result) {
        Log.e(TAG, "sharedSuccess: >>>");
        final String description = ((MerchantQRShare) result).Description;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((BottomTabbarActivity) getActivity()).showHideCover(false);
                DialogFactory.createMessageDialog(getActivity(), TextUtils.isEmpty(description) ? getString(R.string.dialog_share_success) : description);
            }
        });

    }

    @Override
    public <T> void sharedError(T result) {
        if (result != null) {

            if (((MerchantQRShare) result).RespCode != null && ((MerchantQRShare) result).RespCode.equalsIgnoreCase(ApiManager.SESSION_TIMEOUT)) {

                ApiManager.requestHandshake(new ApiResponse<AppSessionHandshake>() {
                    @Override
                    public void onSuccess(AppSessionHandshake result) {

                        if (userLogined())
                            gotoLogin();
                        else
                            handleCallApi();
                    }

                    @Override
                    public void onError(AppSessionHandshake error) {
                        showErrorHandshake();
                    }
                });
            } else {

                final String errorMsg = ((MerchantQRShare) result).Description;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((BottomTabbarActivity) getActivity()).showHideCover(false);
                        DialogFactory.createMessageDialog(getActivity(), TextUtils.isEmpty(errorMsg) ? getString(R.string.dialog_share_error) : errorMsg);

                    }
                });

            }

        } else {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((BottomTabbarActivity) getActivity()).showHideCover(false);
                    DialogFactory.createMessageDialog(getActivity(), getString(R.string.dialog_share_error));
                }
            });
        }
    }

    private void saveBitmap(Bitmap bitmap){
        if(bitmap!=null){
            try {
                String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/DCIM/Camera/";
                String filename = new SimpleDateFormat("yyMMddHHmmss").format(Calendar.getInstance().getTime()) + ".png";
                File folder = new File(path);
                if (!folder.exists()) {
                    folder.mkdirs();
                }
                System.out.print(folder.toString());
                FileOutputStream fileOutputStream = new FileOutputStream(path+"//"+filename);
                BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                bos.flush();
                bos.close();
                fileOutputStream.flush();
                fileOutputStream.close();
                //------Add image to gallery
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA,path+"//"+filename);
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                //-------------------
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("content://media/internal/images/media"));
                Intent galleryIntent = new Intent(Intent.ACTION_VIEW, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivity(galleryIntent);
                notification(galleryIntent);
                Toast.makeText(getActivity(), "Tải hình QR-Code thành công!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Vui lòng cấp quyền truy cập bộ nhớ cho ứng dụng!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void notification(Intent intent){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getActivity())
                        .setSmallIcon(R.drawable.icon_notification)
                        .setContentTitle("Thông báo")
                        .setContentText("Tải hình thành công!")
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        //Intent resultIntent = new Intent(this, ResultActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getActivity());
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    @Override
    public void onResume() {
        super.onResume();
        NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(NOTIFICATION_ID);
    }

    private void processPushFragment(Fragment fragment) {
        if (fragment == null) return;
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_content, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
