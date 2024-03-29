begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
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
name|spi
operator|.
name|BeanProxyFactory
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
comment|/**  * A build to create Camel proxies.  */
end_comment

begin_class
DECL|class|ProxyBuilder
specifier|public
specifier|final
class|class
name|ProxyBuilder
block|{
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|endpoint
specifier|private
name|Endpoint
name|endpoint
decl_stmt|;
DECL|field|binding
specifier|private
name|boolean
name|binding
init|=
literal|true
decl_stmt|;
DECL|method|ProxyBuilder (CamelContext camelContext)
specifier|public
name|ProxyBuilder
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
comment|/**      * Send the proxied message to this endpoint      *      * @param url uri of endpoint      * @return the builder      */
DECL|method|endpoint (String url)
specifier|public
name|ProxyBuilder
name|endpoint
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
name|camelContext
operator|.
name|getEndpoint
argument_list|(
name|url
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Send the proxied message to this endpoint      *      * @param endpoint the endpoint      * @return the builder      */
DECL|method|endpoint (Endpoint endpoint)
specifier|public
name|ProxyBuilder
name|endpoint
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Builds the proxy.      *      * @param interfaceClass the service interface      * @return the proxied bean      * @throws Exception is thrown if error creating the proxy      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|build (Class<T> interfaceClass)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|build
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|interfaceClass
parameter_list|)
throws|throws
name|Exception
block|{
comment|// this method is introduced to avoid compiler warnings about the
comment|// generic Class arrays in the case we've got only one single Class
comment|// to build a Proxy for
return|return
name|build
argument_list|(
operator|(
name|Class
argument_list|<
name|T
argument_list|>
index|[]
operator|)
operator|new
name|Class
index|[]
block|{
name|interfaceClass
block|}
argument_list|)
return|;
block|}
comment|/**      * Builds the proxy.      *      * @param interfaceClasses the service interface(s)      * @return the proxied bean      * @throws Exception is thrown if error creating the proxy      */
DECL|method|build (Class<T>.... interfaceClasses)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|build
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
modifier|...
name|interfaceClasses
parameter_list|)
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|endpoint
argument_list|,
literal|"endpoint"
argument_list|)
expr_stmt|;
comment|// use proxy service
name|BeanProxyFactory
name|factory
init|=
name|camelContext
operator|.
name|adapt
argument_list|(
name|ExtendedCamelContext
operator|.
name|class
argument_list|)
operator|.
name|getBeanProxyFactory
argument_list|()
decl_stmt|;
if|if
condition|(
name|factory
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot find BeanProxyFactory. Make sure camel-bean is on the classpath."
argument_list|)
throw|;
block|}
return|return
name|factory
operator|.
name|createProxy
argument_list|(
name|endpoint
argument_list|,
name|binding
argument_list|,
name|interfaceClasses
argument_list|)
return|;
block|}
block|}
end_class

end_unit

