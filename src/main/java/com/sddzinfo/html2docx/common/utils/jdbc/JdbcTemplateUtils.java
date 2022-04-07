package com.sddzinfo.html2docx.common.utils.jdbc;

import com.sddzinfo.html2docx.entity.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.lang.NonNull;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2021-11-01
 **/
@Slf4j
public class JdbcTemplateUtils {
    /**
     * 只查询一列数据类型对象。用于只有一行查询结果的数据
     *
     * @param sql
     * @param params
     * @param cla    Integer.class,Float.class,Double.Class,Long.class,Boolean.class,Char.class,Byte.class,Short.class
     * @return
     */
    public static Object queryOneColumnForSingleRow(JdbcTemplate jdbcTemplate, String sql, Object[] params, Class cla) {
        Object result = null;
        try {
            if (params == null || params.length > 0) {
                result = jdbcTemplate.queryForObject(sql, cla, params);
            } else {
                result = jdbcTemplate.queryForObject(sql, cla);
            }
        } catch (Exception ex) {
            log.error("数据库访问期间出现错误，", ex);
        }
        return result;
    }

    /**
     * 查询返回实体对象集合
     *
     * @param sql    sql语句
     * @param params 填充sql问号占位符数
     * @param cla    实体对象类型
     * @return
     */
    public static <T> List<T> queryForObjectList(JdbcTemplate jdbcTemplate, String sql, Object[] params, final Class<T> cla) {
        final List<T> list = new ArrayList<>();
        try {
            jdbcTemplate.query(sql, new RowCallbackHandler() {
                public void processRow(ResultSet rs) {
                    try {
                        List<String> columnNames = new ArrayList<String>();
                        ResultSetMetaData meta = rs.getMetaData();
                        int num = meta.getColumnCount();
                        for (int i = 0; i < num; i++) {
                            columnNames.add(meta.getColumnLabel(i + 1).toLowerCase().trim());
                        }
                        Method[] methods = cla.getMethods();
                        List<String> fields = new ArrayList<String>();
                        for (int i = 0; i < methods.length; i++) {
                            if (methods[i].getName().trim().startsWith("set")) {
                                String f = methods[i].getName().trim().substring(3);
                                f = (f.charAt(0) + "").toLowerCase().trim() + f.substring(1);
                                fields.add(f);
                            }
                        }
                        do {
                            T obj = null;
                            try {
                                obj = cla.getConstructor().newInstance();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            for (int i = 0; i < num; i++) {
                                Object objval = rs.getObject(i + 1);
                                for (int n = 0; n < fields.size(); n++) {
                                    String fieldName = fields.get(n).trim();
                                    if (columnNames.get(i).equals(fieldName.toLowerCase().trim())) {
                                        BeanUtils.copyProperty(obj, fieldName, objval);
                                        break;
                                    }
                                }
                            }
                            list.add(obj);
                        } while (rs.next());
                    } catch (Exception ex) {
                        log.error("数据库访问期间出现错误，", ex);
                    }
                }
            }, params);
        } catch (Exception ex) {
            log.error("数据库访问期间出现错误，", ex);
        }
        if (list.size() <= 0) {
            return null;
        }
        return list;
    }

    /**
     * 查询返回List<Map<String,Object>>格式数据,每一个Map代表一行数据，列名为key
     *
     * @param sql    sql语句
     * @param params 填充问号占位符数
     * @return
     */
    public static List<Map<String, Object>> queryForMaps(JdbcTemplate jdbcTemplate, String sql, Object[] params) {
        try {
            if (params != null && params.length > 0) {
                return jdbcTemplate.queryForList(sql, params);
            }
            return jdbcTemplate.queryForList(sql);
        } catch (Exception ex) {
            log.error("数据库访问期间出现错误，", ex);
        }
        return null;
    }

    /**
     * 查询分页（MySQL数据库）
     *
     * @param sql      终执行查询的语句
     * @param params   填充sql语句中的问号占位符数
     * @param page     想要第几页的数据
     * @param pageSize 每页显示多少条数
     * @param cla      要封装成的实体元类型
     * @return pageList对象
     */
    public static <T> Page<T> queryByPageForMySQL(JdbcTemplate jdbcTemplate, String sql, Object[] params, int page, int pageSize, Class<T> cla) throws ClassNotFoundException {
        return queryByPageForMySQL(jdbcTemplate, sql, params, page, pageSize, cla, true);
    }

    /**
     * 查询分页（MySQL数据库）
     *
     * @param sql         终执行查询的语句
     * @param params      填充sql语句中的问号占位符数
     * @param page        想要第几页的数据
     * @param pageSize    每页显示多少条数
     * @param cla         要封装成的实体元类型
     * @param isCalcPages
     * @return pageList对象
     */
    @NonNull
    public static <T> Page<T> queryByPageForMySQL(JdbcTemplate jdbcTemplate, String sql, Object[] params, int page, int pageSize, Class<T> cla, boolean isCalcPages) throws ClassNotFoundException {
        if (cla == null) {
            throw new ClassNotFoundException("Class must not null");
        }
//        String rowsql = "select count(*) from (" + sql + ") _tempTableName";   //查询总行数sql
        //这个导致数据量过大。
        int selectPos = sql.toLowerCase().indexOf("select");
        int fromPos = sql.toLowerCase().indexOf("from");
        String rowsql = "select count(*) " + sql.substring(fromPos);   //查询总行数sql


        int pages = 0;   //总页数
        Integer rows = -1;  //查询总行数
        if (isCalcPages) {
            log.info("获取行数SQL：{}", rowsql);
            rows = (Integer) queryOneColumnForSingleRow(jdbcTemplate, rowsql, params, Integer.class);
//            if (rows == null) {
//                //快速失败
//                return new Page<>();
//            }
        }

        //判断页数,如果是页大小的整数倍就为rows/pageRow如果不是整数倍就为rows/pageRow+1

        //查询第page页的数据sql语句
        if (page <= 1) {
            sql += " limit 0," + pageSize;
        } else {
            sql += " limit " + ((page - 1) * pageSize) + "," + pageSize;
        }
        //查询第page页数据
        List<T> list = null;
        log.info("获取数据SQL：{}", sql);
        list = queryForObjectList(jdbcTemplate, sql, params, cla);

        //返回分页格式数据
        Page<T> tPage = new Page<>();
        tPage.setPageNo(page);  //设置显示的当前页数
//        tPage.setPages(pages);  //设置总页数
        tPage.setPageSize(pageSize);
        tPage.setList(list);   //设置当前页数据
        if (isCalcPages && rows != null) {
            tPage.setTotalRows(rows);    //设置总记录数
        }
        return tPage;
    }
}
