package top.ashman.wx.application;

import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * @author singoasher
 * @date 2018/1/30
 */
@FeignClient(name = "odd")
public interface OddService {

}
