begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management.mbean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
operator|.
name|mbean
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
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
name|ProducerTemplate
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
name|spi
operator|.
name|ManagementStrategy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jmx
operator|.
name|export
operator|.
name|annotation
operator|.
name|ManagedAttribute
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jmx
operator|.
name|export
operator|.
name|annotation
operator|.
name|ManagedOperation
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jmx
operator|.
name|export
operator|.
name|annotation
operator|.
name|ManagedResource
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Managed CamelContext"
argument_list|)
DECL|class|ManagedCamelContext
specifier|public
class|class
name|ManagedCamelContext
block|{
DECL|field|context
specifier|private
specifier|final
name|CamelContext
name|context
decl_stmt|;
DECL|method|ManagedCamelContext (CamelContext context)
specifier|public
name|ManagedCamelContext
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
DECL|method|init (ManagementStrategy strategy)
specifier|public
name|void
name|init
parameter_list|(
name|ManagementStrategy
name|strategy
parameter_list|)
block|{
comment|// do nothing
block|}
DECL|method|getContext ()
specifier|public
name|CamelContext
name|getContext
parameter_list|()
block|{
return|return
name|context
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Camel id"
argument_list|)
DECL|method|getCamelId ()
specifier|public
name|String
name|getCamelId
parameter_list|()
block|{
return|return
name|context
operator|.
name|getName
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Camel Version"
argument_list|)
DECL|method|getCamelVersion ()
specifier|public
name|String
name|getCamelVersion
parameter_list|()
block|{
return|return
name|context
operator|.
name|getVersion
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Camel State"
argument_list|)
DECL|method|getState ()
specifier|public
name|String
name|getState
parameter_list|()
block|{
comment|// must use String type to be sure remote JMX can read the attribute without requiring Camel classes.
name|ServiceStatus
name|status
init|=
name|context
operator|.
name|getStatus
argument_list|()
decl_stmt|;
comment|// if no status exists then its stopped
if|if
condition|(
name|status
operator|==
literal|null
condition|)
block|{
name|status
operator|=
name|ServiceStatus
operator|.
name|Stopped
expr_stmt|;
block|}
return|return
name|status
operator|.
name|name
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Uptime"
argument_list|)
DECL|method|getUptime ()
specifier|public
name|String
name|getUptime
parameter_list|()
block|{
return|return
name|context
operator|.
name|getUptime
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Camel Properties"
argument_list|)
DECL|method|getProperties ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getProperties
parameter_list|()
block|{
if|if
condition|(
name|context
operator|.
name|getProperties
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|context
operator|.
name|getProperties
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Tracing"
argument_list|)
DECL|method|getTracing ()
specifier|public
name|Boolean
name|getTracing
parameter_list|()
block|{
return|return
name|context
operator|.
name|isTracing
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Tracing"
argument_list|)
DECL|method|setTracing (Boolean tracing)
specifier|public
name|void
name|setTracing
parameter_list|(
name|Boolean
name|tracing
parameter_list|)
block|{
name|context
operator|.
name|setTracing
argument_list|(
name|tracing
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Current number of inflight Exchanges"
argument_list|)
DECL|method|getInflightExchanges ()
specifier|public
name|Integer
name|getInflightExchanges
parameter_list|()
block|{
return|return
name|context
operator|.
name|getInflightRepository
argument_list|()
operator|.
name|size
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Shutdown timeout"
argument_list|)
DECL|method|setTimeout (long timeout)
specifier|public
name|void
name|setTimeout
parameter_list|(
name|long
name|timeout
parameter_list|)
block|{
name|context
operator|.
name|getShutdownStrategy
argument_list|()
operator|.
name|setTimeout
argument_list|(
name|timeout
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Shutdown timeout"
argument_list|)
DECL|method|getTimeout ()
specifier|public
name|long
name|getTimeout
parameter_list|()
block|{
return|return
name|context
operator|.
name|getShutdownStrategy
argument_list|()
operator|.
name|getTimeout
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Shutdown timeout time unit"
argument_list|)
DECL|method|setTimeUnit (TimeUnit timeUnit)
specifier|public
name|void
name|setTimeUnit
parameter_list|(
name|TimeUnit
name|timeUnit
parameter_list|)
block|{
name|context
operator|.
name|getShutdownStrategy
argument_list|()
operator|.
name|setTimeUnit
argument_list|(
name|timeUnit
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Shutdown timeout time unit"
argument_list|)
DECL|method|getTimeUnit ()
specifier|public
name|TimeUnit
name|getTimeUnit
parameter_list|()
block|{
return|return
name|context
operator|.
name|getShutdownStrategy
argument_list|()
operator|.
name|getTimeUnit
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Whether to force shutdown now when a timeout occurred"
argument_list|)
DECL|method|setShutdownNowOnTimeout (boolean shutdownNowOnTimeout)
specifier|public
name|void
name|setShutdownNowOnTimeout
parameter_list|(
name|boolean
name|shutdownNowOnTimeout
parameter_list|)
block|{
name|context
operator|.
name|getShutdownStrategy
argument_list|()
operator|.
name|setShutdownNowOnTimeout
argument_list|(
name|shutdownNowOnTimeout
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Whether to force shutdown now when a timeout occurred"
argument_list|)
DECL|method|isShutdownNowOnTimeout ()
specifier|public
name|boolean
name|isShutdownNowOnTimeout
parameter_list|()
block|{
return|return
name|context
operator|.
name|getShutdownStrategy
argument_list|()
operator|.
name|isShutdownNowOnTimeout
argument_list|()
return|;
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Start Camel"
argument_list|)
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|context
operator|.
name|isSuspended
argument_list|()
condition|)
block|{
name|context
operator|.
name|resume
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Stop Camel (shutdown)"
argument_list|)
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Suspend Camel"
argument_list|)
DECL|method|suspend ()
specifier|public
name|void
name|suspend
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|suspend
argument_list|()
expr_stmt|;
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Resume Camel"
argument_list|)
DECL|method|resume ()
specifier|public
name|void
name|resume
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|context
operator|.
name|isSuspended
argument_list|()
condition|)
block|{
name|context
operator|.
name|resume
argument_list|()
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"CamelContext is not suspended"
argument_list|)
throw|;
block|}
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Send body (in only)"
argument_list|)
DECL|method|sendBody (String endpointUri, String body)
specifier|public
name|void
name|sendBody
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|String
name|body
parameter_list|)
throws|throws
name|Exception
block|{
name|ProducerTemplate
name|template
init|=
name|context
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
name|endpointUri
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|template
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Request body (in out)"
argument_list|)
DECL|method|requestBody (String endpointUri, String body)
specifier|public
name|Object
name|requestBody
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|String
name|body
parameter_list|)
throws|throws
name|Exception
block|{
name|ProducerTemplate
name|template
init|=
name|context
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
name|Object
name|answer
init|=
literal|null
decl_stmt|;
try|try
block|{
name|answer
operator|=
name|template
operator|.
name|requestBody
argument_list|(
name|endpointUri
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|template
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

