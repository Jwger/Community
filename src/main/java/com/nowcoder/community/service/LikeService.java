package com.nowcoder.community.service;

import com.nowcoder.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    @Autowired
    private RedisTemplate redisTemplate;

    //点赞
    public void like(int userId, int entityType, int entityId, int entityUserId) {
/*        //拼出来存的KEY
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);

        Boolean isMember = redisTemplate.opsForSet().isMember(entityLikeKey, userId);

        if (isMember) {
            redisTemplate.opsForSet().remove(entityLikeKey, userId);

        } else {
            redisTemplate.opsForSet().add(entityLikeKey, userId);
        }
    }*/

        //重构
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
                String userLikeKey = RedisKeyUtil.getUserLikeKey(entityUserId);
                //当前实体 有没有对这个点过赞
                boolean isMember = operations.opsForSet().isMember(entityLikeKey, userId);
                operations.multi();

                if (isMember) {
                    //取消赞
                    operations.opsForSet().remove(entityLikeKey, userId);
                    operations.opsForValue().decrement(userLikeKey);
                } else {
                    operations.opsForSet().add(entityLikeKey, userId);
                    operations.opsForValue().increment(userLikeKey);
                }

                return operations.exec();
            }
        });
    }

    //统计数量，查询某个用户获得的数量
    public int findUserLikeCount(int UserId) {
        String userLikeKey = RedisKeyUtil.getUserLikeKey(UserId);
        Integer count = (Integer) redisTemplate.opsForValue().get(userLikeKey);
        return count == null ? 0 : count.intValue();
    }


    //查询某实体点赞的数量
    public long findEntityLikeCount(int entityType, int entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);

        return redisTemplate.opsForSet().size(entityLikeKey);
    }

    //查询实体点赞状态
    public int findEntityLikeStatus(int userId, int entityType, int entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);

        return redisTemplate.opsForSet().isMember(entityLikeKey, userId) ? 1 : 0;
    }
}
