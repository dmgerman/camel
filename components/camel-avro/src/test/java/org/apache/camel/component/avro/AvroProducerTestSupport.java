begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.avro
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|avro
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|avro
operator|.
name|ipc
operator|.
name|Server
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
name|avro
operator|.
name|generated
operator|.
name|Key
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
name|avro
operator|.
name|generated
operator|.
name|Value
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
name|avro
operator|.
name|impl
operator|.
name|KeyValueProtocolImpl
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
name|avro
operator|.
name|test
operator|.
name|TestReflectionImpl
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
name|junit
operator|.
name|After
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
DECL|class|AvroProducerTestSupport
specifier|public
specifier|abstract
class|class
name|AvroProducerTestSupport
extends|extends
name|AvroTestSupport
block|{
DECL|field|server
name|Server
name|server
decl_stmt|;
DECL|field|serverReflection
name|Server
name|serverReflection
decl_stmt|;
DECL|field|keyValue
name|KeyValueProtocolImpl
name|keyValue
init|=
operator|new
name|KeyValueProtocolImpl
argument_list|()
decl_stmt|;
DECL|field|testReflection
name|TestReflectionImpl
name|testReflection
init|=
operator|new
name|TestReflectionImpl
argument_list|()
decl_stmt|;
DECL|method|initializeServer ()
specifier|protected
specifier|abstract
name|void
name|initializeServer
parameter_list|()
throws|throws
name|IOException
function_decl|;
annotation|@
name|Override
DECL|method|doPreSetup ()
specifier|protected
name|void
name|doPreSetup
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doPreSetup
argument_list|()
expr_stmt|;
name|initializeServer
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
if|if
condition|(
name|server
operator|!=
literal|null
condition|)
block|{
name|server
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|serverReflection
operator|!=
literal|null
condition|)
block|{
name|serverReflection
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testInOnly ()
specifier|public
name|void
name|testInOnly
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|Key
name|key
init|=
name|Key
operator|.
name|newBuilder
argument_list|()
operator|.
name|setKey
argument_list|(
literal|"1"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|Value
name|value
init|=
name|Value
operator|.
name|newBuilder
argument_list|()
operator|.
name|setValue
argument_list|(
literal|"test value"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|Object
index|[]
name|request
init|=
block|{
name|key
block|,
name|value
block|}
decl_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:in"
argument_list|,
name|request
argument_list|,
name|AvroConstants
operator|.
name|AVRO_MESSAGE_NAME
argument_list|,
literal|"put"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|value
argument_list|,
name|keyValue
operator|.
name|getStore
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInOnlyWithMessageNameInRoute ()
specifier|public
name|void
name|testInOnlyWithMessageNameInRoute
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result-in-message-name"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Key
name|key
init|=
name|Key
operator|.
name|newBuilder
argument_list|()
operator|.
name|setKey
argument_list|(
literal|"1"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|Value
name|value
init|=
name|Value
operator|.
name|newBuilder
argument_list|()
operator|.
name|setValue
argument_list|(
literal|"test value"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|Object
index|[]
name|request
init|=
block|{
name|key
block|,
name|value
block|}
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:in-message-name"
argument_list|,
name|request
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|value
argument_list|,
name|keyValue
operator|.
name|getStore
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInOnlyReflection ()
specifier|public
name|void
name|testInOnlyReflection
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|String
name|name
init|=
literal|"Chuck"
decl_stmt|;
name|Object
index|[]
name|request
init|=
block|{
name|name
block|}
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:in-reflection"
argument_list|,
name|request
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|name
argument_list|,
name|testReflection
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInOnlyWithWrongMessageNameInMessage ()
specifier|public
name|void
name|testInOnlyWithWrongMessageNameInMessage
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|MockEndpoint
name|mockInMessageEnd
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result-in-message-name"
argument_list|)
decl_stmt|;
name|mockInMessageEnd
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mockErrorChannel
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:in-message-name-error"
argument_list|)
decl_stmt|;
name|mockErrorChannel
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Key
name|key
init|=
name|Key
operator|.
name|newBuilder
argument_list|()
operator|.
name|setKey
argument_list|(
literal|"1"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|Value
name|value
init|=
name|Value
operator|.
name|newBuilder
argument_list|()
operator|.
name|setValue
argument_list|(
literal|"test value"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|Object
index|[]
name|request
init|=
block|{
name|key
block|,
name|value
block|}
decl_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:in-message-name"
argument_list|,
name|request
argument_list|,
name|AvroConstants
operator|.
name|AVRO_MESSAGE_NAME
argument_list|,
literal|"/get"
argument_list|)
expr_stmt|;
name|mockErrorChannel
operator|.
name|assertIsSatisfied
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
name|mockInMessageEnd
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInOut ()
specifier|public
name|void
name|testInOut
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|keyValue
operator|.
name|getStore
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|Key
name|key
init|=
name|Key
operator|.
name|newBuilder
argument_list|()
operator|.
name|setKey
argument_list|(
literal|"2"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|Value
name|value
init|=
name|Value
operator|.
name|newBuilder
argument_list|()
operator|.
name|setValue
argument_list|(
literal|"test value"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|keyValue
operator|.
name|getStore
argument_list|()
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result-inout"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:inout"
argument_list|,
name|key
argument_list|,
name|AvroConstants
operator|.
name|AVRO_MESSAGE_NAME
argument_list|,
literal|"get"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInOutMessageNameInRoute ()
specifier|public
name|void
name|testInOutMessageNameInRoute
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|keyValue
operator|.
name|getStore
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|Key
name|key
init|=
name|Key
operator|.
name|newBuilder
argument_list|()
operator|.
name|setKey
argument_list|(
literal|"2"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|Value
name|value
init|=
name|Value
operator|.
name|newBuilder
argument_list|()
operator|.
name|setValue
argument_list|(
literal|"test value"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|keyValue
operator|.
name|getStore
argument_list|()
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result-inout-message-name"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:inout-message-name"
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInOutReflection ()
specifier|public
name|void
name|testInOutReflection
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|int
name|age
init|=
literal|100
decl_stmt|;
name|Object
index|[]
name|request
init|=
block|{
name|age
block|}
decl_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result-inout-reflection"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
operator|++
name|age
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:inout-reflection"
argument_list|,
name|request
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

