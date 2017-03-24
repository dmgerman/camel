begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.viwilo.camel
package|package
name|com
operator|.
name|viwilo
operator|.
name|camel
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
name|com
operator|.
name|viwilo
operator|.
name|camel
operator|.
name|constants
operator|.
name|DigitalOceanResources
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
name|impl
operator|.
name|UriEndpointComponent
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
comment|/**  * Represents the component that manages {@link DigitalOceanEndpoint}.  */
end_comment

begin_class
DECL|class|DigitalOceanComponent
specifier|public
class|class
name|DigitalOceanComponent
extends|extends
name|UriEndpointComponent
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|DigitalOceanComponent
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|DigitalOceanComponent ()
specifier|public
name|DigitalOceanComponent
parameter_list|()
block|{
name|super
argument_list|(
name|DigitalOceanEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|DigitalOceanComponent (CamelContext context)
specifier|public
name|DigitalOceanComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|DigitalOceanEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
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
name|DigitalOceanConfiguration
name|configuration
init|=
operator|new
name|DigitalOceanConfiguration
argument_list|()
decl_stmt|;
name|setProperties
argument_list|(
name|configuration
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setResource
argument_list|(
name|DigitalOceanResources
operator|.
name|valueOf
argument_list|(
name|remaining
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|configuration
operator|.
name|getOAuthToken
argument_list|()
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|configuration
operator|.
name|getDigitalOceanClient
argument_list|()
argument_list|)
condition|)
throw|throw
operator|new
name|DigitalOceanException
argument_list|(
literal|"oAuthToken or digitalOceanClient must be specified"
argument_list|)
throw|;
name|Endpoint
name|endpoint
init|=
operator|new
name|DigitalOceanEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|configuration
argument_list|)
decl_stmt|;
return|return
name|endpoint
return|;
block|}
block|}
end_class

end_unit

