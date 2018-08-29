begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
package|;
end_package

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
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|ContextTestSupport
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

begin_class
DECL|class|FileConsumerThreadsInProgressIssueTest
specifier|public
class|class
name|FileConsumerThreadsInProgressIssueTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|duplicate
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|duplicate
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|processor
specifier|private
specifier|final
name|SampleProcessor
name|processor
init|=
operator|new
name|SampleProcessor
argument_list|(
name|duplicate
argument_list|)
decl_stmt|;
DECL|field|number
specifier|private
name|int
name|number
init|=
literal|2000
decl_stmt|;
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
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
literal|"file:target/manyfiles?sortBy=file:name&delay=10&synchronous=false"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"myRoute"
argument_list|)
operator|.
name|noAutoStartup
argument_list|()
operator|.
name|threads
argument_list|(
literal|1
argument_list|,
literal|10
argument_list|)
operator|.
name|maxQueueSize
argument_list|(
literal|0
argument_list|)
operator|.
name|convertBodyTo
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|process
argument_list|(
name|processor
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:done"
argument_list|,
literal|"mock:done"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|testFileConsumerThreadsInProgressIssue ()
specifier|public
name|void
name|testFileConsumerThreadsInProgressIssue
parameter_list|()
throws|throws
name|Exception
block|{
comment|// give longer timeout for stopping
name|context
operator|.
name|getShutdownStrategy
argument_list|()
operator|.
name|setTimeout
argument_list|(
literal|180
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:done"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
name|number
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectsNoDuplicates
argument_list|(
name|body
argument_list|()
argument_list|)
expr_stmt|;
name|createManyFiles
argument_list|(
name|number
argument_list|)
expr_stmt|;
name|context
operator|.
name|startRoute
argument_list|(
literal|"myRoute"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|setResultWaitTime
argument_list|(
literal|180
operator|*
literal|1000
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|int
name|found
init|=
literal|0
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"====================="
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Printing duplicates"
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|ent
range|:
name|duplicate
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Integer
name|count
init|=
name|ent
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|count
operator|>
literal|1
condition|)
block|{
name|found
operator|++
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
name|ent
operator|.
name|getKey
argument_list|()
operator|+
literal|" :: "
operator|+
name|count
argument_list|)
expr_stmt|;
block|}
block|}
name|assertEquals
argument_list|(
literal|"Should not contain duplicates"
argument_list|,
literal|0
argument_list|,
name|found
argument_list|)
expr_stmt|;
block|}
DECL|method|createManyFiles (int number)
specifier|private
specifier|static
name|void
name|createManyFiles
parameter_list|(
name|int
name|number
parameter_list|)
throws|throws
name|Exception
block|{
name|deleteDirectory
argument_list|(
literal|"target/manyfiles"
argument_list|)
expr_stmt|;
name|createDirectory
argument_list|(
literal|"target/manyfiles"
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|number
condition|;
name|i
operator|++
control|)
block|{
name|String
name|pad
init|=
name|String
operator|.
name|format
argument_list|(
literal|"%04d"
argument_list|,
name|i
argument_list|)
decl_stmt|;
name|PrintWriter
name|writer
init|=
operator|new
name|PrintWriter
argument_list|(
literal|"target/manyfiles/newFile-"
operator|+
name|pad
argument_list|,
literal|"UTF-8"
argument_list|)
decl_stmt|;
name|writer
operator|.
name|println
argument_list|(
name|pad
argument_list|)
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|class|SampleProcessor
specifier|private
class|class
name|SampleProcessor
implements|implements
name|Processor
block|{
DECL|field|duplicate
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|duplicate
decl_stmt|;
DECL|method|SampleProcessor (Map<String, Integer> duplicate)
specifier|public
name|SampleProcessor
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|duplicate
parameter_list|)
block|{
name|this
operator|.
name|duplicate
operator|=
name|duplicate
expr_stmt|;
block|}
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
name|Integer
name|integer
init|=
name|duplicate
operator|.
name|get
argument_list|(
name|exchange
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|integer
operator|==
literal|null
condition|)
block|{
name|duplicate
operator|.
name|put
argument_list|(
name|exchange
operator|.
name|toString
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|integer
operator|++
expr_stmt|;
name|duplicate
operator|.
name|put
argument_list|(
name|exchange
operator|.
name|toString
argument_list|()
argument_list|,
name|integer
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|info
argument_list|(
literal|"Process called for-"
operator|+
name|exchange
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|20
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

