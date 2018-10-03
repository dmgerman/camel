begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.guava.eventbus
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|guava
operator|.
name|eventbus
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|eventbus
operator|.
name|EventBus
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
name|support
operator|.
name|DefaultProducer
import|;
end_import

begin_comment
comment|/**  * Guava EventBus (http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/eventbus/EventBus.html)  * producer forwarding messages from Camel routes to the bus.  */
end_comment

begin_class
DECL|class|GuavaEventBusProducer
specifier|public
class|class
name|GuavaEventBusProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|eventBus
specifier|private
specifier|final
name|EventBus
name|eventBus
decl_stmt|;
DECL|method|GuavaEventBusProducer (Endpoint endpoint, EventBus eventBus)
specifier|public
name|GuavaEventBusProducer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|EventBus
name|eventBus
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|eventBus
operator|=
name|eventBus
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
name|Object
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|body
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Posting: {} to EventBus: {}"
argument_list|,
name|body
argument_list|,
name|eventBus
argument_list|)
expr_stmt|;
name|eventBus
operator|.
name|post
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Body is null, cannot post to EventBus"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

