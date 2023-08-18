package com.unitechs.biz.common.utils;

//修改记录: 日期，修改人，修改方法名（可省略参数），任务号/bug号（规则同vss label）， 设计文档，本次修改功能描述（一次修改尽量写一行）

/*
* 功能描述:处理IPv6地址拆分
* 创建日期:2013-02-25
* 创建人:李丹
* 文档编号：集团IPv6地址管理-20130220
* 任务号/bug号：
*/

import java.util.ArrayList;
import java.util.List;

public class IPv6addr {

	private final static char[] hex = new char[] { '0', '1', '2', '3', '4','5', '6', '7', 
		                                           '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	public final static int SIZE = 16;
	public final static int MAXMASK = SIZE << 3;
	private byte[] addr = new byte[SIZE];
	private int mask = SIZE << 3;

	public String toString() {
		String s = "";
		for (int i = 0; i < SIZE / 2; i++) {
			s += ":" + toHex(addr[i * 2]) + toHex(addr[i * 2 + 1]);
		}
		if (mask != MAXMASK) {
			s += "/" + mask;
		}
		return s.substring(1);
	}

	public IPv6addr(byte[] addr) {
		this(addr, MAXMASK);
	}
	
	public IPv6addr(String saddr){
		str2byte( saddr,this.addr);
	}
	
	private IPv6addr(byte[] addr, int mask) {
		if (addr.length != SIZE) {
			throw new RuntimeException("AddrIllegalException");
		}
		if (mask < 0 || mask > MAXMASK) {
			throw new RuntimeException("MaskIllegalException");
		}
		System.arraycopy(addr, 0, this.addr, 0, SIZE);
		this.mask = mask;
	}

	@Override
	public IPv6addr clone() {
		return new IPv6addr(this.addr, this.mask);
	}
	
	private static void str2byte(String input,byte[] b){
		input=input.replaceAll("[:]", "").toUpperCase();
		if(input.length()!=(SIZE<<1)){
			throw new RuntimeException("AddrIllegalException");
		}
		for(int i=0;i<SIZE;i++){
			b[i]=str2byte(input.substring(i*2,i*2+2));
		}		
	}
	private static byte str2byte(String s2){		
		return (byte)Integer.parseInt(s2, 16);
	}

	private static int getIntVal(byte b) {
		return (b + 256) % 256;
	}

	private static String toHex(byte b) {
		int i = getIntVal(b);
		return new String(new char[] { hex[i / 16], hex[i % 16] });

	}

	private static int compare(byte b1, byte b2) {
		return getIntVal(b1) - getIntVal(b2);
	}

	protected int compareTo(IPv6addr ip2) {
		for (int i = 0; i < IPv6addr.SIZE; i++) {
			int c = compare(addr[i], ip2.addr[i]);
			if (c != 0)
				return c;
		}
		return 0;
	}

	private IPv6addr getEndAddr(int mask) {
		if (mask >= 0 && mask <= MAXMASK) {
			IPv6addr end = this.clone();
			int s = mask >> 3;
			int y = mask & 7;

			for (int i = (y == 0 ? s : s + 1); i < SIZE; i++) {
				if (this.addr[i] != 0) {
					return null;
				}
				end.addr[i] = -1;
			}
			if (y != 0) {
				if ((end.addr[s] & ((1 << (8 - y)) - 1)) != 0)
					return null;
				end.addr[s] = (byte) (end.addr[s] | ((1 << (8 - y)) - 1));
			}
			return end;

		}
		return null;
	}

	public IPv6addr nextAddr() {
		byte[] b = new byte[SIZE];
		System.arraycopy(addr, 0, b, 0, SIZE);
		boolean add = true;
		for (int i = SIZE - 1; i >= 0; i--) {
			if (add) {
				b[i]++;
				if (b[i] != 0) {
					add = false;
				}
			} else {
				break;
			}
		}
		return new IPv6addr(b);
	}

	public static List<IPv6addr> splitIp(IPv6addr ip1, IPv6addr ip2) {
		if (ip1.compareTo(ip2) > 0) {
			throw new RuntimeException("InputIllegalException");
		}
		List<IPv6addr> ret = new ArrayList<IPv6addr>(MAXMASK);
		splitIp(ip1, ip2, ret);
		return ret;
	}

	private static void splitIp(IPv6addr ip1, IPv6addr ip2, List<IPv6addr> ret) {
		IPv6addr end = null;
		int mask = MAXMASK;
		for (int m = MAXMASK; m >= 0; m--) {
			IPv6addr p = ip1.getEndAddr(m);
			if (p == null || p.compareTo(ip2) > 0)
				break;
			mask = m;
			end = p;
		}
		ret.add(new IPv6addr(ip1.addr, mask));

		if (end != null) {
			if (end.compareTo(ip2) < 0) {
				splitIp(end.nextAddr(), ip2, ret);
			}
		}

	}
	
	public static IPv6addr[] get2IPbyMask(IPv6addr addr,int mask){
		IPv6addr start=addr.clone();
		start.mask=MAXMASK;
		
		IPv6addr end=addr.clone();
		end.mask=MAXMASK;
		
		int s = mask >> 3;//s= mask/8, 计算有几组8位2进制数字
		int y = mask & 7;//y = mask%8, 每个8位里面有多少位 要不变，其他(8-y)位要全0或全1
		
		//是算多少个8位后要全0或全1
		for (int i = (y == 0 ? s : s + 1); i < SIZE; i++) {
			start.addr[i]=0;
			end.addr[i] = -1;
		}
		if (y != 0) {
			start.addr[s] = (byte) (addr.addr[s] & 0xff & (-1 << (8 - y)));//后 8-y位 变0
			end.addr[s] = (byte) (start.addr[s] | ((1 << (8 - y)) - 1));//后8-y位 变1
		}		
		
		return new IPv6addr[]{start,end};
	}

	
	public static String IPV6Formatting(String IPstr)
	{
 		if (IPstr==null)
		{
			return "Error";
		}
		if ("".equals(IPstr))
		{
			return "Error";
		}
		int i = 0;
		int marknum = 0;
		int mark1 = IPstr.indexOf("::");
		int mark2 = IPstr.lastIndexOf("::");
		if(mark1!=mark2)
		{
			return "Error";
		}
		String tmpstr = IPstr;
//		if(tmpstr.indexOf(".")>=0)
//		{
//			String v4tmp = tmpstr.substring(tmpstr.lastIndexOf(":")+1,tmpstr.length());
//			if(IPFormatting(v4tmp).indexOf("Error")>=0)
//			{
//				return "Error";			
//			}
//			marknum = marknum +2;
//			tmpstr = tmpstr.substring(0,tmpstr.lastIndexOf(":"));
//		}
		int strlen = tmpstr.length();
		for(i=0;i<strlen;i++)
		{
			char tmpchar = tmpstr.charAt(i);
			if(tmpchar!=':' && (tmpchar<'0' || tmpchar>'9') && (tmpchar<'a' || tmpchar>'f') && (tmpchar<'A' || tmpchar>'F'))
			{
				return "Error";
			}
		}
		tmpstr = tmpstr + ":";
		while(tmpstr.indexOf(":")!=-1)
		{
			String substr = tmpstr.substring(0,tmpstr.indexOf(":"));
			int tmplen = tmpstr.length();
			if(tmplen != tmpstr.indexOf(":")+1)
			{
				tmpstr = tmpstr.substring(tmpstr.indexOf(":")+1,tmpstr.length());
			}
			else
			{
				tmpstr = "";
			}
			if(!substr.equals(""))
			{
				if(substr.length()>4)
				{
					return "Error";
				}
			}
			marknum=marknum+1;
			if(marknum>8)
			{
				return "Error";
			}
		}
		if(marknum<3)
		{
			return "Error";
		}
		if(mark1==-1 && marknum<8)
		{
			return "Error";
		}
		return IPstr;
	}


	public static String toSimple(String FullIPv6)
	{
		String allchar=":"+FullIPv6;
		if(allchar.length()!=40)return null;
		int[] toInt=new int[9];
		for(int i=0;i<8;i++)
		{
			toInt[i]=Integer.parseInt(allchar.substring(i*5+1,i*5+5),16);
		}

		toInt[8]=1;

		int max_start=-1,max_end=-1;

		int start=-1,end=-1;

		for(int i=0;i<8;i++)
		{
			if(start==-1&&toInt[i]==0)
			{
				start=i;
			}
			if(toInt[i]==0&&toInt[i+1]!=0)
			{
				end=i;

				if(end-start>=max_end-max_start)
				{
					max_start=start;

					max_end=end;

					start=-1;

					end=-1;
				}
			}
		}

		StringBuffer sb=new StringBuffer();

		for(int i=0;i<max_start;i++)
		{
			sb.append(":").append(Integer.toString(toInt[i],16));
		}

		if(max_start!=-1)
		{
			sb.append(":");

			if(max_start==0)
			{
				sb.append(":");
			}

			if(max_end==7)
			{
				sb.append(":");
			}
		}

		for(int i=max_end+1;i<8;i++)
		{
			sb.append(":").append(Integer.toString(toInt[i],16));
		}


		return new String(sb).substring(1).toUpperCase();
	}
}
