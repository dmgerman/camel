begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.servicenow
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|servicenow
package|;
end_package

begin_interface
DECL|interface|ServiceNowConstants
specifier|public
interface|interface
name|ServiceNowConstants
block|{
DECL|field|RESOURCE
name|String
name|RESOURCE
init|=
literal|"CamelServiceNowResource"
decl_stmt|;
DECL|field|TABLE
name|String
name|TABLE
init|=
literal|"CamelServiceNowTable"
decl_stmt|;
DECL|field|ACTION
name|String
name|ACTION
init|=
literal|"CamelServiceNowAction"
decl_stmt|;
DECL|field|MODEL
name|String
name|MODEL
init|=
literal|"CamelServiceNowModel"
decl_stmt|;
DECL|field|RESOURCE_TABLE
name|String
name|RESOURCE_TABLE
init|=
literal|"table"
decl_stmt|;
DECL|field|RESOURCE_AGGREGATE
name|String
name|RESOURCE_AGGREGATE
init|=
literal|"aggregate"
decl_stmt|;
DECL|field|RESOURCE_IMPORT
name|String
name|RESOURCE_IMPORT
init|=
literal|"import"
decl_stmt|;
DECL|field|ACTION_RETRIEVE
name|String
name|ACTION_RETRIEVE
init|=
literal|"retrieve"
decl_stmt|;
DECL|field|ACTION_CREATE
name|String
name|ACTION_CREATE
init|=
literal|"create"
decl_stmt|;
DECL|field|ACTION_MODIFY
name|String
name|ACTION_MODIFY
init|=
literal|"modify"
decl_stmt|;
DECL|field|ACTION_DELETE
name|String
name|ACTION_DELETE
init|=
literal|"delete"
decl_stmt|;
DECL|field|ACTION_UPDATE
name|String
name|ACTION_UPDATE
init|=
literal|"update"
decl_stmt|;
DECL|field|SYSPARM_ID
name|String
name|SYSPARM_ID
init|=
literal|"CamelServiceNowSysId"
decl_stmt|;
DECL|field|SYSPARM_QUERY
name|String
name|SYSPARM_QUERY
init|=
literal|"CamelServiceNowQuery"
decl_stmt|;
DECL|field|SYSPARM_DISPLAY_VALUE
name|String
name|SYSPARM_DISPLAY_VALUE
init|=
literal|"CamelServiceNowDisplayValue"
decl_stmt|;
DECL|field|SYSPARM_INPUT_DISPLAY_VALUE
name|String
name|SYSPARM_INPUT_DISPLAY_VALUE
init|=
literal|"CamelServiceNowInputDisplayValue"
decl_stmt|;
DECL|field|SYSPARM_EXCLUDE_REFERENCE_LINK
name|String
name|SYSPARM_EXCLUDE_REFERENCE_LINK
init|=
literal|"CamelServiceNowExcludeReferenceLink"
decl_stmt|;
DECL|field|SYSPARM_FIELDS
name|String
name|SYSPARM_FIELDS
init|=
literal|"CamelServiceNowFields"
decl_stmt|;
DECL|field|SYSPARM_MIN_FIELDS
name|String
name|SYSPARM_MIN_FIELDS
init|=
literal|"CamelServiceNowMinFields"
decl_stmt|;
DECL|field|SYSPARM_MAX_FIELDS
name|String
name|SYSPARM_MAX_FIELDS
init|=
literal|"CamelServiceNowMaxFields"
decl_stmt|;
DECL|field|SYSPARM_SUM_FIELDS
name|String
name|SYSPARM_SUM_FIELDS
init|=
literal|"CamelServiceNowSumFields"
decl_stmt|;
DECL|field|SYSPARM_LIMIT
name|String
name|SYSPARM_LIMIT
init|=
literal|"CamelServiceNowLimit"
decl_stmt|;
DECL|field|SYSPARM_VIEW
name|String
name|SYSPARM_VIEW
init|=
literal|"CamelServiceNowView"
decl_stmt|;
DECL|field|SYSPARM_SUPPRESS_AUTO_SYS_FIELD
name|String
name|SYSPARM_SUPPRESS_AUTO_SYS_FIELD
init|=
literal|"CamelServiceNowSuppressAutoSysField"
decl_stmt|;
DECL|field|SYSPARM_AVG_FIELDS
name|String
name|SYSPARM_AVG_FIELDS
init|=
literal|"CamelServiceNowAvgFields"
decl_stmt|;
DECL|field|SYSPARM_COUNT
name|String
name|SYSPARM_COUNT
init|=
literal|"CamelServiceNowCount"
decl_stmt|;
DECL|field|SYSPARM_GROUP_BY
name|String
name|SYSPARM_GROUP_BY
init|=
literal|"CamelServiceGroupBy"
decl_stmt|;
DECL|field|SYSPARM_ORDER_BY
name|String
name|SYSPARM_ORDER_BY
init|=
literal|"CamelServiceOrderBy"
decl_stmt|;
DECL|field|SYSPARM_HAVING
name|String
name|SYSPARM_HAVING
init|=
literal|"CamelServiceHaving"
decl_stmt|;
block|}
end_interface

end_unit

