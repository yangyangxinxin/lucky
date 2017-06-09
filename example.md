#### 关于分页查询、多条件查询的示例

       PhotoQuery photoQuery = new PhotoQuery();

       List<ConditionParam<PhotoQueryField>> conditionParams = new ArrayList<>();
       List<OrderParam<PhotoQueryField>> orderParams = new ArrayList<>();

       Paged paged = new Paged();
       paged.setPageSize(10);
       paged.setPage(1);

       // where photoId = 1
       conditionParams.add(new ConditionParam<PhotoQueryField>(PhotoQueryField.PHOTO_ID, 1, ConditionParam.OPERATION_EQ));

       // order by create desc
       orderParams.add(new OrderParam<PhotoQueryField>(PhotoQueryField.CREATE_TIME, OrderParam.ORDER_TYPE_DESC));

       photoQuery.setPaged(paged);
       photoQuery.setConditionParams(conditionParams);
       photoQuery.setOrderParams(orderParams);

       ResultInfo<PagedResult<PhotoDTO>> result = photoService.query(photoQuery);
       if (result.isSuccess()) {
           PagedResult<PhotoDTO> pagedResult = result.getData();
           List<PhotoDTO> photoDTOS = pagedResult.getResults();
           for (PhotoDTO photoDTO : photoDTOS) {
               System.out.println(JSON.toJSONString(photoDTO));
           }
       }
       System.out.println(result.getMsg());