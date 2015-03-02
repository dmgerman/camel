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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|List
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

begin_comment
comment|/**  * Utility class for sorting of mail messages  */
end_comment

begin_class
DECL|class|MailSorter
specifier|public
specifier|final
class|class
name|MailSorter
block|{
comment|/**      * No instances      */
DECL|method|MailSorter ()
specifier|private
name|MailSorter
parameter_list|()
block|{     }
comment|/**      * Sort the messages. This emulates sorting the messages on the server if the server doesn't have the sorting      * capability. See RFC 5256      * Does not support complex sorting like in the RFC (with Base Subject or other similar stuff), just simple      * comparisons.      *      * @param messages Messages to sort. Are sorted in place      * @param sortTerm Sort term      */
DECL|method|sortMessages (Message[] messages, final SortTerm[] sortTerm)
specifier|public
specifier|static
name|void
name|sortMessages
parameter_list|(
name|Message
index|[]
name|messages
parameter_list|,
specifier|final
name|SortTerm
index|[]
name|sortTerm
parameter_list|)
block|{
specifier|final
name|List
argument_list|<
name|SortTermWithDescending
argument_list|>
name|sortTermsWithDescending
init|=
name|getSortTermsWithDescending
argument_list|(
name|sortTerm
argument_list|)
decl_stmt|;
name|sortMessages
argument_list|(
name|messages
argument_list|,
name|sortTermsWithDescending
argument_list|)
expr_stmt|;
block|}
comment|/**      * Compute the potentially descending sort terms from the input list      *      * @param sortTerm Input list      * @return Sort terms list including if the respective sort should be sorted in descending order      */
DECL|method|getSortTermsWithDescending (SortTerm[] sortTerm)
specifier|private
specifier|static
name|List
argument_list|<
name|SortTermWithDescending
argument_list|>
name|getSortTermsWithDescending
parameter_list|(
name|SortTerm
index|[]
name|sortTerm
parameter_list|)
block|{
comment|// List of reversable sort terms. If the boolean is true the respective sort term is descending
specifier|final
name|List
argument_list|<
name|SortTermWithDescending
argument_list|>
name|sortTermsWithDescending
init|=
operator|new
name|ArrayList
argument_list|<
name|SortTermWithDescending
argument_list|>
argument_list|(
name|sortTerm
operator|.
name|length
argument_list|)
decl_stmt|;
comment|// Descending next item in input because the last item was a "descending"
name|boolean
name|descendingNext
init|=
literal|false
decl_stmt|;
for|for
control|(
name|SortTerm
name|term
range|:
name|sortTerm
control|)
block|{
if|if
condition|(
name|term
operator|.
name|equals
argument_list|(
name|SortTerm
operator|.
name|REVERSE
argument_list|)
condition|)
block|{
if|if
condition|(
name|descendingNext
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Double reverse in sort term is not allowed"
argument_list|)
throw|;
block|}
name|descendingNext
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
name|sortTermsWithDescending
operator|.
name|add
argument_list|(
operator|new
name|SortTermWithDescending
argument_list|(
name|term
argument_list|,
name|descendingNext
argument_list|)
argument_list|)
expr_stmt|;
name|descendingNext
operator|=
literal|false
expr_stmt|;
block|}
block|}
return|return
name|sortTermsWithDescending
return|;
block|}
comment|/**      * Sort messages using the list of properties      *      * @param messages             Messages to sort. Are sorted in place      * @param sortTermsWithDescending Sort terms list including if the respective sort should be sorted in descending order      */
DECL|method|sortMessages (Message[] messages, final List<SortTermWithDescending> sortTermsWithDescending)
specifier|private
specifier|static
name|void
name|sortMessages
parameter_list|(
name|Message
index|[]
name|messages
parameter_list|,
specifier|final
name|List
argument_list|<
name|SortTermWithDescending
argument_list|>
name|sortTermsWithDescending
parameter_list|)
block|{
name|Arrays
operator|.
name|sort
argument_list|(
name|messages
argument_list|,
operator|new
name|Comparator
argument_list|<
name|Message
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|Message
name|m1
parameter_list|,
name|Message
name|m2
parameter_list|)
block|{
try|try
block|{
for|for
control|(
name|SortTermWithDescending
name|reversableTerm
range|:
name|sortTermsWithDescending
control|)
block|{
name|int
name|comparison
init|=
name|compareMessageProperty
argument_list|(
name|m1
argument_list|,
name|m2
argument_list|,
name|reversableTerm
operator|.
name|getTerm
argument_list|()
argument_list|)
decl_stmt|;
comment|// Descending
if|if
condition|(
name|reversableTerm
operator|.
name|isDescending
argument_list|()
condition|)
block|{
name|comparison
operator|=
operator|-
name|comparison
expr_stmt|;
block|}
comment|// Abort on first non-equal
if|if
condition|(
name|comparison
operator|!=
literal|0
condition|)
block|{
return|return
name|comparison
return|;
block|}
block|}
comment|// Equal
return|return
literal|0
return|;
block|}
catch|catch
parameter_list|(
name|MessagingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|/**      * Compare the value of the property of the two messages.      *      * @param msg1     Message 1      * @param msg2     Message 2      * @param property Property to compare      * @return msg1.property.compareTo(msg2.property)      * @throws javax.mail.MessagingException If message data could not be read.      */
DECL|method|compareMessageProperty (Message msg1, Message msg2, SortTerm property)
specifier|private
specifier|static
name|int
name|compareMessageProperty
parameter_list|(
name|Message
name|msg1
parameter_list|,
name|Message
name|msg2
parameter_list|,
name|SortTerm
name|property
parameter_list|)
throws|throws
name|MessagingException
block|{
if|if
condition|(
name|property
operator|.
name|equals
argument_list|(
name|SortTerm
operator|.
name|TO
argument_list|)
condition|)
block|{
name|InternetAddress
name|addr1
init|=
operator|(
name|InternetAddress
operator|)
name|msg1
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
decl_stmt|;
name|InternetAddress
name|addr2
init|=
operator|(
name|InternetAddress
operator|)
name|msg2
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
decl_stmt|;
return|return
name|addr1
operator|.
name|getAddress
argument_list|()
operator|.
name|compareTo
argument_list|(
name|addr2
operator|.
name|getAddress
argument_list|()
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|property
operator|.
name|equals
argument_list|(
name|SortTerm
operator|.
name|CC
argument_list|)
condition|)
block|{
name|InternetAddress
name|addr1
init|=
operator|(
name|InternetAddress
operator|)
name|msg1
operator|.
name|getRecipients
argument_list|(
name|Message
operator|.
name|RecipientType
operator|.
name|CC
argument_list|)
index|[
literal|0
index|]
decl_stmt|;
name|InternetAddress
name|addr2
init|=
operator|(
name|InternetAddress
operator|)
name|msg2
operator|.
name|getRecipients
argument_list|(
name|Message
operator|.
name|RecipientType
operator|.
name|CC
argument_list|)
index|[
literal|0
index|]
decl_stmt|;
return|return
name|addr1
operator|.
name|getAddress
argument_list|()
operator|.
name|compareTo
argument_list|(
name|addr2
operator|.
name|getAddress
argument_list|()
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|property
operator|.
name|equals
argument_list|(
name|SortTerm
operator|.
name|FROM
argument_list|)
condition|)
block|{
name|InternetAddress
name|addr1
init|=
operator|(
name|InternetAddress
operator|)
name|msg1
operator|.
name|getFrom
argument_list|()
index|[
literal|0
index|]
decl_stmt|;
name|InternetAddress
name|addr2
init|=
operator|(
name|InternetAddress
operator|)
name|msg2
operator|.
name|getFrom
argument_list|()
index|[
literal|0
index|]
decl_stmt|;
return|return
name|addr1
operator|.
name|getAddress
argument_list|()
operator|.
name|compareTo
argument_list|(
name|addr2
operator|.
name|getAddress
argument_list|()
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|property
operator|.
name|equals
argument_list|(
name|SortTerm
operator|.
name|ARRIVAL
argument_list|)
condition|)
block|{
name|Date
name|arr1
init|=
name|msg1
operator|.
name|getReceivedDate
argument_list|()
decl_stmt|;
name|Date
name|arr2
init|=
name|msg2
operator|.
name|getReceivedDate
argument_list|()
decl_stmt|;
return|return
name|arr1
operator|.
name|compareTo
argument_list|(
name|arr2
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|property
operator|.
name|equals
argument_list|(
name|SortTerm
operator|.
name|DATE
argument_list|)
condition|)
block|{
name|Date
name|sent1
init|=
name|msg1
operator|.
name|getSentDate
argument_list|()
decl_stmt|;
name|Date
name|sent2
init|=
name|msg2
operator|.
name|getSentDate
argument_list|()
decl_stmt|;
return|return
name|sent1
operator|.
name|compareTo
argument_list|(
name|sent2
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|property
operator|.
name|equals
argument_list|(
name|SortTerm
operator|.
name|SIZE
argument_list|)
condition|)
block|{
name|int
name|size1
init|=
name|msg1
operator|.
name|getSize
argument_list|()
decl_stmt|;
name|int
name|size2
init|=
name|msg2
operator|.
name|getSize
argument_list|()
decl_stmt|;
return|return
name|Integer
operator|.
name|compare
argument_list|(
name|size1
argument_list|,
name|size2
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|property
operator|.
name|equals
argument_list|(
name|SortTerm
operator|.
name|SUBJECT
argument_list|)
condition|)
block|{
name|String
name|sub1
init|=
name|msg1
operator|.
name|getSubject
argument_list|()
decl_stmt|;
name|String
name|sub2
init|=
name|msg2
operator|.
name|getSubject
argument_list|()
decl_stmt|;
return|return
name|sub1
operator|.
name|compareTo
argument_list|(
name|sub2
argument_list|)
return|;
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Unknown sort term: %s"
argument_list|,
name|property
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
comment|/**      * A sort term with a bit indicating if sorting should be descending for this term      */
DECL|class|SortTermWithDescending
specifier|private
specifier|static
specifier|final
class|class
name|SortTermWithDescending
block|{
DECL|field|term
specifier|private
name|SortTerm
name|term
decl_stmt|;
DECL|field|descending
specifier|private
name|boolean
name|descending
decl_stmt|;
DECL|method|SortTermWithDescending (SortTerm term, boolean descending)
specifier|private
name|SortTermWithDescending
parameter_list|(
name|SortTerm
name|term
parameter_list|,
name|boolean
name|descending
parameter_list|)
block|{
name|this
operator|.
name|term
operator|=
name|term
expr_stmt|;
name|this
operator|.
name|descending
operator|=
name|descending
expr_stmt|;
block|}
comment|/**          * @return Actual search term          */
DECL|method|getTerm ()
specifier|public
name|SortTerm
name|getTerm
parameter_list|()
block|{
return|return
name|term
return|;
block|}
comment|/**          * @return true if sorting should be descending, false if it should be ascending          */
DECL|method|isDescending ()
specifier|public
name|boolean
name|isDescending
parameter_list|()
block|{
return|return
name|descending
return|;
block|}
block|}
block|}
end_class

end_unit

