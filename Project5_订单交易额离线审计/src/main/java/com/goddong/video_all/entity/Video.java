package com.goddong.video_all.entity;

import java.io.Serializable;

/**
 * (Video)实体类
 *
 * @author makejava
 * @since 2020-04-05 19:05:22
 */
public class Video implements Serializable {
    private static final long serialVersionUID = 685607844181891670L;
    /**
    * 视频id
    */
    private String id;
    /**
    * 标题
    */
    private String title;
    /**
    * 描述
    */
    private String desp;
    /**
    * 最新弹幕数量
    */
    private Integer danmu;
    /**
    * 金币数量
    */
    private Integer coin;
    /**
    * 不喜欢
    */
    private Integer dislike;
    /**
    * 收藏
    */
    private Integer favorite;
    /**
    * 最高排名
    */
    private Integer hisRank;
    /**
    * 点赞数
    */
    private Integer likeCount;
    /**
    * 目前排名
    */
    private Integer nowRank;
    /**
    * 回复数
    */
    private Integer reply;
    /**
    * 分享数
    */
    private Integer share;
    /**
    * 观看数
    */
    private Integer view;
    /**
    * 弹幕内容
    */
    private String danmuku;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesp() {
        return desp;
    }

    public void setDesp(String desp) {
        this.desp = desp;
    }

    public Integer getDanmu() {
        return danmu;
    }

    public void setDanmu(Integer danmu) {
        this.danmu = danmu;
    }

    public Integer getCoin() {
        return coin;
    }

    public void setCoin(Integer coin) {
        this.coin = coin;
    }

    public Integer getDislike() {
        return dislike;
    }

    public void setDislike(Integer dislike) {
        this.dislike = dislike;
    }

    public Integer getFavorite() {
        return favorite;
    }

    public void setFavorite(Integer favorite) {
        this.favorite = favorite;
    }

    public Integer getHisRank() {
        return hisRank;
    }

    public void setHisRank(Integer hisRank) {
        this.hisRank = hisRank;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getNowRank() {
        return nowRank;
    }

    public void setNowRank(Integer nowRank) {
        this.nowRank = nowRank;
    }

    public Integer getReply() {
        return reply;
    }

    public void setReply(Integer reply) {
        this.reply = reply;
    }

    public Integer getShare() {
        return share;
    }

    public void setShare(Integer share) {
        this.share = share;
    }

    public Integer getView() {
        return view;
    }

    public void setView(Integer view) {
        this.view = view;
    }

    public String getDanmuku() {
        return danmuku;
    }

    public void setDanmuku(String danmuku) {
        this.danmuku = danmuku;
    }

}