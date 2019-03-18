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
name|HashMap
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
name|Endpoint
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
name|EndpointRegistry
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
name|support
operator|.
name|LRUCacheFactory
import|;
end_import

begin_comment
comment|/**  * A provisional (temporary) {@link EndpointRegistry} that is only used during startup of Apache Camel to  * make starting Camel faster while {@link LRUCacheFactory} is warming up etc.  */
end_comment

begin_class
DECL|class|ProvisionalEndpointRegistry
class|class
name|ProvisionalEndpointRegistry
extends|extends
name|HashMap
argument_list|<
name|EndpointKey
argument_list|,
name|Endpoint
argument_list|>
implements|implements
name|EndpointRegistry
argument_list|<
name|EndpointKey
argument_list|>
block|{
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|staticSize ()
specifier|public
name|int
name|staticSize
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
annotation|@
name|Override
DECL|method|dynamicSize ()
specifier|public
name|int
name|dynamicSize
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
annotation|@
name|Override
DECL|method|getMaximumCacheSize ()
specifier|public
name|int
name|getMaximumCacheSize
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
annotation|@
name|Override
DECL|method|purge ()
specifier|public
name|void
name|purge
parameter_list|()
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|isStatic (String key)
specifier|public
name|boolean
name|isStatic
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|isDynamic (String key)
specifier|public
name|boolean
name|isDynamic
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|cleanUp ()
specifier|public
name|void
name|cleanUp
parameter_list|()
block|{
comment|// noop
block|}
block|}
end_class

end_unit

