begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.log
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|log
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
name|Component
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
name|impl
operator|.
name|ProcessorEndpoint
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
name|Constants
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
name|CamelLogProcessor
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
name|DefaultExchangeFormatter
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
name|DefaultMaskingFormatter
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
name|ThroughputLogger
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
name|ExchangeFormatter
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
name|MaskingFormatter
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
name|Metadata
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
name|UriEndpoint
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
name|UriParam
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
name|UriPath
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
name|ServiceHelper
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

begin_comment
comment|/**  * The log component logs message exchanges to the underlying logging mechanism.  *  * Camel uses sfl4j which allows you to configure logging to the actual logging system.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"1.1.0"
argument_list|,
name|scheme
operator|=
literal|"log"
argument_list|,
name|title
operator|=
literal|"Log"
argument_list|,
name|syntax
operator|=
literal|"log:loggerName"
argument_list|,
name|producerOnly
operator|=
literal|true
argument_list|,
name|label
operator|=
literal|"core,monitoring"
argument_list|)
DECL|class|LogEndpoint
specifier|public
class|class
name|LogEndpoint
extends|extends
name|ProcessorEndpoint
block|{
DECL|field|logger
specifier|private
specifier|volatile
name|Processor
name|logger
decl_stmt|;
DECL|field|providedLogger
specifier|private
name|Logger
name|providedLogger
decl_stmt|;
DECL|field|localFormatter
specifier|private
name|ExchangeFormatter
name|localFormatter
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"Name of the logging category to use"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|loggerName
specifier|private
name|String
name|loggerName
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"INFO"
argument_list|,
name|enums
operator|=
literal|"ERROR,WARN,INFO,DEBUG,TRACE,OFF"
argument_list|)
DECL|field|level
specifier|private
name|String
name|level
decl_stmt|;
annotation|@
name|UriParam
DECL|field|marker
specifier|private
name|String
name|marker
decl_stmt|;
annotation|@
name|UriParam
DECL|field|groupSize
specifier|private
name|Integer
name|groupSize
decl_stmt|;
annotation|@
name|UriParam
DECL|field|groupInterval
specifier|private
name|Long
name|groupInterval
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|groupActiveOnly
specifier|private
name|Boolean
name|groupActiveOnly
decl_stmt|;
annotation|@
name|UriParam
DECL|field|groupDelay
specifier|private
name|Long
name|groupDelay
decl_stmt|;
comment|// we want to include the uri options of the DefaultExchangeFormatter
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|exchangeFormatter
specifier|private
name|DefaultExchangeFormatter
name|exchangeFormatter
decl_stmt|;
annotation|@
name|UriParam
DECL|field|logMask
specifier|private
name|Boolean
name|logMask
decl_stmt|;
DECL|method|LogEndpoint ()
specifier|public
name|LogEndpoint
parameter_list|()
block|{     }
DECL|method|LogEndpoint (String endpointUri, Component component)
specifier|public
name|LogEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
DECL|method|LogEndpoint (String endpointUri, Component component, Processor logger)
specifier|public
name|LogEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|,
name|Processor
name|logger
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|setLogger
argument_list|(
name|logger
argument_list|)
expr_stmt|;
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
if|if
condition|(
name|logger
operator|==
literal|null
condition|)
block|{
name|logger
operator|=
name|createLogger
argument_list|()
expr_stmt|;
block|}
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|logger
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
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|logger
argument_list|)
expr_stmt|;
block|}
DECL|method|setLogger (Processor logger)
specifier|public
name|void
name|setLogger
parameter_list|(
name|Processor
name|logger
parameter_list|)
block|{
name|this
operator|.
name|logger
operator|=
name|logger
expr_stmt|;
comment|// the logger is the processor
name|setProcessor
argument_list|(
name|this
operator|.
name|logger
argument_list|)
expr_stmt|;
block|}
DECL|method|getLogger ()
specifier|public
name|Processor
name|getLogger
parameter_list|()
block|{
return|return
name|logger
return|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
comment|// ensure logger is created and started first
if|if
condition|(
name|logger
operator|==
literal|null
condition|)
block|{
name|logger
operator|=
name|createLogger
argument_list|()
expr_stmt|;
block|}
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|logger
argument_list|)
expr_stmt|;
return|return
operator|new
name|LogProducer
argument_list|(
name|this
argument_list|,
name|logger
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createEndpointUri ()
specifier|protected
name|String
name|createEndpointUri
parameter_list|()
block|{
return|return
literal|"log:"
operator|+
name|logger
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Creates the logger {@link Processor} to be used.      */
DECL|method|createLogger ()
specifier|protected
name|Processor
name|createLogger
parameter_list|()
throws|throws
name|Exception
block|{
name|Processor
name|answer
decl_stmt|;
comment|// setup a new logger here
name|CamelLogger
name|camelLogger
decl_stmt|;
name|LoggingLevel
name|loggingLevel
init|=
name|LoggingLevel
operator|.
name|INFO
decl_stmt|;
if|if
condition|(
name|level
operator|!=
literal|null
condition|)
block|{
name|loggingLevel
operator|=
name|LoggingLevel
operator|.
name|valueOf
argument_list|(
name|level
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|providedLogger
operator|==
literal|null
condition|)
block|{
name|camelLogger
operator|=
operator|new
name|CamelLogger
argument_list|(
name|loggerName
argument_list|,
name|loggingLevel
argument_list|,
name|getMarker
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|camelLogger
operator|=
operator|new
name|CamelLogger
argument_list|(
name|providedLogger
argument_list|,
name|loggingLevel
argument_list|,
name|getMarker
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getGroupSize
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|=
operator|new
name|ThroughputLogger
argument_list|(
name|camelLogger
argument_list|,
name|getGroupSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|getGroupInterval
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Boolean
name|groupActiveOnly
init|=
name|getGroupActiveOnly
argument_list|()
operator|!=
literal|null
condition|?
name|getGroupActiveOnly
argument_list|()
else|:
name|Boolean
operator|.
name|TRUE
decl_stmt|;
name|Long
name|groupDelay
init|=
name|getGroupDelay
argument_list|()
decl_stmt|;
name|answer
operator|=
operator|new
name|ThroughputLogger
argument_list|(
name|camelLogger
argument_list|,
name|this
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|getGroupInterval
argument_list|()
argument_list|,
name|groupDelay
argument_list|,
name|groupActiveOnly
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
operator|new
name|CamelLogProcessor
argument_list|(
name|camelLogger
argument_list|,
name|localFormatter
argument_list|,
name|getMaskingFormatter
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// the logger is the processor
name|setProcessor
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|getMaskingFormatter ()
specifier|private
name|MaskingFormatter
name|getMaskingFormatter
parameter_list|()
block|{
if|if
condition|(
name|logMask
operator|!=
literal|null
condition|?
name|logMask
else|:
name|getCamelContext
argument_list|()
operator|.
name|isLogMask
argument_list|()
condition|)
block|{
name|MaskingFormatter
name|formatter
init|=
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
name|Constants
operator|.
name|CUSTOM_LOG_MASK_REF
argument_list|,
name|MaskingFormatter
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|formatter
operator|==
literal|null
condition|)
block|{
name|formatter
operator|=
operator|new
name|DefaultMaskingFormatter
argument_list|()
expr_stmt|;
block|}
return|return
name|formatter
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Logging level to use.      *<p/>      * The default value is INFO.      */
DECL|method|getLevel ()
specifier|public
name|String
name|getLevel
parameter_list|()
block|{
return|return
name|level
return|;
block|}
comment|/**      * Logging level to use.      *<p/>      * The default value is INFO.      */
DECL|method|setLevel (String level)
specifier|public
name|void
name|setLevel
parameter_list|(
name|String
name|level
parameter_list|)
block|{
name|this
operator|.
name|level
operator|=
name|level
expr_stmt|;
block|}
comment|/**      * An optional Marker name to use.      */
DECL|method|getMarker ()
specifier|public
name|String
name|getMarker
parameter_list|()
block|{
return|return
name|marker
return|;
block|}
comment|/**      * An optional Marker name to use.      */
DECL|method|setMarker (String marker)
specifier|public
name|void
name|setMarker
parameter_list|(
name|String
name|marker
parameter_list|)
block|{
name|this
operator|.
name|marker
operator|=
name|marker
expr_stmt|;
block|}
comment|/**      * An integer that specifies a group size for throughput logging.      */
DECL|method|getGroupSize ()
specifier|public
name|Integer
name|getGroupSize
parameter_list|()
block|{
return|return
name|groupSize
return|;
block|}
comment|/**      * An integer that specifies a group size for throughput logging.      */
DECL|method|setGroupSize (Integer groupSize)
specifier|public
name|void
name|setGroupSize
parameter_list|(
name|Integer
name|groupSize
parameter_list|)
block|{
name|this
operator|.
name|groupSize
operator|=
name|groupSize
expr_stmt|;
block|}
comment|/**      * If specified will group message stats by this time interval (in millis)      */
DECL|method|getGroupInterval ()
specifier|public
name|Long
name|getGroupInterval
parameter_list|()
block|{
return|return
name|groupInterval
return|;
block|}
comment|/**      * If specified will group message stats by this time interval (in millis)      */
DECL|method|setGroupInterval (Long groupInterval)
specifier|public
name|void
name|setGroupInterval
parameter_list|(
name|Long
name|groupInterval
parameter_list|)
block|{
name|this
operator|.
name|groupInterval
operator|=
name|groupInterval
expr_stmt|;
block|}
comment|/**      * If true, will hide stats when no new messages have been received for a time interval, if false, show stats regardless of message traffic.      */
DECL|method|getGroupActiveOnly ()
specifier|public
name|Boolean
name|getGroupActiveOnly
parameter_list|()
block|{
return|return
name|groupActiveOnly
return|;
block|}
comment|/**      * If true, will hide stats when no new messages have been received for a time interval, if false, show stats regardless of message traffic.      */
DECL|method|setGroupActiveOnly (Boolean groupActiveOnly)
specifier|public
name|void
name|setGroupActiveOnly
parameter_list|(
name|Boolean
name|groupActiveOnly
parameter_list|)
block|{
name|this
operator|.
name|groupActiveOnly
operator|=
name|groupActiveOnly
expr_stmt|;
block|}
comment|/**      * Set the initial delay for stats (in millis)      */
DECL|method|getGroupDelay ()
specifier|public
name|Long
name|getGroupDelay
parameter_list|()
block|{
return|return
name|groupDelay
return|;
block|}
comment|/**      * Set the initial delay for stats (in millis)      */
DECL|method|setGroupDelay (Long groupDelay)
specifier|public
name|void
name|setGroupDelay
parameter_list|(
name|Long
name|groupDelay
parameter_list|)
block|{
name|this
operator|.
name|groupDelay
operator|=
name|groupDelay
expr_stmt|;
block|}
DECL|method|getLocalFormatter ()
specifier|public
name|ExchangeFormatter
name|getLocalFormatter
parameter_list|()
block|{
return|return
name|localFormatter
return|;
block|}
DECL|method|setLocalFormatter (ExchangeFormatter localFormatter)
specifier|public
name|void
name|setLocalFormatter
parameter_list|(
name|ExchangeFormatter
name|localFormatter
parameter_list|)
block|{
name|this
operator|.
name|localFormatter
operator|=
name|localFormatter
expr_stmt|;
block|}
DECL|method|getProvidedLogger ()
specifier|public
name|Logger
name|getProvidedLogger
parameter_list|()
block|{
return|return
name|providedLogger
return|;
block|}
DECL|method|setProvidedLogger (Logger providedLogger)
specifier|public
name|void
name|setProvidedLogger
parameter_list|(
name|Logger
name|providedLogger
parameter_list|)
block|{
name|this
operator|.
name|providedLogger
operator|=
name|providedLogger
expr_stmt|;
block|}
comment|/**      * The logger name to use      */
DECL|method|getLoggerName ()
specifier|public
name|String
name|getLoggerName
parameter_list|()
block|{
return|return
name|loggerName
return|;
block|}
comment|/**      * The logger name to use      */
DECL|method|setLoggerName (String loggerName)
specifier|public
name|void
name|setLoggerName
parameter_list|(
name|String
name|loggerName
parameter_list|)
block|{
name|this
operator|.
name|loggerName
operator|=
name|loggerName
expr_stmt|;
block|}
DECL|method|getLogMask ()
specifier|public
name|Boolean
name|getLogMask
parameter_list|()
block|{
return|return
name|logMask
return|;
block|}
comment|/**      * If true, mask sensitive information like password or passphrase in the log.      */
DECL|method|setLogMask (Boolean logMask)
specifier|public
name|void
name|setLogMask
parameter_list|(
name|Boolean
name|logMask
parameter_list|)
block|{
name|this
operator|.
name|logMask
operator|=
name|logMask
expr_stmt|;
block|}
block|}
end_class

end_unit

