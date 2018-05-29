package com.sacombank.merchants.widgets.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sacombank.merchants.R;
import com.sacombank.merchants.util.Utils;

/**
 * Created by DUY on 8/3/2017.
 */

public class DialogFactory {

    public interface DialogListener {
        void setEmail(String email);

        interface ForgotPassSuccessListener {
            void forgotPassSuccess();
        }
        interface ForgotPass1ErrorListener {
            void forgotPassError();
        }
        interface AccountInfoListener {
            void getAccountInfoError();
        }
        interface CancelTransactionSuccessListener {
            void cancelTransactionSuccess();
        }
        interface RetryListener {
            void retry();
        }

        interface PasswordInputListener {
            void setPassword(String password);
        }
        interface MobilePosListener {
            void goHome();
        }
        interface LogoutListener {
            void logOut();
        }

    }


    public static void createMessageDialog(Context context, String message) {
        final TranslucentDialog dialog = new TranslucentDialog(context);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_notify);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        TextView tvMessage = (TextView) dialog.findViewById(R.id.txtContent);
        tvMessage.setText(message);
        TextView btnQuit = (TextView) dialog.findViewById(R.id.btYes);
        btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public static void createTryAgainDialog(Context context, String message, final DialogListener.RetryListener listener) {
        final TranslucentDialog dialog = new TranslucentDialog(context);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_retry);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        TextView tvMessage = (TextView) dialog.findViewById(R.id.txtContent);
        tvMessage.setText(message);
        TextView btnTry = (TextView) dialog.findViewById(R.id.btYes);
        TextView btnQuit = (TextView) dialog.findViewById(R.id.btNo);
        btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnTry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.retry();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void createMessageDialogWithListener(Context context, String message, final DialogListener.ForgotPassSuccessListener forgotPassSuccessListener) {
        final TranslucentDialog dialog = new TranslucentDialog(context);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_notify);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        TextView tvMessage = (TextView) dialog.findViewById(R.id.txtContent);
        tvMessage.setText(message);
        TextView btnQuit = (TextView) dialog.findViewById(R.id.btYes);
        btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotPassSuccessListener.forgotPassSuccess();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void createMessageDialogForgotPassword(Context context, String message, final DialogListener.ForgotPass1ErrorListener forgotPass1ErrorListener) {
        final TranslucentDialog dialog = new TranslucentDialog(context);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_notify);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        TextView tvMessage = (TextView) dialog.findViewById(R.id.txtContent);
        tvMessage.setText(message);
        TextView btnQuit = (TextView) dialog.findViewById(R.id.btYes);
        btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotPass1ErrorListener.forgotPassError();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void createMessageDialogAccountInfo(Context context, String message, final DialogListener.AccountInfoListener accountInfoListener) {
        final TranslucentDialog dialog = new TranslucentDialog(context);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_notify);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        TextView tvMessage = (TextView) dialog.findViewById(R.id.txtContent);
        tvMessage.setText(message);
        TextView btnQuit = (TextView) dialog.findViewById(R.id.btYes);
        btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accountInfoListener.getAccountInfoError();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void createMessageDialogMobilePos(Context context, String message, final DialogListener.MobilePosListener listener) {
        final TranslucentDialog dialog = new TranslucentDialog(context);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_notify);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        TextView tvMessage = (TextView) dialog.findViewById(R.id.txtContent);
        tvMessage.setText(message);
        TextView btnQuit = (TextView) dialog.findViewById(R.id.btYes);
        btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.goHome();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void createMessageDialogLogout(Context context, String message, final DialogListener.LogoutListener listener) {
        final TranslucentDialog dialog = new TranslucentDialog(context);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_logout);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        TextView tvMessage = (TextView) dialog.findViewById(R.id.txtContent);
        tvMessage.setText(message);
        TextView btnYes = (TextView) dialog.findViewById(R.id.btYes);
        TextView btnNo = (TextView) dialog.findViewById(R.id.btNo);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.logOut();
                dialog.dismiss();
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public static void createCancelTransactionDialogWithListener(Context context, String message, final DialogListener.CancelTransactionSuccessListener listener) {
        final TranslucentDialog dialog = new TranslucentDialog(context);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_notify);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        TextView tvMessage = (TextView) dialog.findViewById(R.id.txtContent);
        tvMessage.setText(message);
        TextView btnQuit = (TextView) dialog.findViewById(R.id.btYes);
        btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.cancelTransactionSuccess();
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public static void createEmailInputDialog(Context context, String message, final DialogListener listener) {
        final TranslucentDialog dialog = new TranslucentDialog(context);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_email_input);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        TextView tvMessage = (TextView) dialog.findViewById(R.id.txtContent);
        final TextView tvError = (TextView) dialog.findViewById(R.id.tvError);
        tvMessage.setText(message);
        final EditText edtMail = (EditText) dialog.findViewById(R.id.edtEmail);
        TextView btnQuit = (TextView) dialog.findViewById(R.id.btYes);
        btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isValidEmail(edtMail.getText().toString())) {
                    dialog.dismiss();
                    listener.setEmail(edtMail.getText().toString());
                    tvError.setVisibility(View.GONE);
                } else {
                    tvError.setVisibility(View.VISIBLE);
                }
            }
        });
        dialog.show();
    }

    public static void createPaswordInputDialog(Context context, String message, final DialogListener.PasswordInputListener listener) {
        final TranslucentDialog dialog = new TranslucentDialog(context);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_input_password);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        TextView tvMessage = (TextView) dialog.findViewById(R.id.txtContent);
        //tvMessage.setText(message);
        final EditText edtPass = (EditText) dialog.findViewById(R.id.edt);
        TextView btnQuit = (TextView) dialog.findViewById(R.id.btYes);
        btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                listener.setPassword(edtPass.getText().toString());
            }
        });
        dialog.show();
    }
}
