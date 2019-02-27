package com.creatoo.hn;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.creatoo.hn.dao.mapper.WhgSysCultMapper;
import com.creatoo.hn.dao.mapper.WhgSysIdsCustomMapper;
import com.creatoo.hn.dao.model.WhgSysCult;
import com.creatoo.hn.util.ENVUtils;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.enums.EnumState;
import com.creatoo.hn.util.enums.EnumStateDel;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings("ALL")
@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.creatoo.hn.dao.mapper")
@EnableCaching
@EnableScheduling
public class SzwhgAdminApplication implements CommandLineRunner{
	@Autowired
	private WhgSysCultMapper whgSysCultMapper;

	@Autowired
	private Environment env;
	@Autowired
	private WhgSysIdsCustomMapper whgSysIdsCustomMapper;

	/**
	 * 使用 @Bean 注入 FastJsonHttpMessageConverter
	 * @return
	 */
	@Bean
	public HttpMessageConverters fastJsonHttpMessageConverters()
	{
		//1、需要先定义一个 convert 转换消息对象；
		FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();

		//2、添加 fastJson 的配置信息，比如: 是否要格式化返回的Json数据；
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);

		//3、在 Convert 中添加配置信息;
		fastConverter.setFastJsonConfig(fastJsonConfig);

		List<MediaType> supportedMediaTypes = new ArrayList();
		supportedMediaTypes.add(MediaType.TEXT_HTML);
		supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
		fastConverter.setSupportedMediaTypes(supportedMediaTypes);

		//4、
		HttpMessageConverter<?> converter = fastConverter;
		return new HttpMessageConverters(converter);
	}


	@Override
	@Transactional
	public void run(String... strings) throws Exception {
		try {
			WhgSysCult rootCult = this.whgSysCultMapper.selectByPrimaryKey(Constant.ROOT_SYS_CULT_ID);
			if (rootCult == null){
                rootCult = new WhgSysCult();
                rootCult.setId(Constant.ROOT_SYS_CULT_ID);
                rootCult.setPid(Constant.ROOT_SYS_CULT_PID);
                rootCult.setCrtdate(new Date());
                rootCult.setCrtuser(Constant.SUPER_USER_ID);
                rootCult.setState(EnumState.STATE_YES.getValue());
                rootCult.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
                rootCult.setStatemdfdate(new Date());
                rootCult.setStatemdfuser(Constant.SUPER_USER_ID);
                rootCult.setName("中心文化馆");
                rootCult.setIdx(1);

				this.whgSysCultMapper.insert(rootCult);
			}
			ENVUtils envUtils = new ENVUtils() ;
			envUtils.setEnv(env);

			//设置DAO
			IDUtils.setWhgSysIdsCustomMapper(whgSysIdsCustomMapper);
		} catch (Exception e) {
			throw e;
		}
	}

	public static void main(String[] args) {
		System.setProperty("spring.profiles.active", "dev");
		SpringApplication.run(SzwhgAdminApplication.class, args);
	}
}
