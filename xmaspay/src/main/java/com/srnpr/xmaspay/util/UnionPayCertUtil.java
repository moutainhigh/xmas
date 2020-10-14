package com.srnpr.xmaspay.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.RSAPublicKeySpec;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang.StringUtils;
import com.srnpr.xmaspay.config.UnionPayConfig;
import com.srnpr.zapcom.baseclass.BaseClass;

/**
 * 银联支付证书工具类
 * @author pang_jhui
 *
 */
public class UnionPayCertUtil extends BaseClass {
	
	private volatile static UnionPayCertUtil INSTANCE = null;
	
	/** 证书容器. */
	private static KeyStore keyStore = null;
	/** 密码加密证书 */
	private static X509Certificate encryptCert = null;
	/** 验证签名证书. */
	private static X509Certificate validateCert = null;
	/** 验签证书存储Map. */
	private static Map<String, X509Certificate> certMap = new HashMap<String, X509Certificate>();
	/** 基于Map存储多商户RSA私钥 */
	private final static Map<String, KeyStore> certKeyStoreMap = new ConcurrentHashMap<String, KeyStore>();
	/**银联支付*/
	private static UnionPayConfig unionPayConfig = new UnionPayConfig();

	public UnionPayCertUtil() {
		
		init();
		
	}
	
	public static UnionPayCertUtil getInstance(){
		
		if(INSTANCE == null){
			
			synchronized (UnionPayCertUtil.class) {
				
				if(INSTANCE == null){
					
					INSTANCE = new UnionPayCertUtil();
					
				}
				
			}
			
		}
		
		return INSTANCE;
		
	}

	/**
	 * 初始化所有证书.
	 */
	public void init() {
		
		if (unionPayConfig.getSingleMode()) {
			// 单证书模式,初始化配置文件中的签名证书
			initSignCert();
		}
		
		initEncryptCert();// 初始化加密公钥证书
		initValidateCertFromDir();// 初始化所有的验签证书
		
	}

	/**
	 * 加载签名证书
	 */
	public void initSignCert(){

		if (null != keyStore) {

			keyStore = null;

		}

		try {
			keyStore = getKeyInfo(unionPayConfig.getSignCertPath(), unionPayConfig.getSignCertPwd(),
					unionPayConfig.getSignCertType());
		} catch (IOException e) {
			
			bLogError(0, e.getMessage());
			
		}

	}

	

	/**
	 * 加载RSA签名证书
	 * 
	 * @param certFilePath
	 * @param certPwd
	 * @throws IOException 
	 */
	public void loadRsaCert(String certFilePath, String certPwd) {
		
		KeyStore keyStore = null;
		
		try {
			
			keyStore = getKeyInfo(certFilePath, certPwd, "PKCS12");
			
		} catch (IOException e) {
			
			bLogError(0, e.getMessage());
			
		}
		
		certKeyStoreMap.put(certFilePath, keyStore);

	}

	/**
	 * 加载密码加密证书 目前支持有两种加密
	 */
	private void initEncryptCert() {
		
		if (StringUtils.isNotBlank(unionPayConfig.getEncryptCertPath())) {
			
			encryptCert = initCert(unionPayConfig.getEncryptCertPath());
			
		} 
	}

