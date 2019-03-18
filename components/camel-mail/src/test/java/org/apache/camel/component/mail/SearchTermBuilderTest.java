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
name|Date
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
name|InternetAddress
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
name|javax
operator|.
name|mail
operator|.
name|search
operator|.
name|SearchTerm
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mail
operator|.
name|SearchTermBuilder
operator|.
name|Comparison
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mail
operator|.
name|SearchTermBuilder
operator|.
name|Op
operator|.
name|or
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|SearchTermBuilderTest
specifier|public
class|class
name|SearchTermBuilderTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
DECL|method|testSearchTermBuilderFromAndSubject ()
specifier|public
name|void
name|testSearchTermBuilderFromAndSubject
parameter_list|()
throws|throws
name|Exception
block|{
name|SearchTermBuilder
name|build
init|=
operator|new
name|SearchTermBuilder
argument_list|()
decl_stmt|;
name|SearchTerm
name|st
init|=
name|build
operator|.
name|from
argument_list|(
literal|"someone@somewhere.com"
argument_list|)
operator|.
name|subject
argument_list|(
literal|"Camel"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|st
argument_list|)
expr_stmt|;
comment|// create dummy message
name|Mailbox
operator|.
name|clearAll
argument_list|()
expr_stmt|;
name|JavaMailSender
name|sender
init|=
operator|new
name|DefaultJavaMailSender
argument_list|()
decl_stmt|;
name|MimeMessage
name|msg
init|=
operator|new
name|MimeMessage
argument_list|(
name|sender
operator|.
name|getSession
argument_list|()
argument_list|)
decl_stmt|;
name|msg
operator|.
name|setSubject
argument_list|(
literal|"Yeah Camel rocks"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setText
argument_list|(
literal|"Apache Camel is a cool project. Have a fun ride."
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setFrom
argument_list|(
operator|new
name|InternetAddress
argument_list|(
literal|"someone@somewhere.com"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should match message"
argument_list|,
name|st
operator|.
name|match
argument_list|(
name|msg
argument_list|)
argument_list|)
expr_stmt|;
name|MimeMessage
name|msg2
init|=
operator|new
name|MimeMessage
argument_list|(
name|sender
operator|.
name|getSession
argument_list|()
argument_list|)
decl_stmt|;
name|msg2
operator|.
name|setSubject
argument_list|(
literal|"Apache Camel is fantastic"
argument_list|)
expr_stmt|;
name|msg2
operator|.
name|setText
argument_list|(
literal|"I like Camel."
argument_list|)
expr_stmt|;
name|msg2
operator|.
name|setFrom
argument_list|(
operator|new
name|InternetAddress
argument_list|(
literal|"donotreply@somewhere.com"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Should not match message, as from doesn't match"
argument_list|,
name|st
operator|.
name|match
argument_list|(
name|msg2
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSearchTermBuilderFromOrSubject ()
specifier|public
name|void
name|testSearchTermBuilderFromOrSubject
parameter_list|()
throws|throws
name|Exception
block|{
name|SearchTermBuilder
name|build
init|=
operator|new
name|SearchTermBuilder
argument_list|()
decl_stmt|;
name|SearchTerm
name|st
init|=
name|build
operator|.
name|subject
argument_list|(
literal|"Camel"
argument_list|)
operator|.
name|from
argument_list|(
name|or
argument_list|,
literal|"admin@apache.org"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|st
argument_list|)
expr_stmt|;
comment|// create dummy message
name|Mailbox
operator|.
name|clearAll
argument_list|()
expr_stmt|;
name|JavaMailSender
name|sender
init|=
operator|new
name|DefaultJavaMailSender
argument_list|()
decl_stmt|;
name|MimeMessage
name|msg
init|=
operator|new
name|MimeMessage
argument_list|(
name|sender
operator|.
name|getSession
argument_list|()
argument_list|)
decl_stmt|;
name|msg
operator|.
name|setSubject
argument_list|(
literal|"Yeah Camel rocks"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setText
argument_list|(
literal|"Apache Camel is a cool project. Have a fun ride."
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setFrom
argument_list|(
operator|new
name|InternetAddress
argument_list|(
literal|"someone@somewhere.com"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should match message"
argument_list|,
name|st
operator|.
name|match
argument_list|(
name|msg
argument_list|)
argument_list|)
expr_stmt|;
name|MimeMessage
name|msg2
init|=
operator|new
name|MimeMessage
argument_list|(
name|sender
operator|.
name|getSession
argument_list|()
argument_list|)
decl_stmt|;
name|msg2
operator|.
name|setSubject
argument_list|(
literal|"Beware"
argument_list|)
expr_stmt|;
name|msg2
operator|.
name|setText
argument_list|(
literal|"This is from the administrator."
argument_list|)
expr_stmt|;
name|msg2
operator|.
name|setFrom
argument_list|(
operator|new
name|InternetAddress
argument_list|(
literal|"admin@apache.org"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should match message, as its from admin"
argument_list|,
name|st
operator|.
name|match
argument_list|(
name|msg2
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSearchTermSentLast24Hours ()
specifier|public
name|void
name|testSearchTermSentLast24Hours
parameter_list|()
throws|throws
name|Exception
block|{
name|SearchTermBuilder
name|build
init|=
operator|new
name|SearchTermBuilder
argument_list|()
decl_stmt|;
name|long
name|offset
init|=
operator|-
literal|1
operator|*
operator|(
literal|24
operator|*
literal|60
operator|*
literal|60
operator|*
literal|1000L
operator|)
decl_stmt|;
name|SearchTerm
name|st
init|=
name|build
operator|.
name|subject
argument_list|(
literal|"Camel"
argument_list|)
operator|.
name|sentNow
argument_list|(
name|Comparison
operator|.
name|GE
argument_list|,
name|offset
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|st
argument_list|)
expr_stmt|;
comment|// create dummy message
name|Mailbox
operator|.
name|clearAll
argument_list|()
expr_stmt|;
name|JavaMailSender
name|sender
init|=
operator|new
name|DefaultJavaMailSender
argument_list|()
decl_stmt|;
name|MimeMessage
name|msg
init|=
operator|new
name|MimeMessage
argument_list|(
name|sender
operator|.
name|getSession
argument_list|()
argument_list|)
decl_stmt|;
name|msg
operator|.
name|setSubject
argument_list|(
literal|"Yeah Camel rocks"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setText
argument_list|(
literal|"Apache Camel is a cool project. Have a fun ride."
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setFrom
argument_list|(
operator|new
name|InternetAddress
argument_list|(
literal|"someone@somewhere.com"
argument_list|)
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setSentDate
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should match message"
argument_list|,
name|st
operator|.
name|match
argument_list|(
name|msg
argument_list|)
argument_list|)
expr_stmt|;
name|MimeMessage
name|msg2
init|=
operator|new
name|MimeMessage
argument_list|(
name|sender
operator|.
name|getSession
argument_list|()
argument_list|)
decl_stmt|;
name|msg2
operator|.
name|setSubject
argument_list|(
literal|"Camel in Action"
argument_list|)
expr_stmt|;
name|msg2
operator|.
name|setText
argument_list|(
literal|"Hey great book"
argument_list|)
expr_stmt|;
name|msg2
operator|.
name|setFrom
argument_list|(
operator|new
name|InternetAddress
argument_list|(
literal|"dude@apache.org"
argument_list|)
argument_list|)
expr_stmt|;
comment|// mark it as sent 2 days ago
name|long
name|twoDays
init|=
literal|2
operator|*
literal|24
operator|*
literal|60
operator|*
literal|60
operator|*
literal|1000L
decl_stmt|;
name|long
name|time
init|=
operator|new
name|Date
argument_list|()
operator|.
name|getTime
argument_list|()
operator|-
name|twoDays
decl_stmt|;
name|msg2
operator|.
name|setSentDate
argument_list|(
operator|new
name|Date
argument_list|(
name|time
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Should not match message as its too old"
argument_list|,
name|st
operator|.
name|match
argument_list|(
name|msg2
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testComparison ()
specifier|public
name|void
name|testComparison
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|Comparison
operator|.
name|LE
operator|.
name|asNum
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|Comparison
operator|.
name|LT
operator|.
name|asNum
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|Comparison
operator|.
name|EQ
operator|.
name|asNum
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|Comparison
operator|.
name|NE
operator|.
name|asNum
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|Comparison
operator|.
name|GT
operator|.
name|asNum
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|Comparison
operator|.
name|GE
operator|.
name|asNum
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

