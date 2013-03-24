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

begin_class
DECL|class|SimpleWhiteSpaceTest
specifier|public
class|class
name|SimpleWhiteSpaceTest
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
DECL|method|testExpressionWithSpace ()
specifier|public
name|void
name|testExpressionWithSpace
parameter_list|()
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"some text"
argument_list|)
expr_stmt|;
name|assertPredicate
argument_list|(
literal|"${in.body} contains 'some' and ${in.body} contains 'text'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|testExpressionWithTabs ()
specifier|public
name|void
name|testExpressionWithTabs
parameter_list|()
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"some text"
argument_list|)
expr_stmt|;
name|assertPredicate
argument_list|(
literal|"${in.body} contains 'some' and\t${in.body} contains 'text'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|testUnixMultiLineExpression ()
specifier|public
name|void
name|testUnixMultiLineExpression
parameter_list|()
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"some text"
argument_list|)
expr_stmt|;
name|assertPredicate
argument_list|(
literal|"${in.body} contains 'some' and\n${in.body} contains 'text'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|testWindowsMultiLineExpression ()
specifier|public
name|void
name|testWindowsMultiLineExpression
parameter_list|()
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"some text"
argument_list|)
expr_stmt|;
name|assertPredicate
argument_list|(
literal|"${in.body} contains 'some' and\r\n${in.body} contains 'text'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|testMacMultiLineExpression ()
specifier|public
name|void
name|testMacMultiLineExpression
parameter_list|()
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"some text"
argument_list|)
expr_stmt|;
name|assertPredicate
argument_list|(
literal|"${in.body} contains 'some' and\r${in.body} contains 'text'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|testExpressionWithMultiLineString ()
specifier|public
name|void
name|testExpressionWithMultiLineString
parameter_list|()
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"\tsome\nmulti\rline\r\ntext"
argument_list|)
expr_stmt|;
name|assertPredicate
argument_list|(
literal|"${in.body} == '\tsome\nmulti\rline\r\ntext'"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

