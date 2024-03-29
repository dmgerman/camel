begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test.blueprint.component.rest
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
operator|.
name|component
operator|.
name|rest
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
name|CollectionFormat
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
name|model
operator|.
name|rest
operator|.
name|RestParamType
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
name|test
operator|.
name|blueprint
operator|.
name|CamelBlueprintTestSupport
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
DECL|class|FromRestGetTest
specifier|public
class|class
name|FromRestGetTest
extends|extends
name|CamelBlueprintTestSupport
block|{
annotation|@
name|Override
DECL|method|getBlueprintDescriptor ()
specifier|protected
name|String
name|getBlueprintDescriptor
parameter_list|()
block|{
return|return
literal|"org/apache/camel/test/blueprint/component/rest/FromRestGetTest.xml"
return|;
block|}
DECL|method|getExpectedNumberOfRoutes ()
specifier|protected
name|int
name|getExpectedNumberOfRoutes
parameter_list|()
block|{
return|return
literal|2
operator|+
literal|3
return|;
block|}
annotation|@
name|Test
DECL|method|testFromRestModel ()
specifier|public
name|void
name|testFromRestModel
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
name|getExpectedNumberOfRoutes
argument_list|()
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
name|assertEquals
argument_list|(
literal|2
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
name|getParams
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|RestParamType
operator|.
name|header
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
name|getParams
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|RestParamType
operator|.
name|query
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
name|getParams
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"header param description1"
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
name|getParams
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"header param description2"
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
name|getParams
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"integer"
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
name|getParams
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getDataType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"string"
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
name|getParams
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getDataType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"1"
argument_list|,
literal|"2"
argument_list|,
literal|"3"
argument_list|,
literal|"4"
argument_list|)
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
name|getParams
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getAllowableValues
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"c"
argument_list|,
literal|"d"
argument_list|)
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
name|getParams
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getAllowableValues
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1"
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
name|getParams
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getDefaultValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"b"
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
name|getParams
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getDefaultValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
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
name|getParams
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getCollectionFormat
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|CollectionFormat
operator|.
name|multi
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
name|getParams
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getCollectionFormat
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"header_count"
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
name|getParams
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"header_letter"
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
name|getParams
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|TRUE
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
name|getParams
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getRequired
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|FALSE
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
name|getParams
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getRequired
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"300"
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
name|getResponseMsgs
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"rate"
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
name|getResponseMsgs
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getHeaders
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Rate limit"
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
name|getResponseMsgs
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getHeaders
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"integer"
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
name|getResponseMsgs
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getHeaders
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getDataType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test msg"
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
name|getResponseMsgs
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|class
operator|.
name|getCanonicalName
argument_list|()
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
name|getResponseMsgs
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getResponseModel
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
block|}
end_class

end_unit

