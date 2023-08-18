package com.unitechs.biz.common.utils;
/**
 * 基本功能：IPv4编码格式转换与数据校验
 * <p>
 * <pre>
 * 类    型：基础类
 * 数据库表： 
 * 编    者：sunqt
 * 完成日期： 2010-12-28
 * 修    改：
 * 修改日期：
 * 修改内容：
 * </pre>
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**  
 * 基本功能：IPv4编码格式转换与数ffff据  校验
 * <p>
 * <pre>
 * 类    型：基础类
 * 数据库表： 
 * 编    者：sunqt
 * 完成日期： 2010-12-28
 * 修    改：
 * 修改日期：
 * 修改内容：
 * </pre>
 */
public class IPv4ConverUtil {
	
	public void IPv4ConverUtil(){
		
	}
	
	IPFormat ipf = new IPFormat();
	
	/**
	 * 基本功能:校验单个IPv4地址
	 * 方法名称: validateSimpleIPv4
	 * <p>
	 * <pre>
	 * 类	型：公有函数
	 * 编	者：孙秋桐
	 * 完成日期：20101123
	 * 数据库表：
	 * 修	改：
	 * 修改日期：
	 * 修改内容：
	 * </pre>
	 * @param ipAddress		IP地址(可以不是全编码)
	 * @return String		0:非法地址,1:合法地址 
	 */
	public String validateSimpleIPv4(String ipAddress){
		String flag = "0";
		String endtempstr = ipAddress.substring(ipAddress.length()-1);
		if("#".equals(endtempstr) || ".".equals(endtempstr)){
		
			flag = "0";
			return flag;
		}
		
		String ipaddr = ipAddress.replace(".","#");
		String[] st = ipaddr.split("#");
		int len = st.length;
		if(len == 4){			
			for(int i=0;i<4;i++){
				
				String tem = st[i];
				int len1 = tem.length();
				if(len1>0 && len1<4){					
				   for(int j=0;j<len1;j++){					   
					   char str = tem.charAt(j);
					   if((str>='0' && str<='9')){
						   
						   flag = "1"; 						   
					   }else{
						   
						   flag = "0";
						   return flag;
					   }					   
				   }
				}else{
					
					flag = "0";
					return flag;
				}
				
				//校验地址在0-255之间
				int k = Integer.parseInt(tem);
				if (k>255 || k<0){
					flag = "0";
					return flag;
				}
				
				
			}			
		}		
		return flag;		
	}
	
	/**
	 * 基本功能:校验单个IPv4地址段
	 * 方法名称: validateIPv4
	 * <p>
	 * <pre>
	 * 类	型：公有函数
	 * 编	者：孙秋桐
	 * 完成日期：20101228
	 * 数据库表：
	 * 修	改：
	 * 修改日期：
	 * 修改内容：
	 * </pre>
	 * @param ipAddress		IP地址段(可以不是全编码)
	 * @return String		0:非法地址,1:合法地址 
	 */
	public String validateIPv4(String ipAddress){
		
		String flag = "0";
		
		int index = ipAddress.indexOf("-");
		if(index!=-1){
			
			String[] st = ipAddress.split("-");
			int len = st.length;
			if(len ==2){
				
				String beginIP = st[0];
				String endIP = st[1];
				String fla1 = validateSimpleIPv4(beginIP);
				String fla2 = validateSimpleIPv4(endIP);
				if("1".equals(fla1) && "1".equals(fla2)){
					
					//调用地址比较方法判断大小
					String tem = compareToIPv4(endIP,beginIP);
					if(!"-1".equals(tem)){
						return "1";
					}
				}
			}
		}
		
		return flag;
	}
	
