/* 修改记录: 日期,修改人,修改方法名,任务号/bug号,本次修改功能描述*/
//20100226,刘文博,toLongValue,ID2325,广东地址池校验及华为地址池配置,2010020402－广东地址池校验及华为地址池配置－杜娟-2325.doc
package com.unitechs.biz.common.utils;
import java.util.Vector;

public class IPFormat {

    public IPFormat() {
    }

    /**
     *  * 基本功能：计算起始IP和  终止IP  之间的IP个数
        * <p>
        * <pre>
        * 类    型：公共函数
        * 修    改：张天佑
        * 修改日期：
        * 修改内容：
        * </pre>
     * @param startip 起始IP
     * @param endip 终止IP
     * @return
     * @throws Exception
     */
    public static int getIpSize(String startip,String endip)throws Exception{
    	return (toIntValue(toFullIpString(endip))-toIntValue(toFullIpString(startip)))+1;
    }
    /**
        * 基本功能：检查IP地址合法性
        * <p>
        * <pre>
        * 类    型：公共函数
        * 修    改：
        * 修改日期：
        * 修改内容：
        * </pre>
        * @param ipAddr IP地址
        * @return 是否合法
    */
    public static boolean checkIpValid(String ipAddr)
    {
        try
        {
            int begin=0;
            int end,segValue;

            //分析地址前三段
            for(int i=0;i<3;i++)
            {
                //找不到“."分隔符，为非法IP
                end = ipAddr.indexOf('.',begin);
                if (end==-1) return false;

                //数据不在0－255之间，为非法IP
                segValue=Integer.parseInt(ipAddr.substring(begin,end));
                if ((segValue<0) || (segValue>255)) return false;

                //下一段的起始点
                begin = end+1;

            }

            //地址最后一段数据不在0－255之间，为非法IP
            segValue=Integer.parseInt(ipAddr.substring(begin));
            if ((segValue<0) || (segValue>255)) return false;

            //返回成功
            return true;

        }
        //若发生异常，则为非法IP
        catch(Exception e)
        {
            return false;
        }
    }

    /**
        * 基本功能：从掩码获取掩码位数
        * <p>
        * <pre>
        * 类    型：公共函数
        * 修    改：
        * 修改日期：
        * 修改内容：
        * </pre>
        * @param mask 掩码
        * @throws Exception
        * @return 掩码位数（1－32）
    */
    public static int getMaskBitNumber(String mask) throws Exception
    {
        int maskIntValue;
        int returnValue=0;

        try
        {
            maskIntValue = toIntValue(mask);
            //从左向右找到第一个0
            int testValue = 0x80000000;
            while(returnValue<32)
            {
                if ((maskIntValue & testValue) == 0) break;
                returnValue ++ ;
                testValue >>>= 1;
            }

            //掩码应大于0
            if (returnValue==0) throw new Exception("");

            //若掩码为32位，则直接返回
            if (returnValue==32) return 32;

            //否则检查右面是否都为0
            if ((maskIntValue<<returnValue) == 0)
                return returnValue ;
            else
                throw new Exception("");

        }
        //若发生异常，则为异常掩码
        catch(Exception e)
        {
            throw new Exception("invalid mask :"+mask+"->"+e.toString());
        }
    }

    /**
        * 基本功能：从掩码位数获取掩码
        * <p>
        * <pre>
        * 类    型：公共函数
        * 修    改：
        * 修改日期：
        * 修改内容：
        * </pre>
        * @param bitNumber 掩码位数(0-32)
        * @throws Exception
        * @return 掩码
    */
    public static String getMaskString(int bitNumber) throws Exception
    {
        return toSimpleIpString(getMaskIntValue(bitNumber)) ;
    }

