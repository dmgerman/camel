begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.etcd.processor.remote
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|etcd
operator|.
name|processor
operator|.
name|remote
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Objects
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
import|;
end_import

begin_import
import|import
name|mousio
operator|.
name|etcd4j
operator|.
name|requests
operator|.
name|EtcdKeyGetRequest
import|;
end_import

begin_import
import|import
name|mousio
operator|.
name|etcd4j
operator|.
name|responses
operator|.
name|EtcdKeysResponse
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
name|RuntimeCamelException
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
name|etcd
operator|.
name|EtcdConfiguration
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
name|ServiceCallServer
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
DECL|class|EtcdServiceCallServerListStrategies
specifier|public
specifier|final
class|class
name|EtcdServiceCallServerListStrategies
block|{
DECL|method|EtcdServiceCallServerListStrategies ()
specifier|private
name|EtcdServiceCallServerListStrategies
parameter_list|()
block|{     }
DECL|class|OnDemand
specifier|public
specifier|static
specifier|final
class|class
name|OnDemand
extends|extends
name|EtcdServiceCallServerListStrategy
block|{
DECL|method|OnDemand (EtcdConfiguration configuration)
specifier|public
name|OnDemand
parameter_list|(
name|EtcdConfiguration
name|configuration
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getUpdatedListOfServers (String name)
specifier|public
name|List
argument_list|<
name|ServiceCallServer
argument_list|>
name|getUpdatedListOfServers
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|List
argument_list|<
name|ServiceCallServer
argument_list|>
name|servers
init|=
name|Collections
operator|.
name|emptyList
argument_list|()
decl_stmt|;
try|try
block|{
specifier|final
name|EtcdConfiguration
name|conf
init|=
name|getConfiguration
argument_list|()
decl_stmt|;
specifier|final
name|EtcdKeyGetRequest
name|request
init|=
name|getClient
argument_list|()
operator|.
name|get
argument_list|(
name|conf
operator|.
name|getServicePath
argument_list|()
argument_list|)
operator|.
name|recursive
argument_list|()
decl_stmt|;
if|if
condition|(
name|conf
operator|.
name|hasTimeout
argument_list|()
condition|)
block|{
name|request
operator|.
name|timeout
argument_list|(
name|conf
operator|.
name|getTimeout
argument_list|()
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
specifier|final
name|EtcdKeysResponse
name|response
init|=
name|request
operator|.
name|send
argument_list|()
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|Objects
operator|.
name|nonNull
argument_list|(
name|response
operator|.
name|node
argument_list|)
operator|&&
operator|!
name|response
operator|.
name|node
operator|.
name|nodes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|servers
operator|=
name|response
operator|.
name|node
operator|.
name|nodes
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|node
lambda|->
name|node
operator|.
name|value
argument_list|)
operator|.
name|filter
argument_list|(
name|ObjectHelper
operator|::
name|isNotEmpty
argument_list|)
operator|.
name|map
argument_list|(
name|this
operator|::
name|nodeFromString
argument_list|)
operator|.
name|filter
argument_list|(
name|Objects
operator|::
name|nonNull
argument_list|)
operator|.
name|filter
argument_list|(
name|s
lambda|->
name|name
operator|.
name|equalsIgnoreCase
argument_list|(
name|s
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
operator|.
name|sorted
argument_list|(
name|EtcdServiceCallServer
operator|.
name|COMPARATOR
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
return|return
name|servers
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"OnDemand"
return|;
block|}
block|}
comment|// *************************************************************************
comment|// Helpers
comment|// *************************************************************************
DECL|method|onDemand (EtcdConfiguration configuration)
specifier|public
specifier|static
name|EtcdServiceCallServerListStrategy
name|onDemand
parameter_list|(
name|EtcdConfiguration
name|configuration
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|OnDemand
argument_list|(
name|configuration
argument_list|)
return|;
block|}
block|}
end_class

end_unit

