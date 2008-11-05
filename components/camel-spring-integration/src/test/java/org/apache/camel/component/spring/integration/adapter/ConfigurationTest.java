begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spring.integration.adapter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spring
operator|.
name|integration
operator|.
name|adapter
package|;
end_package

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
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|AbstractXmlApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_class
DECL|class|ConfigurationTest
specifier|public
class|class
name|ConfigurationTest
extends|extends
name|TestCase
block|{
DECL|field|context
specifier|private
name|AbstractXmlApplicationContext
name|context
decl_stmt|;
DECL|method|testCamelSourceEndpoint ()
specifier|public
name|void
name|testCamelSourceEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"/org/apache/camel/component/spring/integration/adapter/CamelSource.xml"
block|}
argument_list|)
expr_stmt|;
name|CamelSourceAdapter
name|camelSourceA
init|=
operator|(
name|CamelSourceAdapter
operator|)
name|context
operator|.
name|getBean
argument_list|(
literal|"camelSourceA"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|camelSourceA
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get the wrong request channel name"
argument_list|,
name|camelSourceA
operator|.
name|getChannel
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
literal|"channelA"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ExpectReply should be false "
argument_list|,
name|camelSourceA
operator|.
name|isExpectReply
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|CamelSourceAdapter
name|camelSourceB
init|=
operator|(
name|CamelSourceAdapter
operator|)
name|context
operator|.
name|getBean
argument_list|(
literal|"camelSourceB"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|camelSourceB
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get the wrong request channel name"
argument_list|,
name|camelSourceB
operator|.
name|getChannel
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
literal|"channelB"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ExpectReply should be true "
argument_list|,
name|camelSourceB
operator|.
name|isExpectReply
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|context
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
DECL|method|testCamelTragetEndpoint ()
specifier|public
name|void
name|testCamelTragetEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"/org/apache/camel/component/spring/integration/adapter/CamelTarget.xml"
block|}
argument_list|)
expr_stmt|;
name|CamelTargetAdapter
name|camelTargetA
init|=
operator|(
name|CamelTargetAdapter
operator|)
name|context
operator|.
name|getBean
argument_list|(
literal|"camelTargetA"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|camelTargetA
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Subscript the wrong CamelEndpointUri"
argument_list|,
name|camelTargetA
operator|.
name|getCamelEndpointUri
argument_list|()
argument_list|,
literal|"direct:EndpointA"
argument_list|)
expr_stmt|;
name|CamelTargetAdapter
name|camelTargetB
init|=
operator|(
name|CamelTargetAdapter
operator|)
name|context
operator|.
name|getBean
argument_list|(
literal|"camelTargetB"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|camelTargetB
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Subscript the wrong reply channel name"
argument_list|,
name|camelTargetB
operator|.
name|getReplyChannel
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
literal|"channelC"
argument_list|)
expr_stmt|;
name|context
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

