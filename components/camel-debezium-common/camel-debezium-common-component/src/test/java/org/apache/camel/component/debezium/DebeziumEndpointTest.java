begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.debezium
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|debezium
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|io
operator|.
name|debezium
operator|.
name|data
operator|.
name|Envelope
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
name|Producer
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
name|debezium
operator|.
name|configuration
operator|.
name|FileConnectorEmbeddedDebeziumConfiguration
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
name|DefaultCamelContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|kafka
operator|.
name|connect
operator|.
name|data
operator|.
name|Schema
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|kafka
operator|.
name|connect
operator|.
name|data
operator|.
name|SchemaBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|kafka
operator|.
name|connect
operator|.
name|data
operator|.
name|Struct
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|kafka
operator|.
name|connect
operator|.
name|source
operator|.
name|SourceRecord
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
name|junit
operator|.
name|MockitoJUnitRunner
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
name|assertEquals
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
name|assertFalse
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
name|assertNotNull
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
name|assertNull
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
DECL|class|DebeziumEndpointTest
specifier|public
class|class
name|DebeziumEndpointTest
block|{
DECL|field|debeziumEndpoint
specifier|private
name|DebeziumEndpoint
name|debeziumEndpoint
decl_stmt|;
annotation|@
name|Mock
DECL|field|processor
specifier|private
name|Processor
name|processor
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|debeziumEndpoint
operator|=
operator|new
name|DebeziumTestEndpoint
argument_list|(
literal|""
argument_list|,
operator|new
name|DebeziumTestComponent
argument_list|(
operator|new
name|DefaultCamelContext
argument_list|()
argument_list|)
argument_list|,
operator|new
name|FileConnectorEmbeddedDebeziumConfiguration
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testIfCreatesConsumer ()
specifier|public
name|void
name|testIfCreatesConsumer
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Consumer
name|debeziumConsumer
init|=
name|debeziumEndpoint
operator|.
name|createConsumer
argument_list|(
name|processor
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|debeziumConsumer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|UnsupportedOperationException
operator|.
name|class
argument_list|)
DECL|method|testIfFailsToCreateProducer ()
specifier|public
name|void
name|testIfFailsToCreateProducer
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Producer
name|debeziumConsumer
init|=
name|debeziumEndpoint
operator|.
name|createProducer
argument_list|()
decl_stmt|;
block|}
annotation|@
name|Test
DECL|method|testIfCreatesExchangeFromSourceCreateRecord ()
specifier|public
name|void
name|testIfCreatesExchangeFromSourceCreateRecord
parameter_list|()
block|{
specifier|final
name|SourceRecord
name|sourceRecord
init|=
name|createCreateRecord
argument_list|()
decl_stmt|;
specifier|final
name|Exchange
name|exchange
init|=
name|debeziumEndpoint
operator|.
name|createDbzExchange
argument_list|(
name|sourceRecord
argument_list|)
decl_stmt|;
specifier|final
name|Message
name|inMessage
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
comment|// assert headers
name|assertEquals
argument_list|(
literal|"dummy"
argument_list|,
name|inMessage
operator|.
name|getHeader
argument_list|(
name|DebeziumConstants
operator|.
name|HEADER_IDENTIFIER
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Envelope
operator|.
name|Operation
operator|.
name|CREATE
operator|.
name|code
argument_list|()
argument_list|,
name|inMessage
operator|.
name|getHeader
argument_list|(
name|DebeziumConstants
operator|.
name|HEADER_OPERATION
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|Struct
name|key
init|=
operator|(
name|Struct
operator|)
name|inMessage
operator|.
name|getHeader
argument_list|(
name|DebeziumConstants
operator|.
name|HEADER_KEY
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|12345
argument_list|,
name|key
operator|.
name|getInt32
argument_list|(
literal|"id"
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertSourceMetadata
argument_list|(
name|inMessage
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|inMessage
operator|.
name|getHeader
argument_list|(
name|DebeziumConstants
operator|.
name|HEADER_TIMESTAMP
argument_list|)
argument_list|)
expr_stmt|;
comment|// assert value
specifier|final
name|Struct
name|body
init|=
operator|(
name|Struct
operator|)
name|inMessage
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|byte
operator|)
literal|1
argument_list|,
name|body
operator|.
name|getInt8
argument_list|(
literal|"id"
argument_list|)
operator|.
name|byteValue
argument_list|()
argument_list|)
expr_stmt|;
comment|// assert schema
name|assertSchema
argument_list|(
name|body
operator|.
name|schema
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testIfCreatesExchangeFromSourceDeleteRecord ()
specifier|public
name|void
name|testIfCreatesExchangeFromSourceDeleteRecord
parameter_list|()
block|{
specifier|final
name|SourceRecord
name|sourceRecord
init|=
name|createDeleteRecord
argument_list|()
decl_stmt|;
specifier|final
name|Exchange
name|exchange
init|=
name|debeziumEndpoint
operator|.
name|createDbzExchange
argument_list|(
name|sourceRecord
argument_list|)
decl_stmt|;
specifier|final
name|Message
name|inMessage
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
comment|// assert headers
name|assertEquals
argument_list|(
literal|"dummy"
argument_list|,
name|inMessage
operator|.
name|getHeader
argument_list|(
name|DebeziumConstants
operator|.
name|HEADER_IDENTIFIER
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Envelope
operator|.
name|Operation
operator|.
name|DELETE
operator|.
name|code
argument_list|()
argument_list|,
name|inMessage
operator|.
name|getHeader
argument_list|(
name|DebeziumConstants
operator|.
name|HEADER_OPERATION
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|Struct
name|key
init|=
operator|(
name|Struct
operator|)
name|inMessage
operator|.
name|getHeader
argument_list|(
name|DebeziumConstants
operator|.
name|HEADER_KEY
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|12345
argument_list|,
name|key
operator|.
name|getInt32
argument_list|(
literal|"id"
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|inMessage
operator|.
name|getHeader
argument_list|(
name|DebeziumConstants
operator|.
name|HEADER_BEFORE
argument_list|)
argument_list|)
expr_stmt|;
comment|// assert value
specifier|final
name|Struct
name|body
init|=
operator|(
name|Struct
operator|)
name|inMessage
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|body
argument_list|)
expr_stmt|;
comment|// we expect body to be null since is a delete
block|}
annotation|@
name|Test
DECL|method|testIfCreatesExchangeFromSourceDeleteRecordWithNull ()
specifier|public
name|void
name|testIfCreatesExchangeFromSourceDeleteRecordWithNull
parameter_list|()
block|{
specifier|final
name|SourceRecord
name|sourceRecord
init|=
name|createDeleteRecordWithNull
argument_list|()
decl_stmt|;
specifier|final
name|Exchange
name|exchange
init|=
name|debeziumEndpoint
operator|.
name|createDbzExchange
argument_list|(
name|sourceRecord
argument_list|)
decl_stmt|;
specifier|final
name|Message
name|inMessage
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
comment|// assert headers
name|assertEquals
argument_list|(
literal|"dummy"
argument_list|,
name|inMessage
operator|.
name|getHeader
argument_list|(
name|DebeziumConstants
operator|.
name|HEADER_IDENTIFIER
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|Struct
name|key
init|=
operator|(
name|Struct
operator|)
name|inMessage
operator|.
name|getHeader
argument_list|(
name|DebeziumConstants
operator|.
name|HEADER_KEY
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|12345
argument_list|,
name|key
operator|.
name|getInt32
argument_list|(
literal|"id"
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
comment|// assert value
specifier|final
name|Struct
name|body
init|=
operator|(
name|Struct
operator|)
name|inMessage
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testIfCreatesExchangeFromSourceUpdateRecord ()
specifier|public
name|void
name|testIfCreatesExchangeFromSourceUpdateRecord
parameter_list|()
block|{
specifier|final
name|SourceRecord
name|sourceRecord
init|=
name|createUpdateRecord
argument_list|()
decl_stmt|;
specifier|final
name|Exchange
name|exchange
init|=
name|debeziumEndpoint
operator|.
name|createDbzExchange
argument_list|(
name|sourceRecord
argument_list|)
decl_stmt|;
specifier|final
name|Message
name|inMessage
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
comment|// assert headers
name|assertEquals
argument_list|(
literal|"dummy"
argument_list|,
name|inMessage
operator|.
name|getHeader
argument_list|(
name|DebeziumConstants
operator|.
name|HEADER_IDENTIFIER
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Envelope
operator|.
name|Operation
operator|.
name|UPDATE
operator|.
name|code
argument_list|()
argument_list|,
name|inMessage
operator|.
name|getHeader
argument_list|(
name|DebeziumConstants
operator|.
name|HEADER_OPERATION
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|Struct
name|key
init|=
operator|(
name|Struct
operator|)
name|inMessage
operator|.
name|getHeader
argument_list|(
name|DebeziumConstants
operator|.
name|HEADER_KEY
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|12345
argument_list|,
name|key
operator|.
name|getInt32
argument_list|(
literal|"id"
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertSourceMetadata
argument_list|(
name|inMessage
argument_list|)
expr_stmt|;
comment|// assert value
specifier|final
name|Struct
name|before
init|=
operator|(
name|Struct
operator|)
name|inMessage
operator|.
name|getHeader
argument_list|(
name|DebeziumConstants
operator|.
name|HEADER_BEFORE
argument_list|)
decl_stmt|;
specifier|final
name|Struct
name|after
init|=
operator|(
name|Struct
operator|)
name|inMessage
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|before
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|after
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|byte
operator|)
literal|1
argument_list|,
name|before
operator|.
name|getInt8
argument_list|(
literal|"id"
argument_list|)
operator|.
name|byteValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|byte
operator|)
literal|2
argument_list|,
name|after
operator|.
name|getInt8
argument_list|(
literal|"id"
argument_list|)
operator|.
name|byteValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testIfCreatesExchangeFromSourceRecordOtherThanStruct ()
specifier|public
name|void
name|testIfCreatesExchangeFromSourceRecordOtherThanStruct
parameter_list|()
block|{
specifier|final
name|SourceRecord
name|sourceRecord
init|=
name|createStringRecord
argument_list|()
decl_stmt|;
specifier|final
name|Exchange
name|exchange
init|=
name|debeziumEndpoint
operator|.
name|createDbzExchange
argument_list|(
name|sourceRecord
argument_list|)
decl_stmt|;
specifier|final
name|Message
name|inMessage
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
comment|// assert headers
name|assertEquals
argument_list|(
literal|"dummy"
argument_list|,
name|inMessage
operator|.
name|getHeader
argument_list|(
name|DebeziumConstants
operator|.
name|HEADER_IDENTIFIER
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|inMessage
operator|.
name|getHeader
argument_list|(
name|DebeziumConstants
operator|.
name|HEADER_OPERATION
argument_list|)
argument_list|)
expr_stmt|;
comment|// assert value
specifier|final
name|String
name|value
init|=
operator|(
name|String
operator|)
name|inMessage
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|sourceRecord
operator|.
name|value
argument_list|()
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testIfHandlesUnknownSchema ()
specifier|public
name|void
name|testIfHandlesUnknownSchema
parameter_list|()
block|{
specifier|final
name|SourceRecord
name|sourceRecord
init|=
name|createUnknownUnnamedSchemaRecord
argument_list|()
decl_stmt|;
specifier|final
name|Exchange
name|exchange
init|=
name|debeziumEndpoint
operator|.
name|createDbzExchange
argument_list|(
name|sourceRecord
argument_list|)
decl_stmt|;
specifier|final
name|Message
name|inMessage
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
comment|// assert headers
name|assertEquals
argument_list|(
literal|"dummy"
argument_list|,
name|inMessage
operator|.
name|getHeader
argument_list|(
name|DebeziumConstants
operator|.
name|HEADER_IDENTIFIER
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|inMessage
operator|.
name|getHeader
argument_list|(
name|DebeziumConstants
operator|.
name|HEADER_OPERATION
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|inMessage
operator|.
name|getHeader
argument_list|(
name|DebeziumConstants
operator|.
name|HEADER_KEY
argument_list|)
argument_list|)
expr_stmt|;
comment|// assert value
specifier|final
name|Struct
name|body
init|=
operator|(
name|Struct
operator|)
name|inMessage
operator|.
name|getBody
argument_list|()
decl_stmt|;
comment|// we have to get value of after with struct, we are strict about this case
name|assertNull
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
DECL|method|createCreateRecord ()
specifier|private
name|SourceRecord
name|createCreateRecord
parameter_list|()
block|{
specifier|final
name|Schema
name|recordSchema
init|=
name|SchemaBuilder
operator|.
name|struct
argument_list|()
operator|.
name|field
argument_list|(
literal|"id"
argument_list|,
name|SchemaBuilder
operator|.
name|int8
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
specifier|final
name|Schema
name|sourceSchema
init|=
name|SchemaBuilder
operator|.
name|struct
argument_list|()
operator|.
name|field
argument_list|(
literal|"lsn"
argument_list|,
name|SchemaBuilder
operator|.
name|int32
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|Envelope
name|envelope
init|=
name|Envelope
operator|.
name|defineSchema
argument_list|()
operator|.
name|withName
argument_list|(
literal|"dummy.Envelope"
argument_list|)
operator|.
name|withRecord
argument_list|(
name|recordSchema
argument_list|)
operator|.
name|withSource
argument_list|(
name|sourceSchema
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
specifier|final
name|Struct
name|after
init|=
operator|new
name|Struct
argument_list|(
name|recordSchema
argument_list|)
decl_stmt|;
specifier|final
name|Struct
name|source
init|=
operator|new
name|Struct
argument_list|(
name|sourceSchema
argument_list|)
decl_stmt|;
name|after
operator|.
name|put
argument_list|(
literal|"id"
argument_list|,
operator|(
name|byte
operator|)
literal|1
argument_list|)
expr_stmt|;
name|source
operator|.
name|put
argument_list|(
literal|"lsn"
argument_list|,
literal|1234
argument_list|)
expr_stmt|;
specifier|final
name|Struct
name|payload
init|=
name|envelope
operator|.
name|create
argument_list|(
name|after
argument_list|,
name|source
argument_list|,
name|System
operator|.
name|nanoTime
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|new
name|SourceRecord
argument_list|(
operator|new
name|HashMap
argument_list|<>
argument_list|()
argument_list|,
name|createSourceOffset
argument_list|()
argument_list|,
literal|"dummy"
argument_list|,
name|createKeySchema
argument_list|()
argument_list|,
name|createKeyRecord
argument_list|()
argument_list|,
name|envelope
operator|.
name|schema
argument_list|()
argument_list|,
name|payload
argument_list|)
return|;
block|}
DECL|method|createDeleteRecord ()
specifier|private
name|SourceRecord
name|createDeleteRecord
parameter_list|()
block|{
specifier|final
name|Schema
name|recordSchema
init|=
name|SchemaBuilder
operator|.
name|struct
argument_list|()
operator|.
name|field
argument_list|(
literal|"id"
argument_list|,
name|SchemaBuilder
operator|.
name|int8
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|Envelope
name|envelope
init|=
name|Envelope
operator|.
name|defineSchema
argument_list|()
operator|.
name|withName
argument_list|(
literal|"dummy.Envelope"
argument_list|)
operator|.
name|withRecord
argument_list|(
name|recordSchema
argument_list|)
operator|.
name|withSource
argument_list|(
name|SchemaBuilder
operator|.
name|struct
argument_list|()
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
specifier|final
name|Struct
name|before
init|=
operator|new
name|Struct
argument_list|(
name|recordSchema
argument_list|)
decl_stmt|;
name|before
operator|.
name|put
argument_list|(
literal|"id"
argument_list|,
operator|(
name|byte
operator|)
literal|1
argument_list|)
expr_stmt|;
specifier|final
name|Struct
name|payload
init|=
name|envelope
operator|.
name|delete
argument_list|(
name|before
argument_list|,
literal|null
argument_list|,
name|System
operator|.
name|nanoTime
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|new
name|SourceRecord
argument_list|(
operator|new
name|HashMap
argument_list|<>
argument_list|()
argument_list|,
name|createSourceOffset
argument_list|()
argument_list|,
literal|"dummy"
argument_list|,
name|createKeySchema
argument_list|()
argument_list|,
name|createKeyRecord
argument_list|()
argument_list|,
name|envelope
operator|.
name|schema
argument_list|()
argument_list|,
name|payload
argument_list|)
return|;
block|}
DECL|method|createDeleteRecordWithNull ()
specifier|private
name|SourceRecord
name|createDeleteRecordWithNull
parameter_list|()
block|{
specifier|final
name|Schema
name|recordSchema
init|=
name|SchemaBuilder
operator|.
name|struct
argument_list|()
operator|.
name|field
argument_list|(
literal|"id"
argument_list|,
name|SchemaBuilder
operator|.
name|int8
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|Envelope
name|envelope
init|=
name|Envelope
operator|.
name|defineSchema
argument_list|()
operator|.
name|withName
argument_list|(
literal|"dummy.Envelope"
argument_list|)
operator|.
name|withRecord
argument_list|(
name|recordSchema
argument_list|)
operator|.
name|withSource
argument_list|(
name|SchemaBuilder
operator|.
name|struct
argument_list|()
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
specifier|final
name|Struct
name|before
init|=
operator|new
name|Struct
argument_list|(
name|recordSchema
argument_list|)
decl_stmt|;
name|before
operator|.
name|put
argument_list|(
literal|"id"
argument_list|,
operator|(
name|byte
operator|)
literal|1
argument_list|)
expr_stmt|;
return|return
operator|new
name|SourceRecord
argument_list|(
operator|new
name|HashMap
argument_list|<>
argument_list|()
argument_list|,
name|createSourceOffset
argument_list|()
argument_list|,
literal|"dummy"
argument_list|,
name|createKeySchema
argument_list|()
argument_list|,
name|createKeyRecord
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|createUpdateRecord ()
specifier|private
name|SourceRecord
name|createUpdateRecord
parameter_list|()
block|{
specifier|final
name|Schema
name|recordSchema
init|=
name|SchemaBuilder
operator|.
name|struct
argument_list|()
operator|.
name|field
argument_list|(
literal|"id"
argument_list|,
name|SchemaBuilder
operator|.
name|int8
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
specifier|final
name|Schema
name|sourceSchema
init|=
name|SchemaBuilder
operator|.
name|struct
argument_list|()
operator|.
name|field
argument_list|(
literal|"lsn"
argument_list|,
name|SchemaBuilder
operator|.
name|int32
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|Envelope
name|envelope
init|=
name|Envelope
operator|.
name|defineSchema
argument_list|()
operator|.
name|withName
argument_list|(
literal|"dummy.Envelope"
argument_list|)
operator|.
name|withRecord
argument_list|(
name|recordSchema
argument_list|)
operator|.
name|withSource
argument_list|(
name|sourceSchema
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
specifier|final
name|Struct
name|before
init|=
operator|new
name|Struct
argument_list|(
name|recordSchema
argument_list|)
decl_stmt|;
specifier|final
name|Struct
name|source
init|=
operator|new
name|Struct
argument_list|(
name|sourceSchema
argument_list|)
decl_stmt|;
specifier|final
name|Struct
name|after
init|=
operator|new
name|Struct
argument_list|(
name|recordSchema
argument_list|)
decl_stmt|;
name|before
operator|.
name|put
argument_list|(
literal|"id"
argument_list|,
operator|(
name|byte
operator|)
literal|1
argument_list|)
expr_stmt|;
name|after
operator|.
name|put
argument_list|(
literal|"id"
argument_list|,
operator|(
name|byte
operator|)
literal|2
argument_list|)
expr_stmt|;
name|source
operator|.
name|put
argument_list|(
literal|"lsn"
argument_list|,
literal|1234
argument_list|)
expr_stmt|;
specifier|final
name|Struct
name|payload
init|=
name|envelope
operator|.
name|update
argument_list|(
name|before
argument_list|,
name|after
argument_list|,
name|source
argument_list|,
name|System
operator|.
name|nanoTime
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|new
name|SourceRecord
argument_list|(
operator|new
name|HashMap
argument_list|<>
argument_list|()
argument_list|,
name|createSourceOffset
argument_list|()
argument_list|,
literal|"dummy"
argument_list|,
name|createKeySchema
argument_list|()
argument_list|,
name|createKeyRecord
argument_list|()
argument_list|,
name|envelope
operator|.
name|schema
argument_list|()
argument_list|,
name|payload
argument_list|)
return|;
block|}
DECL|method|createUnknownUnnamedSchemaRecord ()
specifier|private
name|SourceRecord
name|createUnknownUnnamedSchemaRecord
parameter_list|()
block|{
specifier|final
name|Schema
name|recordSchema
init|=
name|SchemaBuilder
operator|.
name|struct
argument_list|()
operator|.
name|field
argument_list|(
literal|"id"
argument_list|,
name|SchemaBuilder
operator|.
name|int8
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
specifier|final
name|Struct
name|before
init|=
operator|new
name|Struct
argument_list|(
name|recordSchema
argument_list|)
decl_stmt|;
name|before
operator|.
name|put
argument_list|(
literal|"id"
argument_list|,
operator|(
name|byte
operator|)
literal|1
argument_list|)
expr_stmt|;
return|return
operator|new
name|SourceRecord
argument_list|(
operator|new
name|HashMap
argument_list|<>
argument_list|()
argument_list|,
operator|new
name|HashMap
argument_list|<>
argument_list|()
argument_list|,
literal|"dummy"
argument_list|,
name|recordSchema
argument_list|,
name|before
argument_list|)
return|;
block|}
DECL|method|createStringRecord ()
specifier|private
name|SourceRecord
name|createStringRecord
parameter_list|()
block|{
specifier|final
name|Schema
name|recordSchema
init|=
name|Schema
operator|.
name|STRING_SCHEMA
decl_stmt|;
return|return
operator|new
name|SourceRecord
argument_list|(
operator|new
name|HashMap
argument_list|<>
argument_list|()
argument_list|,
name|createSourceOffset
argument_list|()
argument_list|,
literal|"dummy"
argument_list|,
name|recordSchema
argument_list|,
literal|"test_record"
argument_list|)
return|;
block|}
DECL|method|createSourceOffset ()
specifier|private
name|HashMap
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|createSourceOffset
parameter_list|()
block|{
specifier|final
name|HashMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|sourceOffsets
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|sourceOffsets
operator|.
name|put
argument_list|(
literal|"pos"
argument_list|,
literal|111
argument_list|)
expr_stmt|;
return|return
name|sourceOffsets
return|;
block|}
DECL|method|createKeySchema ()
specifier|private
name|Schema
name|createKeySchema
parameter_list|()
block|{
return|return
name|SchemaBuilder
operator|.
name|struct
argument_list|()
operator|.
name|field
argument_list|(
literal|"id"
argument_list|,
name|SchemaBuilder
operator|.
name|int32
argument_list|()
operator|.
name|build
argument_list|()
argument_list|)
return|;
block|}
DECL|method|createKeyRecord ()
specifier|private
name|Struct
name|createKeyRecord
parameter_list|()
block|{
specifier|final
name|Struct
name|key
init|=
operator|new
name|Struct
argument_list|(
name|createKeySchema
argument_list|()
argument_list|)
decl_stmt|;
name|key
operator|.
name|put
argument_list|(
literal|"id"
argument_list|,
literal|12345
argument_list|)
expr_stmt|;
return|return
name|key
return|;
block|}
DECL|method|assertSourceMetadata (final Message inMessage)
specifier|private
name|void
name|assertSourceMetadata
parameter_list|(
specifier|final
name|Message
name|inMessage
parameter_list|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|source
init|=
name|inMessage
operator|.
name|getHeader
argument_list|(
name|DebeziumConstants
operator|.
name|HEADER_SOURCE_METADATA
argument_list|,
name|Map
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1234
argument_list|,
name|source
operator|.
name|get
argument_list|(
literal|"lsn"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|assertSchema (final Schema schema)
specifier|private
name|void
name|assertSchema
parameter_list|(
specifier|final
name|Schema
name|schema
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|schema
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|schema
operator|.
name|fields
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

