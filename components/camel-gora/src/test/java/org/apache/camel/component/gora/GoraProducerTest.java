begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.gora
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|gora
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
name|ExchangePattern
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
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|gora
operator|.
name|utils
operator|.
name|GoraUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|gora
operator|.
name|persistency
operator|.
name|Persistent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|gora
operator|.
name|persistency
operator|.
name|StateManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|gora
operator|.
name|query
operator|.
name|Query
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|gora
operator|.
name|query
operator|.
name|impl
operator|.
name|QueryBase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|gora
operator|.
name|store
operator|.
name|DataStore
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
name|Ignore
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
name|internal
operator|.
name|verification
operator|.
name|VerificationModeFactory
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
name|OngoingStubbing
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|verification
operator|.
name|VerificationMode
import|;
end_import

begin_import
import|import
name|org
operator|.
name|powermock
operator|.
name|api
operator|.
name|mockito
operator|.
name|PowerMockito
import|;
end_import

begin_import
import|import
name|org
operator|.
name|powermock
operator|.
name|core
operator|.
name|classloader
operator|.
name|annotations
operator|.
name|PrepareForTest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|powermock
operator|.
name|modules
operator|.
name|junit4
operator|.
name|PowerMockRunner
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|*
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
name|atLeastOnce
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
name|atMost
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
name|internal
operator|.
name|verification
operator|.
name|VerificationModeFactory
operator|.
name|times
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|powermock
operator|.
name|api
operator|.
name|mockito
operator|.
name|PowerMockito
operator|.
name|mockStatic
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|powermock
operator|.
name|api
operator|.
name|mockito
operator|.
name|PowerMockito
operator|.
name|mock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|powermock
operator|.
name|api
operator|.
name|mockito
operator|.
name|PowerMockito
operator|.
name|when
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|powermock
operator|.
name|api
operator|.
name|mockito
operator|.
name|PowerMockito
operator|.
name|verifyStatic
import|;
end_import

