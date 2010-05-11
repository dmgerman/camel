begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bean
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
name|language
operator|.
name|XPath
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
name|jndi
operator|.
name|JndiContext
import|;
end_import

begin_class
DECL|class|BeanWithXPathInjectionUsingResultTypeTest
specifier|public
class|class
name|BeanWithXPathInjectionUsingResultTypeTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|myBean
specifier|protected
name|MyBean
name|myBean
init|=
operator|new
name|MyBean
argument_list|()
decl_stmt|;
DECL|method|testSendMessage ()
specifier|public
name|void
name|testSendMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"bean:myBean"
argument_list|,
literal|"<a><b>12</b></a>"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bean ab: "
operator|+
name|myBean
argument_list|,
literal|"12"
argument_list|,
name|myBean
operator|.
name|ab
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bean abText: "
operator|+
name|myBean
argument_list|,
literal|"a12"
argument_list|,
name|myBean
operator|.
name|abText
argument_list|)
expr_stmt|;
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
name|JndiContext
name|answer
init|=
operator|new
name|JndiContext
argument_list|()
decl_stmt|;
name|answer
operator|.
name|bind
argument_list|(
literal|"myBean"
argument_list|,
name|myBean
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|class|MyBean
specifier|public
specifier|static
class|class
name|MyBean
block|{
DECL|field|ab
specifier|public
name|String
name|ab
decl_stmt|;
DECL|field|abText
specifier|public
name|String
name|abText
decl_stmt|;
DECL|method|read (@PathR) String ab, @XPath(value = R, resultType = String.class) String abText)
specifier|public
name|void
name|read
parameter_list|(
annotation|@
name|XPath
argument_list|(
literal|"//a/b/text()"
argument_list|)
name|String
name|ab
parameter_list|,
annotation|@
name|XPath
argument_list|(
name|value
operator|=
literal|"concat('a',//a/b)"
argument_list|,
name|resultType
operator|=
name|String
operator|.
name|class
argument_list|)
name|String
name|abText
parameter_list|)
block|{
name|this
operator|.
name|ab
operator|=
name|ab
expr_stmt|;
name|this
operator|.
name|abText
operator|=
name|abText
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

