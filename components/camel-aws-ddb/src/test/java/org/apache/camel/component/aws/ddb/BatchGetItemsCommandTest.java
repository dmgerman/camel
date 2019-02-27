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
name|KeysAndAttributes
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

begin_class
DECL|class|BatchGetItemsCommandTest
specifier|public
class|class
name|BatchGetItemsCommandTest
block|{
DECL|field|command
specifier|private
name|BatchGetItemsCommand
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
name|BatchGetItemsCommand
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
name|key
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|key
operator|.
name|put
argument_list|(
literal|"1"
argument_list|,
operator|new
name|AttributeValue
argument_list|(
literal|"Key_1"
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|AttributeValue
argument_list|>
name|unprocessedKey
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|unprocessedKey
operator|.
name|put
argument_list|(
literal|"1"
argument_list|,
operator|new
name|AttributeValue
argument_list|(
literal|"UNPROCESSED_KEY"
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|KeysAndAttributes
argument_list|>
name|keysAndAttributesMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|KeysAndAttributes
name|keysAndAttributes
init|=
operator|new
name|KeysAndAttributes
argument_list|()
operator|.
name|withKeys
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|keysAndAttributesMap
operator|.
name|put
argument_list|(
literal|"DOMAIN1"
argument_list|,
name|keysAndAttributes
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
name|BATCH_ITEMS
argument_list|,
name|keysAndAttributesMap
argument_list|)
expr_stmt|;
name|command
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|keysAndAttributesMap
argument_list|,
name|ddbClient
operator|.
name|batchGetItemRequest
operator|.
name|getRequestItems
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|AttributeValue
argument_list|>
argument_list|>
name|batchResponse
init|=
operator|(
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|AttributeValue
argument_list|>
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
name|BATCH_RESPONSE
argument_list|,
name|Map
operator|.
name|class
argument_list|)
operator|.
name|get
argument_list|(
literal|"DOMAIN1"
argument_list|)
decl_stmt|;
name|AttributeValue
name|value
init|=
name|batchResponse
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|get
argument_list|(
literal|"attrName"
argument_list|)
decl_stmt|;
name|KeysAndAttributes
name|unProcessedAttributes
init|=
operator|(
name|KeysAndAttributes
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
name|UNPROCESSED_KEYS
argument_list|,
name|Map
operator|.
name|class
argument_list|)
operator|.
name|get
argument_list|(
literal|"DOMAIN1"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|AttributeValue
argument_list|>
name|next
init|=
name|unProcessedAttributes
operator|.
name|getKeys
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|AttributeValue
argument_list|(
literal|"attrValue"
argument_list|)
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|unprocessedKey
argument_list|,
name|next
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
