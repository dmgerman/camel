begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.api.dto.composite
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|composite
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
name|Iterator
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|core
operator|.
name|JsonProcessingException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|MapperFeature
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|ObjectMapper
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|ObjectWriter
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|XStream
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
name|salesforce
operator|.
name|api
operator|.
name|utils
operator|.
name|JsonUtils
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
name|salesforce
operator|.
name|api
operator|.
name|utils
operator|.
name|XStreamUtils
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
name|salesforce
operator|.
name|dto
operator|.
name|generated
operator|.
name|Account
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
name|salesforce
operator|.
name|dto
operator|.
name|generated
operator|.
name|Asset
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
name|salesforce
operator|.
name|dto
operator|.
name|generated
operator|.
name|Contact
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertArrayEquals
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
name|assertSame
import|;
end_import

begin_class
DECL|class|SObjectTreeTest
specifier|public
class|class
name|SObjectTreeTest
extends|extends
name|CompositeTestBase
block|{
annotation|@
name|Test
DECL|method|emptyTreeShouldBeZeroSized ()
specifier|public
name|void
name|emptyTreeShouldBeZeroSized
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|0
argument_list|,
operator|new
name|SObjectTree
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldCollectAllObjectTypesInTheTree ()
specifier|public
name|void
name|shouldCollectAllObjectTypesInTheTree
parameter_list|()
block|{
specifier|final
name|SObjectTree
name|tree
init|=
operator|new
name|SObjectTree
argument_list|()
decl_stmt|;
name|tree
operator|.
name|addObject
argument_list|(
operator|new
name|Account
argument_list|()
argument_list|)
operator|.
name|addChild
argument_list|(
operator|new
name|Contact
argument_list|()
argument_list|)
operator|.
name|addChild
argument_list|(
literal|"Assets"
argument_list|,
operator|new
name|Asset
argument_list|()
argument_list|)
expr_stmt|;
name|tree
operator|.
name|addObject
argument_list|(
operator|new
name|Account
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|Class
index|[]
name|types
init|=
name|tree
operator|.
name|objectTypes
argument_list|()
decl_stmt|;
name|Arrays
operator|.
name|sort
argument_list|(
name|types
argument_list|,
parameter_list|(
specifier|final
name|Class
name|l
parameter_list|,
specifier|final
name|Class
name|r
parameter_list|)
lambda|->
name|l
operator|.
name|getName
argument_list|()
operator|.
name|compareTo
argument_list|(
name|r
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|Class
index|[]
block|{
name|Account
operator|.
name|class
block|,
name|Asset
operator|.
name|class
block|,
name|Contact
operator|.
name|class
block|}
argument_list|,
name|types
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldSerializeToJson ()
specifier|public
name|void
name|shouldSerializeToJson
parameter_list|()
throws|throws
name|JsonProcessingException
block|{
specifier|final
name|ObjectMapper
name|mapper
init|=
name|JsonUtils
operator|.
name|createObjectMapper
argument_list|()
decl_stmt|;
name|mapper
operator|.
name|configure
argument_list|(
name|MapperFeature
operator|.
name|SORT_PROPERTIES_ALPHABETICALLY
argument_list|,
literal|true
argument_list|)
expr_stmt|;
specifier|final
name|ObjectWriter
name|writer
init|=
name|mapper
operator|.
name|writerFor
argument_list|(
name|SObjectTree
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|SObjectTree
name|tree
init|=
operator|new
name|SObjectTree
argument_list|()
decl_stmt|;
specifier|final
name|SObjectNode
name|account1
init|=
operator|new
name|SObjectNode
argument_list|(
name|tree
argument_list|,
name|simpleAccount
argument_list|)
decl_stmt|;
name|account1
operator|.
name|addChild
argument_list|(
literal|"Contacts"
argument_list|,
name|smith
argument_list|)
expr_stmt|;
name|account1
operator|.
name|addChild
argument_list|(
literal|"Contacts"
argument_list|,
name|evans
argument_list|)
expr_stmt|;
name|tree
operator|.
name|addNode
argument_list|(
name|account1
argument_list|)
expr_stmt|;
specifier|final
name|SObjectNode
name|account2
init|=
operator|new
name|SObjectNode
argument_list|(
name|tree
argument_list|,
name|simpleAccount2
argument_list|)
decl_stmt|;
name|tree
operator|.
name|addNode
argument_list|(
name|account2
argument_list|)
expr_stmt|;
specifier|final
name|String
name|json
init|=
name|writer
operator|.
name|writeValueAsString
argument_list|(
name|tree
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Should serialize to JSON as in Salesforce example"
argument_list|,
literal|"{"
operator|+
literal|"\"records\":["
operator|+
literal|"{"
operator|+
literal|"\"Industry\":\"Banking\","
operator|+
literal|"\"Name\":\"SampleAccount\","
operator|+
literal|"\"NumberOfEmployees\":100,"
operator|+
literal|"\"Phone\":\"1234567890\","
operator|+
literal|"\"Website\":\"www.salesforce.com\","
operator|+
literal|"\"attributes\":{"
operator|+
literal|"\"referenceId\":\"ref1\","
operator|+
literal|"\"type\":\"Account\""
operator|+
literal|"},"
operator|+
literal|"\"Contacts\":{"
operator|+
literal|"\"records\":["
operator|+
literal|"{"
operator|+
literal|"\"Email\":\"sample@salesforce.com\","
operator|+
literal|"\"LastName\":\"Smith\","
operator|+
literal|"\"Title\":\"President\","
operator|+
literal|"\"attributes\":{"
operator|+
literal|"\"referenceId\":\"ref2\","
operator|+
literal|"\"type\":\"Contact\""
operator|+
literal|"}"
operator|+
literal|"},"
operator|+
literal|"{"
operator|+
literal|"\"Email\":\"sample@salesforce.com\","
operator|+
literal|"\"LastName\":\"Evans\","
operator|+
literal|"\"Title\":\"Vice President\","
operator|+
literal|"\"attributes\":{"
operator|+
literal|"\"referenceId\":\"ref3\","
operator|+
literal|"\"type\":\"Contact\""
operator|+
literal|"}"
operator|+
literal|"}"
operator|+
literal|"]"
operator|+
literal|"}"
operator|+
literal|"},"
operator|+
literal|"{"
operator|+
literal|"\"Industry\":\"Banking\","
operator|+
literal|"\"Name\":\"SampleAccount2\","
operator|+
literal|"\"NumberOfEmployees\":100,"
operator|+
literal|"\"Phone\":\"1234567890\","
operator|+
literal|"\"Website\":\"www.salesforce2.com\","
operator|+
literal|"\"attributes\":{"
operator|+
literal|"\"referenceId\":\"ref4\","
operator|+
literal|"\"type\":\"Account\""
operator|+
literal|"}"
operator|+
literal|"}"
operator|+
literal|"]"
operator|+
literal|"}"
argument_list|,
name|json
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldSerializeToXml ()
specifier|public
name|void
name|shouldSerializeToXml
parameter_list|()
block|{
specifier|final
name|SObjectTree
name|tree
init|=
operator|new
name|SObjectTree
argument_list|()
decl_stmt|;
specifier|final
name|SObjectNode
name|account1
init|=
operator|new
name|SObjectNode
argument_list|(
name|tree
argument_list|,
name|simpleAccount
argument_list|)
decl_stmt|;
name|account1
operator|.
name|addChild
argument_list|(
literal|"Contacts"
argument_list|,
name|smith
argument_list|)
expr_stmt|;
name|account1
operator|.
name|addChild
argument_list|(
literal|"Contacts"
argument_list|,
name|evans
argument_list|)
expr_stmt|;
name|tree
operator|.
name|addNode
argument_list|(
name|account1
argument_list|)
expr_stmt|;
specifier|final
name|SObjectNode
name|account2
init|=
operator|new
name|SObjectNode
argument_list|(
name|tree
argument_list|,
name|simpleAccount2
argument_list|)
decl_stmt|;
name|tree
operator|.
name|addNode
argument_list|(
name|account2
argument_list|)
expr_stmt|;
specifier|final
name|XStream
name|xStream
init|=
name|XStreamUtils
operator|.
name|createXStream
argument_list|(
name|SObjectTree
operator|.
name|class
argument_list|,
name|Account
operator|.
name|class
argument_list|,
name|Contact
operator|.
name|class
argument_list|,
name|Asset
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|String
name|xml
init|=
name|xStream
operator|.
name|toXML
argument_list|(
name|tree
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Should serialize to XML as in Salesforce example"
argument_list|,
literal|"<SObjectTreeRequest>"
comment|//
operator|+
literal|"<records type=\"Account\" referenceId=\"ref1\">"
comment|//
operator|+
literal|"<Name>SampleAccount</Name>"
comment|//
operator|+
literal|"<Phone>1234567890</Phone>"
comment|//
operator|+
literal|"<Website>www.salesforce.com</Website>"
comment|//
operator|+
literal|"<Industry>Banking</Industry>"
comment|//
operator|+
literal|"<NumberOfEmployees>100</NumberOfEmployees>"
comment|//
operator|+
literal|"<Contacts>"
comment|//
operator|+
literal|"<records type=\"Contact\" referenceId=\"ref2\">"
comment|//
operator|+
literal|"<LastName>Smith</LastName>"
comment|//
operator|+
literal|"<Email>sample@salesforce.com</Email>"
comment|//
operator|+
literal|"<Title>President</Title>"
comment|//
operator|+
literal|"</records>"
comment|//
operator|+
literal|"<records type=\"Contact\" referenceId=\"ref3\">"
comment|//
operator|+
literal|"<LastName>Evans</LastName>"
comment|//
operator|+
literal|"<Email>sample@salesforce.com</Email>"
comment|//
operator|+
literal|"<Title>Vice President</Title>"
comment|//
operator|+
literal|"</records>"
comment|//
operator|+
literal|"</Contacts>"
comment|//
operator|+
literal|"</records>"
comment|//
operator|+
literal|"<records type=\"Account\" referenceId=\"ref4\">"
comment|//
operator|+
literal|"<Name>SampleAccount2</Name>"
comment|//
operator|+
literal|"<Phone>1234567890</Phone>"
comment|//
operator|+
literal|"<Website>www.salesforce2.com</Website>"
comment|//
operator|+
literal|"<Industry>Banking</Industry>"
comment|//
operator|+
literal|"<NumberOfEmployees>100</NumberOfEmployees>"
comment|//
operator|+
literal|"</records>"
comment|//
operator|+
literal|"</SObjectTreeRequest>"
argument_list|,
name|xml
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldSetIdByReferences ()
specifier|public
name|void
name|shouldSetIdByReferences
parameter_list|()
block|{
specifier|final
name|SObjectTree
name|tree
init|=
operator|new
name|SObjectTree
argument_list|()
decl_stmt|;
specifier|final
name|SObjectNode
name|account1
init|=
operator|new
name|SObjectNode
argument_list|(
name|tree
argument_list|,
name|simpleAccount
argument_list|)
decl_stmt|;
name|account1
operator|.
name|addChild
argument_list|(
literal|"Contacts"
argument_list|,
name|smith
argument_list|)
expr_stmt|;
name|account1
operator|.
name|addChild
argument_list|(
literal|"Contacts"
argument_list|,
name|evans
argument_list|)
expr_stmt|;
name|tree
operator|.
name|addNode
argument_list|(
name|account1
argument_list|)
expr_stmt|;
specifier|final
name|SObjectNode
name|account2
init|=
operator|new
name|SObjectNode
argument_list|(
name|tree
argument_list|,
name|simpleAccount2
argument_list|)
decl_stmt|;
name|tree
operator|.
name|addNode
argument_list|(
name|account2
argument_list|)
expr_stmt|;
name|tree
operator|.
name|setIdFor
argument_list|(
literal|"ref1"
argument_list|,
literal|"id1"
argument_list|)
expr_stmt|;
name|tree
operator|.
name|setIdFor
argument_list|(
literal|"ref4"
argument_list|,
literal|"id4"
argument_list|)
expr_stmt|;
name|tree
operator|.
name|setIdFor
argument_list|(
literal|"ref3"
argument_list|,
literal|"id3"
argument_list|)
expr_stmt|;
name|tree
operator|.
name|setIdFor
argument_list|(
literal|"ref2"
argument_list|,
literal|"id2"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"id1"
argument_list|,
name|simpleAccount
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"id2"
argument_list|,
name|smith
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"id3"
argument_list|,
name|evans
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"id4"
argument_list|,
name|simpleAccount2
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldSetIdByReferencesForNestedObjects ()
specifier|public
name|void
name|shouldSetIdByReferencesForNestedObjects
parameter_list|()
block|{
specifier|final
name|SObjectTree
name|tree
init|=
operator|new
name|SObjectTree
argument_list|()
decl_stmt|;
specifier|final
name|Account
name|account
init|=
operator|new
name|Account
argument_list|()
decl_stmt|;
specifier|final
name|SObjectNode
name|accountNode
init|=
operator|new
name|SObjectNode
argument_list|(
name|tree
argument_list|,
name|account
argument_list|)
decl_stmt|;
name|tree
operator|.
name|addNode
argument_list|(
name|accountNode
argument_list|)
expr_stmt|;
specifier|final
name|Contact
name|contact
init|=
operator|new
name|Contact
argument_list|()
decl_stmt|;
specifier|final
name|SObjectNode
name|contactNode
init|=
operator|new
name|SObjectNode
argument_list|(
name|tree
argument_list|,
name|contact
argument_list|)
decl_stmt|;
name|accountNode
operator|.
name|addChild
argument_list|(
literal|"Contacts"
argument_list|,
name|contactNode
argument_list|)
expr_stmt|;
specifier|final
name|Asset
name|asset
init|=
operator|new
name|Asset
argument_list|()
decl_stmt|;
specifier|final
name|SObjectNode
name|assetNode
init|=
operator|new
name|SObjectNode
argument_list|(
name|tree
argument_list|,
name|asset
argument_list|)
decl_stmt|;
name|contactNode
operator|.
name|addChild
argument_list|(
literal|"Assets"
argument_list|,
name|assetNode
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ref1"
argument_list|,
name|accountNode
operator|.
name|getObject
argument_list|()
operator|.
name|getAttributes
argument_list|()
operator|.
name|getReferenceId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ref2"
argument_list|,
name|contactNode
operator|.
name|getObject
argument_list|()
operator|.
name|getAttributes
argument_list|()
operator|.
name|getReferenceId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ref3"
argument_list|,
name|assetNode
operator|.
name|getObject
argument_list|()
operator|.
name|getAttributes
argument_list|()
operator|.
name|getReferenceId
argument_list|()
argument_list|)
expr_stmt|;
name|tree
operator|.
name|setIdFor
argument_list|(
literal|"ref1"
argument_list|,
literal|"id1"
argument_list|)
expr_stmt|;
name|tree
operator|.
name|setIdFor
argument_list|(
literal|"ref3"
argument_list|,
literal|"id3"
argument_list|)
expr_stmt|;
name|tree
operator|.
name|setIdFor
argument_list|(
literal|"ref2"
argument_list|,
literal|"id2"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"id1"
argument_list|,
name|account
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"id2"
argument_list|,
name|contact
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"id3"
argument_list|,
name|asset
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldSetReferences ()
specifier|public
name|void
name|shouldSetReferences
parameter_list|()
block|{
specifier|final
name|SObjectTree
name|tree
init|=
operator|new
name|SObjectTree
argument_list|()
decl_stmt|;
specifier|final
name|SObjectNode
name|account1
init|=
operator|new
name|SObjectNode
argument_list|(
name|tree
argument_list|,
name|simpleAccount
argument_list|)
decl_stmt|;
name|account1
operator|.
name|addChild
argument_list|(
literal|"Contacts"
argument_list|,
name|smith
argument_list|)
expr_stmt|;
name|account1
operator|.
name|addChild
argument_list|(
literal|"Contacts"
argument_list|,
name|evans
argument_list|)
expr_stmt|;
name|tree
operator|.
name|addNode
argument_list|(
name|account1
argument_list|)
expr_stmt|;
specifier|final
name|SObjectNode
name|account2
init|=
operator|new
name|SObjectNode
argument_list|(
name|tree
argument_list|,
name|simpleAccount2
argument_list|)
decl_stmt|;
name|tree
operator|.
name|addNode
argument_list|(
name|account2
argument_list|)
expr_stmt|;
specifier|final
name|SObjectNode
name|simpleAccountFromTree
init|=
name|tree
operator|.
name|records
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"ref1"
argument_list|,
name|simpleAccountFromTree
operator|.
name|getObject
argument_list|()
operator|.
name|getAttributes
argument_list|()
operator|.
name|getReferenceId
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|Iterator
argument_list|<
name|SObjectNode
argument_list|>
name|simpleAccountNodes
init|=
name|simpleAccountFromTree
operator|.
name|getChildNodes
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"ref2"
argument_list|,
name|simpleAccountNodes
operator|.
name|next
argument_list|()
operator|.
name|getObject
argument_list|()
operator|.
name|getAttributes
argument_list|()
operator|.
name|getReferenceId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ref3"
argument_list|,
name|simpleAccountNodes
operator|.
name|next
argument_list|()
operator|.
name|getObject
argument_list|()
operator|.
name|getAttributes
argument_list|()
operator|.
name|getReferenceId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ref4"
argument_list|,
name|account2
operator|.
name|getObject
argument_list|()
operator|.
name|getAttributes
argument_list|()
operator|.
name|getReferenceId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldSupportBuildingObjectTree ()
specifier|public
name|void
name|shouldSupportBuildingObjectTree
parameter_list|()
block|{
specifier|final
name|SObjectTree
name|tree
init|=
operator|new
name|SObjectTree
argument_list|()
decl_stmt|;
name|tree
operator|.
name|addObject
argument_list|(
name|simpleAccount
argument_list|)
operator|.
name|addChildren
argument_list|(
literal|"Contacts"
argument_list|,
name|smith
argument_list|,
name|evans
argument_list|)
expr_stmt|;
name|tree
operator|.
name|addObject
argument_list|(
name|simpleAccount2
argument_list|)
expr_stmt|;
specifier|final
name|SObjectNode
name|firstAccountFromTree
init|=
name|tree
operator|.
name|records
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|simpleAccount
argument_list|,
name|firstAccountFromTree
operator|.
name|getObject
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Account"
argument_list|,
name|firstAccountFromTree
operator|.
name|getObjectType
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|Iterator
argument_list|<
name|SObjectNode
argument_list|>
name|simpleAccountNodes
init|=
name|firstAccountFromTree
operator|.
name|getChildNodes
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
specifier|final
name|SObjectNode
name|smithNode
init|=
name|simpleAccountNodes
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertSame
argument_list|(
name|smith
argument_list|,
name|smithNode
operator|.
name|getObject
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Contact"
argument_list|,
name|smithNode
operator|.
name|getObjectType
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|SObjectNode
name|evansNode
init|=
name|simpleAccountNodes
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertSame
argument_list|(
name|evans
argument_list|,
name|evansNode
operator|.
name|getObject
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Contact"
argument_list|,
name|evansNode
operator|.
name|getObjectType
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|SObjectNode
name|secondAccountFromTree
init|=
name|tree
operator|.
name|records
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|simpleAccount2
argument_list|,
name|secondAccountFromTree
operator|.
name|getObject
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Account"
argument_list|,
name|secondAccountFromTree
operator|.
name|getObjectType
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|treeWithOneNodeShouldHaveSizeOfOne ()
specifier|public
name|void
name|treeWithOneNodeShouldHaveSizeOfOne
parameter_list|()
block|{
specifier|final
name|SObjectTree
name|tree
init|=
operator|new
name|SObjectTree
argument_list|()
decl_stmt|;
name|tree
operator|.
name|addObject
argument_list|(
operator|new
name|Account
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|tree
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|treeWithTwoNestedNodesShouldHaveSizeOfTwo ()
specifier|public
name|void
name|treeWithTwoNestedNodesShouldHaveSizeOfTwo
parameter_list|()
block|{
specifier|final
name|SObjectTree
name|tree
init|=
operator|new
name|SObjectTree
argument_list|()
decl_stmt|;
specifier|final
name|SObjectNode
name|accountNode
init|=
name|tree
operator|.
name|addObject
argument_list|(
operator|new
name|Account
argument_list|()
argument_list|)
decl_stmt|;
name|accountNode
operator|.
name|addChild
argument_list|(
literal|"Contacts"
argument_list|,
operator|new
name|Contact
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|tree
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|treeWithTwoNodesShouldHaveSizeOfTwo ()
specifier|public
name|void
name|treeWithTwoNodesShouldHaveSizeOfTwo
parameter_list|()
block|{
specifier|final
name|SObjectTree
name|tree
init|=
operator|new
name|SObjectTree
argument_list|()
decl_stmt|;
name|tree
operator|.
name|addObject
argument_list|(
operator|new
name|Account
argument_list|()
argument_list|)
expr_stmt|;
name|tree
operator|.
name|addObject
argument_list|(
operator|new
name|Account
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|tree
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

