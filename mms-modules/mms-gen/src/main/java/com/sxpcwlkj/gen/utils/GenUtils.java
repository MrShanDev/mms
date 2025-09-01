package com.sxpcwlkj.gen.utils;

import cn.hutool.core.text.NamingCase;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.sxpcwlkj.common.exception.MmsException;
import com.sxpcwlkj.gen.config.DbType;
import com.sxpcwlkj.gen.config.GenDataSource;
import com.sxpcwlkj.gen.config.query.AbstractQuery;
import com.sxpcwlkj.gen.entity.TableEntity;
import com.sxpcwlkj.gen.entity.TableFieldEntity;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 代码生成器 工具类
 *
 * @author mmsAdmin
 * @Doc mmsadmin.cn
 */
@Slf4j
public class GenUtils {

    /**
     * 根据数据源，获取全部数据表
     *
     * @param datasource 数据源
     */
    public static List<TableEntity> getTableList(GenDataSource datasource) {
        List<TableEntity> tableList = new ArrayList<>();
        try {
            AbstractQuery query = datasource.getDbQuery();

            //查询数据
            PreparedStatement preparedStatement = datasource.getConnection().prepareStatement(query.tableSql(null));
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                TableEntity table = new TableEntity();
                table.setTableName(rs.getString(query.tableName()));
                table.setTableComment(rs.getString(query.tableComment()));
                table.setDatasourceId(datasource.getId());
                tableList.add(table);
            }

            datasource.getConnection().close();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return tableList;
    }

