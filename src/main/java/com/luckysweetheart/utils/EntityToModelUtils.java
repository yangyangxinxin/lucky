package com.luckysweetheart.utils;

import com.luckysweetheart.common.PagedResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Entity 转 Model  工具。<br/><br/>
 * 常规的写法非常复杂,eg:<br/>
 * <pre>
 * <code>
 * public UserDTO findById(Long userId){
 *      User user = userDao.get(userId);
 *      UserDTO userDTO = new UserDTO();
 *      BeanCopierUtils.copy(userDTO,user);
 *      return userDTO;
 * }
 * </code>
 * </pre>
 * 现在简化写法，一句话搞定：
 * <p>
 * <pre>
 * <code>
 * public UserDTO findById(Long userId) {
 *      User user = userDao.get(userId);
 *      return EntityToModelUtils.entityToModel(user,UserDTO.class);
 * }
 * </code>
 * </pre>
 * <p>
 * 同样的，返回Model的List集合：<pre><code> EntityToModelUtils.entityListToModelList(userList,UserDTO.class)</code></pre>即可返回<pre><code>List<\UserDTO></code></pre>
 * <br/>
 * 返回Model 的 PagedResult的对象<pre><code>EntityToModelUtils.entityListToModelList(userPagedResult,UserDTO.class)</code></pre>
 * <p>
 *
 *     <br/>
 * Created by yangxin on 2017/8/25.
 */
public class EntityToModelUtils {

    /**
     * 返回 Model 的Map对象
     *
     * @param entityMap 原对象Map集合
     * @param modelClass 要转换的对象
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <Key, Entity, Model> Map<Key, Model> entityMapToModelMap(Map<Key, Entity> entityMap,
                                                                           Class<Model> modelClass) throws IllegalAccessException, InstantiationException {
        Map<Key, Model> modelMap = new HashMap<>();
        for (Key key : entityMap.keySet()) {
            Entity entity = entityMap.get(key);
            Model model = entityToModel(entity, modelClass);
            modelMap.put(key, model);
        }
        return modelMap;
    }

    /**
     * 返回Model的List集合
     *
     * @param entityList 原对象List
     * @param modelClass 要转换的对象
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <Entity, Model> List<Model> entityListToModelList(List<Entity> entityList, Class<Model> modelClass)
            throws IllegalAccessException, InstantiationException {
        List<Model> modelList = new ArrayList<>();
        for (Entity entity : entityList) {
            Model model = entityToModel(entity, modelClass);
            modelList.add(model);
        }
        return modelList;
    }

    /**
     * 返回Model 的PagedResult对象
     *
     * @param entityPagedResult 原对象PagedResult
     * @param modelClass 要转换的对象
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static <Entity, Model> PagedResult<Model> getModelPagedResult(PagedResult<Entity> entityPagedResult, Class<Model> modelClass) throws InstantiationException, IllegalAccessException {
        PagedResult<Model> result = new PagedResult<>();
        if (entityPagedResult != null && entityPagedResult.getSize() > 0) {
            List<Model> models = entityListToModelList(entityPagedResult.getResults(), modelClass);
            result.setPaged(entityPagedResult.getPaged());
            result.setResults(models);
        }
        return result;
    }

    /**
     * 返回Model对象
     *
     * @param entity 原对象
     * @param modelClass 要转换的对象
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <Entity, Model> Model entityToModel(Entity entity, Class<Model> modelClass)
            throws IllegalAccessException, InstantiationException {
        Model model = modelClass.newInstance();
        BeanCopierUtils.copy(entity, model);
        return model;
    }

}
