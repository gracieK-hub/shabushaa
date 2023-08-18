package com.unitechs.biz.common.utils;

import com.unitechs.framework.logger.Logger;
import com.unitechs.framework.logger.LoggerFactory;

/**
 * 基本功能：IPv6编码格式转换与数据校验
 * <p>
 * <pre>
 * 类    型：基础类
 * 数据库表：
 * 编    者：sunqt
 * 完成日期： 2010-10-19
 * 修    改：
 * 修改日期：
 * 修改内容：
 * </pre>
 */
public class IPv6ConverUtil {

    public void IPv6ConverUtil(){

    }

    private static Logger logger = LoggerFactory.getLogger(IPv6ConverUtil.class);

    //CommonFunction ipv = new CommonFunction();

	/*
	 * 判断流程：(顺序不能调换)
	 * 1.输入地址中存在"-",表示为地址段
	 * 2.输入地址中存在"/",表示为掩码
	 * 3.输入地址中存在"::",表示压缩格式
	 * 4.输入地址用split(":")分隔，若每个值都为4位长度，
	 * 则为全编码方式，否则为压缩格式
	 *
	 */

    /**
     * 基本功能：校验输入IPv6地址的正确性
     * 方法名称: validateIPv6Format(String ipAddress)
     * <p>
     * <pre>
     * 类    型：公共函数
     * 数据库表：
     * 修    改：sunqt
     * 修改日期：2010-10-19
     * 修改内容：
     * </pre>
     * @param  ipAddress 输入ipv6地址
     * @return String 正确：true,错误：false
     */
    public String validateIPv6Format(String ipAddress){

        String flag = "false";

        if(ipAddress!=null && !"".equals(ipAddress)){

            String ipaddress = ipAddress.trim();

            String[] st = ipaddress.split("-");
            int len = st.length;
            if(len == 2){
                //地址段
                String beginip = st[0].trim();
                String endip = st[1].trim();
                String flag1 = validateSimpleIPv6(beginip);
                String flag2 = validateSimpleIPv6(endip);
                if("1".equals(flag1) && "1".equals(flag2)){

                    flag = "true";
                }
            }else if(len == 1){

                String flag3 = validateSimpleIPv6(st[0]);
                if("1".equals(flag3)){

                    flag = "true";
                }
            }
        }
        return flag;
    }


    //校验单个地址(包括全编码，压缩格式，掩码格式),正确返回1，错误返回0

    /**
     * 基本功能：校验单个IPv6地址
     * 方法名称: validateSimpleIPv6(String ipAddress)
     * <p>
     * <pre>
     * 类    型：私有函数
     * 数据库表：
     * 修    改：sunqt
     * 修改日期：2010-10-19
     * 修改内容：
     * </pre>
     * @param  ipAddress 输入ipv6地址
     * @return String 正确：1,错误：0
     */
    private String validateSimpleIPv6(String ipAddress){

        String tag = "0";
        String[] st = ipAddress.split("/");
        int len = st.length;
        if(len == 2){
            //掩码格式
            String ipstr = st[0].trim();
            String mask = st[1].trim();
            int ipnum = Integer.parseInt(mask);
            if(ipnum > 0 && ipnum < 129){

                String tag1 = validateUnMaskIP(ipstr);
                if("1".equals(tag1)){

                    tag = "1";
                }
            }

        }else if(len == 1){
            //全编码或压缩格式编码
            String tag2 = validateUnMaskIP(st[0]);
            if("1".equals(tag2)){

                tag = "1";
            }
        }

        return tag;

    }

    //校验单个地址(压缩格式，全编码格式),正确返回1，错误返回0

    /**
     * 基本功能：校验单个IPv6地址
     * 方法名称: validateUnMaskIP(String ipAddress)
     * <p>
     * <pre>
     * 类    型：私有函数
     * 数据库表：
     * 修    改：sunqt
     * 修改日期：2010-10-19
     * 修改内容：
     * </pre>
     * @param  ipAddress 输入ipv6地址
     * @return String 正确：1,错误：0
     */
    private String validateUnMaskIP(String ipAddress){

        String tag1 = "0";
        int index = ipAddress.indexOf("::");
        if(index != -1){
            //压缩格式编码(带::符号)
            String flag1 = validateCompressFormat(ipAddress);
            if("1".equals(flag1)){
                tag1 = "1";
            }
        }else{
            //全编码或压缩格式编码
            String flag2 = validateAllFormat(ipAddress);
            if("1".equals(flag2)){
                tag1 = "1";
            }
        }

        return tag1;
    }


