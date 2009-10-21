begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language.script
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|script
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|ScriptTestHelper
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|PythonLanguageTest
specifier|public
class|class
name|PythonLanguageTest
extends|extends
name|LanguageTestSupport
block|{
DECL|method|testLanguageExpressions ()
specifier|public
name|void
name|testLanguageExpressions
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|ScriptTestHelper
operator|.
name|canRunTestOnThisPlatform
argument_list|()
condition|)
block|{
return|return;
block|}
comment|// the properties are stored in a set so ordering is not known
name|assertExpression
argument_list|(
literal|"exchange.in.headers"
argument_list|,
literal|"{foo=abc, bar=123}"
argument_list|,
literal|"{bar=123, foo=abc}"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"exchange.in.body"
argument_list|,
literal|"<hello id='m123'>world!</hello>"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"exchange.in.headers.get('foo')"
argument_list|,
literal|"abc"
argument_list|)
expr_stmt|;
name|assertExpression
argument_list|(
literal|"request.headers['foo']"
argument_list|,
literal|"abc"
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
literal|"jython"
return|;
block|}
block|}
end_class

end_unit

