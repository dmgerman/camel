begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.consul.endpoint
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|consul
operator|.
name|endpoint
package|;
end_package

begin_import
import|import
name|com
operator|.
name|orbitz
operator|.
name|consul
operator|.
name|Consul
import|;
end_import

begin_import
import|import
name|com
operator|.
name|orbitz
operator|.
name|consul
operator|.
name|PreparedQueryClient
import|;
end_import

begin_import
import|import
name|com
operator|.
name|orbitz
operator|.
name|consul
operator|.
name|model
operator|.
name|query
operator|.
name|PreparedQuery
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
name|consul
operator|.
name|ConsulConfiguration
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
name|consul
operator|.
name|ConsulConstants
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
name|consul
operator|.
name|ConsulEndpoint
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
name|spi
operator|.
name|InvokeOnHeader
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
DECL|class|ConsulPreparedQueryProducer
specifier|public
specifier|final
class|class
name|ConsulPreparedQueryProducer
extends|extends
name|AbstractConsulProducer
argument_list|<
name|PreparedQueryClient
argument_list|>
block|{
DECL|method|ConsulPreparedQueryProducer (ConsulEndpoint endpoint, ConsulConfiguration configuration)
specifier|public
name|ConsulPreparedQueryProducer
parameter_list|(
name|ConsulEndpoint
name|endpoint
parameter_list|,
name|ConsulConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|configuration
argument_list|,
name|Consul
operator|::
name|preparedQueryClient
argument_list|)
expr_stmt|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|ConsulPreparedQueryActions
operator|.
name|CREATE
argument_list|)
DECL|method|create (Message message)
specifier|protected
name|void
name|create
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|setBodyAndResult
argument_list|(
name|message
argument_list|,
name|getClient
argument_list|()
operator|.
name|createPreparedQuery
argument_list|(
name|message
operator|.
name|getMandatoryBody
argument_list|(
name|PreparedQuery
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|ConsulPreparedQueryActions
operator|.
name|GET
argument_list|)
DECL|method|get (Message message)
specifier|protected
name|void
name|get
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|id
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|ConsulConstants
operator|.
name|CONSUL_PREPARED_QUERY_ID
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|id
argument_list|)
condition|)
block|{
name|setBodyAndResult
argument_list|(
name|message
argument_list|,
name|getClient
argument_list|()
operator|.
name|getPreparedQuery
argument_list|(
name|message
operator|.
name|getMandatoryBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setBodyAndResult
argument_list|(
name|message
argument_list|,
name|getClient
argument_list|()
operator|.
name|getPreparedQuery
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|InvokeOnHeader
argument_list|(
name|ConsulPreparedQueryActions
operator|.
name|EXECUTE
argument_list|)
DECL|method|execute (Message message)
specifier|protected
name|void
name|execute
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|id
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|ConsulConstants
operator|.
name|CONSUL_PREPARED_QUERY_ID
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|id
argument_list|)
condition|)
block|{
name|setBodyAndResult
argument_list|(
name|message
argument_list|,
name|getClient
argument_list|()
operator|.
name|execute
argument_list|(
name|message
operator|.
name|getMandatoryBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setBodyAndResult
argument_list|(
name|message
argument_list|,
name|getClient
argument_list|()
operator|.
name|execute
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