    /**
     * 基本功能：校验IPv6压缩编码
     * 方法名称: validateCompressFormat(String ipAddress)
     * <p>
     * <pre>
     * 类    型：私有函数
     * 数据库表：
     * 修    改：sunqt
     * 修改日期：2010-10-19
     * 修改内容：
     * </pre>
     * @param  ipAddress 输入ipv6地址
     * @return String 正确：1,错误：0
     */
    private String validateCompressFormat(String ipAddress){

        String tag2 = "0";

        String punctuation = "0";
        if(ipAddress.endsWith("::")){
            punctuation = "1";
        }

        int mark1 = ipAddress.indexOf("::");
        int mark2 = ipAddress.lastIndexOf("::");
        if(mark1 == mark2 && mark1 >0){

            String temstr = ipAddress.replace("::", ":");

            if("1".equals(punctuation)){

                temstr = temstr.substring(0, temstr.length()-1);
            }
            String[] st = temstr.split(":");
            int len = st.length;
            if(len >0){
                for(int i=0;i<len;i++){

                    String tem = st[i];
                    if(!"".equals(tem) && tem.length()<5){
                        //判断tem中每个字符是否在0-9或A-F中或a-f中，长度不大于4位
                        int size = tem.length();
                        for(int j=0;j<size;j++){

                            char str = tem.charAt(j);
                            if((str>='0' && str<='9') || (str>='a' && str<='f') || (str>='A' && str<='F')){

                                tag2 = "1";
                            }else{
                                tag2 = "0";
                                return tag2;
                            }
                        }

                    }else{

                        tag2 = "0";
                        return tag2;
                    }

                }

            }

        }
        return tag2;
    }

    /**
     * 基本功能：校验IPv6全编码或不带(::)压缩格式编码
     * 方法名称: validateCompressFormat(String ipAddress)
     * <p>
     * <pre>
     * 类    型：私有函数
     * 数据库表：
     * 修    改：sunqt
     * 修改日期：2010-10-19
     * 修改内容：
     * </pre>
     * @param  ipAddress 输入ipv6地址
     * @return String 正确：1,错误：0
     */
    private String validateAllFormat(String ipAddress){

        String tag3 = "0";
        String[] st = ipAddress.split(":");
        int len = st.length;
        if(len == 8){

            for(int i=0;i<len;i++){

                String tem = st[i];
                if(!"".equals(tem) && tem.length() <= 4){

                    //判断tem中每个字符是否在0-9或A-F中或a-f中，长度不大于4位
                    int size = tem.length();
                    for(int j=0;j<size;j++){

                        char str = tem.charAt(j);
                        if((str>='0' && str<='9') || (str>='a' && str<='f') || (str>='A' && str<='F')){

                            tag3 = "1";
                        }else{
                            tag3 = "0";
                            return tag3;

                        }
                    }

                }else{

                    tag3 = "0";
                    return tag3;
                }
            }
        }

        return tag3;
    }


    /**
     * 基本功能：获取ipv6的地址格式
     * 方法名称: getFormatType(String ipAddress)
     * <p>
     * <pre>
     * 类    型：公共函数
     * 数据库表：
     * 修    改：sunqt
     * 修改日期：2010-10-19
     * 修改内容：
     * </pre>
     * @param  ipAddress 输入ipv6地址
     * @return String 1:全编码或压缩格式,2:掩码格式,3:地址段,0:类型错误
     */
    public String getFormatType(String ipAddress){

        String flag = "0";
        if(ipAddress!= null && !"".equals(ipAddress)){

            String tag = validateIPv6Format(ipAddress);
            if("true".equals(tag)){

                if(ipAddress.contains("-")){
                    //ip地址段
                    flag = "3";
                    return flag;
                }else if(ipAddress.contains("/")){
                    //掩码格式
                    flag = "2";
                    return flag;
                }else{
                    //全编码或压缩格式
                    flag = "1";
                    return flag;
                }

            }else{

                flag = "0";
                return flag;
            }

        }

        return flag;
    }

    /**
     * 基本功能：将输入ipv6地址信息转换为全编码格式
     * 方法名称: converToAllFormat(String ipAddress)
     * <p>
     * <pre>
     * 类    型：公共函数
     * 数据库表：
     * 修    改：sunqt
     * 修改日期：2010-10-19
     * 修改内容：
     * </pre>
     * @param  ipAddress 输入ipv6地址
     * @return String 全编码格式
     */
    public String converToAllFormat(String ipAddress){

        String newip="";
        String temp = getFormatType(ipAddress);

        if("1".equals(temp)){
            //全编码或压缩格式
            newip = IPV6Format(ipAddress);

        }else if("2".equals(temp)){
            //掩码格式
            newip = inetnumV6Format(ipAddress);

        }else if("3".equals(temp)){
            //ip地址段
            String[] st = ipAddress.split("-");
            String beginip = st[0];
            String endip = st[1];
            String str1 = IPV6Format(beginip);
            String str2 = IPV6Format(endip);
            newip = str1+"-"+str2;

        }

        return newip;
    }

    /**
     * 基本功能：将输入ipv6地址信息转换为全编码格式(起始地址-终止地址)
     * 方法名称: converToAllFormat1(String ipAddress)
     * <p>
     * <pre>
     * 类    型：公共函数
     * 数据库表：
     * 修    改：sunqt
     * 修改日期：2010-10-19
     * 修改内容：
     * </pre>
     * @param  ipAddress 输入ipv6地址
     * @return String 全编码格式
     */
    public String converToAllFormat1(String ipAddress){

        String newip="";
        String temp = getFormatType(ipAddress);

        if("1".equals(temp)){
            //全编码或压缩格式
            String str1 = IPV6Format(ipAddress);
            String str2 = IPV6Format(ipAddress);
            newip = str1+"-"+str2;

        }else if("2".equals(temp)){
            //掩码格式
            newip = inetnumV6Format(ipAddress);

        }else if("3".equals(temp)){
            //ip地址段
            String[] st = ipAddress.split("-");
            String beginip = st[0];
            String endip = st[1];
            String str1 = IPV6Format(beginip);
            String str2 = IPV6Format(endip);
            newip = str1+"-"+str2;

        }

        return newip;
    }

