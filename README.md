# 秒杀系统设计

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

- VO 负责前端展示数据的传输

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

