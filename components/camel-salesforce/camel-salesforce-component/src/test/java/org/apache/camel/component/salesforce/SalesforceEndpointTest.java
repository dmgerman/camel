begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce
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
name|stream
operator|.
name|Collectors
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
name|OperationName
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
name|spi
operator|.
name|UriPath
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
DECL|class|SalesforceEndpointTest
specifier|public
class|class
name|SalesforceEndpointTest
block|{
annotation|@
name|Test
DECL|method|allOperationValuesShouldBeListedInOperationNameUriPath ()
specifier|public
name|void
name|allOperationValuesShouldBeListedInOperationNameUriPath
parameter_list|()
throws|throws
name|NoSuchFieldException
throws|,
name|SecurityException
block|{
name|UriPath
name|uriPath
init|=
name|SalesforceEndpoint
operator|.
name|class
operator|.
name|getDeclaredField
argument_list|(
literal|"operationName"
argument_list|)
operator|.
name|getAnnotation
argument_list|(
name|UriPath
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
index|[]
name|operationNamesInAnnotation
init|=
name|uriPath
operator|.
name|enums
argument_list|()
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
name|Arrays
operator|.
name|sort
argument_list|(
name|operationNamesInAnnotation
argument_list|)
expr_stmt|;
name|String
index|[]
name|operationNamesInEnum
init|=
name|Arrays
operator|.
name|stream
argument_list|(
name|OperationName
operator|.
name|values
argument_list|()
argument_list|)
operator|.
name|map
argument_list|(
name|OperationName
operator|::
name|value
argument_list|)
operator|.
name|toArray
argument_list|(
name|length
lambda|->
operator|new
name|String
index|[
name|length
index|]
argument_list|)
decl_stmt|;
name|Arrays
operator|.
name|sort
argument_list|(
name|operationNamesInEnum
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertArrayEquals
argument_list|(
literal|"All operation values, the String value returned from OperationName::value, must be defined in the @UriPath "
operator|+
literal|"enum parameter of the operationName field in SalesforceEndpoint, set the enums parameter to:\n"
operator|+
name|Arrays
operator|.
name|stream
argument_list|(
name|operationNamesInEnum
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|joining
argument_list|(
literal|","
argument_list|)
argument_list|)
argument_list|,
name|operationNamesInEnum
argument_list|,
name|operationNamesInAnnotation
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

