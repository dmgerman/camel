begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.api.management.mbean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|api
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
name|api
operator|.
name|management
operator|.
name|ManagedAttribute
import|;
end_import

begin_interface
DECL|interface|ManagedTracerMBean
specifier|public
interface|interface
name|ManagedTracerMBean
block|{
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Tracer enabled"
argument_list|)
DECL|method|getEnabled ()
name|boolean
name|getEnabled
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Tracer enabled"
argument_list|)
DECL|method|setEnabled (boolean enabled)
name|void
name|setEnabled
parameter_list|(
name|boolean
name|enabled
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Additional destination Uri"
argument_list|)
DECL|method|getDestinationUri ()
name|String
name|getDestinationUri
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Additional destination Uri"
argument_list|)
DECL|method|setDestinationUri (String uri)
name|void
name|setDestinationUri
parameter_list|(
name|String
name|uri
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Logging Name"
argument_list|)
DECL|method|getLogName ()
name|String
name|getLogName
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Using Jpa"
argument_list|)
DECL|method|getUseJpa ()
name|boolean
name|getUseJpa
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Logging Name"
argument_list|)
DECL|method|setLogName (String logName)
name|void
name|setLogName
parameter_list|(
name|String
name|logName
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Logging Level"
argument_list|)
DECL|method|getLogLevel ()
name|String
name|getLogLevel
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Logging Level"
argument_list|)
DECL|method|setLogLevel (String logLevel)
name|void
name|setLogLevel
parameter_list|(
name|String
name|logLevel
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Log Stacktrace"
argument_list|)
DECL|method|getLogStackTrace ()
name|boolean
name|getLogStackTrace
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Log Stacktrace"
argument_list|)
DECL|method|setLogStackTrace (boolean logStackTrace)
name|void
name|setLogStackTrace
parameter_list|(
name|boolean
name|logStackTrace
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Trace Interceptors"
argument_list|)
DECL|method|getTraceInterceptors ()
name|boolean
name|getTraceInterceptors
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Trace Interceptors"
argument_list|)
DECL|method|setTraceInterceptors (boolean traceInterceptors)
name|void
name|setTraceInterceptors
parameter_list|(
name|boolean
name|traceInterceptors
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Trace Exceptions"
argument_list|)
DECL|method|getTraceExceptions ()
name|boolean
name|getTraceExceptions
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Trace Exceptions"
argument_list|)
DECL|method|setTraceExceptions (boolean traceExceptions)
name|void
name|setTraceExceptions
parameter_list|(
name|boolean
name|traceExceptions
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Trace Out Exchanges"
argument_list|)
DECL|method|getTraceOutExchanges ()
name|boolean
name|getTraceOutExchanges
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Trace Out Exchanges"
argument_list|)
DECL|method|setTraceOutExchanges (boolean traceOutExchanges)
name|void
name|setTraceOutExchanges
parameter_list|(
name|boolean
name|traceOutExchanges
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show body"
argument_list|)
DECL|method|getFormatterShowBody ()
name|boolean
name|getFormatterShowBody
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show body"
argument_list|)
DECL|method|setFormatterShowBody (boolean showBody)
name|void
name|setFormatterShowBody
parameter_list|(
name|boolean
name|showBody
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show body type"
argument_list|)
DECL|method|getFormatterShowBodyType ()
name|boolean
name|getFormatterShowBodyType
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show body type"
argument_list|)
DECL|method|setFormatterShowBodyType (boolean showBodyType)
name|void
name|setFormatterShowBodyType
parameter_list|(
name|boolean
name|showBodyType
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show out body"
argument_list|)
DECL|method|getFormatterShowOutBody ()
name|boolean
name|getFormatterShowOutBody
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show out body"
argument_list|)
DECL|method|setFormatterShowOutBody (boolean showOutBody)
name|void
name|setFormatterShowOutBody
parameter_list|(
name|boolean
name|showOutBody
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show out body type"
argument_list|)
DECL|method|getFormatterShowOutBodyType ()
name|boolean
name|getFormatterShowOutBodyType
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show out body type"
argument_list|)
DECL|method|setFormatterShowOutBodyType (boolean showOutBodyType)
name|void
name|setFormatterShowOutBodyType
parameter_list|(
name|boolean
name|showOutBodyType
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show breadcrumb"
argument_list|)
DECL|method|getFormatterShowBreadCrumb ()
name|boolean
name|getFormatterShowBreadCrumb
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show breadcrumb"
argument_list|)
DECL|method|setFormatterShowBreadCrumb (boolean showBreadCrumb)
name|void
name|setFormatterShowBreadCrumb
parameter_list|(
name|boolean
name|showBreadCrumb
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show exchange id"
argument_list|)
DECL|method|getFormatterShowExchangeId ()
name|boolean
name|getFormatterShowExchangeId
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show exchange id"
argument_list|)
DECL|method|setFormatterShowExchangeId (boolean showExchangeId)
name|void
name|setFormatterShowExchangeId
parameter_list|(
name|boolean
name|showExchangeId
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show headers"
argument_list|)
DECL|method|getFormatterShowHeaders ()
name|boolean
name|getFormatterShowHeaders
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show headers"
argument_list|)
DECL|method|setFormatterShowHeaders (boolean showHeaders)
name|void
name|setFormatterShowHeaders
parameter_list|(
name|boolean
name|showHeaders
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show out headers"
argument_list|)
DECL|method|getFormatterShowOutHeaders ()
name|boolean
name|getFormatterShowOutHeaders
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show out headers"
argument_list|)
DECL|method|setFormatterShowOutHeaders (boolean showOutHeaders)
name|void
name|setFormatterShowOutHeaders
parameter_list|(
name|boolean
name|showOutHeaders
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show properties"
argument_list|)
DECL|method|getFormatterShowProperties ()
name|boolean
name|getFormatterShowProperties
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show properties"
argument_list|)
DECL|method|setFormatterShowProperties (boolean showProperties)
name|void
name|setFormatterShowProperties
parameter_list|(
name|boolean
name|showProperties
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show node"
argument_list|)
DECL|method|getFormatterShowNode ()
name|boolean
name|getFormatterShowNode
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show node"
argument_list|)
DECL|method|setFormatterShowNode (boolean showNode)
name|void
name|setFormatterShowNode
parameter_list|(
name|boolean
name|showNode
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show exchange pattern"
argument_list|)
DECL|method|getFormatterShowExchangePattern ()
name|boolean
name|getFormatterShowExchangePattern
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show exchange pattern"
argument_list|)
DECL|method|setFormatterShowExchangePattern (boolean showExchangePattern)
name|void
name|setFormatterShowExchangePattern
parameter_list|(
name|boolean
name|showExchangePattern
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show exception"
argument_list|)
DECL|method|getFormatterShowException ()
name|boolean
name|getFormatterShowException
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show exception"
argument_list|)
DECL|method|setFormatterShowException (boolean showException)
name|void
name|setFormatterShowException
parameter_list|(
name|boolean
name|showException
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show route id"
argument_list|)
DECL|method|getFormatterShowRouteId ()
name|boolean
name|getFormatterShowRouteId
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show route id"
argument_list|)
DECL|method|setFormatterShowRouteId (boolean showRouteId)
name|void
name|setFormatterShowRouteId
parameter_list|(
name|boolean
name|showRouteId
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter breadcrumb length"
argument_list|)
DECL|method|getFormatterBreadCrumbLength ()
name|int
name|getFormatterBreadCrumbLength
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter breadcrumb length"
argument_list|)
DECL|method|setFormatterBreadCrumbLength (int breadCrumbLength)
name|void
name|setFormatterBreadCrumbLength
parameter_list|(
name|int
name|breadCrumbLength
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show short exchange id"
argument_list|)
DECL|method|getFormatterShowShortExchangeId ()
name|boolean
name|getFormatterShowShortExchangeId
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter show short exchange id"
argument_list|)
DECL|method|setFormatterShowShortExchangeId (boolean showShortExchangeId)
name|void
name|setFormatterShowShortExchangeId
parameter_list|(
name|boolean
name|showShortExchangeId
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter node length"
argument_list|)
DECL|method|getFormatterNodeLength ()
name|int
name|getFormatterNodeLength
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter node length"
argument_list|)
DECL|method|setFormatterNodeLength (int nodeLength)
name|void
name|setFormatterNodeLength
parameter_list|(
name|int
name|nodeLength
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter max chars"
argument_list|)
DECL|method|getFormatterMaxChars ()
name|int
name|getFormatterMaxChars
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Formatter max chars"
argument_list|)
DECL|method|setFormatterMaxChars (int maxChars)
name|void
name|setFormatterMaxChars
parameter_list|(
name|int
name|maxChars
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Should trace events be sent as jmx notifications"
argument_list|)
DECL|method|isJmxTraceNotifications ()
name|boolean
name|isJmxTraceNotifications
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Should trace events be sent as jmx notifications"
argument_list|)
DECL|method|setJmxTraceNotifications (boolean jmxTraceNotifications)
name|void
name|setJmxTraceNotifications
parameter_list|(
name|boolean
name|jmxTraceNotifications
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Maximum size of a message body for trace notification"
argument_list|)
DECL|method|getTraceBodySize ()
name|int
name|getTraceBodySize
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Maximum size of a message body for trace notification"
argument_list|)
DECL|method|setTraceBodySize (int traceBodySize)
name|void
name|setTraceBodySize
parameter_list|(
name|int
name|traceBodySize
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