	/**
	 * 基本功能:校验IPv4掩码
	 * 方法名称: validateMaskIPv4
	 * <p>
	 * <pre>
	 * 类	型：公有函数
	 * 编	者：孙秋桐
	 * 完成日期：20101228
	 * 数据库表：
	 * 修	改：
	 * 修改日期：
	 * 修改内容：
	 * </pre>
	 * @param ipAddress		掩码格式IP地址(可以不是全编码)
	 * @return String		0:非法地址,1:合法地址 
	 */
	public String validateMaskIPv4(String ipAddress){
		
		String flag = "0";
		
		int index = ipAddress.indexOf("/");
		if(index!=-1){
			
			String[] st = ipAddress.split("/");
			int len = st.length;
			if(len ==2){
				
				String ipaddr = st[0];
				String mask = st[1];
				String fla1 = validateSimpleIPv4(ipaddr);
				int k = Integer.parseInt(mask);
				if(k>0 && k<=32 && "1".equals(fla1)){
					
					flag = "1";
					return flag;
				}

			}
		}
		
		return flag;
	}
	
	//IP地址比较大小(思路：分为4组,从大往小开始比较，不相等，得出结果，相等，比较下一组)
	/**
	 * 基本功能:IP地址比较大小
	 * 方法名称: compareToIPv4
	 * <p>
	 * <pre>
	 * 类	型：公有函数
	 * 编	者：孙秋桐
	 * 完成日期：20101228
	 * 数据库表：
	 * 修	改：
	 * 修改日期：
	 * 修改内容：
	 * </pre>
	 * @param ipAddress		合法的单个IP地址(无需全编码)
	 * @return String		1:ipAddr1>ipAddr2  -1:ipAddr1<ipAddr2 0:ipAddr1=ipAddr2
	 * @throws Exception 
	 */
	public String compareToIPv4(String ipAddr1,String ipAddr2){
		
		String result = "0";
		String newipAddr1 = ipAddr1.replace(".", ":");
		String newipAddr2 = ipAddr2.replace(".", ":");
		String[] st1 = newipAddr1.split(":");
		String[] st2 = newipAddr2.split(":");
		for(int i=0;i<4;i++){
			
			String tem1 = st1[i];
			String tem2 = st2[i];
			int t1 = Integer.parseInt(tem1);
			int t2 = Integer.parseInt(tem2);
			if(t1 > t2){
				
				return "1";
			}else if(t1 < t2){
		
				return "-1";
			}			
		} 
		
		return result;
	}

	/**
	 * 基本功能:单个IP地址转换为全编码格式
	 * 方法名称: ipv4SimpleFormatToAllCode
	 * <p>
	 * <pre>
	 * 类	型：公有函数
	 * 编	者：孙秋桐
	 * 完成日期：20101228
	 * 数据库表：
	 * 修	改：
	 * 修改日期：
	 * 修改内容：
	 * </pre>
	 * @param ipAddress		单个合法IP地址
	 * @return String		该IP地址的全编码格式 
	 */
	public String ipv4SimpleFormatToAllCode(String ipAddress){
		
		String ipaddr ="";
		ipAddress = ipAddress.replace(".", ":");
		String[] st = ipAddress.split(":");
		for(int i=0;i<4;i++){
			
			String temp = st[i];
			int len = temp.length();
			if(len ==1){
				
				ipaddr += "00"+temp+".";				
			}else if(len ==2){
				
				ipaddr += "0"+temp+".";
			}else{
				
				ipaddr += temp+".";
			}
		}
		
		ipaddr = ipaddr.substring(0,ipaddr.length()-1);
		
		return ipaddr;
	}
	
	/**
	 * 基本功能:ip地址段转换为全编码格式地址段
	 * 方法名称: ipv4FormatToAllCode
	 * <p>
	 * <pre>
	 * 类	型：公有函数
	 * 编	者：孙秋桐
	 * 完成日期：20101228
	 * 数据库表：
	 * 修	改：
	 * 修改日期：
	 * 修改内容：
	 * </pre>
	 * @param ipAddress		合法IP地址段
	 * @return String		该IP地址的全编码格式 
	 */
	public String ipv4FormatToAllCode(String ipAddress){
		
		String ipAddr = "";
		String[] st = ipAddress.split("-");
		String beginip = st[0];
		String endip = st[1];
		String beginIP = ipv4SimpleFormatToAllCode(beginip);
		String endIP = ipv4SimpleFormatToAllCode(endip);
		
		ipAddr = beginIP + "-" + endIP;
		return ipAddr;
		
	}
	
