begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.rest
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|rest
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
name|model
operator|.
name|ToDefinition
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
name|rest
operator|.
name|RestDefinition
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
name|spring
operator|.
name|SpringTestSupport
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|RestRefTest
specifier|public
class|class
name|RestRefTest
extends|extends
name|SpringTestSupport
block|{
DECL|method|testRestRefTest ()
specifier|public
name|void
name|testRestRefTest
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|2
operator|+
literal|3
argument_list|,
name|context
operator|.
name|getRoutes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|context
operator|.
name|getRestDefinitions
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|RestDefinition
name|rest
init|=
name|context
operator|.
name|getRestDefinitions
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|rest
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/say/hello"
argument_list|,
name|rest
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|rest
operator|.
name|getVerbs
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ToDefinition
name|to
init|=
name|assertIsInstanceOf
argument_list|(
name|ToDefinition
operator|.
name|class
argument_list|,
name|rest
operator|.
name|getVerbs
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getTo
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"direct:hello"
argument_list|,
name|to
operator|.
name|getUri
argument_list|()
argument_list|)
expr_stmt|;
name|rest
operator|=
name|context
operator|.
name|getRestDefinitions
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|rest
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/say/bye"
argument_list|,
name|rest
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|rest
operator|.
name|getVerbs
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"application/json"
argument_list|,
name|rest
operator|.
name|getVerbs
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getConsumes
argument_list|()
argument_list|)
expr_stmt|;
name|to
operator|=
name|assertIsInstanceOf
argument_list|(
name|ToDefinition
operator|.
name|class
argument_list|,
name|rest
operator|.
name|getVerbs
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getTo
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"direct:bye"
argument_list|,
name|to
operator|.
name|getUri
argument_list|()
argument_list|)
expr_stmt|;
comment|// the rest becomes routes and the input is a seda endpoint created by the DummyRestConsumerFactory
name|getMockEndpoint
argument_list|(
literal|"mock:update"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:post-say-bye"
argument_list|,
literal|"I was here"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|String
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"seda:get-say-hello"
argument_list|,
literal|"Me"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|String
name|out2
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"seda:get-say-bye"
argument_list|,
literal|"Me"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|out2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/component/rest/RestRefTest.xml"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