    /**
     * 基本功能：全编码格式转换为显示格式(不校验输入格式的正确性)
     * 方法名称: unConverToFormat(String ipAddress)
     * <p>
     * <pre>
     * 类    型：公共函数
     * 数据库表：
     * 修    改：sunqt
     * 修改日期：2010-10-19
     * 修改内容：
     * </pre>
     * @param  ipAddress 输入ipv6全编码地址
     * @return String 显示编码格式
     */
    public String unConverToFormat(String ipAddress){

        String newip = "";
        String ipaddress = ipAddress.trim();
        if(ipaddress!= null && !"".equals(ipaddress)){

            if(ipaddress.contains("-")){

                String[] st = ipaddress.split("-");
                String beginip = st[0];
                String endip = st[1];
                if(beginip.equals(endip)){
                    //单个全编码IP地址用地址段形式显示
                    newip = UnIPV6Format(beginip);
                }else{
                    //判断是否能转换为掩码表示形式
                    //能：显示为掩码，不能：显示为压缩编码
                    String result = unConverToMask(ipaddress);
                    if(!"".equals(result)){
                        //掩码格式
                        newip = result;
                    }else{
                        //压缩格式
                        String newbeginip = UnIPV6Format(beginip);
                        String newendip = UnIPV6Format(endip);
                        newip = newbeginip + "-" + newendip;
                    }
                }

            }else{
                //单个全编码IP地址
                newip = UnIPV6Format(ipaddress);
            }

        }

        return newip;
    }

    //全编码地址段转换为掩码格式(前提:输入数据正确且必须为全编码地址段，此方法不校验输入信息的正确性)
    /**
     * 基本功能：全编码地址段转换为掩码格式
     * 方法名称: unConverToMask(String ipAddress)
     * <p>
     * <pre>
     * 类    型：私有函数
     * 数据库表：
     * 修    改：sunqt
     * 修改日期：2010-10-20
     * 修改内容：
     * </pre>
     * @param  ipAddress 全编码IP地址
     * @return String 对应掩码地址
     */
    public String unConverToMask(String ipAddress){

        String newip = "";
        String ipaddress = ipAddress.trim();
        if(ipaddress!= null && !"".equals(ipaddress)){

            String[] st = ipaddress.split("-");
            String beginip = st[0];
            String endip = st[1];
            String tembeginip = HexadecimalToBinary(beginip);
            String temendip = HexadecimalToBinary(endip);

            int m=0;
            int n=0;
            for(int i=127;i>=0;i--){

                char tb = tembeginip.charAt(i);
                char te = temendip.charAt(i);
                if(tb == '0' && te == '1'){
                    n++;
                }else{
                    break;
                }
            }

            for(int j=0;j<128;j++){

                char tb1 = tembeginip.charAt(j);
                char te1 = temendip.charAt(j);
                if(tb1 == te1){
                    m++;
                }else{
                    break;
                }
            }

            if(m+n == 128){
                String tem1 = UnIPV6Format(beginip);
                newip = tem1 + "/" + String.valueOf(m);
            }
        }

        return newip;
    }

    /**
     * 基本功能：十六进制字符串转换为二进制字符串
     * 方法名称: HexadecimalToBinary(String ipStr)
     * <p>
     * <pre>
     * 类    型：私有函数
     * 数据库表：
     * 修    改：sunqt
     * 修改日期：2010-10-20
     * 修改内容：
     * </pre>
     * @param  ipStr 十六进制IP地址
     * @return String 对应二进制IP地址
     */
    public String HexadecimalToBinary(String ipStr){

        String newstr="";
        StringBuffer sb = new StringBuffer("");
        String ipstr = ipStr.trim();
        if(ipstr!=null && !"".equals(ipstr)){

            String[] st = ipstr.split(":");
            int len = st.length;
            for(int i=0;i<len;i++){

                String tem = st[i];
                int size = tem.length();
                for(int j=0;j<size;j++){

                    char c = tem.charAt(j);
                    String binvalue = HexToBinary(c);
                    sb.append(binvalue);
                }

            }

            newstr = sb.toString();
            sb = null;
        }

        return newstr;

    }

    /**
     * 基本功能：十六进制与二进制对应关系
     * 方法名称: HexToBinary(char c)
     * <p>
     * <pre>
     * 类    型：私有函数
     * 数据库表：
     * 修    改：sunqt
     * 修改日期：2010-10-20
     * 修改内容：
     * </pre>
     * @param  c 输入十六进制字符
     * @return String 对应二进制字符串
     */
    private String HexToBinary(char c){

        String str="";
        switch (c) {
            case '0': str="0000"; break;
            case '1': str="0001"; break;
            case '2': str="0010"; break;
            case '3': str="0011"; break;
            case '4': str="0100"; break;
            case '5': str="0101"; break;
            case '6': str="0110"; break;
            case '7': str="0111"; break;
            case '8': str="1000"; break;
            case '9': str="1001"; break;
            case 'A': str="1010"; break;
            case 'a': str="1010"; break;
            case 'B': str="1011"; break;
            case 'b': str="1011"; break;
            case 'C': str="1100"; break;
            case 'c': str="1100"; break;
            case 'D': str="1101"; break;
            case 'd': str="1101"; break;
            case 'E': str="1110"; break;
            case 'e': str="1110"; break;
            case 'F': str="1111"; break;
            case 'f': str="1111"; break;
            default:
                break;

        }

        return str;
    }

