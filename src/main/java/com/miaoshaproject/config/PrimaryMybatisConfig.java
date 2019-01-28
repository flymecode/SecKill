/**
 * @author: maxu1
 * @date: 2019/1/28 9:35
 */

package com.miaoshaproject.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * @author maxu
 */
@Configuration
@MapperScan(basePackages = {"com.miaoshaproject.mapper"}, sqlSessionTemplateRef = "firstSqlSessionTemplate")
public class PrimaryMybatisConfig {

	@Bean(name = "firstDataSource")
	@ConfigurationProperties(prefix = "first.spring.datasource")
	@Primary
	public DataSource firstDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "firstSqlSessionFactory")
	public SqlSessionFactory firstSqlSessionFactroy(@Qualifier("firstDataSource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		return bean.getObject();

//		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
//		try {
//			bean.setMapperLocations(resolver.getResources("classpath*:mybatis/mapper/meta/*.xml"));
//			return bean.getObject();
//		} catch(Exception e) {
//			throw new RuntimeException(e);
//		}
	}

	@Bean(name = "firstSqlSessionTemplate")
	public SqlSessionTemplate reportSqlSessionTemplate(@Qualifier("firstSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
		SqlSessionTemplate template = new SqlSessionTemplate(sqlSessionFactory);
		return template;
	}
}
