begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util.function
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|function
package|;
end_package

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
DECL|class|PredicatesTest
specifier|public
class|class
name|PredicatesTest
block|{
annotation|@
name|Test
DECL|method|testNegate ()
specifier|public
name|void
name|testNegate
parameter_list|()
block|{
name|Assert
operator|.
name|assertFalse
argument_list|(
name|Predicates
operator|.
name|of
argument_list|(
name|String
operator|::
name|isEmpty
argument_list|)
operator|.
name|test
argument_list|(
literal|"something"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|Predicates
operator|.
name|of
argument_list|(
name|String
operator|::
name|isEmpty
argument_list|)
operator|.
name|negate
argument_list|()
operator|.
name|test
argument_list|(
literal|"something"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|Predicates
operator|.
name|negate
argument_list|(
name|String
operator|::
name|isEmpty
argument_list|)
operator|.
name|test
argument_list|(
literal|"something"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|Predicates
operator|.
name|negate
argument_list|(
name|String
operator|::
name|isEmpty
argument_list|)
operator|.
name|test
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|Predicates
operator|.
name|of
argument_list|(
name|String
operator|::
name|isEmpty
argument_list|)
operator|.
name|negate
argument_list|()
operator|.
name|test
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
