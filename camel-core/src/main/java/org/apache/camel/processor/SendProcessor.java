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
name|ExchangePattern
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
name|ServiceSupport
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
comment|/**  * Processor for forwarding exchanges to an endpoint destination.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|SendProcessor
specifier|public
class|class
name|SendProcessor
extends|extends
name|ServiceSupport
implements|implements
name|Processor
block|{
DECL|field|LOG
specifier|protected
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
name|SendProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|destination
specifier|protected
name|Endpoint
name|destination
decl_stmt|;
DECL|field|producer
specifier|protected
name|Producer
name|producer
decl_stmt|;
DECL|field|pattern
specifier|protected
name|ExchangePattern
name|pattern
decl_stmt|;
DECL|method|SendProcessor (Endpoint destination)
specifier|public
name|SendProcessor
parameter_list|(
name|Endpoint
name|destination
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|destination
argument_list|,
literal|"destination"
argument_list|)
expr_stmt|;
name|this
operator|.
name|destination
operator|=
name|destination
expr_stmt|;
block|}
DECL|method|SendProcessor (Endpoint destination, ExchangePattern pattern)
specifier|public
name|SendProcessor
parameter_list|(
name|Endpoint
name|destination
parameter_list|,
name|ExchangePattern
name|pattern
parameter_list|)
block|{
name|this
argument_list|(
name|destination
argument_list|)
expr_stmt|;
name|this
operator|.
name|pattern
operator|=
name|pattern
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
literal|"sendTo("
operator|+
name|destination
operator|+
operator|(
name|pattern
operator|!=
literal|null
condition|?
literal|" "
operator|+
name|pattern
else|:
literal|""
operator|)
operator|+
literal|")"
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
if|if
condition|(
name|producer
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|isStopped
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Ignoring exchange sent after processor is stopped: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"No producer, this processor has not been started: "
operator|+
name|this
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|exchange
operator|=
name|configureExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
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
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
name|producer
operator|=
name|destination
operator|.
name|createProducer
argument_list|()
expr_stmt|;
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|this
operator|.
name|producer
argument_list|)
expr_stmt|;
block|}
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
name|this
operator|.
name|producer
argument_list|)
expr_stmt|;
name|this
operator|.
name|producer
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|configureExchange (Exchange exchange)
specifier|protected
name|Exchange
name|configureExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|pattern
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|setPattern
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
block|}
return|return
name|exchange
return|;
block|}
block|}
end_class

end_unit

