begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.reactive.streams.api
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|reactive
operator|.
name|streams
operator|.
name|api
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
name|CamelContextAware
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
name|component
operator|.
name|reactive
operator|.
name|streams
operator|.
name|ReactiveStreamsConsumer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|reactivestreams
operator|.
name|Publisher
import|;
end_import

begin_import
import|import
name|org
operator|.
name|reactivestreams
operator|.
name|Subscriber
import|;
end_import

begin_comment
comment|/**  * The interface to which any implementation of the reactive-streams engine should comply.  */
end_comment

begin_interface
DECL|interface|CamelReactiveStreamsService
specifier|public
interface|interface
name|CamelReactiveStreamsService
extends|extends
name|CamelContextAware
extends|,
name|Service
block|{
comment|/*      * Main API methods.      */
comment|/**      * Returns the publisher associated to the given stream name.      * A publisher can be used to push Camel exchanges to reactive-streams subscribers.      *      * @param name the stream name      * @return the stream publisher      */
DECL|method|getPublisher (String name)
name|Publisher
argument_list|<
name|Exchange
argument_list|>
name|getPublisher
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Returns the publisher associated to the given stream name.      * A publisher can be used to push Camel exchange to external reactive-streams subscribers.      *      * The publisher converts automatically exchanges to the given type.      *      * @param name the stream name      * @param type the type of the emitted items      * @param<T> the type of items emitted by the publisher      * @return the publisher associated to the stream      */
DECL|method|getPublisher (String name, Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|Publisher
argument_list|<
name|T
argument_list|>
name|getPublisher
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**      * Returns the subscriber associated to the given stream name.      * A subscriber can be used to push items coming from external reactive-streams publishers to Camel routes.      *      * @param name the stream name      * @return the subscriber associated with the stream      */
DECL|method|getSubscriber (String name)
name|Subscriber
argument_list|<
name|Exchange
argument_list|>
name|getSubscriber
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Returns the subscriber associated to the given stream name.      * A subscriber can be used to push items coming from external reactive-streams publishers to Camel routes.      *      * The subscriber converts automatically items of the given type to exchanges before pushing them.      *      * @param name the stream name      * @param type the publisher converts automatically exchanges to the given type.      * @param<T> the type of items accepted by the subscriber      * @return the subscriber associated with the stream      */
DECL|method|getSubscriber (String name, Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|Subscriber
argument_list|<
name|T
argument_list|>
name|getSubscriber
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/*      * Methods for producers.      */
comment|/**      * Used by Camel to send the exchange to all active subscriptions on the given stream.      * The callback is used to signal that the exchange has been delivered to the subscribers.      *      * @param name the stream name      * @param exchange the exchange to be forwarded to the external subscribers      * @param callback the callback that signals the delivery of the exchange      */
DECL|method|process (String name, Exchange exchange, DispatchCallback<Exchange> callback)
name|void
name|process
parameter_list|(
name|String
name|name
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|DispatchCallback
argument_list|<
name|Exchange
argument_list|>
name|callback
parameter_list|)
function_decl|;
comment|/*      * Methods for consumers.      */
comment|/**      * Used by Camel to associate the subscriber of the stream with the given name to a specific Camel consumer.      * This method is used to bind a Camel route to a reactive stream.      *      * @param name the stream name      * @param consumer the consumer of the route      * @throws IllegalStateException if another consumer is already associated with the given stream name      */
DECL|method|attachConsumer (String name, ReactiveStreamsConsumer consumer)
name|void
name|attachConsumer
parameter_list|(
name|String
name|name
parameter_list|,
name|ReactiveStreamsConsumer
name|consumer
parameter_list|)
function_decl|;
comment|/**      * Used by Camel to detach the existing consumer from the given stream.      *      * @param name the stream name      */
DECL|method|detachConsumer (String name)
name|void
name|detachConsumer
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

