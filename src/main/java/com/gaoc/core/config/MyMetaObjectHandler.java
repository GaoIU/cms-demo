package com.gaoc.core.config;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.gaoc.core.enums.TimeEnum;
import com.gaoc.core.properties.BaseProperties;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

	private final BaseProperties baseProperties;

	@Override
	public void insertFill(MetaObject metaObject) {
		setValue(baseProperties.getCreateTimeName(), metaObject, baseProperties.getCreateTimeType());
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		setValue(baseProperties.getUpdateTimeName(), metaObject, baseProperties.getUpdateTimeType());
	}

	private void setValue(String fieldName, MetaObject metaObject, TimeEnum timeEnum) {
		switch (timeEnum) {
		case DATE:
			this.setFieldValByName(fieldName, new Date(), metaObject);
			break;

		case TIMESTAMP:
			this.setFieldValByName(fieldName, new Timestamp(System.currentTimeMillis()), metaObject);
			break;

		case LOCAL_DATE:
			this.setFieldValByName(fieldName, LocalDate.now(), metaObject);
			break;

		case LOCAL_DATE_TIME:
			this.setFieldValByName(fieldName, LocalDateTime.now(), metaObject);
			break;

		default:
			break;
		}
	}

}
