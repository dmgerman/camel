begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_class
DECL|class|FujiServiceNowProcessor
specifier|public
specifier|abstract
class|class
name|FujiServiceNowProcessor
extends|extends
name|AbstractServiceNowProcessor
block|{
DECL|method|FujiServiceNowProcessor (ServiceNowEndpoint endpoint)
specifier|protected
name|FujiServiceNowProcessor
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
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
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
name|apiVersion
init|=
name|getApiVersion
argument_list|(
name|in
argument_list|)
decl_stmt|;
specifier|final
name|String
name|action
init|=
name|in
operator|.
name|getHeader
argument_list|(
name|ServiceNowConstants
operator|.
name|ACTION
argument_list|,
name|String
operator|.
name|class
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
name|doProcess
argument_list|(
name|exchange
argument_list|,
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|requestModel
argument_list|,
literal|"requestModel"
argument_list|)
argument_list|,
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|responseModel
argument_list|,
literal|"responseModel"
argument_list|)
argument_list|,
name|apiVersion
argument_list|,
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|action
argument_list|,
literal|"action"
argument_list|)
argument_list|,
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|tableName
argument_list|,
literal|"tableName"
argument_list|)
argument_list|,
name|sysId
argument_list|)
expr_stmt|;
block|}
DECL|method|doProcess ( Exchange exchange, Class<?> requestModel, Class<?> responseModel, String apiVersion, String action, String tableName, String sysId)
specifier|protected
specifier|abstract
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
name|apiVersion
parameter_list|,
name|String
name|action
parameter_list|,
name|String
name|tableName
parameter_list|,
name|String
name|sysId
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_class

end_unit

