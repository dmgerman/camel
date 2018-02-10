begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.controlbus
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|controlbus
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanServer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|ObjectName
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
name|Route
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
name|ServiceStatus
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
name|builder
operator|.
name|ExpressionBuilder
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
name|DefaultAsyncProducer
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
name|Language
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
name|CamelLogger
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
name|ExchangeHelper
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
comment|/**  * The control bus producer.  */
end_comment

begin_class
DECL|class|ControlBusProducer
specifier|public
class|class
name|ControlBusProducer
extends|extends
name|DefaultAsyncProducer
block|{
DECL|field|ROUTE_ID_EXPRESSION
specifier|private
specifier|static
specifier|final
name|Expression
name|ROUTE_ID_EXPRESSION
init|=
name|ExpressionBuilder
operator|.
name|routeIdExpression
argument_list|()
decl_stmt|;
DECL|field|logger
specifier|private
specifier|final
name|CamelLogger
name|logger
decl_stmt|;
DECL|method|ControlBusProducer (Endpoint endpoint, CamelLogger logger)
specifier|public
name|ControlBusProducer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|CamelLogger
name|logger
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|logger
operator|=
name|logger
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|ControlBusEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|ControlBusEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange, AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|getLanguage
argument_list|()
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|processByLanguage
argument_list|(
name|exchange
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getLanguage
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
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|getAction
argument_list|()
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|processByAction
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
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
block|}
block|}
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
DECL|method|processByLanguage (Exchange exchange, Language language)
specifier|protected
name|void
name|processByLanguage
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Language
name|language
parameter_list|)
throws|throws
name|Exception
block|{
name|LanguageTask
name|task
init|=
operator|new
name|LanguageTask
argument_list|(
name|exchange
argument_list|,
name|language
argument_list|)
decl_stmt|;
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|isAsync
argument_list|()
condition|)
block|{
name|getEndpoint
argument_list|()
operator|.
name|getComponent
argument_list|()
operator|.
name|getExecutorService
argument_list|()
operator|.
name|submit
argument_list|(
name|task
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|task
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|processByAction (Exchange exchange)
specifier|protected
name|void
name|processByAction
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|ActionTask
name|task
init|=
operator|new
name|ActionTask
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|isAsync
argument_list|()
condition|)
block|{
name|getEndpoint
argument_list|()
operator|.
name|getComponent
argument_list|()
operator|.
name|getExecutorService
argument_list|()
operator|.
name|submit
argument_list|(
name|task
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|task
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Tasks to run when processing by language.      */
DECL|class|LanguageTask
specifier|private
specifier|final
class|class
name|LanguageTask
implements|implements
name|Runnable
block|{
DECL|field|exchange
specifier|private
specifier|final
name|Exchange
name|exchange
decl_stmt|;
DECL|field|language
specifier|private
specifier|final
name|Language
name|language
decl_stmt|;
DECL|method|LanguageTask (Exchange exchange, Language language)
specifier|private
name|LanguageTask
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Language
name|language
parameter_list|)
block|{
name|this
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
name|this
operator|.
name|language
operator|=
name|language
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
name|String
name|task
init|=
literal|null
decl_stmt|;
name|Object
name|result
init|=
literal|null
decl_stmt|;
try|try
block|{
comment|// create dummy exchange
name|Exchange
name|dummy
init|=
name|ExchangeHelper
operator|.
name|createCopy
argument_list|(
name|exchange
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|task
operator|=
name|dummy
operator|.
name|getIn
argument_list|()
operator|.
name|getMandatoryBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|task
operator|!=
literal|null
condition|)
block|{
name|Expression
name|exp
init|=
name|language
operator|.
name|createExpression
argument_list|(
name|task
argument_list|)
decl_stmt|;
name|result
operator|=
name|exp
operator|.
name|evaluate
argument_list|(
name|dummy
argument_list|,
name|Object
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|result
operator|!=
literal|null
operator|&&
operator|!
name|getEndpoint
argument_list|()
operator|.
name|isAsync
argument_list|()
condition|)
block|{
comment|// can only set result on exchange if sync
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|task
operator|!=
literal|null
condition|)
block|{
name|logger
operator|.
name|log
argument_list|(
literal|"ControlBus task done ["
operator|+
name|task
operator|+
literal|"] with result -> "
operator|+
operator|(
name|result
operator|!=
literal|null
condition|?
name|result
else|:
literal|"void"
operator|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|logger
operator|.
name|log
argument_list|(
literal|"Error executing ControlBus task ["
operator|+
name|task
operator|+
literal|"]. This exception will be ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Tasks to run when processing by route action.      */
DECL|class|ActionTask
specifier|private
specifier|final
class|class
name|ActionTask
implements|implements
name|Runnable
block|{
DECL|field|exchange
specifier|private
specifier|final
name|Exchange
name|exchange
decl_stmt|;
DECL|method|ActionTask (Exchange exchange)
specifier|private
name|ActionTask
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|this
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
name|String
name|action
init|=
name|getEndpoint
argument_list|()
operator|.
name|getAction
argument_list|()
decl_stmt|;
name|String
name|id
init|=
name|getEndpoint
argument_list|()
operator|.
name|getRouteId
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
literal|"current"
argument_list|,
name|id
argument_list|)
condition|)
block|{
name|id
operator|=
name|ROUTE_ID_EXPRESSION
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
name|Object
name|result
init|=
literal|null
decl_stmt|;
name|String
name|task
init|=
name|action
operator|+
literal|" route "
operator|+
name|id
decl_stmt|;
try|try
block|{
if|if
condition|(
literal|"start"
operator|.
name|equals
argument_list|(
name|action
argument_list|)
condition|)
block|{
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|startRoute
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"stop"
operator|.
name|equals
argument_list|(
name|action
argument_list|)
condition|)
block|{
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|stopRoute
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"suspend"
operator|.
name|equals
argument_list|(
name|action
argument_list|)
condition|)
block|{
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|suspendRoute
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"resume"
operator|.
name|equals
argument_list|(
name|action
argument_list|)
condition|)
block|{
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|resumeRoute
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"restart"
operator|.
name|equals
argument_list|(
name|action
argument_list|)
condition|)
block|{
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|stopRoute
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|int
name|delay
init|=
name|getEndpoint
argument_list|()
operator|.
name|getRestartDelay
argument_list|()
decl_stmt|;
try|try
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Sleeping {} ms before starting route"
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
name|delay
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|startRoute
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"status"
operator|.
name|equals
argument_list|(
name|action
argument_list|)
condition|)
block|{
name|ServiceStatus
name|status
init|=
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getRouteStatus
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|status
operator|!=
literal|null
condition|)
block|{
name|result
operator|=
name|status
operator|.
name|name
argument_list|()
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
literal|"stats"
operator|.
name|equals
argument_list|(
name|action
argument_list|)
condition|)
block|{
comment|// camel context or per route
name|String
name|name
init|=
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getManagementName
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
name|result
operator|=
literal|"JMX is disabled, cannot get stats"
expr_stmt|;
block|}
else|else
block|{
name|ObjectName
name|on
decl_stmt|;
name|String
name|operation
decl_stmt|;
if|if
condition|(
name|id
operator|==
literal|null
condition|)
block|{
name|CamelContext
name|camelContext
init|=
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
decl_stmt|;
name|on
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getManagementNamingStrategy
argument_list|()
operator|.
name|getObjectNameForCamelContext
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
name|operation
operator|=
literal|"dumpRoutesStatsAsXml"
expr_stmt|;
block|}
else|else
block|{
name|Route
name|route
init|=
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getRoute
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|on
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getManagementNamingStrategy
argument_list|()
operator|.
name|getObjectNameForRoute
argument_list|(
name|route
argument_list|)
expr_stmt|;
name|operation
operator|=
literal|"dumpRouteStatsAsXml"
expr_stmt|;
block|}
if|if
condition|(
name|on
operator|!=
literal|null
condition|)
block|{
name|MBeanServer
name|server
init|=
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getManagementAgent
argument_list|()
operator|.
name|getMBeanServer
argument_list|()
decl_stmt|;
name|result
operator|=
name|server
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
name|operation
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|true
block|,
literal|true
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"boolean"
block|,
literal|"boolean"
block|}
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|result
operator|=
literal|"Cannot lookup route with id "
operator|+
name|id
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|result
operator|!=
literal|null
operator|&&
operator|!
name|getEndpoint
argument_list|()
operator|.
name|isAsync
argument_list|()
condition|)
block|{
comment|// can only set result on exchange if sync
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
name|logger
operator|.
name|log
argument_list|(
literal|"ControlBus task done ["
operator|+
name|task
operator|+
literal|"] with result -> "
operator|+
operator|(
name|result
operator|!=
literal|null
condition|?
name|result
else|:
literal|"void"
operator|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|logger
operator|.
name|log
argument_list|(
literal|"Error executing ControlBus task ["
operator|+
name|task
operator|+
literal|"]. This exception will be ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

