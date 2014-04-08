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
name|java
operator|.
name|util
operator|.
name|Locale
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
name|impl
operator|.
name|UriEndpointComponent
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
name|util
operator|.
name|CamelLogger
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

begin_comment
comment|/**  * The<a href="http://camel.apache.org/log.html">Log Component</a>  * to log message exchanges to the underlying logging mechanism.  *  * @version   */
end_comment

begin_class
DECL|class|LogComponent
specifier|public
class|class
name|LogComponent
extends|extends
name|UriEndpointComponent
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|LogComponent
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|exchangeFormatter
specifier|private
name|ExchangeFormatter
name|exchangeFormatter
decl_stmt|;
DECL|method|LogComponent ()
specifier|public
name|LogComponent
parameter_list|()
block|{
name|super
argument_list|(
name|LogEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|LoggingLevel
name|level
init|=
name|getLoggingLevel
argument_list|(
name|parameters
argument_list|)
decl_stmt|;
name|Logger
name|providedLogger
init|=
name|getLogger
argument_list|(
name|parameters
argument_list|)
decl_stmt|;
if|if
condition|(
name|providedLogger
operator|==
literal|null
condition|)
block|{
comment|// try to look up the logger in registry
name|Map
argument_list|<
name|String
argument_list|,
name|Logger
argument_list|>
name|availableLoggers
init|=
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|findByTypeWithName
argument_list|(
name|Logger
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|availableLoggers
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|providedLogger
operator|=
name|availableLoggers
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Using custom Logger: {}"
argument_list|,
name|providedLogger
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|availableLoggers
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"More than one {} instance found in the registry. Falling back to creating logger from URI {}."
argument_list|,
name|Logger
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|uri
argument_list|)
expr_stmt|;
block|}
block|}
name|LogEndpoint
name|endpoint
init|=
operator|new
name|LogEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setLevel
argument_list|(
name|level
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|CamelLogger
name|camelLogger
init|=
literal|null
decl_stmt|;
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
name|remaining
argument_list|,
name|level
argument_list|,
name|endpoint
operator|.
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
name|level
argument_list|,
name|endpoint
operator|.
name|getMarker
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Processor
name|logger
decl_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getGroupSize
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|logger
operator|=
operator|new
name|ThroughputLogger
argument_list|(
name|camelLogger
argument_list|,
name|endpoint
operator|.
name|getGroupSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|endpoint
operator|.
name|getGroupInterval
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Boolean
name|groupActiveOnly
init|=
name|endpoint
operator|.
name|getGroupActiveOnly
argument_list|()
operator|!=
literal|null
condition|?
name|endpoint
operator|.
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
name|endpoint
operator|.
name|getGroupDelay
argument_list|()
decl_stmt|;
name|logger
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
name|endpoint
operator|.
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
comment|// first, try to use the user-specified formatter (or the one picked up from the Registry and transferred to
comment|// the property by a previous endpoint initialisation); if null, try to pick it up from the Registry now
name|ExchangeFormatter
name|localFormatter
init|=
name|exchangeFormatter
decl_stmt|;
if|if
condition|(
name|localFormatter
operator|==
literal|null
condition|)
block|{
name|localFormatter
operator|=
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
literal|"logFormatter"
argument_list|,
name|ExchangeFormatter
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|localFormatter
operator|!=
literal|null
condition|)
block|{
name|exchangeFormatter
operator|=
name|localFormatter
expr_stmt|;
name|setProperties
argument_list|(
name|exchangeFormatter
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
block|}
block|}
comment|// if no formatter is available in the Registry, create a local one of the default type, for a single use
if|if
condition|(
name|localFormatter
operator|==
literal|null
condition|)
block|{
name|localFormatter
operator|=
operator|new
name|DefaultExchangeFormatter
argument_list|()
expr_stmt|;
name|setProperties
argument_list|(
name|localFormatter
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
block|}
name|logger
operator|=
operator|new
name|CamelLogProcessor
argument_list|(
name|camelLogger
argument_list|,
name|localFormatter
argument_list|)
expr_stmt|;
block|}
name|endpoint
operator|.
name|setLogger
argument_list|(
name|logger
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
comment|/**      * Gets the logging level, will default to use INFO if no level parameter provided.      */
DECL|method|getLoggingLevel (Map<String, Object> parameters)
specifier|protected
name|LoggingLevel
name|getLoggingLevel
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
block|{
name|String
name|levelText
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"level"
argument_list|,
name|String
operator|.
name|class
argument_list|,
literal|"INFO"
argument_list|)
decl_stmt|;
return|return
name|LoggingLevel
operator|.
name|valueOf
argument_list|(
name|levelText
operator|.
name|toUpperCase
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Gets optional {@link Logger} instance from parameters. If non-null, the provided instance will be used as      * {@link Logger} in {@link CamelLogger}      *       * @param parameters      * @return      */
DECL|method|getLogger (Map<String, Object> parameters)
specifier|protected
name|Logger
name|getLogger
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
block|{
return|return
name|getAndRemoveOrResolveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"logger"
argument_list|,
name|Logger
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|getExchangeFormatter ()
specifier|public
name|ExchangeFormatter
name|getExchangeFormatter
parameter_list|()
block|{
return|return
name|exchangeFormatter
return|;
block|}
comment|/**      * Sets a custom {@link ExchangeFormatter} to convert the Exchange to a String suitable for logging.      *<p />      * If not specified, we default to {@link DefaultExchangeFormatter}.      */
DECL|method|setExchangeFormatter (ExchangeFormatter exchangeFormatter)
specifier|public
name|void
name|setExchangeFormatter
parameter_list|(
name|ExchangeFormatter
name|exchangeFormatter
parameter_list|)
block|{
name|this
operator|.
name|exchangeFormatter
operator|=
name|exchangeFormatter
expr_stmt|;
block|}
block|}
end_class

end_unit

