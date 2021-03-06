package com.rrong777.utils.code;

import com.rrong777.web.properties.SecurityProerties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class ImageCodeGenarator implements ValidateCodeGenerator{
    private SecurityProerties securityProerties;
    public ImageCode generate(ServletWebRequest request) {
        // getIntParameter 从请求中取一个 int 参数  参数名叫width  取不到的话给第三个默认值
        int width = ServletRequestUtils.getIntParameter(request.getRequest(), "width", securityProerties.getCode().getImage().getWidth());
        int height = ServletRequestUtils.getIntParameter(request.getRequest(), "height", securityProerties.getCode().getImage().getHeight());
        // 生成一个图片对象
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();

        // 图片对象上生成一些随机的条纹
        Random random = new Random();
        g.setColor(getRandColor(200,250));
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("Times New Roman", Font.ITALIC, 20));
        g.setColor(getRandColor(160, 200));
        for (int i = 0; i < 155; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }

        // 生成4位的随机数
        String sRand = "";
        for (int i = 0; i < securityProerties.getCode().getImage().getLength(); i++) {
            String rand = String.valueOf(random.nextInt(10));
            sRand += rand;
            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            // 写到图片上面
            g.drawString(rand, 13 * i + 6, 16);
        }

        g.dispose();

        return new ImageCode(image, sRand, securityProerties.getCode().getImage().getExpireIn());
    }
    private Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    public SecurityProerties getSecurityProerties() {
        return securityProerties;
    }

    public void setSecurityProerties(SecurityProerties securityProerties) {
        this.securityProerties = securityProerties;
    }
}
