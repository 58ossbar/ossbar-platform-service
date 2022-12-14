package com.ossbar.modules.sys.api;

import com.ossbar.core.baseclass.domain.R;
import com.ossbar.modules.sys.dto.dict.SaveDictDTO;
import com.ossbar.modules.sys.dto.dict.SaveDictTypeDTO;
import com.ossbar.modules.sys.vo.dict.TsysDictVO;

import java.util.List;
import java.util.Map;

/**
 * 字典管理
 * @author huj
 * @create 2022-08-16 15:46
 * @email hujun@creatorblue.com
 * @company 创蓝科技 www.creatorblue.com
 */
public interface TsysDictService {

    /**
     * 获取字典表中parentType!=0的字典信息
     *
     * @return
     */
    List<TsysDictVO> selectVoListByMap(Map<String, Object> map);

    /**
     * 获取字典表中parentType!=0的字典信息
     *
     * @return
     */
    R selectListByMapNotZero(Map<String, Object> map);

    /**
     * 查询操作
     * @author huangwb
     * @param params (page页码,limit显示条数)
     * @return R
     */
    R query(Map<String, Object> params);

    /**
     * 获取字典详情信息
     * @author huangwb
     * @param dictId
     * @return R
     */
    R view(String dictId);

    /**
     * 查询指定父字典id的所有数据
     * @param parentId
     * @return
     */
    R selectListParentId(String parentId);

    /**
     * 根据条件查询数据
     * @return
     * @throws Exception
     */
    R dicttree(String dictname);

    /**
     * 新增字典分类
     * @param dict
     * @return
     */
    R saveType(SaveDictTypeDTO dict);

    /**
     * 修改字典分类
     * @param dict
     * @return
     */
    R updateType(SaveDictTypeDTO dict);

    /**
     * 新增字典
     * @param dto
     * @return
     */
    R save(SaveDictDTO dto);

    /**
     * 修改字典
     * @param dto
     * @return
     */
    R update(SaveDictDTO dto);

    /**
     *
     * 删除
     *
     * @author huangwb
     * @param ids
     * @return R
     */
    R deleteType(String[] ids);

    /**
     * 根据条件获取排序号，已+1
     * @param parentType
     * @return
     */
    Integer getMaxSortNum(String parentType);

}
