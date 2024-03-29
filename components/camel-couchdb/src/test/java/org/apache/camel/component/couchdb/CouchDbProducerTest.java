begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.couchdb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|couchdb
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|UUID
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gson
operator|.
name|JsonElement
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gson
operator|.
name|JsonObject
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
name|InvalidPayloadException
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
name|Message
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
name|lightcouch
operator|.
name|Response
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|Mock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|invocation
operator|.
name|InvocationOnMock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|junit
operator|.
name|MockitoJUnitRunner
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|stubbing
operator|.
name|Answer
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|ArgumentMatchers
operator|.
name|any
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|verify
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|when
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|MockitoJUnitRunner
operator|.
name|class
argument_list|)
DECL|class|CouchDbProducerTest
specifier|public
class|class
name|CouchDbProducerTest
block|{
annotation|@
name|Mock
DECL|field|client
specifier|private
name|CouchDbClientWrapper
name|client
decl_stmt|;
annotation|@
name|Mock
DECL|field|endpoint
specifier|private
name|CouchDbEndpoint
name|endpoint
decl_stmt|;
annotation|@
name|Mock
DECL|field|exchange
specifier|private
name|Exchange
name|exchange
decl_stmt|;
annotation|@
name|Mock
DECL|field|msg
specifier|private
name|Message
name|msg
decl_stmt|;
annotation|@
name|Mock
DECL|field|response
specifier|private
name|Response
name|response
decl_stmt|;
DECL|field|producer
specifier|private
name|CouchDbProducer
name|producer
decl_stmt|;
annotation|@
name|Before
DECL|method|before ()
specifier|public
name|void
name|before
parameter_list|()
block|{
name|producer
operator|=
operator|new
name|CouchDbProducer
argument_list|(
name|endpoint
argument_list|,
name|client
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|InvalidPayloadException
operator|.
name|class
argument_list|)
DECL|method|testBodyMandatory ()
specifier|public
name|void
name|testBodyMandatory
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|msg
operator|.
name|getMandatoryBody
argument_list|()
argument_list|)
operator|.
name|thenThrow
argument_list|(
name|InvalidPayloadException
operator|.
name|class
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDocumentHeadersAreSet ()
specifier|public
name|void
name|testDocumentHeadersAreSet
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|id
init|=
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|String
name|rev
init|=
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|JsonObject
name|doc
init|=
operator|new
name|JsonObject
argument_list|()
decl_stmt|;
name|doc
operator|.
name|addProperty
argument_list|(
literal|"_id"
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|doc
operator|.
name|addProperty
argument_list|(
literal|"_rev"
argument_list|,
name|rev
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|msg
operator|.
name|getMandatoryBody
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|doc
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|client
operator|.
name|update
argument_list|(
name|doc
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|response
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|response
operator|.
name|getRev
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|rev
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|msg
argument_list|)
operator|.
name|setHeader
argument_list|(
name|CouchDbConstants
operator|.
name|HEADER_DOC_ID
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|msg
argument_list|)
operator|.
name|setHeader
argument_list|(
name|CouchDbConstants
operator|.
name|HEADER_DOC_REV
argument_list|,
name|rev
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|InvalidPayloadException
operator|.
name|class
argument_list|)
DECL|method|testNullSaveResponseThrowsError ()
specifier|public
name|void
name|testNullSaveResponseThrowsError
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMandatoryBody
argument_list|()
argument_list|)
operator|.
name|thenThrow
argument_list|(
name|InvalidPayloadException
operator|.
name|class
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|producer
operator|.
name|getBodyAsJsonElement
argument_list|(
name|exchange
argument_list|)
argument_list|)
operator|.
name|thenThrow
argument_list|(
name|InvalidPayloadException
operator|.
name|class
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDeleteResponse ()
specifier|public
name|void
name|testDeleteResponse
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|id
init|=
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|String
name|rev
init|=
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|JsonObject
name|doc
init|=
operator|new
name|JsonObject
argument_list|()
decl_stmt|;
name|doc
operator|.
name|addProperty
argument_list|(
literal|"_id"
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|doc
operator|.
name|addProperty
argument_list|(
literal|"_rev"
argument_list|,
name|rev
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|msg
operator|.
name|getHeader
argument_list|(
name|CouchDbConstants
operator|.
name|HEADER_METHOD
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"DELETE"
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|msg
operator|.
name|getMandatoryBody
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|doc
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|client
operator|.
name|remove
argument_list|(
name|doc
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|response
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|response
operator|.
name|getRev
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|rev
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|msg
argument_list|)
operator|.
name|setHeader
argument_list|(
name|CouchDbConstants
operator|.
name|HEADER_DOC_ID
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|msg
argument_list|)
operator|.
name|setHeader
argument_list|(
name|CouchDbConstants
operator|.
name|HEADER_DOC_REV
argument_list|,
name|rev
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetResponse ()
specifier|public
name|void
name|testGetResponse
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|id
init|=
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|JsonObject
name|doc
init|=
operator|new
name|JsonObject
argument_list|()
decl_stmt|;
name|doc
operator|.
name|addProperty
argument_list|(
literal|"_id"
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|msg
operator|.
name|getHeader
argument_list|(
name|CouchDbConstants
operator|.
name|HEADER_METHOD
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"GET"
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|msg
operator|.
name|getHeader
argument_list|(
name|CouchDbConstants
operator|.
name|HEADER_DOC_ID
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|msg
operator|.
name|getMandatoryBody
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|doc
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|client
operator|.
name|get
argument_list|(
name|id
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|msg
argument_list|)
operator|.
name|getHeader
argument_list|(
name|CouchDbConstants
operator|.
name|HEADER_DOC_ID
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testStringBodyIsConvertedToJsonTree ()
specifier|public
name|void
name|testStringBodyIsConvertedToJsonTree
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|msg
operator|.
name|getMandatoryBody
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"{ \"name\" : \"coldplay\" }"
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|client
operator|.
name|save
argument_list|(
name|any
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenAnswer
argument_list|(
operator|new
name|Answer
argument_list|<
name|Response
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Response
name|answer
parameter_list|(
name|InvocationOnMock
name|invocation
parameter_list|)
throws|throws
name|Throwable
block|{
name|assertTrue
argument_list|(
name|invocation
operator|.
name|getArguments
argument_list|()
index|[
literal|0
index|]
operator|.
name|getClass
argument_list|()
operator|+
literal|" but wanted "
operator|+
name|JsonElement
operator|.
name|class
argument_list|,
name|invocation
operator|.
name|getArguments
argument_list|()
index|[
literal|0
index|]
operator|instanceof
name|JsonElement
argument_list|)
expr_stmt|;
return|return
operator|new
name|Response
argument_list|()
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|client
argument_list|)
operator|.
name|save
argument_list|(
name|any
argument_list|(
name|JsonObject
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

