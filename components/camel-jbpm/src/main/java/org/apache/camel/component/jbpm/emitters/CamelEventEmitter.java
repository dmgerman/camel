begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jbpm.emitters
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jbpm
operator|.
name|emitters
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|jbpm
operator|.
name|JBPMConsumer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jbpm
operator|.
name|persistence
operator|.
name|api
operator|.
name|integration
operator|.
name|EventCollection
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jbpm
operator|.
name|persistence
operator|.
name|api
operator|.
name|integration
operator|.
name|EventEmitter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jbpm
operator|.
name|persistence
operator|.
name|api
operator|.
name|integration
operator|.
name|InstanceView
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jbpm
operator|.
name|persistence
operator|.
name|api
operator|.
name|integration
operator|.
name|base
operator|.
name|BaseEventCollection
import|;
end_import

begin_class
DECL|class|CamelEventEmitter
specifier|public
class|class
name|CamelEventEmitter
implements|implements
name|EventEmitter
block|{
DECL|field|consumer
specifier|private
name|JBPMConsumer
name|consumer
decl_stmt|;
DECL|field|sendItems
specifier|private
name|boolean
name|sendItems
decl_stmt|;
DECL|method|CamelEventEmitter (JBPMConsumer consumer, boolean sendItems)
specifier|public
name|CamelEventEmitter
parameter_list|(
name|JBPMConsumer
name|consumer
parameter_list|,
name|boolean
name|sendItems
parameter_list|)
block|{
name|this
operator|.
name|consumer
operator|=
name|consumer
expr_stmt|;
name|this
operator|.
name|sendItems
operator|=
name|sendItems
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|deliver (Collection<InstanceView<?>> data)
specifier|public
name|void
name|deliver
parameter_list|(
name|Collection
argument_list|<
name|InstanceView
argument_list|<
name|?
argument_list|>
argument_list|>
name|data
parameter_list|)
block|{
comment|// no-op
block|}
annotation|@
name|Override
DECL|method|apply (Collection<InstanceView<?>> data)
specifier|public
name|void
name|apply
parameter_list|(
name|Collection
argument_list|<
name|InstanceView
argument_list|<
name|?
argument_list|>
argument_list|>
name|data
parameter_list|)
block|{
if|if
condition|(
name|consumer
operator|==
literal|null
operator|||
name|data
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|sendItems
condition|)
block|{
name|data
operator|.
name|forEach
argument_list|(
name|item
lambda|->
name|consumer
operator|.
name|sendMessage
argument_list|(
literal|"Emitter"
argument_list|,
name|item
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|consumer
operator|.
name|sendMessage
argument_list|(
literal|"Emitter"
argument_list|,
name|data
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|drop (Collection<InstanceView<?>> data)
specifier|public
name|void
name|drop
parameter_list|(
name|Collection
argument_list|<
name|InstanceView
argument_list|<
name|?
argument_list|>
argument_list|>
name|data
parameter_list|)
block|{
comment|// no-op
block|}
annotation|@
name|Override
DECL|method|newCollection ()
specifier|public
name|EventCollection
name|newCollection
parameter_list|()
block|{
return|return
operator|new
name|BaseEventCollection
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{      }
block|}
end_class

end_unit

