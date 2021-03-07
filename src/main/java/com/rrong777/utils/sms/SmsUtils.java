package com.rrong777.utils.sms;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.rrong777.model.http.HttpResult;

public class SmsUtils {
    public static HttpResult SendVerificationCodeBySms(String phoneNum, String verificationCode) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI4G53wu5hgkb5nRSsunNP", "2mK6R5CVmoVfLu4RSyeRUTpAOQzAAS");
        IAcsClient client = new DefaultAcsClient(profile);

        String templateParam = "{\"code\":" +"\"" + verificationCode + "\"}";

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phoneNum);
        request.putQueryParameter("SignName", "loveletter");
        request.putQueryParameter("TemplateCode", "SMS_187756479");
        request.putQueryParameter("TemplateParam", templateParam);
        CommonResponse response = null;
        try {
            response = client.getCommonResponse(request);
            // TODO: 2020/4/20  手机号码错误验证
            return HttpResult.ok(response.getData());
        } catch (ServerException e) {
            return HttpResult.error(502,e.getErrMsg());
        } catch (ClientException e) {
            return HttpResult.error(501, e.getErrMsg());
        }
    }


}
