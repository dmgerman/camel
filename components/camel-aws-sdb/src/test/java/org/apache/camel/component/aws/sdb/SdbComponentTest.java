begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.sdb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|aws
operator|.
name|sdb
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
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpledb
operator|.
name|model
operator|.
name|Attribute
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpledb
operator|.
name|model
operator|.
name|DeletableItem
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpledb
operator|.
name|model
operator|.
name|Item
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpledb
operator|.
name|model
operator|.
name|ReplaceableAttribute
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpledb
operator|.
name|model
operator|.
name|ReplaceableItem
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpledb
operator|.
name|model
operator|.
name|UpdateCondition
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
name|BindToRegistry
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
name|impl
operator|.
name|DefaultProducerTemplate
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
name|junit4
operator|.
name|CamelTestSupport
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
DECL|class|SdbComponentTest
specifier|public
class|class
name|SdbComponentTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|BindToRegistry
argument_list|(
literal|"amazonSDBClient"
argument_list|)
DECL|field|amazonSDBClient
specifier|private
name|AmazonSDBClientMock
name|amazonSDBClient
init|=
operator|new
name|AmazonSDBClientMock
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|doesntCreateDomainOnStartIfExists ()
specifier|public
name|void
name|doesntCreateDomainOnStartIfExists
parameter_list|()
throws|throws
name|Exception
block|{
name|assertNull
argument_list|(
name|amazonSDBClient
operator|.
name|createDomainRequest
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createDomainOnStartIfNotExists ()
specifier|public
name|void
name|createDomainOnStartIfNotExists
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultProducerTemplate
operator|.
name|newInstance
argument_list|(
name|context
argument_list|,
literal|"aws-sdb://NonExistingDomain?amazonSDBClient=#amazonSDBClient&operation=GetAttributes"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"NonExistingDomain"
argument_list|,
name|amazonSDBClient
operator|.
name|createDomainRequest
operator|.
name|getDomainName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|batchDeleteAttributes ()
specifier|public
name|void
name|batchDeleteAttributes
parameter_list|()
block|{
specifier|final
name|List
argument_list|<
name|DeletableItem
argument_list|>
name|deletableItems
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|DeletableItem
index|[]
block|{
operator|new
name|DeletableItem
argument_list|(
literal|"ITEM1"
argument_list|,
literal|null
argument_list|)
block|,
operator|new
name|DeletableItem
argument_list|(
literal|"ITEM2"
argument_list|,
literal|null
argument_list|)
block|}
argument_list|)
decl_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SdbConstants
operator|.
name|OPERATION
argument_list|,
name|SdbOperations
operator|.
name|BatchDeleteAttributes
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SdbConstants
operator|.
name|DELETABLE_ITEMS
argument_list|,
name|deletableItems
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"TestDomain"
argument_list|,
name|amazonSDBClient
operator|.
name|batchDeleteAttributesRequest
operator|.
name|getDomainName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|deletableItems
argument_list|,
name|amazonSDBClient
operator|.
name|batchDeleteAttributesRequest
operator|.
name|getItems
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|batchPutAttributes ()
specifier|public
name|void
name|batchPutAttributes
parameter_list|()
block|{
specifier|final
name|List
argument_list|<
name|ReplaceableItem
argument_list|>
name|replaceableItems
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|ReplaceableItem
index|[]
block|{
operator|new
name|ReplaceableItem
argument_list|(
literal|"ITEM1"
argument_list|)
block|}
argument_list|)
decl_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SdbConstants
operator|.
name|OPERATION
argument_list|,
name|SdbOperations
operator|.
name|BatchPutAttributes
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SdbConstants
operator|.
name|REPLACEABLE_ITEMS
argument_list|,
name|replaceableItems
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"TestDomain"
argument_list|,
name|amazonSDBClient
operator|.
name|batchPutAttributesRequest
operator|.
name|getDomainName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|replaceableItems
argument_list|,
name|amazonSDBClient
operator|.
name|batchPutAttributesRequest
operator|.
name|getItems
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|deleteAttributes ()
specifier|public
name|void
name|deleteAttributes
parameter_list|()
block|{
specifier|final
name|List
argument_list|<
name|Attribute
argument_list|>
name|attributes
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Attribute
index|[]
block|{
operator|new
name|Attribute
argument_list|(
literal|"NAME1"
argument_list|,
literal|"VALUE1"
argument_list|)
block|}
argument_list|)
decl_stmt|;
specifier|final
name|UpdateCondition
name|condition
init|=
operator|new
name|UpdateCondition
argument_list|(
literal|"Key1"
argument_list|,
literal|"Value1"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SdbConstants
operator|.
name|OPERATION
argument_list|,
name|SdbOperations
operator|.
name|DeleteAttributes
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SdbConstants
operator|.
name|ATTRIBUTES
argument_list|,
name|attributes
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SdbConstants
operator|.
name|ITEM_NAME
argument_list|,
literal|"ITEM1"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SdbConstants
operator|.
name|UPDATE_CONDITION
argument_list|,
name|condition
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"TestDomain"
argument_list|,
name|amazonSDBClient
operator|.
name|deleteAttributesRequest
operator|.
name|getDomainName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ITEM1"
argument_list|,
name|amazonSDBClient
operator|.
name|deleteAttributesRequest
operator|.
name|getItemName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|condition
argument_list|,
name|amazonSDBClient
operator|.
name|deleteAttributesRequest
operator|.
name|getExpected
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|attributes
argument_list|,
name|amazonSDBClient
operator|.
name|deleteAttributesRequest
operator|.
name|getAttributes
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|deleteAttributesItemNameIsRequired ()
specifier|public
name|void
name|deleteAttributesItemNameIsRequired
parameter_list|()
block|{
specifier|final
name|List
argument_list|<
name|Attribute
argument_list|>
name|attributes
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Attribute
index|[]
block|{
operator|new
name|Attribute
argument_list|(
literal|"NAME1"
argument_list|,
literal|"VALUE1"
argument_list|)
block|}
argument_list|)
decl_stmt|;
specifier|final
name|UpdateCondition
name|condition
init|=
operator|new
name|UpdateCondition
argument_list|(
literal|"Key1"
argument_list|,
literal|"Value1"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SdbConstants
operator|.
name|OPERATION
argument_list|,
name|SdbOperations
operator|.
name|DeleteAttributes
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SdbConstants
operator|.
name|ATTRIBUTES
argument_list|,
name|attributes
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SdbConstants
operator|.
name|UPDATE_CONDITION
argument_list|,
name|condition
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|Exception
name|exception
init|=
name|exchange
operator|.
name|getException
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|exception
operator|instanceof
name|IllegalArgumentException
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|deleteDomain ()
specifier|public
name|void
name|deleteDomain
parameter_list|()
block|{
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SdbConstants
operator|.
name|OPERATION
argument_list|,
name|SdbOperations
operator|.
name|DeleteDomain
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"TestDomain"
argument_list|,
name|amazonSDBClient
operator|.
name|deleteDomainRequest
operator|.
name|getDomainName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|domainMetadata ()
specifier|public
name|void
name|domainMetadata
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SdbConstants
operator|.
name|OPERATION
argument_list|,
name|SdbOperations
operator|.
name|DomainMetadata
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"TestDomain"
argument_list|,
name|amazonSDBClient
operator|.
name|domainMetadataRequest
operator|.
name|getDomainName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Integer
argument_list|(
literal|10
argument_list|)
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SdbConstants
operator|.
name|TIMESTAMP
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Integer
argument_list|(
literal|11
argument_list|)
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SdbConstants
operator|.
name|ITEM_COUNT
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Integer
argument_list|(
literal|12
argument_list|)
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SdbConstants
operator|.
name|ATTRIBUTE_NAME_COUNT
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Integer
argument_list|(
literal|13
argument_list|)
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SdbConstants
operator|.
name|ATTRIBUTE_VALUE_COUNT
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Long
argument_list|(
literal|1000000
argument_list|)
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SdbConstants
operator|.
name|ATTRIBUTE_NAME_SIZE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Long
argument_list|(
literal|2000000
argument_list|)
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SdbConstants
operator|.
name|ATTRIBUTE_VALUE_SIZE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Long
argument_list|(
literal|3000000
argument_list|)
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SdbConstants
operator|.
name|ITEM_NAME_SIZE
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Test
DECL|method|getAttributes ()
specifier|public
name|void
name|getAttributes
parameter_list|()
block|{
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|attributeNames
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"ATTRIBUTE1"
block|}
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SdbConstants
operator|.
name|OPERATION
argument_list|,
name|SdbOperations
operator|.
name|GetAttributes
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SdbConstants
operator|.
name|ITEM_NAME
argument_list|,
literal|"ITEM1"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SdbConstants
operator|.
name|CONSISTENT_READ
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SdbConstants
operator|.
name|ATTRIBUTE_NAMES
argument_list|,
name|attributeNames
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"TestDomain"
argument_list|,
name|amazonSDBClient
operator|.
name|getAttributesRequest
operator|.
name|getDomainName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ITEM1"
argument_list|,
name|amazonSDBClient
operator|.
name|getAttributesRequest
operator|.
name|getItemName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|amazonSDBClient
operator|.
name|getAttributesRequest
operator|.
name|getConsistentRead
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|attributeNames
argument_list|,
name|amazonSDBClient
operator|.
name|getAttributesRequest
operator|.
name|getAttributeNames
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Attribute
argument_list|>
name|attributes
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SdbConstants
operator|.
name|ATTRIBUTES
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|attributes
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"AttributeOne"
argument_list|,
name|attributes
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
literal|"Value One"
argument_list|,
name|attributes
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"AttributeTwo"
argument_list|,
name|attributes
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
literal|"Value Two"
argument_list|,
name|attributes
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getAttributesItemNameIsRequired ()
specifier|public
name|void
name|getAttributesItemNameIsRequired
parameter_list|()
block|{
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|attributeNames
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"ATTRIBUTE1"
block|}
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SdbConstants
operator|.
name|OPERATION
argument_list|,
name|SdbOperations
operator|.
name|GetAttributes
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SdbConstants
operator|.
name|CONSISTENT_READ
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SdbConstants
operator|.
name|ATTRIBUTE_NAMES
argument_list|,
name|attributeNames
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|Exception
name|exception
init|=
name|exchange
operator|.
name|getException
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|exception
operator|instanceof
name|IllegalArgumentException
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|}
argument_list|)
annotation|@
name|Test
DECL|method|listDomains ()
specifier|public
name|void
name|listDomains
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SdbConstants
operator|.
name|OPERATION
argument_list|,
name|SdbOperations
operator|.
name|ListDomains
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SdbConstants
operator|.
name|MAX_NUMBER_OF_DOMAINS
argument_list|,
operator|new
name|Integer
argument_list|(
literal|5
argument_list|)
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SdbConstants
operator|.
name|NEXT_TOKEN
argument_list|,
literal|"TOKEN1"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Integer
argument_list|(
literal|5
argument_list|)
argument_list|,
name|amazonSDBClient
operator|.
name|listDomainsRequest
operator|.
name|getMaxNumberOfDomains
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"TOKEN1"
argument_list|,
name|amazonSDBClient
operator|.
name|listDomainsRequest
operator|.
name|getNextToken
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|domains
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SdbConstants
operator|.
name|DOMAIN_NAMES
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"TOKEN2"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SdbConstants
operator|.
name|NEXT_TOKEN
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|domains
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|domains
operator|.
name|contains
argument_list|(
literal|"DOMAIN1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|domains
operator|.
name|contains
argument_list|(
literal|"DOMAIN2"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|putAttributes ()
specifier|public
name|void
name|putAttributes
parameter_list|()
block|{
specifier|final
name|List
argument_list|<
name|ReplaceableAttribute
argument_list|>
name|replaceableAttributes
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|ReplaceableAttribute
index|[]
block|{
operator|new
name|ReplaceableAttribute
argument_list|(
literal|"NAME1"
argument_list|,
literal|"VALUE1"
argument_list|,
literal|true
argument_list|)
block|}
argument_list|)
decl_stmt|;
specifier|final
name|UpdateCondition
name|updateCondition
init|=
operator|new
name|UpdateCondition
argument_list|(
literal|"NAME1"
argument_list|,
literal|"VALUE1"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SdbConstants
operator|.
name|OPERATION
argument_list|,
name|SdbOperations
operator|.
name|PutAttributes
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SdbConstants
operator|.
name|ITEM_NAME
argument_list|,
literal|"ITEM1"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SdbConstants
operator|.
name|UPDATE_CONDITION
argument_list|,
name|updateCondition
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SdbConstants
operator|.
name|REPLACEABLE_ATTRIBUTES
argument_list|,
name|replaceableAttributes
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"TestDomain"
argument_list|,
name|amazonSDBClient
operator|.
name|putAttributesRequest
operator|.
name|getDomainName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ITEM1"
argument_list|,
name|amazonSDBClient
operator|.
name|putAttributesRequest
operator|.
name|getItemName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|updateCondition
argument_list|,
name|amazonSDBClient
operator|.
name|putAttributesRequest
operator|.
name|getExpected
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|replaceableAttributes
argument_list|,
name|amazonSDBClient
operator|.
name|putAttributesRequest
operator|.
name|getAttributes
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|putAttributesItemNameIsRequired ()
specifier|public
name|void
name|putAttributesItemNameIsRequired
parameter_list|()
block|{
specifier|final
name|List
argument_list|<
name|ReplaceableAttribute
argument_list|>
name|replaceableAttributes
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|ReplaceableAttribute
index|[]
block|{
operator|new
name|ReplaceableAttribute
argument_list|(
literal|"NAME1"
argument_list|,
literal|"VALUE1"
argument_list|,
literal|true
argument_list|)
block|}
argument_list|)
decl_stmt|;
specifier|final
name|UpdateCondition
name|updateCondition
init|=
operator|new
name|UpdateCondition
argument_list|(
literal|"NAME1"
argument_list|,
literal|"VALUE1"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SdbConstants
operator|.
name|OPERATION
argument_list|,
name|SdbOperations
operator|.
name|PutAttributes
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SdbConstants
operator|.
name|UPDATE_CONDITION
argument_list|,
name|updateCondition
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SdbConstants
operator|.
name|REPLACEABLE_ATTRIBUTES
argument_list|,
name|replaceableAttributes
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|Exception
name|exception
init|=
name|exchange
operator|.
name|getException
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|exception
operator|instanceof
name|IllegalArgumentException
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Test
DECL|method|select ()
specifier|public
name|void
name|select
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SdbConstants
operator|.
name|OPERATION
argument_list|,
name|SdbOperations
operator|.
name|Select
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SdbConstants
operator|.
name|NEXT_TOKEN
argument_list|,
literal|"TOKEN1"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SdbConstants
operator|.
name|CONSISTENT_READ
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SdbConstants
operator|.
name|SELECT_EXPRESSION
argument_list|,
literal|"SELECT NAME1 FROM DOMAIN1 WHERE NAME1 LIKE 'VALUE1'"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|amazonSDBClient
operator|.
name|selectRequest
operator|.
name|getConsistentRead
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"TOKEN1"
argument_list|,
name|amazonSDBClient
operator|.
name|selectRequest
operator|.
name|getNextToken
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"SELECT NAME1 FROM DOMAIN1 WHERE NAME1 LIKE 'VALUE1'"
argument_list|,
name|amazonSDBClient
operator|.
name|selectRequest
operator|.
name|getSelectExpression
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Item
argument_list|>
name|items
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SdbConstants
operator|.
name|ITEMS
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"TOKEN2"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SdbConstants
operator|.
name|NEXT_TOKEN
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|items
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ITEM1"
argument_list|,
name|items
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
literal|"ITEM2"
argument_list|,
name|items
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
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"aws-sdb://TestDomain?amazonSDBClient=#amazonSDBClient&operation=GetAttributes"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

