package top.ashman.wx.application.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import top.ashman.wx.domain.model.task.TaskCommand;

/**
 * @author singoasher
 * @date 2018/1/30
 */
@FeignClient(name = "odd")
public interface OddClient {
    /**
     * 新建任务
     * @param taskCommand
     */
    @PostMapping
    void create(@RequestBody TaskCommand taskCommand);
}
