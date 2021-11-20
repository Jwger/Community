package com.nowcoder.community.controller;

import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements CommunityConstant {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title, String content) {
        User user = hostHolder.getUser();
        if (user == null) {
            return CommunityUtil.getJSONString(403, "你还没有登录哦!");
        }

        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle(title);
        post.setContent(content);
        post.setCreateTime(new Date());
        discussPostService.addDiscussPost(post);

        // 报错的情况,将来统一处理.
        return CommunityUtil.getJSONString(0, "发布成功!");
    }

    @RequestMapping(path = "/detail/{DiscussPostId}", method = RequestMethod.GET)
    //在这里写进去一个page springmvc会自动把我的数据传进model里面 直接调用model可以调用
    public String getDiscussPost(@PathVariable("DiscussPostId") int discussPostId, Model model, Page page) {
        //查帖子
        DiscussPost post = discussPostService.findDiscussPostById(discussPostId);
        model.addAttribute("post", post);
        //查作者

        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user", user);

        //查评论的分页信息
        page.setLimit(5);
        page.setPath("/discuss/detail/" + discussPostId);
        //一共有多少条数据 数据库有这个
        page.setRows(post.getCommentCount());

        //评论 ： 给帖子的评论
        //回复 ： 给评论给的评论

        //评论列表
        List<Comment> commentList = commentService.findCommentsByEntity(
                ENTITY_TYPE_POST, post.getId(), page.getOffset(), page.getLimit());

        //评论的vo列表，全部的
        List<Map<String, Object>> commentVoList = new ArrayList<>();
        //遍历集合，把数据取出来
        if (commentList != null) {
            for (Comment comment : commentList) {
                //一个评论的vo
                Map<String, Object> commentVo = new HashMap<>();
                //往vo里面添加评论
                commentVo.put("comment", comment);
                //往vo中添加评论作者
                commentVo.put("user", userService.findUserById(comment.getUserId()));

                //评论的评论 多个，也是列表
                List<Comment> replyList = commentService.findCommentsByEntity(ENTITY_TYPE_COMMENT,
                        comment.getId(), 0, Integer.MAX_VALUE);
                //一个回复的vo列表
                List<Map<String, Object>> replyVoList = new ArrayList<>();
                if (replyList != null) {
                    for (Comment reply : replyList) {
                        //一个回复的vo
                        Map<String, Object> replyVo = new HashMap<>();
                        //往vo里面添加回复
                        replyVo.put("reply", reply);
                        //往vo中添加评论作者
                        replyVo.put("user", userService.findUserById(reply.getUserId()));

                        //回复目标，查他的user 有可能没有，没有就是回复帖子的，有是回复用户的
                        //target == 0?
                        User target = reply.getTargetId() == 0 ? null : userService.findUserById(reply.getTargetId());
                        replyVo.put("target", target);

                        replyVoList.add(replyVo);
                    }
                }
                commentVo.put("replys", replyVoList);
                //有几个回复 数量
                int replyCount = commentService.findCommentCount(ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("replyCount", replyCount);

                commentVoList.add(commentVo);
            }
        }
        model.addAttribute("comments", commentVoList);
        return "/site/discuss-detail";
    }
}
