begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ignite.queue
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ignite
operator|.
name|queue
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

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
name|component
operator|.
name|ignite
operator|.
name|AbstractIgniteComponent
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
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|URISupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ignite
operator|.
name|Ignite
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ignite
operator|.
name|Ignition
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ignite
operator|.
name|configuration
operator|.
name|IgniteConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * The Ignite Queue Component.  */
end_comment

begin_class
DECL|class|IgniteQueueComponent
specifier|public
class|class
name|IgniteQueueComponent
extends|extends
name|AbstractIgniteComponent
block|{
DECL|method|fromIgnite (Ignite ignite)
specifier|public
specifier|static
name|IgniteQueueComponent
name|fromIgnite
parameter_list|(
name|Ignite
name|ignite
parameter_list|)
block|{
name|IgniteQueueComponent
name|answer
init|=
operator|new
name|IgniteQueueComponent
argument_list|()
decl_stmt|;
name|answer
operator|.
name|setIgnite
argument_list|(
name|ignite
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|fromConfiguration (IgniteConfiguration configuration)
specifier|public
specifier|static
name|IgniteQueueComponent
name|fromConfiguration
parameter_list|(
name|IgniteConfiguration
name|configuration
parameter_list|)
block|{
name|IgniteQueueComponent
name|answer
init|=
operator|new
name|IgniteQueueComponent
argument_list|()
decl_stmt|;
name|answer
operator|.
name|setIgniteConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|fromInputStream (InputStream inputStream)
specifier|public
specifier|static
name|IgniteQueueComponent
name|fromInputStream
parameter_list|(
name|InputStream
name|inputStream
parameter_list|)
block|{
name|IgniteQueueComponent
name|answer
init|=
operator|new
name|IgniteQueueComponent
argument_list|()
decl_stmt|;
name|answer
operator|.
name|setConfigurationResource
argument_list|(
name|inputStream
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|fromUrl (URL url)
specifier|public
specifier|static
name|IgniteQueueComponent
name|fromUrl
parameter_list|(
name|URL
name|url
parameter_list|)
block|{
name|IgniteQueueComponent
name|answer
init|=
operator|new
name|IgniteQueueComponent
argument_list|()
decl_stmt|;
name|answer
operator|.
name|setConfigurationResource
argument_list|(
name|url
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|fromLocation (String location)
specifier|public
specifier|static
name|IgniteQueueComponent
name|fromLocation
parameter_list|(
name|String
name|location
parameter_list|)
block|{
name|IgniteQueueComponent
name|answer
init|=
operator|new
name|IgniteQueueComponent
argument_list|()
decl_stmt|;
name|answer
operator|.
name|setConfigurationResource
argument_list|(
name|location
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
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
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
literal|"Camel Context"
argument_list|)
expr_stmt|;
name|IgniteQueueEndpoint
name|answer
init|=
operator|new
name|IgniteQueueEndpoint
argument_list|(
name|uri
argument_list|,
name|remaining
argument_list|,
name|parameters
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|setProperties
argument_list|(
name|answer
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