	/**
	 * 基本功能:ip掩码转换为全编码格式地址段
	 * 方法名称: ipv4MaskFormatToAllCode
	 * <p>
	 * <pre>
	 * 类	型：公有函数
	 * 编	者：孙秋桐
	 * 完成日期：20101228
	 * 数据库表：
	 * 修	改：
	 * 修改日期：
	 * 修改内容：
	 * </pre>
	 * @param ipAddress		合法IP掩码
	 * @return String		该IP地址的全编码格式 
	 * @throws Exception 
	 */
	public String ipv4MaskFormatToAllCode(String ipAddress) throws Exception{
		
		String ipAddr = "";
		
		int index = ipAddress.indexOf("/");
		String ipaddr = ipAddress.substring(0,index);
		String mask = ipAddress.substring(index+1);		
		int mk = Integer.parseInt(mask);		
		Vector vc = ipf.getIpRange(ipaddr,mk);
		String beginIP = (String)vc.get(0);
		String endIP = (String)vc.get(1);
		
		ipAddr = beginIP + "-" + endIP;
		return ipAddr;
		
	}

	/**
	 * 基本功能:输入的IP地址格式化为全编码地址段，单个IP地址转换为地址段,前提条件:输入IP地址合法
	 * 方法名称: IPv4Format
	 * <p>
	 * <pre>
	 * 类	型：公有函数
	 * 编	者：孙秋桐
	 * 完成日期：20101228
	 * 数据库表：
	 * 修	改：
	 * 修改日期：
	 * 修改内容：
	 * </pre>
	 * @param ipAddress		合法IP掩码
	 * @return String		该IP地址的全编码格式 
	 * @throws Exception 
	 */
	public String ipv4Format(String ipAddress) throws Exception{
		
		String result = "";
		if(ipAddress.indexOf("-")!=-1){
			//IP地址段
			result = ipv4FormatToAllCode(ipAddress);			
		}else if(ipAddress.indexOf("/")!=-1){
			//掩码
			result = ipv4MaskFormatToAllCode(ipAddress);			
		}else{
			//单个地址
			String temip = ipv4SimpleFormatToAllCode(ipAddress);
			result = temip + "-" + temip;
		}
		
		return result;
	}
	
	/**
	 * 基本功能:单个IP地址全编码格式转换为显示格式
	 * 方法名称: ipv4SimpleUnFormat
	 * <p>
	 * <pre>
	 * 类	型：公有函数
	 * 编	者：孙秋桐
	 * 完成日期：20101228
	 * 数据库表：
	 * 修	改：
	 * 修改日期：
	 * 修改内容：
	 * </pre>
	 * @param ipAddress		合法IP地址全编码格式
	 * @return String		该IP地址的显示格式 
	 */
	public String ipv4SimpleUnFormat(String ipAddress){
		
		String result ="";
		ipAddress = ipAddress.replace(".", ":");
		String[] st = ipAddress.split(":");
		for(int i=0;i<4;i++){
			
			String tem = st[i];
			Integer temint = Integer.parseInt(tem);
			
			result += temint.toString() + ".";			
		}
		
		result = result.substring(0,result.length()-1);
		
		return result;
	}
	
	/**
	 * 基本功能:IP地址全编码格式地址段转换为显示格式
	 * 方法名称: ipv4cUnFormat
	 * <p>
	 * <pre>
	 * 类	型：公有函数
	 * 编	者：孙秋桐
	 * 完成日期：20101228
	 * 数据库表：
	 * 修	改：
	 * 修改日期：
	 * 修改内容：
	 * </pre>
	 * @param ipAddress		合法IP地址全编码格式地址段
	 * @return String		该IP地址段的显示格式 
	 */
	public String ipv4cUnFormat(String ipAddress){
		
		String result ="";
		String[] st = ipAddress.split("-");
		String beginIP = st[0];
		String endIP = st[1];
		String beginip = ipv4SimpleUnFormat(beginIP);
		String endip = ipv4SimpleUnFormat(endIP);
		
		result = beginip + "-" + endip;		
		return result;
	}
	
