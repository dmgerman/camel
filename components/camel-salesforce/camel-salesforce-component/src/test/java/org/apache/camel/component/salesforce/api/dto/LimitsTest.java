begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|beans
operator|.
name|BeanInfo
import|;
end_import

begin_import
import|import
name|java
operator|.
name|beans
operator|.
name|IntrospectionException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|beans
operator|.
name|Introspector
import|;
end_import

begin_import
import|import
name|java
operator|.
name|beans
operator|.
name|PropertyDescriptor
import|;
end_import

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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|Limits
operator|.
name|Usage
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
name|CoreMatchers
operator|.
name|is
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
name|assertFalse
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_class
DECL|class|LimitsTest
specifier|public
class|class
name|LimitsTest
block|{
annotation|@
name|Test
DECL|method|shouldBeKnownIfDefined ()
specifier|public
name|void
name|shouldBeKnownIfDefined
parameter_list|()
block|{
name|assertFalse
argument_list|(
literal|"Known usage must not declare itself as unknown"
argument_list|,
operator|new
name|Usage
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
operator|.
name|isUnknown
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldDeserializeFromSalesforceGeneratedJSON ()
specifier|public
name|void
name|shouldDeserializeFromSalesforceGeneratedJSON
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
name|Limits
operator|.
name|class
argument_list|)
operator|.
name|readValue
argument_list|(
name|LimitsTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"/org/apache/camel/component/salesforce/api/dto/limits.json"
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
literal|"Limits should be parsed from JSON"
argument_list|,
name|read
argument_list|,
name|instanceOf
argument_list|(
name|Limits
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|Limits
name|limits
init|=
operator|(
name|Limits
operator|)
name|read
decl_stmt|;
specifier|final
name|Usage
name|dailyApiRequests
init|=
name|limits
operator|.
name|getDailyApiRequests
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
literal|"Should have some usage present"
argument_list|,
name|dailyApiRequests
operator|.
name|isUnknown
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Per application usage should be present"
argument_list|,
name|dailyApiRequests
operator|.
name|getPerApplicationUsage
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"'Camel Salesman' application usage should be present"
argument_list|,
name|dailyApiRequests
operator|.
name|forApplication
argument_list|(
literal|"Camel Salesman"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldSupportGettingAllDefinedUsages ()
specifier|public
name|void
name|shouldSupportGettingAllDefinedUsages
parameter_list|()
throws|throws
name|IntrospectionException
block|{
specifier|final
name|BeanInfo
name|beanInfo
init|=
name|Introspector
operator|.
name|getBeanInfo
argument_list|(
name|Limits
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|PropertyDescriptor
index|[]
name|propertyDescriptors
init|=
name|beanInfo
operator|.
name|getPropertyDescriptors
argument_list|()
decl_stmt|;
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|found
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|PropertyDescriptor
name|descriptor
range|:
name|propertyDescriptors
control|)
block|{
name|found
operator|.
name|add
argument_list|(
name|descriptor
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|defined
init|=
name|Arrays
operator|.
name|stream
argument_list|(
name|Limits
operator|.
name|Operation
operator|.
name|values
argument_list|()
argument_list|)
operator|.
name|map
argument_list|(
name|Limits
operator|.
name|Operation
operator|::
name|name
argument_list|)
operator|.
name|map
argument_list|(
name|Introspector
operator|::
name|decapitalize
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toSet
argument_list|()
argument_list|)
decl_stmt|;
name|defined
operator|.
name|removeAll
argument_list|(
name|found
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
literal|"All operations declared in Operation enum should have it's corresponding getter"
argument_list|,
name|defined
argument_list|,
name|is
argument_list|(
name|Collections
operator|.
name|emptySet
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|usageShouldBeUnknownIfUnknown ()
specifier|public
name|void
name|usageShouldBeUnknownIfUnknown
parameter_list|()
block|{
name|assertTrue
argument_list|(
literal|"Unknown usage must declare itself as such"
argument_list|,
name|Usage
operator|.
name|UNKNOWN
operator|.
name|isUnknown
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