    /**
     * 基本功能：十六进制与十进制对应关系
     * 方法名称: HexToDecimal(char c)
     * <p>
     * <pre>
     * 类    型：私有函数
     * 数据库表：
     * 修    改：sunqt
     * 修改日期：2010-10-20
     * 修改内容：
     * </pre>
     * @param  c 输入十六进制字符
     * @return int 对应十进制数字
     */
    private int HexToDecimal(char c){

        int str=-1;
        switch (c) {
            case '0': str=0; break;
            case '1': str=1; break;
            case '2': str=2; break;
            case '3': str=3; break;
            case '4': str=4; break;
            case '5': str=5; break;
            case '6': str=6; break;
            case '7': str=7; break;
            case '8': str=8; break;
            case '9': str=9; break;
            case 'A': str=10; break;
            case 'a': str=10; break;
            case 'B': str=11; break;
            case 'b': str=11; break;
            case 'C': str=12; break;
            case 'c': str=12; break;
            case 'D': str=13; break;
            case 'd': str=13; break;
            case 'E': str=14; break;
            case 'e': str=14; break;
            case 'F': str=15; break;
            case 'f': str=15; break;
            default:
                break;
        }

        return str;
    }

    /**
     * 基本功能：十进制与十六进制对应关系
     * 方法名称: DecimalToHex(int c)
     * <p>
     * <pre>
     * 类    型：私有函数
     * 数据库表：
     * 修    改：sunqt
     * 修改日期：2010-10-20
     * 修改内容：
     * </pre>
     * @param  c 输入十进制数字
     * @return int 对应十六进制字符串
     */
    private String DecimalToHex(int c){

        String str="";
        switch (c) {
            case 0: str="0"; break;
            case 1: str="1"; break;
            case 2: str="2"; break;
            case 3: str="3"; break;
            case 4: str="4"; break;
            case 5: str="5"; break;
            case 6: str="6"; break;
            case 7: str="7"; break;
            case 8: str="8"; break;
            case 9: str="9"; break;
            case 10: str="A"; break;
            case 11: str="B"; break;
            case 12: str="C"; break;
            case 13: str="D"; break;
            case 14: str="E"; break;
            case 15: str="F"; break;
            default:
                break;
        }

        return str;
    }

    /**
     * 基本功能：IPv6地址转换为数字(全编码IP地址)
     * 方法名称: converIPv6ToInt(String ipAddress)
     * <p>
     * <pre>
     * 类    型：公有函数
     * 数据库表：
     * 修    改：sunqt
     * 修改日期：2010-10-20
     * 修改内容：
     * </pre>
     * @param  ipAddress 输入IPv6地址字符串
     * @return double 对应数字
     */
    public double converIPv6ToInt(String ipAddress){

        double result = 0;
        StringBuffer sb = new StringBuffer("");

        if(ipAddress != null && !"".equals(ipAddress)){

            String[] st = ipAddress.split(":");
            int len = st.length;
            if(len == 8){

                for(int i=0;i<len;i++){

                    String tem = st[i];
                    int len1 = tem.length();
                    if(len1 == 4){

                        for(int j=0;j<len1;j++){

                            char c = tem.charAt(j);
                            if((c>='a' && c<='f') || (c>='A' && c<='F')){
                                //次方数
                                int m = 32-i*4-j-1;
                                //十进制数
                                int decm = Integer.parseInt(String.valueOf(c), 16);
                                sb.append(Integer.toString(decm) + "*" + "16e"+Integer.toString(m)+"+");
                                //有问题?(类型转换)
                                result = result + decm * Math.pow(16,m);

                            }else if(c>='0' && c<='9'){

                                //次方数
                                int m = 32-i*4-j-1;
                                //十进制数
                                int decm = Integer.parseInt(String.valueOf(c));
                                sb.append(Integer.toString(decm) + "*" + "16e"+Integer.toString(m)+"+");
                                //有问题?(类型转换)
                                result = result + decm * Math.pow(16,m);

                            }else{

                                result = 0;
                                return result;
                            }
                        }

                    }else{

                        result = 0;
                        return result;
                    }

                }

            }

        }

        String opt = sb.toString();
        sb = null;
        String opt1 = opt.substring(0, opt.length()-1);
        System.out.println(opt1);
        return result;
    }

