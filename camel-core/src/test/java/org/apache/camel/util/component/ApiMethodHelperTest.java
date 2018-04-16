begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util.component
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|component
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|HashMap
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
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|component
operator|.
name|ApiMethodArg
operator|.
name|arg
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
name|assertTrue
import|;
end_import

begin_class
DECL|class|ApiMethodHelperTest
specifier|public
class|class
name|ApiMethodHelperTest
block|{
DECL|field|sayHis
specifier|private
specifier|static
name|TestMethod
index|[]
name|sayHis
init|=
operator|new
name|TestMethod
index|[]
block|{
name|TestMethod
operator|.
name|SAYHI
block|,
name|TestMethod
operator|.
name|SAYHI_1
block|}
decl_stmt|;
DECL|field|apiMethodHelper
specifier|private
specifier|static
name|ApiMethodHelper
argument_list|<
name|TestMethod
argument_list|>
name|apiMethodHelper
decl_stmt|;
static|static
block|{
specifier|final
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|aliases
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|aliases
operator|.
name|put
argument_list|(
literal|"say(.*)"
argument_list|,
literal|"$1"
argument_list|)
expr_stmt|;
name|apiMethodHelper
operator|=
operator|new
name|ApiMethodHelper
argument_list|<>
argument_list|(
name|TestMethod
operator|.
name|class
argument_list|,
name|aliases
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"names"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetCandidateMethods ()
specifier|public
name|void
name|testGetCandidateMethods
parameter_list|()
block|{
name|List
argument_list|<
name|ApiMethod
argument_list|>
name|methods
init|=
name|apiMethodHelper
operator|.
name|getCandidateMethods
argument_list|(
literal|"sayHi"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Can't find sayHi(*)"
argument_list|,
literal|2
argument_list|,
name|methods
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|methods
operator|=
name|apiMethodHelper
operator|.
name|getCandidateMethods
argument_list|(
literal|"hi"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Can't find sayHi(name)"
argument_list|,
literal|2
argument_list|,
name|methods
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|methods
operator|=
name|apiMethodHelper
operator|.
name|getCandidateMethods
argument_list|(
literal|"hi"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Can't find sayHi(name)"
argument_list|,
literal|1
argument_list|,
name|methods
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|methods
operator|=
name|apiMethodHelper
operator|.
name|getCandidateMethods
argument_list|(
literal|"greetMe"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Can't find greetMe(name)"
argument_list|,
literal|1
argument_list|,
name|methods
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|methods
operator|=
name|apiMethodHelper
operator|.
name|getCandidateMethods
argument_list|(
literal|"greetUs"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"name1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Can't find greetUs(name1, name2)"
argument_list|,
literal|1
argument_list|,
name|methods
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|methods
operator|=
name|apiMethodHelper
operator|.
name|getCandidateMethods
argument_list|(
literal|"greetAll"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"nameMap"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Can't find greetAll(nameMap)"
argument_list|,
literal|1
argument_list|,
name|methods
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|methods
operator|=
name|apiMethodHelper
operator|.
name|getCandidateMethods
argument_list|(
literal|"greetInnerChild"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"child"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Can't find greetInnerChild(child)"
argument_list|,
literal|1
argument_list|,
name|methods
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFilterMethods ()
specifier|public
name|void
name|testFilterMethods
parameter_list|()
block|{
name|List
argument_list|<
name|ApiMethod
argument_list|>
name|methods
init|=
name|apiMethodHelper
operator|.
name|filterMethods
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|sayHis
argument_list|)
argument_list|,
name|ApiMethodHelper
operator|.
name|MatchType
operator|.
name|EXACT
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Exact match failed for sayHi()"
argument_list|,
literal|1
argument_list|,
name|methods
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Exact match failed for sayHi()"
argument_list|,
name|TestMethod
operator|.
name|SAYHI
argument_list|,
name|methods
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|methods
operator|=
name|apiMethodHelper
operator|.
name|filterMethods
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|sayHis
argument_list|)
argument_list|,
name|ApiMethodHelper
operator|.
name|MatchType
operator|.
name|SUBSET
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Subset match failed for sayHi(*)"
argument_list|,
literal|2
argument_list|,
name|methods
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|methods
operator|=
name|apiMethodHelper
operator|.
name|filterMethods
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|sayHis
argument_list|)
argument_list|,
name|ApiMethodHelper
operator|.
name|MatchType
operator|.
name|SUBSET
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Subset match failed for sayHi(name)"
argument_list|,
literal|1
argument_list|,
name|methods
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Exact match failed for sayHi()"
argument_list|,
name|TestMethod
operator|.
name|SAYHI_1
argument_list|,
name|methods
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|methods
operator|=
name|apiMethodHelper
operator|.
name|filterMethods
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|sayHis
argument_list|)
argument_list|,
name|ApiMethodHelper
operator|.
name|MatchType
operator|.
name|SUPER_SET
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Super set match failed for sayHi(name)"
argument_list|,
literal|1
argument_list|,
name|methods
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Exact match failed for sayHi()"
argument_list|,
name|TestMethod
operator|.
name|SAYHI_1
argument_list|,
name|methods
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|methods
operator|=
name|apiMethodHelper
operator|.
name|filterMethods
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|TestMethod
operator|.
name|values
argument_list|()
argument_list|)
argument_list|,
name|ApiMethodHelper
operator|.
name|MatchType
operator|.
name|SUPER_SET
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Super set match failed for sayHi(name)"
argument_list|,
literal|2
argument_list|,
name|methods
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// test nullable names
name|methods
operator|=
name|apiMethodHelper
operator|.
name|filterMethods
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|TestMethod
operator|.
name|GREETALL
argument_list|,
name|TestMethod
operator|.
name|GREETALL_1
argument_list|,
name|TestMethod
operator|.
name|GREETALL_2
argument_list|)
argument_list|,
name|ApiMethodHelper
operator|.
name|MatchType
operator|.
name|SUPER_SET
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Super set match with null args failed for greetAll(names)"
argument_list|,
literal|1
argument_list|,
name|methods
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetArguments ()
specifier|public
name|void
name|testGetArguments
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"GetArguments failed for hi"
argument_list|,
literal|2
argument_list|,
name|apiMethodHelper
operator|.
name|getArguments
argument_list|(
literal|"hi"
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"GetArguments failed for greetMe"
argument_list|,
literal|2
argument_list|,
name|apiMethodHelper
operator|.
name|getArguments
argument_list|(
literal|"greetMe"
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"GetArguments failed for greetUs"
argument_list|,
literal|4
argument_list|,
name|apiMethodHelper
operator|.
name|getArguments
argument_list|(
literal|"greetUs"
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"GetArguments failed for greetAll"
argument_list|,
literal|6
argument_list|,
name|apiMethodHelper
operator|.
name|getArguments
argument_list|(
literal|"greetAll"
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"GetArguments failed for greetInnerChild"
argument_list|,
literal|2
argument_list|,
name|apiMethodHelper
operator|.
name|getArguments
argument_list|(
literal|"greetInnerChild"
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetMissingProperties ()
specifier|public
name|void
name|testGetMissingProperties
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"Missing properties for hi"
argument_list|,
literal|1
argument_list|,
name|apiMethodHelper
operator|.
name|getMissingProperties
argument_list|(
literal|"hi"
argument_list|,
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|HashSet
argument_list|<
name|String
argument_list|>
name|argNames
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|argNames
operator|.
name|add
argument_list|(
literal|"name"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Missing properties for greetMe"
argument_list|,
literal|0
argument_list|,
name|apiMethodHelper
operator|.
name|getMissingProperties
argument_list|(
literal|"greetMe"
argument_list|,
name|argNames
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|argNames
operator|.
name|clear
argument_list|()
expr_stmt|;
name|argNames
operator|.
name|add
argument_list|(
literal|"name1"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Missing properties for greetMe"
argument_list|,
literal|1
argument_list|,
name|apiMethodHelper
operator|.
name|getMissingProperties
argument_list|(
literal|"greetUs"
argument_list|,
name|argNames
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAllArguments ()
specifier|public
name|void
name|testAllArguments
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"Get all arguments"
argument_list|,
literal|8
argument_list|,
name|apiMethodHelper
operator|.
name|allArguments
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetType ()
specifier|public
name|void
name|testGetType
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"Get type name"
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|apiMethodHelper
operator|.
name|getType
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get type name1"
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|apiMethodHelper
operator|.
name|getType
argument_list|(
literal|"name1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get type name2"
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|apiMethodHelper
operator|.
name|getType
argument_list|(
literal|"name2"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get type nameMap"
argument_list|,
name|Map
operator|.
name|class
argument_list|,
name|apiMethodHelper
operator|.
name|getType
argument_list|(
literal|"nameMap"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get type child"
argument_list|,
name|TestProxy
operator|.
name|InnerChild
operator|.
name|class
argument_list|,
name|apiMethodHelper
operator|.
name|getType
argument_list|(
literal|"child"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetHighestPriorityMethod ()
specifier|public
name|void
name|testGetHighestPriorityMethod
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"Get highest priority method"
argument_list|,
name|TestMethod
operator|.
name|SAYHI_1
argument_list|,
name|ApiMethodHelper
operator|.
name|getHighestPriorityMethod
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|sayHis
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInvokeMethod ()
specifier|public
name|void
name|testInvokeMethod
parameter_list|()
throws|throws
name|Exception
block|{
name|TestProxy
name|proxy
init|=
operator|new
name|TestProxy
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"sayHi()"
argument_list|,
literal|"Hello!"
argument_list|,
name|ApiMethodHelper
operator|.
name|invokeMethod
argument_list|(
name|proxy
argument_list|,
name|TestMethod
operator|.
name|SAYHI
argument_list|,
name|Collections
operator|.
expr|<
name|String
argument_list|,
name|Object
operator|>
name|emptyMap
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
literal|"Dave"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"sayHi(name)"
argument_list|,
literal|"Hello Dave"
argument_list|,
name|ApiMethodHelper
operator|.
name|invokeMethod
argument_list|(
name|proxy
argument_list|,
name|TestMethod
operator|.
name|SAYHI_1
argument_list|,
name|properties
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"greetMe(name)"
argument_list|,
literal|"Greetings Dave"
argument_list|,
name|ApiMethodHelper
operator|.
name|invokeMethod
argument_list|(
name|proxy
argument_list|,
name|TestMethod
operator|.
name|GREETME
argument_list|,
name|properties
argument_list|)
argument_list|)
expr_stmt|;
name|properties
operator|.
name|clear
argument_list|()
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"name1"
argument_list|,
literal|"Dave"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"name2"
argument_list|,
literal|"Frank"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"greetUs(name1, name2)"
argument_list|,
literal|"Greetings Dave, Frank"
argument_list|,
name|ApiMethodHelper
operator|.
name|invokeMethod
argument_list|(
name|proxy
argument_list|,
name|TestMethod
operator|.
name|GREETUS
argument_list|,
name|properties
argument_list|)
argument_list|)
expr_stmt|;
name|properties
operator|.
name|clear
argument_list|()
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"names"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"Dave"
block|,
literal|"Frank"
block|}
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"greetAll(names)"
argument_list|,
literal|"Greetings Dave, Frank"
argument_list|,
name|ApiMethodHelper
operator|.
name|invokeMethod
argument_list|(
name|proxy
argument_list|,
name|TestMethod
operator|.
name|GREETALL
argument_list|,
name|properties
argument_list|)
argument_list|)
expr_stmt|;
name|properties
operator|.
name|clear
argument_list|()
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|nameMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|nameMap
operator|.
name|put
argument_list|(
literal|"Dave"
argument_list|,
literal|"Hello"
argument_list|)
expr_stmt|;
name|nameMap
operator|.
name|put
argument_list|(
literal|"Frank"
argument_list|,
literal|"Goodbye"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"nameMap"
argument_list|,
name|nameMap
argument_list|)
expr_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|result
init|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
operator|)
name|ApiMethodHelper
operator|.
name|invokeMethod
argument_list|(
name|proxy
argument_list|,
name|TestMethod
operator|.
name|GREETALL_2
argument_list|,
name|properties
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"greetAll(nameMap)"
argument_list|,
name|result
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|result
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|assertTrue
argument_list|(
literal|"greetAll(nameMap)"
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|endsWith
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// test with a derived proxy
name|proxy
operator|=
operator|new
name|TestProxy
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|sayHi
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
literal|"Howdy "
operator|+
name|name
return|;
block|}
block|}
expr_stmt|;
name|properties
operator|.
name|clear
argument_list|()
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
literal|"Dave"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Derived sayHi(name)"
argument_list|,
literal|"Howdy Dave"
argument_list|,
name|ApiMethodHelper
operator|.
name|invokeMethod
argument_list|(
name|proxy
argument_list|,
name|TestMethod
operator|.
name|SAYHI_1
argument_list|,
name|properties
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|enum|TestMethod
enum|enum
name|TestMethod
implements|implements
name|ApiMethod
block|{
DECL|enumConstant|SAYHI
name|SAYHI
argument_list|(
name|String
operator|.
name|class
argument_list|,
literal|"sayHi"
argument_list|)
block|,
DECL|enumConstant|SAYHI_1
name|SAYHI_1
argument_list|(
name|String
operator|.
name|class
argument_list|,
literal|"sayHi"
argument_list|,
name|arg
argument_list|(
literal|"name"
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
block|,
DECL|enumConstant|GREETME
name|GREETME
argument_list|(
name|String
operator|.
name|class
argument_list|,
literal|"greetMe"
argument_list|,
name|arg
argument_list|(
literal|"name"
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
block|,
DECL|enumConstant|GREETUS
name|GREETUS
argument_list|(
name|String
operator|.
name|class
argument_list|,
literal|"greetUs"
argument_list|,
name|arg
argument_list|(
literal|"name1"
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|,
name|arg
argument_list|(
literal|"name2"
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
block|,
DECL|enumConstant|GREETALL
name|GREETALL
argument_list|(
name|String
operator|.
name|class
argument_list|,
literal|"greetAll"
argument_list|,
name|arg
argument_list|(
literal|"names"
argument_list|,
operator|new
name|String
index|[
literal|0
index|]
operator|.
name|getClass
argument_list|()
argument_list|)
argument_list|)
block|,
DECL|enumConstant|GREETALL_1
name|GREETALL_1
argument_list|(
name|String
operator|.
name|class
argument_list|,
literal|"greetAll"
argument_list|,
name|arg
argument_list|(
literal|"nameList"
argument_list|,
name|List
operator|.
name|class
argument_list|)
argument_list|)
block|,
DECL|enumConstant|GREETALL_2
name|GREETALL_2
argument_list|(
name|Map
operator|.
name|class
argument_list|,
literal|"greetAll"
argument_list|,
name|arg
argument_list|(
literal|"nameMap"
argument_list|,
name|Map
operator|.
name|class
argument_list|)
argument_list|)
block|,
DECL|enumConstant|GREETTIMES
name|GREETTIMES
argument_list|(
operator|new
name|String
index|[
literal|0
index|]
operator|.
name|getClass
argument_list|()
argument_list|,
literal|"greetTimes"
argument_list|,
name|arg
argument_list|(
literal|"name"
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|,
name|arg
argument_list|(
literal|"times"
argument_list|,
name|int
operator|.
name|class
argument_list|)
argument_list|)
block|,
DECL|enumConstant|GREETINNERCHILD
name|GREETINNERCHILD
argument_list|(
operator|new
name|String
index|[
literal|0
index|]
operator|.
name|getClass
argument_list|()
argument_list|,
literal|"greetInnerChild"
argument_list|,
name|arg
argument_list|(
literal|"child"
argument_list|,
name|TestProxy
operator|.
name|InnerChild
operator|.
name|class
argument_list|)
argument_list|)
block|;
DECL|field|apiMethod
specifier|private
specifier|final
name|ApiMethod
name|apiMethod
decl_stmt|;
DECL|method|TestMethod (Class<?> resultType, String name, ApiMethodArg... args)
name|TestMethod
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|resultType
parameter_list|,
name|String
name|name
parameter_list|,
name|ApiMethodArg
modifier|...
name|args
parameter_list|)
block|{
name|this
operator|.
name|apiMethod
operator|=
operator|new
name|ApiMethodImpl
argument_list|(
name|TestProxy
operator|.
name|class
argument_list|,
name|resultType
argument_list|,
name|name
argument_list|,
name|args
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|apiMethod
operator|.
name|getName
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getResultType ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getResultType
parameter_list|()
block|{
return|return
name|apiMethod
operator|.
name|getResultType
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getArgNames ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getArgNames
parameter_list|()
block|{
return|return
name|apiMethod
operator|.
name|getArgNames
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getArgTypes ()
specifier|public
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|getArgTypes
parameter_list|()
block|{
return|return
name|apiMethod
operator|.
name|getArgTypes
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getMethod ()
specifier|public
name|Method
name|getMethod
parameter_list|()
block|{
return|return
name|apiMethod
operator|.
name|getMethod
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

