begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language.juel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|juel
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|el
operator|.
name|ELContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|el
operator|.
name|ExpressionFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|el
operator|.
name|ValueExpression
import|;
end_import

begin_import
import|import
name|de
operator|.
name|odysseus
operator|.
name|el
operator|.
name|ExpressionFactoryImpl
import|;
end_import

begin_import
import|import
name|de
operator|.
name|odysseus
operator|.
name|el
operator|.
name|util
operator|.
name|SimpleContext
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|JuelTest
specifier|public
class|class
name|JuelTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
DECL|method|testJuel ()
specifier|public
name|void
name|testJuel
parameter_list|()
throws|throws
name|Exception
block|{
name|ExpressionFactory
name|factory
init|=
operator|new
name|ExpressionFactoryImpl
argument_list|()
decl_stmt|;
name|ELContext
name|context
init|=
operator|new
name|SimpleContext
argument_list|()
decl_stmt|;
name|ValueExpression
name|valueExpression
init|=
name|factory
operator|.
name|createValueExpression
argument_list|(
name|context
argument_list|,
literal|"${123 * 2}"
argument_list|,
name|Object
operator|.
name|class
argument_list|)
decl_stmt|;
name|Object
name|value
init|=
name|valueExpression
operator|.
name|getValue
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Result is a Long object"
argument_list|,
literal|246L
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

