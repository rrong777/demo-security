package com.rrong777.utils.code.sms;

/**
 * @author wuqr
 * @Title: 验证码工具类
 * @Description: 生成验证码等
 * @date 2020/4/19 13:40
 */
public class VerificationCodeUtils {
    /**
     * 获得一个10的倍数
     * @return
     */
    public static Integer getMultiple(int length) {
        int multiple = 1;
        for(int i = length; i > 0; i--) {
            multiple *= 10;
        }
        return multiple;
    }

    /**
     * 生成六位随机验证码
     * @return 验证码
     */
    public static String generateVerificationCode() {
        // 0 < 1 Math.random() + 1 全是1开头的了
        Integer code = 0;
        while (code == 0) {
            // 重新生成
            code = (int)(Math.random() * 1000000);
        }
        // multiple必须是double 如果500001这样 int强转就会等于1
        // 运算必须有一个强转成double参与运算 否则 500001 得出的结果1.0 !> 1
        // 如果刚好是100000就会是1
        double multiple = (double) 100000 / code;
        if(multiple > 1) {
            // 强转回int类型 只取整数倍数大小，
            code = code * getMultiple(((int)multiple+"").length());
        }
        return code.toString();
    }
}
