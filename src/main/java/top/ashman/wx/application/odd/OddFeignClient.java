package top.ashman.wx.application.odd;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import top.ashman.wx.application.odd.task.TaskCommand;
import top.ashman.wx.application.odd.task.TaskDTO;

import java.util.List;

/**
 * @author singoasher
 * @date 2018/2/8
 */
@FeignClient(name = "odd")
public interface OddFeignClient {

    /**
     * Create Task
     *
     * @param taskCommand Create Command
     */
    @RequestMapping(path = "/v1/task", method = RequestMethod.POST)
    void create(@RequestBody TaskCommand taskCommand);

    /**
     * List all tasks
     *
     * @return All task
     */
    @RequestMapping(path = "/v1/tasks", method = RequestMethod.GET)
    List<TaskDTO> list();

    /**
     * Remove all expired
     */
    @RequestMapping(path = "/v1/tasks/status/expired", method = RequestMethod.DELETE)
    void removeAllExpired();

    /**
     * Active all expired
     */
    @RequestMapping(path = "/v1/tasks/status/expired", method = RequestMethod.PUT)
    void activeAllExpired();
}
