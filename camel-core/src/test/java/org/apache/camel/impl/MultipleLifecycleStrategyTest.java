begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|Component
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
name|component
operator|.
name|log
operator|.
name|LogComponent
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
name|jndi
operator|.
name|JndiContext
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|MultipleLifecycleStrategyTest
specifier|public
class|class
name|MultipleLifecycleStrategyTest
extends|extends
name|TestSupport
block|{
DECL|field|dummy1
specifier|private
name|DummyLifecycleStrategy
name|dummy1
init|=
operator|new
name|DummyLifecycleStrategy
argument_list|()
decl_stmt|;
DECL|field|dummy2
specifier|private
name|DummyLifecycleStrategy
name|dummy2
init|=
operator|new
name|DummyLifecycleStrategy
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
name|context
operator|.
name|addLifecycleStrategy
argument_list|(
name|dummy2
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
DECL|method|testMultipleLifecycleStrategies ()
specifier|public
name|void
name|testMultipleLifecycleStrategies
parameter_list|()
throws|throws
name|Exception
block|{
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
name|Component
name|log
init|=
operator|new
name|LogComponent
argument_list|()
decl_stmt|;
name|context
operator|.
name|addComponent
argument_list|(
literal|"log"
argument_list|,
name|log
argument_list|)
expr_stmt|;
name|context
operator|.
name|addEndpoint
argument_list|(
literal|"log:/foo"
argument_list|,
name|log
operator|.
name|createEndpoint
argument_list|(
literal|"log://foo"
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|removeComponent
argument_list|(
literal|"log"
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|expectedEvents
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|"onContextStart"
argument_list|,
literal|"onServiceAdd"
argument_list|,
literal|"onServiceAdd"
argument_list|,
literal|"onServiceAdd"
argument_list|,
literal|"onServiceAdd"
argument_list|,
literal|"onServiceAdd"
argument_list|,
literal|"onServiceAdd"
argument_list|,
literal|"onServiceAdd"
argument_list|,
literal|"onServiceAdd"
argument_list|,
literal|"onServiceAdd"
argument_list|,
literal|"onServiceAdd"
argument_list|,
literal|"onComponentAdd"
argument_list|,
literal|"onEndpointAdd"
argument_list|,
literal|"onComponentRemove"
argument_list|,
literal|"onContextStop"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedEvents
argument_list|,
name|dummy1
operator|.
name|getEvents
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedEvents
argument_list|,
name|dummy2
operator|.
name|getEvents
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

