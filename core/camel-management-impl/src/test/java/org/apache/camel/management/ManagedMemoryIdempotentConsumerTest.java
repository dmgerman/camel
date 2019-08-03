begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
package|;
end_package

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
name|javax
operator|.
name|management
operator|.
name|MBeanServer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|ObjectName
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
name|Message
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
name|spi
operator|.
name|IdempotentRepository
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
name|processor
operator|.
name|idempotent
operator|.
name|MemoryIdempotentRepository
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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

begin_class
DECL|class|ManagedMemoryIdempotentConsumerTest
specifier|public
class|class
name|ManagedMemoryIdempotentConsumerTest
extends|extends
name|ManagementTestSupport
block|{
DECL|field|startEndpoint
specifier|protected
name|Endpoint
name|startEndpoint
decl_stmt|;
DECL|field|resultEndpoint
specifier|protected
name|MockEndpoint
name|resultEndpoint
decl_stmt|;
DECL|field|repo
specifier|private
name|IdempotentRepository
name|repo
decl_stmt|;
annotation|@
name|Test
DECL|method|testDuplicateMessagesAreFilteredOut ()
specifier|public
name|void
name|testDuplicateMessagesAreFilteredOut
parameter_list|()
throws|throws
name|Exception
block|{
comment|// JMX tests dont work well on AIX CI servers (hangs them)
if|if
condition|(
name|isPlatform
argument_list|(
literal|"aix"
argument_list|)
condition|)
block|{
return|return;
block|}
name|MBeanServer
name|mbeanServer
init|=
name|getMBeanServer
argument_list|()
decl_stmt|;
comment|// services
name|Set
argument_list|<
name|ObjectName
argument_list|>
name|names
init|=
name|mbeanServer
operator|.
name|queryNames
argument_list|(
operator|new
name|ObjectName
argument_list|(
literal|"org.apache.camel"
operator|+
literal|":type=services,*"
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|ObjectName
name|on
init|=
literal|null
decl_stmt|;
for|for
control|(
name|ObjectName
name|name
range|:
name|names
control|)
block|{
if|if
condition|(
name|name
operator|.
name|toString
argument_list|()
operator|.
name|contains
argument_list|(
literal|"MemoryIdempotentRepository"
argument_list|)
condition|)
block|{
name|on
operator|=
name|name
expr_stmt|;
break|break;
block|}
block|}
name|assertTrue
argument_list|(
literal|"Should be registered"
argument_list|,
name|mbeanServer
operator|.
name|isRegistered
argument_list|(
name|on
argument_list|)
argument_list|)
expr_stmt|;
name|Integer
name|size
init|=
operator|(
name|Integer
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"CacheSize"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|size
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|repo
operator|.
name|contains
argument_list|(
literal|"1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|repo
operator|.
name|contains
argument_list|(
literal|"2"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|repo
operator|.
name|contains
argument_list|(
literal|"3"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|repo
operator|.
name|contains
argument_list|(
literal|"4"
argument_list|)
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"one"
argument_list|,
literal|"two"
argument_list|,
literal|"three"
argument_list|)
expr_stmt|;
name|sendMessage
argument_list|(
literal|"1"
argument_list|,
literal|"one"
argument_list|)
expr_stmt|;
name|sendMessage
argument_list|(
literal|"2"
argument_list|,
literal|"two"
argument_list|)
expr_stmt|;
name|sendMessage
argument_list|(
literal|"1"
argument_list|,
literal|"one"
argument_list|)
expr_stmt|;
name|sendMessage
argument_list|(
literal|"2"
argument_list|,
literal|"two"
argument_list|)
expr_stmt|;
name|sendMessage
argument_list|(
literal|"4"
argument_list|,
literal|"four"
argument_list|)
expr_stmt|;
name|sendMessage
argument_list|(
literal|"1"
argument_list|,
literal|"one"
argument_list|)
expr_stmt|;
name|sendMessage
argument_list|(
literal|"3"
argument_list|,
literal|"three"
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|repo
operator|.
name|contains
argument_list|(
literal|"1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|repo
operator|.
name|contains
argument_list|(
literal|"2"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|repo
operator|.
name|contains
argument_list|(
literal|"3"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|repo
operator|.
name|contains
argument_list|(
literal|"4"
argument_list|)
argument_list|)
expr_stmt|;
name|size
operator|=
operator|(
name|Integer
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"CacheSize"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|size
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
comment|// remove one from repo
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"remove"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"1"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"java.lang.String"
block|}
argument_list|)
expr_stmt|;
comment|// there should be 3 now
name|size
operator|=
operator|(
name|Integer
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"CacheSize"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|size
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|repo
operator|.
name|contains
argument_list|(
literal|"1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|repo
operator|.
name|contains
argument_list|(
literal|"2"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|repo
operator|.
name|contains
argument_list|(
literal|"3"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|repo
operator|.
name|contains
argument_list|(
literal|"4"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDuplicateMessagesCountAreCorrectlyCounted ()
specifier|public
name|void
name|testDuplicateMessagesCountAreCorrectlyCounted
parameter_list|()
throws|throws
name|Exception
block|{
comment|// JMX tests dont work well on AIX CI servers (hangs them)
if|if
condition|(
name|isPlatform
argument_list|(
literal|"aix"
argument_list|)
condition|)
block|{
return|return;
block|}
name|MBeanServer
name|mbeanServer
init|=
name|getMBeanServer
argument_list|()
decl_stmt|;
comment|// processors
name|Set
argument_list|<
name|ObjectName
argument_list|>
name|names
init|=
name|mbeanServer
operator|.
name|queryNames
argument_list|(
operator|new
name|ObjectName
argument_list|(
literal|"org.apache.camel"
operator|+
literal|":type=processors,*"
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|ObjectName
name|on
init|=
literal|null
decl_stmt|;
for|for
control|(
name|ObjectName
name|name
range|:
name|names
control|)
block|{
if|if
condition|(
name|name
operator|.
name|toString
argument_list|()
operator|.
name|contains
argument_list|(
literal|"idempotentConsumer"
argument_list|)
condition|)
block|{
name|on
operator|=
name|name
expr_stmt|;
break|break;
block|}
block|}
name|assertTrue
argument_list|(
literal|"Should be registered"
argument_list|,
name|mbeanServer
operator|.
name|isRegistered
argument_list|(
name|on
argument_list|)
argument_list|)
expr_stmt|;
name|Long
name|count
init|=
operator|(
name|Long
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"DuplicateMessageCount"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0L
argument_list|,
name|count
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"one"
argument_list|,
literal|"two"
argument_list|)
expr_stmt|;
name|sendMessage
argument_list|(
literal|"1"
argument_list|,
literal|"one"
argument_list|)
expr_stmt|;
name|sendMessage
argument_list|(
literal|"2"
argument_list|,
literal|"two"
argument_list|)
expr_stmt|;
name|sendMessage
argument_list|(
literal|"1"
argument_list|,
literal|"one"
argument_list|)
expr_stmt|;
name|sendMessage
argument_list|(
literal|"2"
argument_list|,
literal|"two"
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|count
operator|=
operator|(
name|Long
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"DuplicateMessageCount"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2L
argument_list|,
name|count
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
comment|// reset the count
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"resetDuplicateMessageCount"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// count should be resetted
name|count
operator|=
operator|(
name|Long
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"DuplicateMessageCount"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0L
argument_list|,
name|count
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
name|resetMocks
argument_list|()
expr_stmt|;
name|resultEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"five"
argument_list|)
expr_stmt|;
name|sendMessage
argument_list|(
literal|"4"
argument_list|,
literal|"four"
argument_list|)
expr_stmt|;
name|sendMessage
argument_list|(
literal|"4"
argument_list|,
literal|"four"
argument_list|)
expr_stmt|;
name|sendMessage
argument_list|(
literal|"5"
argument_list|,
literal|"five"
argument_list|)
expr_stmt|;
name|sendMessage
argument_list|(
literal|"4"
argument_list|,
literal|"four"
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|count
operator|=
operator|(
name|Long
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"DuplicateMessageCount"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3L
argument_list|,
name|count
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|sendMessage (final Object messageId, final Object body)
specifier|protected
name|void
name|sendMessage
parameter_list|(
specifier|final
name|Object
name|messageId
parameter_list|,
specifier|final
name|Object
name|body
parameter_list|)
block|{
name|template
operator|.
name|send
argument_list|(
name|startEndpoint
argument_list|,
name|exchange
lambda|->
block|{
comment|// now lets fire in a message
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|in
operator|.
name|setBody
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
literal|"messageId"
argument_list|,
name|messageId
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|repo
operator|=
name|MemoryIdempotentRepository
operator|.
name|memoryIdempotentRepository
argument_list|()
expr_stmt|;
comment|// lets start with 4
name|repo
operator|.
name|add
argument_list|(
literal|"4"
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|startEndpoint
operator|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"direct:start"
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|idempotentConsumer
argument_list|(
name|header
argument_list|(
literal|"messageId"
argument_list|)
argument_list|,
name|repo
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

