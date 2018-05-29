package com.stb.api;

import java.util.UUID;

public class STBGlobal {
	public static final String ServerUrl="https://cardtest.sacombank.com.vn:9443/epay/stb";
	public static final String ServerHandshakeUrl="https://cardtest.sacombank.com.vn:9443/epay/stbhandshake";
	public static final String ClientUsername="a5f3be4d-59b2-4261-a80a-89ba92dcfcbf";
	public static final String ClientPassword="85e645cf7b6d7553";
	public static final String ClientPrivateKey="MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCFhTCX8qB6Eut9AE9svCN+DjOcGG1+Pl93sjh8aTvD/VUgivzKcsERJAWHn6M6VJQ1HaDvBgEW5r7DPHS2LuWY1DoXNqa6TLW90TYK7KGPIuSSn0rqdwYVkhSDG0ybwSCUy/mBsfAxboK8px1jozqfx8MLAUl1L4ve2hFD/qwMVCIEWq8JlnPSULtssalJq8n91eRoHMTQpzSXIpDoK6p8lk82vUbHIJnNbpoY5Hac/uehR0Yt1WTpMCaR3NiSXGwf4DSJghVVGOy8sNw4m0Imjc8PoMlx12Ht2e9pjL2uesvaaUAUTk4v3s5B0XQWTmaypYw/W0n7LQuJ+gt4QFtzAgMBAAECggEABLoCTl64b5nb3ED2jtLaLswx54S125I5rvMQLNdM1lNRgCVrbX9CdmjgCT0NejMkS/0QhWWhBD6G4kJxn0Fpv09D/y5gGJg+lsmhMCHQVlDV5prc/A/al0jkkP+WZq0fiiTuYDj8V8OhvSvtTmmo2Z8ouGsDPrLAMNNnaRDK5mTGSng0TqDfO/Ylj2saBLXsv4ELxW7ZRR4OqMTfQ07am0zs8or5oFn9AxW/b4pcJuzVH2sqdUnCb3Luw2B+x1VGyc5l56pip9HTuD3zXZYroyE6FTsUvrYbDXFp1+Q3rmSH7IsqWJoGEvD98BDP5kmndMEYSc9SDTXvrImKs2s8AQKBgQD+18PIA7USZPZpB7zz4wqqduyC8ErYNnAsTd1ppOyBNt2RVtWdBMXdcH84OKJb1skJphWAFoLV9jzk5k2sq9xBSbSPgKDyo54TAuXzXm8Cghh58F+NIVQ/j1M3dRY+By6FtGHX4xLLjQk1xhRhyb9vKYSMlHEvzO+ZbT/7GIo60wKBgQCGIGWaWg3BHU+PkslLfZZefI/cVt5tDCpBy4dbU1gbyxHfemVwA0ebJCJN3y1kyMADeIOpyvCaSqI9cWEHuepOP4d7wjoJu5QPWIE+FunoGxgRUeKP+pHyTwbMHUikcPwg9EDoqM4xwdV/pj+5PWFP/LWg5FZBZ57kmQ/Gjrm44QKBgDsy9A/qjI2RVoxfd81P2JJwvdBM8ZX4T2Z7UAvSe/4oOKAig8zN5wjhsSvmPBEJe/n50tTGZe4+HTcvKKwL4EZqUL9lKf4zc+0Gp1Q5g6AZiIYLCKdILsH1EWe4k0olcPEuTexQm3Uddh4/vUxaCVZRVrExanmoKaCCeHJj6PhxAoGAfVDnnT9mengYTdynlpDQXZekEgpX5yrmkHLJFNraB53EJTkGbZGi5aDtLDTJBNFMFfITkiC6jG4PB7lXHxDlihZxT4A5Gb7Ypz3aA1kFUod+BTCsrjk/A1Yy06LIrFukNmX//2E4xeSExa9e+oQO190VFi5BKPrXyl34D1xqygECgYEAl0ESfWjPFmkIeiMS5YZZ3HjH3jHMDw5kChxc1p7Cj3LXlw+q2NFvcdHAZUyloPKOHFW0hx7Zlt9FuLLIeWC6ewu28+zludhtcwQCcB2vs05+Uvl7ZFs5R1sgdDIyGdtSEtQtEI/CiUhb2aiLkAcIZ3Hr7miUpk9UDe+ZfeI8/WM=";
	public static final String ServerPublicKey="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwQAvZB5qw+LGSAWrJKTDl7ApEGO6gUS10qLoMx/YoQwMdeceScCHPk6zREXfSyxkxwdAn3wmFm9FOQ6uRwlbgR+fZHFwhfXoFLIQ3pbmRQIa+0oGrrilnLybCsnM/gP7LtVeDVekPIip/yT9RnItenNnTodW91FoAU4WMawdw9oUT9Se/RvPzvHQXZ9gHj0nTFzvbc/qpgoy6MR8bxLgSVyPKtwF89k+DCDPr1KTpjNOjOsz5BZiO115KwJ5CPExUIuI9WcEV8liNEsShir6zamYRH1HLohHj7ZB1eVsWfPdDOgtd7t1FsJs/YqUo2juHWDAdqWZ87sDOUKdVbICTQIDAQAB";

	public static String SessionID;
	public static String SessionKey;
	public static int SessionTimeout;
	public static String PINPublicExponent;
	public static String PINPublicModulus;
	public static String PINPublicKeyExponent;
	public static String PINPublicKeyModulus;
//	public static final Properties Properties = new Properties();
	
	static {
		try {
//			Properties.load(new FileInputStream("STBClient.properties"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void generateSessionID() {
		SessionID = UUID.randomUUID().toString().replaceAll("-", "").substring(16);
		System.out.println("SessionID toString = " + UUID.randomUUID().toString());
		System.out.println("SessionID = " + SessionID);
	}
}
