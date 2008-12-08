begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.guice.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|guice
operator|.
name|impl
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
name|AnnotatedElement
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Field
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Objects
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Provider
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|spi
operator|.
name|AnnotationProviderFactory
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|spi
operator|.
name|InjectionAnnotation
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
name|impl
operator|.
name|CamelPostProcessorHelper
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
comment|/**  * Injects values into the {@link EndpointInject} injection point  *  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|InjectionAnnotation
argument_list|(
name|EndpointInject
operator|.
name|class
argument_list|)
DECL|class|EndpointInjector
specifier|public
class|class
name|EndpointInjector
extends|extends
name|CamelPostProcessorHelper
implements|implements
name|AnnotationProviderFactory
block|{
annotation|@
name|Inject
DECL|method|EndpointInjector (CamelContext camelContext)
specifier|public
name|EndpointInjector
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
DECL|method|createProvider (final AnnotatedElement member)
specifier|public
name|Provider
name|createProvider
parameter_list|(
specifier|final
name|AnnotatedElement
name|member
parameter_list|)
block|{
specifier|final
name|EndpointInject
name|inject
init|=
name|member
operator|.
name|getAnnotation
argument_list|(
name|EndpointInject
operator|.
name|class
argument_list|)
decl_stmt|;
name|Objects
operator|.
name|nonNull
argument_list|(
name|inject
argument_list|,
literal|"@EndpointInject is not present!"
argument_list|)
expr_stmt|;
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|type
decl_stmt|;
specifier|final
name|String
name|injectionPointName
decl_stmt|;
specifier|final
name|String
name|endpointRef
init|=
name|inject
operator|.
name|name
argument_list|()
decl_stmt|;
specifier|final
name|String
name|uri
init|=
name|inject
operator|.
name|uri
argument_list|()
decl_stmt|;
if|if
condition|(
name|member
operator|instanceof
name|Field
condition|)
block|{
name|Field
name|field
init|=
operator|(
name|Field
operator|)
name|member
decl_stmt|;
name|type
operator|=
name|field
operator|.
name|getType
argument_list|()
expr_stmt|;
name|injectionPointName
operator|=
name|field
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|member
operator|instanceof
name|Method
condition|)
block|{
name|Method
name|method
init|=
operator|(
name|Method
operator|)
name|member
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|parameterTypes
init|=
name|method
operator|.
name|getParameterTypes
argument_list|()
decl_stmt|;
if|if
condition|(
name|parameterTypes
operator|.
name|length
operator|==
literal|1
condition|)
block|{
name|type
operator|=
name|parameterTypes
index|[
literal|0
index|]
expr_stmt|;
name|injectionPointName
operator|=
name|ObjectHelper
operator|.
name|getPropertyName
argument_list|(
name|method
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Only a single method parameter value supported for @EndpointInject on "
operator|+
name|method
argument_list|)
throw|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Annotated element "
operator|+
name|member
operator|+
literal|" not supported"
argument_list|)
throw|;
block|}
return|return
operator|new
name|Provider
argument_list|()
block|{
specifier|public
name|Object
name|get
parameter_list|()
block|{
return|return
name|getInjectionValue
argument_list|(
name|type
argument_list|,
name|uri
argument_list|,
name|endpointRef
argument_list|,
name|injectionPointName
argument_list|)
return|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

