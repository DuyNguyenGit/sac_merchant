package com.stb.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stb.api.bo.AppSessionHandshake;
import com.stb.api.crypto.Base64Util;
import com.stb.api.crypto.MD5Crypto;
import com.stb.api.crypto.RSACrypto;
import com.stb.api.crypto.TripleDESCrypto;
import com.stb.api.listeners.ApiResponse;
import com.stb.api.model.STBModel;
import com.stb.api.model.STBResponse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

//import java.util.Base64;

public class STBClient {
    /**
     * handshake protocol
     *
     * @param businessObject
     * @param <T>
     * @return
     */
    public static <T> void handshake(final T businessObject, final ApiResponse<AppSessionHandshake> listener) {

        (new Thread() {
            @Override
            public void run() {
                super.run();
                Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                // Create model
                System.out.println("businessObject 11 === " + new Gson().toJson(businessObject));
                STBModel model = new STBModel();
                model.Data = TripleDESCrypto.encrypt(gson.toJson(businessObject), STBGlobal.ClientPassword);
                System.out.println("model.Data = " + model.Data);
                model.FunctionName = businessObject.getClass().getSimpleName();
                model.RequestDateTime = getRequestDateTime();
                model.RequestID = getRequestID();
                System.out.println("SESSIONID 11 === " + model.SessionID);
                System.out.println("model 11 === " + new Gson().toJson(model));

                String authorization = String.format("Basic %s", Base64Util.getEncoder().encodeToString(String.format("%s:%s", STBGlobal.ClientUsername, STBGlobal.ClientPassword).getBytes()));
                String signature = RSACrypto.sign(MD5Crypto.calculate(gson.toJson(model)), STBGlobal.ClientPrivateKey);
                T resultObject = businessObject;
                try {
                    SSLContext sslContext = SSLContext.getInstance("TLS");
                    TrustManager[] trustManagers = getTrustManager();
                    sslContext.init(null, trustManagers, new SecureRandom());
                    HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

                    System.setProperty("https.proxyHost", "proxy.sacombank.corp.vn");
                    System.setProperty("https.proxyPort", "3128");
                    System.setProperty("http.proxyUser", "toantns26467");
                    System.setProperty("http.proxyPassword", "");

                    // Create HTTP request
                    URL url = new URL(STBGlobal.ServerHandshakeUrl);
                    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("Accept", "application/json");
                    connection.setRequestProperty("Authorization", authorization);
                    connection.setRequestProperty("Signature", signature);
                    connection.setReadTimeout(7000);
                    connection.setConnectTimeout(7000);
                    connection.setDoOutput(true);
//                    connection.connect();

                    // Write stream
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                    writer.write(gson.toJson(model));
                    writer.close();

                    // Read stream
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    String result = new String();
                    while ((inputLine = reader.readLine()) != null) {
                        result += inputLine;
                    }
                    System.out.println("result  === " + result);
                    model = gson.fromJson(result, STBModel.class);
                    reader.close();

                    System.out.println("model 22 === " + new Gson().toJson(model));
                    System.out.println("RespCode 11 === " + model.RespCode);
                    System.out.println("Data 11 === " + model.Data);
                    // Parse business object
                    if (model.Data != null && model.Data.length() > 0) {

                        System.out.println("Data 22 === " + model.Data);
                        String data = TripleDESCrypto.decrypt(model.Data, STBGlobal.ClientPassword);
                        resultObject = (T) gson.fromJson(data, businessObject.getClass());

                        System.out.println("RespCode 22 === " + model.RespCode);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    System.out.println("Exception === " + e.getMessage());
                    e.printStackTrace();
                } finally {
                    // Assign response
                    ((STBResponse) resultObject).Description = model.Description;
                    ((STBResponse) resultObject).RespCode = model.RespCode;
                    listener.onSuccess((AppSessionHandshake) resultObject);
                }

            }
        }).start();

    }


    public static <T> void send(final T businessObject, final String sessionID, final String sessionKey) {
        (new Thread() {
            @Override
            public void run() {
                super.run();
                if (sessionKey == null || sessionKey.length() != 16) {
                    System.out.println("Invalid SessionKey");
                    return;
                }
                Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                System.out.println("sessionKey send  =  " + sessionKey);
                // Create model
                STBModel model = new STBModel();
                model.Data = TripleDESCrypto.encrypt(gson.toJson(businessObject), sessionKey);
                model.FunctionName = businessObject.getClass().getSimpleName();
                model.RequestDateTime = getCurentDateTime();
                System.out.println("model.RequestDateTime send = " + model.RequestDateTime);
                model.RequestID = getRequestID();
                model.SessionID = sessionID;

                String authorization = String.format("Basic %s", Base64Util.getEncoder().encodeToString(String.format("%s:%s", STBGlobal.ClientUsername, STBGlobal.ClientPassword).getBytes()));
                String signature = RSACrypto.sign(MD5Crypto.calculate(gson.toJson(model)), STBGlobal.ClientPrivateKey);
                T resultObject = businessObject;
                try {
                    SSLContext sslContext = SSLContext.getInstance("TLS");
                    TrustManager[] trustManagers = getTrustManager();
                    sslContext.init(null, trustManagers, new SecureRandom());
                    HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

                    System.setProperty("https.proxyHost", "proxy.sacombank.corp.vn");
                    System.setProperty("https.proxyPort", "3128");
                    System.setProperty("http.proxyUser", "toantns26467");
                    System.setProperty("http.proxyPassword", "");

                    // Create HTTP request
                    URL url = new URL(STBGlobal.ServerUrl);
                    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("Accept", "application/json");
                    connection.setRequestProperty("Authorization", authorization);
                    connection.setRequestProperty("Signature", signature);
                    connection.setDoOutput(true);
                    System.out.println("model 11 = " + gson.toJson(model));
                    // Write stream
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                    writer.write(gson.toJson(model));
                    writer.close();

                    System.out.println("model.RequestDateTime read = " + getCurentDateTime());
                    System.out.println("model 22 = " + gson.toJson(model));

                    // Read stream
                    InputStream inputStream = connection.getInputStream();
                    //System.out.println("inputStream = " + gson.toJson(inputStream));
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                    System.out.println("reader = " + reader.readLine());
                    model = gson.fromJson(reader.readLine(), STBModel.class);
                    reader.close();
//                    System.out.println("model = " + gson.toJson(model));
                    // Parse business object
                    if (model.Data != null && model.Data.length() > 0) {
                        String data = TripleDESCrypto.decrypt(model.Data, sessionKey);
                        resultObject = (T) gson.fromJson(data, businessObject.getClass());
//                        System.out.println("resultObject = " + gson.toJson(resultObject));
                    }
                } catch (IOException | NoSuchAlgorithmException | KeyManagementException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    // Assign response
                    ((STBResponse) resultObject).Description = model.Description;
                    ((STBResponse) resultObject).RespCode = model.RespCode;
                }
            }
        }).start();

    }


//
//    public static <T> void send(final T businessObject, final String sessionID, final ApiResponse<ConsumerAccountInquiry> listener) {
//        (new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                Gson gson = new GsonBuilder().disableHtmlEscaping().create();
//                System.out.println("sessionID send = " + sessionID);
//                // Create model
//                STBModel model = new STBModel();
//                model.Data = TripleDESCrypto.encrypt(gson.toJson(businessObject), STBGlobal.ClientPassword);
//                model.FunctionName = businessObject.getClass().getSimpleName();
//                model.RequestDateTime = getRequestDateTime();
//                model.RequestID = getRequestID();
//                model.SessionID = sessionID;
//
//                String authorization = String.format("Basic %s", Base64Util.getEncoder().encodeToString(String.format("%s:%s", STBGlobal.ClientUsername, STBGlobal.ClientPassword).getBytes()));
//                String signature = RSACrypto.sign(MD5Crypto.calculate(gson.toJson(model)), STBGlobal.ClientPrivateKey);
//                T resultObject = businessObject;
//                try {
//                    SSLContext sslContext = SSLContext.getInstance("TLS");
//                    TrustManager[] trustManagers = getTrustManager();
//                    sslContext.init(null, trustManagers, new SecureRandom());
//                    HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
//
//                    System.setProperty("https.proxyHost", "proxy.sacombank.corp.vn");
//                    System.setProperty("https.proxyPort", "3128");
//                    System.setProperty("http.proxyUser", "toantns26467");
//                    System.setProperty("http.proxyPassword", "");
//
//                    // Create HTTP request
//                    URL url = new URL(STBGlobal.ServerUrl);
//                    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
//                    connection.setRequestMethod("POST");
//                    connection.setRequestProperty("Content-Type", "application/json");
//                    connection.setRequestProperty("Accept", "application/json");
//                    connection.setRequestProperty("Authorization", authorization);
//                    connection.setRequestProperty("Signature", signature);
//                    connection.setDoOutput(true);
//
//                    // Write stream
//                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
//                    writer.write(gson.toJson(model));
//                    writer.close();
//                    System.out.println("result send  === " + gson.toJson(model));
//                    // Read stream
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                    String inputLine;
//                    String result = new String();
//                    while ((inputLine = reader.readLine()) != null) {
//                        result += inputLine;
//                    }
//                    System.out.println("result  === " + result);
//                    model = gson.fromJson(result, STBModel.class);
//                    reader.close();
//
//
//                    System.out.println("model 22 send=== " + new Gson().toJson(model));
//                    System.out.println("RespCode 11 send=== " + model.RespCode);
//                    System.out.println("Data 11 send=== " + model.Data);
//                    // Parse business object
//                    if (model.Data != null && model.Data.length() > 0) {
//                        String data = TripleDESCrypto.decrypt(model.Data, STBGlobal.ClientPassword);
//                        resultObject = (T) gson.fromJson(data, businessObject.getClass());
//                        System.out.println("RespCode 22 send=== " + model.RespCode);
//                    }
//                } catch (IOException | NoSuchAlgorithmException | KeyManagementException e) {
//                    // TODO Auto-generated catch block
//                    System.out.println("Exception send=== " + e.getMessage());
//                    e.printStackTrace();
//                } finally {
//                    // Assign response
//                    ((STBResponse) resultObject).Description = model.Description;
//                    ((STBResponse) resultObject).RespCode = model.RespCode;
//                    listener.onSuccess((ConsumerAccountInquiry) resultObject);
//                }
//
//            }
//        }).start();
//
//
//    }

    public static TrustManager[] getTrustManager() {
        TrustManager[] certs = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] xcs, String string) throws CertificateException {
                //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] xcs, String string) throws CertificateException {
                //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null; //To change body of generated methods, choose Tools | Templates.
            }
        }};
        return certs;
    }

    private static String getRequestID() {
        return UUID.randomUUID().toString();
    }

    private static String getRequestDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, 3);
        return sdf.format(calendar.getTime());
    }


    private static String getCurentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(new Date());
    }


}