	/**
	 * 证书初始化
	 * @param path
	 * @return
	 */
	private X509Certificate initCert(String path) {
		X509Certificate encryptCertTemp = null;
		CertificateFactory cf = null;
		FileInputStream in = null;
		try {
			cf = CertificateFactory.getInstance("X.509");
			in = new FileInputStream(path);
			encryptCertTemp = (X509Certificate) cf.generateCertificate(in);

		} catch (CertificateException e) {
			bLogError(0, "InitCert Error:" + e.getMessage());

		} catch (FileNotFoundException e) {

			bLogError(0, "InitCert Error File Not Found:" + e.getMessage());

		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					bLogError(0, e.getMessage());
				}
			}
		}
		return encryptCertTemp;
	}

	/**
	 * 从指定目录下加载验证签名证书
	 * 
	 */
	private void initValidateCertFromDir() {
		
		certMap.clear();
		
		String dir = unionPayConfig.getValidateCertDir();
		
		bLogInfo(0, "加载验证签名证书目录==>" + dir);
		
		if (StringUtils.isNotBlank(dir)) {
			
			bLogError(0, "ERROR: acpsdk.validateCert.dir is empty");
			
			return;
			
		}
		
		CertificateFactory cf = null;
		
		FileInputStream in = null;
		
		try {
			
			cf = CertificateFactory.getInstance("X.509");
			
			File fileDir = new File(dir);
			
			File[] files = fileDir.listFiles(new CerFilter());
			
			for (int i = 0; i < files.length; i++) {
				
				File file = files[i];
				
				in = new FileInputStream(file.getAbsolutePath());
				
				validateCert = (X509Certificate) cf.generateCertificate(in);
				
				certMap.put(validateCert.getSerialNumber().toString(), validateCert);
				
				// 打印证书加载信息,供测试阶段调试
				bLogError(0, "[" + file.getAbsolutePath() + "][CertId=" + validateCert.getSerialNumber().toString() + "]");
			}
			
			bLogError(0, "LoadVerifyCert Successful");
			
		} catch (CertificateException e) {
			
			bLogError(0, e.getMessage());
			
		} catch (FileNotFoundException e) {
			
			bLogError(0, e.getMessage());
			
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					bLogError(0, e.getMessage());
				}
			}
		}
	}

	/**
	 * 获取签名证书私钥（单证书模式）
	 * 
	 * @return
	 */
	public  PrivateKey getSignCertPrivateKey() {
		try {
			Enumeration<String> aliasenum = keyStore.aliases();
			String keyAlias = null;
			if (aliasenum.hasMoreElements()) {
				keyAlias = aliasenum.nextElement();
			}
			PrivateKey privateKey = (PrivateKey) keyStore.getKey(keyAlias,
					unionPayConfig.getSignCertPwd().toCharArray());
			return privateKey;
		} catch (KeyStoreException e) {
			
			bLogError(0, "getSignCertPrivateKey Error:"+e.getMessage());
			
			return null;
			
		} catch (UnrecoverableKeyException e) {
			
			bLogError(0, "getSignCertPrivateKey Error:"+e.getMessage());
			
			return null;
			
		} catch (NoSuchAlgorithmException e) {
			
			bLogError(0, "getSignCertPrivateKey Error:"+e.getMessage());
			
			return null;
			
		}
	}

	

	public  PrivateKey getSignCertPrivateKeyByStoreMap(String certPath, String certPwd) {
		if (!certKeyStoreMap.containsKey(certPath)) {

			loadRsaCert(certPath, certPwd);

		}
		try {
			Enumeration<String> aliasenum = certKeyStoreMap.get(certPath).aliases();
			String keyAlias = null;
			if (aliasenum.hasMoreElements()) {
				keyAlias = aliasenum.nextElement();
			}
			PrivateKey privateKey = (PrivateKey) certKeyStoreMap.get(certPath).getKey(keyAlias, certPwd.toCharArray());
			return privateKey;
		} catch (KeyStoreException e) {
			bLogError(0, "getSignCertPrivateKeyByStoreMap Error:"+e.getMessage());
			return null;
		} catch (UnrecoverableKeyException e) {
			bLogError(0, "getSignCertPrivateKeyByStoreMap Error:"+e.getMessage());
			return null;
		} catch (NoSuchAlgorithmException e) {
			bLogError(0, "getSignCertPrivateKeyByStoreMap Error:"+e.getMessage());
			return null;
		}
	}

	/**
	 * 获取加密证书公钥.密码加密时需要
	 * 
	 * @return
	 */
	public PublicKey getEncryptCertPublicKey() {
		if (null == encryptCert) {
			String path = unionPayConfig.getEncryptCertPath();
			if (StringUtils.isNotBlank(path)) {
				encryptCert = initCert(path);
				return encryptCert.getPublicKey();
			} else {
				bLogError(0, "ERROR: acpsdk.encryptCert.path is empty");
				return null;
			}
		} else {
			return encryptCert.getPublicKey();
		}
	}

	/**
	 * 验证签名证书
	 * 
	 * @return 验证签名证书的公钥
	 */
	public PublicKey getValidateKey() {
		if (null == validateCert) {
			return null;
		}
		return validateCert.getPublicKey();
	}

	/**
	 * 通过certId获取证书Map中对应证书的公钥
	 * 
	 * @param certId
	 *            证书物理序号
	 * @return 通过证书编号获取到的公钥
	 */
	public PublicKey getValidateKey(String certId) {
		X509Certificate cf = null;
		if (certMap.containsKey(certId)) {
			// 存在certId对应的证书对象
			cf = certMap.get(certId);
			return cf.getPublicKey();
		} else {
			// 不存在则重新Load证书文件目录
			initValidateCertFromDir();
			if (certMap.containsKey(certId)) {
				// 存在certId对应的证书对象
				cf = certMap.get(certId);
				return cf.getPublicKey();
			} else {
				bLogError(0, "缺少certId=[" + certId + "]对应的验签证书.");
				return null;
			}
		}
	}

	/**
	 * 获取签名证书中的证书序列号（单证书）
	 * 
	 * @return 证书的物理编号
	 */
	public String getSignCertId() {
		try {
			Enumeration<String> aliasenum = keyStore.aliases();
			String keyAlias = null;
			if (aliasenum.hasMoreElements()) {
				keyAlias = aliasenum.nextElement();
			}
			X509Certificate cert = (X509Certificate) keyStore.getCertificate(keyAlias);
			return cert.getSerialNumber().toString();
		} catch (Exception e) {
			bLogError(0, "getSignCertId Error:"+e.getMessage());
			return null;
		}
	}

	/**
	 * 获取加密证书的证书序列号
	 * 
	 * @return
	 */
	public String getEncryptCertId() {
		if (null == encryptCert) {
			String path = unionPayConfig.getEncryptCertPath();
			if (StringUtils.isNotBlank(path)) {
				encryptCert = initCert(path);
				return encryptCert.getSerialNumber().toString();
			} else {
				bLogError(0, "ERROR: acpsdk.encryptCert.path is empty");
				return null;
			}
		} else {
			return encryptCert.getSerialNumber().toString();
		}
	}

	/**
	 * 获取签名证书公钥对象
	 * 
	 * @return
	 */
	public PublicKey getSignPublicKey() {
		try {
			Enumeration<String> aliasenum = keyStore.aliases();
			String keyAlias = null;
			if (aliasenum.hasMoreElements()) // we are readin just one
			// certificate.
			{
				keyAlias = (String) aliasenum.nextElement();
			}
			Certificate cert = keyStore.getCertificate(keyAlias);
			PublicKey pubkey = cert.getPublicKey();
			return pubkey;
		} catch (Exception e) {
			bLogError(0, e.getMessage());
			return null;
		}
	}

	/**
	 * 将证书文件读取为证书存储对象
	 * 
	 * @param pfxkeyfile
	 *            证书文件名
	 * @param keypwd
	 *            证书密码
	 * @param type
	 *            证书类型
	 * @return 证书对象
	 * @throws IOException
	 */
	public KeyStore getKeyInfo(String pfxkeyfile, String keypwd, String type) throws IOException {
		bLogInfo(0, "加载签名证书==>" + pfxkeyfile); 
		FileInputStream fis = null;
		try {
			KeyStore ks = null;
			if ("JKS".equals(type)) {
				ks = KeyStore.getInstance(type);
			} else if ("PKCS12".equals(type)) {
				String jdkVendor = System.getProperty("java.vm.vendor");
				String javaVersion = System.getProperty("java.version");
				bLogError(0, "java.vm.vendor=[" + jdkVendor + "]");
				bLogError(0, "java.version=[" + javaVersion + "]");
				if (null != jdkVendor && jdkVendor.startsWith("IBM")) {
					// 如果使用IBMJDK,则强制设置BouncyCastleProvider的指定位置,解决使用IBMJDK时兼容性问题
					Security.insertProviderAt(new org.bouncycastle.jce.provider.BouncyCastleProvider(), 1);
					printSysInfo();
				} else {
					Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
				}
				
				ks = KeyStore.getInstance(type);
			}
			bLogError(0, "Load RSA CertPath=[" + pfxkeyfile + "],Pwd=[" + keypwd + "]");
			fis = new FileInputStream(pfxkeyfile);
			char[] nPassword = null;
			nPassword = null == keypwd || "".equals(keypwd.trim()) ? null : keypwd.toCharArray();
			if (null != ks) {
				ks.load(fis, nPassword);
			}
			return ks;
		} catch (Exception e) {
			if (Security.getProvider("BC") == null) {
				bLogError(0, "BC Provider not installed.");
			}
			bLogError(0, "getKeyInfo Error:"+e.getMessage());
			if ((e instanceof KeyStoreException) && "PKCS12".equals(type)) {
				Security.removeProvider("BC");
			}
			return null;
		} finally {
			if (null != fis)
				fis.close();
		}
	}

	// 打印系统环境信息
	public void printSysInfo() {
		bLogInfo(0, "================= SYS INFO begin====================");
		bLogInfo(0,"os_name:" + System.getProperty("os.name"));
		bLogInfo(0,"os_arch:" + System.getProperty("os.arch"));
		bLogInfo(0,"os_version:" + System.getProperty("os.version"));
		bLogInfo(0,"java_vm_specification_version:" + System.getProperty("java.vm.specification.version"));
		bLogInfo(0,"java_vm_specification_vendor:" + System.getProperty("java.vm.specification.vendor"));
		bLogInfo(0,"java_vm_specification_name:" + System.getProperty("java.vm.specification.name"));
		bLogInfo(0,"java_vm_version:" + System.getProperty("java.vm.version"));
		bLogInfo(0,"java_vm_name:" + System.getProperty("java.vm.name"));
		bLogInfo(0,"java.version:" + System.getProperty("java.version"));
		printProviders();
		bLogInfo(0,"================= SYS INFO end=====================");
	}

	public void printProviders() {
		bLogError(0, "Providers List:");
		Provider[] providers = Security.getProviders();
		for (int i = 0; i < providers.length; i++) {
			bLogError(0, i + 1 + "." + providers[i].getName());
		}
	}

	/**
	 * 证书文件过滤器
	 * 
	 */
	public class CerFilter implements FilenameFilter {
		public boolean isCer(String name) {
			if (name.toLowerCase().endsWith(".cer")) {
				return true;
			} else {
				return false;
			}
		}

		public boolean accept(File dir, String name) {
			return isCer(name);
		}
	}

	

	public String getCertIdByKeyStoreMap(String certPath, String certPwd) {
		if (!certKeyStoreMap.containsKey(certPath)) {
			// 缓存中未查询到,则加载RSA证书
			loadRsaCert(certPath, certPwd);
		}
		return getCertIdIdByStore(certKeyStoreMap.get(certPath));
	}

	private String getCertIdIdByStore(KeyStore keyStore) {
		Enumeration<String> aliasenum = null;
		try {
			aliasenum = keyStore.aliases();
			String keyAlias = null;
			if (aliasenum.hasMoreElements()) {
				keyAlias = aliasenum.nextElement();
			}
			X509Certificate cert = (X509Certificate) keyStore.getCertificate(keyAlias);
			return cert.getSerialNumber().toString();
		} catch (KeyStoreException e) {
			bLogError(0, "getCertIdIdByStore Error:" + e.getMessage());
			return null;
		}
	}

	/**
	 * 获取证书容器
	 * 
	 * @return
	 */
	public Map<String, X509Certificate> getCertMap() {
		return certMap;
	}

	/**
	 * 设置证书容器
	 * 
	 * @param certMap
	 */
	public void setCertMap(Map<String, X509Certificate> certMap) {
		UnionPayCertUtil.certMap = certMap;
	}

	/**
	 * 使用模和指数生成RSA公钥 注意：此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同
	 * 
	 * @param modulus
	 *            模
	 * @param exponent
	 *            指数
	 * @return
	 */
	public PublicKey getPublicKey(String modulus, String exponent) {
		try {
			BigInteger b1 = new BigInteger(modulus);
			BigInteger b2 = new BigInteger(exponent);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);
			return keyFactory.generatePublic(keySpec);
		} catch (Exception e) {
			bLogError(0, "构造RSA公钥失败:" + e.getMessage());
			return null;
		}
	}

	/**
	 * 使用模和指数的方式获取公钥对象
	 * 
	 * @return
	 */
	public PublicKey getEncryptTrackCertPublicKey(String modulus, String exponent) {
		if (StringUtils.isEmpty(modulus) || StringUtils.isEmpty(exponent)) {
			bLogError(0, "[modulus] OR [exponent] invalid");
			return null;
		}
		return getPublicKey(modulus, exponent);
	}

}
