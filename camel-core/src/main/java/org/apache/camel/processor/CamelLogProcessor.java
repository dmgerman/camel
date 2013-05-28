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
name|AsyncProcessor
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
name|AsyncProcessorHelper
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

begin_comment
comment|/**  * A {@link Processor} which just logs to a {@link CamelLogger} object which can be used  * as an exception handler instead of using a dead letter queue.  *<p/>  * The name<tt>CamelLogger</tt> has been chosen to avoid any name clash with log kits  * which has a<tt>Logger</tt> class.  *  * @version   */
end_comment

begin_class
DECL|class|CamelLogProcessor
specifier|public
class|class
name|CamelLogProcessor
implements|implements
name|AsyncProcessor
block|{
DECL|field|log
specifier|private
name|CamelLogger
name|log
decl_stmt|;
DECL|field|formatter
specifier|private
name|ExchangeFormatter
name|formatter
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
DECL|method|CamelLogProcessor (CamelLogger log)
specifier|public
name|CamelLogProcessor
parameter_list|(
name|CamelLogger
name|log
parameter_list|)
block|{
name|this
operator|.
name|formatter
operator|=
operator|new
name|DefaultExchangeFormatter
argument_list|()
expr_stmt|;
name|this
operator|.
name|log
operator|=
name|log
expr_stmt|;
block|}
DECL|method|CamelLogProcessor (CamelLogger log, ExchangeFormatter formatter)
specifier|public
name|CamelLogProcessor
parameter_list|(
name|CamelLogger
name|log
parameter_list|,
name|ExchangeFormatter
name|formatter
parameter_list|)
block|{
name|this
argument_list|(
name|log
argument_list|)
expr_stmt|;
name|this
operator|.
name|formatter
operator|=
name|formatter
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
name|log
operator|+
literal|"]"
return|;
block|}
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|AsyncProcessorHelper
operator|.
name|process
argument_list|(
name|this
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
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
name|log
operator|.
name|shouldLog
argument_list|()
condition|)
block|{
name|log
operator|.
name|log
argument_list|(
name|formatter
operator|.
name|format
argument_list|(
name|exchange
argument_list|)
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
name|log
operator|.
name|shouldLog
argument_list|()
condition|)
block|{
name|log
operator|.
name|log
argument_list|(
name|formatter
operator|.
name|format
argument_list|(
name|exchange
argument_list|)
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
name|log
operator|.
name|shouldLog
argument_list|()
condition|)
block|{
name|log
operator|.
name|log
argument_list|(
name|formatter
operator|.
name|format
argument_list|(
name|exchange
argument_list|)
operator|+
name|message
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getLogger ()
specifier|public
name|CamelLogger
name|getLogger
parameter_list|()
block|{
return|return
name|log
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
name|log
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
name|log
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
name|log
operator|.
name|setMarker
argument_list|(
name|marker
argument_list|)
expr_stmt|;
block|}
DECL|class|DefaultExchangeFormatter
specifier|static
class|class
name|DefaultExchangeFormatter
implements|implements
name|ExchangeFormatter
block|{
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

