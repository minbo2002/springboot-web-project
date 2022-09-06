package org.zerock.ex2.guestbook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Data
@AllArgsConstructor
public class UploadResultDTO implements Serializable {

    private String fileName;
    private String uuid;
    private String folderPath;

    // 실제 파일과 관련된 모든 정보를 갖는데 나중에 전체 경로가 필요한 경우를 대비해서 getImageURL 제공
    public String getImageURL() {
        try {
            return URLEncoder.encode(folderPath + "/" + uuid + fileName, "UTF-8");
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
