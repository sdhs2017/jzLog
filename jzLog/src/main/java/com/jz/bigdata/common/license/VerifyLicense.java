package com.jz.bigdata.common.license;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.prefs.Preferences;

import org.springframework.stereotype.Service;

import de.schlichtherle.license.CipherParam;
import de.schlichtherle.license.DefaultCipherParam;
import de.schlichtherle.license.DefaultKeyStoreParam;
import de.schlichtherle.license.DefaultLicenseParam;
import de.schlichtherle.license.KeyStoreParam;
import de.schlichtherle.license.LicenseContent;
import de.schlichtherle.license.LicenseParam;
import de.schlichtherle.license.LicenseManager;

@Service(value="licenseService")
public class VerifyLicense {

	//common param
		private static String PUBLICALIAS = "";
		private static String STOREPWD = "";
		private static String SUBJECT = "";
		private static String licPath = "";
		private static String pubPath = "";
		/*private static String mac = "";*/
		
		
		public void setParam(String propertiesPath) {
			// 获取参数
			Properties prop = new Properties();
			InputStream in = getClass().getResourceAsStream(propertiesPath);
			try {
				prop.load(in);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			PUBLICALIAS = prop.getProperty("PUBLICALIAS");
			STOREPWD = prop.getProperty("STOREPWD");
			SUBJECT = prop.getProperty("SUBJECT");
			licPath = prop.getProperty("licPath");
			pubPath = prop.getProperty("pubPath");
			//mac = prop.getProperty("mac");
		}

		public boolean verify() {		
			/************** 证书使用者端执行 ******************/

			LicenseManager licenseManager = LicenseManagerHolder
					.getLicenseManager(initLicenseParams());
			// 安装证书
			try {
				//licenseManager.install(new File(licPath));
				File file = new File(getClass().getClassLoader().getResource(licPath).getFile());
				System.out.println(file.getAbsolutePath());
				licenseManager.install(file);
				System.out.println("客户端安装证书成功!");
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("客户端证书安装失败!");
				return false;
			}
			// 验证证书
			try {
				LicenseContent ss = licenseManager.verify();
				/*
				Object object =  ss.getExtra();
				
				//String stringArray[] = Arrays.asList(object).toArray(new String[0]);
				String [] ssss = (String[]) object;
				System.out.println(ssss[1]);
				if (validateMacAddress(ssss[0])) {
					System.out.println(ssss[0]);
				}*/
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = ss.getIssued();
				ss.getNotAfter();
				System.out.println(format.format(date));
				System.out.println("有效期："+format.format(ss.getNotBefore())+"-"+format.format(ss.getNotAfter()));
				System.out.println("客户端验证证书成功!");
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("客户端证书验证失效!");
				return false;
			}
			return true;
		}

		// 返回验证证书需要的参数
		private static LicenseParam initLicenseParams() {
			Preferences preference = Preferences
					.userNodeForPackage(VerifyLicense.class);
			CipherParam cipherParam = new DefaultCipherParam(STOREPWD);

			KeyStoreParam privateStoreParam = new DefaultKeyStoreParam(
					VerifyLicense.class, pubPath, PUBLICALIAS, STOREPWD, null);
			LicenseParam licenseParams = new DefaultLicenseParam(SUBJECT,
					preference, privateStoreParam, cipherParam);
			return licenseParams;
		}
		
		/**
		 * 验证mac地址
		 * @param macAddress
		 * @return
		 * @throws SocketException
		 */
		/*public static boolean validateMacAddress(String macAddress) throws SocketException {
	        boolean returnFlag = false;
	        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
	        
	        for (NetworkInterface netint : Collections.list(nets)) {
	            byte[] mac = netint.getHardwareAddress();
	            StringBuilder sb = new StringBuilder();
	            if (mac != null) {
	                for (int i = 0; i < mac.length; i++) {
	                    sb.append(String.format("%02X%s", mac[i],
	                            (i < mac.length - 1) ? "-" : ""));
	                }
	                System.out.println("mac=" + sb.toString());
	            }
	            if (sb.toString().equals(macAddress)) {
	                returnFlag = true;
	            }
	        }
	        return returnFlag;

	    }*/
		
		public static void main(String [] args) {
			File file = new File(new VerifyLicense().getClass().getClassLoader().getResource("").getFile());
			System.out.println(file.getAbsolutePath());
		}
}
