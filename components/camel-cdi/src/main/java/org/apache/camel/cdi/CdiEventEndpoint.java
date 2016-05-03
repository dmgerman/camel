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
name|lang
operator|.
name|annotation
operator|.
name|Annotation
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Field
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|GenericArrayType
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|ParameterizedType
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Stream
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
operator|.
name|joining
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|context
operator|.
name|spi
operator|.
name|CreationalContext
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
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|Any
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|BeanManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|InjectionTarget
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|util
operator|.
name|TypeLiteral
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
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
comment|/**  * A Camel {@link Endpoint} that bridges the CDI events facility with Camel routes so that CDI events  * can be seamlessly observed / consumed (respectively produced / fired) from Camel consumers (respectively by Camel producers).<p>  *  * The {@code CdiEventEndpoint<T>} bean can be used to observe / consume CDI events whose event type is {@code T}, for example:  *<pre><code>  * {@literal @}Inject  *  CdiEventEndpoint{@literal<}String{@literal>} cdiEventEndpoint;  *  *  from(cdiEventEndpoint).log("CDI event received: ${body}");  *</code></pre>  *  * Conversely, the {@code CdiEventEndpoint<T>} bean can be used to produce / fire CDI events whose event type is {@code T}, for example:  *<pre><code>  * {@literal @}Inject  *  CdiEventEndpoint{@literal<}String{@literal>} cdiEventEndpoint;  *  *  from("direct:event").to(cdiEventEndpoint).log("CDI event sent: ${body}");  *</code></pre>  *  * The type variable {@code T}, respectively the qualifiers, of a particular {@code CdiEventEndpoint<T>} injection point  * are automatically translated into the parameterized<i>event type</i>, respectively into the<i>event qualifiers</i>, e.g.:  *<pre><code>  * {@literal @}Inject  * {@literal @}FooQualifier  *  CdiEventEndpoint{@literal<}List{@literal<}String{@literal>}{@literal>} cdiEventEndpoint;  *  *  from("direct:event").to(cdiEventEndpoint);  *  *  void observeCdiEvents({@literal @}Observes {@literal @}FooQualifier List{@literal<}String{@literal>} event) {  *      logger.info("CDI event: {}", event);  *  }  *</code></pre>  *  * When multiple Camel contexts exist in the CDI container, the {@code @ContextName} qualifier can be used  * to qualify the {@code CdiEventEndpoint<T>} injection points, e.g.:  *<pre><code>  * {@literal @}Inject  * {@literal @}ContextName("foo")  *  CdiEventEndpoint{@literal<}List{@literal<}String{@literal>}{@literal>} cdiEventEndpoint;  *  *  // Only observe / consume events having the {@literal @}ContextName("foo") qualifier  *  from(cdiEventEndpoint).log("Camel context 'foo'{@literal>} CDI event received: ${body}");  *  *  // Produce / fire events with the {@literal @}ContextName("foo") qualifier  *  from("...").to(cdiEventEndpoint);  *  *  void observeCdiEvents({@literal @}Observes {@literal @}ContextName("foo") List{@literal<}String{@literal>} event) {  *      logger.info("Camel context 'foo'{@literal>} CDI event: {}", event);  *  }  *</code></pre>  */
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
DECL|field|type
specifier|private
specifier|final
name|Type
name|type
decl_stmt|;
DECL|field|qualifiers
specifier|private
specifier|final
name|Set
argument_list|<
name|Annotation
argument_list|>
name|qualifiers
decl_stmt|;
DECL|field|manager
specifier|private
specifier|final
name|BeanManager
name|manager
decl_stmt|;
DECL|method|CdiEventEndpoint (String endpointUri, Type type, Set<Annotation> qualifiers, BeanManager manager)
name|CdiEventEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Type
name|type
parameter_list|,
name|Set
argument_list|<
name|Annotation
argument_list|>
name|qualifiers
parameter_list|,
name|BeanManager
name|manager
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|)
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|qualifiers
operator|=
name|qualifiers
expr_stmt|;
name|this
operator|.
name|manager
operator|=
name|manager
expr_stmt|;
block|}
DECL|method|eventEndpointUri (Type type, Set<Annotation> qualifiers)
specifier|static
name|String
name|eventEndpointUri
parameter_list|(
name|Type
name|type
parameter_list|,
name|Set
argument_list|<
name|Annotation
argument_list|>
name|qualifiers
parameter_list|)
block|{
return|return
literal|"cdi-event://"
operator|+
name|authorityFromType
argument_list|(
name|type
argument_list|)
operator|+
name|qualifiers
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|CdiSpiHelper
operator|::
name|createAnnotationId
argument_list|)
operator|.
name|collect
argument_list|(
name|joining
argument_list|(
literal|"%2C"
argument_list|,
name|qualifiers
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|?
literal|"?qualifiers="
else|:
literal|""
argument_list|,
literal|""
argument_list|)
argument_list|)
return|;
block|}
DECL|method|authorityFromType (Type type)
specifier|private
specifier|static
name|String
name|authorityFromType
parameter_list|(
name|Type
name|type
parameter_list|)
block|{
if|if
condition|(
name|type
operator|instanceof
name|Class
condition|)
block|{
return|return
name|Class
operator|.
name|class
operator|.
name|cast
argument_list|(
name|type
argument_list|)
operator|.
name|getName
argument_list|()
return|;
block|}
if|if
condition|(
name|type
operator|instanceof
name|ParameterizedType
condition|)
block|{
return|return
name|Stream
operator|.
name|of
argument_list|(
operator|(
operator|(
name|ParameterizedType
operator|)
name|type
operator|)
operator|.
name|getActualTypeArguments
argument_list|()
argument_list|)
operator|.
name|map
argument_list|(
name|CdiEventEndpoint
operator|::
name|authorityFromType
argument_list|)
operator|.
name|collect
argument_list|(
name|joining
argument_list|(
literal|"%2C"
argument_list|,
name|authorityFromType
argument_list|(
operator|(
operator|(
name|ParameterizedType
operator|)
name|type
operator|)
operator|.
name|getRawType
argument_list|()
argument_list|)
operator|+
literal|"%3C"
argument_list|,
literal|"%3E"
argument_list|)
argument_list|)
return|;
block|}
if|if
condition|(
name|type
operator|instanceof
name|GenericArrayType
condition|)
block|{
return|return
name|authorityFromType
argument_list|(
operator|(
operator|(
name|GenericArrayType
operator|)
name|type
operator|)
operator|.
name|getGenericComponentType
argument_list|()
argument_list|)
operator|+
literal|"%5B%5D"
return|;
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot create URI authority for event type ["
operator|+
name|type
operator|+
literal|"]"
argument_list|)
throw|;
block|}
DECL|method|getQualifiers ()
name|Set
argument_list|<
name|Annotation
argument_list|>
name|getQualifiers
parameter_list|()
block|{
return|return
name|qualifiers
return|;
block|}
DECL|method|getType ()
name|Type
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
annotation|@
name|Override
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
throws|throws
name|IllegalAccessException
block|{
comment|// FIXME: to be replaced once event firing with dynamic parameterized type
comment|// is properly supported (see https://issues.jboss.org/browse/CDI-516)
name|TypeLiteral
argument_list|<
name|T
argument_list|>
name|literal
init|=
operator|new
name|TypeLiteral
argument_list|<
name|T
argument_list|>
argument_list|()
block|{         }
decl_stmt|;
for|for
control|(
name|Field
name|field
range|:
name|TypeLiteral
operator|.
name|class
operator|.
name|getDeclaredFields
argument_list|()
control|)
block|{
if|if
condition|(
name|field
operator|.
name|getType
argument_list|()
operator|.
name|equals
argument_list|(
name|Type
operator|.
name|class
argument_list|)
condition|)
block|{
name|field
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|field
operator|.
name|set
argument_list|(
name|literal
argument_list|,
name|type
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
name|InjectionTarget
argument_list|<
name|AnyEvent
argument_list|>
name|target
init|=
name|manager
operator|.
name|createInjectionTarget
argument_list|(
name|manager
operator|.
name|createAnnotatedType
argument_list|(
name|AnyEvent
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|CreationalContext
argument_list|<
name|AnyEvent
argument_list|>
name|ctx
init|=
name|manager
operator|.
name|createCreationalContext
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|AnyEvent
name|instance
init|=
name|target
operator|.
name|produce
argument_list|(
name|ctx
argument_list|)
decl_stmt|;
name|target
operator|.
name|inject
argument_list|(
name|instance
argument_list|,
name|ctx
argument_list|)
expr_stmt|;
return|return
operator|new
name|CdiEventProducer
argument_list|<>
argument_list|(
name|this
argument_list|,
name|instance
operator|.
name|event
operator|.
name|select
argument_list|(
name|literal
argument_list|,
name|qualifiers
operator|.
name|stream
argument_list|()
operator|.
name|toArray
argument_list|(
name|Annotation
index|[]
operator|::
operator|new
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Vetoed
DECL|class|AnyEvent
specifier|private
specifier|static
class|class
name|AnyEvent
block|{
annotation|@
name|Any
annotation|@
name|Inject
DECL|field|event
specifier|private
name|Event
argument_list|<
name|Object
argument_list|>
name|event
decl_stmt|;
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
DECL|method|addConsumer (CdiEventConsumer<T> consumer)
name|void
name|addConsumer
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
DECL|method|removeConsumer (CdiEventConsumer<T> consumer)
name|void
name|removeConsumer
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
name|consumers
operator|.
name|forEach
argument_list|(
name|consumer
lambda|->
name|consumer
operator|.
name|notify
argument_list|(
name|t
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

