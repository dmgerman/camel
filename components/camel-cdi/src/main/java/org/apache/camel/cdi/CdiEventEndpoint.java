begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.cdi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cdi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|event
operator|.
name|Event
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
name|CamelContext
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
name|impl
operator|.
name|DefaultEndpoint
import|;
end_import

begin_comment
comment|/**  * A Camel {@link Endpoint} that bridges the CDI events facility with Camel routes so that CDI events  * can be seamlessly observed / consumed (respectively produced / fired) from Camel consumers (respectively by Camel producers).  *<p/>  * The {@code CdiEventEndpoint<T>} bean can be used to observe / consume CDI events whose event type is {@code T}, for example:  *<pre><code>  * {@literal @}Inject  * CdiEventEndpoint{@literal<}String{@literal>} cdiEventEndpoint;  *  * from(cdiEventEndpoint).log("CDI event received: ${body}");  *</code></pre>  *  * Conversely, the {@code CdiEventEndpoint<T>} bean can be used to produce / fire CDI events whose event type is {@code T}, for example:  *<pre><code>  * {@literal @}Inject  * CdiEventEndpoint{@literal<}String{@literal>} cdiEventEndpoint;  *  * from("direct:event").to(cdiEventEndpoint).log("CDI event sent: ${body}");  *</code></pre>  *  * The type variable {@code T}, respectively the qualifiers, of a particular {@code CdiEventEndpoint<T>} injection point  * are automatically translated into the parameterized<i>event type</i>, respectively into the<i>event qualifiers</i>, e.g.:  *<pre><code>  * {@literal @}Inject  * {@literal @}FooQualifier  * CdiEventEndpoint{@literal<}List{@literal<}String{@literal>}{@literal>} cdiEventEndpoint;  *  * from("direct:event").to(cdiEventEndpoint);  *  * void observeCdiEvents({@literal @}Observes {@literal @}FooQualifier List{@literal<}String{@literal>} event) {  *     logger.info("CDI event: {}", event);  * }  *</code></pre>  *  * When multiple Camel contexts exist in the CDI container, the {@code @ContextName} qualifier can be used  * to qualify the {@code CdiEventEndpoint<T>} injection points, e.g.:  *<pre><code>  * {@literal @}Inject  * {@literal @}ContextName("foo")  * CdiEventEndpoint{@literal<}List{@literal<}String{@literal>}{@literal>} cdiEventEndpoint;  *  * // Only observe / consume events having the {@literal @}ContextName("foo") qualifier  * from(cdiEventEndpoint).log("Camel context 'foo'> CDI event received: ${body}");  *  * // Produce / fire events with the {@literal @}ContextName("foo") qualifier  * from("...").to(cdiEventEndpoint);  *  * void observeCdiEvents({@literal @}Observes {@literal @}ContextName("foo") List{@literal<}String{@literal>} event) {  *     logger.info("Camel context 'foo'> CDI event: {}", event);  * }  *</code></pre>  */
end_comment

begin_class
DECL|class|CdiEventEndpoint
specifier|public
specifier|final
class|class
name|CdiEventEndpoint
parameter_list|<
name|T
parameter_list|>
extends|extends
name|DefaultEndpoint
block|{
DECL|field|consumers
specifier|private
specifier|final
name|List
argument_list|<
name|CdiEventConsumer
argument_list|<
name|T
argument_list|>
argument_list|>
name|consumers
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|event
specifier|private
specifier|final
name|Event
argument_list|<
name|T
argument_list|>
name|event
decl_stmt|;
DECL|method|CdiEventEndpoint (Event<T> event, String endpointUri, CamelContext context, ForwardingObserverMethod<T> observer)
name|CdiEventEndpoint
parameter_list|(
name|Event
argument_list|<
name|T
argument_list|>
name|event
parameter_list|,
name|String
name|endpointUri
parameter_list|,
name|CamelContext
name|context
parameter_list|,
name|ForwardingObserverMethod
argument_list|<
name|T
argument_list|>
name|observer
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|this
operator|.
name|event
operator|=
name|event
expr_stmt|;
name|observer
operator|.
name|setObserver
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
block|{
return|return
operator|new
name|CdiEventConsumer
argument_list|<>
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
block|{
return|return
operator|new
name|CdiEventProducer
argument_list|<>
argument_list|(
name|this
argument_list|,
name|event
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|registerConsumer (CdiEventConsumer<T> consumer)
name|void
name|registerConsumer
parameter_list|(
name|CdiEventConsumer
argument_list|<
name|T
argument_list|>
name|consumer
parameter_list|)
block|{
synchronized|synchronized
init|(
name|consumers
init|)
block|{
name|consumers
operator|.
name|add
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|unregisterConsumer (CdiEventConsumer<T> consumer)
name|void
name|unregisterConsumer
parameter_list|(
name|CdiEventConsumer
argument_list|<
name|T
argument_list|>
name|consumer
parameter_list|)
block|{
synchronized|synchronized
init|(
name|consumers
init|)
block|{
name|consumers
operator|.
name|remove
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|notify (T t)
name|void
name|notify
parameter_list|(
name|T
name|t
parameter_list|)
block|{
synchronized|synchronized
init|(
name|consumers
init|)
block|{
for|for
control|(
name|CdiEventConsumer
argument_list|<
name|T
argument_list|>
name|consumer
range|:
name|consumers
control|)
block|{
name|consumer
operator|.
name|notify
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

