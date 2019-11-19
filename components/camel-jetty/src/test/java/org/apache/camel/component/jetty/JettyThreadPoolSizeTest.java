begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jetty
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jetty
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
name|junit
operator|.
name|Ignore
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
annotation|@
name|Ignore
argument_list|(
literal|"Fails on CI server"
argument_list|)
DECL|class|JettyThreadPoolSizeTest
specifier|public
class|class
name|JettyThreadPoolSizeTest
extends|extends
name|BaseJettyTest
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|JettyThreadPoolSizeTest
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Test
DECL|method|threadPoolTest ()
specifier|public
name|void
name|threadPoolTest
parameter_list|()
block|{
name|long
name|initialJettyThreadNumber
init|=
name|countJettyThread
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"initial Jetty thread number (expected 5): "
operator|+
name|initialJettyThreadNumber
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|initialJettyThreadNumber
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|long
name|jettyThreadNumberAfterStop
init|=
name|countJettyThread
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Jetty thread number after stopping Camel Context: (expected 0): "
operator|+
name|jettyThreadNumberAfterStop
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|jettyThreadNumberAfterStop
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
comment|// setup the jetty component with the custom minThreads
name|JettyHttpComponent
name|jettyComponent
init|=
operator|(
name|JettyHttpComponent
operator|)
name|context
operator|.
name|getComponent
argument_list|(
literal|"jetty"
argument_list|)
decl_stmt|;
name|jettyComponent
operator|.
name|setMinThreads
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|jettyComponent
operator|.
name|setMaxThreads
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jetty://http://localhost:{{port}}/myserverWithCustomPoolSize"
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
DECL|method|countJettyThread ()
specifier|private
name|long
name|countJettyThread
parameter_list|()
block|{
name|Set
argument_list|<
name|Thread
argument_list|>
name|threadSet
init|=
name|Thread
operator|.
name|getAllStackTraces
argument_list|()
operator|.
name|keySet
argument_list|()
decl_stmt|;
return|return
name|threadSet
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|thread
lambda|->
name|thread
operator|.
name|getName
argument_list|()
operator|.
name|contains
argument_list|(
literal|"CamelJettyServer"
argument_list|)
argument_list|)
operator|.
name|count
argument_list|()
return|;
block|}
block|}
end_class

end_unit

