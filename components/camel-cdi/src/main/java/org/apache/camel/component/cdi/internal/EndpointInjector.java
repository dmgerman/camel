begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cdi.internal
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cdi
operator|.
name|internal
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|Produces
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|InjectionPoint
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
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
name|EndpointInject
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
name|cdi
operator|.
name|Mock
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
name|mock
operator|.
name|MockEndpoint
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

begin_comment
comment|/**  * Injects endpoints into beans  */
end_comment

begin_class
DECL|class|EndpointInjector
specifier|public
class|class
name|EndpointInjector
block|{
annotation|@
name|Inject
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
annotation|@
name|Produces
annotation|@
name|Mock
DECL|method|createMockEndpoint (InjectionPoint point)
specifier|protected
name|MockEndpoint
name|createMockEndpoint
parameter_list|(
name|InjectionPoint
name|point
parameter_list|)
block|{
name|String
name|url
init|=
literal|""
decl_stmt|;
name|String
name|name
init|=
literal|""
decl_stmt|;
name|EndpointInject
name|annotation
init|=
name|point
operator|.
name|getAnnotated
argument_list|()
operator|.
name|getAnnotation
argument_list|(
name|EndpointInject
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|annotation
operator|!=
literal|null
condition|)
block|{
name|url
operator|=
name|annotation
operator|.
name|uri
argument_list|()
expr_stmt|;
name|name
operator|=
name|annotation
operator|.
name|ref
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|name
operator|=
name|point
operator|.
name|getMember
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|url
argument_list|)
condition|)
block|{
name|url
operator|=
literal|"mock:"
operator|+
name|name
expr_stmt|;
block|}
return|return
name|camelContext
operator|.
name|getEndpoint
argument_list|(
name|url
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
return|;
block|}
annotation|@
name|Produces
DECL|method|createEndpoint (InjectionPoint point)
specifier|public
name|Endpoint
name|createEndpoint
parameter_list|(
name|InjectionPoint
name|point
parameter_list|)
block|{
name|Class
argument_list|<
name|?
extends|extends
name|Endpoint
argument_list|>
name|endpointType
init|=
name|Endpoint
operator|.
name|class
decl_stmt|;
name|Type
name|pointType
init|=
name|point
operator|.
name|getType
argument_list|()
decl_stmt|;
if|if
condition|(
name|pointType
operator|instanceof
name|Class
argument_list|<
name|?
argument_list|>
condition|)
block|{
name|endpointType
operator|=
operator|(
name|Class
argument_list|<
name|?
extends|extends
name|Endpoint
argument_list|>
operator|)
name|pointType
expr_stmt|;
block|}
name|EndpointInject
name|annotation
init|=
name|point
operator|.
name|getAnnotated
argument_list|()
operator|.
name|getAnnotation
argument_list|(
name|EndpointInject
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|annotation
operator|!=
literal|null
condition|)
block|{
name|String
name|uri
init|=
name|annotation
operator|.
name|uri
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|uri
argument_list|)
condition|)
block|{
return|return
name|camelContext
operator|.
name|getEndpoint
argument_list|(
name|uri
argument_list|,
name|endpointType
argument_list|)
return|;
block|}
name|String
name|ref
init|=
name|annotation
operator|.
name|ref
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|ref
argument_list|)
condition|)
block|{
return|return
name|camelContext
operator|.
name|getEndpoint
argument_list|(
literal|"ref:"
operator|+
name|ref
argument_list|,
name|endpointType
argument_list|)
return|;
block|}
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Could not create instance of Endpoint for the given injection point "
operator|+
name|point
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

