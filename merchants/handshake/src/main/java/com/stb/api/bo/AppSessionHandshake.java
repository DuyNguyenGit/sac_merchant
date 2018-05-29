package com.stb.api.bo;

import com.stb.api.model.STBResponse;

public class AppSessionHandshake extends STBResponse {
	public String DeviceID;
	public String LanguageID;
	public String SessionID;
	public String RefNumber;
	
	public String SessionKey;
	public int SessionTimeout;
	public String PINPublicKeyExponent;
	public String PINPublicKeyModulus;
}
