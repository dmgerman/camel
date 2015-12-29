begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.google.mail
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|google
operator|.
name|mail
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|MessagingException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|Session
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|internet
operator|.
name|MimeMessage
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|client
operator|.
name|repackaged
operator|.
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|codec
operator|.
name|binary
operator|.
name|Base64
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|gmail
operator|.
name|model
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
name|google
operator|.
name|mail
operator|.
name|internal
operator|.
name|GmailUsersThreadsApiMethod
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
name|google
operator|.
name|mail
operator|.
name|internal
operator|.
name|GoogleMailApiCollection
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Test class for {@link com.google.api.services.gmail.Gmail$Users$Threads}  * APIs.  */
end_comment

begin_class
DECL|class|GmailUsersThreadsIntegrationTest
specifier|public
class|class
name|GmailUsersThreadsIntegrationTest
extends|extends
name|AbstractGoogleMailTestSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|GmailUsersThreadsIntegrationTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|PATH_PREFIX
specifier|private
specifier|static
specifier|final
name|String
name|PATH_PREFIX
init|=
name|GoogleMailApiCollection
operator|.
name|getCollection
argument_list|()
operator|.
name|getApiName
argument_list|(
name|GmailUsersThreadsApiMethod
operator|.
name|class
argument_list|)
operator|.
name|getName
argument_list|()
decl_stmt|;
DECL|method|createThreadedTestEmail (String previousThreadId)
specifier|private
name|Message
name|createThreadedTestEmail
parameter_list|(
name|String
name|previousThreadId
parameter_list|)
throws|throws
name|MessagingException
throws|,
name|IOException
block|{
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|gmail
operator|.
name|model
operator|.
name|Profile
name|profile
init|=
name|requestBody
argument_list|(
literal|"google-mail://users/getProfile?inBody=userId"
argument_list|,
name|CURRENT_USERID
argument_list|)
decl_stmt|;
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|Session
name|session
init|=
name|Session
operator|.
name|getDefaultInstance
argument_list|(
name|props
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|MimeMessage
name|mm
init|=
operator|new
name|MimeMessage
argument_list|(
name|session
argument_list|)
decl_stmt|;
name|mm
operator|.
name|addRecipients
argument_list|(
name|javax
operator|.
name|mail
operator|.
name|Message
operator|.
name|RecipientType
operator|.
name|TO
argument_list|,
name|profile
operator|.
name|getEmailAddress
argument_list|()
argument_list|)
expr_stmt|;
name|mm
operator|.
name|setSubject
argument_list|(
literal|"Hello from camel-google-mail"
argument_list|)
expr_stmt|;
name|mm
operator|.
name|setContent
argument_list|(
literal|"Camel rocks!"
argument_list|,
literal|"text/plain"
argument_list|)
expr_stmt|;
name|Message
name|createMessageWithEmail
init|=
name|createMessageWithEmail
argument_list|(
name|mm
argument_list|)
decl_stmt|;
if|if
condition|(
name|previousThreadId
operator|!=
literal|null
condition|)
block|{
name|createMessageWithEmail
operator|.
name|setThreadId
argument_list|(
name|previousThreadId
argument_list|)
expr_stmt|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelGoogleMail.userId"
argument_list|,
name|CURRENT_USERID
argument_list|)
expr_stmt|;
comment|// parameter type is com.google.api.services.gmail.model.Message
name|headers
operator|.
name|put
argument_list|(
literal|"CamelGoogleMail.content"
argument_list|,
name|createMessageWithEmail
argument_list|)
expr_stmt|;
return|return
name|requestBodyAndHeaders
argument_list|(
literal|"google-mail://messages/send"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
return|;
block|}
DECL|method|createMessageWithEmail (MimeMessage email)
specifier|private
name|Message
name|createMessageWithEmail
parameter_list|(
name|MimeMessage
name|email
parameter_list|)
throws|throws
name|MessagingException
throws|,
name|IOException
block|{
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|email
operator|.
name|writeTo
argument_list|(
name|baos
argument_list|)
expr_stmt|;
name|String
name|encodedEmail
init|=
name|Base64
operator|.
name|encodeBase64URLSafeString
argument_list|(
name|baos
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
name|Message
name|message
init|=
operator|new
name|Message
argument_list|()
decl_stmt|;
name|message
operator|.
name|setRaw
argument_list|(
name|encodedEmail
argument_list|)
expr_stmt|;
return|return
name|message
return|;
block|}
annotation|@
name|Test
DECL|method|testList ()
specifier|public
name|void
name|testList
parameter_list|()
throws|throws
name|Exception
block|{
name|Message
name|m1
init|=
name|createThreadedTestEmail
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|Message
name|m2
init|=
name|createThreadedTestEmail
argument_list|(
name|m1
operator|.
name|getThreadId
argument_list|()
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"CamelGoogleMail.q"
argument_list|,
literal|"subject:\"Hello from camel-google-mail\""
argument_list|)
expr_stmt|;
comment|// using String message body for single parameter "userId"
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|gmail
operator|.
name|model
operator|.
name|ListThreadsResponse
name|result
init|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://LIST"
argument_list|,
name|CURRENT_USERID
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"list result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|getThreads
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"list: "
operator|+
name|result
argument_list|)
expr_stmt|;
name|headers
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
expr_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelGoogleMail.userId"
argument_list|,
name|CURRENT_USERID
argument_list|)
expr_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelGoogleMail.id"
argument_list|,
name|m1
operator|.
name|getThreadId
argument_list|()
argument_list|)
expr_stmt|;
name|requestBodyAndHeaders
argument_list|(
literal|"direct://DELETE"
argument_list|,
literal|null
argument_list|,
name|headers
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
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
block|{
comment|// test route for delete
name|from
argument_list|(
literal|"direct://DELETE"
argument_list|)
operator|.
name|to
argument_list|(
literal|"google-mail://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/delete"
argument_list|)
expr_stmt|;
comment|// test route for get
name|from
argument_list|(
literal|"direct://GET"
argument_list|)
operator|.
name|to
argument_list|(
literal|"google-mail://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/get"
argument_list|)
expr_stmt|;
comment|// test route for list
name|from
argument_list|(
literal|"direct://LIST"
argument_list|)
operator|.
name|to
argument_list|(
literal|"google-mail://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/list?inBody=userId"
argument_list|)
expr_stmt|;
comment|// test route for modify
name|from
argument_list|(
literal|"direct://MODIFY"
argument_list|)
operator|.
name|to
argument_list|(
literal|"google-mail://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/modify"
argument_list|)
expr_stmt|;
comment|// test route for trash
name|from
argument_list|(
literal|"direct://TRASH"
argument_list|)
operator|.
name|to
argument_list|(
literal|"google-mail://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/trash"
argument_list|)
expr_stmt|;
comment|// test route for untrash
name|from
argument_list|(
literal|"direct://UNTRASH"
argument_list|)
operator|.
name|to
argument_list|(
literal|"google-mail://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/untrash"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

