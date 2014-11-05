begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.osgi.hazelcast
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|osgi
operator|.
name|hazelcast
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
name|hazelcast
operator|.
name|HazelcastConstants
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
name|itest
operator|.
name|osgi
operator|.
name|OSGiIntegrationTestSupport
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
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|Option
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|junit
operator|.
name|PaxExam
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|OptionUtils
operator|.
name|combine
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|PaxExam
operator|.
name|class
argument_list|)
DECL|class|HazelcastTest
specifier|public
class|class
name|HazelcastTest
extends|extends
name|OSGiIntegrationTestSupport
block|{
annotation|@
name|Test
DECL|method|testAtomicNumber ()
specifier|public
name|void
name|testAtomicNumber
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|reply
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:getatomic"
argument_list|,
literal|null
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|reply
argument_list|)
expr_stmt|;
name|long
name|value
init|=
name|Long
operator|.
name|parseLong
argument_list|(
name|reply
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Should be 0"
argument_list|,
literal|0L
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:setatomic"
argument_list|,
literal|100L
argument_list|)
expr_stmt|;
name|reply
operator|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:getatomic"
argument_list|,
literal|null
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|reply
argument_list|)
expr_stmt|;
name|value
operator|=
name|Long
operator|.
name|parseLong
argument_list|(
name|reply
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should be 100"
argument_list|,
literal|100L
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMap ()
specifier|public
name|void
name|testMap
parameter_list|()
throws|throws
name|Exception
block|{
name|SimpleObject
name|item1
init|=
operator|new
name|SimpleObject
argument_list|(
literal|1L
argument_list|,
literal|"Some value"
argument_list|)
decl_stmt|;
name|SimpleObject
name|item2
init|=
operator|new
name|SimpleObject
argument_list|(
literal|2L
argument_list|,
literal|"Some other value"
argument_list|)
decl_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:mapput"
argument_list|,
name|item1
argument_list|,
name|HazelcastConstants
operator|.
name|OBJECT_ID
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:mapput"
argument_list|,
name|item2
argument_list|,
name|HazelcastConstants
operator|.
name|OBJECT_ID
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|Object
name|result2
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"direct:mapget"
argument_list|,
literal|null
argument_list|,
name|HazelcastConstants
operator|.
name|OBJECT_ID
argument_list|,
literal|"2"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should be equal"
argument_list|,
name|item2
argument_list|,
name|result2
argument_list|)
expr_stmt|;
name|Object
name|result1
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"direct:mapget"
argument_list|,
literal|null
argument_list|,
name|HazelcastConstants
operator|.
name|OBJECT_ID
argument_list|,
literal|"1"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should be equal"
argument_list|,
name|item1
argument_list|,
name|result1
argument_list|)
expr_stmt|;
name|Object
name|resul3
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"direct:mapget"
argument_list|,
literal|null
argument_list|,
name|HazelcastConstants
operator|.
name|OBJECT_ID
argument_list|,
literal|"3"
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|resul3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testQueue ()
specifier|public
name|void
name|testQueue
parameter_list|()
throws|throws
name|Exception
block|{
name|SimpleObject
name|item1
init|=
operator|new
name|SimpleObject
argument_list|(
literal|1L
argument_list|,
literal|"Some value"
argument_list|)
decl_stmt|;
name|SimpleObject
name|item2
init|=
operator|new
name|SimpleObject
argument_list|(
literal|2L
argument_list|,
literal|"Some other value"
argument_list|)
decl_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:queueput"
argument_list|,
name|item1
argument_list|,
name|HazelcastConstants
operator|.
name|OPERATION
argument_list|,
name|HazelcastConstants
operator|.
name|ADD_OPERATION
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:queueput"
argument_list|,
name|item2
argument_list|,
name|HazelcastConstants
operator|.
name|OPERATION
argument_list|,
name|HazelcastConstants
operator|.
name|ADD_OPERATION
argument_list|)
expr_stmt|;
name|Object
name|result1
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:queuepoll"
argument_list|,
operator|new
name|Object
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should be equal"
argument_list|,
name|item1
argument_list|,
name|result1
argument_list|)
expr_stmt|;
name|Object
name|result2
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:queuepoll"
argument_list|,
operator|new
name|Object
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should be equal"
argument_list|,
name|item1
argument_list|,
name|result1
argument_list|)
expr_stmt|;
name|Object
name|resul3
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:queuepoll"
argument_list|,
operator|new
name|Object
argument_list|()
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|resul3
argument_list|)
expr_stmt|;
block|}
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
comment|//Atomic number
name|from
argument_list|(
literal|"direct:getatomic"
argument_list|)
operator|.
name|setHeader
argument_list|(
name|HazelcastConstants
operator|.
name|OPERATION
argument_list|,
name|constant
argument_list|(
name|HazelcastConstants
operator|.
name|GET_OPERATION
argument_list|)
argument_list|)
operator|.
name|toF
argument_list|(
literal|"hazelcast:atomicvalue:myvalue"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:setatomic"
argument_list|)
operator|.
name|setHeader
argument_list|(
name|HazelcastConstants
operator|.
name|OPERATION
argument_list|,
name|constant
argument_list|(
name|HazelcastConstants
operator|.
name|SETVALUE_OPERATION
argument_list|)
argument_list|)
operator|.
name|toF
argument_list|(
literal|"hazelcast:atomicvalue:myvalue"
argument_list|)
expr_stmt|;
comment|//Map
name|from
argument_list|(
literal|"direct:mapput"
argument_list|)
operator|.
name|setHeader
argument_list|(
name|HazelcastConstants
operator|.
name|OPERATION
argument_list|,
name|constant
argument_list|(
name|HazelcastConstants
operator|.
name|PUT_OPERATION
argument_list|)
argument_list|)
operator|.
name|toF
argument_list|(
literal|"hazelcast:map:mymap"
argument_list|,
name|HazelcastConstants
operator|.
name|MAP_PREFIX
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:mapget"
argument_list|)
operator|.
name|setHeader
argument_list|(
name|HazelcastConstants
operator|.
name|OPERATION
argument_list|,
name|constant
argument_list|(
name|HazelcastConstants
operator|.
name|GET_OPERATION
argument_list|)
argument_list|)
operator|.
name|toF
argument_list|(
literal|"hazelcast:map:mymap"
argument_list|)
expr_stmt|;
comment|//Queue
name|from
argument_list|(
literal|"direct:queueput"
argument_list|)
operator|.
name|setHeader
argument_list|(
name|HazelcastConstants
operator|.
name|OPERATION
argument_list|,
name|constant
argument_list|(
name|HazelcastConstants
operator|.
name|PUT_OPERATION
argument_list|)
argument_list|)
operator|.
name|toF
argument_list|(
literal|"hazelcast:queue:myqueue"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:queuepoll"
argument_list|)
operator|.
name|setHeader
argument_list|(
name|HazelcastConstants
operator|.
name|OPERATION
argument_list|,
name|constant
argument_list|(
name|HazelcastConstants
operator|.
name|POLL_OPERATION
argument_list|)
argument_list|)
operator|.
name|toF
argument_list|(
literal|"hazelcast:queue:myqueue"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Configuration
DECL|method|configure ()
specifier|public
specifier|static
name|Option
index|[]
name|configure
parameter_list|()
block|{
name|Option
index|[]
name|options
init|=
name|combine
argument_list|(
name|getDefaultCamelKarafOptions
argument_list|()
argument_list|,
comment|// using the features to install the other camel components
name|loadCamelFeatures
argument_list|(
literal|"camel-hazelcast"
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|options
return|;
block|}
block|}
end_class

end_unit

