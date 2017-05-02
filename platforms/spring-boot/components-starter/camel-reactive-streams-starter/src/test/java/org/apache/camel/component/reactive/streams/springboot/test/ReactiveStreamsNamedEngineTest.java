begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.reactive.streams.springboot.test
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
name|springboot
operator|.
name|test
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
name|builder
operator|.
name|RouteBuilder
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
name|component
operator|.
name|reactive
operator|.
name|streams
operator|.
name|api
operator|.
name|CamelReactiveStreams
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
name|api
operator|.
name|CamelReactiveStreamsService
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
name|engine
operator|.
name|CamelSubscriber
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
name|engine
operator|.
name|DefaultCamelReactiveStreamsService
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
name|springboot
operator|.
name|ReactiveStreamsComponentAutoConfiguration
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
name|springboot
operator|.
name|ReactiveStreamsServiceAutoConfiguration
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
name|spring
operator|.
name|boot
operator|.
name|CamelAutoConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Autowired
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|autoconfigure
operator|.
name|SpringBootApplication
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|test
operator|.
name|context
operator|.
name|SpringBootTest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Component
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|annotation
operator|.
name|DirtiesContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|SpringRunner
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|SpringRunner
operator|.
name|class
argument_list|)
annotation|@
name|SpringBootApplication
annotation|@
name|DirtiesContext
annotation|@
name|SpringBootTest
argument_list|(
name|classes
operator|=
block|{
name|ReactiveStreamsServiceAutoConfiguration
operator|.
name|class
block|,
name|ReactiveStreamsComponentAutoConfiguration
operator|.
name|class
block|,
name|CamelAutoConfiguration
operator|.
name|class
block|}
argument_list|,
name|properties
operator|=
block|{
literal|"camel.component.reactive-streams.service-type=my-engine"
block|}
argument_list|)
DECL|class|ReactiveStreamsNamedEngineTest
specifier|public
class|class
name|ReactiveStreamsNamedEngineTest
block|{
annotation|@
name|Autowired
DECL|field|context
specifier|private
name|CamelContext
name|context
decl_stmt|;
annotation|@
name|Autowired
DECL|field|reactiveStreamsService
specifier|private
name|CamelReactiveStreamsService
name|reactiveStreamsService
decl_stmt|;
annotation|@
name|Test
DECL|method|testAutoConfiguration ()
specifier|public
name|void
name|testAutoConfiguration
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|CamelReactiveStreamsService
name|service
init|=
name|CamelReactiveStreams
operator|.
name|get
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|service
operator|instanceof
name|MyEngine
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|service
argument_list|,
name|reactiveStreamsService
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Component
argument_list|(
literal|"my-engine"
argument_list|)
DECL|class|MyEngine
specifier|static
class|class
name|MyEngine
implements|implements
name|CamelReactiveStreamsService
block|{
annotation|@
name|Override
DECL|method|fromStream (String s)
specifier|public
name|Publisher
argument_list|<
name|Exchange
argument_list|>
name|fromStream
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|fromStream (String s, Class<T> aClass)
specifier|public
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
name|s
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|aClass
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|streamSubscriber (String s)
specifier|public
name|Subscriber
argument_list|<
name|Exchange
argument_list|>
name|streamSubscriber
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|streamSubscriber (String s, Class<T> aClass)
specifier|public
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
name|s
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|aClass
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|toStream (String s, Object o)
specifier|public
name|Publisher
argument_list|<
name|Exchange
argument_list|>
name|toStream
parameter_list|(
name|String
name|s
parameter_list|,
name|Object
name|o
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|toStream (String s)
specifier|public
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
name|s
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|toStream (String s, Object o, Class<T> aClass)
specifier|public
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
name|s
parameter_list|,
name|Object
name|o
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|aClass
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|toStream (String s, Class<T> aClass)
specifier|public
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
name|s
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|aClass
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|from (String s)
specifier|public
name|Publisher
argument_list|<
name|Exchange
argument_list|>
name|from
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|from (String s, Class<T> aClass)
specifier|public
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
name|s
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|aClass
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|subscriber (String s)
specifier|public
name|Subscriber
argument_list|<
name|Exchange
argument_list|>
name|subscriber
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|subscriber (String s, Class<T> aClass)
specifier|public
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
name|s
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|aClass
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|to (String s, Object o)
specifier|public
name|Publisher
argument_list|<
name|Exchange
argument_list|>
name|to
parameter_list|(
name|String
name|s
parameter_list|,
name|Object
name|o
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|to (String s)
specifier|public
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
name|s
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|to (String s, Object o, Class<T> aClass)
specifier|public
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
name|s
parameter_list|,
name|Object
name|o
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|aClass
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|to (String s, Class<T> aClass)
specifier|public
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
name|s
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|aClass
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|process (String s, Function<? super Publisher<Exchange>, ?> function)
specifier|public
name|void
name|process
parameter_list|(
name|String
name|s
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
name|function
parameter_list|)
block|{          }
annotation|@
name|Override
DECL|method|process (String s, Class<T> aClass, Function<? super Publisher<T>, ?> function)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|void
name|process
parameter_list|(
name|String
name|s
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|aClass
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
name|function
parameter_list|)
block|{          }
annotation|@
name|Override
DECL|method|attachCamelProducer (String s, ReactiveStreamsProducer producer)
specifier|public
name|void
name|attachCamelProducer
parameter_list|(
name|String
name|s
parameter_list|,
name|ReactiveStreamsProducer
name|producer
parameter_list|)
block|{          }
annotation|@
name|Override
DECL|method|detachCamelProducer (String s)
specifier|public
name|void
name|detachCamelProducer
parameter_list|(
name|String
name|s
parameter_list|)
block|{          }
annotation|@
name|Override
DECL|method|sendCamelExchange (String s, Exchange exchange)
specifier|public
name|void
name|sendCamelExchange
parameter_list|(
name|String
name|s
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{          }
annotation|@
name|Override
DECL|method|attachCamelConsumer (String s, ReactiveStreamsConsumer consumer)
specifier|public
name|CamelSubscriber
name|attachCamelConsumer
parameter_list|(
name|String
name|s
parameter_list|,
name|ReactiveStreamsConsumer
name|consumer
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|detachCamelConsumer (String s)
specifier|public
name|void
name|detachCamelConsumer
parameter_list|(
name|String
name|s
parameter_list|)
block|{          }
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{          }
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{          }
annotation|@
name|Override
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
literal|"my-engine"
return|;
block|}
block|}
block|}
end_class

end_unit

