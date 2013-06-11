begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
package|;
end_package

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
name|java
operator|.
name|util
operator|.
name|Properties
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
name|SortedMap
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
name|CamelContext
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
name|util
operator|.
name|CamelContextHelper
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
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|TestSupport
operator|.
name|isJavaVersion
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
name|assertTrue
import|;
end_import

begin_comment
comment|/**  * Test we can auto discover components on the classpath  */
end_comment

begin_class
DECL|class|ComponentDiscoveryTest
specifier|public
class|class
name|ComponentDiscoveryTest
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ComponentDiscoveryTest
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Test
DECL|method|testComponentDiscovery ()
specifier|public
name|void
name|testComponentDiscovery
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|SortedMap
argument_list|<
name|String
argument_list|,
name|Properties
argument_list|>
name|map
init|=
name|CamelContextHelper
operator|.
name|findComponents
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Should never return null"
argument_list|,
name|map
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Component map should never be empty"
argument_list|,
operator|!
name|map
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|String
index|[]
name|expectedComponentNames
init|=
block|{
literal|"file"
block|,
literal|"vm"
block|}
decl_stmt|;
for|for
control|(
name|String
name|expectedName
range|:
name|expectedComponentNames
control|)
block|{
name|Properties
name|properties
init|=
name|map
operator|.
name|get
argument_list|(
name|expectedName
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Component map contain component: "
operator|+
name|expectedName
argument_list|,
name|properties
operator|!=
literal|null
argument_list|)
expr_stmt|;
block|}
name|Set
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Properties
argument_list|>
argument_list|>
name|entries
init|=
name|map
operator|.
name|entrySet
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Properties
argument_list|>
name|entry
range|:
name|entries
control|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Found component "
operator|+
name|entry
operator|.
name|getKey
argument_list|()
operator|+
literal|" with properties: "
operator|+
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testComponentDocumentation ()
specifier|public
name|void
name|testComponentDocumentation
parameter_list|()
throws|throws
name|Exception
block|{
comment|// cannot be tested on java 1.6
if|if
condition|(
name|isJavaVersion
argument_list|(
literal|"1.6"
argument_list|)
condition|)
block|{
return|return;
block|}
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|String
name|html
init|=
name|context
operator|.
name|getComponentDocumentation
argument_list|(
literal|"bean"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Should have found some auto-generated HTML if on Java 7"
argument_list|,
name|html
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"HTML: "
operator|+
name|html
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

