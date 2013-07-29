begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|AvroRuntimeException
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
name|Requestor
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
name|Transceiver
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
name|TestPojo
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
name|TestReflection
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
DECL|class|AvroConsumerTestSupport
specifier|public
specifier|abstract
class|class
name|AvroConsumerTestSupport
extends|extends
name|AvroTestSupport
block|{
DECL|field|avroPortMessageInRoute
specifier|protected
name|int
name|avroPortMessageInRoute
init|=
name|setupFreePort
argument_list|(
literal|"avroPortMessageInRoute"
argument_list|)
decl_stmt|;
DECL|field|avroPortForWrongMessages
specifier|protected
name|int
name|avroPortForWrongMessages
init|=
name|setupFreePort
argument_list|(
literal|"avroPortForWrongMessages"
argument_list|)
decl_stmt|;
DECL|field|transceiver
name|Transceiver
name|transceiver
decl_stmt|;
DECL|field|requestor
name|Requestor
name|requestor
decl_stmt|;
DECL|field|transceiverMessageInRoute
name|Transceiver
name|transceiverMessageInRoute
decl_stmt|;
DECL|field|requestorMessageInRoute
name|Requestor
name|requestorMessageInRoute
decl_stmt|;
DECL|field|transceiverForWrongMessages
name|Transceiver
name|transceiverForWrongMessages
decl_stmt|;
DECL|field|requestorForWrongMessages
name|Requestor
name|requestorForWrongMessages
decl_stmt|;
DECL|field|reflectTransceiver
name|Transceiver
name|reflectTransceiver
decl_stmt|;
DECL|field|reflectRequestor
name|Requestor
name|reflectRequestor
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
name|TestReflection
name|testReflection
init|=
operator|new
name|TestReflectionImpl
argument_list|()
decl_stmt|;
DECL|field|REFLECTION_TEST_NAME
specifier|public
specifier|static
specifier|final
name|String
name|REFLECTION_TEST_NAME
init|=
literal|"Chucky"
decl_stmt|;
DECL|field|REFLECTION_TEST_AGE
specifier|public
specifier|static
specifier|final
name|int
name|REFLECTION_TEST_AGE
init|=
literal|100
decl_stmt|;
DECL|method|initializeTranceiver ()
specifier|protected
specifier|abstract
name|void
name|initializeTranceiver
parameter_list|()
throws|throws
name|IOException
function_decl|;
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
name|transceiver
operator|!=
literal|null
condition|)
block|{
name|transceiver
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|transceiverMessageInRoute
operator|!=
literal|null
condition|)
block|{
name|transceiverMessageInRoute
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|transceiverForWrongMessages
operator|!=
literal|null
condition|)
block|{
name|transceiverForWrongMessages
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|reflectTransceiver
operator|!=
literal|null
condition|)
block|{
name|reflectTransceiver
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
name|Exception
block|{
name|initializeTranceiver
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
name|requestor
operator|.
name|request
argument_list|(
literal|"put"
argument_list|,
name|request
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInOnlyMessageInRoute ()
specifier|public
name|void
name|testInOnlyMessageInRoute
parameter_list|()
throws|throws
name|Exception
block|{
name|initializeTranceiver
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
name|requestorMessageInRoute
operator|.
name|request
argument_list|(
literal|"put"
argument_list|,
name|request
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInOnlyReflectRequestor ()
specifier|public
name|void
name|testInOnlyReflectRequestor
parameter_list|()
throws|throws
name|Exception
block|{
name|initializeTranceiver
argument_list|()
expr_stmt|;
name|Object
index|[]
name|request
init|=
block|{
name|REFLECTION_TEST_NAME
block|}
decl_stmt|;
name|reflectRequestor
operator|.
name|request
argument_list|(
literal|"setName"
argument_list|,
name|request
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|REFLECTION_TEST_NAME
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
argument_list|(
name|expected
operator|=
name|AvroRuntimeException
operator|.
name|class
argument_list|)
DECL|method|testInOnlyWrongMessageName ()
specifier|public
name|void
name|testInOnlyWrongMessageName
parameter_list|()
throws|throws
name|Exception
block|{
name|initializeTranceiver
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
name|requestorMessageInRoute
operator|.
name|request
argument_list|(
literal|"throwException"
argument_list|,
name|request
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|AvroRuntimeException
operator|.
name|class
argument_list|)
DECL|method|testInOnlyToNotExistingRoute ()
specifier|public
name|void
name|testInOnlyToNotExistingRoute
parameter_list|()
throws|throws
name|Exception
block|{
name|initializeTranceiver
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
name|requestorForWrongMessages
operator|.
name|request
argument_list|(
literal|"get"
argument_list|,
name|request
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInOnlyReflectSingleParameterNotSet ()
specifier|public
name|void
name|testInOnlyReflectSingleParameterNotSet
parameter_list|()
throws|throws
name|Exception
block|{
name|initializeTranceiver
argument_list|()
expr_stmt|;
name|Object
index|[]
name|request
init|=
block|{
literal|100
block|}
decl_stmt|;
name|reflectRequestor
operator|.
name|request
argument_list|(
literal|"setAge"
argument_list|,
name|request
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|testReflection
operator|.
name|getAge
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInOnlyReflectionPojoTest ()
specifier|public
name|void
name|testInOnlyReflectionPojoTest
parameter_list|()
throws|throws
name|Exception
block|{
name|initializeTranceiver
argument_list|()
expr_stmt|;
name|TestPojo
name|testPojo
init|=
operator|new
name|TestPojo
argument_list|()
decl_stmt|;
name|testPojo
operator|.
name|setPojoName
argument_list|(
literal|"pojo1"
argument_list|)
expr_stmt|;
name|Object
index|[]
name|request
init|=
block|{
name|testPojo
block|}
decl_stmt|;
name|reflectRequestor
operator|.
name|request
argument_list|(
literal|"setTestPojo"
argument_list|,
name|request
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|testPojo
operator|.
name|getPojoName
argument_list|()
argument_list|,
name|testReflection
operator|.
name|getTestPojo
argument_list|()
operator|.
name|getPojoName
argument_list|()
argument_list|)
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
name|Exception
block|{
name|initializeTranceiver
argument_list|()
expr_stmt|;
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
name|Object
index|[]
name|request
init|=
block|{
name|key
block|}
decl_stmt|;
name|Object
name|response
init|=
name|requestor
operator|.
name|request
argument_list|(
literal|"get"
argument_list|,
name|request
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|value
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInOutMessageInRoute ()
specifier|public
name|void
name|testInOutMessageInRoute
parameter_list|()
throws|throws
name|Exception
block|{
name|initializeTranceiver
argument_list|()
expr_stmt|;
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
name|Object
index|[]
name|request
init|=
block|{
name|key
block|}
decl_stmt|;
name|Object
name|response
init|=
name|requestorMessageInRoute
operator|.
name|request
argument_list|(
literal|"get"
argument_list|,
name|request
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|value
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInOutReflectRequestor ()
specifier|public
name|void
name|testInOutReflectRequestor
parameter_list|()
throws|throws
name|Exception
block|{
name|initializeTranceiver
argument_list|()
expr_stmt|;
name|Object
index|[]
name|request
init|=
block|{
name|REFLECTION_TEST_AGE
block|}
decl_stmt|;
name|Object
name|response
init|=
name|reflectRequestor
operator|.
name|request
argument_list|(
literal|"increaseAge"
argument_list|,
name|request
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|testReflection
operator|.
name|getAge
argument_list|()
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInOutReflectionPojoTest ()
specifier|public
name|void
name|testInOutReflectionPojoTest
parameter_list|()
throws|throws
name|Exception
block|{
name|initializeTranceiver
argument_list|()
expr_stmt|;
name|TestPojo
name|testPojo
init|=
operator|new
name|TestPojo
argument_list|()
decl_stmt|;
name|testPojo
operator|.
name|setPojoName
argument_list|(
literal|"pojo2"
argument_list|)
expr_stmt|;
name|Object
index|[]
name|request
init|=
block|{
name|testPojo
block|}
decl_stmt|;
name|reflectRequestor
operator|.
name|request
argument_list|(
literal|"setTestPojo"
argument_list|,
name|request
argument_list|)
expr_stmt|;
name|request
operator|=
operator|new
name|Object
index|[
literal|0
index|]
expr_stmt|;
name|Object
name|response
init|=
name|reflectRequestor
operator|.
name|request
argument_list|(
literal|"getTestPojo"
argument_list|,
name|request
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|testPojo
operator|.
name|getPojoName
argument_list|()
argument_list|,
operator|(
operator|(
name|TestPojo
operator|)
name|response
operator|)
operator|.
name|getPojoName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