    /**
     * 基本功能：全编码格式的IP地址相减(前提:ipStr1>ipStr2,且都为全编码格式)
     * 方法名称: SubTractIP(String ipStr1,String ipStr2)
     * <p>
     * <pre>
     * 类    型：公有函数
     * 数据库表：
     * 修    改：sunqt
     * 修改日期：2010-10-20
     * 修改内容：
     * </pre>
     * @param  ipStr1,ipStr2 输入IPv6地址字符串
     * @return String 相减后得到的十六进制字符串
     */
    public String SubTractIP(String ipStr1,String ipStr2){

        String newip = "";
        int size = 39;
        String[] temip = new String[size];
        String[] st1 = ipStr1.split(":");
        String[] st2 = ipStr2.split(":");
        int len1 = st1.length;
        int len2 = st2.length;
        if(len1 == 8 && len2 == 8){
            //借位标识(1:借位,2:不借位)
            int flag = 0;
            //每段进行比较，借位，加减按十进制计算
            for(int i=7;i>=0;i--){

                if(st1[i].length()!=4 || st2[i].length()!=4){

                    return "";
                }else{
                    //做减法
                    for(int j=3;j>=0;j--){
                        char c1 = st1[i].charAt(j);
                        char c2 = st2[i].charAt(j);
                        int n=0;
                        if(flag == 1){
                            //前一组有借位
                            int n1 = HexToDecimal(c1);
                            int n2 = HexToDecimal(c2);
                            if(n1-1>=n2){
                                //本组无借位
                                n = n1-1-n2;
                                String s = DecimalToHex(n);
                                temip[size-1]=s;
                                size--;
                                flag = 0;
                            }else{
                                //本组有借位
                                n = n1-1+16-n2;
                                String s = DecimalToHex(n);
                                temip[size-1]=s;
                                size--;
                                flag = 1;
                            }
                        }else{
                            //前一组无借位
                            int n1 = HexToDecimal(c1);
                            int n2 = HexToDecimal(c2);
                            if(n1>=n2){
                                //本组无借位
                                n = n1-n2;
                                String s = DecimalToHex(n);
                                temip[size-1]=s;
                                size--;
                                flag = 0;
                            }else{
                                //本组有借位
                                n = n1+16-n2;
                                String s = DecimalToHex(n);
                                temip[size-1]=s;
                                size--;
                                flag = 1;
                            }
                        }
                    }

                    if(i!=0){
                        temip[size-1]=":";
                        size--;
                    }

                }

            }
        }

        for(int k=0;k<39;k++){

            newip = newip + temip[k];
        }

        return newip;
    }

    /**
     * 基本功能：将不带":"的十六进制字符串转换为全编码格式的字符串
     * 方法名称: ToALLCodeIPFormat(String ipAddress)
     * <p>
     * <pre>
     * 类    型：公有函数
     * 数据库表：
     * 修    改：sunqt
     * 修改日期：2010-10-20
     * 修改内容：
     * </pre>
     * @param  ipAddress 输入IPv6地址字符串
     * @return String 全编码的十六进制字符串
     */
    public String ToALLCodeIPFormat(String ipAddress){

        String newip = "";
        String temp="";
        if(ipAddress!=null && !"".equals(ipAddress)){

            int len = ipAddress.length();
            int len1 = 32- len;
            for(int i=0;i<len1;i++){

                temp = temp + "0";
            }
            temp = temp + ipAddress;
            int len2 = temp.length();
            for(int j=0;j<len2;j+=4){

                newip = newip + temp.substring(j,j+4)+":";
            }

            newip = newip.substring(0, newip.length()-1);
        }
        return newip;
    }

    /**
     * 基本功能：全编码格式的IP地址相加(前提:都为全编码格式)
     * 方法名称: SumIP(String ipStr1,String ipStr2)
     * <p>
     * <pre>
     * 类    型：公有函数
     * 数据库表：
     * 修    改：sunqt
     * 修改日期：2010-10-20
     * 修改内容：
     * </pre>
     * @param  ipStr1,ipStr2 输入IPv6地址字符串
     * @return String 相加后得到的十六进制字符串
     */
    public String SumIP(String ipStr1,String ipStr2){

        String newip = "";
        int size = 39;
        String[] temip = new String[size];
        String[] st1 = ipStr1.split(":");
        String[] st2 = ipStr2.split(":");
        int len1 = st1.length;
        int len2 = st2.length;
        if(len1 == 8 && len2 == 8){
            //借位标识(1:进位,0:不进位)
            int flag = 0;
            //每段进行比较，借位，加减按十进制计算
            for(int i=7;i>=0;i--){

                if(st1[i].length()!=4 || st2[i].length()!=4){

                    return "";
                }else{
                    //做加法
                    for(int j=3;j>=0;j--){
                        char c1 = st1[i].charAt(j);
                        char c2 = st2[i].charAt(j);
                        int n=0;
                        if(flag == 1){
                            //前一组有进位
                            int n1 = HexToDecimal(c1);
                            int n2 = HexToDecimal(c2);
                            n = n1+n2+1;
                            if(n>15){
                                n = n-16;
                                String s = DecimalToHex(n);
                                temip[size-1]=s;
                                size--;
                                flag = 1;
                            }else{
                                String s = DecimalToHex(n);
                                temip[size-1]=s;
                                size--;
                                flag = 0;
                            }
                        }else{
                            //前一组无进位
                            int n1 = HexToDecimal(c1);
                            int n2 = HexToDecimal(c2);
                            n = n1+n2;
                            if(n>15){
                                n = n-16;
                                String s = DecimalToHex(n);
                                temip[size-1]=s;
                                size--;
                                flag = 1;
                            }else{
                                String s = DecimalToHex(n);
                                temip[size-1]=s;
                                size--;
                                flag = 0;
                            }
                        }
                    }

                    if(i!=0){
                        temip[size-1]=":";
                        size--;
                    }

                }

            }

            if(flag == 1){
                //还有进位，说明相加后的结果超出长度范围，错误
                return "-1";

            }
        }

        for(int k=0;k<39;k++){

            newip = newip + temip[k];
        }

        return newip;

    }

