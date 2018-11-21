package org.corefine.common.jdbc;

import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface CRUDMapper<Entity> {

    Entity findOne(Long id);

    void insert(Entity entity);

    void insertBatch(Collection<Entity> entitys);

    void update(Entity entity);

    void updateBatch(Collection<Entity> entitys);

    void delete(Long id);

    void deleteBatch(Collection<Long> ids);

    /**
     * 查询（根据 columnMap 条件）
     *
     * @param columnMap 表字段 map 对象
     * @return 结果集合
     */
    List<Entity> selectByMap(@Param("cm") Map<String, Object> columnMap);

    /**
     * count（根据 columnMap 条件）
     *
     * @param columnMap 表字段 map 对象
     * @return Long
     */
    Long countByMap(@Param("cm") Map<String, Object> columnMap);

    /**
     * 更新（根据 columnMap 条件）
     *
     * @param columnMap 表字段 map 对象
     * @param dataMap 表值 map 对象
     */
    void updateByMap(@Param("cm") Map<String, Object> columnMap, @Param("dm") Map<String, Object> dataMap);

    /**
     * 删除（根据 columnMap 条件）
     *
     * @param columnMap 表字段 map 对象
     */
    void deleteByMap(@Param("cm") Map<String, Object> columnMap);
}