begin_comment
comment|/**  * GORA Producer Tests  *  * TODO:<b>NOTE:</b> Query methods does not yet has tests  *  * @author ipolyzos  */
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|PowerMockRunner
operator|.
name|class
argument_list|)
annotation|@
name|PrepareForTest
argument_list|(
name|GoraUtils
operator|.
name|class
argument_list|)
DECL|class|GoraProducerTest
specifier|public
class|class
name|GoraProducerTest
extends|extends
name|GoraTestSupport
block|{
comment|/**      * Mock CamelExchange      */
DECL|field|mockCamelExchange
specifier|private
name|Exchange
name|mockCamelExchange
decl_stmt|;
comment|/**      * Mock Gora Endpoint      */
DECL|field|mockGoraEndpoint
specifier|private
name|GoraEndpoint
name|mockGoraEndpoint
decl_stmt|;
comment|/**      * Mock Gora Configuration      */
DECL|field|mockGoraConfiguration
specifier|private
name|GoraConfiguration
name|mockGoraConfiguration
decl_stmt|;
comment|/**      * Mock Camel Message      */
DECL|field|mockCamelMessage
specifier|private
name|Message
name|mockCamelMessage
decl_stmt|;
comment|/**      * Mock Gora DataStore      */
DECL|field|mockDatastore
specifier|private
name|DataStore
name|mockDatastore
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
block|{
comment|//setup mocks
name|mockCamelExchange
operator|=
name|mock
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
expr_stmt|;
name|mockGoraEndpoint
operator|=
name|mock
argument_list|(
name|GoraEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|mockGoraConfiguration
operator|=
name|mock
argument_list|(
name|GoraConfiguration
operator|.
name|class
argument_list|)
expr_stmt|;
name|mockCamelMessage
operator|=
name|mock
argument_list|(
name|Message
operator|.
name|class
argument_list|)
expr_stmt|;
name|mockDatastore
operator|=
name|mock
argument_list|(
name|DataStore
operator|.
name|class
argument_list|)
expr_stmt|;
comment|//setup default conditions
name|when
argument_list|(
name|mockCamelExchange
operator|.
name|getIn
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|mockCamelMessage
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|mockCamelExchange
operator|.
name|getPattern
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|RuntimeException
operator|.
name|class
argument_list|)
DECL|method|processShouldThrowExceptionIfOperationIsNull ()
specifier|public
name|void
name|processShouldThrowExceptionIfOperationIsNull
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|GoraProducer
name|producer
init|=
operator|new
name|GoraProducer
argument_list|(
name|mockGoraEndpoint
argument_list|,
name|mockGoraConfiguration
argument_list|,
name|mockDatastore
argument_list|)
decl_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|mockCamelExchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|RuntimeException
operator|.
name|class
argument_list|)
DECL|method|shouldThrowExceptionIfOperationIsUnknown ()
specifier|public
name|void
name|shouldThrowExceptionIfOperationIsUnknown
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|mockCamelExchange
operator|.
name|getIn
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|mockCamelMessage
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|mockCamelMessage
operator|.
name|getHeader
argument_list|(
name|GoraAttribute
operator|.
name|GORA_OPERATION
operator|.
name|value
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"dah"
argument_list|)
expr_stmt|;
specifier|final
name|GoraProducer
name|producer
init|=
operator|new
name|GoraProducer
argument_list|(
name|mockGoraEndpoint
argument_list|,
name|mockGoraConfiguration
argument_list|,
name|mockDatastore
argument_list|)
decl_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|mockCamelExchange
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|mockCamelExchange
argument_list|,
name|atMost
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getIn
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|mockCamelMessage
argument_list|,
name|atMost
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getHeader
argument_list|(
name|GoraAttribute
operator|.
name|GORA_OPERATION
operator|.
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldInvokeDastorePut ()
specifier|public
name|void
name|shouldInvokeDastorePut
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|mockCamelExchange
operator|.
name|getIn
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|mockCamelMessage
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|mockCamelMessage
operator|.
name|getHeader
argument_list|(
name|GoraAttribute
operator|.
name|GORA_OPERATION
operator|.
name|value
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"PUT"
argument_list|)
expr_stmt|;
specifier|final
name|Long
name|sampleKey
init|=
operator|new
name|Long
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|mockCamelMessage
operator|.
name|getHeader
argument_list|(
name|GoraAttribute
operator|.
name|GORA_KEY
operator|.
name|value
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|sampleKey
argument_list|)
expr_stmt|;
specifier|final
name|Persistent
name|sampleValue
init|=
name|mock
argument_list|(
name|Persistent
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|mockCamelMessage
operator|.
name|getBody
argument_list|(
name|Persistent
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|sampleValue
argument_list|)
expr_stmt|;
specifier|final
name|Message
name|outMessage
init|=
name|mock
argument_list|(
name|Message
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|mockCamelExchange
operator|.
name|getOut
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|outMessage
argument_list|)
expr_stmt|;
specifier|final
name|GoraProducer
name|producer
init|=
operator|new
name|GoraProducer
argument_list|(
name|mockGoraEndpoint
argument_list|,
name|mockGoraConfiguration
argument_list|,
name|mockDatastore
argument_list|)
decl_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|mockCamelExchange
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|mockCamelExchange
argument_list|,
name|atLeastOnce
argument_list|()
argument_list|)
operator|.
name|getIn
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|mockCamelMessage
argument_list|,
name|atLeastOnce
argument_list|()
argument_list|)
operator|.
name|getHeader
argument_list|(
name|GoraAttribute
operator|.
name|GORA_OPERATION
operator|.
name|value
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|mockCamelMessage
argument_list|,
name|atLeastOnce
argument_list|()
argument_list|)
operator|.
name|getHeader
argument_list|(
name|GoraAttribute
operator|.
name|GORA_KEY
operator|.
name|value
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|mockCamelMessage
argument_list|,
name|atLeastOnce
argument_list|()
argument_list|)
operator|.
name|getBody
argument_list|(
name|Persistent
operator|.
name|class
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|mockDatastore
argument_list|,
name|atMost
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|put
argument_list|(
name|sampleKey
argument_list|,
name|sampleValue
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldInvokeDastoreGet ()
specifier|public
name|void
name|shouldInvokeDastoreGet
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|mockCamelExchange
operator|.
name|getIn
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|mockCamelMessage
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|mockCamelMessage
operator|.
name|getHeader
argument_list|(
name|GoraAttribute
operator|.
name|GORA_OPERATION
operator|.
name|value
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"get"
argument_list|)
expr_stmt|;
specifier|final
name|Long
name|sampleKey
init|=
operator|new
name|Long
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|mockCamelMessage
operator|.
name|getHeader
argument_list|(
name|GoraAttribute
operator|.
name|GORA_KEY
operator|.
name|value
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|sampleKey
argument_list|)
expr_stmt|;
specifier|final
name|Message
name|outMessage
init|=
name|mock
argument_list|(
name|Message
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|mockCamelExchange
operator|.
name|getOut
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|outMessage
argument_list|)
expr_stmt|;
specifier|final
name|GoraProducer
name|producer
init|=
operator|new
name|GoraProducer
argument_list|(
name|mockGoraEndpoint
argument_list|,
name|mockGoraConfiguration
argument_list|,
name|mockDatastore
argument_list|)
decl_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|mockCamelExchange
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|mockCamelExchange
argument_list|,
name|atLeastOnce
argument_list|()
argument_list|)
operator|.
name|getIn
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|mockCamelMessage
argument_list|,
name|atLeastOnce
argument_list|()
argument_list|)
operator|.
name|getHeader
argument_list|(
name|GoraAttribute
operator|.
name|GORA_OPERATION
operator|.
name|value
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|mockCamelMessage
argument_list|,
name|atLeastOnce
argument_list|()
argument_list|)
operator|.
name|getHeader
argument_list|(
name|GoraAttribute
operator|.
name|GORA_KEY
operator|.
name|value
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|mockDatastore
argument_list|,
name|atMost
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|get
argument_list|(
name|sampleKey
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldInvokeDatastoreDelete ()
specifier|public
name|void
name|shouldInvokeDatastoreDelete
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|mockCamelExchange
operator|.
name|getIn
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|mockCamelMessage
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|mockCamelMessage
operator|.
name|getHeader
argument_list|(
name|GoraAttribute
operator|.
name|GORA_OPERATION
operator|.
name|value
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"dEletE"
argument_list|)
expr_stmt|;
specifier|final
name|Long
name|sampleKey
init|=
operator|new
name|Long
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|mockCamelMessage
operator|.
name|getHeader
argument_list|(
name|GoraAttribute
operator|.
name|GORA_KEY
operator|.
name|value
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|sampleKey
argument_list|)
expr_stmt|;
specifier|final
name|Message
name|outMessage
init|=
name|mock
argument_list|(
name|Message
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|mockCamelExchange
operator|.
name|getOut
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|outMessage
argument_list|)
expr_stmt|;
specifier|final
name|GoraProducer
name|producer
init|=
operator|new
name|GoraProducer
argument_list|(
name|mockGoraEndpoint
argument_list|,
name|mockGoraConfiguration
argument_list|,
name|mockDatastore
argument_list|)
decl_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|mockCamelExchange
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|mockCamelExchange
argument_list|,
name|atLeastOnce
argument_list|()
argument_list|)
operator|.
name|getIn
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|mockCamelMessage
argument_list|,
name|atLeastOnce
argument_list|()
argument_list|)
operator|.
name|getHeader
argument_list|(
name|GoraAttribute
operator|.
name|GORA_OPERATION
operator|.
name|value
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|mockCamelMessage
argument_list|,
name|atLeastOnce
argument_list|()
argument_list|)
operator|.
name|getHeader
argument_list|(
name|GoraAttribute
operator|.
name|GORA_KEY
operator|.
name|value
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|mockDatastore
argument_list|,
name|atMost
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|delete
argument_list|(
name|sampleKey
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldInvokeDastoreSchemaExists ()
specifier|public
name|void
name|shouldInvokeDastoreSchemaExists
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|mockCamelExchange
operator|.
name|getIn
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|mockCamelMessage
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|mockCamelMessage
operator|.
name|getHeader
argument_list|(
name|GoraAttribute
operator|.
name|GORA_OPERATION
operator|.
name|value
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"schemaExists"
argument_list|)
expr_stmt|;
specifier|final
name|Message
name|outMessage
init|=
name|mock
argument_list|(
name|Message
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|mockCamelExchange
operator|.
name|getOut
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|outMessage
argument_list|)
expr_stmt|;
specifier|final
name|GoraProducer
name|producer
init|=
operator|new
name|GoraProducer
argument_list|(
name|mockGoraEndpoint
argument_list|,
name|mockGoraConfiguration
argument_list|,
name|mockDatastore
argument_list|)
decl_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|mockCamelExchange
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|mockCamelExchange
argument_list|,
name|atLeastOnce
argument_list|()
argument_list|)
operator|.
name|getIn
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|mockCamelMessage
argument_list|,
name|atLeastOnce
argument_list|()
argument_list|)
operator|.
name|getHeader
argument_list|(
name|GoraAttribute
operator|.
name|GORA_OPERATION
operator|.
name|value
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|mockDatastore
argument_list|,
name|atMost
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|schemaExists
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldInvokeDastoreCreateSchema ()
specifier|public
name|void
name|shouldInvokeDastoreCreateSchema
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|mockCamelExchange
operator|.
name|getIn
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|mockCamelMessage
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|mockCamelMessage
operator|.
name|getHeader
argument_list|(
name|GoraAttribute
operator|.
name|GORA_OPERATION
operator|.
name|value
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"createSchema"
argument_list|)
expr_stmt|;
specifier|final
name|Message
name|outMessage
init|=
name|mock
argument_list|(
name|Message
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|mockCamelExchange
operator|.
name|getOut
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|outMessage
argument_list|)
expr_stmt|;
specifier|final
name|GoraProducer
name|producer
init|=
operator|new
name|GoraProducer
argument_list|(
name|mockGoraEndpoint
argument_list|,
name|mockGoraConfiguration
argument_list|,
name|mockDatastore
argument_list|)
decl_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|mockCamelExchange
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|mockCamelExchange
argument_list|,
name|atLeastOnce
argument_list|()
argument_list|)
operator|.
name|getIn
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|mockCamelMessage
argument_list|,
name|atLeastOnce
argument_list|()
argument_list|)
operator|.
name|getHeader
argument_list|(
name|GoraAttribute
operator|.
name|GORA_OPERATION
operator|.
name|value
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|mockDatastore
argument_list|,
name|atMost
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|createSchema
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldInvokeDastoreGetSchemaName ()
specifier|public
name|void
name|shouldInvokeDastoreGetSchemaName
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|mockCamelExchange
operator|.
name|getIn
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|mockCamelMessage
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|mockCamelMessage
operator|.
name|getHeader
argument_list|(
name|GoraAttribute
operator|.
name|GORA_OPERATION
operator|.
name|value
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"GetSchemANamE"
argument_list|)
expr_stmt|;
specifier|final
name|Message
name|outMessage
init|=
name|mock
argument_list|(
name|Message
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|mockCamelExchange
operator|.
name|getOut
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|outMessage
argument_list|)
expr_stmt|;
specifier|final
name|GoraProducer
name|producer
init|=
operator|new
name|GoraProducer
argument_list|(
name|mockGoraEndpoint
argument_list|,
name|mockGoraConfiguration
argument_list|,
name|mockDatastore
argument_list|)
decl_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|mockCamelExchange
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|mockCamelExchange
argument_list|,
name|atLeastOnce
argument_list|()
argument_list|)
operator|.
name|getIn
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|mockCamelMessage
argument_list|,
name|atLeastOnce
argument_list|()
argument_list|)
operator|.
name|getHeader
argument_list|(
name|GoraAttribute
operator|.
name|GORA_OPERATION
operator|.
name|value
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|mockDatastore
argument_list|,
name|atMost
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getSchemaName
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldInvokeDatastoreDeleteSchema ()
specifier|public
name|void
name|shouldInvokeDatastoreDeleteSchema
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|mockCamelExchange
operator|.
name|getIn
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|mockCamelMessage
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|mockCamelMessage
operator|.
name|getHeader
argument_list|(
name|GoraAttribute
operator|.
name|GORA_OPERATION
operator|.
name|value
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"DeleteSChEmA"
argument_list|)
expr_stmt|;
specifier|final
name|Message
name|outMessage
init|=
name|mock
argument_list|(
name|Message
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|mockCamelExchange
operator|.
name|getOut
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|outMessage
argument_list|)
expr_stmt|;
specifier|final
name|GoraProducer
name|producer
init|=
operator|new
name|GoraProducer
argument_list|(
name|mockGoraEndpoint
argument_list|,
name|mockGoraConfiguration
argument_list|,
name|mockDatastore
argument_list|)
decl_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|mockCamelExchange
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|mockCamelExchange
argument_list|,
name|atLeastOnce
argument_list|()
argument_list|)
operator|.
name|getIn
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|mockCamelMessage
argument_list|,
name|atLeastOnce
argument_list|()
argument_list|)
operator|.
name|getHeader
argument_list|(
name|GoraAttribute
operator|.
name|GORA_OPERATION
operator|.
name|value
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|mockDatastore
argument_list|,
name|atMost
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|deleteSchema
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldInvokeDatastoreQuery ()
specifier|public
name|void
name|shouldInvokeDatastoreQuery
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|mockCamelExchange
operator|.
name|getIn
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|mockCamelMessage
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|mockCamelMessage
operator|.
name|getHeader
argument_list|(
name|GoraAttribute
operator|.
name|GORA_OPERATION
operator|.
name|value
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"query"
argument_list|)
expr_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|mockProperties
init|=
name|mock
argument_list|(
name|Map
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|mockCamelMessage
operator|.
name|getHeaders
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|mockProperties
argument_list|)
expr_stmt|;
specifier|final
name|Message
name|outMessage
init|=
name|mock
argument_list|(
name|Message
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|mockCamelExchange
operator|.
name|getOut
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|outMessage
argument_list|)
expr_stmt|;
name|mockStatic
argument_list|(
name|GoraUtils
operator|.
name|class
argument_list|)
expr_stmt|;
specifier|final
name|Query
name|mockQuery
init|=
name|mock
argument_list|(
name|QueryBase
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|GoraUtils
operator|.
name|constractQueryFromPropertiesMap
argument_list|(
name|mockProperties
argument_list|,
name|mockDatastore
argument_list|,
name|mockGoraConfiguration
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|mockQuery
argument_list|)
expr_stmt|;
specifier|final
name|GoraProducer
name|producer
init|=
operator|new
name|GoraProducer
argument_list|(
name|mockGoraEndpoint
argument_list|,
name|mockGoraConfiguration
argument_list|,
name|mockDatastore
argument_list|)
decl_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|mockCamelExchange
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|mockCamelExchange
argument_list|,
name|atLeastOnce
argument_list|()
argument_list|)
operator|.
name|getIn
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|mockCamelMessage
argument_list|,
name|atLeastOnce
argument_list|()
argument_list|)
operator|.
name|getHeader
argument_list|(
name|GoraAttribute
operator|.
name|GORA_OPERATION
operator|.
name|value
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|mockQuery
argument_list|,
name|atLeastOnce
argument_list|()
argument_list|)
operator|.
name|execute
argument_list|()
expr_stmt|;
name|verifyStatic
argument_list|(
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldInvokeDatastoreDeleteByQuery ()
specifier|public
name|void
name|shouldInvokeDatastoreDeleteByQuery
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|mockCamelExchange
operator|.
name|getIn
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|mockCamelMessage
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|mockCamelMessage
operator|.
name|getHeader
argument_list|(
name|GoraAttribute
operator|.
name|GORA_OPERATION
operator|.
name|value
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"deleteByQuery"
argument_list|)
expr_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|mockProperties
init|=
name|mock
argument_list|(
name|Map
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|mockCamelMessage
operator|.
name|getHeaders
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|mockProperties
argument_list|)
expr_stmt|;
specifier|final
name|Message
name|outMessage
init|=
name|mock
argument_list|(
name|Message
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|mockCamelExchange
operator|.
name|getOut
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|outMessage
argument_list|)
expr_stmt|;
name|mockStatic
argument_list|(
name|GoraUtils
operator|.
name|class
argument_list|)
expr_stmt|;
specifier|final
name|Query
name|mockQuery
init|=
name|mock
argument_list|(
name|QueryBase
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|GoraUtils
operator|.
name|constractQueryFromPropertiesMap
argument_list|(
name|mockProperties
argument_list|,
name|mockDatastore
argument_list|,
name|mockGoraConfiguration
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|mockQuery
argument_list|)
expr_stmt|;
specifier|final
name|GoraProducer
name|producer
init|=
operator|new
name|GoraProducer
argument_list|(
name|mockGoraEndpoint
argument_list|,
name|mockGoraConfiguration
argument_list|,
name|mockDatastore
argument_list|)
decl_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|mockCamelExchange
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|mockCamelExchange
argument_list|,
name|atLeastOnce
argument_list|()
argument_list|)
operator|.
name|getIn
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|mockCamelMessage
argument_list|,
name|atLeastOnce
argument_list|()
argument_list|)
operator|.
name|getHeader
argument_list|(
name|GoraAttribute
operator|.
name|GORA_OPERATION
operator|.
name|value
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|mockDatastore
argument_list|,
name|atMost
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|deleteByQuery
argument_list|(
name|mockQuery
argument_list|)
expr_stmt|;
name|verifyStatic
argument_list|(
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

