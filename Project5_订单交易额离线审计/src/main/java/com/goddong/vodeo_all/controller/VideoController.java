package com.goddong.vodeo_all.controller;

import com.goddong.vodeo_all.entity.Video;
import com.goddong.vodeo_all.service.VideoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (Video)表控制层
 *
 * @author makejava
 * @since 2020-04-05 19:04:30
 */
@RestController
@RequestMapping("video")
public class VideoController {
    /**
     * 服务对象
     */
    @Resource
    private VideoService videoService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public Video selectOne(String id) {
        return this.videoService.queryById(id);
    }

}