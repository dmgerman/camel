begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.interceptor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|interceptor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

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
name|Processor
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
name|model
operator|.
name|InterceptorRef
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
name|model
operator|.
name|ProcessorType
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
name|DelegateProcessor
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
name|Logger
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
name|InterceptStrategy
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
name|TraceableUnitOfWork
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
name|IntrospectionSupport
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
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * An interceptor for debugging and tracing routes  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|TraceInterceptor
specifier|public
class|class
name|TraceInterceptor
extends|extends
name|DelegateProcessor
implements|implements
name|ExchangeFormatter
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|TraceInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|JPA_TRACE_EVENT_MESSAGE
specifier|private
specifier|static
specifier|final
name|String
name|JPA_TRACE_EVENT_MESSAGE
init|=
literal|"org.apache.camel.processor.interceptor.JpaTraceEventMessage"
decl_stmt|;
DECL|field|TRACE_EVENT
specifier|private
specifier|static
specifier|final
name|String
name|TRACE_EVENT
init|=
literal|"CamelTraceEvent"
decl_stmt|;
DECL|field|logger
specifier|private
name|Logger
name|logger
decl_stmt|;
DECL|field|traceEventProducer
specifier|private
name|Producer
name|traceEventProducer
decl_stmt|;
DECL|field|node
specifier|private
specifier|final
name|ProcessorType
name|node
decl_stmt|;
DECL|field|tracer
specifier|private
specifier|final
name|Tracer
name|tracer
decl_stmt|;
DECL|field|formatter
specifier|private
name|TraceFormatter
name|formatter
decl_stmt|;
DECL|field|jpaTraceEventMessageClass
specifier|private
name|Class
name|jpaTraceEventMessageClass
decl_stmt|;
DECL|method|TraceInterceptor (ProcessorType node, Processor target, TraceFormatter formatter, Tracer tracer)
specifier|public
name|TraceInterceptor
parameter_list|(
name|ProcessorType
name|node
parameter_list|,
name|Processor
name|target
parameter_list|,
name|TraceFormatter
name|formatter
parameter_list|,
name|Tracer
name|tracer
parameter_list|)
block|{
name|super
argument_list|(
name|target
argument_list|)
expr_stmt|;
name|this
operator|.
name|tracer
operator|=
name|tracer
expr_stmt|;
name|this
operator|.
name|node
operator|=
name|node
expr_stmt|;
name|this
operator|.
name|formatter
operator|=
name|formatter
expr_stmt|;
comment|// set logger to use
if|if
condition|(
name|tracer
operator|.
name|getLogName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|logger
operator|=
operator|new
name|Logger
argument_list|(
name|LogFactory
operator|.
name|getLog
argument_list|(
name|tracer
operator|.
name|getLogName
argument_list|()
argument_list|)
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// use default logger
name|logger
operator|=
operator|new
name|Logger
argument_list|(
name|LogFactory
operator|.
name|getLog
argument_list|(
name|TraceInterceptor
operator|.
name|class
argument_list|)
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
comment|// set logging level if provided
if|if
condition|(
name|tracer
operator|.
name|getLogLevel
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|logger
operator|.
name|setLevel
argument_list|(
name|tracer
operator|.
name|getLogLevel
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|tracer
operator|.
name|getFormatter
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|formatter
operator|=
name|tracer
operator|.
name|getFormatter
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|TraceInterceptor (ProcessorType node, Processor target, Tracer tracer)
specifier|public
name|TraceInterceptor
parameter_list|(
name|ProcessorType
name|node
parameter_list|,
name|Processor
name|target
parameter_list|,
name|Tracer
name|tracer
parameter_list|)
block|{
name|this
argument_list|(
name|node
argument_list|,
name|target
argument_list|,
literal|null
argument_list|,
name|tracer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"TraceInterceptor["
operator|+
name|node
operator|+
literal|"]"
return|;
block|}
DECL|method|process (final Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// interceptor will also trace routes supposed only for TraceEvents so we need to skip
comment|// logging TraceEvents to avoid infinite looping
if|if
condition|(
name|exchange
operator|instanceof
name|TraceEventExchange
operator|||
name|exchange
operator|.
name|getProperty
argument_list|(
name|TRACE_EVENT
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
operator|!=
literal|null
condition|)
block|{
comment|// but we must still process to allow routing of TraceEvents to eg a JPA endpoint
name|super
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
return|return;
block|}
name|boolean
name|shouldLog
init|=
name|shouldLogNode
argument_list|(
name|node
argument_list|)
operator|&&
name|shouldLogExchange
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
comment|// okay this is a regular exchange being routed we might need to log and trace
try|try
block|{
comment|// before
if|if
condition|(
name|shouldLog
condition|)
block|{
name|logExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|traceExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
comment|// if traceable then register this as the previous node, now it has been logged
if|if
condition|(
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
operator|instanceof
name|TraceableUnitOfWork
condition|)
block|{
name|TraceableUnitOfWork
name|tuow
init|=
operator|(
name|TraceableUnitOfWork
operator|)
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
decl_stmt|;
name|tuow
operator|.
name|addInterceptedNode
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
block|}
comment|// process the exchange
name|super
operator|.
name|proceed
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
comment|// after (trace out)
if|if
condition|(
name|shouldLog
operator|&&
name|tracer
operator|.
name|isTraceOutExchanges
argument_list|()
condition|)
block|{
name|logExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|traceExchange
argument_list|(
name|exchange
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
if|if
condition|(
name|shouldLogException
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
name|logException
argument_list|(
name|exchange
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
throw|throw
name|e
throw|;
block|}
block|}
DECL|method|format (Exchange exchange)
specifier|public
name|Object
name|format
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|formatter
operator|.
name|format
argument_list|(
name|this
argument_list|,
name|this
operator|.
name|getNode
argument_list|()
argument_list|,
name|exchange
argument_list|)
return|;
block|}
comment|// Properties
comment|//-------------------------------------------------------------------------
DECL|method|getNode ()
specifier|public
name|ProcessorType
name|getNode
parameter_list|()
block|{
return|return
name|node
return|;
block|}
DECL|method|getLogger ()
specifier|public
name|Logger
name|getLogger
parameter_list|()
block|{
return|return
name|logger
return|;
block|}
DECL|method|getFormatter ()
specifier|public
name|TraceFormatter
name|getFormatter
parameter_list|()
block|{
return|return
name|formatter
return|;
block|}
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
DECL|method|logExchange (Exchange exchange)
specifier|protected
name|void
name|logExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// process the exchange that formats and logs it
name|logger
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|traceExchange (Exchange exchange)
specifier|protected
name|void
name|traceExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// should we send a trace event to an optional destination?
if|if
condition|(
name|tracer
operator|.
name|getDestinationUri
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// create event and add it as a property on the original exchange
name|TraceEventExchange
name|event
init|=
operator|new
name|TraceEventExchange
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|Date
name|timestamp
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
name|event
operator|.
name|setNodeId
argument_list|(
name|node
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|event
operator|.
name|setTimestamp
argument_list|(
name|timestamp
argument_list|)
expr_stmt|;
name|event
operator|.
name|setTracedExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
comment|// create event message to send in body
name|TraceEventMessage
name|msg
init|=
operator|new
name|DefaultTraceEventMessage
argument_list|(
name|timestamp
argument_list|,
name|node
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
comment|// should we use ordinay or jpa objects
if|if
condition|(
name|tracer
operator|.
name|isUseJpa
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Using class: "
operator|+
name|JPA_TRACE_EVENT_MESSAGE
operator|+
literal|" for tracing event messages"
argument_list|)
expr_stmt|;
comment|// load the jpa event class
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
name|jpaTraceEventMessageClass
operator|==
literal|null
condition|)
block|{
name|jpaTraceEventMessageClass
operator|=
name|ObjectHelper
operator|.
name|loadClass
argument_list|(
name|JPA_TRACE_EVENT_MESSAGE
argument_list|)
expr_stmt|;
if|if
condition|(
name|jpaTraceEventMessageClass
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot find class: "
operator|+
name|JPA_TRACE_EVENT_MESSAGE
operator|+
literal|". Make sure camel-jpa.jar is on the classpath."
argument_list|)
throw|;
block|}
block|}
block|}
name|Object
name|jpa
init|=
name|ObjectHelper
operator|.
name|newInstance
argument_list|(
name|jpaTraceEventMessageClass
argument_list|)
decl_stmt|;
comment|// copy options from event to jpa
name|Map
name|options
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|IntrospectionSupport
operator|.
name|getProperties
argument_list|(
name|msg
argument_list|,
name|options
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|jpa
argument_list|,
name|options
argument_list|)
expr_stmt|;
comment|// and set the timestamp as its not a String type
name|IntrospectionSupport
operator|.
name|setProperty
argument_list|(
name|jpa
argument_list|,
literal|"timestamp"
argument_list|,
name|msg
operator|.
name|getTimestamp
argument_list|()
argument_list|)
expr_stmt|;
name|event
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|jpa
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|event
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
comment|// marker property to indicate its a tracing event being routed in case
comment|// new Exchange instances is created during trace routing so we can check
comment|// for this marker when interceptor also kickins in during routing of trace events
name|event
operator|.
name|setProperty
argument_list|(
name|TRACE_EVENT
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
try|try
block|{
comment|// process the trace route
name|getTraceEventProducer
argument_list|(
name|exchange
argument_list|)
operator|.
name|process
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// log and ignore this as the original Exchange should be allowed to continue
name|LOG
operator|.
name|error
argument_list|(
literal|"Error processing TraceEventExchange (original Exchange will be continued): "
operator|+
name|event
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|logException (Exchange exchange, Throwable throwable)
specifier|protected
name|void
name|logException
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Throwable
name|throwable
parameter_list|)
block|{
if|if
condition|(
name|tracer
operator|.
name|isTraceExceptions
argument_list|()
condition|)
block|{
name|logger
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|throwable
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns true if the given exchange should be logged in the trace list      */
DECL|method|shouldLogExchange (Exchange exchange)
specifier|protected
name|boolean
name|shouldLogExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|tracer
operator|.
name|isEnabled
argument_list|()
operator|&&
operator|(
name|tracer
operator|.
name|getTraceFilter
argument_list|()
operator|==
literal|null
operator|||
name|tracer
operator|.
name|getTraceFilter
argument_list|()
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
operator|)
return|;
block|}
comment|/**      * Returns true if the given exchange should be logged when an exception was thrown      */
DECL|method|shouldLogException (Exchange exchange)
specifier|protected
name|boolean
name|shouldLogException
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|tracer
operator|.
name|isTraceExceptions
argument_list|()
return|;
block|}
comment|/**      * Returns whether exchanges coming out of processors should be traced      */
DECL|method|shouldTraceOutExchanges ()
specifier|public
name|boolean
name|shouldTraceOutExchanges
parameter_list|()
block|{
return|return
name|tracer
operator|.
name|isTraceOutExchanges
argument_list|()
return|;
block|}
comment|/**      * Returns true if the given node should be logged in the trace list      */
DECL|method|shouldLogNode (ProcessorType node)
specifier|protected
name|boolean
name|shouldLogNode
parameter_list|(
name|ProcessorType
name|node
parameter_list|)
block|{
if|if
condition|(
name|node
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
operator|!
name|tracer
operator|.
name|isTraceInterceptors
argument_list|()
operator|&&
operator|(
name|node
operator|instanceof
name|InterceptStrategy
operator|||
name|node
operator|instanceof
name|InterceptorRef
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
DECL|method|getTraceEventProducer (Exchange exchange)
specifier|private
specifier|synchronized
name|Producer
name|getTraceEventProducer
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|traceEventProducer
operator|==
literal|null
condition|)
block|{
comment|// create producer when we have access the the camel context (we dont in doStart)
name|traceEventProducer
operator|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getEndpoint
argument_list|(
name|tracer
operator|.
name|getDestinationUri
argument_list|()
argument_list|)
operator|.
name|createProducer
argument_list|()
expr_stmt|;
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|traceEventProducer
argument_list|)
expr_stmt|;
block|}
return|return
name|traceEventProducer
return|;
block|}
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
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|traceEventProducer
operator|=
literal|null
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
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
if|if
condition|(
name|traceEventProducer
operator|!=
literal|null
condition|)
block|{
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|traceEventProducer
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

