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
name|javax
operator|.
name|naming
operator|.
name|Context
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
name|Exchange
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
name|Header
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
name|LanguageTestSupport
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
name|Message
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
name|bean
operator|.
name|MethodNotFoundException
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
name|language
operator|.
name|bean
operator|.
name|BeanLanguage
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
DECL|class|BeanTest
specifier|public
class|class
name|BeanTest
extends|extends
name|LanguageTestSupport
block|{
annotation|@
name|Test
DECL|method|testSimpleExpressions ()
specifier|public
name|void
name|testSimpleExpressions
parameter_list|()
throws|throws
name|Exception
block|{
name|assertExpression
argument_list|(
literal|"foo.echo('e::o')"
argument_list|,
literal|"e::o"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"foo.echo('e.o')"
argument_list|,
literal|"e.o"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"my.company.MyClass::echo('a')"
argument_list|,
literal|"a"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"my.company.MyClass::echo('a.b')"
argument_list|,
literal|"a.b"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"my.company.MyClass::echo('a::b')"
argument_list|,
literal|"a::b"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"foo.cheese"
argument_list|,
literal|"abc"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"foo?method=cheese"
argument_list|,
literal|"abc"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"my.company.MyClass::cheese"
argument_list|,
literal|"abc"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"foo?method=echo('e::o')"
argument_list|,
literal|"e::o"
argument_list|)
expr_stmt|;
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
name|assertPredicate
argument_list|(
literal|"foo.isFooHeaderAbc"
argument_list|)
expr_stmt|;
name|assertPredicate
argument_list|(
literal|"foo?method=isFooHeaderAbc"
argument_list|)
expr_stmt|;
name|assertPredicate
argument_list|(
literal|"my.company.MyClass::isFooHeaderAbc"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDoubleColon ()
specifier|public
name|void
name|testDoubleColon
parameter_list|()
throws|throws
name|Exception
block|{
name|assertPredicate
argument_list|(
literal|"foo::isFooHeaderAbc"
argument_list|)
expr_stmt|;
name|assertPredicateFails
argument_list|(
literal|"foo:isFooHeaderAbc"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBeanTypeExpression ()
specifier|public
name|void
name|testBeanTypeExpression
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|exp
init|=
name|BeanLanguage
operator|.
name|bean
argument_list|(
name|MyUser
operator|.
name|class
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|createExchangeWithBody
argument_list|(
literal|"Claus"
argument_list|)
decl_stmt|;
name|Object
name|result
init|=
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|Object
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello Claus"
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBeanTypeAndMethodExpression ()
specifier|public
name|void
name|testBeanTypeAndMethodExpression
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|exp
init|=
name|BeanLanguage
operator|.
name|bean
argument_list|(
name|MyUser
operator|.
name|class
argument_list|,
literal|"hello"
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|createExchangeWithBody
argument_list|(
literal|"Claus"
argument_list|)
decl_stmt|;
name|Object
name|result
init|=
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|Object
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello Claus"
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBeanInstanceAndMethodExpression ()
specifier|public
name|void
name|testBeanInstanceAndMethodExpression
parameter_list|()
throws|throws
name|Exception
block|{
name|MyUser
name|user
init|=
operator|new
name|MyUser
argument_list|()
decl_stmt|;
name|Expression
name|exp
init|=
name|BeanLanguage
operator|.
name|bean
argument_list|(
name|user
argument_list|,
literal|"hello"
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|createExchangeWithBody
argument_list|(
literal|"Claus"
argument_list|)
decl_stmt|;
name|Object
name|result
init|=
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|Object
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello Claus"
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNoMethod ()
specifier|public
name|void
name|testNoMethod
parameter_list|()
throws|throws
name|Exception
block|{
name|MyUser
name|user
init|=
operator|new
name|MyUser
argument_list|()
decl_stmt|;
name|Expression
name|exp
init|=
name|BeanLanguage
operator|.
name|bean
argument_list|(
name|user
argument_list|,
literal|"unknown"
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|createExchangeWithBody
argument_list|(
literal|"Claus"
argument_list|)
decl_stmt|;
name|Object
name|result
init|=
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|Object
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
name|MethodNotFoundException
name|e
init|=
name|assertIsInstanceOf
argument_list|(
name|MethodNotFoundException
operator|.
name|class
argument_list|,
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|user
argument_list|,
name|e
operator|.
name|getBean
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"unknown"
argument_list|,
name|e
operator|.
name|getMethodName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNoMethodBeanLookup ()
specifier|public
name|void
name|testNoMethodBeanLookup
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|exp
init|=
name|BeanLanguage
operator|.
name|bean
argument_list|(
literal|"foo.cake"
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|createExchangeWithBody
argument_list|(
literal|"Claus"
argument_list|)
decl_stmt|;
name|Object
name|result
init|=
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|Object
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
name|MethodNotFoundException
name|e
init|=
name|assertIsInstanceOf
argument_list|(
name|MethodNotFoundException
operator|.
name|class
argument_list|,
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByName
argument_list|(
literal|"foo"
argument_list|)
argument_list|,
name|e
operator|.
name|getBean
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cake"
argument_list|,
name|e
operator|.
name|getMethodName
argument_list|()
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
literal|"bean"
return|;
block|}
annotation|@
name|Override
DECL|method|createJndiContext ()
specifier|protected
name|Context
name|createJndiContext
parameter_list|()
throws|throws
name|Exception
block|{
name|Context
name|context
init|=
name|super
operator|.
name|createJndiContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|bind
argument_list|(
literal|"foo"
argument_list|,
operator|new
name|MyBean
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|bind
argument_list|(
literal|"my.company.MyClass"
argument_list|,
operator|new
name|MyBean
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
DECL|class|MyBean
specifier|public
specifier|static
class|class
name|MyBean
block|{
DECL|method|cheese (Exchange exchange)
specifier|public
name|Object
name|cheese
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
return|return
name|in
operator|.
name|getHeader
argument_list|(
literal|"foo"
argument_list|)
return|;
block|}
DECL|method|echo (String echo)
specifier|public
name|String
name|echo
parameter_list|(
name|String
name|echo
parameter_list|)
block|{
return|return
name|echo
return|;
block|}
DECL|method|isFooHeaderAbc (@eaderR) String foo)
specifier|public
name|boolean
name|isFooHeaderAbc
parameter_list|(
annotation|@
name|Header
argument_list|(
literal|"foo"
argument_list|)
name|String
name|foo
parameter_list|)
block|{
return|return
literal|"abc"
operator|.
name|equals
argument_list|(
name|foo
argument_list|)
return|;
block|}
block|}
DECL|class|MyUser
specifier|public
specifier|static
class|class
name|MyUser
block|{
DECL|method|hello (String name)
specifier|public
name|String
name|hello
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
literal|"Hello "
operator|+
name|name
return|;
block|}
block|}
block|}
end_class

end_unit

