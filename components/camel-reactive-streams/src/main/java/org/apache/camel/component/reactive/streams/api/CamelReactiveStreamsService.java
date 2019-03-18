begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Function
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
name|ReactiveStreamsCamelSubscriber
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
name|ReactiveStreamsProducer
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
name|spi
operator|.
name|HasId
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
name|Service
extends|,
name|HasId
block|{
comment|/*      * Main API methods.      */
comment|/**      * Returns the publisher associated to the given stream name.      * A publisher can be used to push Camel exchanges to reactive-streams subscribers.      *      * @param name the stream name      * @return the stream publisher      */
DECL|method|fromStream (String name)
name|Publisher
argument_list|<
name|Exchange
argument_list|>
name|fromStream
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Returns the publisher associated to the given stream name.      * A publisher can be used to push Camel exchange to external reactive-streams subscribers.      *      * The publisher converts automatically exchanges to the given type.      *      * @param name the stream name      * @param type the type of the emitted items      * @param<T> the type of items emitted by the publisher      * @return the publisher associated to the stream      */
DECL|method|fromStream (String name, Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|Publisher
argument_list|<
name|T
argument_list|>
name|fromStream
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
DECL|method|streamSubscriber (String name)
name|Subscriber
argument_list|<
name|Exchange
argument_list|>
name|streamSubscriber
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Returns the subscriber associated to the given stream name.      * A subscriber can be used to push items coming from external reactive-streams publishers to Camel routes.      *      * The subscriber converts automatically items of the given type to exchanges before pushing them.      *      * @param name the stream name      * @param type the publisher converts automatically exchanges to the given type.      * @param<T> the type of items accepted by the subscriber      * @return the subscriber associated with the stream      */
DECL|method|streamSubscriber (String name, Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|Subscriber
argument_list|<
name|T
argument_list|>
name|streamSubscriber
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
comment|/**      * Pushes the given data into the specified Camel stream and returns a Publisher (mono) holding      * the resulting exchange or an error.      *      * @param name the stream name      * @param data the data to push      * @return a publisher with the resulting exchange      */
DECL|method|toStream (String name, Object data)
name|Publisher
argument_list|<
name|Exchange
argument_list|>
name|toStream
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|data
parameter_list|)
function_decl|;
comment|/**      * Returns a function that pushes data into the specified Camel stream and      * returns a Publisher (mono) holding the resulting exchange or an error.      *      * This is a curryied version of {@link CamelReactiveStreamsService#toStream(String, Object)}.      *      * @param name the stream name      * @return a function that returns a publisher with the resulting exchange      */
DECL|method|toStream (String name)
name|Function
argument_list|<
name|?
argument_list|,
name|?
extends|extends
name|Publisher
argument_list|<
name|Exchange
argument_list|>
argument_list|>
name|toStream
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Pushes the given data into the specified Camel stream and returns a Publisher (mono) holding      * the exchange output or an error.      *      * @param name the stream name      * @param data the data to push      * @param type  the type to which the output should be converted      * @param<T> the generic type of the resulting Publisher      * @return a publisher with the resulting data      */
DECL|method|toStream (String name, Object data, Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|Publisher
argument_list|<
name|T
argument_list|>
name|toStream
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|data
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**      * Returns a function that pushes data into the specified Camel stream and      * returns a Publisher (mono) holding the exchange output or an error.      *      * This is a curryied version of {@link CamelReactiveStreamsService#toStream(String, Object, Class)}.      *      * @param name the stream name      * @param type  the type to which the output should be converted      * @param<T> the generic type of the resulting Publisher      * @return a function that returns a publisher with the resulting data      */
DECL|method|toStream (String name, Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|Function
argument_list|<
name|Object
argument_list|,
name|Publisher
argument_list|<
name|T
argument_list|>
argument_list|>
name|toStream
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
comment|/*      * Direct client API methods      */
comment|/**      * Creates a new stream from the endpoint URI (used as Camel Consumer) and returns      * the associated {@code Publisher}.      *      * If a stream has already been created, the existing {@link Publisher} is returned.      *      * @param uri the consumer uri      * @return the publisher associated to the uri      */
DECL|method|from (String uri)
name|Publisher
argument_list|<
name|Exchange
argument_list|>
name|from
parameter_list|(
name|String
name|uri
parameter_list|)
function_decl|;
comment|/**      * Creates a new stream of the given type from the endpoint URI (used as Camel Consumer) and returns      * the associated {@code Publisher}.      *      * If a stream has already been created, the existing {@link Publisher} is returned.      *      * @param uri the consumer uri      * @param type the type of items emitted by the publisher      * @param<T> the type to which Camel should convert exchanges to      * @return the publisher associated to the uri      */
DECL|method|from (String uri, Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|Publisher
argument_list|<
name|T
argument_list|>
name|from
parameter_list|(
name|String
name|uri
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**      * Creates a new route that pushes data to the endpoint URI and returns      * the associated {@code Subscriber}.      *      * This method always create a new stream.      *      * @param uri the target uri      * @return the subscriber associated to the uri      */
DECL|method|subscriber (String uri)
name|Subscriber
argument_list|<
name|Exchange
argument_list|>
name|subscriber
parameter_list|(
name|String
name|uri
parameter_list|)
function_decl|;
comment|/**      * Creates a new route that pushes data to the endpoint URI and returns      * the associated {@code Subscriber}.      *      * This method always create a new stream.      *      * @param uri the target uri      * @param type the type of items that the subscriber can receive      * @param<T> the type from which Camel should convert data to exchanges      * @return the subscriber associated to the uri      */
DECL|method|subscriber (String uri, Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|Subscriber
argument_list|<
name|T
argument_list|>
name|subscriber
parameter_list|(
name|String
name|uri
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**      * Creates a new route that uses the endpoint URI as producer, pushes the given data to the route      * and returns a {@code Publisher} that will eventually return the resulting exchange or an error.      *      * @param uri the producer uri      * @param data the data to push      * @return a publisher with the resulting exchange      */
DECL|method|to (String uri, Object data)
name|Publisher
argument_list|<
name|Exchange
argument_list|>
name|to
parameter_list|(
name|String
name|uri
parameter_list|,
name|Object
name|data
parameter_list|)
function_decl|;
comment|/**      * Creates a new route that uses the endpoint URI as producer, and returns a      * function that pushes the data into the route and returns the      * {@code Publisher} that holds the resulting exchange or the error.      *      *      * This is a curryied version of {@link CamelReactiveStreamsService#to(String, Object)}.      *      * @param uri the producer uri      * @return a function that returns a publisher with the resulting exchange      */
DECL|method|to (String uri)
name|Function
argument_list|<
name|Object
argument_list|,
name|Publisher
argument_list|<
name|Exchange
argument_list|>
argument_list|>
name|to
parameter_list|(
name|String
name|uri
parameter_list|)
function_decl|;
comment|/**      * Creates a new route that uses the endpoint URI as producer, pushes the given data to the route      * and returns a {@code Publisher} that will eventually return the exchange output or an error.      *      * @param uri the producer uri      * @param data the data to push      * @param type  the type to which the output should be converted      * @param<T> the generic type of the resulting Publisher      * @return a publisher with the resulting data      */
DECL|method|to (String uri, Object data, Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|Publisher
argument_list|<
name|T
argument_list|>
name|to
parameter_list|(
name|String
name|uri
parameter_list|,
name|Object
name|data
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**      * Creates a new route that uses the endpoint URI as producer, and returns a      * function that pushes the data into the route and returns the      * {@code Publisher} that holds the exchange output or an error.      *      * This is a curryied version of {@link CamelReactiveStreamsService#to(String, Object, Class)}.      *      * @param uri the producer uri      * @param type  the type to which the output should be converted      * @param<T> the generic type of the resulting Publisher      * @return a function that returns a publisher with the resulting data      */
DECL|method|to (String uri, Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|Function
argument_list|<
name|Object
argument_list|,
name|Publisher
argument_list|<
name|T
argument_list|>
argument_list|>
name|to
parameter_list|(
name|String
name|uri
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**      * Adds a processing step at the specified endpoint uri (usually a "direct:name") that delegates      * to the given reactive processor.      *      * The processor receives a {@link Publisher} of exchanges and returns an object.      * If the output of the processor is a {@link Publisher}, it will be unwrapped before      * delivering the result to the source route.      *      * @param uri the uri where the processor should be attached      * @param processor the reactive processor      */
DECL|method|process (String uri, Function<? super Publisher<Exchange>, ?> processor)
name|void
name|process
parameter_list|(
name|String
name|uri
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|Publisher
argument_list|<
name|Exchange
argument_list|>
argument_list|,
name|?
argument_list|>
name|processor
parameter_list|)
function_decl|;
comment|/**      * Adds a processing step at the specified endpoint uri (usually a "direct:name") that delegates      * to the given reactive processor.      *      * The processor receives a {@link Publisher} of items of the given type and returns an object.      * If the output of the processor is a {@link Publisher}, it will be unwrapped before      * delivering the result to the source route.      *      * @param uri the uri where the processor should be attached      * @param type  the type to which the body of the exchange should be converted      * @param<T> the generic type of the Publisher that should be processed      * @param processor the reactive processor      */
DECL|method|process (String uri, Class<T> type, Function<? super Publisher<T>, ?> processor)
parameter_list|<
name|T
parameter_list|>
name|void
name|process
parameter_list|(
name|String
name|uri
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|Publisher
argument_list|<
name|T
argument_list|>
argument_list|,
name|?
argument_list|>
name|processor
parameter_list|)
function_decl|;
comment|/*      * Methods for Camel producers.      */
comment|/**      * Used by Camel to associate the publisher of the stream with the given name to a specific Camel producer.      * This method is used to bind a Camel route to a reactive stream.      *      * @param name the stream name      * @param producer the producer of the route      * @throws IllegalStateException if another producer is already associated with the given stream name      */
DECL|method|attachCamelProducer (String name, ReactiveStreamsProducer producer)
name|void
name|attachCamelProducer
parameter_list|(
name|String
name|name
parameter_list|,
name|ReactiveStreamsProducer
name|producer
parameter_list|)
function_decl|;
comment|/**      * Used by Camel to detach the existing producer from the given stream.      *      * @param name the stream name      */
DECL|method|detachCamelProducer (String name)
name|void
name|detachCamelProducer
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Used by Camel to send the exchange to all active subscriptions on the given stream.      * The callback is used to signal that the exchange has been delivered to the subscribers.      *      * @param name the stream name      * @param exchange the exchange to be forwarded to the external subscribers      */
DECL|method|sendCamelExchange (String name, Exchange exchange)
name|void
name|sendCamelExchange
parameter_list|(
name|String
name|name
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
function_decl|;
comment|/*      * Methods for Camel consumers.      */
comment|/**      * Used by Camel to associate the subscriber of the stream with the given name to a specific Camel consumer.      * This method is used to bind a Camel route to a reactive stream.      *      * @param name the stream name      * @param consumer the consumer of the route      * @return the associated subscriber      * @throws IllegalStateException if another consumer is already associated with the given stream name      */
DECL|method|attachCamelConsumer (String name, ReactiveStreamsConsumer consumer)
name|ReactiveStreamsCamelSubscriber
name|attachCamelConsumer
parameter_list|(
name|String
name|name
parameter_list|,
name|ReactiveStreamsConsumer
name|consumer
parameter_list|)
function_decl|;
comment|/**      * Used by Camel to detach the existing consumer from the given stream.      *      * @param name the stream name      */
DECL|method|detachCamelConsumer (String name)
name|void
name|detachCamelConsumer
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

