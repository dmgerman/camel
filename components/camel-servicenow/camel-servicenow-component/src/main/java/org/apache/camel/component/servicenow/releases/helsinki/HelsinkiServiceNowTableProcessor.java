begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.servicenow.releases.helsinki
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
operator|.
name|releases
operator|.
name|helsinki
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|HttpMethod
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MediaType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|servicenow
operator|.
name|AbstractServiceNowProcessor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|servicenow
operator|.
name|ServiceNowEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|servicenow
operator|.
name|ServiceNowParams
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|servicenow
operator|.
name|ServiceNowConstants
operator|.
name|ACTION_CREATE
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|servicenow
operator|.
name|ServiceNowConstants
operator|.
name|ACTION_DELETE
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|servicenow
operator|.
name|ServiceNowConstants
operator|.
name|ACTION_MODIFY
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|servicenow
operator|.
name|ServiceNowConstants
operator|.
name|ACTION_RETRIEVE
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|servicenow
operator|.
name|ServiceNowConstants
operator|.
name|ACTION_UPDATE
import|;
end_import

begin_class
DECL|class|HelsinkiServiceNowTableProcessor
class|class
name|HelsinkiServiceNowTableProcessor
extends|extends
name|AbstractServiceNowProcessor
block|{
DECL|method|HelsinkiServiceNowTableProcessor (ServiceNowEndpoint endpoint)
name|HelsinkiServiceNowTableProcessor
parameter_list|(
name|ServiceNowEndpoint
name|endpoint
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|addDispatcher
argument_list|(
name|ACTION_RETRIEVE
argument_list|,
name|this
operator|::
name|retrieveRecord
argument_list|)
expr_stmt|;
name|addDispatcher
argument_list|(
name|ACTION_CREATE
argument_list|,
name|this
operator|::
name|createRecord
argument_list|)
expr_stmt|;
name|addDispatcher
argument_list|(
name|ACTION_MODIFY
argument_list|,
name|this
operator|::
name|modifyRecord
argument_list|)
expr_stmt|;
name|addDispatcher
argument_list|(
name|ACTION_DELETE
argument_list|,
name|this
operator|::
name|deleteRecord
argument_list|)
expr_stmt|;
name|addDispatcher
argument_list|(
name|ACTION_UPDATE
argument_list|,
name|this
operator|::
name|updateRecord
argument_list|)
expr_stmt|;
block|}
comment|/*      * GET      * https://instance.service-now.com/api/now/table/{tableName}      * https://instance.service-now.com/api/now/table/{tableName}/{sys_id}      */
DECL|method|retrieveRecord (Exchange exchange)
specifier|private
name|void
name|retrieveRecord
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
specifier|final
name|String
name|tableName
init|=
name|getTableName
argument_list|(
name|in
argument_list|)
decl_stmt|;
specifier|final
name|String
name|apiVersion
init|=
name|getApiVersion
argument_list|(
name|in
argument_list|)
decl_stmt|;
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|responseModel
init|=
name|getResponseModel
argument_list|(
name|in
argument_list|,
name|tableName
argument_list|)
decl_stmt|;
specifier|final
name|String
name|sysId
init|=
name|getSysID
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|Response
name|response
init|=
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|sysId
argument_list|)
condition|?
name|client
operator|.
name|reset
argument_list|()
operator|.
name|types
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON_TYPE
argument_list|)
operator|.
name|path
argument_list|(
literal|"now"
argument_list|)
operator|.
name|path
argument_list|(
name|apiVersion
argument_list|)
operator|.
name|path
argument_list|(
literal|"table"
argument_list|)
operator|.
name|path
argument_list|(
name|tableName
argument_list|)
operator|.
name|query
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_QUERY
argument_list|,
name|in
argument_list|)
operator|.
name|query
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_DISPLAY_VALUE
argument_list|,
name|in
argument_list|)
operator|.
name|query
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_EXCLUDE_REFERENCE_LINK
argument_list|,
name|in
argument_list|)
operator|.
name|query
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_SUPPRESS_PAGINATION_HEADER
argument_list|,
name|in
argument_list|)
operator|.
name|query
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_FIELDS
argument_list|,
name|in
argument_list|)
operator|.
name|query
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_LIMIT
argument_list|,
name|in
argument_list|)
operator|.
name|query
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_OFFSET
argument_list|,
name|in
argument_list|)
operator|.
name|query
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_VIEW
argument_list|,
name|in
argument_list|)
operator|.
name|query
argument_list|(
name|responseModel
argument_list|)
operator|.
name|invoke
argument_list|(
name|HttpMethod
operator|.
name|GET
argument_list|)
else|:
name|client
operator|.
name|reset
argument_list|()
operator|.
name|types
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON_TYPE
argument_list|)
operator|.
name|path
argument_list|(
literal|"now"
argument_list|)
operator|.
name|path
argument_list|(
name|apiVersion
argument_list|)
operator|.
name|path
argument_list|(
literal|"table"
argument_list|)
operator|.
name|path
argument_list|(
name|tableName
argument_list|)
operator|.
name|path
argument_list|(
name|sysId
argument_list|)
operator|.
name|query
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_DISPLAY_VALUE
argument_list|,
name|in
argument_list|)
operator|.
name|query
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_EXCLUDE_REFERENCE_LINK
argument_list|,
name|in
argument_list|)
operator|.
name|query
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_FIELDS
argument_list|,
name|in
argument_list|)
operator|.
name|query
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_VIEW
argument_list|,
name|in
argument_list|)
operator|.
name|query
argument_list|(
name|responseModel
argument_list|)
operator|.
name|invoke
argument_list|(
name|HttpMethod
operator|.
name|GET
argument_list|)
decl_stmt|;
name|setBodyAndHeaders
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|,
name|responseModel
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
comment|/*      * POST      * https://instance.service-now.com/api/now/table/{tableName}      */
DECL|method|createRecord (Exchange exchange)
specifier|private
name|void
name|createRecord
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
specifier|final
name|String
name|tableName
init|=
name|getTableName
argument_list|(
name|in
argument_list|)
decl_stmt|;
specifier|final
name|String
name|apiVersion
init|=
name|getApiVersion
argument_list|(
name|in
argument_list|)
decl_stmt|;
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|requestModel
init|=
name|getRequestModel
argument_list|(
name|in
argument_list|,
name|tableName
argument_list|)
decl_stmt|;
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|responseModel
init|=
name|getResponseModel
argument_list|(
name|in
argument_list|,
name|tableName
argument_list|)
decl_stmt|;
specifier|final
name|String
name|sysId
init|=
name|getSysID
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|validateBody
argument_list|(
name|in
argument_list|,
name|requestModel
argument_list|)
expr_stmt|;
name|Response
name|response
init|=
name|client
operator|.
name|reset
argument_list|()
operator|.
name|types
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON_TYPE
argument_list|)
operator|.
name|path
argument_list|(
literal|"now"
argument_list|)
operator|.
name|path
argument_list|(
name|apiVersion
argument_list|)
operator|.
name|path
argument_list|(
literal|"table"
argument_list|)
operator|.
name|path
argument_list|(
name|tableName
argument_list|)
operator|.
name|query
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_DISPLAY_VALUE
argument_list|,
name|in
argument_list|)
operator|.
name|query
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_EXCLUDE_REFERENCE_LINK
argument_list|,
name|in
argument_list|)
operator|.
name|query
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_FIELDS
argument_list|,
name|in
argument_list|)
operator|.
name|query
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_INPUT_DISPLAY_VALUE
argument_list|,
name|in
argument_list|)
operator|.
name|query
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_SUPPRESS_AUTO_SYS_FIELD
argument_list|,
name|in
argument_list|)
operator|.
name|query
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_VIEW
argument_list|,
name|in
argument_list|)
operator|.
name|query
argument_list|(
name|responseModel
argument_list|)
operator|.
name|invoke
argument_list|(
name|HttpMethod
operator|.
name|POST
argument_list|,
name|in
operator|.
name|getMandatoryBody
argument_list|()
argument_list|)
decl_stmt|;
name|setBodyAndHeaders
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|,
name|responseModel
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
comment|/*      * PUT      * https://instance.service-now.com/api/now/table/{tableName}/{sys_id}      */
DECL|method|modifyRecord (Exchange exchange)
specifier|private
name|void
name|modifyRecord
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
specifier|final
name|String
name|tableName
init|=
name|getTableName
argument_list|(
name|in
argument_list|)
decl_stmt|;
specifier|final
name|String
name|apiVersion
init|=
name|getApiVersion
argument_list|(
name|in
argument_list|)
decl_stmt|;
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|requestModel
init|=
name|getRequestModel
argument_list|(
name|in
argument_list|,
name|tableName
argument_list|)
decl_stmt|;
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|responseModel
init|=
name|getResponseModel
argument_list|(
name|in
argument_list|,
name|tableName
argument_list|)
decl_stmt|;
specifier|final
name|String
name|sysId
init|=
name|getSysID
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|validateBody
argument_list|(
name|in
argument_list|,
name|requestModel
argument_list|)
expr_stmt|;
name|Response
name|response
init|=
name|client
operator|.
name|reset
argument_list|()
operator|.
name|types
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON_TYPE
argument_list|)
operator|.
name|path
argument_list|(
literal|"now"
argument_list|)
operator|.
name|path
argument_list|(
name|apiVersion
argument_list|)
operator|.
name|path
argument_list|(
literal|"table"
argument_list|)
operator|.
name|path
argument_list|(
name|tableName
argument_list|)
operator|.
name|path
argument_list|(
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|sysId
argument_list|,
literal|"sysId"
argument_list|)
argument_list|)
operator|.
name|query
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_DISPLAY_VALUE
argument_list|,
name|in
argument_list|)
operator|.
name|query
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_EXCLUDE_REFERENCE_LINK
argument_list|,
name|in
argument_list|)
operator|.
name|query
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_FIELDS
argument_list|,
name|in
argument_list|)
operator|.
name|query
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_INPUT_DISPLAY_VALUE
argument_list|,
name|in
argument_list|)
operator|.
name|query
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_SUPPRESS_AUTO_SYS_FIELD
argument_list|,
name|in
argument_list|)
operator|.
name|query
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_VIEW
argument_list|,
name|in
argument_list|)
operator|.
name|query
argument_list|(
name|responseModel
argument_list|)
operator|.
name|invoke
argument_list|(
name|HttpMethod
operator|.
name|PUT
argument_list|,
name|in
operator|.
name|getMandatoryBody
argument_list|()
argument_list|)
decl_stmt|;
name|setBodyAndHeaders
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|,
name|responseModel
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
comment|/*      * DELETE      * https://instance.service-now.com/api/now/table/{tableName}/{sys_id}      */
DECL|method|deleteRecord (Exchange exchange)
specifier|private
name|void
name|deleteRecord
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
specifier|final
name|String
name|tableName
init|=
name|getTableName
argument_list|(
name|in
argument_list|)
decl_stmt|;
specifier|final
name|String
name|apiVersion
init|=
name|getApiVersion
argument_list|(
name|in
argument_list|)
decl_stmt|;
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|responseModel
init|=
name|getResponseModel
argument_list|(
name|in
argument_list|,
name|tableName
argument_list|)
decl_stmt|;
specifier|final
name|String
name|sysId
init|=
name|getSysID
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|Response
name|response
init|=
name|client
operator|.
name|reset
argument_list|()
operator|.
name|types
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON_TYPE
argument_list|)
operator|.
name|path
argument_list|(
literal|"now"
argument_list|)
operator|.
name|path
argument_list|(
name|apiVersion
argument_list|)
operator|.
name|path
argument_list|(
literal|"table"
argument_list|)
operator|.
name|path
argument_list|(
name|tableName
argument_list|)
operator|.
name|path
argument_list|(
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|sysId
argument_list|,
literal|"sysId"
argument_list|)
argument_list|)
operator|.
name|query
argument_list|(
name|responseModel
argument_list|)
operator|.
name|invoke
argument_list|(
name|HttpMethod
operator|.
name|DELETE
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|setBodyAndHeaders
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|,
name|responseModel
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
comment|/*      * PATCH      * instance://instance.service-now.com/api/now/table/{tableName}/{sys_id}      */
DECL|method|updateRecord (Exchange exchange)
specifier|private
name|void
name|updateRecord
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
specifier|final
name|String
name|tableName
init|=
name|getTableName
argument_list|(
name|in
argument_list|)
decl_stmt|;
specifier|final
name|String
name|apiVersion
init|=
name|getApiVersion
argument_list|(
name|in
argument_list|)
decl_stmt|;
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|requestModel
init|=
name|getRequestModel
argument_list|(
name|in
argument_list|,
name|tableName
argument_list|)
decl_stmt|;
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|responseModel
init|=
name|getResponseModel
argument_list|(
name|in
argument_list|,
name|tableName
argument_list|)
decl_stmt|;
specifier|final
name|String
name|sysId
init|=
name|getSysID
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|validateBody
argument_list|(
name|in
argument_list|,
name|requestModel
argument_list|)
expr_stmt|;
name|Response
name|response
init|=
name|client
operator|.
name|reset
argument_list|()
operator|.
name|types
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON_TYPE
argument_list|)
operator|.
name|path
argument_list|(
literal|"now"
argument_list|)
operator|.
name|path
argument_list|(
name|apiVersion
argument_list|)
operator|.
name|path
argument_list|(
literal|"table"
argument_list|)
operator|.
name|path
argument_list|(
name|tableName
argument_list|)
operator|.
name|path
argument_list|(
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|sysId
argument_list|,
literal|"sysId"
argument_list|)
argument_list|)
operator|.
name|query
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_DISPLAY_VALUE
argument_list|,
name|in
argument_list|)
operator|.
name|query
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_EXCLUDE_REFERENCE_LINK
argument_list|,
name|in
argument_list|)
operator|.
name|query
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_FIELDS
argument_list|,
name|in
argument_list|)
operator|.
name|query
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_INPUT_DISPLAY_VALUE
argument_list|,
name|in
argument_list|)
operator|.
name|query
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_SUPPRESS_AUTO_SYS_FIELD
argument_list|,
name|in
argument_list|)
operator|.
name|query
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_VIEW
argument_list|,
name|in
argument_list|)
operator|.
name|query
argument_list|(
name|responseModel
argument_list|)
operator|.
name|invoke
argument_list|(
literal|"PATCH"
argument_list|,
name|in
operator|.
name|getMandatoryBody
argument_list|()
argument_list|)
decl_stmt|;
name|setBodyAndHeaders
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|,
name|responseModel
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

