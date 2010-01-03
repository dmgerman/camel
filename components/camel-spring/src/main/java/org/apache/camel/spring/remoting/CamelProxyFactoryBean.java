begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.remoting
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|remoting
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
name|CamelContextAware
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
name|Producer
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
name|bean
operator|.
name|ProxyHelper
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
name|spring
operator|.
name|util
operator|.
name|CamelContextResolverHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|BeansException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|DisposableBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|FactoryBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContextAware
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|remoting
operator|.
name|support
operator|.
name|UrlBasedRemoteAccessor
import|;
end_import

begin_comment
comment|/**  * A {@link FactoryBean} to create a Proxy to a a Camel Pojo Endpoint.  */
end_comment

begin_class
DECL|class|CamelProxyFactoryBean
specifier|public
class|class
name|CamelProxyFactoryBean
extends|extends
name|UrlBasedRemoteAccessor
implements|implements
name|FactoryBean
implements|,
name|CamelContextAware
implements|,
name|DisposableBean
implements|,
name|ApplicationContextAware
block|{
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|camelContextId
specifier|private
name|String
name|camelContextId
decl_stmt|;
DECL|field|applicationContext
specifier|private
name|ApplicationContext
name|applicationContext
decl_stmt|;
DECL|field|endpoint
specifier|private
name|Endpoint
name|endpoint
decl_stmt|;
DECL|field|serviceProxy
specifier|private
name|Object
name|serviceProxy
decl_stmt|;
DECL|field|producer
specifier|private
name|Producer
name|producer
decl_stmt|;
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|afterPropertiesSet ()
specifier|public
name|void
name|afterPropertiesSet
parameter_list|()
block|{
name|super
operator|.
name|afterPropertiesSet
argument_list|()
expr_stmt|;
try|try
block|{
if|if
condition|(
name|endpoint
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|camelContext
operator|==
literal|null
operator|&&
name|camelContextId
operator|!=
literal|null
condition|)
block|{
name|camelContext
operator|=
name|CamelContextResolverHelper
operator|.
name|getCamelContextWithId
argument_list|(
name|applicationContext
argument_list|,
name|camelContextId
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getServiceUrl
argument_list|()
operator|==
literal|null
operator|||
name|camelContext
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"If endpoint is not specified, the serviceUrl and camelContext must be specified."
argument_list|)
throw|;
block|}
name|endpoint
operator|=
name|camelContext
operator|.
name|getEndpoint
argument_list|(
name|getServiceUrl
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|endpoint
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Could not resolve endpoint: "
operator|+
name|getServiceUrl
argument_list|()
argument_list|)
throw|;
block|}
block|}
name|this
operator|.
name|producer
operator|=
name|endpoint
operator|.
name|createProducer
argument_list|()
expr_stmt|;
name|this
operator|.
name|producer
operator|.
name|start
argument_list|()
expr_stmt|;
name|this
operator|.
name|serviceProxy
operator|=
name|ProxyHelper
operator|.
name|createProxy
argument_list|(
name|endpoint
argument_list|,
name|producer
argument_list|,
name|getServiceInterface
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|destroy ()
specifier|public
name|void
name|destroy
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
name|producer
operator|.
name|stop
argument_list|()
expr_stmt|;
name|this
operator|.
name|producer
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|serviceProxy
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|getServiceInterface ()
specifier|public
name|Class
name|getServiceInterface
parameter_list|()
block|{
return|return
name|super
operator|.
name|getServiceInterface
argument_list|()
return|;
block|}
DECL|method|getServiceUrl ()
specifier|public
name|String
name|getServiceUrl
parameter_list|()
block|{
return|return
name|super
operator|.
name|getServiceUrl
argument_list|()
return|;
block|}
DECL|method|getObject ()
specifier|public
name|Object
name|getObject
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|serviceProxy
return|;
block|}
DECL|method|getObjectType ()
specifier|public
name|Class
name|getObjectType
parameter_list|()
block|{
return|return
name|getServiceInterface
argument_list|()
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|getEndpoint ()
specifier|public
name|Endpoint
name|getEndpoint
parameter_list|()
block|{
return|return
name|endpoint
return|;
block|}
DECL|method|setEndpoint (Endpoint endpoint)
specifier|public
name|void
name|setEndpoint
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
block|}
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
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
DECL|method|setApplicationContext (ApplicationContext applicationContext)
specifier|public
name|void
name|setApplicationContext
parameter_list|(
name|ApplicationContext
name|applicationContext
parameter_list|)
throws|throws
name|BeansException
block|{
name|this
operator|.
name|applicationContext
operator|=
name|applicationContext
expr_stmt|;
block|}
block|}
end_class

end_unit

