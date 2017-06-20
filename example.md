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

#### 邮件发送示例

> 例如现在发送一条激活账户的邮件，需要发送给`981987024@qq.com`，代码如下：

    EmailSender emailSender = EmailSender.init().emailTemplate(EmailTemplate.REGISTER).to("981987024@qq.com").
                    param("your key","your value").subject("注册3").send();

> 你需要准备好邮件的模板，放在`/static/email/`目录下，或者自己定义，总之在`EmailTemplate`中配置好你的邮件模板路径，邮件模板采用的是Freemarker，模板中的变量可以通过 `param("your key","your value")`解析。邮件发送完成后，会在
`lucky_email_snapshoot`表中存储，如果邮件发送失败了，那么也会在`EmailScan`这个定时任务中每隔一定时间扫描重发。

#### WARM

subject、收件人一定不要忘记了指定！！！