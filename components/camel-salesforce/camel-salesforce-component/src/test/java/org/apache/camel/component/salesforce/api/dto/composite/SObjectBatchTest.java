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
name|regex
operator|.
name|Pattern
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
name|dto
operator|.
name|composite
operator|.
name|SObjectBatch
operator|.
name|Method
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
name|Account_IndustryEnum
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
DECL|class|SObjectBatchTest
specifier|public
class|class
name|SObjectBatchTest
block|{
DECL|field|batch
specifier|private
specifier|final
name|SObjectBatch
name|batch
decl_stmt|;
DECL|method|SObjectBatchTest ()
specifier|public
name|SObjectBatchTest
parameter_list|()
block|{
name|batch
operator|=
operator|new
name|SObjectBatch
argument_list|(
literal|"37.0"
argument_list|)
expr_stmt|;
specifier|final
name|Account
name|account
init|=
operator|new
name|Account
argument_list|()
decl_stmt|;
name|account
operator|.
name|setName
argument_list|(
literal|"NewAccountName"
argument_list|)
expr_stmt|;
name|account
operator|.
name|setIndustry
argument_list|(
name|Account_IndustryEnum
operator|.
name|ENVIRONMENTAL
argument_list|)
expr_stmt|;
name|batch
operator|.
name|addCreate
argument_list|(
name|account
argument_list|)
expr_stmt|;
name|batch
operator|.
name|addDelete
argument_list|(
literal|"Account"
argument_list|,
literal|"001D000000K0fXOIAZ"
argument_list|)
expr_stmt|;
name|batch
operator|.
name|addGet
argument_list|(
literal|"Account"
argument_list|,
literal|"001D000000K0fXOIAZ"
argument_list|,
literal|"Name"
argument_list|,
literal|"BillingPostalCode"
argument_list|)
expr_stmt|;
name|batch
operator|.
name|addGetByExternalId
argument_list|(
literal|"Account"
argument_list|,
literal|"EPK"
argument_list|,
literal|"12345"
argument_list|)
expr_stmt|;
name|batch
operator|.
name|addGetRelated
argument_list|(
literal|"Account"
argument_list|,
literal|"001D000000K0fXOIAZ"
argument_list|,
literal|"CreatedBy"
argument_list|,
literal|"Name"
argument_list|)
expr_stmt|;
name|batch
operator|.
name|addLimits
argument_list|()
expr_stmt|;
specifier|final
name|Account
name|updates1
init|=
operator|new
name|Account
argument_list|()
decl_stmt|;
name|updates1
operator|.
name|setName
argument_list|(
literal|"NewName"
argument_list|)
expr_stmt|;
name|updates1
operator|.
name|setAccountNumber
argument_list|(
literal|"AC12345"
argument_list|)
expr_stmt|;
name|batch
operator|.
name|addUpdate
argument_list|(
literal|"Account"
argument_list|,
literal|"001D000000K0fXOIAZ"
argument_list|,
name|updates1
argument_list|)
expr_stmt|;
specifier|final
name|Account
name|updates2
init|=
operator|new
name|Account
argument_list|()
decl_stmt|;
name|updates2
operator|.
name|setName
argument_list|(
literal|"NewName"
argument_list|)
expr_stmt|;
name|batch
operator|.
name|addUpdateByExternalId
argument_list|(
literal|"Account"
argument_list|,
literal|"EPK"
argument_list|,
literal|"12345"
argument_list|,
name|updates2
argument_list|)
expr_stmt|;
specifier|final
name|Account
name|updates3
init|=
operator|new
name|Account
argument_list|()
decl_stmt|;
name|updates3
operator|.
name|setName
argument_list|(
literal|"NewName"
argument_list|)
expr_stmt|;
name|batch
operator|.
name|addUpsertByExternalId
argument_list|(
literal|"Account"
argument_list|,
literal|"EPK"
argument_list|,
literal|"12345"
argument_list|,
name|updates3
argument_list|)
expr_stmt|;
name|batch
operator|.
name|addGeneric
argument_list|(
name|Method
operator|.
name|PATCH
argument_list|,
literal|"/some/url"
argument_list|)
expr_stmt|;
name|batch
operator|.
name|addQuery
argument_list|(
literal|"SELECT Name FROM Account"
argument_list|)
expr_stmt|;
name|batch
operator|.
name|addQueryAll
argument_list|(
literal|"SELECT Name FROM Account"
argument_list|)
expr_stmt|;
name|batch
operator|.
name|addSearch
argument_list|(
literal|"FIND {joe}"
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
name|String
name|json
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"\\s+(?=([^\"]*\"[^\"]*\")*[^\"]*$)"
argument_list|,
name|Pattern
operator|.
name|DOTALL
argument_list|)
operator|.
name|matcher
argument_list|(
literal|"{\n"
operator|+
literal|"  \"batchRequests\": [\n"
operator|+
literal|"    {\n"
operator|+
literal|"      \"method\": \"POST\",\n"
operator|+
literal|"      \"url\": \"v37.0/sobjects/Account/\",\n"
operator|+
literal|"      \"richInput\": {\n"
operator|+
literal|"        \"attributes\": {\n"
operator|+
literal|"          \"type\": \"Account\"\n"
operator|+
literal|"        },\n"
operator|+
literal|"        \"Industry\": \"Environmental\",\n"
operator|+
literal|"        \"Name\": \"NewAccountName\"\n"
operator|+
literal|"      }\n"
operator|+
literal|"    },\n"
operator|+
literal|"    {\n"
operator|+
literal|"      \"method\": \"DELETE\",\n"
operator|+
literal|"      \"url\": \"v37.0/sobjects/Account/001D000000K0fXOIAZ\"\n"
operator|+
literal|"    },\n"
operator|+
literal|"    {\n"
operator|+
literal|"      \"method\": \"GET\",\n"
operator|+
literal|"      \"url\": \"v37.0/sobjects/Account/001D000000K0fXOIAZ?fields=Name,BillingPostalCode\"\n"
operator|+
literal|"    },\n"
operator|+
literal|"    {\n"
operator|+
literal|"      \"method\": \"GET\",\n"
operator|+
literal|"      \"url\": \"v37.0/sobjects/Account/EPK/12345\"\n"
operator|+
literal|"    },\n"
operator|+
literal|"    {\n"
operator|+
literal|"      \"method\": \"GET\",\n"
operator|+
literal|"      \"url\": \"v37.0/sobjects/Account/001D000000K0fXOIAZ/CreatedBy?fields=Name\"\n"
operator|+
literal|"    },\n"
operator|+
literal|"    {\n"
operator|+
literal|"      \"method\": \"GET\",\n"
operator|+
literal|"      \"url\": \"v37.0/limits/\"\n"
operator|+
literal|"    },\n"
operator|+
literal|"    {\n"
operator|+
literal|"      \"method\": \"PATCH\",\n"
operator|+
literal|"      \"url\": \"v37.0/sobjects/Account/001D000000K0fXOIAZ\",\n"
operator|+
literal|"      \"richInput\": {\n"
operator|+
literal|"        \"attributes\": {\n"
operator|+
literal|"          \"type\": \"Account\"\n"
operator|+
literal|"        },\n"
operator|+
literal|"        \"AccountNumber\": \"AC12345\",\n"
operator|+
literal|"        \"Name\": \"NewName\"\n"
operator|+
literal|"      }\n"
operator|+
literal|"    },\n"
operator|+
literal|"    {\n"
operator|+
literal|"      \"method\": \"PATCH\",\n"
operator|+
literal|"      \"url\": \"v37.0/sobjects/Account/EPK/12345\",\n"
operator|+
literal|"      \"richInput\": {\n"
operator|+
literal|"        \"attributes\": {\n"
operator|+
literal|"          \"type\": \"Account\"\n"
operator|+
literal|"        },\n"
operator|+
literal|"        \"Name\": \"NewName\"\n"
operator|+
literal|"      }\n"
operator|+
literal|"    },\n"
operator|+
literal|"    {\n"
operator|+
literal|"      \"method\": \"PATCH\",\n"
operator|+
literal|"      \"url\": \"v37.0/sobjects/Account/EPK/12345\",\n"
operator|+
literal|"      \"richInput\": {\n"
operator|+
literal|"        \"attributes\": {\n"
operator|+
literal|"          \"type\": \"Account\"\n"
operator|+
literal|"        },\n"
operator|+
literal|"        \"Name\": \"NewName\"\n"
operator|+
literal|"      }\n"
operator|+
literal|"    },\n"
operator|+
literal|"    {\n"
operator|+
literal|"      \"method\": \"PATCH\",\n"
operator|+
literal|"      \"url\": \"v37.0/some/url\"\n"
operator|+
literal|"    },\n"
operator|+
literal|"    {\n"
operator|+
literal|"      \"method\": \"GET\",\n"
operator|+
literal|"      \"url\": \"v37.0/query/?q=SELECT Name FROM Account\"\n"
operator|+
literal|"    },\n"
operator|+
literal|"    {\n"
operator|+
literal|"      \"method\": \"GET\",\n"
operator|+
literal|"      \"url\": \"v37.0/queryAll/?q=SELECT Name FROM Account\"\n"
operator|+
literal|"    },\n"
operator|+
literal|"    {\n"
operator|+
literal|"      \"method\": \"GET\",\n"
operator|+
literal|"      \"url\": \"v37.0/search/?q=FIND {joe}\"\n"
operator|+
literal|"    }\n"
operator|+
literal|"  ]\n"
operator|+
literal|"}"
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|""
argument_list|)
decl_stmt|;
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
name|String
name|serialized
init|=
name|mapper
operator|.
name|writerFor
argument_list|(
name|SObjectBatch
operator|.
name|class
argument_list|)
operator|.
name|writeValueAsString
argument_list|(
name|batch
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Should serialize as expected by Salesforce"
argument_list|,
name|json
argument_list|,
name|serialized
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
name|String
name|xml
init|=
literal|"<batch>"
comment|//
operator|+
literal|"<batchRequests>"
comment|//
operator|+
literal|"<batchRequest>"
comment|//
operator|+
literal|"<method>POST</method>"
comment|//
operator|+
literal|"<url>v37.0/sobjects/Account/</url>"
comment|//
operator|+
literal|"<richInput>"
comment|//
operator|+
literal|"<Account>"
comment|//
operator|+
literal|"<Name>NewAccountName</Name>"
comment|//
operator|+
literal|"<Industry>Environmental</Industry>"
comment|//
operator|+
literal|"</Account>"
comment|//
operator|+
literal|"</richInput>"
comment|//
operator|+
literal|"</batchRequest>"
comment|//
operator|+
literal|"<batchRequest>"
comment|//
operator|+
literal|"<method>DELETE</method>"
comment|//
operator|+
literal|"<url>v37.0/sobjects/Account/001D000000K0fXOIAZ</url>"
comment|//
operator|+
literal|"</batchRequest>"
comment|//
operator|+
literal|"<batchRequest>"
comment|//
operator|+
literal|"<method>GET</method>"
comment|//
operator|+
literal|"<url>v37.0/sobjects/Account/001D000000K0fXOIAZ?fields=Name,BillingPostalCode</url>"
comment|//
operator|+
literal|"</batchRequest>"
comment|//
operator|+
literal|"<batchRequest>"
comment|//
operator|+
literal|"<method>GET</method>"
comment|//
operator|+
literal|"<url>v37.0/sobjects/Account/EPK/12345</url>"
comment|//
operator|+
literal|"</batchRequest>"
comment|//
operator|+
literal|"<batchRequest>"
comment|//
operator|+
literal|"<method>GET</method>"
comment|//
operator|+
literal|"<url>v37.0/sobjects/Account/001D000000K0fXOIAZ/CreatedBy?fields=Name</url>"
comment|//
operator|+
literal|"</batchRequest>"
comment|//
operator|+
literal|"<batchRequest>"
comment|//
operator|+
literal|"<method>GET</method>"
comment|//
operator|+
literal|"<url>v37.0/limits/</url>"
comment|//
operator|+
literal|"</batchRequest>"
comment|//
operator|+
literal|"<batchRequest>"
comment|//
operator|+
literal|"<method>PATCH</method>"
comment|//
operator|+
literal|"<url>v37.0/sobjects/Account/001D000000K0fXOIAZ</url>"
comment|//
operator|+
literal|"<richInput>"
comment|//
operator|+
literal|"<Account>"
comment|//
operator|+
literal|"<Name>NewName</Name>"
comment|//
operator|+
literal|"<AccountNumber>AC12345</AccountNumber>"
comment|//
operator|+
literal|"</Account>"
comment|//
operator|+
literal|"</richInput>"
comment|//
operator|+
literal|"</batchRequest>"
comment|//
operator|+
literal|"<batchRequest>"
comment|//
operator|+
literal|"<method>PATCH</method>"
comment|//
operator|+
literal|"<url>v37.0/sobjects/Account/EPK/12345</url>"
comment|//
operator|+
literal|"<richInput>"
comment|//
operator|+
literal|"<Account>"
comment|//
operator|+
literal|"<Name>NewName</Name>"
comment|//
operator|+
literal|"</Account>"
comment|//
operator|+
literal|"</richInput>"
comment|//
operator|+
literal|"</batchRequest>"
comment|//
operator|+
literal|"<batchRequest>"
comment|//
operator|+
literal|"<method>PATCH</method>"
comment|//
operator|+
literal|"<url>v37.0/sobjects/Account/EPK/12345</url>"
comment|//
operator|+
literal|"<richInput>"
comment|//
operator|+
literal|"<Account>"
comment|//
operator|+
literal|"<Name>NewName</Name>"
comment|//
operator|+
literal|"</Account>"
comment|//
operator|+
literal|"</richInput>"
comment|//
operator|+
literal|"</batchRequest>"
comment|//
operator|+
literal|"<batchRequest>"
comment|//
operator|+
literal|"<method>PATCH</method>"
comment|//
operator|+
literal|"<url>v37.0/some/url</url>"
comment|//
operator|+
literal|"</batchRequest>"
comment|//
operator|+
literal|"<batchRequest>"
comment|//
operator|+
literal|"<method>GET</method>"
comment|//
operator|+
literal|"<url>v37.0/query/?q=SELECT Name FROM Account</url>"
comment|//
operator|+
literal|"</batchRequest>"
comment|//
operator|+
literal|"<batchRequest>"
comment|//
operator|+
literal|"<method>GET</method>"
comment|//
operator|+
literal|"<url>v37.0/queryAll/?q=SELECT Name FROM Account</url>"
comment|//
operator|+
literal|"</batchRequest>"
comment|//
operator|+
literal|"<batchRequest>"
comment|//
operator|+
literal|"<method>GET</method>"
comment|//
operator|+
literal|"<url>v37.0/search/?q=FIND {joe}</url>"
comment|//
operator|+
literal|"</batchRequest>"
comment|//
operator|+
literal|"</batchRequests>"
comment|//
operator|+
literal|"</batch>"
decl_stmt|;
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|classes
init|=
operator|new
name|Class
index|[
name|batch
operator|.
name|objectTypes
argument_list|()
operator|.
name|length
operator|+
literal|1
index|]
decl_stmt|;
name|classes
index|[
literal|0
index|]
operator|=
name|SObjectBatch
operator|.
name|class
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|batch
operator|.
name|objectTypes
argument_list|()
argument_list|,
literal|0
argument_list|,
name|classes
argument_list|,
literal|1
argument_list|,
name|batch
operator|.
name|objectTypes
argument_list|()
operator|.
name|length
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
name|classes
argument_list|)
decl_stmt|;
specifier|final
name|String
name|serialized
init|=
name|xStream
operator|.
name|toXML
argument_list|(
name|batch
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Should serialize as expected by Salesforce"
argument_list|,
name|xml
argument_list|,
name|serialized
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

