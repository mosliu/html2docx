package com.sddzinfo.html2docx.common.jpa.service;

import com.sddzinfo.html2docx.common.jpa.dto.PageParameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * All rights Reserved, Designed By www.xaaef.com
 * <p>
 * 通用 service 实现类
 * </p>
 *
 * @author Wang Chen Chen <932560435@qq.com>
 * @version 2.0
 * @date 2019/4/18 11:45
 * @copyright 2019 http://www.xaaef.com/ Inc. All rights reserved.
 */

@Slf4j
public class BaseServiceImpl<T, ID, R extends JpaRepository<T, ID>> implements BaseService<T, ID> {

    @Autowired
    protected R repository;

//    @Override
//    public PageInfo<T> findAllPage(QueryParameter parameter) {
//        log.info("findAll parameter: {}", parameter);
//        PageInfo<T> pageInfo = PageHelper
//                .startPage(parameter.getPageNum(), parameter.getPageSize())
//                .doSelectPageInfo(() -> baseMapper.selectAll());
//        return pageInfo;
//    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }


    @Override
    public Iterable<T> findAll(Sort sort) {
        return repository.findAll(sort);
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public List<T> findAll(T entity) {
        log.info("findAll entity: {}", entity);
        if (entity == null) {
            return repository.findAll();
        }
        return findAll(entity, null);
//        return repository.select(entity);
    }

    @Override
    public List<T> findAll(T entity, ExampleMatcher matcher) {

        if (entity == null) {
            return repository.findAll();
        }
        if (matcher == null) {
            matcher = ExampleMatcher.matching() //构建对象
                    .withIgnoreNullValues()
//                .withIncludeNullValues();
                    .withIgnoreCase(false)
//                .withIgnorePaths("id"); // ignore primitives as they default to 0
//                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.startsWith()) //姓名采用“开始匹配”的方式查询
//                .withIgnorePaths("focus");  //忽略属性：是否关注。因为是基本类型，需要忽略掉
            ;
        }
        log.info("findAll entity[{}] with matcher: {}", entity, matcher);
        Example<T> example = Example.of(entity, matcher);
        return repository.findAll(example);
    }

    @Override
    public Page<T> findAllPage(PageParameter parameter) {
        log.info("findAll parameter: {}", parameter);
        int pageNum = parameter.getPageNum() - 1;
        pageNum = pageNum < 0 ? 0 : pageNum;
        parameter.setPageNum(pageNum);

        Pageable of = PageRequest.of(parameter.getPageNum(), parameter.getPageSize());
        Page<T> all = repository.findAll(of);
//        PageInfo<T> pageInfo = PageHelper
//                .startPage(parameter.getPageNum(), parameter.getPageSize())
//                .doSelectPageInfo(() -> baseMapper.selectAll());
        return all;
    }

    @Override
    public T findById(ID id) {
        log.info("findById id: {}", id);
        if (id == null) {
            return null;
        }
        return repository.findById(id).orElse(null);
//        return repository.selectByPrimaryKey(id);
    }


    @Override
    public T find(T entity) {
//        if (entity == null) return null;
        return find(entity, null);
    }

    @Override
    public Iterable<T> findAllById(Iterable<ID> ids) {
        return repository.findAllById(ids);
    }

    @Override
    public T find(T entity, ExampleMatcher matcher) {
        if (entity == null) return null;

        if (matcher == null) {
            matcher = ExampleMatcher.matching() //构建对象
                    .withIgnoreNullValues()
//                .withIncludeNullValues();
                    .withIgnoreCase(false)
//                .withIgnorePaths("id"); // ignore primitives as they default to 0
//                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.startsWith()) //姓名采用“开始匹配”的方式查询
//                .withIgnorePaths("focus");  //忽略属性：是否关注。因为是基本类型，需要忽略掉
            ;
        }
        log.info("find entity[{}] with matcher: {}", entity, matcher);
        Example<T> example = Example.of(entity, matcher);
        return repository.findOne(example).orElse(null);
//        return repository.selectOne(entity);
//        log.info("find entity: {}", entity);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public T save(T entity) {
        if (entity != null) {
//        entity.setCreateTime(LocalDateTime.now());
//        entity.setLastUpdateTime(LocalDateTime.now());
            log.info("save entity: {}", entity);
            return repository.save(entity);
//            return repository.insertUseGeneratedKeys(entity);
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<T> batchSave(List<T> list) {
        if (list == null) {
            return null;
        }
//        list.forEach(res -> {
//            res.setCreateTime(LocalDateTime.now());
//            res.setLastUpdateTime(LocalDateTime.now());
//        });
        log.info("batchCreate list: {}", list);
        return repository.saveAll(list);
//        return repository.insertList(list);
    }

//    @Transactional(rollbackFor = Exception.class)
//    @Override
//    public int update(T entity) {
//        entity.setLastUpdateTime(LocalDateTime.now());
//        log.info("update entity: {}", entity);
//        return repository.updateByPrimaryKeySelective(entity);
//    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(ID id) {
        log.info("deleteById id: {}", id);
        repository.deleteById(id);
//        return repository.deleteByPrimaryKey(id);
    }

//    @Transactional(rollbackFor = Exception.class)
//    @Override
//    public int delete(T entity) {
//        log.info("delete entity: {}", entity);
//        return repository.delete(entity);
//    }

    @Override
    public long count(T entity) {

//        log.info("count entity: {}", entity);
//        return repository.selectCount(entity);
        return count(entity, null);
    }

    @Override
    public long count(T entity, ExampleMatcher matcher) {
        if (entity == null) {
            return repository.count();
//            return 0;
        }

        if (matcher == null) {
            matcher = ExampleMatcher.matching() //构建对象
                    .withIgnoreNullValues()
//                .withIncludeNullValues();
                    .withIgnoreCase(false)
//                .withIgnorePaths("id"); // ignore primitives as they default to 0
//                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.startsWith()) //姓名采用“开始匹配”的方式查询
//                .withIgnorePaths("focus");  //忽略属性：是否关注。因为是基本类型，需要忽略掉
            ;
        }
        log.info("count entity[{}] with matcher: {}", entity, matcher);
        Example<T> example = Example.of(entity, matcher);
        return repository.count(example);
//        return repository.findOne(example).orElse(null);
//        return repository.selectOne(entity);
//        log.info("find entity: {}", entity);
    }

    @Override
    public boolean existsWithPrimaryKey(ID id) {
        log.info("existsWithPrimaryKey id: {}", id);
        return repository.existsById(id);
//        return repository.existsWithPrimaryKey(id);
    }
}