    /**
        * 基本功能：从IP地址及掩码位数得到IP地址范围(fullIpString)
        * <p>
        * <pre>
        * 类    型：公共函数
        * 修    改：
        * 修改日期：
        * 修改内容：
        * </pre>
        * @param ipAddr 网络地址
        * @param maskBitNumber 掩码位数(0-32)
        * @throws Exception
        * @return Vector(0) min IP string;Vector(1) max IP string
    */
    public static Vector getIpRange(String ipAddr,int maskBitNumber) throws Exception
    {
        int mask=getMaskIntValue(maskBitNumber);
        int minip=toIntValue(ipAddr)&mask;
        int maxip=minip|(~mask);
        String minIpFullStr=toFullIpString(minip);
        String maxIpFullStr=toFullIpString(maxip);
        Vector vt=new Vector();
        vt.add(minIpFullStr);
        vt.add(maxIpFullStr);
        return vt;
    }

    /**
        * 基本功能：从IP地址及掩码得到IP地址范围(fullIpString)
        * <p>
        * <pre>
        * 类    型：公共函数
        * 修    改：
        * 修改日期：
        * 修改内容：
        * </pre>
        * @param ipAddr 网络地址
        * @param mask 掩码
        * @throws Exception
        * @return Vector(0) min IP string;Vector(1) max IP string
    */
    public static Vector getIpRange(String ipAddr,String mask) throws Exception
    {
        return(getIpRange(ipAddr,getMaskBitNumber(mask))) ;
    }

    /**
        * 基本功能：将全IP地址串（010.000.000.001)转换为简单IP地址串(10.0.0.1)
        * <p>
        * <pre>
        * 类    型：公共函数
        * 修    改：
        * 修改日期：
        * 修改内容：
        * </pre>
        * @param fullIpString 全IP地址(010.000.000.001)
        * @throws Exception
        * @return 简单IP地址(10.0.0.1)
    */
    public static String toSimpleIpString(String fullIpString) throws Exception
    {
        return toSimpleIpString(toIntValue(fullIpString));
    }

    /**
        * 基本功能：将简单IP地址串(10.0..0.1)转换为全IP地址串（010.000.000.001)
        * <p>
        * <pre>
        * 类    型：公共函数
        * 修    改：
        * 修改日期：
        * 修改内容：
        * </pre>
        * @param simpleIpString 简单IP地址(10.0.0.1)
        * @throws Exception
        * @return 全IP地址(010.000.000.001)
    */
    public static String toFullIpString(String simpleIpString) throws Exception
    {
        return toFullIpString(toIntValue(simpleIpString));
    }

    /**
        * 基本功能：将int类型IP地址串转换为简单IP地址串(10.0.0.1)
        * <p>
        * <pre>
        * 类    型：私有函数
        * 修    改：
        * 修改日期：
        * 修改内容：
        * </pre>
        * @param intIpString int类型IP地址
        * @return 简单IP地址(10.0.0.1)
    */
    public static String toSimpleIpString(int intIpString)
    {
        return Integer.toString((intIpString&0xff000000)>>>24) + "."
            + Integer.toString((intIpString&0x00ff0000)>>>16) + "."
            + Integer.toString((intIpString&0x0000ff00)>>>8) + "."
            + Integer.toString(intIpString&0x000000ff) ;
    }
    
    public static String toSimpleIpString(long intIpString)
    {
        return Long.toString((intIpString&0xff000000)>>>24) + "."
            + Long.toString((intIpString&0x00ff0000)>>>16) + "."
            + Long.toString((intIpString&0x0000ff00)>>>8) + "."
            + Long.toString(intIpString&0x000000ff) ;
    }

    /**
        * 基本功能：将int类型IP地址串转换为全IP地址串（010.000.000.001)
        * <p>
        * <pre>
        * 类    型：私有函数
        * 修    改：
        * 修改日期：
        * 修改内容：
        * </pre>
        * @param intIpString int类型IP地址
        * @return 全IP地址(010.000.000.001)
    */
    public static String toFullIpString(int intIpString)
    {
        java.text.DecimalFormat df = new java.text.DecimalFormat("000");
        return df.format((intIpString&0xff000000)>>>24) + "."
            + df.format((intIpString&0x00ff0000)>>>16) + "."
            + df.format((intIpString&0x0000ff00)>>>8) + "."
            + df.format(intIpString&0x000000ff) ;
    }
    
