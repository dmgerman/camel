begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.reactive.streams
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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|BlockingQueue
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|LinkedBlockingDeque
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import
name|io
operator|.
name|reactivex
operator|.
name|Flowable
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
name|mock
operator|.
name|MockEndpoint
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
name|support
operator|.
name|ReactiveStreamsTestSupport
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
name|JndiRegistry
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
name|reactivestreams
operator|.
name|Publisher
import|;
end_import

begin_class
DECL|class|DirectClientAPITest
specifier|public
class|class
name|DirectClientAPITest
extends|extends
name|ReactiveStreamsTestSupport
block|{
annotation|@
name|Test
DECL|method|testFromDirect ()
specifier|public
name|void
name|testFromDirect
parameter_list|()
throws|throws
name|Exception
block|{
name|Publisher
argument_list|<
name|Integer
argument_list|>
name|data
init|=
name|camel
operator|.
name|publishURI
argument_list|(
literal|"direct:endpoint"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|BlockingQueue
argument_list|<
name|Integer
argument_list|>
name|queue
init|=
operator|new
name|LinkedBlockingDeque
argument_list|<>
argument_list|()
decl_stmt|;
name|Flowable
operator|.
name|fromPublisher
argument_list|(
name|data
argument_list|)
operator|.
name|map
argument_list|(
name|i
lambda|->
operator|-
name|i
argument_list|)
operator|.
name|doOnNext
argument_list|(
name|queue
operator|::
name|add
argument_list|)
operator|.
name|subscribe
argument_list|()
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:endpoint"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|Integer
name|res
init|=
name|queue
operator|.
name|poll
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|res
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|res
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFromDirectOnHotContext ()
specifier|public
name|void
name|testFromDirectOnHotContext
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|200
argument_list|)
expr_stmt|;
name|Publisher
argument_list|<
name|Integer
argument_list|>
name|data
init|=
name|camel
operator|.
name|publishURI
argument_list|(
literal|"direct:endpoint"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|BlockingQueue
argument_list|<
name|Integer
argument_list|>
name|queue
init|=
operator|new
name|LinkedBlockingDeque
argument_list|<>
argument_list|()
decl_stmt|;
name|Flowable
operator|.
name|fromPublisher
argument_list|(
name|data
argument_list|)
operator|.
name|map
argument_list|(
name|i
lambda|->
operator|-
name|i
argument_list|)
operator|.
name|doOnNext
argument_list|(
name|queue
operator|::
name|add
argument_list|)
operator|.
name|subscribe
argument_list|()
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:endpoint"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|Integer
name|res
init|=
name|queue
operator|.
name|poll
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|res
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|res
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDirectCall ()
specifier|public
name|void
name|testDirectCall
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|BlockingQueue
argument_list|<
name|String
argument_list|>
name|queue
init|=
operator|new
name|LinkedBlockingDeque
argument_list|<>
argument_list|()
decl_stmt|;
name|Flowable
operator|.
name|just
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|)
operator|.
name|flatMap
argument_list|(
name|camel
operator|.
name|requestURI
argument_list|(
literal|"bean:hello"
argument_list|,
name|String
operator|.
name|class
argument_list|)
operator|::
name|apply
argument_list|)
operator|.
name|doOnNext
argument_list|(
name|queue
operator|::
name|add
argument_list|)
operator|.
name|subscribe
argument_list|()
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
literal|3
condition|;
name|i
operator|++
control|)
block|{
name|String
name|res
init|=
name|queue
operator|.
name|poll
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello "
operator|+
name|i
argument_list|,
name|res
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testDirectCallOverload ()
specifier|public
name|void
name|testDirectCallOverload
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|BlockingQueue
argument_list|<
name|String
argument_list|>
name|queue
init|=
operator|new
name|LinkedBlockingDeque
argument_list|<>
argument_list|()
decl_stmt|;
name|Flowable
operator|.
name|just
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|)
operator|.
name|flatMap
argument_list|(
name|e
lambda|->
name|camel
operator|.
name|requestURI
argument_list|(
literal|"bean:hello"
argument_list|,
name|e
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|doOnNext
argument_list|(
name|queue
operator|::
name|add
argument_list|)
operator|.
name|subscribe
argument_list|()
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
literal|3
condition|;
name|i
operator|++
control|)
block|{
name|String
name|res
init|=
name|queue
operator|.
name|poll
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello "
operator|+
name|i
argument_list|,
name|res
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testDirectCallWithExchange ()
specifier|public
name|void
name|testDirectCallWithExchange
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|BlockingQueue
argument_list|<
name|String
argument_list|>
name|queue
init|=
operator|new
name|LinkedBlockingDeque
argument_list|<>
argument_list|()
decl_stmt|;
name|Flowable
operator|.
name|just
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|)
operator|.
name|flatMap
argument_list|(
name|camel
operator|.
name|requestURI
argument_list|(
literal|"bean:hello"
argument_list|)
operator|::
name|apply
argument_list|)
operator|.
name|map
argument_list|(
name|ex
lambda|->
name|ex
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|doOnNext
argument_list|(
name|queue
operator|::
name|add
argument_list|)
operator|.
name|subscribe
argument_list|()
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
literal|3
condition|;
name|i
operator|++
control|)
block|{
name|String
name|res
init|=
name|queue
operator|.
name|poll
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello "
operator|+
name|i
argument_list|,
name|res
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testDirectCallWithExchangeOverload ()
specifier|public
name|void
name|testDirectCallWithExchangeOverload
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|BlockingQueue
argument_list|<
name|String
argument_list|>
name|queue
init|=
operator|new
name|LinkedBlockingDeque
argument_list|<>
argument_list|()
decl_stmt|;
name|Flowable
operator|.
name|just
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|)
operator|.
name|flatMap
argument_list|(
name|e
lambda|->
name|camel
operator|.
name|requestURI
argument_list|(
literal|"bean:hello"
argument_list|,
name|e
argument_list|)
argument_list|)
operator|.
name|map
argument_list|(
name|ex
lambda|->
name|ex
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|doOnNext
argument_list|(
name|queue
operator|::
name|add
argument_list|)
operator|.
name|subscribe
argument_list|()
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
literal|3
condition|;
name|i
operator|++
control|)
block|{
name|String
name|res
init|=
name|queue
operator|.
name|poll
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello "
operator|+
name|i
argument_list|,
name|res
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testProxiedDirectCall ()
specifier|public
name|void
name|testProxiedDirectCall
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:proxy"
argument_list|)
operator|.
name|to
argument_list|(
literal|"bean:hello"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|simple
argument_list|(
literal|"proxy to ${body}"
argument_list|)
expr_stmt|;
block|}
block|}
operator|.
name|addRoutesToCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|BlockingQueue
argument_list|<
name|String
argument_list|>
name|queue
init|=
operator|new
name|LinkedBlockingDeque
argument_list|<>
argument_list|()
decl_stmt|;
name|Flowable
operator|.
name|just
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|)
operator|.
name|flatMap
argument_list|(
name|camel
operator|.
name|requestURI
argument_list|(
literal|"direct:proxy"
argument_list|,
name|String
operator|.
name|class
argument_list|)
operator|::
name|apply
argument_list|)
operator|.
name|doOnNext
argument_list|(
name|queue
operator|::
name|add
argument_list|)
operator|.
name|subscribe
argument_list|()
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
literal|3
condition|;
name|i
operator|++
control|)
block|{
name|String
name|res
init|=
name|queue
operator|.
name|poll
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"proxy to Hello "
operator|+
name|i
argument_list|,
name|res
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testDirectCallFromCamel ()
specifier|public
name|void
name|testDirectCallFromCamel
parameter_list|()
throws|throws
name|Exception
block|{
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:source"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:stream"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|simple
argument_list|(
literal|"after stream: ${body}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:dest"
argument_list|)
expr_stmt|;
block|}
block|}
operator|.
name|addRoutesToCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|camel
operator|.
name|processFromURI
argument_list|(
literal|"direct:stream"
argument_list|,
name|p
lambda|->
name|Flowable
operator|.
name|fromPublisher
argument_list|(
name|p
argument_list|)
operator|.
name|map
argument_list|(
name|exchange
lambda|->
block|{
name|int
name|val
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Integer
operator|.
name|class
argument_list|)
argument_list|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
operator|-
name|val
argument_list|)
argument_list|;                             return
name|exchange
argument_list|;
block|}
block|)
end_class

