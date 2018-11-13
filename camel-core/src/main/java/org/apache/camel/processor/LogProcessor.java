begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
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
name|Expression
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
name|Traceable
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
name|support
operator|.
name|AsyncProcessorSupport
import|;
end_import

begin_comment
comment|/**  * A processor which evaluates an {@link Expression} and logs it.  */
end_comment

begin_class
DECL|class|LogProcessor
specifier|public
class|class
name|LogProcessor
extends|extends
name|AsyncProcessorSupport
implements|implements
name|Traceable
implements|,
name|IdAware
block|{
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
DECL|field|expression
specifier|private
specifier|final
name|Expression
name|expression
decl_stmt|;
DECL|field|logger
specifier|private
specifier|final
name|CamelLogger
name|logger
decl_stmt|;
DECL|field|formatter
specifier|private
specifier|final
name|MaskingFormatter
name|formatter
decl_stmt|;
DECL|field|listeners
specifier|private
specifier|final
name|Set
argument_list|<
name|LogListener
argument_list|>
name|listeners
decl_stmt|;
DECL|method|LogProcessor (Expression expression, CamelLogger logger, MaskingFormatter formatter, Set<LogListener> listeners)
specifier|public
name|LogProcessor
parameter_list|(
name|Expression
name|expression
parameter_list|,
name|CamelLogger
name|logger
parameter_list|,
name|MaskingFormatter
name|formatter
parameter_list|,
name|Set
argument_list|<
name|LogListener
argument_list|>
name|listeners
parameter_list|)
block|{
name|this
operator|.
name|expression
operator|=
name|expression
expr_stmt|;
name|this
operator|.
name|logger
operator|=
name|logger
expr_stmt|;
name|this
operator|.
name|formatter
operator|=
name|formatter
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
try|try
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
name|msg
init|=
name|expression
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|formatter
operator|!=
literal|null
condition|)
block|{
name|msg
operator|=
name|formatter
operator|.
name|format
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
name|msg
operator|=
name|fireListeners
argument_list|(
name|exchange
argument_list|,
name|msg
argument_list|)
expr_stmt|;
name|logger
operator|.
name|doLog
argument_list|(
name|msg
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
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
comment|// callback must be invoked
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
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
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Log("
operator|+
name|logger
operator|.
name|getLog
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|")["
operator|+
name|expression
operator|+
literal|"]"
return|;
block|}
DECL|method|getTraceLabel ()
specifier|public
name|String
name|getTraceLabel
parameter_list|()
block|{
return|return
literal|"log["
operator|+
name|expression
operator|+
literal|"]"
return|;
block|}
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
DECL|method|getExpression ()
specifier|public
name|Expression
name|getExpression
parameter_list|()
block|{
return|return
name|expression
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
DECL|method|getLogFormatter ()
specifier|public
name|MaskingFormatter
name|getLogFormatter
parameter_list|()
block|{
return|return
name|formatter
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
comment|// noop
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
comment|// noop
block|}
block|}
end_class

end_unit

