begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.endpoint.dsl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|endpoint
operator|.
name|dsl
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Generated
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
name|EndpointConsumerBuilder
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
name|EndpointProducerBuilder
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
name|endpoint
operator|.
name|AbstractEndpointBuilder
import|;
end_import

begin_comment
comment|/**  * The log component logs message exchanges to the underlying logging mechanism.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|LogEndpointBuilderFactory
specifier|public
interface|interface
name|LogEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the Log component.      */
DECL|interface|LogEndpointBuilder
specifier|public
interface|interface
name|LogEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedLogEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedLogEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * If true, will hide stats when no new messages have been received for          * a time interval, if false, show stats regardless of message traffic.          *           * The option is a:<code>java.lang.Boolean</code> type.          *           * Group: producer          */
DECL|method|groupActiveOnly (Boolean groupActiveOnly)
specifier|default
name|LogEndpointBuilder
name|groupActiveOnly
parameter_list|(
name|Boolean
name|groupActiveOnly
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"groupActiveOnly"
argument_list|,
name|groupActiveOnly
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If true, will hide stats when no new messages have been received for          * a time interval, if false, show stats regardless of message traffic.          *           * The option will be converted to a<code>java.lang.Boolean</code>          * type.          *           * Group: producer          */
DECL|method|groupActiveOnly (String groupActiveOnly)
specifier|default
name|LogEndpointBuilder
name|groupActiveOnly
parameter_list|(
name|String
name|groupActiveOnly
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"groupActiveOnly"
argument_list|,
name|groupActiveOnly
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Set the initial delay for stats (in millis).          *           * The option is a:<code>java.lang.Long</code> type.          *           * Group: producer          */
DECL|method|groupDelay (Long groupDelay)
specifier|default
name|LogEndpointBuilder
name|groupDelay
parameter_list|(
name|Long
name|groupDelay
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"groupDelay"
argument_list|,
name|groupDelay
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Set the initial delay for stats (in millis).          *           * The option will be converted to a<code>java.lang.Long</code> type.          *           * Group: producer          */
DECL|method|groupDelay (String groupDelay)
specifier|default
name|LogEndpointBuilder
name|groupDelay
parameter_list|(
name|String
name|groupDelay
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"groupDelay"
argument_list|,
name|groupDelay
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If specified will group message stats by this time interval (in          * millis).          *           * The option is a:<code>java.lang.Long</code> type.          *           * Group: producer          */
DECL|method|groupInterval (Long groupInterval)
specifier|default
name|LogEndpointBuilder
name|groupInterval
parameter_list|(
name|Long
name|groupInterval
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"groupInterval"
argument_list|,
name|groupInterval
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If specified will group message stats by this time interval (in          * millis).          *           * The option will be converted to a<code>java.lang.Long</code> type.          *           * Group: producer          */
DECL|method|groupInterval (String groupInterval)
specifier|default
name|LogEndpointBuilder
name|groupInterval
parameter_list|(
name|String
name|groupInterval
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"groupInterval"
argument_list|,
name|groupInterval
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * An integer that specifies a group size for throughput logging.          *           * The option is a:<code>java.lang.Integer</code> type.          *           * Group: producer          */
DECL|method|groupSize (Integer groupSize)
specifier|default
name|LogEndpointBuilder
name|groupSize
parameter_list|(
name|Integer
name|groupSize
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"groupSize"
argument_list|,
name|groupSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * An integer that specifies a group size for throughput logging.          *           * The option will be converted to a<code>java.lang.Integer</code>          * type.          *           * Group: producer          */
DECL|method|groupSize (String groupSize)
specifier|default
name|LogEndpointBuilder
name|groupSize
parameter_list|(
name|String
name|groupSize
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"groupSize"
argument_list|,
name|groupSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Logging level to use. The default value is INFO.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|level (String level)
specifier|default
name|LogEndpointBuilder
name|level
parameter_list|(
name|String
name|level
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"level"
argument_list|,
name|level
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If true, mask sensitive information like password or passphrase in          * the log.          *           * The option is a:<code>java.lang.Boolean</code> type.          *           * Group: producer          */
DECL|method|logMask (Boolean logMask)
specifier|default
name|LogEndpointBuilder
name|logMask
parameter_list|(
name|Boolean
name|logMask
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"logMask"
argument_list|,
name|logMask
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If true, mask sensitive information like password or passphrase in          * the log.          *           * The option will be converted to a<code>java.lang.Boolean</code>          * type.          *           * Group: producer          */
DECL|method|logMask (String logMask)
specifier|default
name|LogEndpointBuilder
name|logMask
parameter_list|(
name|String
name|logMask
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"logMask"
argument_list|,
name|logMask
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * An optional Marker name to use.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|marker (String marker)
specifier|default
name|LogEndpointBuilder
name|marker
parameter_list|(
name|String
name|marker
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"marker"
argument_list|,
name|marker
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Limits the number of characters logged per line.          *           * The option is a:<code>int</code> type.          *           * Group: formatting          */
DECL|method|maxChars (int maxChars)
specifier|default
name|LogEndpointBuilder
name|maxChars
parameter_list|(
name|int
name|maxChars
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"maxChars"
argument_list|,
name|maxChars
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Limits the number of characters logged per line.          *           * The option will be converted to a<code>int</code> type.          *           * Group: formatting          */
DECL|method|maxChars (String maxChars)
specifier|default
name|LogEndpointBuilder
name|maxChars
parameter_list|(
name|String
name|maxChars
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"maxChars"
argument_list|,
name|maxChars
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If enabled then each information is outputted on a newline.          *           * The option is a:<code>boolean</code> type.          *           * Group: formatting          */
DECL|method|multiline (boolean multiline)
specifier|default
name|LogEndpointBuilder
name|multiline
parameter_list|(
name|boolean
name|multiline
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"multiline"
argument_list|,
name|multiline
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If enabled then each information is outputted on a newline.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: formatting          */
DECL|method|multiline (String multiline)
specifier|default
name|LogEndpointBuilder
name|multiline
parameter_list|(
name|String
name|multiline
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"multiline"
argument_list|,
name|multiline
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Quick option for turning all options on. (multiline, maxChars has to          * be manually set if to be used).          *           * The option is a:<code>boolean</code> type.          *           * Group: formatting          */
DECL|method|showAll (boolean showAll)
specifier|default
name|LogEndpointBuilder
name|showAll
parameter_list|(
name|boolean
name|showAll
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"showAll"
argument_list|,
name|showAll
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Quick option for turning all options on. (multiline, maxChars has to          * be manually set if to be used).          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: formatting          */
DECL|method|showAll (String showAll)
specifier|default
name|LogEndpointBuilder
name|showAll
parameter_list|(
name|String
name|showAll
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"showAll"
argument_list|,
name|showAll
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Show the message body.          *           * The option is a:<code>boolean</code> type.          *           * Group: formatting          */
DECL|method|showBody (boolean showBody)
specifier|default
name|LogEndpointBuilder
name|showBody
parameter_list|(
name|boolean
name|showBody
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"showBody"
argument_list|,
name|showBody
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Show the message body.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: formatting          */
DECL|method|showBody (String showBody)
specifier|default
name|LogEndpointBuilder
name|showBody
parameter_list|(
name|String
name|showBody
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"showBody"
argument_list|,
name|showBody
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Show the body Java type.          *           * The option is a:<code>boolean</code> type.          *           * Group: formatting          */
DECL|method|showBodyType (boolean showBodyType)
specifier|default
name|LogEndpointBuilder
name|showBodyType
parameter_list|(
name|boolean
name|showBodyType
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"showBodyType"
argument_list|,
name|showBodyType
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Show the body Java type.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: formatting          */
DECL|method|showBodyType (String showBodyType)
specifier|default
name|LogEndpointBuilder
name|showBodyType
parameter_list|(
name|String
name|showBodyType
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"showBodyType"
argument_list|,
name|showBodyType
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * f the exchange has a caught exception, show the exception message (no          * stack trace).A caught exception is stored as a property on the          * exchange (using the key org.apache.camel.Exchange#EXCEPTION_CAUGHT          * and for instance a doCatch can catch exceptions.          *           * The option is a:<code>boolean</code> type.          *           * Group: formatting          */
DECL|method|showCaughtException ( boolean showCaughtException)
specifier|default
name|LogEndpointBuilder
name|showCaughtException
parameter_list|(
name|boolean
name|showCaughtException
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"showCaughtException"
argument_list|,
name|showCaughtException
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * f the exchange has a caught exception, show the exception message (no          * stack trace).A caught exception is stored as a property on the          * exchange (using the key org.apache.camel.Exchange#EXCEPTION_CAUGHT          * and for instance a doCatch can catch exceptions.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: formatting          */
DECL|method|showCaughtException ( String showCaughtException)
specifier|default
name|LogEndpointBuilder
name|showCaughtException
parameter_list|(
name|String
name|showCaughtException
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"showCaughtException"
argument_list|,
name|showCaughtException
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If the exchange has an exception, show the exception message (no          * stacktrace).          *           * The option is a:<code>boolean</code> type.          *           * Group: formatting          */
DECL|method|showException (boolean showException)
specifier|default
name|LogEndpointBuilder
name|showException
parameter_list|(
name|boolean
name|showException
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"showException"
argument_list|,
name|showException
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If the exchange has an exception, show the exception message (no          * stacktrace).          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: formatting          */
DECL|method|showException (String showException)
specifier|default
name|LogEndpointBuilder
name|showException
parameter_list|(
name|String
name|showException
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"showException"
argument_list|,
name|showException
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Show the unique exchange ID.          *           * The option is a:<code>boolean</code> type.          *           * Group: formatting          */
DECL|method|showExchangeId (boolean showExchangeId)
specifier|default
name|LogEndpointBuilder
name|showExchangeId
parameter_list|(
name|boolean
name|showExchangeId
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"showExchangeId"
argument_list|,
name|showExchangeId
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Show the unique exchange ID.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: formatting          */
DECL|method|showExchangeId (String showExchangeId)
specifier|default
name|LogEndpointBuilder
name|showExchangeId
parameter_list|(
name|String
name|showExchangeId
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"showExchangeId"
argument_list|,
name|showExchangeId
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Shows the Message Exchange Pattern (or MEP for short).          *           * The option is a:<code>boolean</code> type.          *           * Group: formatting          */
DECL|method|showExchangePattern ( boolean showExchangePattern)
specifier|default
name|LogEndpointBuilder
name|showExchangePattern
parameter_list|(
name|boolean
name|showExchangePattern
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"showExchangePattern"
argument_list|,
name|showExchangePattern
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Shows the Message Exchange Pattern (or MEP for short).          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: formatting          */
DECL|method|showExchangePattern ( String showExchangePattern)
specifier|default
name|LogEndpointBuilder
name|showExchangePattern
parameter_list|(
name|String
name|showExchangePattern
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"showExchangePattern"
argument_list|,
name|showExchangePattern
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If enabled Camel will output files.          *           * The option is a:<code>boolean</code> type.          *           * Group: formatting          */
DECL|method|showFiles (boolean showFiles)
specifier|default
name|LogEndpointBuilder
name|showFiles
parameter_list|(
name|boolean
name|showFiles
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"showFiles"
argument_list|,
name|showFiles
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If enabled Camel will output files.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: formatting          */
DECL|method|showFiles (String showFiles)
specifier|default
name|LogEndpointBuilder
name|showFiles
parameter_list|(
name|String
name|showFiles
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"showFiles"
argument_list|,
name|showFiles
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If enabled Camel will on Future objects wait for it to complete to          * obtain the payload to be logged.          *           * The option is a:<code>boolean</code> type.          *           * Group: formatting          */
DECL|method|showFuture (boolean showFuture)
specifier|default
name|LogEndpointBuilder
name|showFuture
parameter_list|(
name|boolean
name|showFuture
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"showFuture"
argument_list|,
name|showFuture
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If enabled Camel will on Future objects wait for it to complete to          * obtain the payload to be logged.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: formatting          */
DECL|method|showFuture (String showFuture)
specifier|default
name|LogEndpointBuilder
name|showFuture
parameter_list|(
name|String
name|showFuture
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"showFuture"
argument_list|,
name|showFuture
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Show the message headers.          *           * The option is a:<code>boolean</code> type.          *           * Group: formatting          */
DECL|method|showHeaders (boolean showHeaders)
specifier|default
name|LogEndpointBuilder
name|showHeaders
parameter_list|(
name|boolean
name|showHeaders
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"showHeaders"
argument_list|,
name|showHeaders
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Show the message headers.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: formatting          */
DECL|method|showHeaders (String showHeaders)
specifier|default
name|LogEndpointBuilder
name|showHeaders
parameter_list|(
name|String
name|showHeaders
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"showHeaders"
argument_list|,
name|showHeaders
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If the exchange has an out message, show the out message.          *           * The option is a:<code>boolean</code> type.          *           * Group: formatting          */
DECL|method|showOut (boolean showOut)
specifier|default
name|LogEndpointBuilder
name|showOut
parameter_list|(
name|boolean
name|showOut
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"showOut"
argument_list|,
name|showOut
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If the exchange has an out message, show the out message.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: formatting          */
DECL|method|showOut (String showOut)
specifier|default
name|LogEndpointBuilder
name|showOut
parameter_list|(
name|String
name|showOut
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"showOut"
argument_list|,
name|showOut
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Show the exchange properties.          *           * The option is a:<code>boolean</code> type.          *           * Group: formatting          */
DECL|method|showProperties (boolean showProperties)
specifier|default
name|LogEndpointBuilder
name|showProperties
parameter_list|(
name|boolean
name|showProperties
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"showProperties"
argument_list|,
name|showProperties
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Show the exchange properties.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: formatting          */
DECL|method|showProperties (String showProperties)
specifier|default
name|LogEndpointBuilder
name|showProperties
parameter_list|(
name|String
name|showProperties
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"showProperties"
argument_list|,
name|showProperties
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Show the stack trace, if an exchange has an exception. Only effective          * if one of showAll, showException or showCaughtException are enabled.          *           * The option is a:<code>boolean</code> type.          *           * Group: formatting          */
DECL|method|showStackTrace (boolean showStackTrace)
specifier|default
name|LogEndpointBuilder
name|showStackTrace
parameter_list|(
name|boolean
name|showStackTrace
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"showStackTrace"
argument_list|,
name|showStackTrace
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Show the stack trace, if an exchange has an exception. Only effective          * if one of showAll, showException or showCaughtException are enabled.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: formatting          */
DECL|method|showStackTrace (String showStackTrace)
specifier|default
name|LogEndpointBuilder
name|showStackTrace
parameter_list|(
name|String
name|showStackTrace
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"showStackTrace"
argument_list|,
name|showStackTrace
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether Camel should show stream bodies or not (eg such as          * java.io.InputStream). Beware if you enable this option then you may          * not be able later to access the message body as the stream have          * already been read by this logger. To remedy this you will have to use          * Stream Caching.          *           * The option is a:<code>boolean</code> type.          *           * Group: formatting          */
DECL|method|showStreams (boolean showStreams)
specifier|default
name|LogEndpointBuilder
name|showStreams
parameter_list|(
name|boolean
name|showStreams
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"showStreams"
argument_list|,
name|showStreams
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether Camel should show stream bodies or not (eg such as          * java.io.InputStream). Beware if you enable this option then you may          * not be able later to access the message body as the stream have          * already been read by this logger. To remedy this you will have to use          * Stream Caching.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: formatting          */
DECL|method|showStreams (String showStreams)
specifier|default
name|LogEndpointBuilder
name|showStreams
parameter_list|(
name|String
name|showStreams
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"showStreams"
argument_list|,
name|showStreams
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether to skip line separators when logging the message body.This          * allows to log the message body in one line, setting this option to          * false will preserve any line separators from the body, which then          * will log the body as is.          *           * The option is a:<code>boolean</code> type.          *           * Group: formatting          */
DECL|method|skipBodyLineSeparator ( boolean skipBodyLineSeparator)
specifier|default
name|LogEndpointBuilder
name|skipBodyLineSeparator
parameter_list|(
name|boolean
name|skipBodyLineSeparator
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"skipBodyLineSeparator"
argument_list|,
name|skipBodyLineSeparator
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether to skip line separators when logging the message body.This          * allows to log the message body in one line, setting this option to          * false will preserve any line separators from the body, which then          * will log the body as is.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: formatting          */
DECL|method|skipBodyLineSeparator ( String skipBodyLineSeparator)
specifier|default
name|LogEndpointBuilder
name|skipBodyLineSeparator
parameter_list|(
name|String
name|skipBodyLineSeparator
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"skipBodyLineSeparator"
argument_list|,
name|skipBodyLineSeparator
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the outputs style to use.          *           * The option is a:          *<code>org.apache.camel.support.processor.DefaultExchangeFormatter$OutputStyle</code> type.          *           * Group: formatting          */
DECL|method|style (OutputStyle style)
specifier|default
name|LogEndpointBuilder
name|style
parameter_list|(
name|OutputStyle
name|style
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"style"
argument_list|,
name|style
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the outputs style to use.          *           * The option will be converted to a          *<code>org.apache.camel.support.processor.DefaultExchangeFormatter$OutputStyle</code> type.          *           * Group: formatting          */
DECL|method|style (String style)
specifier|default
name|LogEndpointBuilder
name|style
parameter_list|(
name|String
name|style
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"style"
argument_list|,
name|style
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the Log component.      */
DECL|interface|AdvancedLogEndpointBuilder
specifier|public
interface|interface
name|AdvancedLogEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|LogEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|LogEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedLogEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( String basicPropertyBinding)
specifier|default
name|AdvancedLogEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|String
name|basicPropertyBinding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous (boolean synchronous)
specifier|default
name|AdvancedLogEndpointBuilder
name|synchronous
parameter_list|(
name|boolean
name|synchronous
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous (String synchronous)
specifier|default
name|AdvancedLogEndpointBuilder
name|synchronous
parameter_list|(
name|String
name|synchronous
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Proxy enum for      *<code>org.apache.camel.support.processor.DefaultExchangeFormatter$OutputStyle</code> enum.      */
DECL|enum|OutputStyle
enum|enum
name|OutputStyle
block|{
DECL|enumConstant|Default
name|Default
block|,
DECL|enumConstant|Tab
name|Tab
block|,
DECL|enumConstant|Fixed
name|Fixed
block|;     }
comment|/**      * Log (camel-log)      * The log component logs message exchanges to the underlying logging      * mechanism.      *       * Category: core,monitoring      * Available as of version: 1.1      * Maven coordinates: org.apache.camel:camel-log      *       * Syntax:<code>log:loggerName</code>      *       * Path parameter: loggerName (required)      * The logger name to use      */
DECL|method|log (String path)
specifier|default
name|LogEndpointBuilder
name|log
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|LogEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|LogEndpointBuilder
implements|,
name|AdvancedLogEndpointBuilder
block|{
specifier|public
name|LogEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"log"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|LogEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

