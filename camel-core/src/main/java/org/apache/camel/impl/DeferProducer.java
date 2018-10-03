begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|support
operator|.
name|ServiceHelper
import|;
end_import

begin_comment
comment|/**  * A {@link Producer} that defers being started, until {@link org.apache.camel.CamelContext} has been started, this  * ensures that the producer is able to adapt to changes that may otherwise occur during starting  * CamelContext. If we do not defer starting the producer it may not adapt to those changes, and  * send messages to wrong endpoints.  */
end_comment

begin_class
DECL|class|DeferProducer
specifier|public
class|class
name|DeferProducer
extends|extends
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|ServiceSupport
implements|implements
name|Producer
implements|,
name|AsyncProcessor
block|{
DECL|field|delegate
specifier|private
name|Producer
name|delegate
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|Endpoint
name|endpoint
decl_stmt|;
DECL|method|DeferProducer (Endpoint endpoint)
specifier|public
name|DeferProducer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
annotation|@
name|Override
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
name|delegate
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Not started"
argument_list|)
throw|;
block|}
name|delegate
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
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
name|delegate
operator|==
literal|null
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|IllegalStateException
argument_list|(
literal|"Not started"
argument_list|)
argument_list|)
expr_stmt|;
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
if|if
condition|(
name|delegate
operator|instanceof
name|AsyncProcessor
condition|)
block|{
return|return
operator|(
operator|(
name|AsyncProcessor
operator|)
name|delegate
operator|)
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
return|;
block|}
comment|// fallback to sync mode
try|try
block|{
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
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
comment|// need to lookup endpoint again as it may be intercepted
name|Endpoint
name|lookup
init|=
name|endpoint
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getEndpoint
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
decl_stmt|;
name|delegate
operator|=
name|lookup
operator|.
name|createProducer
argument_list|()
expr_stmt|;
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|delegate
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
name|delegate
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
return|return
name|delegate
operator|.
name|isSingleton
argument_list|()
return|;
block|}
else|else
block|{
comment|// assume singleton by default
return|return
literal|true
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|Endpoint
name|getEndpoint
parameter_list|()
block|{
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
return|return
name|delegate
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|endpoint
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
return|return
name|delegate
operator|.
name|toString
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|"DelegateProducer["
operator|+
name|endpoint
operator|+
literal|"]"
return|;
block|}
block|}
block|}
end_class

end_unit

