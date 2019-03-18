begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.api.dto
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
name|ObjectMapper
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
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|core
operator|.
name|IsInstanceOf
operator|.
name|instanceOf
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
name|assertThat
import|;
end_import

begin_class
DECL|class|RecentItemTest
specifier|public
class|class
name|RecentItemTest
block|{
annotation|@
name|Test
DECL|method|shouldDeserializeFromJSON ()
specifier|public
name|void
name|shouldDeserializeFromJSON
parameter_list|()
throws|throws
name|JsonProcessingException
throws|,
name|IOException
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
specifier|final
name|Object
name|read
init|=
name|mapper
operator|.
name|readerFor
argument_list|(
name|RecentItem
operator|.
name|class
argument_list|)
operator|.
name|readValue
argument_list|(
literal|"{ \n"
operator|+
comment|//
literal|"    \"attributes\" : \n"
operator|+
comment|//
literal|"    { \n"
operator|+
comment|//
literal|"        \"type\" : \"Account\", \n"
operator|+
comment|//
literal|"        \"url\" : \"/services/data/v28.0/sobjects/Account/a06U000000CelH0IAJ\" \n"
operator|+
comment|//
literal|"    }, \n"
operator|+
comment|//
literal|"    \"Id\" : \"a06U000000CelH0IAJ\", \n"
operator|+
comment|//
literal|"    \"Name\" : \"Acme\" \n"
operator|+
comment|//
literal|"}"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
literal|"RecentItem should deserialize from JSON"
argument_list|,
name|read
argument_list|,
name|instanceOf
argument_list|(
name|RecentItem
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|RecentItem
name|recentItem
init|=
operator|(
name|RecentItem
operator|)
name|read
decl_stmt|;
name|assertEquals
argument_list|(
literal|"RecentItem.Id should be deserialized"
argument_list|,
name|recentItem
operator|.
name|getId
argument_list|()
argument_list|,
literal|"a06U000000CelH0IAJ"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"RecentItem.Name should be deserialized"
argument_list|,
name|recentItem
operator|.
name|getName
argument_list|()
argument_list|,
literal|"Acme"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"RecentItem.attributes should be deserialized"
argument_list|,
name|recentItem
operator|.
name|getAttributes
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"RecentItem.attributes.type should be deserialized"
argument_list|,
name|recentItem
operator|.
name|getAttributes
argument_list|()
operator|.
name|getType
argument_list|()
argument_list|,
literal|"Account"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"RecentItem.attributes.url should be deserialized"
argument_list|,
name|recentItem
operator|.
name|getAttributes
argument_list|()
operator|.
name|getUrl
argument_list|()
argument_list|,
literal|"/services/data/v28.0/sobjects/Account/a06U000000CelH0IAJ"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

