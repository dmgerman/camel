begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
package|;
end_package

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
name|Service
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
name|TestSupport
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
name|jndi
operator|.
name|JndiContext
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
DECL|class|LifecycleStrategyServiceTest
specifier|public
class|class
name|LifecycleStrategyServiceTest
extends|extends
name|TestSupport
block|{
DECL|field|dummy1
specifier|private
name|MyLifecycleStrategy
name|dummy1
init|=
operator|new
name|MyLifecycleStrategy
argument_list|()
decl_stmt|;
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|(
operator|new
name|JndiContext
argument_list|()
argument_list|)
decl_stmt|;
name|context
operator|.
name|addLifecycleStrategy
argument_list|(
name|dummy1
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
annotation|@
name|Test
DECL|method|testLifecycleStrategyService ()
specifier|public
name|void
name|testLifecycleStrategyService
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|dummy1
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|CamelContext
name|context
init|=
name|createCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|dummy1
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|dummy1
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|class|MyLifecycleStrategy
specifier|private
specifier|static
class|class
name|MyLifecycleStrategy
extends|extends
name|DummyLifecycleStrategy
implements|implements
name|Service
block|{
DECL|field|started
specifier|private
specifier|volatile
name|boolean
name|started
decl_stmt|;
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
block|{
name|started
operator|=
literal|true
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{
name|started
operator|=
literal|false
expr_stmt|;
block|}
DECL|method|isStarted ()
specifier|public
name|boolean
name|isStarted
parameter_list|()
block|{
return|return
name|started
return|;
block|}
block|}
block|}
end_class

end_unit

