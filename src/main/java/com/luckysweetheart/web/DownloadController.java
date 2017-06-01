package com.luckysweetheart.web;

import com.luckysweetheart.common.IdWorker;
import com.luckysweetheart.dto.StoreDataDTO;
import com.luckysweetheart.exception.StorageException;
import com.luckysweetheart.store.StoreService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;

/**
 * Created by yangxin on 2017/5/26.
 */
@Controller
public class DownloadController extends BaseController {

    @Resource
    private StoreService storeService;

    @Resource
    private IdWorker idWorker;

    @RequestMapping("/download")
    public void downloadFile(String cosPath) {
        byte bs[];
        StoreDataDTO dto = storeService.getByCosPath(cosPath);
        String fileName;
        if (dto != null) {
            fileName = dto.getFileName();
        } else {
            fileName = idWorker.nextId() + ".png";
        }
        try {
            bs = storeService.download(cosPath);
            if (bs != null) {
                response.setContentType("application/x-download");
                response.setCharacterEncoding("utf-8");

                String userAgent = request.getHeader("User-Agent").toLowerCase();
                String strHeader = "";
                /**
                 * 	1.  IE浏览器，采用URLEncoder编码
                 *	2.  Opera浏览器，采用filename*方式
                 *	3.  Safari浏览器，采用ISO编码的中文输出
                 *	4.  Chrome浏览器，采用Base64编码或ISO编码的中文输出
                 *	5.  FireFox浏览器，采用Base64或filename*或ISO编码的中文输出
                 *
                 * */
                //IE使用URLEncoder
                if (userAgent.contains("windows")) {
                    strHeader = "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8");
                    //其他使用转iso
                } else {
                    strHeader = "attachment;filename=" + new String((fileName).getBytes("utf-8"), "ISO8859-1");
                }
                response.addHeader("Content-Disposition", strHeader);
                OutputStream os;
                os = response.getOutputStream();
                os.write(bs);
                os.flush();
                os.close();
            }
        } catch (Exception e) {
            logger.error("下载原始文档时出现异常", e);
        }
    }

}
