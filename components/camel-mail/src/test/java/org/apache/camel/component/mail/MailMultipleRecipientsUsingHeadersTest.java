begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mail
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mail
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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

begin_import
import|import
name|org
operator|.
name|jvnet
operator|.
name|mock_javamail
operator|.
name|Mailbox
import|;
end_import

begin_comment
comment|/**  * Unit test for Mail using camel headers to set recipeient subject.  */
end_comment

begin_class
DECL|class|MailMultipleRecipientsUsingHeadersTest
specifier|public
class|class
name|MailMultipleRecipientsUsingHeadersTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testMailMultipleRecipientUsingHeaders ()
specifier|public
name|void
name|testMailMultipleRecipientUsingHeaders
parameter_list|()
throws|throws
name|Exception
block|{
name|Mailbox
operator|.
name|clearAll
argument_list|()
expr_stmt|;
comment|// START SNIPPET: e1
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"To"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"davsclaus@apache.org"
block|,
literal|"janstey@apache.org"
block|}
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"From"
argument_list|,
literal|"jstrachan@apache.org"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"Subject"
argument_list|,
literal|"Camel rocks"
argument_list|)
expr_stmt|;
name|String
name|body
init|=
literal|"Hello Riders.\nYes it does.\n\nRegards James."
decl_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"smtp://davsclaus@apache.org"
argument_list|,
name|body
argument_list|,
name|map
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e1
name|Mailbox
name|box
init|=
name|Mailbox
operator|.
name|get
argument_list|(
literal|"davsclaus@apache.org"
argument_list|)
decl_stmt|;
name|Message
name|msg
init|=
name|box
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"davsclaus@apache.org"
argument_list|,
name|msg
operator|.
name|getRecipients
argument_list|(
name|Message
operator|.
name|RecipientType
operator|.
name|TO
argument_list|)
index|[
literal|0
index|]
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"janstey@apache.org"
argument_list|,
name|msg
operator|.
name|getRecipients
argument_list|(
name|Message
operator|.
name|RecipientType
operator|.
name|TO
argument_list|)
index|[
literal|1
index|]
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"jstrachan@apache.org"
argument_list|,
name|msg
operator|.
name|getFrom
argument_list|()
index|[
literal|0
index|]
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Camel rocks"
argument_list|,
name|msg
operator|.
name|getSubject
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
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
throws|throws
name|Exception
block|{
comment|// no routes
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

