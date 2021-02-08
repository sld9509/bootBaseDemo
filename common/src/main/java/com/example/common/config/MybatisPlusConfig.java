package com.example.common.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.example.common.enumeration.DeleteEnum;
import com.example.common.enumeration.StatusEnum;
import org.apache.http.client.utils.DateUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * MyBatis配置类
 * @author 孙灵达
 * @date 2020-12-30
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * 分页插件-指定数据库
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor().setDbType(DbType.MYSQL);
    }

    /**
     * 乐观锁
     */
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }

    /**
     * 自动填充-插入和更新
     */
    @Component
    public static class MyMetaObjectHandler implements MetaObjectHandler {

        @Override
        public void insertFill(MetaObject metaObject) {
            String now = DateUtils.formatDate(new Date(), "YYYYMMddHHmmss");
            this.strictInsertFill(metaObject, "createTime", String.class, now);
            this.strictUpdateFill(metaObject, "updateTime", String.class, now);
            this.strictInsertFill(metaObject, "isDeleted", Integer.class, DeleteEnum.NO.getCode());
            this.strictInsertFill(metaObject, "status", Integer.class, StatusEnum.VALID.getCode());
            this.strictInsertFill(metaObject, "lockVersion", Integer.class, 0);
        }

        @Override
        public void updateFill(MetaObject metaObject) {
            String now = DateUtils.formatDate(new Date(), "YYYYMMddHHmmss");
            this.strictUpdateFill(metaObject, "updateTime", String.class, now);
        }
    }
}
