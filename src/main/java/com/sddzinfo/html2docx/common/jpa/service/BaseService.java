package com.sddzinfo.html2docx.common.jpa.service;

import com.sddzinfo.html2docx.common.jpa.dto.PageParameter;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * All rights Reserved, Designed By www.xaaef.com
 * <p>
 * 通用 service 接口
 * </p>
 * <p>
 *
 * @version 2.0
 * @copyright 2019 http://www.xaaef.com/ Inc. All rights reserved.
 */

public interface BaseService<T, ID> {

//    /**
//     * 分页查询
//     *
//     * @param parameter
//     * @return PageInfo<T>
//     
//     */
//    PageInfo<T> findAllPage(QueryParameter parameter);


    List<T> findAll();

    Iterable<T> findAll(Sort sort);

    Page<T> findAll(Pageable pageable);

    List<T> findAll(T entity);
    /**
     * 查看所有数据，根据条件查询
     *
     * @param entity
     * @param matcher ExampleMatcher
     * @return List<T>
     */
    List<T> findAll(T entity, ExampleMatcher matcher);

    Page<T> findAllPage(PageParameter parameter);

    /**
     * 根据ID查询数据
     *
     * @param id
     * @return T
     */
    T findById(ID id);

    /**
     * 根据条件查询，只返回一条数据
     *
     * @param entity
     * @return T
     */
    T find(T entity);

    Iterable<T> findAllById(Iterable<ID> ids);

    /**
     * 根据条件查询，只返回一条数据
     *
     * @param entity
     * @param matcher ExampleMatcher
     * @return T
     */
    T find(T entity, ExampleMatcher matcher);

    /**
     * 新增数据
     *
     * @param entity
     * @return int
     */
    T save(T entity);

    /**
     * 批量新增数据， 返回 新增的数量
     *
     * @param list
     * @return int
     */
    List<T> batchSave(List<T> list);

//    /**
//     * 修改数据，必须带 ID ，返回 被修改的数量
//     *
//     * @param entity
//     * @return int
//     */
//    int update(T entity);

    /**
     * 根据ID删除数据，返回 删除的数量
     *
     * @param id
     * @return int
     */
    void deleteById(ID id);

//    /**
//     * 根据条件删除，返回 删除的数量
//     *
//     * @param entity
//     * @return int
//     */
//    int delete(T entity);

    /**
     * 根据ID判断数据是否存在
     *
     * @param entity
     * @return int
     */
    long count(T entity);

    /**
     * 根据ID判断数据是否存在
     *
     * @param entity
     * @param matcher ExampleMatcher
     * @return int
     */
    long count(T entity, ExampleMatcher matcher);

    /**
     * 根据ID判断数据是否存在
     *
     * @param id
     * @return boolean
     */
    boolean existsWithPrimaryKey(ID id);

}
