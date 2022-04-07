package com.sddzinfo.html2docx.common.jpa.controller;

import com.sddzinfo.html2docx.common.dto.CommonResponseDto;
import com.sddzinfo.html2docx.common.jpa.dto.PageParameter;
import com.sddzinfo.html2docx.common.jpa.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sddzinfo.html2docx.common.jpa.utils.MyBeanUtils.copyPropertiesIgnoreNull;


/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2021-06-10
 **/
@Slf4j
public class BaseController<T, ID, S extends BaseService<T, ID>> {
    @Autowired
    protected S baseService;

    /**
     * 根据Id查询
     */
    @GetMapping("/{id}")
    public CommonResponseDto findById(@PathVariable("id") ID id) {
        log.info("get ID : {}", id);
        return CommonResponseDto.success(baseService.findById(id));
    }

    /**
     * 查询所有
     *
     * @return
     */
    @GetMapping("/all")
    public CommonResponseDto findAll() {
        return CommonResponseDto.success(baseService.findAll());
    }

    @GetMapping("/page")
    public CommonResponseDto findAllByPage(PageParameter parameter) {
        Page<T> allPage = baseService.findAllPage(parameter);

//        log.info("{}", allPage.getContent());
        List<T> content = allPage.getContent();
        Map<String, Object> rtnM = new HashMap<>();
        rtnM.put("list", content);
        rtnM.put("total", allPage.getTotalPages());
        return CommonResponseDto.success(rtnM);
    }


//    /**
//     * 分页 查询所有
//     */
//    @GetMapping("/page")
//    public CommonResponseDto<Page<T>> findAll(QueryParameter parameter) {
//        PageInfo<T> page = baseService.findAllPage(parameter);
//        return CommonResponseDto.success(page.getSize(), page.getTotal(), page.getList());
//    }

    /**
     * 新增 不需要添加id
     */
    @PostMapping()
    public CommonResponseDto create(@RequestBody T entity) {
        log.info("create:  {}", entity);
        try {
            T find = baseService.find(entity);


            T save = baseService.save(entity);
            return CommonResponseDto.success(save);
        } catch (Exception e) {
            return CommonResponseDto.fail(e.getMessage());
        }
    }

    /**
     * 修改必须要id
     *
     * @param entity
     * @return
     */
    @PutMapping()
    public CommonResponseDto update(@RequestBody T entity) {

        log.info("update:  {}", entity);

        try {
            //反射获取id
            Class<?> aClass = entity.getClass();
            Field idField = null;
            idField = aClass.getDeclaredField("id");
            idField.setAccessible(true);
            Object o = idField.get(entity);
            T byId = baseService.findById((ID) o);
            //仅复制非空属性
            copyPropertiesIgnoreNull(entity, byId);
            entity = byId;

        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.error("反射获取id失败", e);
        } catch (Exception otherException) {
            log.error("反射获取id失败", otherException);
        }


        try {
            T save = baseService.save(entity);
            return CommonResponseDto.success(save);
        } catch (Exception e) {
            return CommonResponseDto.fail(e.getMessage());
        }
    }

    /**
     * 删除 只需要id即可
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public CommonResponseDto delete(@PathVariable("id") ID id) {
        log.info("delete:  {}", id);
        try {
            baseService.deleteById(id);
            return CommonResponseDto.success("deleted!");
        } catch (Exception e) {
            return CommonResponseDto.fail(e.getMessage());
        }
    }
}
