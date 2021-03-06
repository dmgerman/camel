begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.disruptor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|disruptor
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
name|ExchangeHelper
import|;
end_import

begin_comment
comment|/**  * Implementation of the {@link SynchronizedExchange} interface optimized for single consumers.  */
end_comment

begin_class
DECL|class|SingleConsumerSynchronizedExchange
specifier|public
class|class
name|SingleConsumerSynchronizedExchange
extends|extends
name|AbstractSynchronizedExchange
block|{
DECL|method|SingleConsumerSynchronizedExchange (Exchange exchange)
specifier|public
name|SingleConsumerSynchronizedExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|super
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|consumed (Exchange result)
specifier|public
name|void
name|consumed
parameter_list|(
name|Exchange
name|result
parameter_list|)
block|{
name|ExchangeHelper
operator|.
name|copyResults
argument_list|(
name|getExchange
argument_list|()
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|performSynchronization
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