	//改进:首先判断是否可以转换为掩码格式，然后再转换
	/**
	 * 基本功能:IP地址全编码格式地址段转换为掩码格式
	 * 方法名称: ipv4MaskUnFormat
	 * <p>
	 * <pre>
	 * 类	型：公有函数
	 * 编	者：孙秋桐
	 * 完成日期：20101228
	 * 数据库表：
	 * 修	改：
	 * 修改日期：
	 * 修改内容：
	 * </pre>
	 * @param ipAddress		合法IP地址全编码格式地址段
	 * @return String		该IP地址段的掩码格式 
	 * @throws Exception 
	 */
	public String ipv4MaskUnFormat(String ipAddress) throws Exception{
		
		String result = "";
		String[] st = ipAddress.split("-");
		String beginIP = st[0];
		String endIP = st[1];
		result = ipf.format2IpMask(beginIP, endIP);
		return result;		
	}
	
	/**
	 * 基本功能:IP地址全编码格式转变为显示格式(单个地址全编码转换为地址段显示格式)
	 * 方法名称: ipv4UnFormat
	 * <p>
	 * <pre>
	 * 类	型：公有函数
	 * 编	者：孙秋桐
	 * 完成日期：20101228
	 * 数据库表：
	 * 修	改：
	 * 修改日期：
	 * 修改内容：
	 * </pre>
	 * @param ipAddress		合法IP地址全编码格式地址
	 * @return String		该IP地址段的显示格式 
	 * @throws Exception 
	 */
	public String ipv4UnFormat(String ipAddress) throws Exception{
		
		String result="";
		if(ipAddress.indexOf("-")!=-1){
			//地址段
			String[] st = ipAddress.split("-");
			String beginIP = st[0];
			String endIP = st[1];
			result = ipf.format2IpMask(beginIP, endIP);			
		}else{
			
			String temp = ipv4SimpleUnFormat(ipAddress);
			result = temp + "-" + temp;			
		}
		
		return result;
	}
	
	/**
	 * 基本功能:IPv4地址加法(不是全编码格式)
	 * 方法名称: sumIPv4(String ipAddress1,String ipAddress2)
	 * <p>
	 * <pre>
	 * 类	型：公有函数
	 * 编	者：孙秋桐
	 * 完成日期：20101228
	 * 数据库表：
	 * 修	改：
	 * 修改日期：
	 * 修改内容：
	 * </pre>
	 * @param ipAddress1	合法IP地址
	 * @param ipAddress2	合法IP地址
	 * @return String		ipAddress1+ipAddress2(不是全编码格式)，-1：和超出地址范围
	 */
	public String sumIPv4(String ipAddress1,String ipAddress2){
		
		String newip = "";
		int size = 4;
		String[] temip = new String[size];
		String ipaddr1 = ipAddress1.replace(".", ":");
		String ipaddr2 = ipAddress2.replace(".", ":");
		String[] st1 = ipaddr1.split(":");
		String[] st2 = ipaddr2.split(":");
		
		//满256进一
		//进位标识(1:进位,0:不进位)
		int flag = 0;
		for(int i=3;i>=0;i--){
			
			String tem1 = st1[i];
			String tem2 = st2[i];
			int temint1 = Integer.parseInt(tem1);
			int temint2 = Integer.parseInt(tem2);
			String temp = "";
			
			if(flag == 1){
			   //前一组有进位
			   int n = temint1 + temint2 + 1;
			   if(n>255){							
					n = n-256;
					Integer ite = new Integer(n);
					String newtem = ite.toString();
					temip[size-1]="."+newtem;
					   size--;
					   flag = 1;		
				}else{
					Integer ite = new Integer(n);
					String newtem = ite.toString();
					temip[size-1]="."+newtem;
					   size--;
					   flag = 0;
				}				
			}else{
				//前一组无进位 
				int n = temint1 + temint2;
				if(n>255){							
					n = n-256;
					Integer ite = new Integer(n);
					String newtem = ite.toString();
					temip[size-1]="."+newtem;
						size--;
						flag = 1;		
				}else{
					Integer ite = new Integer(n);
					String newtem = ite.toString();
					temip[size-1]="."+newtem;
						size--;
						flag = 0;
					}	 						 
				 }		
		}
		
		if(flag == 1){
			//还有进位，说明相加后的结果超出长度范围，错误
			return "-1";				
		}
		
		for(int k=0;k<4;k++){
			
			newip = newip + temip[k];			
		}

		newip = newip.substring(1);
		return newip;	
	}
	
