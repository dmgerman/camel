begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Body
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
name|builder
operator|.
name|RouteBuilder
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
name|mock
operator|.
name|MockEndpoint
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
name|DefaultMessage
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|SplitterPojoTest
specifier|public
class|class
name|SplitterPojoTest
extends|extends
name|ContextTestSupport
block|{
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
literal|"mySplitterBean"
argument_list|,
operator|new
name|MySplitterBean
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
block|}
DECL|method|testSplitBodyWithPojoBean ()
specifier|public
name|void
name|testSplitBodyWithPojoBean
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|reset
argument_list|()
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"James"
argument_list|,
literal|"Jonathan"
argument_list|,
literal|"Hadrian"
argument_list|,
literal|"Claus"
argument_list|,
literal|"Willem"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:body"
argument_list|,
literal|"James,Jonathan,Hadrian,Claus,Willem"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testSplitMessageWithPojoBean ()
specifier|public
name|void
name|testSplitMessageWithPojoBean
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|users
index|[]
init|=
block|{
literal|"James"
block|,
literal|"Jonathan"
block|,
literal|"Hadrian"
block|,
literal|"Claus"
block|,
literal|"Willem"
block|}
decl_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|reset
argument_list|()
expr_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:message"
argument_list|,
literal|"Test Body Message"
argument_list|,
literal|"user"
argument_list|,
literal|"James,Jonathan,Hadrian,Claus,Willem"
argument_list|)
expr_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Exchange
name|exchange
range|:
name|mock
operator|.
name|getExchanges
argument_list|()
control|)
block|{
name|assertEquals
argument_list|(
literal|"We got a wrong body "
argument_list|,
literal|"Test Body Message"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"We got a wrong header "
argument_list|,
name|users
index|[
name|i
index|]
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"user"
argument_list|)
argument_list|)
expr_stmt|;
name|i
operator|++
expr_stmt|;
block|}
block|}
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
comment|// START SNIPPET: e1
name|from
argument_list|(
literal|"direct:body"
argument_list|)
comment|// here we use a POJO bean mySplitterBean to do the split of the payload
operator|.
name|split
argument_list|()
operator|.
name|method
argument_list|(
literal|"mySplitterBean"
argument_list|,
literal|"splitBody"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:message"
argument_list|)
comment|// here we use a POJO bean mySplitterBean to do the split of the message
comment|// with a certain header value
operator|.
name|split
argument_list|()
operator|.
name|method
argument_list|(
literal|"mySplitterBean"
argument_list|,
literal|"splitMessage"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e1
block|}
block|}
return|;
block|}
comment|// START SNIPPET: e2
DECL|class|MySplitterBean
specifier|public
class|class
name|MySplitterBean
block|{
comment|/**          * The split body method returns something that is iteratable such as a java.util.List.          *          * @param body the payload of the incoming message          * @return a list containing each part splitted          */
DECL|method|splitBody (String body)
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|splitBody
parameter_list|(
name|String
name|body
parameter_list|)
block|{
comment|// since this is based on an unit test you can of cause
comment|// use different logic for splitting as Camel have out
comment|// of the box support for splitting a String based on comma
comment|// but this is for show and tell, since this is java code
comment|// you have the full power how you like to split your messages
name|List
argument_list|<
name|String
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|String
index|[]
name|parts
init|=
name|body
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|part
range|:
name|parts
control|)
block|{
name|answer
operator|.
name|add
argument_list|(
name|part
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
comment|/**          * The split message method returns something that is iteratable such as a java.util.List.          *          * @param header the header of the incoming message with the name user          * @param body the payload of the incoming message          * @return a list containing each part splitted          */
DECL|method|splitMessage (@eadervalue = R) String header, @Body String body)
specifier|public
name|List
argument_list|<
name|Message
argument_list|>
name|splitMessage
parameter_list|(
annotation|@
name|Header
argument_list|(
name|value
operator|=
literal|"user"
argument_list|)
name|String
name|header
parameter_list|,
annotation|@
name|Body
name|String
name|body
parameter_list|)
block|{
comment|// we can leverage the Parameter Binding Annotations
comment|// http://camel.apache.org/parameter-binding-annotations.html
comment|// to access the message header and body at same time,
comment|// then create the message that we want, splitter will
comment|// take care rest of them.
comment|// *NOTE* this feature requires Camel version>= 1.6.1
name|List
argument_list|<
name|Message
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|Message
argument_list|>
argument_list|()
decl_stmt|;
name|String
index|[]
name|parts
init|=
name|header
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|part
range|:
name|parts
control|)
block|{
name|DefaultMessage
name|message
init|=
operator|new
name|DefaultMessage
argument_list|()
decl_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
literal|"user"
argument_list|,
name|part
argument_list|)
expr_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
block|}
comment|// END SNIPPET: e2
block|}
end_class

end_unit

