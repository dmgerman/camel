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
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicLong
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
name|util
operator|.
name|StopWatch
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
name|util
operator|.
name|TimeUtils
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|BeanPerformanceTest
specifier|public
class|class
name|BeanPerformanceTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|INVOKED
specifier|private
specifier|static
specifier|final
name|AtomicLong
name|INVOKED
init|=
operator|new
name|AtomicLong
argument_list|()
decl_stmt|;
DECL|field|times
specifier|private
specifier|final
name|int
name|times
init|=
literal|100000
decl_stmt|;
DECL|method|doSomething (String payload)
specifier|public
specifier|static
name|void
name|doSomething
parameter_list|(
name|String
name|payload
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|payload
argument_list|)
expr_stmt|;
name|INVOKED
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBeanPerformance ()
specifier|public
name|void
name|testBeanPerformance
parameter_list|()
throws|throws
name|Exception
block|{
name|StopWatch
name|watch
init|=
operator|new
name|StopWatch
argument_list|()
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Invoking a bean in a route {} times"
argument_list|,
name|times
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
name|times
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|info
argument_list|(
literal|"Took {} to invoke the bean {} times"
argument_list|,
name|TimeUtils
operator|.
name|printDuration
argument_list|(
name|watch
operator|.
name|taken
argument_list|()
argument_list|)
argument_list|,
name|times
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|times
argument_list|,
name|INVOKED
operator|.
name|get
argument_list|()
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
literal|"direct:start"
argument_list|)
operator|.
name|bean
argument_list|(
name|BeanPerformanceTest
operator|.
name|class
argument_list|,
literal|"doSomething"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