	/**
	 * 基本功能:IPv4地址减法(不是全编码格式)
	 * 方法名称: subStractIPv4(String ipAddress1,String ipAddress2)
	 * <p>
	 * <pre>
	 * 类	型：公有函数
	 * 编	者：孙秋桐
	 * 完成日期：20101228
	 * 数据库表：
	 * 修	改：
	 * 修改日期：
	 * 修改内容：
	 * </pre>
	 * @param ipAddress1	合法IP地址
	 * @param ipAddress2	合法IP地址
	 * @return String		ipAddress1-ipAddress2(不是全编码格式)，-1：和超出地址范围
	 */
	public String subStractIPv4(String ipAddress1,String ipAddress2){
		
		String newip = "";
		int size = 4;
		String[] temip = new String[size];
		String ipaddr1 = ipAddress1.replace(".", ":");
		String ipaddr2 = ipAddress2.replace(".", ":");
		String[] st1 = ipaddr1.split(":");
		String[] st2 = ipaddr2.split(":");
		
		//借位标识(1:借位,0:不进位)
		int flag = 0;
		for(int i=3;i>=0;i--){
			
			String tem1 = st1[i];
			String tem2 = st2[i];
			int temint1 = Integer.parseInt(tem1);
			int temint2 = Integer.parseInt(tem2);
			String temp = "";
			
			if(flag == 1){
			    //前一组有借位
				if(temint1-1 >= temint2){
				   //本组无借位
				   int n = temint1-1-temint2; 
				   Integer ite = new Integer(n);
				   String newtem = ite.toString();
				   temip[size-1]="."+newtem;
				   size--;
				   flag = 0;							
				}else{
				   //本组有借位	
				   int n = temint1-1+256-temint2;
				   Integer ite = new Integer(n);
				   String newtem = ite.toString();
				   temip[size-1]="."+newtem;
				   size--;
				   flag = 1;
				}				
			}else{
				//前一组无借位 
				if(temint1 >= temint2){
				   //本组无借位
				   int n = temint1-temint2; 
				   Integer ite = new Integer(n);
				   String newtem = ite.toString();
				   temip[size-1]="."+newtem;
				   size--;
				   flag = 0;							
				}else{
					//本组有借位	
					int n = temint1+256-temint2;
					Integer ite = new Integer(n);
					String newtem = ite.toString();
					temip[size-1]="."+newtem;
					size--;
					flag = 1;
				}		 
			}		
		}
		
		if(flag == 1){
			//还有借位，说明相减后的结果为负数，错误
			return "-1";				
		}
		
		for(int k=0;k<4;k++){
			
			newip = newip + temip[k];			
		}

		newip = newip.substring(1);
		return newip;	
	}
	
	/**
	 * 基本功能:获取该地址的前一地址(不是全编码格式)
	 * 方法名称: preIPv4(String ipAddress)
	 * <p>
	 * <pre>
	 * 类	型：公有函数
	 * 编	者：孙秋桐
	 * 完成日期：20101228
	 * 数据库表：
	 * 修	改：
	 * 修改日期：
	 * 修改内容：
	 * </pre>
	 * @param ipAddress	合法IP地址
	 * @return String	ipAddress1-1(不是全编码格式)
	 */
	public String preIPv4(String ipAddress){
		
		String result = "";
		String ipstr = "0.0.0.1";
		result = subStractIPv4(ipAddress,ipstr);
		return result;		
	}
	
	/**
	 * 基本功能:获取该地址的前一地址(全编码格式)
	 * 方法名称: preIPv4ToAllCode(String ipAddress)
	 * <p>
	 * <pre>
	 * 类	型：公有函数
	 * 编	者：孙秋桐
	 * 完成日期：20101228
	 * 数据库表：
	 * 修	改：
	 * 修改日期：
	 * 修改内容：
	 * </pre>
	 * @param ipAddress	合法IP地址
	 * @return String	ipAddress1-1(全编码格式)
	 */
	public String preIPv4ToAllCode(String ipAddress){
		
		String result = "";
		String ipstr = "0.0.0.1";
		String temp = subStractIPv4(ipAddress,ipstr);
		result = ipv4SimpleFormatToAllCode(temp);
		return result;		
	}
	
