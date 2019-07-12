<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
#if(${entityPackage})
<mapper namespace="$!{entityPackage}.dao.${className}Dao">
	<resultMap id="baseResultMap" type="$!{entityPackage}.entity.$!{className}">
#else
<mapper namespace="com.zlw.mine.performance.dao.${className}Dao">
	<resultMap id="baseResultMap" type="com.zlw.mine.performance.entity.$!{className}">
#end
#foreach($item in $!{columnDatas})
		<result column="$!item.columnName" property="$item.fieldName" />
#end
	</resultMap>

	<sql id="baseColumnList">
		$!{SQL.columnFields}
	</sql>

	<sql id="baseWhereClause">
		where 1=1
		<trim suffixOverrides=",">
#foreach($item in $!{columnDatas})
#set($testStr = $!item.fieldName + " != null")
#if($!item.dataType == 'String')
#set($testStr = $!testStr + " and " + $!item.fieldName + " != ''")
#end
			<if test="$!testStr">
				and $!item.columnName=#{$!item.fieldName}
			</if>
#end
		</trim>
	</sql>

	<sql id="selectiveSetClause">
		<trim suffixOverrides=",">
#foreach($item in $!{columnDatas})
#set($testStr = $!item.fieldName + " != null")
#if($!item.dataType == 'String')
#set($testStr = $!testStr + " and " + $!item.fieldName + " != ''")
#end
#if($!item.fieldName=='modifyTime')
			modify_time=now()
#else
			<if test="$!testStr">
				$!item.columnName=#{$!item.fieldName},
			</if>
#end
#end
		</trim>
	</sql>

	<insert id="add" parameterType="Object">
#if ($keyType =='02')
		<selectKey resultType="java.lang.Integer" order="AFTER" keyProperty="id">
			SELECT LAST_INSERT_ID()
		</selectKey>
#end
		$!{SQL.insert}
	</insert>

	<update id="update" parameterType="Object">
		$!{SQL.update}
	</update>

	<update id="updateBySelective" parameterType="Object">
		$!{SQL.updateSelective}
	</update>

	<delete id="delete" parameterType="Object">
		$!{SQL.delete}
	</delete>

	<select id="queryById" resultMap="baseResultMap" parameterType="Object">
		$!{SQL.selectById}
	</select>

	<select id="queryByCount" resultType="java.lang.Integer"
		parameterType="Object">
		select count(1) from ${tableName}
		<include refid="baseWhereClause" />
	</select>

	<select id="queryByList" resultMap="baseResultMap"
		parameterType="Object">
		select
		<include refid="baseColumnList" />
		from ${tableName}
		<include refid="baseWhereClause" />
	</select>

</mapper>
