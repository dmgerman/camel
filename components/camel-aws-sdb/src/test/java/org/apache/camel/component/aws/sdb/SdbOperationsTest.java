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
DECL|class|SdbOperationsTest
specifier|public
class|class
name|SdbOperationsTest
block|{
annotation|@
name|Test
DECL|method|supportedOperationCount ()
specifier|public
name|void
name|supportedOperationCount
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|9
argument_list|,
name|SdbOperations
operator|.
name|values
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|valueOf ()
specifier|public
name|void
name|valueOf
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|SdbOperations
operator|.
name|BatchDeleteAttributes
argument_list|,
name|SdbOperations
operator|.
name|valueOf
argument_list|(
literal|"BatchDeleteAttributes"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SdbOperations
operator|.
name|BatchPutAttributes
argument_list|,
name|SdbOperations
operator|.
name|valueOf
argument_list|(
literal|"BatchPutAttributes"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SdbOperations
operator|.
name|DeleteAttributes
argument_list|,
name|SdbOperations
operator|.
name|valueOf
argument_list|(
literal|"DeleteAttributes"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SdbOperations
operator|.
name|DeleteDomain
argument_list|,
name|SdbOperations
operator|.
name|valueOf
argument_list|(
literal|"DeleteDomain"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SdbOperations
operator|.
name|DomainMetadata
argument_list|,
name|SdbOperations
operator|.
name|valueOf
argument_list|(
literal|"DomainMetadata"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SdbOperations
operator|.
name|GetAttributes
argument_list|,
name|SdbOperations
operator|.
name|valueOf
argument_list|(
literal|"GetAttributes"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SdbOperations
operator|.
name|ListDomains
argument_list|,
name|SdbOperations
operator|.
name|valueOf
argument_list|(
literal|"ListDomains"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SdbOperations
operator|.
name|PutAttributes
argument_list|,
name|SdbOperations
operator|.
name|valueOf
argument_list|(
literal|"PutAttributes"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SdbOperations
operator|.
name|Select
argument_list|,
name|SdbOperations
operator|.
name|valueOf
argument_list|(
literal|"Select"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testToString ()
specifier|public
name|void
name|testToString
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|SdbOperations
operator|.
name|BatchDeleteAttributes
operator|.
name|toString
argument_list|()
argument_list|,
literal|"BatchDeleteAttributes"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SdbOperations
operator|.
name|BatchPutAttributes
operator|.
name|toString
argument_list|()
argument_list|,
literal|"BatchPutAttributes"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SdbOperations
operator|.
name|DeleteAttributes
operator|.
name|toString
argument_list|()
argument_list|,
literal|"DeleteAttributes"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SdbOperations
operator|.
name|DeleteDomain
operator|.
name|toString
argument_list|()
argument_list|,
literal|"DeleteDomain"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SdbOperations
operator|.
name|DomainMetadata
operator|.
name|toString
argument_list|()
argument_list|,
literal|"DomainMetadata"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SdbOperations
operator|.
name|GetAttributes
operator|.
name|toString
argument_list|()
argument_list|,
literal|"GetAttributes"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SdbOperations
operator|.
name|ListDomains
operator|.
name|toString
argument_list|()
argument_list|,
literal|"ListDomains"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SdbOperations
operator|.
name|PutAttributes
operator|.
name|toString
argument_list|()
argument_list|,
literal|"PutAttributes"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SdbOperations
operator|.
name|Select
operator|.
name|toString
argument_list|()
argument_list|,
literal|"Select"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