	/**
	 * 基本功能:获取该地址的后一地址(不是全编码格式)
	 * 方法名称: laterIPv4(String ipAddress)
	 * <p>
	 * <pre>
	 * 类	型：公有函数
	 * 编	者：孙秋桐
	 * 完成日期：20101228
	 * 数据库表：
	 * 修	改：
	 * 修改日期：
	 * 修改内容：
	 * </pre>
	 * @param ipAddress	合法IP地址
	 * @return String	ipAddress1+1(不是全编码格式)
	 */
	public String laterIPv4(String ipAddress){
		
		String result = "";
		String ipstr = "0.0.0.1";
		result = sumIPv4(ipAddress,ipstr);
		return result;		
	}
	
	/**
	 * 基本功能:获取该地址的后一地址(全编码格式)
	 * 方法名称: laterIPv4ToAllCode(String ipAddress)
	 * <p>
	 * <pre>
	 * 类	型：公有函数
	 * 编	者：孙秋桐
	 * 完成日期：20101228
	 * 数据库表：
	 * 修	改：
	 * 修改日期：
	 * 修改内容：
	 * </pre>
	 * @param ipAddress	合法IP地址
	 * @return String	ipAddress1+1(全编码格式)
	 */
	public String laterIPv4ToAllCode(String ipAddress){
		
		String result = "";
		String ipstr = "0.0.0.1";
		String temp = sumIPv4(ipAddress,ipstr);
		result = ipv4SimpleFormatToAllCode(temp);
		return result;		
	}
	
	/**
	 * 基本功能:计算地址数量
	 * 方法名称: calculateIPSum(String startip, String endip) throws Exception
	 * <p>
	 * <pre>
	 * 类	型：公有函数
	 * 编	者：lidan
	 * 完成日期：2012-10-25
	 * 数据库表：
	 * 修	改：
	 * 修改日期：
	 * 修改内容：
	 * </pre>
	 * @param String startip 起始地址
	 * @param String endip 终止地址
	 * @return long 地址数量
	 */
	public long calculateIPSum(String startip, String endip) throws Exception
	{
		long startl = IPFormat.toLongValue(startip);
		long endl = IPFormat.toLongValue(endip);
		
		long sum = endl - startl + 1;
		
		return sum;
	}
	
	/**
	 * 基本功能: 计算终止地址
	 * 方法名称: sumIPv4(String startip, int ipnum)
	 * <p>
	 * <pre>
	 * 类	型：公有函数
	 * 编	者：lidan
	 * 完成日期：2012-11-13
	 * 数据库表：
	 * 修	改：
	 * 修改日期：
	 * 修改内容：
	 * </pre>
	 * @param String startip 起始地址
	 * @param int ipnum	地址数
	 * @return String 结果地址
	 */
	public String sumIPv4ToAllCode(String startip, int ipnum) throws Exception
	{
		long startnum = IPFormat.toLongValue(startip);
		
		long endnum = startnum + ipnum - 1;
		
		String endip = IPFormat.toFullIpString(endnum);
		
		return endip;
	}
	
	/**
	 * 基本功能:将地址拆分为用掩码表示
	 * 方法名称: splitIPToMaskFormat(String startip,String endip)
	 * <p>
	 * <pre>
	 * 类	型：公有函数
	 * 编	者：lidan
	 * 完成日期：2011-09-15
	 * 数据库表：

	 * 修	改：
	 * 修改日期：

	 * 修改内容：

	 * </pre>
	 * @param String startip 起始地址
	 * @param String endip 终止地址
	 * @return List
	 */
	public List splitIPToMaskFormat(String startip,String endip) throws Exception
	{
		List list = new ArrayList();
		
		int startipi = IPFormat.toIntValue(startip);
		int endipi = IPFormat.toIntValue(endip);
		
		List splitlist = splitIp(startipi,endipi);
		
		String tmpstartip = "",tmpendip = "";
		
		for( int i = 0; i < splitlist.size(); i++ )
		{
			int ipInt = (Integer)splitlist.get(i);
			
			if( i == 0 )
			{
				tmpstartip = IPFormat.toSimpleIpString(ipInt);
			}
			else
			{
				tmpendip = IPFormat.toSimpleIpString(ipInt-1);
				
				String[] iparr = {tmpstartip,tmpendip};
				
				list.add(iparr);
				
				tmpstartip = IPFormat.toSimpleIpString(ipInt);
			}
		}
		
		String[] arr = {tmpstartip,this.ipv4SimpleUnFormat(endip)};
		list.add(arr);
		
		return list;
	}
	
