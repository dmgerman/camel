begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.lanaguage.sql
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|lanaguage
operator|.
name|sql
package|;
end_package

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
name|Expression
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
name|builder
operator|.
name|sql
operator|.
name|Person
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
name|builder
operator|.
name|sql
operator|.
name|SqlTest
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
name|Language
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
DECL|class|SqlLanguageTest
specifier|public
class|class
name|SqlLanguageTest
extends|extends
name|SqlTest
block|{
annotation|@
name|Test
DECL|method|testExpression ()
specifier|public
name|void
name|testExpression
parameter_list|()
throws|throws
name|Exception
block|{
name|Language
name|language
init|=
name|assertResolveLanguage
argument_list|(
name|getLanguageName
argument_list|()
argument_list|)
decl_stmt|;
name|Expression
name|expression
init|=
name|language
operator|.
name|createExpression
argument_list|(
literal|"SELECT * FROM org.apache.camel.builder.sql.Person where city = 'London'"
argument_list|)
decl_stmt|;
name|List
name|value
init|=
name|expression
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
name|list
init|=
operator|(
name|List
operator|)
name|value
decl_stmt|;
name|assertEquals
argument_list|(
literal|"List size"
argument_list|,
literal|2
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Object
name|person
range|:
name|list
control|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Found: "
operator|+
name|person
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Test
DECL|method|testExpressionWithHeaderVariable ()
specifier|public
name|void
name|testExpressionWithHeaderVariable
parameter_list|()
throws|throws
name|Exception
block|{
name|Language
name|language
init|=
name|assertResolveLanguage
argument_list|(
name|getLanguageName
argument_list|()
argument_list|)
decl_stmt|;
name|Expression
name|expression
init|=
name|language
operator|.
name|createExpression
argument_list|(
literal|"SELECT * FROM org.apache.camel.builder.sql.Person where name = :fooHeader"
argument_list|)
decl_stmt|;
name|List
name|value
init|=
name|expression
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Person
argument_list|>
name|list
init|=
operator|(
name|List
argument_list|<
name|Person
argument_list|>
operator|)
name|value
decl_stmt|;
name|assertEquals
argument_list|(
literal|"List size"
argument_list|,
literal|1
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Person
name|person
range|:
name|list
control|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Found: "
operator|+
name|person
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"name"
argument_list|,
literal|"James"
argument_list|,
name|person
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testPredicates ()
specifier|public
name|void
name|testPredicates
parameter_list|()
throws|throws
name|Exception
block|{
name|Language
name|language
init|=
name|assertResolveLanguage
argument_list|(
name|getLanguageName
argument_list|()
argument_list|)
decl_stmt|;
name|assertPredicate
argument_list|(
name|language
operator|.
name|createPredicate
argument_list|(
literal|"SELECT * FROM org.apache.camel.builder.sql.Person where city = 'London'"
argument_list|)
argument_list|,
name|exchange
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertPredicate
argument_list|(
name|language
operator|.
name|createPredicate
argument_list|(
literal|"SELECT * FROM org.apache.camel.builder.sql.Person where city = 'Manchester'"
argument_list|)
argument_list|,
name|exchange
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPredicateWithHeaderVariable ()
specifier|public
name|void
name|testPredicateWithHeaderVariable
parameter_list|()
throws|throws
name|Exception
block|{
name|Language
name|language
init|=
name|assertResolveLanguage
argument_list|(
name|getLanguageName
argument_list|()
argument_list|)
decl_stmt|;
name|assertPredicate
argument_list|(
name|language
operator|.
name|createPredicate
argument_list|(
literal|"SELECT * FROM org.apache.camel.builder.sql.Person where name = :fooHeader"
argument_list|)
argument_list|,
name|exchange
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|getLanguageName ()
specifier|protected
name|String
name|getLanguageName
parameter_list|()
block|{
return|return
literal|"sql"
return|;
block|}
block|}
end_class

end_unit

