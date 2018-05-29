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
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class STBAppClient {
	private static String _sessionID;
	private static String _sessionKey;
	private static int _sessionTimeout;
	
	public static void handshake(final String deviceID, final String languageID, final ApiResponse<AppSessionHandshake> listener) {
		(new Thread() {
			@Override
			public void run() {
				Gson gson = new GsonBuilder().disableHtmlEscaping().create();

				_sessionID = UUID.randomUUID().toString().replaceAll("-", "").substring(16);

				AppSessionHandshake sessionHandshake = new AppSessionHandshake();
				sessionHandshake.DeviceID = deviceID;
				sessionHandshake.LanguageID = languageID;
				sessionHandshake.SessionID = RSACrypto.encrypt(_sessionID, STBGlobal.ServerPublicKey); //STBGlobal.Properties.getProperty("ServerPublicKey"));

				// Create model
				STBModel model = new STBModel();
				model.Data = TripleDESCrypto.encrypt(gson.toJson(sessionHandshake),
						//STBGlobal.Properties.getProperty("ClientPassword"));
						STBGlobal.ClientPassword);
				model.FunctionName = sessionHandshake.getClass().getSimpleName();
				model.RequestDateTime = getRequestDateTime();
				model.RequestID = getRequestID();

				String authorization = String.format("Basic %s",
						Base64Util.getEncoder()
								.encodeToString(String.format("%s:%s", STBGlobal.ClientUsername,//STBGlobal.Properties.getProperty("ClientUsername"),
										//STBGlobal.Properties.getProperty("ClientPassword")).getBytes()));
										STBGlobal.ClientPassword).getBytes()));
				String signature = RSACrypto.sign(MD5Crypto.calculate(gson.toJson(model)),
						//STBGlobal.Properties.getProperty("ClientPrivateKey"));
						STBGlobal.ClientPrivateKey);

				try {
					SSLContext sslContext = SSLContext.getInstance("TLS");
					TrustManager[] trustManagers = getTrustManager();
					sslContext.init(null, trustManagers, new SecureRandom());
					HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

					// Create HTTP request
					URL url = new URL(STBGlobal.ServerHandshakeUrl);//STBGlobal.Properties.getProperty("ServerHandshakeUrl"));
					HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
					connection.setRequestMethod("POST");
					connection.setRequestProperty("Content-Type", "application/json");
					connection.setRequestProperty("Accept", "application/json");
					connection.setRequestProperty("Authorization", authorization);
					connection.setRequestProperty("Signature", signature);
					connection.setDoOutput(true);

					// Write stream
					BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
					writer.write(gson.toJson(model));
					writer.close();

					// Read stream
					BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
					model = gson.fromJson(reader.readLine(), STBModel.class);
					reader.close();
					System.out.println("handshake::response =  " + model.Data);
					if (model.Data != null && model.Data.length() > 0) {
						System.out.println("handshake::Data =  " + model.Data);
						sessionHandshake = gson.fromJson(
								TripleDESCrypto.decrypt(model.Data, STBGlobal.ClientPassword),//STBGlobal.Properties.getProperty("ClientPassword")),
								sessionHandshake.getClass());

						_sessionKey = TripleDESCrypto.decrypt(sessionHandshake.SessionKey, _sessionID);
						_sessionTimeout = sessionHandshake.SessionTimeout;

						STBGlobal.PINPublicKeyExponent = TripleDESCrypto.decrypt(sessionHandshake.PINPublicKeyExponent,
								_sessionID);
						STBGlobal.PINPublicKeyModulus = TripleDESCrypto.decrypt(sessionHandshake.PINPublicKeyModulus,
								_sessionID);
					}
				} catch (IOException | NoSuchAlgorithmException | KeyManagementException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
                    sessionHandshake.Description = model.Description;
                    sessionHandshake.RespCode = model.RespCode;
                    listener.onSuccess((AppSessionHandshake) sessionHandshake);
				}
			}
		}).start();
	}

	public static <T> void send(final T businessObject, final ApiResponse<T> listerner) {
		(new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//super.run();
				Gson gson = new GsonBuilder().disableHtmlEscaping().create();

				// Create model
				STBModel model = new STBModel();
				model.Data = TripleDESCrypto.encrypt(gson.toJson(businessObject), _sessionKey);
				String funcName = businessObject.getClass().getSimpleName();
				System.out.print("funcName:"+funcName);
				model.FunctionName =  funcName;
				model.RequestDateTime = getRequestDateTime();
				model.RequestID = getRequestID();
				model.SessionID = _sessionID;

				String authorization = String.format("Basic %s",
						Base64Util.getEncoder()
								.encodeToString(String.format("%s:%s", STBGlobal.ClientUsername,//STBGlobal.Properties.getProperty("ClientUsername"),
										//STBGlobal.Properties.getProperty("ClientPassword")).getBytes()));
										STBGlobal.ClientPassword).getBytes()));
				String signature = RSACrypto.sign(MD5Crypto.calculate(gson.toJson(model)),
						//STBGlobal.Properties.getProperty("ClientPrivateKey"));
						STBGlobal.ClientPrivateKey);

				T resultObject = businessObject;
				
				try {
					SSLContext sslContext = SSLContext.getInstance("TLS");
					TrustManager[] trustManagers = getTrustManager();
					sslContext.init(null, trustManagers, new SecureRandom());
					HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

					// Create HTTP request
					URL url = new URL(STBGlobal.ServerUrl);//STBGlobal.Properties.getProperty("ServerUrl"));
					HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
					connection.setRequestMethod("POST");
					connection.setRequestProperty("Content-Type", "application/json");
					connection.setRequestProperty("Accept", "application/json");
					connection.setRequestProperty("Authorization", authorization);
					connection.setRequestProperty("Signature", signature);
					connection.setDoOutput(true);
					// Write stream
					System.out.println("data before send =  " + gson.toJson(model));
					BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
					writer.write(gson.toJson(model));
					writer.close();
					// Read stream
					BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
					model = gson.fromJson(reader.readLine(), STBModel.class);
					reader.close();
                    System.out.println("data =  " + gson.toJson(model));
                    // Parse business object
					if (model.Data != null && model.Data.length() > 0) {
                        String resultData = TripleDESCrypto.decrypt(model.Data, _sessionKey);
                        System.out.println("resultData = " + resultData);
						resultObject = (T) gson.fromJson(resultData, businessObject.getClass());
					}
					else
                    {
						resultObject = businessObject;
					}
				} catch (IOException | NoSuchAlgorithmException | KeyManagementException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					// Assign response
					((STBResponse) resultObject).Description = model.Description;
					((STBResponse) resultObject).RespCode = model.RespCode;
                    if(model.RespCode != null && model.RespCode.equals("00"))
                        listerner.onSuccess(resultObject);
                    else {
						listerner.onError(resultObject);
					}
				}
			}
		}).start();
	}

	public static String getSessionKey(){
		return _sessionKey;
	}

	private static TrustManager[] getTrustManager() {
		TrustManager[] certs = new TrustManager[] { new X509TrustManager() {
			@Override
			public void checkClientTrusted(java.security.cert.X509Certificate[] xcs, String string)
					throws CertificateException {
			}

			@Override
			public void checkServerTrusted(java.security.cert.X509Certificate[] xcs, String string)
					throws CertificateException {
			}

			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		} };
		return certs;
	}

	private static String getRequestID() {
		return UUID.randomUUID().toString();
	}

	private static String getRequestDateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		return sdf.format(new Date());
	}
}
