begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.stub
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|stub
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
name|WaitForTaskToComplete
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
name|component
operator|.
name|seda
operator|.
name|QueueReference
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
name|component
operator|.
name|seda
operator|.
name|SedaEndpoint
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
name|component
operator|.
name|seda
operator|.
name|SedaProducer
import|;
end_import

begin_class
DECL|class|StubProducer
specifier|public
class|class
name|StubProducer
extends|extends
name|SedaProducer
block|{
DECL|method|StubProducer (SedaEndpoint endpoint, WaitForTaskToComplete waitForTaskToComplete, long timeout, boolean blockWhenFull, boolean discardWhenFull, long offerTimeout)
specifier|public
name|StubProducer
parameter_list|(
name|SedaEndpoint
name|endpoint
parameter_list|,
name|WaitForTaskToComplete
name|waitForTaskToComplete
parameter_list|,
name|long
name|timeout
parameter_list|,
name|boolean
name|blockWhenFull
parameter_list|,
name|boolean
name|discardWhenFull
parameter_list|,
name|long
name|offerTimeout
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|waitForTaskToComplete
argument_list|,
name|timeout
argument_list|,
name|blockWhenFull
argument_list|,
name|discardWhenFull
argument_list|,
name|offerTimeout
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|StubEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|StubEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
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
name|AsyncCallback
name|cb
init|=
name|callback
decl_stmt|;
name|QueueReference
name|queueReference
init|=
name|getEndpoint
argument_list|()
operator|.
name|getQueueReference
argument_list|()
decl_stmt|;
name|boolean
name|empty
init|=
name|queueReference
operator|==
literal|null
operator|||
operator|!
name|queueReference
operator|.
name|hasConsumers
argument_list|()
decl_stmt|;
comment|// if no consumers then use InOnly mode
specifier|final
name|ExchangePattern
name|pattern
init|=
name|exchange
operator|.
name|getPattern
argument_list|()
decl_stmt|;
if|if
condition|(
name|empty
operator|&&
name|pattern
operator|!=
name|ExchangePattern
operator|.
name|InOnly
condition|)
block|{
name|exchange
operator|.
name|setPattern
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
expr_stmt|;
name|cb
operator|=
name|doneSync
lambda|->
block|{
comment|// and restore the old pattern after processing
name|exchange
operator|.
name|setPattern
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
name|doneSync
argument_list|)
expr_stmt|;
block|}
expr_stmt|;
block|}
return|return
name|super
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|cb
argument_list|)
return|;
block|}
block|}
end_class

end_unit

