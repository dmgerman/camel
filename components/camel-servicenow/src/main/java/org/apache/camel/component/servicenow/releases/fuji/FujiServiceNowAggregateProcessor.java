begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.servicenow.releases.fuji
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
name|fuji
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
name|ServiceNowConstants
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

begin_class
DECL|class|FujiServiceNowAggregateProcessor
class|class
name|FujiServiceNowAggregateProcessor
extends|extends
name|FujiServiceNowProcessor
block|{
DECL|method|FujiServiceNowAggregateProcessor (ServiceNowEndpoint endpoint)
name|FujiServiceNowAggregateProcessor
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
block|}
annotation|@
name|Override
DECL|method|doProcess (Exchange exchange, Class<?> requestModel, Class<?> responseModel, String action, String apiVersion, String tableName, String sysId)
specifier|protected
name|void
name|doProcess
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|requestModel
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|responseModel
parameter_list|,
name|String
name|action
parameter_list|,
name|String
name|apiVersion
parameter_list|,
name|String
name|tableName
parameter_list|,
name|String
name|sysId
parameter_list|)
throws|throws
name|Exception
block|{
name|Response
name|response
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|ServiceNowConstants
operator|.
name|ACTION_RETRIEVE
argument_list|,
name|action
argument_list|,
literal|true
argument_list|)
condition|)
block|{
name|response
operator|=
name|retrieveStats
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|,
name|requestModel
argument_list|,
name|responseModel
argument_list|,
name|tableName
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unknown action "
operator|+
name|action
argument_list|)
throw|;
block|}
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
DECL|method|retrieveStats (Message in, Class<?> requestModel, Class<?> responseModel, String tableName)
specifier|private
name|Response
name|retrieveStats
parameter_list|(
name|Message
name|in
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|requestModel
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|responseModel
parameter_list|,
name|String
name|tableName
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|String
name|apiVersion
init|=
name|getApiVersion
argument_list|(
name|in
argument_list|)
decl_stmt|;
return|return
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
literal|"stats"
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
name|SYSPARM_AVG_FIELDS
argument_list|,
name|in
argument_list|)
operator|.
name|query
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_COUNT
argument_list|,
name|in
argument_list|)
operator|.
name|query
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_MIN_FIELDS
argument_list|,
name|in
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
name|SYSPARM_MAX_FIELDS
argument_list|,
name|in
argument_list|)
operator|.
name|query
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_SUM_FIELDS
argument_list|,
name|in
argument_list|)
operator|.
name|query
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_GROUP_BY
argument_list|,
name|in
argument_list|)
operator|.
name|query
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_ORDER_BY
argument_list|,
name|in
argument_list|)
operator|.
name|query
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_HAVING
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
name|responseModel
argument_list|)
operator|.
name|invoke
argument_list|(
name|HttpMethod
operator|.
name|GET
argument_list|)
return|;
block|}
block|}
end_class

end_unit

