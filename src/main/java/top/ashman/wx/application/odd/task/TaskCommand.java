package top.ashman.wx.application.odd.task;

import lombok.Builder;
import lombok.Data;

/**
 * @author singoasher
 * @date 2018/1/29
 */
@Data
@Builder
public class TaskCommand {
    private String content;
}
