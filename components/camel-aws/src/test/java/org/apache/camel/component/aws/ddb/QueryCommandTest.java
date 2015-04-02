begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.ddb
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
name|ddb
package|;
end_package

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
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|Map
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
name|impl
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
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|dynamodbv2
operator|.
name|model
operator|.
name|AttributeValue
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
name|dynamodbv2
operator|.
name|model
operator|.
name|ComparisonOperator
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
name|dynamodbv2
operator|.
name|model
operator|.
name|Condition
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
name|dynamodbv2
operator|.
name|model
operator|.
name|ConsumedCapacity
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
name|dynamodbv2
operator|.
name|model
operator|.
name|KeySchemaElement
import|;
end_import

begin_class
DECL|class|QueryCommandTest
specifier|public
class|class
name|QueryCommandTest
block|{
DECL|field|command
specifier|private
name|QueryCommand
name|command
decl_stmt|;
DECL|field|ddbClient
specifier|private
name|AmazonDDBClientMock
name|ddbClient
decl_stmt|;
DECL|field|configuration
specifier|private
name|DdbConfiguration
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
name|ddbClient
operator|=
operator|new
name|AmazonDDBClientMock
argument_list|()
expr_stmt|;
name|configuration
operator|=
operator|new
name|DdbConfiguration
argument_list|()
expr_stmt|;
name|configuration
operator|.
name|setTableName
argument_list|(
literal|"DOMAIN1"
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
name|QueryCommand
argument_list|(
name|ddbClient
argument_list|,
name|configuration
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|execute ()
specifier|public
name|void
name|execute
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|AttributeValue
argument_list|>
name|startKey
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|AttributeValue
argument_list|>
argument_list|()
decl_stmt|;
name|startKey
operator|.
name|put
argument_list|(
literal|"1"
argument_list|,
operator|new
name|AttributeValue
argument_list|(
literal|"startKey"
argument_list|)
argument_list|)
expr_stmt|;
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
literal|"attrNameOne"
argument_list|,
literal|"attrNameTwo"
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|DdbConstants
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
name|DdbConstants
operator|.
name|CONSISTENT_READ
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|DdbConstants
operator|.
name|START_KEY
argument_list|,
name|startKey
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|DdbConstants
operator|.
name|LIMIT
argument_list|,
literal|10
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|DdbConstants
operator|.
name|SCAN_INDEX_FORWARD
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Condition
argument_list|>
name|keyConditions
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Condition
argument_list|>
argument_list|()
decl_stmt|;
name|Condition
name|condition
init|=
operator|new
name|Condition
argument_list|()
operator|.
name|withComparisonOperator
argument_list|(
name|ComparisonOperator
operator|.
name|GT
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|withAttributeValueList
argument_list|(
operator|new
name|AttributeValue
argument_list|()
operator|.
name|withN
argument_list|(
literal|"1985"
argument_list|)
argument_list|)
decl_stmt|;
name|keyConditions
operator|.
name|put
argument_list|(
literal|"1"
argument_list|,
name|condition
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|DdbConstants
operator|.
name|KEY_CONDITIONS
argument_list|,
name|keyConditions
argument_list|)
expr_stmt|;
name|command
operator|.
name|execute
argument_list|()
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|AttributeValue
argument_list|>
name|mapAssert
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|AttributeValue
argument_list|>
argument_list|()
decl_stmt|;
name|mapAssert
operator|.
name|put
argument_list|(
literal|"1"
argument_list|,
operator|new
name|AttributeValue
argument_list|(
literal|"LAST_KEY"
argument_list|)
argument_list|)
expr_stmt|;
name|ConsumedCapacity
name|consumed
init|=
operator|(
name|ConsumedCapacity
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|DdbConstants
operator|.
name|CONSUMED_CAPACITY
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|1
argument_list|)
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|DdbConstants
operator|.
name|COUNT
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Double
operator|.
name|valueOf
argument_list|(
literal|1.0
argument_list|)
argument_list|,
name|consumed
operator|.
name|getCapacityUnits
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|mapAssert
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|DdbConstants
operator|.
name|LAST_EVALUATED_KEY
argument_list|,
name|Map
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|keyConditions
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|DdbConstants
operator|.
name|KEY_CONDITIONS
argument_list|,
name|Map
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|items
init|=
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|DdbConstants
operator|.
name|ITEMS
argument_list|,
name|List
operator|.
name|class
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|AttributeValue
argument_list|(
literal|"attrValue"
argument_list|)
argument_list|,
name|items
operator|.
name|get
argument_list|(
literal|"attrName"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

