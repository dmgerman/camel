begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ref
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ref
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
name|Consumer
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
name|component
operator|.
name|direct
operator|.
name|DirectComponent
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
name|impl
operator|.
name|JndiRegistry
import|;
end_import

begin_class
DECL|class|RefComponentTest
specifier|public
class|class
name|RefComponentTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|bindToRegistry (JndiRegistry jndi)
specifier|private
name|void
name|bindToRegistry
parameter_list|(
name|JndiRegistry
name|jndi
parameter_list|)
throws|throws
name|Exception
block|{
name|Component
name|comp
init|=
operator|new
name|DirectComponent
argument_list|()
decl_stmt|;
name|comp
operator|.
name|setCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|Endpoint
name|slow
init|=
name|comp
operator|.
name|createEndpoint
argument_list|(
literal|"direct:somename"
argument_list|)
decl_stmt|;
name|Consumer
name|consumer
init|=
name|slow
operator|.
name|createConsumer
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
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
name|template
operator|.
name|send
argument_list|(
literal|"mock:result"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|consumer
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// bind our endpoint to the registry for ref to lookup
name|jndi
operator|.
name|bind
argument_list|(
literal|"foo"
argument_list|,
name|slow
argument_list|)
expr_stmt|;
block|}
DECL|method|testRef ()
specifier|public
name|void
name|testRef
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|JndiRegistry
name|jndi
init|=
operator|(
name|JndiRegistry
operator|)
name|context
operator|.
name|getRegistry
argument_list|()
decl_stmt|;
name|bindToRegistry
argument_list|(
name|jndi
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"ref:foo"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

