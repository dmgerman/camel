begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Address
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
name|internet
operator|.
name|InternetAddress
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|mail
operator|.
name|imap
operator|.
name|SortTerm
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
name|mockito
operator|.
name|Mockito
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|when
import|;
end_import

begin_comment
comment|/**  * Tests mail sort util  */
end_comment

begin_class
DECL|class|MailSorterTest
specifier|public
class|class
name|MailSorterTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|MESSAGES
specifier|private
specifier|static
specifier|final
name|Message
index|[]
name|MESSAGES
init|=
operator|new
name|Message
index|[
literal|3
index|]
decl_stmt|;
DECL|field|TIE_BREAKER
specifier|private
specifier|static
specifier|final
name|Message
name|TIE_BREAKER
decl_stmt|;
comment|/**      * All possible sort terms      */
DECL|field|POSSIBLE_TERMS
specifier|private
specifier|static
specifier|final
name|SortTerm
index|[]
name|POSSIBLE_TERMS
init|=
operator|new
name|SortTerm
index|[]
block|{
name|SortTerm
operator|.
name|ARRIVAL
block|,
name|SortTerm
operator|.
name|CC
block|,
name|SortTerm
operator|.
name|DATE
block|,
name|SortTerm
operator|.
name|FROM
block|,
name|SortTerm
operator|.
name|SIZE
block|,
name|SortTerm
operator|.
name|TO
block|,
name|SortTerm
operator|.
name|SUBJECT
block|}
decl_stmt|;
static|static
block|{
try|try
block|{
name|MESSAGES
index|[
literal|0
index|]
operator|=
name|createMessage
argument_list|(
literal|"to1"
argument_list|,
literal|"cc1"
argument_list|,
literal|"from1"
argument_list|,
operator|new
name|Date
argument_list|(
literal|1
argument_list|)
argument_list|,
operator|new
name|Date
argument_list|(
literal|1001
argument_list|)
argument_list|,
literal|1
argument_list|,
literal|"subject1"
argument_list|)
expr_stmt|;
name|MESSAGES
index|[
literal|1
index|]
operator|=
name|createMessage
argument_list|(
literal|"to2"
argument_list|,
literal|"cc2"
argument_list|,
literal|"from2"
argument_list|,
operator|new
name|Date
argument_list|(
literal|2
argument_list|)
argument_list|,
operator|new
name|Date
argument_list|(
literal|1002
argument_list|)
argument_list|,
literal|2
argument_list|,
literal|"subject2"
argument_list|)
expr_stmt|;
name|MESSAGES
index|[
literal|2
index|]
operator|=
name|createMessage
argument_list|(
literal|"to3"
argument_list|,
literal|"cc3"
argument_list|,
literal|"from3"
argument_list|,
operator|new
name|Date
argument_list|(
literal|3
argument_list|)
argument_list|,
operator|new
name|Date
argument_list|(
literal|1003
argument_list|)
argument_list|,
literal|3
argument_list|,
literal|"subject3"
argument_list|)
expr_stmt|;
comment|// Message that creates a tie on all fields except for one
name|TIE_BREAKER
operator|=
name|createMessage
argument_list|(
literal|"to3"
argument_list|,
literal|"cc3"
argument_list|,
literal|"from3"
argument_list|,
operator|new
name|Date
argument_list|(
literal|3
argument_list|)
argument_list|,
operator|new
name|Date
argument_list|(
literal|1003
argument_list|)
argument_list|,
literal|3
argument_list|,
literal|"subject0TieBreaker"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MessagingException
name|e
parameter_list|)
block|{
comment|// Rethrow as unchecked. Can not occur anyways
throw|throw
operator|new
name|IllegalStateException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Create a new message with the specified data      */
DECL|method|createMessage (String to, String cc, String from, Date received, Date sent, int size, String subject)
specifier|private
specifier|static
name|Message
name|createMessage
parameter_list|(
name|String
name|to
parameter_list|,
name|String
name|cc
parameter_list|,
name|String
name|from
parameter_list|,
name|Date
name|received
parameter_list|,
name|Date
name|sent
parameter_list|,
name|int
name|size
parameter_list|,
name|String
name|subject
parameter_list|)
throws|throws
name|MessagingException
block|{
specifier|final
name|Message
name|msg
init|=
name|Mockito
operator|.
name|mock
argument_list|(
name|Message
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|msg
operator|.
name|getFrom
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
operator|new
name|Address
index|[]
block|{
operator|new
name|InternetAddress
argument_list|(
name|from
argument_list|)
block|}
argument_list|)
expr_stmt|;
name|when
argument_list|(
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
argument_list|)
operator|.
name|thenReturn
argument_list|(
operator|new
name|Address
index|[]
block|{
operator|new
name|InternetAddress
argument_list|(
name|to
argument_list|)
block|}
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|msg
operator|.
name|getRecipients
argument_list|(
name|Message
operator|.
name|RecipientType
operator|.
name|CC
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
operator|new
name|Address
index|[]
block|{
operator|new
name|InternetAddress
argument_list|(
name|cc
argument_list|)
block|}
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|msg
operator|.
name|getSentDate
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|sent
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|msg
operator|.
name|getReceivedDate
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|received
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|msg
operator|.
name|getSize
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|size
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|msg
operator|.
name|getSubject
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|subject
argument_list|)
expr_stmt|;
return|return
name|msg
return|;
block|}
annotation|@
name|Test
DECL|method|testSortMessages ()
specifier|public
name|void
name|testSortMessages
parameter_list|()
throws|throws
name|Exception
block|{
name|Message
index|[]
name|expected
init|=
operator|new
name|Message
index|[]
block|{
name|MESSAGES
index|[
literal|0
index|]
block|,
name|MESSAGES
index|[
literal|1
index|]
block|,
name|MESSAGES
index|[
literal|2
index|]
block|}
decl_stmt|;
comment|// Sort using all the terms. Message order should be the same no matter what term is used
for|for
control|(
name|SortTerm
name|term
range|:
name|POSSIBLE_TERMS
control|)
block|{
name|Message
index|[]
name|actual
init|=
name|MESSAGES
operator|.
name|clone
argument_list|()
decl_stmt|;
name|MailSorter
operator|.
name|sortMessages
argument_list|(
name|actual
argument_list|,
operator|new
name|SortTerm
index|[]
block|{
name|term
block|}
argument_list|)
expr_stmt|;
try|try
block|{
name|assertArrayEquals
argument_list|(
name|actual
argument_list|,
name|expected
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Term: "
operator|+
name|term
operator|.
name|toString
argument_list|()
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
block|}
annotation|@
name|Test
DECL|method|testSortMessagesReverse ()
specifier|public
name|void
name|testSortMessagesReverse
parameter_list|()
throws|throws
name|Exception
block|{
name|Message
index|[]
name|expected
init|=
operator|new
name|Message
index|[]
block|{
name|MESSAGES
index|[
literal|2
index|]
block|,
name|MESSAGES
index|[
literal|1
index|]
block|,
name|MESSAGES
index|[
literal|0
index|]
block|}
decl_stmt|;
comment|// Sort using all the terms. Message order should be the same no matter what term is used
for|for
control|(
name|SortTerm
name|term
range|:
name|POSSIBLE_TERMS
control|)
block|{
name|Message
index|[]
name|actual
init|=
name|MESSAGES
operator|.
name|clone
argument_list|()
decl_stmt|;
name|MailSorter
operator|.
name|sortMessages
argument_list|(
name|actual
argument_list|,
operator|new
name|SortTerm
index|[]
block|{
name|SortTerm
operator|.
name|REVERSE
block|,
name|term
block|}
argument_list|)
expr_stmt|;
try|try
block|{
name|assertArrayEquals
argument_list|(
name|actual
argument_list|,
name|expected
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AssertionError
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
literal|"Term: "
operator|+
name|term
operator|.
name|toString
argument_list|()
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
block|}
annotation|@
name|Test
DECL|method|testSortMessagesMulti ()
specifier|public
name|void
name|testSortMessagesMulti
parameter_list|()
throws|throws
name|Exception
block|{
name|Message
index|[]
name|expected
init|=
operator|new
name|Message
index|[]
block|{
name|MESSAGES
index|[
literal|0
index|]
block|,
name|MESSAGES
index|[
literal|1
index|]
block|,
name|MESSAGES
index|[
literal|2
index|]
block|}
decl_stmt|;
comment|// Sort using all the terms. Message order should be the same no matter what term is used. The second term
comment|// should be ignored since it is already the decider.
for|for
control|(
name|SortTerm
name|term1
range|:
name|POSSIBLE_TERMS
control|)
block|{
for|for
control|(
name|SortTerm
name|term2
range|:
name|POSSIBLE_TERMS
control|)
block|{
name|Message
index|[]
name|actual
init|=
name|MESSAGES
operator|.
name|clone
argument_list|()
decl_stmt|;
name|MailSorter
operator|.
name|sortMessages
argument_list|(
name|actual
argument_list|,
operator|new
name|SortTerm
index|[]
block|{
name|term1
block|,
name|SortTerm
operator|.
name|REVERSE
block|,
name|term2
block|}
argument_list|)
expr_stmt|;
try|try
block|{
name|assertArrayEquals
argument_list|(
name|actual
argument_list|,
name|expected
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AssertionError
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Terms: %s, %s"
argument_list|,
name|term1
operator|.
name|toString
argument_list|()
argument_list|,
name|term2
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
block|}
block|}
annotation|@
name|Test
DECL|method|testSortMessagesWithTie ()
specifier|public
name|void
name|testSortMessagesWithTie
parameter_list|()
throws|throws
name|Exception
block|{
name|Message
index|[]
name|given
init|=
operator|new
name|Message
index|[]
block|{
name|MESSAGES
index|[
literal|2
index|]
block|,
name|TIE_BREAKER
block|}
decl_stmt|;
comment|// Sort according to the whole list. Only the last element breaks the tie
name|Message
index|[]
name|actual1
init|=
name|given
operator|.
name|clone
argument_list|()
decl_stmt|;
name|MailSorter
operator|.
name|sortMessages
argument_list|(
name|actual1
argument_list|,
name|POSSIBLE_TERMS
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|actual1
argument_list|,
operator|new
name|Message
index|[]
block|{
name|TIE_BREAKER
block|,
name|MESSAGES
index|[
literal|2
index|]
block|}
argument_list|)
expr_stmt|;
comment|// now reverse the last element (the tie breaker)
name|SortTerm
index|[]
name|reversed
init|=
operator|new
name|SortTerm
index|[
name|POSSIBLE_TERMS
operator|.
name|length
operator|+
literal|1
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|POSSIBLE_TERMS
argument_list|,
literal|0
argument_list|,
name|reversed
argument_list|,
literal|0
argument_list|,
name|POSSIBLE_TERMS
operator|.
name|length
operator|-
literal|1
argument_list|)
expr_stmt|;
name|reversed
index|[
name|reversed
operator|.
name|length
operator|-
literal|2
index|]
operator|=
name|SortTerm
operator|.
name|REVERSE
expr_stmt|;
name|reversed
index|[
name|reversed
operator|.
name|length
operator|-
literal|1
index|]
operator|=
name|POSSIBLE_TERMS
index|[
name|POSSIBLE_TERMS
operator|.
name|length
operator|-
literal|1
index|]
expr_stmt|;
comment|// And check again
name|Message
index|[]
name|actual2
init|=
name|given
operator|.
name|clone
argument_list|()
decl_stmt|;
name|MailSorter
operator|.
name|sortMessages
argument_list|(
name|actual2
argument_list|,
name|reversed
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|actual2
argument_list|,
operator|new
name|Message
index|[]
block|{
name|MESSAGES
index|[
literal|2
index|]
block|,
name|TIE_BREAKER
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