begin_empty_stmt
unit|)
empty_stmt|;
end_empty_stmt

begin_for
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
literal|3
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:source"
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
end_for

begin_decl_stmt
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:dest"
argument_list|)
decl_stmt|;
end_decl_stmt

begin_expr_stmt
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|3
argument_list|)
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
end_expr_stmt

begin_decl_stmt
name|int
name|id
init|=
literal|1
decl_stmt|;
end_decl_stmt

begin_for
for|for
control|(
name|Exchange
name|ex
range|:
name|mock
operator|.
name|getExchanges
argument_list|()
control|)
block|{
name|String
name|content
init|=
name|ex
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"after stream: "
operator|+
operator|(
operator|-
name|id
operator|++
operator|)
argument_list|,
name|content
argument_list|)
expr_stmt|;
block|}
end_for

begin_function
unit|}       @
name|Test
DECL|method|testDirectCallFromCamelWithConversion ()
specifier|public
name|void
name|testDirectCallFromCamelWithConversion
parameter_list|()
throws|throws
name|Exception
block|{
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:source"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:stream"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|simple
argument_list|(
literal|"after stream: ${body}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:dest"
argument_list|)
expr_stmt|;
block|}
block|}
operator|.
name|addRoutesToCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|camel
operator|.
name|processFromURI
argument_list|(
literal|"direct:stream"
argument_list|,
name|Integer
operator|.
name|class
argument_list|,
name|p
lambda|->
name|Flowable
operator|.
name|fromPublisher
argument_list|(
name|p
argument_list|)
operator|.
name|map
argument_list|(
name|i
lambda|->
operator|-
name|i
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
literal|3
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:source"
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:dest"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|int
name|id
init|=
literal|1
decl_stmt|;
for|for
control|(
name|Exchange
name|ex
range|:
name|mock
operator|.
name|getExchanges
argument_list|()
control|)
block|{
name|String
name|content
init|=
name|ex
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"after stream: "
operator|+
operator|(
operator|-
name|id
operator|++
operator|)
argument_list|,
name|content
argument_list|)
expr_stmt|;
block|}
block|}
end_function

begin_function
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|registry
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"hello"
argument_list|,
operator|new
name|SampleBean
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|registry
return|;
block|}
end_function

begin_class
DECL|class|SampleBean
specifier|public
specifier|static
class|class
name|SampleBean
block|{
DECL|method|hello (String name)
specifier|public
name|String
name|hello
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
literal|"Hello "
operator|+
name|name
return|;
block|}
block|}
end_class

unit|}
end_unit

