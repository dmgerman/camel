begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutorService
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
name|ScheduledExecutorService
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
name|CamelContext
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
name|ExtendedCamelContext
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
name|impl
operator|.
name|engine
operator|.
name|BaseExecutorServiceManager
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
name|model
operator|.
name|OptionalIdentifiedDefinition
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
name|NodeIdFactory
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
name|ThreadPoolProfile
import|;
end_import

begin_comment
comment|/**  * Default {@link org.apache.camel.spi.ExecutorServiceManager}.  */
end_comment

begin_class
DECL|class|DefaultExecutorServiceManager
specifier|public
class|class
name|DefaultExecutorServiceManager
extends|extends
name|BaseExecutorServiceManager
block|{
DECL|method|DefaultExecutorServiceManager (CamelContext camelContext)
specifier|public
name|DefaultExecutorServiceManager
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|super
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|newThreadPool (Object source, String name, ThreadPoolProfile profile)
specifier|public
name|ExecutorService
name|newThreadPool
parameter_list|(
name|Object
name|source
parameter_list|,
name|String
name|name
parameter_list|,
name|ThreadPoolProfile
name|profile
parameter_list|)
block|{
return|return
name|super
operator|.
name|newThreadPool
argument_list|(
name|forceId
argument_list|(
name|source
argument_list|)
argument_list|,
name|name
argument_list|,
name|profile
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|newCachedThreadPool (Object source, String name)
specifier|public
name|ExecutorService
name|newCachedThreadPool
parameter_list|(
name|Object
name|source
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
name|super
operator|.
name|newCachedThreadPool
argument_list|(
name|forceId
argument_list|(
name|source
argument_list|)
argument_list|,
name|name
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|newScheduledThreadPool (Object source, String name, ThreadPoolProfile profile)
specifier|public
name|ScheduledExecutorService
name|newScheduledThreadPool
parameter_list|(
name|Object
name|source
parameter_list|,
name|String
name|name
parameter_list|,
name|ThreadPoolProfile
name|profile
parameter_list|)
block|{
return|return
name|super
operator|.
name|newScheduledThreadPool
argument_list|(
name|forceId
argument_list|(
name|source
argument_list|)
argument_list|,
name|name
argument_list|,
name|profile
argument_list|)
return|;
block|}
DECL|method|forceId (Object source)
specifier|protected
name|Object
name|forceId
parameter_list|(
name|Object
name|source
parameter_list|)
block|{
if|if
condition|(
name|source
operator|instanceof
name|OptionalIdentifiedDefinition
condition|)
block|{
name|NodeIdFactory
name|factory
init|=
name|getCamelContext
argument_list|()
operator|.
name|adapt
argument_list|(
name|ExtendedCamelContext
operator|.
name|class
argument_list|)
operator|.
name|getNodeIdFactory
argument_list|()
decl_stmt|;
operator|(
operator|(
name|OptionalIdentifiedDefinition
operator|)
name|source
operator|)
operator|.
name|idOrCreate
argument_list|(
name|factory
argument_list|)
expr_stmt|;
block|}
return|return
name|source
return|;
block|}
block|}
end_class

end_unit

