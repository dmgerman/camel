begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.cloud
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|cloud
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
name|AsyncCallback
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
name|AsyncProcessor
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
name|Exchange
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
name|ExchangePattern
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
name|Expression
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
name|Message
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
name|cloud
operator|.
name|LoadBalancer
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
name|cloud
operator|.
name|ServiceDefinition
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
name|processor
operator|.
name|SendDynamicProcessor
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
name|ServiceSupport
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
name|AsyncProcessorHelper
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
name|ServiceHelper
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
name|StringHelper
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

begin_class
DECL|class|DefaultServiceCallProcessor
specifier|public
class|class
name|DefaultServiceCallProcessor
extends|extends
name|ServiceSupport
implements|implements
name|AsyncProcessor
block|{
DECL|field|LOGGER
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|DefaultServiceCallProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|exchangePattern
specifier|private
specifier|final
name|ExchangePattern
name|exchangePattern
decl_stmt|;
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
DECL|field|scheme
specifier|private
specifier|final
name|String
name|scheme
decl_stmt|;
DECL|field|uri
specifier|private
specifier|final
name|String
name|uri
decl_stmt|;
DECL|field|contextPath
specifier|private
specifier|final
name|String
name|contextPath
decl_stmt|;
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|loadBalancer
specifier|private
specifier|final
name|LoadBalancer
name|loadBalancer
decl_stmt|;
DECL|field|expression
specifier|private
specifier|final
name|Expression
name|expression
decl_stmt|;
DECL|field|processor
specifier|private
name|SendDynamicProcessor
name|processor
decl_stmt|;
DECL|method|DefaultServiceCallProcessor ( CamelContext camelContext, String name, String scheme, String uri, ExchangePattern exchangePattern, LoadBalancer loadBalancer, Expression expression)
specifier|public
name|DefaultServiceCallProcessor
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|scheme
parameter_list|,
name|String
name|uri
parameter_list|,
name|ExchangePattern
name|exchangePattern
parameter_list|,
name|LoadBalancer
name|loadBalancer
parameter_list|,
name|Expression
name|expression
parameter_list|)
block|{
name|this
operator|.
name|uri
operator|=
name|uri
expr_stmt|;
name|this
operator|.
name|exchangePattern
operator|=
name|exchangePattern
expr_stmt|;
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|loadBalancer
operator|=
name|loadBalancer
expr_stmt|;
comment|// setup from the provided name which can contain scheme and context-path information as well
name|String
name|serviceName
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|contains
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|serviceName
operator|=
name|StringHelper
operator|.
name|before
argument_list|(
name|name
argument_list|,
literal|"/"
argument_list|)
expr_stmt|;
name|this
operator|.
name|contextPath
operator|=
name|StringHelper
operator|.
name|after
argument_list|(
name|name
argument_list|,
literal|"/"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|name
operator|.
name|contains
argument_list|(
literal|"?"
argument_list|)
condition|)
block|{
name|serviceName
operator|=
name|StringHelper
operator|.
name|before
argument_list|(
name|name
argument_list|,
literal|"?"
argument_list|)
expr_stmt|;
name|this
operator|.
name|contextPath
operator|=
name|StringHelper
operator|.
name|after
argument_list|(
name|name
argument_list|,
literal|"?"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|serviceName
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|contextPath
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|serviceName
operator|.
name|contains
argument_list|(
literal|":"
argument_list|)
condition|)
block|{
name|this
operator|.
name|scheme
operator|=
name|StringHelper
operator|.
name|before
argument_list|(
name|serviceName
argument_list|,
literal|":"
argument_list|)
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|StringHelper
operator|.
name|after
argument_list|(
name|serviceName
argument_list|,
literal|":"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|scheme
operator|=
name|scheme
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|serviceName
expr_stmt|;
block|}
name|this
operator|.
name|expression
operator|=
name|expression
expr_stmt|;
block|}
comment|// *************************************
comment|// Properties
comment|// *************************************
DECL|method|getExchangePattern ()
specifier|public
name|ExchangePattern
name|getExchangePattern
parameter_list|()
block|{
return|return
name|exchangePattern
return|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|getScheme ()
specifier|public
name|String
name|getScheme
parameter_list|()
block|{
return|return
name|scheme
return|;
block|}
DECL|method|getUri ()
specifier|public
name|String
name|getUri
parameter_list|()
block|{
return|return
name|uri
return|;
block|}
DECL|method|getContextPath ()
specifier|public
name|String
name|getContextPath
parameter_list|()
block|{
return|return
name|contextPath
return|;
block|}
DECL|method|getLoadBalancer ()
specifier|public
name|LoadBalancer
name|getLoadBalancer
parameter_list|()
block|{
return|return
name|loadBalancer
return|;
block|}
DECL|method|getExpression ()
specifier|public
name|Expression
name|getExpression
parameter_list|()
block|{
return|return
name|expression
return|;
block|}
comment|// *************************************
comment|// Lifecycle
comment|// *************************************
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|StringHelper
operator|.
name|notEmpty
argument_list|(
name|name
argument_list|,
literal|"name"
argument_list|,
literal|"service name"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|camelContext
argument_list|,
literal|"camel context"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|expression
argument_list|,
literal|"expression"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|loadBalancer
argument_list|,
literal|"load balancer"
argument_list|)
expr_stmt|;
name|processor
operator|=
operator|new
name|SendDynamicProcessor
argument_list|(
name|uri
argument_list|,
name|expression
argument_list|)
expr_stmt|;
name|processor
operator|.
name|setCamelContext
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
if|if
condition|(
name|exchangePattern
operator|!=
literal|null
condition|)
block|{
name|processor
operator|.
name|setPattern
argument_list|(
name|exchangePattern
argument_list|)
expr_stmt|;
block|}
comment|// Start services if needed
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|processor
argument_list|)
expr_stmt|;
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|loadBalancer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Stop services if needed
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|loadBalancer
argument_list|)
expr_stmt|;
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|processor
argument_list|)
expr_stmt|;
block|}
comment|// *************************************
comment|// Processor
comment|// *************************************
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|AsyncProcessorHelper
operator|.
name|process
argument_list|(
name|this
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (final Exchange exchange, final AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|)
block|{
name|Message
name|message
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|ServiceCallConstants
operator|.
name|SERVICE_CALL_URI
argument_list|,
name|uri
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|ServiceCallConstants
operator|.
name|SERVICE_CALL_CONTEXT_PATH
argument_list|,
name|contextPath
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|ServiceCallConstants
operator|.
name|SERVICE_CALL_SCHEME
argument_list|,
name|scheme
argument_list|)
expr_stmt|;
name|String
name|serviceName
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|ServiceCallConstants
operator|.
name|SERVICE_NAME
argument_list|,
name|name
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
return|return
name|loadBalancer
operator|.
name|process
argument_list|(
name|serviceName
argument_list|,
name|server
lambda|->
name|execute
argument_list|(
name|server
argument_list|,
name|exchange
argument_list|,
name|callback
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
DECL|method|execute (ServiceDefinition server, Exchange exchange, AsyncCallback callback)
specifier|private
name|boolean
name|execute
parameter_list|(
name|ServiceDefinition
name|server
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|host
init|=
name|server
operator|.
name|getHost
argument_list|()
decl_stmt|;
name|int
name|port
init|=
name|server
operator|.
name|getPort
argument_list|()
decl_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Service {} active at server: {}:{}"
argument_list|,
name|name
argument_list|,
name|host
argument_list|,
name|port
argument_list|)
expr_stmt|;
comment|// set selected server as header
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|ServiceCallConstants
operator|.
name|SERVICE_HOST
argument_list|,
name|host
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|ServiceCallConstants
operator|.
name|SERVICE_PORT
argument_list|,
name|port
operator|>
literal|0
condition|?
name|port
else|:
literal|null
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|ServiceCallConstants
operator|.
name|SERVICE_NAME
argument_list|,
name|server
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|ServiceCallConstants
operator|.
name|SERVICE_META
argument_list|,
name|server
operator|.
name|getMetadata
argument_list|()
argument_list|)
expr_stmt|;
comment|// use the dynamic send processor to call the service
return|return
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
return|;
block|}
block|}
end_class

end_unit

