begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.internal
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
name|internal
package|;
end_package

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
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
operator|.
name|internal
operator|.
name|dto
operator|.
name|LoginToken
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
DECL|class|LoginTokenTest
specifier|public
class|class
name|LoginTokenTest
block|{
annotation|@
name|Test
DECL|method|testLoginTokenWithUnknownFields ()
specifier|public
name|void
name|testLoginTokenWithUnknownFields
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|salesforceOAuthResponse
init|=
literal|"{\n"
operator|+
literal|"    \"access_token\": \"00XXXXXXXXXXXX!ARMAQKg_lg_hGaRElvizVFBQHoCpvX8tzwGnROQ0_MDPXSceMeZHtm3JHkPmMhlgK0Km3rpJkwxwHInd_8o022KsDy.p4O.X\",\n"
operator|+
literal|"    \"is_readonly\": \"false\",\n"
operator|+
literal|"    \"signature\": \"XXXXXXXXXX+MYU+JrOXPSbpHa2ihMpSvUqow1iTPh7Q=\",\n"
operator|+
literal|"    \"instance_url\": \"https://xxxxxxxx--xxxxxxx.cs5.my.salesforce.com\",\n"
operator|+
literal|"    \"id\": \"https://test.salesforce.com/id/00DO00000054tO8MAI/005O0000001cmmdIAA\",\n"
operator|+
literal|"    \"token_type\": \"Bearer\",\n"
operator|+
literal|"    \"issued_at\": \"1442798068621\",\n"
operator|+
literal|"    \"an_unrecognised_field\": \"foo\"\n"
operator|+
literal|"}"
decl_stmt|;
name|ObjectMapper
name|mapper
init|=
name|JsonUtils
operator|.
name|createObjectMapper
argument_list|()
decl_stmt|;
name|Exception
name|e
init|=
literal|null
decl_stmt|;
name|LoginToken
name|token
init|=
literal|null
decl_stmt|;
try|try
block|{
name|token
operator|=
name|mapper
operator|.
name|readValue
argument_list|(
name|salesforceOAuthResponse
argument_list|,
name|LoginToken
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|e
operator|=
name|ex
expr_stmt|;
block|}
comment|// assert ObjectMapper deserialized the SF OAuth response and returned a
comment|// valid token back
name|assertNotNull
argument_list|(
literal|"An invalid token was returned"
argument_list|,
name|token
argument_list|)
expr_stmt|;
comment|// assert No exception was thrown during the JSON deserialization
comment|// process
name|assertNull
argument_list|(
literal|"Exception was thrown during JSON deserialisation"
argument_list|,
name|e
argument_list|)
expr_stmt|;
comment|// assert one of the token fields
name|assertEquals
argument_list|(
literal|"false"
argument_list|,
name|token
operator|.
name|getIsReadOnly
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

