begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|processor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|spi
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
name|IdAware
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
name|LogListener
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
name|support
operator|.
name|AsyncProcessorSupport
import|;
end_import

begin_comment
comment|/**  * A {@link Processor} which just logs to a {@link CamelLogger} object which can be used  * as an exception handler instead of using a dead letter queue.  *<p/>  * The name<tt>CamelLogger</tt> has been chosen to avoid any name clash with log kits  * which has a<tt>Logger</tt> class.  */
end_comment

begin_class
DECL|class|CamelLogProcessor
specifier|public
class|class
name|CamelLogProcessor
extends|extends
name|AsyncProcessorSupport
implements|implements
name|IdAware
block|{
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
DECL|field|logger
specifier|private
name|CamelLogger
name|logger
decl_stmt|;
DECL|field|formatter
specifier|private
name|ExchangeFormatter
name|formatter
decl_stmt|;
DECL|field|maskingFormatter
specifier|private
name|MaskingFormatter
name|maskingFormatter
decl_stmt|;
DECL|field|listeners
specifier|private
name|Set
argument_list|<
name|LogListener
argument_list|>
name|listeners
decl_stmt|;
DECL|method|CamelLogProcessor ()
specifier|public
name|CamelLogProcessor
parameter_list|()
block|{
name|this
argument_list|(
operator|new
name|CamelLogger
argument_list|(
name|CamelLogProcessor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|CamelLogProcessor (CamelLogger logger)
specifier|public
name|CamelLogProcessor
parameter_list|(
name|CamelLogger
name|logger
parameter_list|)
block|{
name|this
operator|.
name|formatter
operator|=
operator|new
name|ToStringExchangeFormatter
argument_list|()
expr_stmt|;
name|this
operator|.
name|logger
operator|=
name|logger
expr_stmt|;
block|}
DECL|method|CamelLogProcessor (CamelLogger logger, ExchangeFormatter formatter, MaskingFormatter maskingFormatter, Set<LogListener> listeners)
specifier|public
name|CamelLogProcessor
parameter_list|(
name|CamelLogger
name|logger
parameter_list|,
name|ExchangeFormatter
name|formatter
parameter_list|,
name|MaskingFormatter
name|maskingFormatter
parameter_list|,
name|Set
argument_list|<
name|LogListener
argument_list|>
name|listeners
parameter_list|)
block|{
name|this
argument_list|(
name|logger
argument_list|)
expr_stmt|;
name|this
operator|.
name|formatter
operator|=
name|formatter
expr_stmt|;
name|this
operator|.
name|maskingFormatter
operator|=
name|maskingFormatter
expr_stmt|;
name|this
operator|.
name|listeners
operator|=
name|listeners
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
literal|"Logger["
operator|+
name|logger
operator|+
literal|"]"
return|;
block|}
annotation|@
name|Override
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
annotation|@
name|Override
DECL|method|setId (String id)
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
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
name|logger
operator|.
name|shouldLog
argument_list|()
condition|)
block|{
name|String
name|output
init|=
name|formatter
operator|.
name|format
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|maskingFormatter
operator|!=
literal|null
condition|)
block|{
name|output
operator|=
name|maskingFormatter
operator|.
name|format
argument_list|(
name|output
argument_list|)
expr_stmt|;
block|}
name|output
operator|=
name|fireListeners
argument_list|(
name|exchange
argument_list|,
name|output
argument_list|)
expr_stmt|;
name|logger
operator|.
name|log
argument_list|(
name|output
argument_list|)
expr_stmt|;
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
DECL|method|process (Exchange exchange, Throwable exception)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Throwable
name|exception
parameter_list|)
block|{
if|if
condition|(
name|logger
operator|.
name|shouldLog
argument_list|()
condition|)
block|{
name|String
name|output
init|=
name|formatter
operator|.
name|format
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|maskingFormatter
operator|!=
literal|null
condition|)
block|{
name|output
operator|=
name|maskingFormatter
operator|.
name|format
argument_list|(
name|output
argument_list|)
expr_stmt|;
block|}
name|output
operator|=
name|fireListeners
argument_list|(
name|exchange
argument_list|,
name|output
argument_list|)
expr_stmt|;
name|logger
operator|.
name|log
argument_list|(
name|output
argument_list|,
name|exception
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|process (Exchange exchange, String message)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|message
parameter_list|)
block|{
if|if
condition|(
name|logger
operator|.
name|shouldLog
argument_list|()
condition|)
block|{
name|String
name|output
init|=
name|formatter
operator|.
name|format
argument_list|(
name|exchange
argument_list|)
operator|+
name|message
decl_stmt|;
if|if
condition|(
name|maskingFormatter
operator|!=
literal|null
condition|)
block|{
name|output
operator|=
name|maskingFormatter
operator|.
name|format
argument_list|(
name|output
argument_list|)
expr_stmt|;
block|}
name|output
operator|=
name|fireListeners
argument_list|(
name|exchange
argument_list|,
name|output
argument_list|)
expr_stmt|;
name|logger
operator|.
name|log
argument_list|(
name|output
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|fireListeners (Exchange exchange, String message)
specifier|private
name|String
name|fireListeners
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|message
parameter_list|)
block|{
if|if
condition|(
name|listeners
operator|==
literal|null
condition|)
block|{
return|return
name|message
return|;
block|}
for|for
control|(
name|LogListener
name|listener
range|:
name|listeners
control|)
block|{
if|if
condition|(
name|listener
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
try|try
block|{
name|String
name|output
init|=
name|listener
operator|.
name|onLog
argument_list|(
name|exchange
argument_list|,
name|logger
argument_list|,
name|message
argument_list|)
decl_stmt|;
name|message
operator|=
name|output
operator|!=
literal|null
condition|?
name|output
else|:
name|message
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Ignoring an exception thrown by {}: {}"
argument_list|,
name|listener
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|t
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|""
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|message
return|;
block|}
DECL|method|getLogger ()
specifier|public
name|CamelLogger
name|getLogger
parameter_list|()
block|{
return|return
name|logger
return|;
block|}
DECL|method|setLogName (String logName)
specifier|public
name|void
name|setLogName
parameter_list|(
name|String
name|logName
parameter_list|)
block|{
name|logger
operator|.
name|setLogName
argument_list|(
name|logName
argument_list|)
expr_stmt|;
block|}
DECL|method|setLevel (LoggingLevel level)
specifier|public
name|void
name|setLevel
parameter_list|(
name|LoggingLevel
name|level
parameter_list|)
block|{
name|logger
operator|.
name|setLevel
argument_list|(
name|level
argument_list|)
expr_stmt|;
block|}
DECL|method|setMarker (String marker)
specifier|public
name|void
name|setMarker
parameter_list|(
name|String
name|marker
parameter_list|)
block|{
name|logger
operator|.
name|setMarker
argument_list|(
name|marker
argument_list|)
expr_stmt|;
block|}
DECL|method|setMaskingFormatter (MaskingFormatter maskingFormatter)
specifier|public
name|void
name|setMaskingFormatter
parameter_list|(
name|MaskingFormatter
name|maskingFormatter
parameter_list|)
block|{
name|this
operator|.
name|maskingFormatter
operator|=
name|maskingFormatter
expr_stmt|;
block|}
comment|/**      * {@link ExchangeFormatter} that calls<tt>toString</tt> on the {@link Exchange}.      */
DECL|class|ToStringExchangeFormatter
specifier|static
class|class
name|ToStringExchangeFormatter
implements|implements
name|ExchangeFormatter
block|{
annotation|@
name|Override
DECL|method|format (Exchange exchange)
specifier|public
name|String
name|format
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