    //十进制数转换为十六进制(ipV6全编码格式)
    public String decimalToIPv6AllFormat(int num){

        String newip = "";
        StringBuffer sb = new StringBuffer("");
        int size = 39;
        String[] temip = new String[size];
        String hex = Integer.toString(num, 16);
        String hex1 = hex.toUpperCase();
        int len = hex.length();
        int len1 = 32-len;
        for(int i=0;i<len1;i++){

            sb.append("0");
        }
        sb.append(hex1);
        String temp = sb.toString();
        sb = null;
        String str1 = temp.substring(0, 4);
        String str2 = temp.substring(4, 8);
        String str3 = temp.substring(8, 12);
        String str4 = temp.substring(12, 16);
        String str5 = temp.substring(16, 20);
        String str6 = temp.substring(20, 24);
        String str7 = temp.substring(24, 28);
        String str8 = temp.substring(28, 32);

        newip = str1+":"+str2+":"+str3+":"+str4+":"+str5+":"+str6+":"+str7+":"+str8;
        return newip;

    }

    //转换IPv6掩码格式(输入为ipv6掩码格式字符串)
    //2001:20:12F::/48转换为2001:0020:012F::/48
    public String convertMask(String ipAddress){

        String[] st = ipAddress.split("/");
        String bip = st[0];
        String mask = st[1];
        String temstr = "";
        StringBuffer sb = new StringBuffer("");
        if(bip.contains("::")){

            if(bip.endsWith("::")){

                temstr = bip.replace("::", ":");
                temstr = temstr.substring(0, temstr.length()-1);
            }else{

                temstr = bip.replace("::", ":");
            }
        }else{

            temstr = bip;
        }

        String st1[] = temstr.split(":");
        int len = st1.length;
        for(int i=0;i<len;i++){

            String tem = st1[i];
            int len1 = tem.length();
            if(len1 == 4){
                sb.append(tem).append(":");
            }else if(len1 == 3){

                sb.append("0").append(tem).append(":");
            }else if(len1 == 2){

                sb.append("00").append(tem).append(":");
            }else if(len1 ==1){

                sb.append("000").append(tem).append(":");
            }

        }

        String str = sb.toString();
        str = str.substring(0, str.length()-1);
        String newip = str+"/"+mask;

        sb = null;
        return newip;

    }

    //IP地址比较大小(思路：分为4组,从大往小开始比较，不相等，得出结果，相等，比较下一组)
    /**
     * 基本功能:IP地址比较大小
     * 方法名称: compareToIPv6
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
     * @param ipAddr1 ipAddr2		合法的单个IP地址(无需全编码)
     * @return String		1:ipAddr1>ipAddr2  -1:ipAddr1<ipAddr2 0:ipAddr1=ipAddr2
     * @throws Exception
     */
    public String compareToIPv6(String ipAddr1,String ipAddr2){

        String result = "";
        String ipstr1 = IPV6Format(ipAddr1);
        String ipstr2 = IPV6Format(ipAddr2);

        int flag = ipstr1.compareTo(ipstr2);

        if(flag>0){

            result = "1";
        }else if(flag ==0){

            result = "0";
        }else{

            result = "-1";
        }

        return result;
    }

    /**************************commonfunction method start***************************/
    //ipv6地址转换为全编码格式
    public String IPV6Format(String ip)
    {
        if (ip == null)
            return "error";

        ip = ip.trim();

        if (ip.equals("::"))
            return "0000:0000:0000:0000:0000:0000:0000:0000";

        String returnIP = "";

        String tmpstr = ip;

        String convertstr = "";

        try
        {
            // //如果是v4格式，则首先变为v6格式

            if (tmpstr.indexOf(".") > 0)
            {
                tmpstr = tmpstr.substring(tmpstr.lastIndexOf(":") + 1, tmpstr
                        .length());

                tmpstr = tmpstr + ".";

                for (int i = 0; i < 4; i++)
                {
                    String substr = tmpstr.substring(0, tmpstr.indexOf("."));

                    if (i != 3)
                        tmpstr = tmpstr.substring(tmpstr.indexOf(".") + 1,
                                tmpstr.length());

                    int subint = Integer.parseInt(substr);

                    if (i == 2)
                        convertstr = convertstr + ":";

                    substr = Integer.toHexString(subint);

                    if (substr.length() == 1)
                        substr = "0" + substr;

                    convertstr = convertstr + substr;
                }

                ip = ip.substring(0, ip.lastIndexOf(":") + 1);
                ip = ip + convertstr;
            }

            // /判断：数量
            tmpstr = ip;
            int marknum = 0;

            int index;

            while ((index = tmpstr.indexOf(":")) != -1)
            {
                marknum++;

                tmpstr = tmpstr.substring(index + 1);

                if (tmpstr == null)
                    break;
            }

            index = ip.indexOf("::");

            if (index != -1)
            {
                marknum--;

                convertstr = ":";

                if (index == 0 || index == ip.length() - 2)
                {
                    marknum--;
                }

                while (marknum < 7)
                {
                    convertstr += "0000:";
                    marknum++;
                }

                if (convertstr.equals(":"))
                    return "error";

                if (index == 0)
                    convertstr = convertstr.substring(1);
                else if (index == ip.length() - 2)
                    convertstr = convertstr.substring(0,
                            convertstr.length() - 1);

                ip = ip.replaceFirst("::", convertstr);

                if (ip.indexOf("::") != -1)
                    return "error";
            }
            else if (marknum != 7)
                return "error";

            // //进行地址格式化
            ip += ":";

            for (int i = 0; i < 8; i++)
            {
                String substr = ip.substring(0, ip.indexOf(":"));

                if (substr == null || substr.trim().equals(""))
                    return "error";

                if (i != 7)
                    ip = ip.substring(ip.indexOf(":") + 1, ip.length());

                if (substr.length() > 4)
                    return "error";
                else if (substr.length() == 3)
                    substr = "0" + substr;
                else if (substr.length() == 2)
                    substr = "00" + substr;
                else if (substr.length() == 1)
                    substr = "000" + substr;

                returnIP = returnIP + ":" + substr;
            }

            returnIP = returnIP.substring(1);

            returnIP = returnIP.toUpperCase();

            logger.info("IPV6Format return:" + returnIP);

            return returnIP;
        }
        catch (Exception e)
        {
        	logger.error("IPV6Format异常"+e);
        	throw new RuntimeException("IPV6Format异常"+e.getMessage());
        }
    }

