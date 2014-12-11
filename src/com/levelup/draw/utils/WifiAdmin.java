

package com.levelup.draw.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
/**
 * WIFI������
 * @author ZHF
 *
 */
public class WifiAdmin {
	private static  WifiAdmin wifiAdmin = null;
	
	private List<WifiConfiguration> mWifiConfiguration; //��������������Ϣ�༯��(���������б�)  
	private List<ScanResult> mWifiList; //��⵽�������Ϣ�� ����
	
	//�����κ�Wifi����״̬
	private WifiInfo mWifiInfo; 
	
	WifiManager.WifiLock mWifilock; //�ܹ���ֹwifi����˯��״̬��ʹwifiһֱ���ڻ�Ծ״̬
	public WifiManager mWifiManager;
	
	/**
	 * ��ȡ�����ʵ��������ģʽ��
	 * @param context
	 * @return
	 */
	public static WifiAdmin getInstance(Context context) {
		if(wifiAdmin == null) {
			wifiAdmin = new WifiAdmin(context);
			//return wifiAdmin;
		}
		return wifiAdmin;
	}
	private WifiAdmin(Context context) {
		//��ȡϵͳWifi����   WIFI_SERVICE
		this.mWifiManager = (WifiManager) context.getSystemService("wifi");
		//��ȡ������Ϣ
		this.mWifiInfo = this.mWifiManager.getConnectionInfo(); 
	}
	
	/**
	 * �Ƿ����������Ϣ
	 * @param str  �ȵ�����
	 * @return
	 */
	private WifiConfiguration isExsits(String str) {
		if(this.mWifiManager.getConfiguredNetworks()==null){
			return null;
		}
		Iterator localIterator = this.mWifiManager.getConfiguredNetworks().iterator();
		WifiConfiguration localWifiConfiguration;
		do {
			if(!localIterator.hasNext()) return null;
			localWifiConfiguration = (WifiConfiguration) localIterator.next();
		}while(!localWifiConfiguration.SSID.equals("\"" + str + "\""));
		return localWifiConfiguration;
	}
	
	/**����WifiLock�������ش��ļ�ʱ��Ҫ���� **/
	public void AcquireWifiLock() {
		this.mWifilock.acquire();
	}
	/**����һ��WifiLock**/
	public void CreateWifiLock() {
		this.mWifilock = this.mWifiManager.createWifiLock("Test");
	}
	/**����WifiLock**/
	public void ReleaseWifilock() {
		if(mWifilock.isHeld()) { //�ж�ʱ������  
			mWifilock.acquire();
		}
	}
	
	
	/**��Wifi**/
	public void OpenWifi() {
		if(!this.mWifiManager.isWifiEnabled()){ //��ǰwifi������
			this.mWifiManager.setWifiEnabled(true);
		}
	}
	/**wifi�����Ƿ����*/
	public boolean isWifiOpen(){
		return this.mWifiManager.isWifiEnabled();
	}
	/**�ر�Wifi**/
	public void closeWifi() {
		if(mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(false);
		}
	}
	/**�˿�ָ��id��wifi**/ 
	public void disconnectWifi(int paramInt) {
		this.mWifiManager.disableNetwork(paramInt);
	}//file:///F:/Program%20Files/WandouLabs/Applications/2.79.0.6872/templates/app/i18n/zh-cn/images/shadow.png
	
	/**����ָ������**/
	public void addNetwork(WifiConfiguration paramWifiConfiguration) {
		int i = mWifiManager.addNetwork(paramWifiConfiguration);
		System.out.println("id:"+i);
		//�޸Ĺ�
		mWifiManager.enableNetwork(i, true);
	}
	
	/**
	 * ����ָ�����úõ�����
	 * @param index ���ú������ID
	 */
    public void connectConfiguration(int index) {   
        // �����������úõ�������������    
        if (index > mWifiConfiguration.size()) {   
            return;   
        }   
        //�������úõ�ָ��ID������    
        mWifiManager.enableNetwork(mWifiConfiguration.get(index).networkId, true);   
    }
	
