package com.banmatrip.alert.dao;

import com.banmatrip.alert.domain.Department;
import org.apache.ibatis.annotations.Param;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Map;

public interface DepartmentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Department record);

    int insertSelective(Department record);

    Department selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Department record);

    int updateByPrimaryKey(Department record);

    int deleteById(Department department);

    int insertByDepartment(Department department);

    List<Integer> selectByDepartmentName(List<String> departmentName);

    List<Department> selectDepartmentByGroupId(Integer departmentGroupId);

    List<Map<String,Object>> findDepChild(@Param("id") int id);

    List<Map<String,Object>> selectDepTree();

    List<Department> findChild(Integer id);

    List<Map<String,Object>> findAll();

    List<Integer> selectParentIdById(@Param("id") int id);

    int countByEditDepartmentName(Map map);

    int countByAddDepartmentName(@Param("departmentName") String departmentName);

    Integer getIdByName(String name);

    /**
     * 获取部分最大层级
     * @return
     */
    Integer selectMaxDepartmentType();

    /**
     * 获取各个部门人数
     * @return
     */
    List<Map<String,Object>> selectDepartmentUsrCount();
}