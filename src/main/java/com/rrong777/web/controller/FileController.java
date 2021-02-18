package com.rrong777.web.controller;


import com.rrong777.dto.FileInfo;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;

@RestController
@RequestMapping("/file")
public class FileController {
    private String folder = "D:\\data\\workspace\\demo-security\\src\\main\\java\\com\\rrong777\\web\\controller";

    /**
     * 直接上传文件名 例如
     * http://localhost:8070/file/1613288761763
     * 可直接下载1613288761763.txt  文件名就是text.txt
     * @param id
     * @param request
     * @param response
     */
    @GetMapping("/{id}")
    public void download(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) {
        // try后面跟小括号,是jdk7以后的新语法。把流声明在try里面的括号里。方法运行完之后虚拟机会自动帮你把try后面
        // 括号里的流关闭掉。可以简化一点代码.
        try(InputStream inputStream = new FileInputStream(new File(folder, id+".txt"));
                OutputStream outputStream = response.getOutputStream();) {
            response.setContentType("application/x-download");
            // 读到程序里的文件名是id.txt , 但是下面可以指定下载的文件名
            response.addHeader("Content-Disposition", "attachment;filename=test.txt");
            // commons-io的工具类方法,把输入流copy到输出流里面来.把文件的内容写到响应里面来.
            IOUtils.copy(inputStream, outputStream);
            outputStream.flush();
        }catch (Exception e) {

        }
    }
    /**
     * 这里形参名 file 要和whenUploadSuccess()
     * @param file
     * @return
     */
    @PostMapping
    public FileInfo upload(MultipartFile file) throws IOException {
        System.out.println(file.getName());
        System.out.println(file.getOriginalFilename());
        System.out.println(file.getSize());
        File localFile = new File(folder,new Date().getTime() + ".txt");
        // 把上传上来的这个文件转换成本地文件
        file.transferTo(localFile);
        // 这个例子中把上传的文件写到本地的文件夹里面,实际的应用中,可能不会写到本地的一个文件中.可能是阿里的oss服务,也可以是自己搭了一个
        // 文件服务
//        file.getInputStream() 可以用上传的文件得到一个流,把他写到任何的地方去
        return new FileInfo(localFile.getAbsolutePath());
    }
}
