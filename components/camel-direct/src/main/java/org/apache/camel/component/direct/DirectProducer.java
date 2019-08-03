begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.direct
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|direct
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
name|support
operator|.
name|DefaultAsyncProducer
import|;
end_import

begin_comment
comment|/**  * The direct producer.  */
end_comment

begin_class
DECL|class|DirectProducer
specifier|public
class|class
name|DirectProducer
extends|extends
name|DefaultAsyncProducer
block|{
DECL|field|endpoint
specifier|private
specifier|final
name|DirectEndpoint
name|endpoint
decl_stmt|;
DECL|method|DirectProducer (DirectEndpoint endpoint)
specifier|public
name|DirectProducer
parameter_list|(
name|DirectEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
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
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|endpoint
operator|.
name|addProducer
argument_list|(
name|this
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
name|endpoint
operator|.
name|removeProducer
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
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
name|DirectConsumer
name|consumer
init|=
name|endpoint
operator|.
name|getConsumer
argument_list|()
decl_stmt|;
if|if
condition|(
name|consumer
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|endpoint
operator|.
name|isFailIfNoConsumers
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|DirectConsumerNotAvailableException
argument_list|(
literal|"No consumers available on endpoint: "
operator|+
name|endpoint
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
else|else
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"message ignored, no consumers available on endpoint: {}"
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|consumer
operator|.
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
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
name|DirectConsumer
name|consumer
init|=
name|endpoint
operator|.
name|getConsumer
argument_list|()
decl_stmt|;
if|if
condition|(
name|consumer
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|endpoint
operator|.
name|isFailIfNoConsumers
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|DirectConsumerNotAvailableException
argument_list|(
literal|"No consumers available on endpoint: "
operator|+
name|endpoint
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"message ignored, no consumers available on endpoint: {}"
argument_list|,
name|endpoint
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
else|else
block|{
return|return
name|consumer
operator|.
name|getAsyncProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
return|;
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
block|}
block|}
end_class

end_unit

