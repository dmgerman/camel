begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.api.dto.approval
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
name|approval
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
name|dto
operator|.
name|approval
operator|.
name|Approvals
operator|.
name|Info
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
name|assertNull
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
DECL|class|ApprovalsTest
specifier|public
class|class
name|ApprovalsTest
block|{
annotation|@
name|Test
DECL|method|shouldDeserialize ()
specifier|public
name|void
name|shouldDeserialize
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
name|Approvals
operator|.
name|class
argument_list|)
operator|.
name|readValue
argument_list|(
literal|"{\n"
operator|+
comment|//
literal|"  \"approvals\" : {\n"
operator|+
comment|//
literal|"   \"Account\" : [ {\n"
operator|+
comment|//
literal|"     \"description\" : null,\n"
operator|+
comment|//
literal|"     \"id\" : \"04aD00000008Py9\",\n"
operator|+
comment|//
literal|"     \"name\" : \"Account Approval Process\",\n"
operator|+
comment|//
literal|"     \"object\" : \"Account\",\n"
operator|+
comment|//
literal|"     \"sortOrder\" : 1\n"
operator|+
comment|//
literal|"   } ]\n"
operator|+
comment|//
literal|"  }\n"
operator|+
comment|//
literal|"}"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
literal|"Should deserialize Approvals"
argument_list|,
name|read
argument_list|,
name|instanceOf
argument_list|(
name|Approvals
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|Approvals
name|approvals
init|=
operator|(
name|Approvals
operator|)
name|read
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|Info
argument_list|>
argument_list|>
name|approvalsMap
init|=
name|approvals
operator|.
name|getApprovals
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Deserialized approvals should have one entry"
argument_list|,
literal|1
argument_list|,
name|approvalsMap
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|List
argument_list|<
name|Info
argument_list|>
name|accountApprovals
init|=
name|approvalsMap
operator|.
name|get
argument_list|(
literal|"Account"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Deserialized approvals should contain list of `Account` type approvals"
argument_list|,
name|accountApprovals
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"There should be one approval of `Account` type"
argument_list|,
literal|1
argument_list|,
name|accountApprovals
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|Info
name|accountInfo
init|=
name|accountApprovals
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
literal|"Deserialized `Account` approval should have null description"
argument_list|,
name|accountInfo
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Deserialized `Account` approval should have defined id"
argument_list|,
literal|"04aD00000008Py9"
argument_list|,
name|accountInfo
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Deserialized `Account` approval should have defined name"
argument_list|,
literal|"Account Approval Process"
argument_list|,
name|accountInfo
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Deserialized `Account` approval should have defined object"
argument_list|,
literal|"Account"
argument_list|,
name|accountInfo
operator|.
name|getObject
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Deserialized `Account` approval should have defined sortOrder"
argument_list|,
literal|1
argument_list|,
name|accountInfo
operator|.
name|getSortOrder
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