    /**
     * 基本功能：将int类型IP地址串转换为全IP地址串（010.000.000.001)
     * <p>
     * <pre>
     * 类    型：私有函数
     * 修    改：
     * 修改日期：
     * 修改内容：
     * </pre>
     * @param intIpString int类型IP地址
     * @return 全IP地址(010.000.000.001)
     */
    public static String toFullIpString(long longIpString)
    {
    	java.text.DecimalFormat df = new java.text.DecimalFormat("000");
    	return df.format(longIpString>>>24) + "."
         	+ df.format((longIpString&0x00FFFFFF)>>>16) + "."
         	+ df.format((longIpString&0x0000FFFF)>>>8) + "."
         	+ df.format(longIpString&0x000000FF) ;
	}

    /**
        * 基本功能：从掩码位数获取掩码数值
        * <p>
        * <pre>
        * 类    型：私有函数
        * 修    改：
        * 修改日期：
        * 修改内容：
        * </pre>
        * @param bitNumber 掩码位数(0-32)
        * @throws Exception
        * @return int类型掩码
    */
    private static int getMaskIntValue(int bitNumber) throws Exception
    {
        int maskIntValue = 0x80000000;
        if ((bitNumber<=0) || (bitNumber>32)) throw new Exception("invalid Mask bit Number:"+bitNumber);
        maskIntValue >>= (bitNumber - 1);
        return maskIntValue ;
    }

	public static boolean inSubnetMask30(String ip1,String ip2)throws Exception
	{
		return (toIntValue(ip1)|3)==(toIntValue(ip2)|3);
	}

    /**
        * 基本功能：将IP地址串转换int类型数值
        * <p>
        * <pre>
        * 类    型：私有函数
        * 修    改：
        * 修改日期：
        * 修改内容：
        * </pre>
        * @param ipString IP地址串
        * @throws Exception
        * @return int类型(32位)的IP地址
    */
    public static int toIntValue(String ipString) throws Exception
    {
        int begin=0;
        int end,segValue,returnValue=0;

        try
        {
            //分析地址前三段
            for(int i=0;i<3;i++)
            {
                //找不到“."分隔符，为非法IP
                end = ipString.indexOf('.',begin);
                if (end==-1) throw new Exception("invalid IP");

                //数据不在0－255之间，为非法IP
                segValue=Integer.parseInt(ipString.substring(begin,end));
                if ((segValue<0) || (segValue>255)) throw new Exception("invalid IP Seg:"+segValue);

                //拼装返回值
                returnValue = (returnValue << 8) | segValue ;

                //下一段的起始点
                begin = end+1;

            }

            //地址最后一段数据不在0－255之间，为非法IP
            segValue=Integer.parseInt(ipString.substring(begin));
            if ((segValue<0) || (segValue>255)) throw new Exception("invalid IP Seg:"+segValue);

            //拼装最后一段
            returnValue = (returnValue << 8) | segValue ;

            //返回
            return returnValue;
        }
        //若发生异常，则为异常IP
        catch(Exception e)
        {
            throw new Exception("invalid IP:"+ipString+"->"+e.toString());
        }
    }
    
    public static String format2IpMask(int start,int end)throws Exception
    {
    	if(start==end)
    	{
    		return toSimpleIpString(start);
    	}    	
    	
    	int mask=31;
    	
    	boolean out=false;
    	for(;mask>=0;mask--)
    	{    		
    		if((start&(1<<31-mask))!=0||(end&(1<<31-mask))==0)
    		{
    			out=true;
    			break;
    		}
    	}  	
    	
    	if(mask<31)
    	{
    		if(mask!=0||out)
    		{
    			mask++;
    		}
    		if(mask==0||(start&(-1<<(32-mask)))==(end&(-1<<(32-mask))))
    			return toSimpleIpString(start)+"/"+mask;    		
    	}
    	
    	return toSimpleIpString(start)+"-"+toSimpleIpString(end);
    }
    
