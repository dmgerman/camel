begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.tools.apt
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|tools
operator|.
name|apt
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
name|Collections
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|tools
operator|.
name|apt
operator|.
name|helper
operator|.
name|EndpointHelper
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
name|tools
operator|.
name|apt
operator|.
name|model
operator|.
name|EndpointOption
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
DECL|class|EndpointOptionComparatorTest
specifier|public
class|class
name|EndpointOptionComparatorTest
block|{
annotation|@
name|Test
DECL|method|testComparator ()
specifier|public
name|void
name|testComparator
parameter_list|()
block|{
name|String
name|label1
init|=
literal|"common"
decl_stmt|;
name|String
name|label2
init|=
literal|"advanced"
decl_stmt|;
name|String
name|label3
init|=
literal|"common"
decl_stmt|;
name|String
name|label4
init|=
literal|"filter"
decl_stmt|;
name|String
name|group1
init|=
name|EndpointHelper
operator|.
name|labelAsGroupName
argument_list|(
name|label1
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|String
name|group2
init|=
name|EndpointHelper
operator|.
name|labelAsGroupName
argument_list|(
name|label2
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|String
name|group3
init|=
name|EndpointHelper
operator|.
name|labelAsGroupName
argument_list|(
name|label3
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|String
name|group4
init|=
name|EndpointHelper
operator|.
name|labelAsGroupName
argument_list|(
name|label4
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|EndpointOption
name|op1
init|=
operator|new
name|EndpointOption
argument_list|(
literal|"first"
argument_list|,
literal|"First"
argument_list|,
literal|"string"
argument_list|,
literal|true
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|,
literal|"blah"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|,
name|group1
argument_list|,
name|label1
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|EndpointOption
name|op2
init|=
operator|new
name|EndpointOption
argument_list|(
literal|"synchronous"
argument_list|,
literal|"Synchronous"
argument_list|,
literal|"string"
argument_list|,
literal|true
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|,
literal|"blah"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|,
name|group2
argument_list|,
name|label2
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|EndpointOption
name|op3
init|=
operator|new
name|EndpointOption
argument_list|(
literal|"second"
argument_list|,
literal|"Second"
argument_list|,
literal|"string"
argument_list|,
literal|true
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|,
literal|"blah"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|,
name|group3
argument_list|,
name|label3
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|EndpointOption
name|op4
init|=
operator|new
name|EndpointOption
argument_list|(
literal|"country"
argument_list|,
literal|"Country"
argument_list|,
literal|"string"
argument_list|,
literal|true
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|,
literal|"blah"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|,
name|group4
argument_list|,
name|label4
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|EndpointOption
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|op1
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|op2
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|op3
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|op4
argument_list|)
expr_stmt|;
comment|// then by label into the groups
name|Collections
operator|.
name|sort
argument_list|(
name|list
argument_list|,
name|EndpointHelper
operator|.
name|createGroupAndLabelComparator
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"first"
argument_list|,
name|list
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
comment|// common
name|assertEquals
argument_list|(
literal|"second"
argument_list|,
name|list
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
comment|// common
name|assertEquals
argument_list|(
literal|"synchronous"
argument_list|,
name|list
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// advanced
name|assertEquals
argument_list|(
literal|"country"
argument_list|,
name|list
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// filter
block|}
block|}
end_class

end_unit