    //ipv6掩码转换成全编码格式
    public String inetnumV6Format(String ip)
    {
    	String ipAddress = ip.substring(0, ip.indexOf("/"));
		String maskStr = ip.substring(ip.indexOf("/") + 1, ip.length());
		ipAddress=IPV6Format(ipAddress);
		int mask = Integer.parseInt(maskStr);
		IPv6addr  ipv6Addr=new IPv6addr(ipAddress);
		IPv6addr []  ipv6AddrArr=IPv6addr.get2IPbyMask(ipv6Addr, mask);
		return ipv6AddrArr[0].toString()+"-"+ipv6AddrArr[1].toString();
    }

    //根据初始地址和地址数得到终止地址
    public String getIPV6EndIP(String BeginIP, int IPPrefix)
    {
        try
        {
            String IPstr = BeginIP;
            int[] subInt = new int[8];
            IPstr = BeginIP + ":";
            // //分为8段赋值
            for (int i = 0; i < 8; i++)
            {
                String substr = IPstr.substring(0, IPstr.indexOf(":"));
                if (i != 7)
                    IPstr = IPstr.substring(IPstr.indexOf(":") + 1, IPstr
                            .length());
                subInt[i] = Integer.parseInt(substr, 16);
            }
            // //
            int addnum = 0;
            int beginsub = 7;
            if (IPPrefix <= 128 && IPPrefix >= 113)
            {
                addnum = (int) Math.pow(2, (128 - IPPrefix)) - 1;
                beginsub = 7;
            }
            else if (IPPrefix <= 112 && IPPrefix >= 97)
            {
                addnum = (int) Math.pow(2, (112 - IPPrefix)) - 1;
                beginsub = 6;
            }
            else if (IPPrefix <= 96 && IPPrefix >= 81)
            {
                addnum = (int) Math.pow(2, (96 - IPPrefix)) - 1;
                beginsub = 5;
            }
            else if (IPPrefix <= 80 && IPPrefix >= 65)
            {
                addnum = (int) Math.pow(2, (80 - IPPrefix)) - 1;
                beginsub = 4;
            }
            else if (IPPrefix <= 64 && IPPrefix >= 49)
            {
                addnum = (int) Math.pow(2, (64 - IPPrefix)) - 1;
                beginsub = 3;
            }
            else if (IPPrefix <= 48 && IPPrefix >= 33)
            {
                addnum = (int) Math.pow(2, (48 - IPPrefix)) - 1;
                beginsub = 2;
            }
            else if (IPPrefix <= 32 && IPPrefix >= 17)
            {
                addnum = (int) Math.pow(2, (32 - IPPrefix)) - 1;
                beginsub = 1;
            }
            else if (IPPrefix <= 16 && IPPrefix >= 1)
            {
                addnum = (int) Math.pow(2, (16 - IPPrefix)) - 1;
                beginsub = 0;
            }
            else if (IPPrefix == 0)
            {
                if (BeginIP.equals(""))
                    return "";
                else
                    return "error";
            }
            else
            {
                return "error";
            }
            // //将地址数增加到合适的位数上
            for (int j = 7; j >= 0; j--)
            {
                if (j > beginsub)
                    subInt[j] = subInt[j] + 0xFFFF;
                else if (j == beginsub)
                    subInt[j] = subInt[j] + addnum;
                if (subInt[j] <= 0xFFFF)
                {

                }
                else if (subInt[j] > 0xFFFF && j > 0)
                {
                    subInt[j] = subInt[j] - 0xFFFF - 1;
                    subInt[j - 1] = subInt[j - 1] + 1;
                }
                else
                {
                    return "error";
                }
            }

            // //组合并标准化
            IPstr = "";
            for (int i = 0; i < 8; i++)
            {
                IPstr = IPstr + ":";
                String substr = Integer.toHexString(subInt[i]);
                if (substr.length() == 3)
                {
                    substr = "0" + substr;
                }
                else if (substr.length() == 2)
                {
                    substr = "00" + substr;
                }
                else if (substr.length() == 1)
                {
                    substr = "000" + substr;
                }
                else if (substr.length() == 0)
                {
                    substr = "0000";
                }
                IPstr = IPstr + substr;
            }
            IPstr = IPstr.substring(1);

            IPstr = IPstr.toUpperCase();

            logger.info(" getIPV6EndIP return:" + IPstr);

            return IPstr;
        }
        catch (Exception e)
        {
            return "error";
        }
    }

