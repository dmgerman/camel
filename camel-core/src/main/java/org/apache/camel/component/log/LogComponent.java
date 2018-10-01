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
name|impl
operator|.
name|DefaultComponent
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
comment|/**  * The<a href="http://camel.apache.org/log.html">Log Component</a>  * is for logging message exchanges via the underlying logging mechanism.  */
end_comment

begin_class
DECL|class|LogComponent
specifier|public
class|class
name|LogComponent
extends|extends
name|DefaultComponent
block|{
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|exchangeFormatter
specifier|private
name|ExchangeFormatter
name|exchangeFormatter
decl_stmt|;
DECL|method|LogComponent ()
specifier|public
name|LogComponent
parameter_list|()
block|{     }
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
name|log
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
name|log
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
if|if
condition|(
name|providedLogger
operator|==
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setLoggerName
argument_list|(
name|remaining
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|endpoint
operator|.
name|setProvidedLogger
argument_list|(
name|providedLogger
argument_list|)
expr_stmt|;
block|}
comment|// first, try to pick up the ExchangeFormatter from the registry
name|ExchangeFormatter
name|localFormatter
init|=
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
decl_stmt|;
if|if
condition|(
name|localFormatter
operator|!=
literal|null
condition|)
block|{
name|setProperties
argument_list|(
name|localFormatter
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|localFormatter
operator|==
literal|null
operator|&&
name|exchangeFormatter
operator|!=
literal|null
condition|)
block|{
comment|// do not set properties, the exchangeFormatter is explicitly set, therefore the
comment|// user would have set its properties explicitly too
name|localFormatter
operator|=
name|exchangeFormatter
expr_stmt|;
block|}
else|else
block|{
comment|// if no formatter is available in the Registry, create a local one of the default type, for a single use
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
name|endpoint
operator|.
name|setLocalFormatter
argument_list|(
name|localFormatter
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
comment|/**      * Gets optional {@link Logger} instance from parameters. If non-null, the provided instance will be used as      * {@link Logger} in {@link CamelLogger}      *      * @param parameters the parameters      * @return the Logger object from the parameter      */
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

