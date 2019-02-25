begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bean
package|;
end_package

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
name|ProducerTemplate
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
name|impl
operator|.
name|DefaultCamelContext
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
name|SimpleRegistry
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
name|Registry
import|;
end_import

begin_class
DECL|class|CamelSimpleExpressionPerfTestRunner
specifier|public
specifier|final
class|class
name|CamelSimpleExpressionPerfTestRunner
block|{
DECL|field|MESSAGE_LOOP_COUNT
specifier|private
specifier|static
specifier|final
name|int
name|MESSAGE_LOOP_COUNT
init|=
literal|1000
decl_stmt|;
DECL|field|TEST_EXECUTION_COUNT
specifier|private
specifier|static
specifier|final
name|int
name|TEST_EXECUTION_COUNT
init|=
literal|5
decl_stmt|;
DECL|method|CamelSimpleExpressionPerfTestRunner ()
specifier|private
name|CamelSimpleExpressionPerfTestRunner
parameter_list|()
block|{
comment|//Utils class
block|}
DECL|method|main (String[] args)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|long
name|bodyOnly
init|=
name|executePerformanceTest
argument_list|(
operator|new
name|SimpleRegistry
argument_list|()
argument_list|,
literal|"${body}"
argument_list|)
decl_stmt|;
name|long
name|bodyProperty
init|=
name|executePerformanceTest
argument_list|(
operator|new
name|SimpleRegistry
argument_list|()
argument_list|,
literal|"${body[p]}"
argument_list|)
decl_stmt|;
name|long
name|bodyPropertyWithCache
init|=
name|executePerformanceTest
argument_list|(
operator|new
name|SimpleRegistry
argument_list|()
argument_list|,
literal|"${body[p]}"
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|printf
argument_list|(
literal|"${body}: %dms%n"
argument_list|,
name|bodyOnly
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|printf
argument_list|(
literal|"${body[p]} : %dms%n"
argument_list|,
name|bodyProperty
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|printf
argument_list|(
literal|"${body[p]} with cache : %dms%n"
argument_list|,
name|bodyPropertyWithCache
argument_list|)
expr_stmt|;
block|}
DECL|method|executePerformanceTest (Registry registry, final String simpleExpression)
specifier|private
specifier|static
name|long
name|executePerformanceTest
parameter_list|(
name|Registry
name|registry
parameter_list|,
specifier|final
name|String
name|simpleExpression
parameter_list|)
throws|throws
name|Exception
block|{
name|CamelContext
name|ctx
init|=
operator|new
name|DefaultCamelContext
argument_list|(
name|registry
argument_list|)
decl_stmt|;
name|ctx
operator|.
name|addRoutes
argument_list|(
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
literal|"direct:start"
argument_list|)
operator|.
name|loop
argument_list|(
name|MESSAGE_LOOP_COUNT
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"test"
argument_list|)
operator|.
name|simple
argument_list|(
name|simpleExpression
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:plop"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|start
argument_list|()
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|body
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|body
operator|.
name|put
argument_list|(
literal|"p"
argument_list|,
literal|"q"
argument_list|)
expr_stmt|;
name|ProducerTemplate
name|template
init|=
name|ctx
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
comment|// Initial one, it's a dry start, we don't care about this one.
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|body
argument_list|)
expr_stmt|;
comment|// Measure the duration of the executions in nanoseconds
name|long
name|totalNsDuration
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|TEST_EXECUTION_COUNT
condition|;
name|i
operator|++
control|)
block|{
name|long
name|tick
init|=
name|System
operator|.
name|nanoTime
argument_list|()
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|totalNsDuration
operator|+=
name|System
operator|.
name|nanoTime
argument_list|()
operator|-
name|tick
expr_stmt|;
block|}
name|ctx
operator|.
name|stop
argument_list|()
expr_stmt|;
comment|// Return the average duration in milliseconds
return|return
name|totalNsDuration
operator|/
name|TEST_EXECUTION_COUNT
operator|/
literal|1000
operator|/
literal|1000
return|;
block|}
block|}
end_class

end_unit

