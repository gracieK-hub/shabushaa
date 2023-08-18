package com.unitechs.biz.web.req;

import com.unitechs.biz.rpc.dto.req.SaveWsReq;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author wangc
 * @create 2023/8/14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkSheetSaveReq extends SaveWsReq {
    private String status;
    private String servordercode;
    private String modifytime;
    private String opertime;
    private String source;
}
