package org.corefine.common.jdbc;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public abstract class CRUDDao<Mapper extends CRUDMapper<Entity>, Entity extends CRUDEnitty> {
    @Autowired
    protected Mapper mapper;

    /**
     * 获取单条数据
     *
     * @param id id
     * @return entity
     */
    public Entity findOne(Long id) {
        return mapper.findOne(id);
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param entity entity
     */
    public void save(Entity entity) {
        if (entity.isNewRecord()) {
            this.preInsert(entity);
            mapper.insert(entity);
        } else {
            this.preUpdate(entity);
            mapper.update(entity);
        }
    }

    /**
     * 更新前处理
     *
     * @param entity entity
     */
    protected void preUpdate(Entity entity) {
    }

    /**
     * 插入前处理
     *
     * @param entity entity
     */
    protected void preInsert(Entity entity) {
        if (entity.getCreateTime() == null)
            entity.setCreateTime(new Date());
    }

    /**
     * 删除数据
     *
     * @param entity entity
     */
    public void delete(Entity entity) {
        if (entity != null) {
            this.delete(entity.getId());
        }
    }

    public void delete(Long id) {
        if (id != null) {
            mapper.delete(id);
        }
    }

    /**
     * 批量插入
     *
     * @param entitys entitys
     */
    @Transactional
    public void insertBatch(List<Entity> entitys) {
        this.insertBatch(entitys, 1000);
    }

    /**
     * 批量插入
     *
     * @param entitys   entitys
     * @param sliceSize 切片大小
     */
    @Transactional
    public void insertBatch(List<Entity> entitys, int sliceSize) {
        if (entitys.isEmpty()) {
            return;
        }
        Consumer<Entity> consumer = this::preInsert;
        for (Collection<Entity> es : this.sliceCollection(entitys, sliceSize, consumer)) {
            this.mapper.insertBatch(es);
        }
    }

    /**
     * 批量更新
     *
     * @param entitys entitys
     */
    @Transactional
    public void updateBatch(List<Entity> entitys) {
        this.updateBatch(entitys, 1000);
    }

    /**
     * 批量更新
     *
     * @param entitys   entitys
     * @param sliceSize 切片大小
     */
    @Transactional
    public void updateBatch(List<Entity> entitys, int sliceSize) {
        if (entitys.isEmpty()) {
            return;
        }
        Consumer<Entity> consumer = this::preUpdate;
        for (Collection<Entity> es : this.sliceCollection(entitys, sliceSize, consumer)) {
            this.mapper.updateBatch(es);
        }
    }

    /**
     * 批量删除
     * @param ids ids
     */
    @Transactional
    public void deleteBatch(Collection<Long> ids) {
        if (ids.isEmpty()) {
            return;
        }
        this.mapper.deleteBatch(ids);
    }

    /**
     * 切片Collection，为批量提交提供支持
     *
     * @param entitys   entitys
     * @param sliceSize sliceSize
     * @param consumer  每个entity处理，可以为null
     * @return 切分好的Collection
     */
    protected List<List<Entity>> sliceCollection(List<Entity> entitys, int sliceSize, Consumer<Entity> consumer) {
        if (consumer == null) {
            consumer = (entity) -> {
            };
        }
        List<List<Entity>> resultCollection = new ArrayList<>();
        for (int i = 0; i < entitys.size(); ) {
            int startIndex = i;
            int endIndex = (i += sliceSize) >= entitys.size() ? entitys.size() : i;
            List<Entity> sliceCollection = new ArrayList<>(endIndex - startIndex);
            for (int j = startIndex; j < endIndex; j++) {
                Entity entity = entitys.get(j);
                consumer.accept(entity);
                sliceCollection.add(entity);
            }
            resultCollection.add(sliceCollection);
        }
        return resultCollection;
    }

    /**
     * 查询（根据 columnMap 条件）
     *
     * @param columnMap 表字段 map 对象
     * @return 结果集合
     */
    public List<Entity> selectByMap(@Param("cm") Map<String, Object> columnMap) {
        return this.mapper.selectByMap(columnMap);
    }

    /**
     * count（根据 columnMap 条件）
     *
     * @param columnMap 表字段 map 对象
     * @return Long
     */
    public Long countByMap(@Param("cm") Map<String, Object> columnMap) {
        return this.mapper.countByMap(columnMap);
    }

    /**
     * 更新（根据 columnMap 条件）
     *
     * @param columnMap 表字段 map 对象
     * @param dataMap 表值 map 对象
     */
    public void updateByMap(@Param("cm") Map<String, Object> columnMap, @Param("dm") Map<String, Object> dataMap) {
        this.mapper.updateByMap(columnMap, dataMap);
    }
    /**
     * 删除（根据 columnMap 条件）
     *
     * @param columnMap 表字段 map 对象
     */
    public void deleteByMap(@Param("cm") Map<String, Object> columnMap) {
        this.mapper.deleteByMap(columnMap);
    }
}
