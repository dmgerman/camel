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
name|List
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
name|Predicate
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
name|impl
operator|.
name|DefaultCamelContext
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
name|spi
operator|.
name|InterceptStrategy
import|;
end_import

begin_comment
comment|/**  * An interceptor strategy for tracing routes  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|Tracer
specifier|public
class|class
name|Tracer
implements|implements
name|InterceptStrategy
block|{
DECL|field|formatter
specifier|private
name|TraceFormatter
name|formatter
init|=
operator|new
name|DefaultTraceFormatter
argument_list|()
decl_stmt|;
DECL|field|enabled
specifier|private
name|boolean
name|enabled
init|=
literal|true
decl_stmt|;
DECL|field|logName
specifier|private
name|String
name|logName
decl_stmt|;
DECL|field|logLevel
specifier|private
name|LoggingLevel
name|logLevel
decl_stmt|;
DECL|field|traceFilter
specifier|private
name|Predicate
name|traceFilter
decl_stmt|;
DECL|field|traceInterceptors
specifier|private
name|boolean
name|traceInterceptors
decl_stmt|;
DECL|field|traceExceptions
specifier|private
name|boolean
name|traceExceptions
init|=
literal|true
decl_stmt|;
DECL|field|traceOutExchanges
specifier|private
name|boolean
name|traceOutExchanges
decl_stmt|;
DECL|field|destinationUri
specifier|private
name|String
name|destinationUri
decl_stmt|;
DECL|field|destination
specifier|private
name|Endpoint
name|destination
decl_stmt|;
DECL|field|useJpa
specifier|private
name|boolean
name|useJpa
decl_stmt|;
comment|/**      * A helper method to return the Tracer instance for a given {@link CamelContext} if one is enabled      *      * @param context the camel context the tracer is connected to      * @return the tracer or null if none can be found      */
DECL|method|getTracer (CamelContext context)
specifier|public
specifier|static
name|Tracer
name|getTracer
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
if|if
condition|(
name|context
operator|instanceof
name|DefaultCamelContext
condition|)
block|{
name|DefaultCamelContext
name|defaultCamelContext
init|=
operator|(
name|DefaultCamelContext
operator|)
name|context
decl_stmt|;
name|List
argument_list|<
name|InterceptStrategy
argument_list|>
name|list
init|=
name|defaultCamelContext
operator|.
name|getInterceptStrategies
argument_list|()
decl_stmt|;
for|for
control|(
name|InterceptStrategy
name|interceptStrategy
range|:
name|list
control|)
block|{
if|if
condition|(
name|interceptStrategy
operator|instanceof
name|Tracer
condition|)
block|{
return|return
operator|(
name|Tracer
operator|)
name|interceptStrategy
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|wrapProcessorInInterceptors (ProcessorType processorType, Processor target)
specifier|public
name|Processor
name|wrapProcessorInInterceptors
parameter_list|(
name|ProcessorType
name|processorType
parameter_list|,
name|Processor
name|target
parameter_list|)
throws|throws
name|Exception
block|{
comment|// Force the creation of an id, otherwise the id is not available when the trace formatter is
comment|// outputting trace information
name|String
name|id
init|=
name|processorType
operator|.
name|idOrCreate
argument_list|()
decl_stmt|;
return|return
operator|new
name|TraceInterceptor
argument_list|(
name|processorType
argument_list|,
name|target
argument_list|,
name|formatter
argument_list|,
name|this
argument_list|)
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
DECL|method|getDefaultTraceFormatter ()
specifier|public
name|DefaultTraceFormatter
name|getDefaultTraceFormatter
parameter_list|()
block|{
if|if
condition|(
name|formatter
operator|instanceof
name|DefaultTraceFormatter
condition|)
block|{
return|return
operator|(
name|DefaultTraceFormatter
operator|)
name|formatter
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|setFormatter (TraceFormatter formatter)
specifier|public
name|void
name|setFormatter
parameter_list|(
name|TraceFormatter
name|formatter
parameter_list|)
block|{
name|this
operator|.
name|formatter
operator|=
name|formatter
expr_stmt|;
block|}
DECL|method|setEnabled (boolean flag)
specifier|public
name|void
name|setEnabled
parameter_list|(
name|boolean
name|flag
parameter_list|)
block|{
name|enabled
operator|=
name|flag
expr_stmt|;
block|}
DECL|method|isEnabled ()
specifier|public
name|boolean
name|isEnabled
parameter_list|()
block|{
return|return
name|enabled
return|;
block|}
DECL|method|isTraceInterceptors ()
specifier|public
name|boolean
name|isTraceInterceptors
parameter_list|()
block|{
return|return
name|traceInterceptors
return|;
block|}
comment|/**      * Sets whether interceptors should be traced or not      */
DECL|method|setTraceInterceptors (boolean traceInterceptors)
specifier|public
name|void
name|setTraceInterceptors
parameter_list|(
name|boolean
name|traceInterceptors
parameter_list|)
block|{
name|this
operator|.
name|traceInterceptors
operator|=
name|traceInterceptors
expr_stmt|;
block|}
DECL|method|getTraceFilter ()
specifier|public
name|Predicate
name|getTraceFilter
parameter_list|()
block|{
return|return
name|traceFilter
return|;
block|}
comment|/**      * Sets a predicate to be used as filter when tracing      */
DECL|method|setTraceFilter (Predicate traceFilter)
specifier|public
name|void
name|setTraceFilter
parameter_list|(
name|Predicate
name|traceFilter
parameter_list|)
block|{
name|this
operator|.
name|traceFilter
operator|=
name|traceFilter
expr_stmt|;
block|}
DECL|method|getLogLevel ()
specifier|public
name|LoggingLevel
name|getLogLevel
parameter_list|()
block|{
return|return
name|logLevel
return|;
block|}
comment|/**      * Sets the logging level to output tracing. Will use<tt>INFO</tt> level by default.      */
DECL|method|setLogLevel (LoggingLevel logLevel)
specifier|public
name|void
name|setLogLevel
parameter_list|(
name|LoggingLevel
name|logLevel
parameter_list|)
block|{
name|this
operator|.
name|logLevel
operator|=
name|logLevel
expr_stmt|;
block|}
DECL|method|isTraceExceptions ()
specifier|public
name|boolean
name|isTraceExceptions
parameter_list|()
block|{
return|return
name|traceExceptions
return|;
block|}
comment|/**      * Sets whether thrown exceptions should be traced      */
DECL|method|setTraceExceptions (boolean traceExceptions)
specifier|public
name|void
name|setTraceExceptions
parameter_list|(
name|boolean
name|traceExceptions
parameter_list|)
block|{
name|this
operator|.
name|traceExceptions
operator|=
name|traceExceptions
expr_stmt|;
block|}
DECL|method|getLogName ()
specifier|public
name|String
name|getLogName
parameter_list|()
block|{
return|return
name|logName
return|;
block|}
comment|/**      * Sets the logging name to use.      * Will default use<tt>org.apache.camel.processor.interceptor.TraceInterceptor<tt>.      */
DECL|method|setLogName (String logName)
specifier|public
name|void
name|setLogName
parameter_list|(
name|String
name|logName
parameter_list|)
block|{
name|this
operator|.
name|logName
operator|=
name|logName
expr_stmt|;
block|}
comment|/**      * Sets whether exchanges coming out of processors should be traced      */
DECL|method|setTraceOutExchanges (boolean traceOutExchanges)
specifier|public
name|void
name|setTraceOutExchanges
parameter_list|(
name|boolean
name|traceOutExchanges
parameter_list|)
block|{
name|this
operator|.
name|traceOutExchanges
operator|=
name|traceOutExchanges
expr_stmt|;
block|}
DECL|method|isTraceOutExchanges ()
specifier|public
name|boolean
name|isTraceOutExchanges
parameter_list|()
block|{
return|return
name|traceOutExchanges
return|;
block|}
DECL|method|getDestinationUri ()
specifier|public
name|String
name|getDestinationUri
parameter_list|()
block|{
return|return
name|destinationUri
return|;
block|}
comment|/**      * Sets an optional destination to send the traced Exchange wrapped in a {@link TraceEventExchange}.      *<p/>      * Can be used to store tracing as files, in a database or whatever. The routing of the Exchange      * will happen synchronously and the original route will first continue when this destination routing      * has been compledted.      */
DECL|method|setDestinationUri (String destinationUri)
specifier|public
name|void
name|setDestinationUri
parameter_list|(
name|String
name|destinationUri
parameter_list|)
block|{
name|this
operator|.
name|destinationUri
operator|=
name|destinationUri
expr_stmt|;
block|}
DECL|method|getDestination ()
specifier|public
name|Endpoint
name|getDestination
parameter_list|()
block|{
return|return
name|destination
return|;
block|}
comment|/**      * See {@link #setDestinationUri(String)}      */
DECL|method|setDestination (Endpoint destination)
specifier|public
name|void
name|setDestination
parameter_list|(
name|Endpoint
name|destination
parameter_list|)
block|{
name|this
operator|.
name|destination
operator|=
name|destination
expr_stmt|;
block|}
DECL|method|isUseJpa ()
specifier|public
name|boolean
name|isUseJpa
parameter_list|()
block|{
return|return
name|useJpa
return|;
block|}
comment|/**      * Sets whether we should use a JpaTraceEventMessage instead of      * an ordinary {@link org.apache.camel.processor.interceptor.DefaultTraceEventMessage}      *<p/>      * Use this to allow persistence of trace events into a database using JPA.      * This requires camel-jpa in the classpath.      */
DECL|method|setUseJpa (boolean useJpa)
specifier|public
name|void
name|setUseJpa
parameter_list|(
name|boolean
name|useJpa
parameter_list|)
block|{
name|this
operator|.
name|useJpa
operator|=
name|useJpa
expr_stmt|;
block|}
block|}
end_class

end_unit

