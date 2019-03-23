package com.lyon.command;

import android.util.Log;

/**
 * @author lyon                             <br>
 * @version V0.0.1                          <br>
 * Description:                             <br>
 * Date 9/5/2018 8:59 PM                    <br>
 */
public class Receiver {
    public static final String TAG = Receiver.class.getSimpleName();
    /**
     * QQ登录方法
     */
    public void tencentLogin() {
        System.out.print("QQ登录");
    }

    /**
     * 微信登录
     */
    public void weChatLogin() {
        System.out.print("weChat登录");
    }

    /**
     * 新浪登录
     */
    public void sinaLogin() {
        System.out.print("sina登录");
    }
	
	if(isReponsePacket){
		if(CmdUtils.CMD_UP_IMAGE_CODE.equals(rcm)){
			...
	        imgWidth = CmdUtils.byteToInt(dataWidth);// imgWidth:121
	        imgHight = CmdUtils.byteToInt(dataHeight);// imgHight:133
	
	        int byteCount = imgWidth*imgHight;
	        int test1 = byteCount/496;
	        int test2 = byteCount%496;
	        len = 12*test1+2*test1+12+2+byteCount;// len:16555
							
	        for(int i=26;i<ret;i++){              // ret:4095
				bImageAll[bImageIndex] = buffer[i];
		        bImageIndex++;
		    }
	} else {
		if(bImageAll!=null){
			for(int i=0;i<ret;i++){
				bImageAll[bImageIndex] = buffer[i];
				bImageIndex++;
			}
		}
	}
	
	for循环是怎么执行的，现在只走了第一个for循环，然后bImageIndex 的值才是4069
	导致下面的if((bImageIndex == len) && (bImageIndex > 26) && imgWidth>10 && imgHight>10)条件过不去
	
		
}
