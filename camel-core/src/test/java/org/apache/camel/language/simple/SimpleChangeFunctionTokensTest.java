begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language.simple
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|simple
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
name|LanguageTestSupport
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|SimpleChangeFunctionTokensTest
specifier|public
class|class
name|SimpleChangeFunctionTokensTest
extends|extends
name|LanguageTestSupport
block|{
annotation|@
name|Override
DECL|method|getLanguageName ()
specifier|protected
name|String
name|getLanguageName
parameter_list|()
block|{
return|return
literal|"simple"
return|;
block|}
annotation|@
name|Override
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|SimpleLanguage
operator|.
name|changeFunctionStartToken
argument_list|(
literal|"[["
argument_list|)
expr_stmt|;
name|SimpleLanguage
operator|.
name|changeFunctionEndToken
argument_list|(
literal|"]]"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
comment|// replace old tokens
name|SimpleLanguage
operator|.
name|changeFunctionStartToken
argument_list|(
literal|"${"
argument_list|,
literal|"$simple{"
argument_list|)
expr_stmt|;
name|SimpleLanguage
operator|.
name|changeFunctionEndToken
argument_list|(
literal|"}"
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimpleBody ()
specifier|public
name|void
name|testSimpleBody
parameter_list|()
throws|throws
name|Exception
block|{
name|assertExpression
argument_list|(
name|exchange
argument_list|,
literal|"[[body]]"
argument_list|,
literal|"<hello id='m123'>world!</hello>"
argument_list|)
expr_stmt|;
comment|// old tokens do no longer work
name|assertExpression
argument_list|(
name|exchange
argument_list|,
literal|"${body}"
argument_list|,
literal|"${body}"
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimpleConstantAndBody ()
specifier|public
name|void
name|testSimpleConstantAndBody
parameter_list|()
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Camel"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
name|exchange
argument_list|,
literal|"Hi [[body]] how are you"
argument_list|,
literal|"Hi Camel how are you"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
name|exchange
argument_list|,
literal|"'Hi '[[body]]' how are you'"
argument_list|,
literal|"'Hi 'Camel' how are you'"
argument_list|)
expr_stmt|;
comment|// old tokens do no longer work
name|assertExpression
argument_list|(
name|exchange
argument_list|,
literal|"Hi ${body} how are you"
argument_list|,
literal|"Hi ${body} how are you"
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimpleConstantAndBodyAndHeader ()
specifier|public
name|void
name|testSimpleConstantAndBodyAndHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Camel"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"foo"
argument_list|,
literal|"Tiger"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
name|exchange
argument_list|,
literal|"Hi [[body]] how are [[header.foo]]"
argument_list|,
literal|"Hi Camel how are Tiger"
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimpleEqOperator ()
specifier|public
name|void
name|testSimpleEqOperator
parameter_list|()
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Camel"
argument_list|)
expr_stmt|;
name|assertPredicate
argument_list|(
name|exchange
argument_list|,
literal|"[[body]] == 'Tiger'"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertPredicate
argument_list|(
name|exchange
argument_list|,
literal|"[[body]] == 'Camel'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertPredicate
argument_list|(
name|exchange
argument_list|,
literal|"[[body]] == \"Tiger\""
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertPredicate
argument_list|(
name|exchange
argument_list|,
literal|"[[body]] == \"Camel\""
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