	//拆分地址
	public List splitIp(int start,int end)
	{
		List list=new ArrayList();
		
		int minus=end-start+1;
		
		if(minus<=0)
		{
			return list;
		}
		
		int x=less(minus);
		while(x>0)
		{
			if((start&(x-1))==0)
			{
				list.add(start);
				List next=splitIp(start+x,end);
				for(int i=0;i<next.size();i++)
				{
					list.add(next.get(i));
				}
				return list;
			}			
			x=x>>1;
		}
		
		return list;
	}
	
	//计算整数包含的最大的2进制数  
	private int less(int x)
	{
		int num=1<<30;
		
		while(num>0)
		{
			if(x>=num)
			{
				return num;
			}
			num=num>>1;
		}		
		return 0;
	}
	
	//将一段IP地址，规整为可掩的IP地址段
	public String regularIpFormat(String startip, String endip) throws Exception{
		String inetnum = "";
		try{
			//计算该地址段的地址数量ipcount
			int ipcount = 0;
			int startipnum = IPFormat.toIntValue(startip);
			int endipnum = IPFormat.toIntValue(endip);
			ipcount = endipnum - startipnum + 1;
			//依据ipcount来计算掩码位数mask（掩码为0-32的整数）
			int maskbit = this.getMaskBit(ipcount);
			//规整地址为掩码格式inetnum
			//依据startip和maskbig, 计算出IP地址段
			Vector iprange = IPFormat.getIpRange(startip, maskbit);
			//取出起始地址
			String startfullip = (String)iprange.get(0);
			inetnum = this.ipv4SimpleUnFormat(startfullip)+"/"+maskbit;
		}catch(Exception e){
			e.printStackTrace();
		}
		return inetnum;
	}
	
	/**
	 * 基本功能：依据地址数量算掩码位数  
	 * 方法名称：getMaskBit(int ipcount)   
	 * @param  int ipcount 地址数量   
	 * @return  int 掩码位数  
	 */
	public int getMaskBit(int ipcount) throws Exception
	{
        int maskIntValue;
        int returnValue=0;

        try
        {
        	//计算大于等于地址数量ipcount的最小二进制数
            maskIntValue = more(ipcount);
            //从左向右找到第一个0  
            int testValue = 0x80000000;
            while(returnValue<32)
            {
            	returnValue ++ ;
                if ((maskIntValue & testValue) > 0) break;
                testValue >>>= 1;//“>>>”运算符所作的是无符号的位移处理，它不会将所处理的值的最高位视为正负符号, testValue = testValue >>> 1
            }

            //掩码应大于0  
            if (returnValue==0) throw new Exception("");

            //若掩码为32位，则直接返回   
            if (returnValue==32) return 32;

            //否则检查右面是否都为0  
            if ((maskIntValue>>(32-returnValue)) == 1)
                return returnValue ;
            else
                throw new Exception("");

        }
        //若发生异常，则为异常掩码
        catch(Exception e)
        {
            throw new Exception("invalid ipcount :"+ipcount+"->"+e.toString());
        }
    }
	
	//计算大于x的最小的2进制数   
	private int more(int x)
	{
		int num=1<<30;
		int num1=num>>1;
		
		if( x == 1 )
			return x;
		
		while(num>0&&num1>0)
		{
			if( x == num1 )
			{
				return num1;
			}
			else if(x>=num1 && x<=num)
			{
				return num;
			}
			num=num>>1;
			num1=num>>1;
		}		
		return 0;
	}
	
}
