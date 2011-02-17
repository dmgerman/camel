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
name|LoggingLevel
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
name|interceptor
operator|.
name|Tracer
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
literal|"Managed Tracer"
argument_list|)
DECL|class|ManagedTracer
specifier|public
class|class
name|ManagedTracer
block|{
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|tracer
specifier|private
specifier|final
name|Tracer
name|tracer
decl_stmt|;
DECL|method|ManagedTracer (CamelContext camelContext, Tracer tracer)
specifier|public
name|ManagedTracer
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Tracer
name|tracer
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|tracer
operator|=
name|tracer
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
name|camelContext
return|;
block|}
DECL|method|getTracer ()
specifier|public
name|Tracer
name|getTracer
parameter_list|()
block|{
return|return
name|tracer
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Tracer enabled"
argument_list|)
DECL|method|getEnabled ()
specifier|public
name|boolean
name|getEnabled
parameter_list|()
block|{
return|return
name|tracer
operator|.
name|isEnabled
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Tracer enabled"
argument_list|)
DECL|method|setEnabled (boolean enabled)
specifier|public
name|void
name|setEnabled
parameter_list|(
name|boolean
name|enabled
parameter_list|)
block|{
name|tracer
operator|.
name|setEnabled
argument_list|(
name|enabled
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Additional destination Uri"
argument_list|)
DECL|method|getDestinationUri ()
specifier|public
name|String
name|getDestinationUri
parameter_list|()
block|{
return|return
name|tracer
operator|.
name|getDestinationUri
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Additional destination Uri"
argument_list|)
DECL|method|setDestinationUri (String uri)
specifier|public
name|void
name|setDestinationUri
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|uri
argument_list|)
condition|)
block|{
name|tracer
operator|.
name|setDestinationUri
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|tracer
operator|.
name|setDestinationUri
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Logging Name"
argument_list|)
DECL|method|getLogName ()
specifier|public
name|String
name|getLogName
parameter_list|()
block|{
return|return
name|tracer
operator|.
name|getLogName
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Using Jpa"
argument_list|)
DECL|method|getUseJpa ()
specifier|public
name|boolean
name|getUseJpa
parameter_list|()
block|{
return|return
name|tracer
operator|.
name|isUseJpa
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Logging Name"
argument_list|)
DECL|method|setLogName (String logName)
specifier|public
name|void
name|setLogName
parameter_list|(
name|String
name|logName
parameter_list|)
block|{
name|tracer
operator|.
name|setLogName
argument_list|(
name|logName
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Logging Level"
argument_list|)
DECL|method|getLogLevel ()
specifier|public
name|String
name|getLogLevel
parameter_list|()
block|{
return|return
name|tracer
operator|.
name|getLogLevel
argument_list|()
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
literal|"Logging Level"
argument_list|)
DECL|method|setLogLevel (String logLevel)
specifier|public
name|void
name|setLogLevel
parameter_list|(
name|String
name|logLevel
parameter_list|)
block|{
name|tracer
operator|.
name|setLogLevel
argument_list|(
name|LoggingLevel
operator|.
name|valueOf
argument_list|(
name|logLevel
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Log Stacktrace"
argument_list|)
DECL|method|getLogStackTrace ()
specifier|public
name|boolean
name|getLogStackTrace
parameter_list|()
block|{
return|return
name|tracer
operator|.
name|isLogStackTrace
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Log Stacktrace"
argument_list|)
DECL|method|setLogStackTrace (boolean logStackTrace)
specifier|public
name|void
name|setLogStackTrace
parameter_list|(
name|boolean
name|logStackTrace
parameter_list|)
block|{
name|tracer
operator|.
name|setLogStackTrace
argument_list|(
name|logStackTrace
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Trace Interceptors"
argument_list|)
DECL|method|getTraceInterceptors ()
specifier|public
name|boolean
name|getTraceInterceptors
parameter_list|()
block|{
return|return
name|tracer
operator|.
name|isTraceInterceptors
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Trace Interceptors"
argument_list|)
DECL|method|setTraceInterceptors (boolean traceInterceptors)
specifier|public
name|void
name|setTraceInterceptors
parameter_list|(
name|boolean
name|traceInterceptors
parameter_list|)
block|{
name|tracer
operator|.
name|setTraceInterceptors
argument_list|(
name|traceInterceptors
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Trace Exceptions"
argument_list|)
DECL|method|getTraceExceptions ()
specifier|public
name|boolean
name|getTraceExceptions
parameter_list|()
block|{
return|return
name|tracer
operator|.
name|isTraceExceptions
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Trace Exceptions"
argument_list|)
DECL|method|setTraceExceptions (boolean traceExceptions)
specifier|public
name|void
name|setTraceExceptions
parameter_list|(
name|boolean
name|traceExceptions
parameter_list|)
block|{
name|tracer
operator|.
name|setTraceExceptions
argument_list|(
name|traceExceptions
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Trace Out Exchanges"
argument_list|)
DECL|method|getTraceOutExchanges ()
specifier|public
name|boolean
name|getTraceOutExchanges
parameter_list|()
block|{
return|return
name|tracer
operator|.
name|isTraceOutExchanges
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Trace Out Exchanges"
argument_list|)
DECL|method|setTraceOutExchanges (boolean traceOutExchanges)
specifier|public
name|void
name|setTraceOutExchanges
parameter_list|(
name|boolean
name|traceOutExchanges
parameter_list|)
block|{
name|tracer
operator|.
name|setTraceOutExchanges
argument_list|(
name|traceOutExchanges
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show body"
argument_list|)
DECL|method|getFormatterShowBody ()
specifier|public
name|boolean
name|getFormatterShowBody
parameter_list|()
block|{
if|if
condition|(
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|.
name|isShowBody
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show body"
argument_list|)
DECL|method|setFormatterShowBody (boolean showBody)
specifier|public
name|void
name|setFormatterShowBody
parameter_list|(
name|boolean
name|showBody
parameter_list|)
block|{
if|if
condition|(
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|.
name|setShowBody
argument_list|(
name|showBody
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show body type"
argument_list|)
DECL|method|getFormatterShowBodyType ()
specifier|public
name|boolean
name|getFormatterShowBodyType
parameter_list|()
block|{
if|if
condition|(
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|.
name|isShowBodyType
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show body type"
argument_list|)
DECL|method|setFormatterShowBodyType (boolean showBodyType)
specifier|public
name|void
name|setFormatterShowBodyType
parameter_list|(
name|boolean
name|showBodyType
parameter_list|)
block|{
if|if
condition|(
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|.
name|setShowBodyType
argument_list|(
name|showBodyType
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show out body"
argument_list|)
DECL|method|getFormatterShowOutBody ()
specifier|public
name|boolean
name|getFormatterShowOutBody
parameter_list|()
block|{
if|if
condition|(
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|.
name|isShowOutBody
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show out body"
argument_list|)
DECL|method|setFormatterShowOutBody (boolean showOutBody)
specifier|public
name|void
name|setFormatterShowOutBody
parameter_list|(
name|boolean
name|showOutBody
parameter_list|)
block|{
if|if
condition|(
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|.
name|setShowOutBody
argument_list|(
name|showOutBody
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show out body type"
argument_list|)
DECL|method|getFormatterShowOutBodyType ()
specifier|public
name|boolean
name|getFormatterShowOutBodyType
parameter_list|()
block|{
if|if
condition|(
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|.
name|isShowOutBodyType
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show out body type"
argument_list|)
DECL|method|setFormatterShowOutBodyType (boolean showOutBodyType)
specifier|public
name|void
name|setFormatterShowOutBodyType
parameter_list|(
name|boolean
name|showOutBodyType
parameter_list|)
block|{
if|if
condition|(
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|.
name|setShowOutBodyType
argument_list|(
name|showOutBodyType
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show breadcrumb"
argument_list|)
DECL|method|getFormatterShowBreadCrumb ()
specifier|public
name|boolean
name|getFormatterShowBreadCrumb
parameter_list|()
block|{
if|if
condition|(
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|.
name|isShowBreadCrumb
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show breadcrumb"
argument_list|)
DECL|method|setFormatterShowBreadCrumb (boolean showBreadCrumb)
specifier|public
name|void
name|setFormatterShowBreadCrumb
parameter_list|(
name|boolean
name|showBreadCrumb
parameter_list|)
block|{
if|if
condition|(
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|.
name|setShowBreadCrumb
argument_list|(
name|showBreadCrumb
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show exchange id"
argument_list|)
DECL|method|getFormatterShowExchangeId ()
specifier|public
name|boolean
name|getFormatterShowExchangeId
parameter_list|()
block|{
if|if
condition|(
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|.
name|isShowExchangeId
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show exchange id"
argument_list|)
DECL|method|setFormatterShowExchangeId (boolean showExchangeId)
specifier|public
name|void
name|setFormatterShowExchangeId
parameter_list|(
name|boolean
name|showExchangeId
parameter_list|)
block|{
if|if
condition|(
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|.
name|setShowExchangeId
argument_list|(
name|showExchangeId
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show headers"
argument_list|)
DECL|method|getFormatterShowHeaders ()
specifier|public
name|boolean
name|getFormatterShowHeaders
parameter_list|()
block|{
if|if
condition|(
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|.
name|isShowHeaders
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show headers"
argument_list|)
DECL|method|setFormatterShowHeaders (boolean showHeaders)
specifier|public
name|void
name|setFormatterShowHeaders
parameter_list|(
name|boolean
name|showHeaders
parameter_list|)
block|{
if|if
condition|(
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|.
name|setShowHeaders
argument_list|(
name|showHeaders
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show out headers"
argument_list|)
DECL|method|getFormatterShowOutHeaders ()
specifier|public
name|boolean
name|getFormatterShowOutHeaders
parameter_list|()
block|{
if|if
condition|(
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|.
name|isShowOutHeaders
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show out headers"
argument_list|)
DECL|method|setFormatterShowOutHeaders (boolean showOutHeaders)
specifier|public
name|void
name|setFormatterShowOutHeaders
parameter_list|(
name|boolean
name|showOutHeaders
parameter_list|)
block|{
if|if
condition|(
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|.
name|setShowOutHeaders
argument_list|(
name|showOutHeaders
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show properties"
argument_list|)
DECL|method|getFormatterShowProperties ()
specifier|public
name|boolean
name|getFormatterShowProperties
parameter_list|()
block|{
if|if
condition|(
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|.
name|isShowProperties
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show properties"
argument_list|)
DECL|method|setFormatterShowProperties (boolean showProperties)
specifier|public
name|void
name|setFormatterShowProperties
parameter_list|(
name|boolean
name|showProperties
parameter_list|)
block|{
if|if
condition|(
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|.
name|setShowProperties
argument_list|(
name|showProperties
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show node"
argument_list|)
DECL|method|getFormatterShowNode ()
specifier|public
name|boolean
name|getFormatterShowNode
parameter_list|()
block|{
if|if
condition|(
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|.
name|isShowNode
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show node"
argument_list|)
DECL|method|setFormatterShowNode (boolean showNode)
specifier|public
name|void
name|setFormatterShowNode
parameter_list|(
name|boolean
name|showNode
parameter_list|)
block|{
if|if
condition|(
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|.
name|setShowNode
argument_list|(
name|showNode
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show exchange pattern"
argument_list|)
DECL|method|getFormatterShowExchangePattern ()
specifier|public
name|boolean
name|getFormatterShowExchangePattern
parameter_list|()
block|{
if|if
condition|(
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|.
name|isShowExchangePattern
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show exchange pattern"
argument_list|)
DECL|method|setFormatterShowExchangePattern (boolean showExchangePattern)
specifier|public
name|void
name|setFormatterShowExchangePattern
parameter_list|(
name|boolean
name|showExchangePattern
parameter_list|)
block|{
if|if
condition|(
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|.
name|setShowExchangePattern
argument_list|(
name|showExchangePattern
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show exception"
argument_list|)
DECL|method|getFormatterShowException ()
specifier|public
name|boolean
name|getFormatterShowException
parameter_list|()
block|{
if|if
condition|(
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|.
name|isShowException
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show exception"
argument_list|)
DECL|method|setFormatterShowException (boolean showException)
specifier|public
name|void
name|setFormatterShowException
parameter_list|(
name|boolean
name|showException
parameter_list|)
block|{
if|if
condition|(
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|.
name|setShowException
argument_list|(
name|showException
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show route id"
argument_list|)
DECL|method|getFormatterShowRouteId ()
specifier|public
name|boolean
name|getFormatterShowRouteId
parameter_list|()
block|{
if|if
condition|(
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|.
name|isShowRouteId
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show route id"
argument_list|)
DECL|method|setFormatterShowRouteId (boolean showRouteId)
specifier|public
name|void
name|setFormatterShowRouteId
parameter_list|(
name|boolean
name|showRouteId
parameter_list|)
block|{
if|if
condition|(
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|.
name|setShowRouteId
argument_list|(
name|showRouteId
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter breadcrumb length"
argument_list|)
DECL|method|getFormatterBreadCrumbLength ()
specifier|public
name|int
name|getFormatterBreadCrumbLength
parameter_list|()
block|{
if|if
condition|(
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|0
return|;
block|}
return|return
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|.
name|getBreadCrumbLength
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter breadcrumb length"
argument_list|)
DECL|method|setFormatterBreadCrumbLength (int breadCrumbLength)
specifier|public
name|void
name|setFormatterBreadCrumbLength
parameter_list|(
name|int
name|breadCrumbLength
parameter_list|)
block|{
if|if
condition|(
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|.
name|setBreadCrumbLength
argument_list|(
name|breadCrumbLength
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show short exchange id"
argument_list|)
DECL|method|getFormatterShowShortExchangeId ()
specifier|public
name|boolean
name|getFormatterShowShortExchangeId
parameter_list|()
block|{
if|if
condition|(
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|.
name|isShowShortExchangeId
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show short exchange id"
argument_list|)
DECL|method|setFormatterShowShortExchangeId (boolean showShortExchangeId)
specifier|public
name|void
name|setFormatterShowShortExchangeId
parameter_list|(
name|boolean
name|showShortExchangeId
parameter_list|)
block|{
if|if
condition|(
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|.
name|setShowShortExchangeId
argument_list|(
name|showShortExchangeId
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter node length"
argument_list|)
DECL|method|getFormatterNodeLength ()
specifier|public
name|int
name|getFormatterNodeLength
parameter_list|()
block|{
if|if
condition|(
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|0
return|;
block|}
return|return
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|.
name|getNodeLength
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter node length"
argument_list|)
DECL|method|setFormatterNodeLength (int nodeLength)
specifier|public
name|void
name|setFormatterNodeLength
parameter_list|(
name|int
name|nodeLength
parameter_list|)
block|{
if|if
condition|(
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|.
name|setNodeLength
argument_list|(
name|nodeLength
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter max chars"
argument_list|)
DECL|method|getFormatterMaxChars ()
specifier|public
name|int
name|getFormatterMaxChars
parameter_list|()
block|{
if|if
condition|(
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|0
return|;
block|}
return|return
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|.
name|getMaxChars
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter max chars"
argument_list|)
DECL|method|setFormatterMaxChars (int maxChars)
specifier|public
name|void
name|setFormatterMaxChars
parameter_list|(
name|int
name|maxChars
parameter_list|)
block|{
if|if
condition|(
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|tracer
operator|.
name|getDefaultTraceFormatter
argument_list|()
operator|.
name|setMaxChars
argument_list|(
name|maxChars
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