    /**
     * 根据数据源，获取指定数据表
     *
     * @param datasource 数据源
     * @param tableName  表名
     */
    public static TableEntity getTable(GenDataSource datasource, String tableName) {
        try {
            AbstractQuery query = datasource.getDbQuery();

            // 查询数据
            PreparedStatement preparedStatement = datasource.getConnection().prepareStatement(query.tableSql(tableName));
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                TableEntity table = new TableEntity();
                table.setTableName(rs.getString(query.tableName()));
                table.setTableComment(rs.getString(query.tableComment()));
                table.setTableComment(table.getTableComment().replaceAll("\\s*;\\s*$", ""));
                table.setDatasourceId(datasource.getId());
                return table;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        throw new MmsException("数据表不存在：" + tableName);
    }


    /**
     * 获取表字段列表
     *
     * @param datasource 数据源
     * @param tableId    表ID
     * @param tableName  表名
     */
    public static List<TableFieldEntity> getTableFieldList(GenDataSource datasource, Long tableId, String tableName) {
        List<TableFieldEntity> tableFieldList = new ArrayList<>();

        try {
            AbstractQuery query = datasource.getDbQuery();
            String tableFieldsSql = query.tableFieldsSql();
            if (datasource.getDbType() == DbType.Oracle) {
                DatabaseMetaData md = datasource.getConnection().getMetaData();
                tableFieldsSql = String.format(tableFieldsSql.replace("#schema", md.getUserName()), tableName);
            } else {
                tableFieldsSql = String.format(tableFieldsSql, tableName);
            }
            PreparedStatement preparedStatement = datasource.getConnection().prepareStatement(tableFieldsSql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                TableFieldEntity field = new TableFieldEntity();
                field.setTableId(tableId);
                field.setFieldName(rs.getString(query.fieldName()));
                String fieldType = rs.getString(query.fieldType());
                if (fieldType.contains(" ")) {
                    fieldType = fieldType.substring(0, fieldType.indexOf(" "));
                }
                field.setFieldType(fieldType);
                field.setFieldComment(rs.getString(query.fieldComment()));
                String key = rs.getString(query.fieldKey());
                field.setPrimaryPk(StringUtils.isNotBlank(key) && "PRI".equalsIgnoreCase(key));

                tableFieldList.add(field);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return tableFieldList;
    }

    /**
     * 获取模块名
     *
     * @param packageName 包名
     * @return 模块名
     */
    public static String getModuleName(String packageName) {
        return StrUtil.subAfter(packageName, ".", true);
    }

    /**
     * 获取功能名，默认使用表名作为功能名
     *
     * @param tableName 表名
     * @return 功能名
     */
    public static String getFunctionName(String tableName) {
        if (tableName == null || tableName.isEmpty()) {
            return tableName;
        }
        // 匹配下划线及紧随的小写字母，将其转为大写
        String regex = "_([a-z])";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher matcher = pattern.matcher(tableName);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 表名转驼峰并移除前后缀
     *
     * @param upperFirst   首字母大写
     * @param tableName    表名
     * @param removePrefix 删除前缀
     * @param removeSuffix 删除后缀
     * @return java.lang.String
     */
    public static String camelCase(boolean upperFirst, String tableName, String removePrefix, String removeSuffix) {
        String className = tableName;
        // 移除前缀
        if (StrUtil.isNotBlank(removePrefix)) {
            className = StrUtil.removePrefix(tableName, removePrefix);
        }
        // 移除后缀
        if (StrUtil.isNotBlank(removeSuffix)) {
            className = StrUtil.removeSuffix(className, removeSuffix);
        }
        // 是否首字母大写
        if (upperFirst) {
            return NamingCase.toPascalCase(className);
        } else {
            return NamingCase.toCamelCase(className);
        }
    }


    /**
     * 执行更新SQL（INSERT/UPDATE/DELETE）
     * @param datasource 数据源
     * @param sql SQL语句
     * @param params SQL参数
     * @return 受影响的行数
     */

    // 示例：增加 编辑 删除
    //    int affectedRows = executeUpdate(datasource,
    //        "UPDATE users SET name = ? WHERE id = ?",
    //        new Object[]{"张三", 1});
    public static int executeUpdate(GenDataSource datasource, String sql, Object[] params) {
        try (Connection conn = datasource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 设置参数
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    pstmt.setObject(i + 1, params[i]);
                }
            }

            return pstmt.executeUpdate();
        } catch (Exception e) {
            log.error("执行更新失败: {}", sql, e);
            throw new MmsException("执行更新操作失败", e);
        }
    }

    /**
     * 批量执行SQL
     * @param datasource 数据源
     * @param sql SQL语句
     * @param batchParams 批量参数列表
     * @return 每个操作的受影响行数数组
     */

    // 示例：批量插入
    //    List<Object[]> batchParams = new ArrayList<>();
    //    batchParams.add(new Object[]{"user1", "user1@example.com"});
    //    batchParams.add(new Object[]{"user2", "user2@example.com"});
    //        int[] batchResult = executeBatch(datasource,
    //            "INSERT INTO users (name, email) VALUES (?, ?)",
    //            batchParams);
    public static int[] executeBatch(GenDataSource datasource, String sql, List<Object[]> batchParams) {
        try (Connection conn = datasource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 禁用自动提交，开启事务
            conn.setAutoCommit(false);

            for (Object[] params : batchParams) {
                if (params != null) {
                    for (int i = 0; i < params.length; i++) {
                        pstmt.setObject(i + 1, params[i]);
                    }
                }
                pstmt.addBatch();
            }

            int[] result = pstmt.executeBatch();
            // 提交事务
            conn.commit();
            // 恢复自动提交
            conn.setAutoCommit(true);

            return result;
        } catch (Exception e) {
            log.error("批量执行失败: {}", sql, e);
            throw new MmsException("批量执行操作失败", e);
        }
    }

    /**
     * 执行查询SQL并返回对象列表
     * @param datasource 数据源
     * @param sql SQL语句
     * @param params SQL参数
     * @param mapper 结果映射器
     * @param <T> 返回类型
     * @return 查询结果列表
     */

    //示例：查询
    //    List<User> users = queryForList(datasource,
    //        "SELECT * FROM users WHERE status = ?",
    //        new Object[]{"active"},
    //        rs -> {
    //            User u = new User();
    //            u.setId(rs.getInt("id"));
    //            u.setName(rs.getString("name"));
    //            return u;
    //        });
    public static <T> List<T> queryForList(GenDataSource datasource, String sql, Object[] params,
                                           ResultSetMapper<T> mapper) {
        List<T> resultList = new ArrayList<>();

        try (Connection conn = datasource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 设置参数
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    pstmt.setObject(i + 1, params[i]);
                }
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    resultList.add(mapper.mapRow(rs));
                }
            }
            return resultList;
        } catch (Exception e) {
            log.error("执行查询失败: {}", sql, e);
            throw new MmsException("查询数据失败", e);
        }
    }

    // 结果映射器接口
    public interface ResultSetMapper<T> {
        T mapRow(ResultSet rs) throws SQLException;
    }
}
