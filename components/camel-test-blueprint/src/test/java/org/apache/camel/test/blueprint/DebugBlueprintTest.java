begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test.blueprint
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|blueprint
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
name|model
operator|.
name|ProcessorDefinition
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

begin_comment
comment|// START SNIPPET: example
end_comment

begin_comment
comment|// to use camel-test-blueprint, then extend the CamelBlueprintTestSupport class,
end_comment

begin_comment
comment|// and add your unit tests methods as shown below.
end_comment

begin_class
DECL|class|DebugBlueprintTest
specifier|public
class|class
name|DebugBlueprintTest
extends|extends
name|CamelBlueprintTestSupport
block|{
DECL|field|debugBeforeMethodCalled
specifier|private
name|boolean
name|debugBeforeMethodCalled
decl_stmt|;
DECL|field|debugAfterMethodCalled
specifier|private
name|boolean
name|debugAfterMethodCalled
decl_stmt|;
comment|// override this method, and return the location of our Blueprint XML file to be used for testing
annotation|@
name|Override
DECL|method|getBlueprintDescriptor ()
specifier|protected
name|String
name|getBlueprintDescriptor
parameter_list|()
block|{
return|return
literal|"org/apache/camel/test/blueprint/camelContext.xml"
return|;
block|}
comment|// here we have regular JUnit @Test method
annotation|@
name|Test
DECL|method|testRoute ()
specifier|public
name|void
name|testRoute
parameter_list|()
throws|throws
name|Exception
block|{
comment|// set mock expectations
name|getMockEndpoint
argument_list|(
literal|"mock:a"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// send a message
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"World"
argument_list|)
expr_stmt|;
comment|// assert mocks
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// assert on the debugBefore/debugAfter methods below being called as we've enabled the debugger
name|assertTrue
argument_list|(
name|debugBeforeMethodCalled
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|debugAfterMethodCalled
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isUseDebugger ()
specifier|public
name|boolean
name|isUseDebugger
parameter_list|()
block|{
comment|// must enable debugger
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|debugBefore (Exchange exchange, org.apache.camel.Processor processor, ProcessorDefinition<?> definition, String id, String label)
specifier|protected
name|void
name|debugBefore
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Processor
name|processor
parameter_list|,
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|,
name|String
name|id
parameter_list|,
name|String
name|label
parameter_list|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Before "
operator|+
name|definition
operator|+
literal|" with body "
operator|+
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|debugBeforeMethodCalled
operator|=
literal|true
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|debugAfter (Exchange exchange, org.apache.camel.Processor processor, ProcessorDefinition<?> definition, String id, String label, long timeTaken)
specifier|protected
name|void
name|debugAfter
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Processor
name|processor
parameter_list|,
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|,
name|String
name|id
parameter_list|,
name|String
name|label
parameter_list|,
name|long
name|timeTaken
parameter_list|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"After "
operator|+
name|definition
operator|+
literal|" with body "
operator|+
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|debugAfterMethodCalled
operator|=
literal|true
expr_stmt|;
block|}
block|}
end_class

begin_comment
comment|// END SNIPPET: example
end_comment

end_unit

