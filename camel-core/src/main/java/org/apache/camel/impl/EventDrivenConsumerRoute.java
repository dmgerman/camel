begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Route
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
name|Service
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
name|Consumer
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
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * A {@link Route} which starts with an  *<a href="http://activemq.apache.org/camel/event-driven-consumer.html">Event Driven Consumer</a>  *  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|EventDrivenConsumerRoute
specifier|public
class|class
name|EventDrivenConsumerRoute
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
extends|extends
name|Route
argument_list|<
name|E
argument_list|>
block|{
DECL|field|processor
specifier|private
name|Processor
name|processor
decl_stmt|;
DECL|method|EventDrivenConsumerRoute (Endpoint endpoint, Processor processor)
specifier|public
name|EventDrivenConsumerRoute
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|processor
operator|=
name|processor
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
literal|"EventDrivenConsumerRoute["
operator|+
name|getEndpoint
argument_list|()
operator|+
literal|" -> "
operator|+
name|processor
operator|+
literal|"]"
return|;
block|}
DECL|method|getProcessor ()
specifier|public
name|Processor
name|getProcessor
parameter_list|()
block|{
return|return
name|processor
return|;
block|}
DECL|method|setProcessor (Processor processor)
specifier|public
name|void
name|setProcessor
parameter_list|(
name|Processor
name|processor
parameter_list|)
block|{
name|this
operator|.
name|processor
operator|=
name|processor
expr_stmt|;
block|}
comment|/**      * Factory method to lazily create the complete list of services required for this route      * such as adding the processor or consumer      */
DECL|method|addServices (List<Service> services)
specifier|protected
name|void
name|addServices
parameter_list|(
name|List
argument_list|<
name|Service
argument_list|>
name|services
parameter_list|)
throws|throws
name|Exception
block|{
name|Processor
name|processor
init|=
name|getProcessor
argument_list|()
decl_stmt|;
if|if
condition|(
name|processor
operator|instanceof
name|Service
condition|)
block|{
name|Service
name|service
init|=
operator|(
name|Service
operator|)
name|processor
decl_stmt|;
name|services
operator|.
name|add
argument_list|(
name|service
argument_list|)
expr_stmt|;
block|}
name|Endpoint
argument_list|<
name|E
argument_list|>
name|endpoint
init|=
name|getEndpoint
argument_list|()
decl_stmt|;
name|Consumer
argument_list|<
name|E
argument_list|>
name|consumer
init|=
name|endpoint
operator|.
name|createConsumer
argument_list|(
name|processor
argument_list|)
decl_stmt|;
if|if
condition|(
name|consumer
operator|!=
literal|null
condition|)
block|{
name|services
operator|.
name|add
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

