package top.ashman.wx.application.odd.task;

import lombok.Builder;
import lombok.Data;

/**
 * @author singoasher
 * @date 2018/1/31
 */
@Data
@Builder
public class TaskDTO {
    private String id;
    private String userId;
    private String content;
    private String createdTime;
    private String lastModifiedTime;
}
