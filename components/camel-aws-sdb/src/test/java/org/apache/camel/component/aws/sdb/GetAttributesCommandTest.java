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
name|ArrayList
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
name|camel
operator|.
name|support
operator|.
name|DefaultExchange
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
name|assertNull
import|;
end_import

begin_class
DECL|class|GetAttributesCommandTest
specifier|public
class|class
name|GetAttributesCommandTest
block|{
DECL|field|command
specifier|private
name|GetAttributesCommand
name|command
decl_stmt|;
DECL|field|sdbClient
specifier|private
name|AmazonSDBClientMock
name|sdbClient
decl_stmt|;
DECL|field|configuration
specifier|private
name|SdbConfiguration
name|configuration
decl_stmt|;
DECL|field|exchange
specifier|private
name|Exchange
name|exchange
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|sdbClient
operator|=
operator|new
name|AmazonSDBClientMock
argument_list|()
expr_stmt|;
name|configuration
operator|=
operator|new
name|SdbConfiguration
argument_list|()
expr_stmt|;
name|configuration
operator|.
name|setDomainName
argument_list|(
literal|"DOMAIN1"
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setConsistentRead
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|exchange
operator|=
operator|new
name|DefaultExchange
argument_list|(
operator|new
name|DefaultCamelContext
argument_list|()
argument_list|)
expr_stmt|;
name|command
operator|=
operator|new
name|GetAttributesCommand
argument_list|(
name|sdbClient
argument_list|,
name|configuration
argument_list|,
name|exchange
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
DECL|method|execute ()
specifier|public
name|void
name|execute
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|attributeNames
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|attributeNames
operator|.
name|add
argument_list|(
literal|"ATTRIBUTE1"
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
name|command
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"DOMAIN1"
argument_list|,
name|sdbClient
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
name|sdbClient
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
name|sdbClient
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
name|sdbClient
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
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
DECL|method|executeWithoutItemName ()
specifier|public
name|void
name|executeWithoutItemName
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|attributeNames
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|attributeNames
operator|.
name|add
argument_list|(
literal|"ATTRIBUTE1"
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
name|command
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|determineAttributeNames ()
specifier|public
name|void
name|determineAttributeNames
parameter_list|()
block|{
name|assertNull
argument_list|(
name|this
operator|.
name|command
operator|.
name|determineAttributeNames
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|attributeNames
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|attributeNames
operator|.
name|add
argument_list|(
literal|"ATTRIBUTE1"
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
name|assertEquals
argument_list|(
name|attributeNames
argument_list|,
name|this
operator|.
name|command
operator|.
name|determineAttributeNames
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
