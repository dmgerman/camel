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
name|util
operator|.
name|ServiceHelper
import|;
end_import

begin_comment
comment|/**  * Ensures a {@link Producer} is executed within an {@link org.apache.camel.spi.UnitOfWork}.  *  * @version   */
end_comment

begin_class
DECL|class|UnitOfWorkProducer
specifier|public
specifier|final
class|class
name|UnitOfWorkProducer
implements|implements
name|Producer
block|{
DECL|field|producer
specifier|private
specifier|final
name|Producer
name|producer
decl_stmt|;
DECL|field|processor
specifier|private
specifier|final
name|Processor
name|processor
decl_stmt|;
comment|/**      * The producer which should be executed within an {@link org.apache.camel.spi.UnitOfWork}.      *      * @param producer the producer      */
DECL|method|UnitOfWorkProducer (Producer producer)
specifier|public
name|UnitOfWorkProducer
parameter_list|(
name|Producer
name|producer
parameter_list|)
block|{
name|this
operator|.
name|producer
operator|=
name|producer
expr_stmt|;
comment|// wrap in unit of work
name|CamelInternalProcessor
name|internal
init|=
operator|new
name|CamelInternalProcessor
argument_list|(
name|producer
argument_list|)
decl_stmt|;
name|internal
operator|.
name|addTask
argument_list|(
operator|new
name|CamelInternalProcessor
operator|.
name|UnitOfWorkProcessorTask
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|processor
operator|=
name|internal
expr_stmt|;
block|}
DECL|method|getEndpoint ()
specifier|public
name|Endpoint
name|getEndpoint
parameter_list|()
block|{
return|return
name|producer
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
DECL|method|createExchange ()
specifier|public
name|Exchange
name|createExchange
parameter_list|()
block|{
return|return
name|producer
operator|.
name|createExchange
argument_list|()
return|;
block|}
DECL|method|createExchange (ExchangePattern pattern)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|ExchangePattern
name|pattern
parameter_list|)
block|{
return|return
name|producer
operator|.
name|createExchange
argument_list|(
name|pattern
argument_list|)
return|;
block|}
DECL|method|createExchange (Exchange exchange)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|producer
operator|.
name|createExchange
argument_list|(
name|exchange
argument_list|)
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
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|processor
argument_list|)
expr_stmt|;
block|}
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|processor
argument_list|)
expr_stmt|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
name|producer
operator|.
name|isSingleton
argument_list|()
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
literal|"UnitOfWork("
operator|+
name|producer
operator|+
literal|")"
return|;
block|}
block|}
end_class

end_unit

