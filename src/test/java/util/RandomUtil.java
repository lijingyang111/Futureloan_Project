package util;

import com.sun.corba.se.impl.orb.ParserTable;

import java.util.Random;

public class RandomUtil {

    public static String phoneUtil(){
        String phone = "186";
        Random random =new Random();
        for(int i=0;i<8;i++){
            phone= phone+random.nextInt(9);
        }
        return phone;
    }
    public static String getPhone(){
        String phone = "";
        while(true){
            phone = phoneUtil();
            String sql = "select count(*) from member where mobile_phone = '"+phone+"'";
            String count = DBUtil.queryOneField(sql).toString();
            if("0".equals(count)){
                break;
            }else
                continue;
        }

        return phone;
    }


    public static void main(String args[]){
        System.out.println(phoneUtil());
    }
}
