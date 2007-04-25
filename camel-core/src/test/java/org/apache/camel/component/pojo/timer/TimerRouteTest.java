begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.pojo.timer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|pojo
operator|.
name|timer
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
name|atomic
operator|.
name|AtomicInteger
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|pojo
operator|.
name|PojoComponent
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
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * @version $Revision: 520220 $  */
end_comment

begin_class
DECL|class|TimerRouteTest
specifier|public
class|class
name|TimerRouteTest
extends|extends
name|TestCase
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|TimerRouteTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|testPojoRoutes ()
specifier|public
name|void
name|testPojoRoutes
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|container
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
specifier|final
name|AtomicInteger
name|hitCount
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
name|PojoComponent
name|component
init|=
operator|new
name|PojoComponent
argument_list|()
decl_stmt|;
name|component
operator|.
name|addService
argument_list|(
literal|"bar"
argument_list|,
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"hit"
argument_list|)
expr_stmt|;
name|hitCount
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|container
operator|.
name|addComponent
argument_list|(
literal|"pojo"
argument_list|,
name|component
argument_list|)
expr_stmt|;
comment|// lets add some routes
name|container
operator|.
name|addRoutes
argument_list|(
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
literal|"timer://foo?fixedRate=true&delay=0&period=500"
argument_list|)
operator|.
name|to
argument_list|(
literal|"pojo:bar"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|container
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// now lets wait for the timer to fire a few times.
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
operator|*
literal|2
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|""
argument_list|,
name|hitCount
operator|.
name|get
argument_list|()
operator|>=
literal|3
argument_list|)
expr_stmt|;
name|container
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

