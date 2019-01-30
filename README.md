# 秒杀系统设计

收获:
  - 在我们获得需求之后首先设计领域模型(Domain Model)
  - 实战派认为领域模型是一种分析模型，用于分析理解复杂业务领域问题，具体到软件开发过程中就是在分析阶段分析如何满足系统功能性需求

### 数据库设计

- 密码与用户主表分离，并且要密文存储
### 用户模块

- 用户加密方法

```java
public static String encode(String str) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		// 确定计算方法
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		BASE64Encoder base64Encoder = new BASE64Encoder();
		// 加密字符串
		String newstr = base64Encoder.encode(md5.digest(str.getBytes("utf-8")));
		return newstr;
	}
```

- VO <--------->Model<--------> POJO

- VO 负责前端展示数据的传输，前端只需要展示的字段

  ```java
  @Data
  public class UserVO {
  	private Integer id;
  	private String name;
  	private Integer gender;
  	private Integer age;
  	private String telPhone;
  
  }
  ```

  Model位于Service层用于处理业务逻辑需要的数据，可将多个POJO封装成一个Model

  ```java
  @Data
  public class UserModel {
  	private Integer id;
  	private String name;
  	private Integer gender;
  	private Integer age;
  	private String telPhone;
  	private String registerMode;
  	private Integer thirdPartyId;
  	private String encrptPassword;
  }
  ```

  POJO 每个实体类对应数据库中的一个表，实体类中的属性和数据库中的字段一一对应

  ```java
  @Data
  public class UserInfo {
  	private Integer id;
  	private String name;
  	private Integer age;
  	private Integer gender;
  	private String telPhone;
  	private String registerMode;
  	private Integer thirdPartyId;
  }
  ```

  ```java
  @Data
  public class UserPassword {
  	private Integer id;
  	private Integer userId;
  	private String encrptPassword;
  }
  ```

在向前端展示数据的时候封装为统一的响应方式

```java
/**
 * 统一返回实体
 * @author maxu
 */
@Data
public class CommonReturnType {

	// 表明对应请求的处理结果 success 或者 fail
	private String status;
	// 如果 status = suceess 返回前端需要的请求信息
	// 如果 status = fail 返回通用的错误码格式
	private Object data;

	public static CommonReturnType create(Object result) {
		return  CommonReturnType.create(result,"success");
	}
	
	public static CommonReturnType create(Object result,String status) {
		CommonReturnType type = new CommonReturnType();
		type.setData(result);
		type.setStatus(status);
		return type;
	}
}
```

spring中统一异常处理

```java
public class BaseController {
	@ExceptionHandler(Exception.class)
    // 重新设置code
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Object handlerException(HttpServletRequest request, Exception ex) {

		Map responseData = new HashMap();
		if (ex instanceof BusinessException) {
			BusinessException businessException = (BusinessException) ex;
			responseData.put("errCode", businessException.getErrorCode());
			responseData.put("errMsg", businessException.getErrorMsg());
		} else {
			responseData.put("errCode", EmBusinessError.UNKNOWN_ERROR.getErrorCode());
			responseData.put("errMsg", EmBusinessError.UNKNOWN_ERROR.getErrorMsg());
		}
		return CommonReturnType.create(responseData, "fail");
	}

}
```

swagger2配置 - 接口的测试

需要引入pom文件

```xml
<!--swagger2 -->
    <dependency>
      <groupId>io.springfox</groupId>
      <artifactId>springfox-swagger2</artifactId>
      <version>2.8.0</version>
    </dependency>
    <!--swagger-ui-->
    <dependency>
      <groupId>io.springfox</groupId>
      <artifactId>springfox-swagger-ui</artifactId>
      <version>2.7.0</version>
    </dependency>
```

```java
/**
 * @author maxu
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.miaoshaproject.controller"))
				.paths(PathSelectors.any())
				.build();
	}


	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("秒杀模块API")
				.description("更多文章请关注https://github.com/flymecode/miaosha")
				.termsOfServiceUrl("https://github.com/flymecode/miaosha")
				.version("V.1.1.0")
				.build();
	}

}
```
```java
	public CommonReturnType createItem(@RequestParam(name = "title") String title,
	                                   @RequestParam(name = "description") String description,
	                                   @RequestParam(name = "price") BigDecimal price,
	                                   @RequestParam(name = "stock") Integer stock,
	                                   @RequestParam(name = "imgUrl") String imgUrl) throws BusinessException {

		ItemModel itemModel = new ItemModel();
		itemModel.setTitle(title);
		itemModel.setDescription(description);
		itemModel.setPrice(price);
		itemModel.setStock(stock);
		itemModel.setImgUrl(imgUrl);

		ItemModel itemForReuturn = itemService.createItem(itemModel);
		ItemVO itemVO = convertVOFromModel(itemForReuturn);
		return CommonReturnType.create(itemVO);
	}
```
问题：itemModelForReturn和传入参数形成的itemModel有什么区别呢？
   ItemServiceImpl里面的createItem方法为什么返回的itemModel需要重新去数据库再取一次
   
- 因为许多值在原本的itemmodel里是没有的，比如数据库的默认值，itemmodel里是null，经过数据库后变成0或空字符串
   
- 聚合生成的，比如get方法里还会去聚合转换一些逻辑，比如itemstock库存是在get方法里会取，但是itemmodel里只有一个库存数量，没有主键或者以后其他的更多逻辑对一个restful风格的服务，创建方法需要可以返回对象创建后的模型，因此这么做可以将后续逻辑都收口到get方法里

### 异常处理
```java
// 定义一个异常的同意结口
public interface CommonError {
	int getErrorCode();

	String getErrorMsg();

	CommonError setErrorMsg(String errorMsg);

}

// 实现CommonError接口
public class BusinessException extends Exception implements CommonError {
	
	private CommonError commonError;

	//直接接受
	public BusinessException(CommonError commonError) {
		super();
		this.commonError = commonError;
	}

	public BusinessException(CommonError commonError,String errMsg) {
		super();
		this.commonError.setErrorMsg(errMsg);
		this.commonError = commonError;
	}
	@Override
	public int getErrorCode() {
		return commonError.getErrorCode();
	}

	@Override
	public String getErrorMsg() {
		return commonError.getErrorMsg();
	}

	@Override
	public CommonError setErrorMsg(String errorMsg) {
		this.commonError.setErrorMsg(errorMsg);
		return this;
	}
}

// 错误类型定义
public enum EmBusinessError implements CommonError {
	// 通用错误类型0001
	PARAMETER_VALDITION_ERROR(00001,"参数不合法"),
        // 1000x开头定义用户相关信息
	USER_NOT_EXSIT(10001,"用户不存在"),
	UNKNOWN_ERROR(20000,"未知错误"),
	STOCK_NOT_ENOUGH(30001,"库存不足"),
	USER_LOGIN_FAIL(20001,"用户或密码不正确"),
	USER_NOT_LOGIN(20002,"用户未登录") ;
	private int errorCode;
	private String errorMsg;

	EmBusinessError(int errorCode, String errorMsg) {
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}

	@Override
	public int getErrorCode() {
		return this.errorCode;
	}

	@Override
	public String getErrorMsg() {
		return this.errorMsg;
	}

	@Override
	public CommonError setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
		return this;
	}
}
```
   
### 邮箱实现

##### 场景
- 注册验证
- 找回密码
- 网站营销
- 提醒、监控
- 触发机制