    public static String format2IpMask(long start,long end)throws Exception
    {
    	if(start==end)
    	{
    		return toSimpleIpString(start);
    	}    	
    	
    	int mask=31;
    	
    	boolean out=false;
    	for(;mask>=0;mask--)
    	{    		
    		if((start&(1<<31-mask))!=0||(end&(1<<31-mask))==0)
    		{
    			out=true;
    			break;
    		}
    	}  	
    	
    	if(mask<31)
    	{
    		if(mask!=0||out)
    		{
    			mask++;
    		}
    		if(mask==0||(start&(-1<<(32-mask)))==(end&(-1<<(32-mask))))
    			return toSimpleIpString(start)+"/"+mask;    		
    	}
    	
    	return toSimpleIpString(start)+"-"+toSimpleIpString(end);
    }
    
    public static String format2IpMask(String ipstart,String ipend)throws Exception
    {    	
    	int start=toIntValue(ipstart);
    	int end=toIntValue(ipend);
    	return format2IpMask(start,end);
    }

    /**
	* 基本功能：将IP地址段转为Long型(区别toIntValue方法 转换结果不为负数)
	* 编写：刘文博
	* 数据库表： N
	* @param ipString IP字符串(例'192.168.1.1')
	* @return Long型 IP地址的值
	* @throws 转换失败
	*/
	 public static long toLongValue(String ipString) throws Exception
	 {
		 return (long)toIntValue(ipString) & 0xFFFFFFFFL;
	 }
 
	 //取第一个和最后一个可用IP地址
	 public static String[] getFirstAndLastIp(String ipMask){
			String[] result = new String[2];
			if(!ipMask.contains("/")){
				return new String[]{"请传带掩码的地址"};
			}
			String ip = ipMask.split("/")[0];
			String mask = ipMask.split("/")[1];
			if(!IPFormat.checkIpValid(ip)){
				return new String[]{"IP地址格式错误"};
			}
			try {
				if(Integer.parseInt(mask)<1||Integer.parseInt(mask)>30){
					return new String[]{"掩码范围错误"};
				}
			} catch (Exception e) {
				return new String[]{"掩码格式错误"};
			}
			try {
				Vector ipRange = IPFormat.getIpRange(ip,Integer.parseInt(mask));
				String startIP = (String)ipRange.get(0);
				String endIP = (String)ipRange.get(1);
				int startIP_num = IPFormat.toIntValue(startIP);
				int endIP_num = IPFormat.toIntValue(endIP);
				result[0] = IPFormat.toSimpleIpString(startIP_num+1);
				result[1] = IPFormat.toSimpleIpString(endIP_num-1);
			} catch (Exception e) {
				return new String[]{"计算IP地址范围错误"};
			}
			return result;
		} 

	    
    public static void main(String[] args)throws Exception
    {
//        System.out.println(format2IpMask("128.0.0.0","128.0.0.7"));
//    	System.out.println(getIpSize("126.0.0.0", "128.0.0.7"));
//    	String s="192.168.0.1/24";
//    	System.out.println(s.split("/")[0]);
//    	System.out.println(s.split("/")[1]);
//    	System.out.println(getIpRange(s.split("/")[0], Integer.parseInt(s.split("/")[1])));
//    	System.out.println(toIntValue("255.255.255.255"));
//    	System.out.println(getMaskBitNumber("255.255.255.0"));
//    	
//    	String[] str = getFirstAndLastIp("10.0.0.2/29");
//    	for (String temp : str)
//    	{
//    		System.out.println("--------------");
//    		System.out.println(temp);
//    	}
    	
//    	 int mask=getMaskIntValue(30);
//    	 System.out.println(mask);
//    	Vector vec = getIpRange("10.10.10.1", 30);
//    	System.out.println(vec.toString());
    	
    	System.out.println(IPFormat.getMaskBitNumber("255.255.255.0"));
    	
    	System.out.println(Math.pow(2, (32-24)));
    }


}