    /**
     * ����wifi��Ϣ������ر�һ���ȵ� 
     * @param paramWifiConfiguration
     * @param paramBoolean �رձ�־
     */
    public void createWifiAP(WifiConfiguration paramWifiConfiguration,boolean paramBoolean) {
    	try {
			Class localClass = this.mWifiManager.getClass();
			Class[] arrayOfClass = new Class[2];
			arrayOfClass[0] = WifiConfiguration.class;
			arrayOfClass[1] = Boolean.TYPE;
			Method localMethod = localClass.getMethod("setWifiApEnabled",arrayOfClass);
			WifiManager localWifiManager = this.mWifiManager;
			Object[] arrayOfObject = new Object[2];
			arrayOfObject[0] = paramWifiConfiguration;
			arrayOfObject[1] = Boolean.valueOf(paramBoolean);
			localMethod.invoke(localWifiManager, arrayOfObject);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    /** 
     * ����һ��wifi��Ϣ 
     * @param ssid ���� 
     * @param passawrd ���� 
     * @param paramInt ��3��������1�������룬2�Ǽ����룬3��wap���� 
     * @param type ��"ap"����"wifi" 
     * @return 
     */  
    public WifiConfiguration createWifiInfo(String ssid, String passawrd,int paramInt, String type) {
    	//����������Ϣ��
    	WifiConfiguration localWifiConfiguration1 = new WifiConfiguration();
    	//����������������
    	localWifiConfiguration1.allowedAuthAlgorithms.clear();
    	localWifiConfiguration1.allowedGroupCiphers.clear();
    	localWifiConfiguration1.allowedKeyManagement.clear();
    	localWifiConfiguration1.allowedPairwiseCiphers.clear();
    	localWifiConfiguration1.allowedProtocols.clear();
    	
    	if(type.equals("wt")) { //wifi����
    		localWifiConfiguration1.SSID = ("\"" + ssid + "\"");
    		WifiConfiguration localWifiConfiguration2 = isExsits(ssid);
    		if(localWifiConfiguration2 != null) {
    			mWifiManager.removeNetwork(localWifiConfiguration2.networkId); //���б���ɾ��ָ����������������
    		}
    		if(paramInt == 1) { //û������
    			localWifiConfiguration1.wepKeys[0] = "";
    			localWifiConfiguration1.allowedKeyManagement.set(0);
    			localWifiConfiguration1.wepTxKeyIndex = 0;
    		} else if(paramInt == 2) { //������
    			localWifiConfiguration1.hiddenSSID = true;
    			localWifiConfiguration1.wepKeys[0] = ("\"" + passawrd + "\"");
    		} else { //wap���� 
    			localWifiConfiguration1.preSharedKey = ("\"" + passawrd + "\"");
				localWifiConfiguration1.hiddenSSID = true;
				localWifiConfiguration1.allowedAuthAlgorithms.set(0);
				localWifiConfiguration1.allowedGroupCiphers.set(2);
				localWifiConfiguration1.allowedKeyManagement.set(1);
				localWifiConfiguration1.allowedPairwiseCiphers.set(1);
				localWifiConfiguration1.allowedGroupCiphers.set(3);
				localWifiConfiguration1.allowedPairwiseCiphers.set(2);
    		}
    	}else {//"ap" wifi�ȵ�
    		localWifiConfiguration1.SSID = ssid;
			localWifiConfiguration1.allowedAuthAlgorithms.set(1);
			localWifiConfiguration1.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			localWifiConfiguration1.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			localWifiConfiguration1.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
			localWifiConfiguration1.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
			localWifiConfiguration1.allowedKeyManagement.set(0);
			localWifiConfiguration1.wepTxKeyIndex = 0;
			if (paramInt == 1) {  //û������
				localWifiConfiguration1.wepKeys[0] = "";
				localWifiConfiguration1.allowedKeyManagement.set(0);
				localWifiConfiguration1.wepTxKeyIndex = 0;
			} else if (paramInt == 2) { //������
				localWifiConfiguration1.hiddenSSID = true;//�����ϲ��㲥ssid
				localWifiConfiguration1.wepKeys[0] = passawrd;
			} else if (paramInt == 3) {//wap����
				localWifiConfiguration1.preSharedKey = passawrd;
				localWifiConfiguration1.allowedAuthAlgorithms.set(0);
				localWifiConfiguration1.allowedProtocols.set(1);
				localWifiConfiguration1.allowedProtocols.set(0);
				localWifiConfiguration1.allowedKeyManagement.set(1);
				localWifiConfiguration1.allowedPairwiseCiphers.set(2);
				localWifiConfiguration1.allowedPairwiseCiphers.set(1);
			}
    	}
		return localWifiConfiguration1;
    }
    
    /**��ȡ�ȵ���**/
    public String getApSSID() {
		try {
			Method localMethod = this.mWifiManager.getClass().getDeclaredMethod("getWifiApConfiguration", new Class[0]);
			if (localMethod == null) return null;
			Object localObject1 = localMethod.invoke(this.mWifiManager,new Object[0]);
			if (localObject1 == null) return null;
			WifiConfiguration localWifiConfiguration = (WifiConfiguration) localObject1;
			if (localWifiConfiguration.SSID != null) return localWifiConfiguration.SSID;
			Field localField1 = WifiConfiguration.class	.getDeclaredField("mWifiApProfile");
			if (localField1 == null) return null;
			localField1.setAccessible(true);
			Object localObject2 = localField1.get(localWifiConfiguration);
			localField1.setAccessible(false);
			if (localObject2 == null)  return null;
			Field localField2 = localObject2.getClass().getDeclaredField("SSID");
			localField2.setAccessible(true);
			Object localObject3 = localField2.get(localObject2);
			if (localObject3 == null) return null;
			localField2.setAccessible(false);
			String str = (String) localObject3;
			return str;
		} catch (Exception localException) {
		}
		return null;
	}
    
    /**��ȡwifi��**/ 
    public String getBSSID() {  
        if (this.getWifiInfo() == null)  
            return "NULL";  
        return this.getWifiInfo().getBSSID();  
    }  
  
   /**�õ����úõ����� **/
    public List<WifiConfiguration> getConfiguration() {  
        return this.mWifiConfiguration;  
    }  
    
    /**����ȵ��ip��ַ**/
    public String getHOstAddress(){
    	return intToString(mWifiManager.getDhcpInfo().serverAddress);
    }
    
    /**��ȡip��ַ**/  
    public String getIPAddress() {  
    	int ip = (getWifiInfo() == null) ? 0 : getWifiInfo().getIpAddress();  
    	return intToString(ip);
    }  
    /**��ȡ������ַ(Mac)**/ 
    public String getMacAddress() {  
    	 return (getWifiInfo() == null) ? "NULL" : getWifiInfo().getMacAddress();  
    }     
      
    /**��ȡ����id**/ 
    public int getNetworkId() {  
    	 return (getWifiInfo() == null) ? 0 : getWifiInfo().getNetworkId();  
    }  
    /**��ȡ�ȵ㴴��״̬**/ 
    public int getWifiApState() {  
        try {  
            int i = ((Integer) this.mWifiManager.getClass()  
                    .getMethod("getWifiApState", new Class[0])  
                    .invoke(this.mWifiManager, new Object[0])).intValue();  
            return i;  
        } catch (Exception localException) {  
        }  
        return 4;   //δ֪wifi����״̬
    }  
    /**��ȡwifi������Ϣ**/
    public WifiInfo getWifiInfo() {  
        return this.mWifiManager.getConnectionInfo();  
    }  
    /** �õ������б�**/
    public List<ScanResult> getWifiList() {  
        return this.mWifiList;  
    }  
  
    /**�鿴ɨ����**/
    public StringBuilder lookUpScan() {  
        StringBuilder localStringBuilder = new StringBuilder();  
        for (int i = 0; i < mWifiList.size(); i++)  
        {  
        	localStringBuilder.append("Index_"+new Integer(i + 1).toString() + ":");  
            //��ScanResult��Ϣת����һ���ַ�����  
            //���аѰ�����BSSID��SSID��capabilities��frequency��level  
        	localStringBuilder.append((mWifiList.get(i)).toString());  
        	localStringBuilder.append("\n");  
        }  
        return localStringBuilder;  
    }  
      
    /** ����wifi������� **/
    public void setWifiList() {  
        this.mWifiList = this.mWifiManager.getScanResults();  
    }  
    /**��ʼ����wifi**/
    public void startScan() {  
        this.mWifiManager.startScan();  
    }  
    /**�õ�������BSSID**/
    public String GetBSSID() {  
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();  
    }  
    //��int��ipת���ֽ�����
    public static String intToString(int a){
    	StringBuffer sb=new StringBuffer();
    	int b=(a>>0)&0xff;
    	sb.append(b+".");
    	b=(a>>8)&0xff;
    	sb.append(b+".");
    	b=(a>>16)&0xff;
    	sb.append(b+".");
    	b=(a>>24)&0xff;
    	sb.append(b);
		return sb.toString();
    }
}  