    //全编码格式转换为显示格式
    public String UnIPV6Format(String ip)
    {
        try
        {
            String allchar = (":" + ip).trim();
            if (allchar.length() != 40)
                return "error";
            int[] toInt = new int[9];
            for (int i = 0; i < 8; i++)
            {
                toInt[i] = Integer.parseInt(allchar.substring(i * 5 + 1,
                        i * 5 + 5), 16);
            }

            toInt[8] = 1;

            int max_start = -1, max_end = -1;

            int start = -1, end = -1;

            for (int i = 0; i < 8; i++)
            {
                if (start == -1 && toInt[i] == 0)
                {
                    start = i;
                }
                if (toInt[i] == 0 && toInt[i + 1] != 0)
                {
                    end = i;

                    if (end - start >= max_end - max_start)
                    {
                        max_start = start;

                        max_end = end;

                        start = -1;

                        end = -1;
                    }
                }
            }

            StringBuffer sb = new StringBuffer();

            for (int i = 0; i < max_start; i++)
            {
                sb.append(":").append(Integer.toString(toInt[i], 16));
            }

            if (max_start != -1)
            {
                sb.append(":");

                if (max_start == 0)
                {
                    sb.append(":");
                }

                if (max_end == 7)
                {
                    sb.append(":");
                }
            }

            for (int i = max_end + 1; i < 8; i++)
            {
                sb.append(":").append(Integer.toString(toInt[i], 16));
            }

            return new String(sb).substring(1).toUpperCase();
        }
        catch (Exception e)
        {
            return "error";
        }
    }

    //ipv6地址减一
    public String PreIPV6(String ip)
    {
        try
        {
            logger.info("PreIPV6 is called " + ip);
            String returnIP = "";
            String tmpstr = ip;
            int[] subInt = new int[8];
            tmpstr = tmpstr + ":";
            // //分为8段赋值
            for (int i = 0; i < 8; i++)
            {
                String substr = tmpstr.substring(0, tmpstr.indexOf(":"));
                if (i != 7)
                    tmpstr = tmpstr.substring(tmpstr.indexOf(":") + 1, tmpstr
                            .length());
                subInt[i] = Integer.parseInt(substr, 16);
            }
            // /从末端开始减1
            for (int i = 7; i >= 0; i--)
            {
                subInt[i] = subInt[i] - 1;
                if (subInt[i] < 0 && i > 0)
                {
                    subInt[i] = subInt[i] + 0xFFFF + 1;
                }
                else if (subInt[i] < 0 && i == 0)
                    return "error";
                else
                {
                    break;
                }
            }
            // //
            for (int i = 0; i < 8; i++)
            {
                returnIP = returnIP + ":";
                String substr = Integer.toHexString(subInt[i]);
                if (substr.length() == 3)
                {
                    substr = "0" + substr;
                }
                else if (substr.length() == 2)
                {
                    substr = "00" + substr;
                }
                else if (substr.length() == 1)
                {
                    substr = "000" + substr;
                }
                else if (substr.length() == 0)
                {
                    substr = "0000";
                }
                returnIP = returnIP + substr;
            }
            returnIP = returnIP.substring(1);
            returnIP = returnIP.toUpperCase();
            logger.info("PreIPV6 return:" + returnIP);
            return returnIP;
        }
        catch (Exception e)
        {
            return "error";
        }
    }

    //ipv6地址加一
    public String LateIPV6(String ip)
    {
        try
        {
            logger.info("LateIPV6 is called");
            String returnIP = "";
            String tmpstr = ip;
            int[] subInt = new int[8];
            tmpstr = tmpstr + ":";
            // //分为8段赋值
            for (int i = 0; i < 8; i++)
            {
                String substr = tmpstr.substring(0, tmpstr.indexOf(":"));
                if (i != 7)
                    tmpstr = tmpstr.substring(tmpstr.indexOf(":") + 1, tmpstr
                            .length());
                subInt[i] = Integer.parseInt(substr, 16);
            }
            // /从末端开始加1
            for (int i = 7; i >= 0; i--)
            {
                subInt[i] = subInt[i] + 1;
                if (subInt[i] > 0xFFFF && i > 0)
                {
                    subInt[i] = subInt[i] - 0xFFFF - 1;
                }
                else if (subInt[i] > 0xFFFF && i == 0)
                    return "error";
                else
                {
                    break;
                }
            }
            // //
            for (int i = 0; i < 8; i++)
            {
                returnIP = returnIP + ":";
                String substr = Integer.toHexString(subInt[i]);
                if (substr.length() == 3)
                {
                    substr = "0" + substr;
                }
                else if (substr.length() == 2)
                {
                    substr = "00" + substr;
                }
                else if (substr.length() == 1)
                {
                    substr = "000" + substr;
                }
                else if (substr.length() == 0)
                {
                    substr = "0000";
                }
                returnIP = returnIP + substr;
            }
            returnIP = returnIP.substring(1);
            returnIP = returnIP.toUpperCase();
            logger.info("LateIPV6 return:" + returnIP);
            return returnIP;
        }
        catch (Exception e)
        {
            return "error";
        }
    }
    /**************************commonfunction method end***************************/
}



