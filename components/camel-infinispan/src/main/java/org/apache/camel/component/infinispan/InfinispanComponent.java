begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.infinispan
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|infinispan
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|Metadata
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
name|annotations
operator|.
name|Component
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
name|DefaultComponent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|commons
operator|.
name|api
operator|.
name|BasicCacheContainer
import|;
end_import

begin_class
annotation|@
name|Component
argument_list|(
literal|"infinispan"
argument_list|)
DECL|class|InfinispanComponent
specifier|public
class|class
name|InfinispanComponent
extends|extends
name|DefaultComponent
block|{
annotation|@
name|Metadata
argument_list|(
name|description
operator|=
literal|"Default configuration"
argument_list|)
DECL|field|configuration
specifier|private
name|InfinispanConfiguration
name|configuration
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|description
operator|=
literal|"Default Cache container"
argument_list|)
DECL|field|cacheContainer
specifier|private
name|BasicCacheContainer
name|cacheContainer
decl_stmt|;
DECL|method|InfinispanComponent ()
specifier|public
name|InfinispanComponent
parameter_list|()
block|{     }
DECL|method|InfinispanComponent (CamelContext camelContext)
specifier|public
name|InfinispanComponent
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
DECL|method|getConfiguration ()
specifier|public
name|InfinispanConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
comment|/**      * The default configuration shared among endpoints.      */
DECL|method|setConfiguration (InfinispanConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|InfinispanConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|getCacheContainer ()
specifier|public
name|BasicCacheContainer
name|getCacheContainer
parameter_list|()
block|{
return|return
name|cacheContainer
return|;
block|}
comment|/**      * The default cache container.      */
DECL|method|setCacheContainer (BasicCacheContainer cacheContainer)
specifier|public
name|void
name|setCacheContainer
parameter_list|(
name|BasicCacheContainer
name|cacheContainer
parameter_list|)
block|{
name|this
operator|.
name|cacheContainer
operator|=
name|cacheContainer
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|InfinispanConfiguration
name|conf
decl_stmt|;
if|if
condition|(
name|configuration
operator|!=
literal|null
condition|)
block|{
name|conf
operator|=
name|configuration
operator|.
name|copy
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|conf
operator|=
operator|new
name|InfinispanConfiguration
argument_list|()
expr_stmt|;
block|}
name|conf
operator|.
name|setCacheContainer
argument_list|(
name|cacheContainer
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|conf
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
operator|new
name|InfinispanEndpoint
argument_list|(
name|uri
argument_list|,
name|remaining
argument_list|,
name|this
argument_list|,
name|conf
argument_list|)
return|;
block|}
block|}
end_class

end_unit

