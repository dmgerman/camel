begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|ContextTestSupport
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
name|IsSingleton
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
name|Predicate
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
name|ExpressionBuilder
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
name|PredicateBuilder
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
name|JndiRegistry
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
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|service
operator|.
name|ServiceSupport
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
DECL|class|LanguageServiceTest
specifier|public
class|class
name|LanguageServiceTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|my
specifier|private
name|MyLanguage
name|my
init|=
operator|new
name|MyLanguage
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|jndi
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"my"
argument_list|,
name|my
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
block|}
annotation|@
name|Test
DECL|method|testLanguageService ()
specifier|public
name|void
name|testLanguageService
parameter_list|()
throws|throws
name|Exception
block|{
name|MyLanguage
name|myl
init|=
operator|(
name|MyLanguage
operator|)
name|context
operator|.
name|resolveLanguage
argument_list|(
literal|"my"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|myl
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Started"
argument_list|,
name|myl
operator|.
name|getState
argument_list|()
argument_list|)
expr_stmt|;
comment|// simple language is resolved by default hence why there is 2
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|context
operator|.
name|getLanguageNames
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// resolve again, should find same instance
name|MyLanguage
name|myl2
init|=
operator|(
name|MyLanguage
operator|)
name|context
operator|.
name|resolveLanguage
argument_list|(
literal|"my"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|myl2
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|myl
argument_list|,
name|myl2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Started"
argument_list|,
name|myl2
operator|.
name|getState
argument_list|()
argument_list|)
expr_stmt|;
comment|// simple language is resolved by default hence why there is 2
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|context
operator|.
name|getLanguageNames
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Stopped"
argument_list|,
name|myl
operator|.
name|getState
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|getLanguageNames
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNonSingletonLanguage ()
specifier|public
name|void
name|testNonSingletonLanguage
parameter_list|()
throws|throws
name|Exception
block|{
name|Language
name|tol
init|=
name|context
operator|.
name|resolveLanguage
argument_list|(
literal|"tokenize"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|tol
argument_list|)
expr_stmt|;
comment|// simple language is resolved by default hence why there is 2
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|context
operator|.
name|getLanguageNames
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// resolve again, should find another instance
name|Language
name|tol2
init|=
name|context
operator|.
name|resolveLanguage
argument_list|(
literal|"tokenize"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|tol2
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|tol
argument_list|,
name|tol2
argument_list|)
expr_stmt|;
comment|// simple language is resolved by default hence why there is 2
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|context
operator|.
name|getLanguageNames
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|getLanguageNames
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|class|MyLanguage
specifier|public
class|class
name|MyLanguage
extends|extends
name|ServiceSupport
implements|implements
name|Language
implements|,
name|IsSingleton
block|{
DECL|field|state
specifier|private
name|String
name|state
decl_stmt|;
annotation|@
name|Override
DECL|method|createPredicate (String expression)
specifier|public
name|Predicate
name|createPredicate
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
return|return
name|PredicateBuilder
operator|.
name|constant
argument_list|(
literal|true
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createExpression (String expression)
specifier|public
name|Expression
name|createExpression
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|constantExpression
argument_list|(
name|expression
argument_list|)
return|;
block|}
DECL|method|getState ()
specifier|public
name|String
name|getState
parameter_list|()
block|{
return|return
name|state
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|state
operator|=
literal|"Started"
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|state
operator|=
literal|"Stopped"
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
end_class

end_unit

