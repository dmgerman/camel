begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.twitter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|twitter
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
name|twitter
operator|.
name|consumer
operator|.
name|Twitter4JConsumer
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
name|twitter
operator|.
name|consumer
operator|.
name|directmessage
operator|.
name|DirectMessageConsumer
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
name|twitter
operator|.
name|consumer
operator|.
name|search
operator|.
name|SearchConsumer
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
name|twitter
operator|.
name|consumer
operator|.
name|streaming
operator|.
name|FilterConsumer
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
name|twitter
operator|.
name|consumer
operator|.
name|streaming
operator|.
name|SampleConsumer
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
name|twitter
operator|.
name|consumer
operator|.
name|timeline
operator|.
name|HomeConsumer
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
name|twitter
operator|.
name|consumer
operator|.
name|timeline
operator|.
name|MentionsConsumer
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
name|twitter
operator|.
name|consumer
operator|.
name|timeline
operator|.
name|RetweetsConsumer
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
name|twitter
operator|.
name|consumer
operator|.
name|timeline
operator|.
name|UserConsumer
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
name|twitter
operator|.
name|data
operator|.
name|ConsumerType
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
name|twitter
operator|.
name|data
operator|.
name|StreamingType
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
name|twitter
operator|.
name|data
operator|.
name|TimelineType
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
name|twitter
operator|.
name|producer
operator|.
name|DirectMessageProducer
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
name|twitter
operator|.
name|producer
operator|.
name|SearchProducer
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
name|twitter
operator|.
name|producer
operator|.
name|UserProducer
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
name|DefaultProducer
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
comment|/**  * Maps the endpoint URI to the respective Twitter4J consumer or producer.  *<p/>  * URI STRUCTURE:  *<p/>  * timeline/  * public  * home  * friends  * user (ALSO A PRODUCER)  * mentions  * retweetsofme  * user/  * search users (DIRECT ONLY)  * user suggestions (DIRECT ONLY)  * trends/  * daily  * weekly  * userlist  * directmessage (ALSO A PRODUCER)  * streaming/  * filter (POLLING ONLY)  * sample (POLLING ONLY)  */
end_comment

begin_class
DECL|class|Twitter4JFactory
specifier|public
specifier|final
class|class
name|Twitter4JFactory
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
name|Twitter4JFactory
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|Twitter4JFactory ()
specifier|private
name|Twitter4JFactory
parameter_list|()
block|{
comment|// helper class
block|}
DECL|method|getConsumer (TwitterEndpoint te, String uri)
specifier|public
specifier|static
name|Twitter4JConsumer
name|getConsumer
parameter_list|(
name|TwitterEndpoint
name|te
parameter_list|,
name|String
name|uri
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
name|String
index|[]
name|uriSplit
init|=
name|splitUri
argument_list|(
name|uri
argument_list|)
decl_stmt|;
if|if
condition|(
name|uriSplit
operator|.
name|length
operator|>
literal|0
condition|)
block|{
switch|switch
condition|(
name|ConsumerType
operator|.
name|fromUri
argument_list|(
name|uriSplit
index|[
literal|0
index|]
argument_list|)
condition|)
block|{
case|case
name|DIRECTMESSAGE
case|:
return|return
operator|new
name|DirectMessageConsumer
argument_list|(
name|te
argument_list|)
return|;
case|case
name|SEARCH
case|:
name|boolean
name|hasKeywords
init|=
name|te
operator|.
name|getProperties
argument_list|()
operator|.
name|getKeywords
argument_list|()
operator|==
literal|null
operator|||
name|te
operator|.
name|getProperties
argument_list|()
operator|.
name|getKeywords
argument_list|()
operator|.
name|trim
argument_list|()
operator|.
name|isEmpty
argument_list|()
decl_stmt|;
if|if
condition|(
name|hasKeywords
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Type set to SEARCH but no keywords were provided."
argument_list|)
throw|;
block|}
else|else
block|{
return|return
operator|new
name|SearchConsumer
argument_list|(
name|te
argument_list|)
return|;
block|}
case|case
name|STREAMING
case|:
switch|switch
condition|(
name|StreamingType
operator|.
name|fromUri
argument_list|(
name|uriSplit
index|[
literal|1
index|]
argument_list|)
condition|)
block|{
case|case
name|SAMPLE
case|:
return|return
operator|new
name|SampleConsumer
argument_list|(
name|te
argument_list|)
return|;
case|case
name|FILTER
case|:
return|return
operator|new
name|FilterConsumer
argument_list|(
name|te
argument_list|)
return|;
default|default:
break|break;
block|}
break|break;
case|case
name|TIMELINE
case|:
if|if
condition|(
name|uriSplit
operator|.
name|length
operator|>
literal|1
condition|)
block|{
switch|switch
condition|(
name|TimelineType
operator|.
name|fromUri
argument_list|(
name|uriSplit
index|[
literal|1
index|]
argument_list|)
condition|)
block|{
case|case
name|HOME
case|:
return|return
operator|new
name|HomeConsumer
argument_list|(
name|te
argument_list|)
return|;
case|case
name|MENTIONS
case|:
return|return
operator|new
name|MentionsConsumer
argument_list|(
name|te
argument_list|)
return|;
case|case
name|RETWEETSOFME
case|:
return|return
operator|new
name|RetweetsConsumer
argument_list|(
name|te
argument_list|)
return|;
case|case
name|USER
case|:
if|if
condition|(
name|te
operator|.
name|getProperties
argument_list|()
operator|.
name|getUser
argument_list|()
operator|==
literal|null
operator|||
name|te
operator|.
name|getProperties
argument_list|()
operator|.
name|getUser
argument_list|()
operator|.
name|trim
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Fetch type set to USER TIMELINE but no user was set."
argument_list|)
throw|;
block|}
else|else
block|{
return|return
operator|new
name|UserConsumer
argument_list|(
name|te
argument_list|)
return|;
block|}
default|default:
break|break;
block|}
block|}
break|break;
default|default:
break|break;
block|}
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot create any consumer with uri "
operator|+
name|uri
operator|+
literal|". A consumer type was not provided (or an incorrect pairing was used)."
argument_list|)
throw|;
block|}
DECL|method|getProducer (TwitterEndpoint te, String uri)
specifier|public
specifier|static
name|DefaultProducer
name|getProducer
parameter_list|(
name|TwitterEndpoint
name|te
parameter_list|,
name|String
name|uri
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
name|String
index|[]
name|uriSplit
init|=
name|splitUri
argument_list|(
name|uri
argument_list|)
decl_stmt|;
if|if
condition|(
name|uriSplit
operator|.
name|length
operator|>
literal|0
condition|)
block|{
switch|switch
condition|(
name|ConsumerType
operator|.
name|fromUri
argument_list|(
name|uriSplit
index|[
literal|0
index|]
argument_list|)
condition|)
block|{
case|case
name|DIRECTMESSAGE
case|:
if|if
condition|(
name|te
operator|.
name|getProperties
argument_list|()
operator|.
name|getUser
argument_list|()
operator|==
literal|null
operator|||
name|te
operator|.
name|getProperties
argument_list|()
operator|.
name|getUser
argument_list|()
operator|.
name|trim
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Producer type set to DIRECT MESSAGE but no recipient user was set."
argument_list|)
throw|;
block|}
else|else
block|{
return|return
operator|new
name|DirectMessageProducer
argument_list|(
name|te
argument_list|)
return|;
block|}
case|case
name|TIMELINE
case|:
if|if
condition|(
name|uriSplit
operator|.
name|length
operator|>
literal|1
condition|)
block|{
switch|switch
condition|(
name|TimelineType
operator|.
name|fromUri
argument_list|(
name|uriSplit
index|[
literal|1
index|]
argument_list|)
condition|)
block|{
case|case
name|USER
case|:
return|return
operator|new
name|UserProducer
argument_list|(
name|te
argument_list|)
return|;
default|default:
break|break;
block|}
block|}
break|break;
case|case
name|SEARCH
case|:
return|return
operator|new
name|SearchProducer
argument_list|(
name|te
argument_list|)
return|;
default|default:
break|break;
block|}
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot create any producer with uri "
operator|+
name|uri
operator|+
literal|". A producer type was not provided (or an incorrect pairing was used)."
argument_list|)
throw|;
block|}
DECL|method|splitUri (String uri)
specifier|private
specifier|static
name|String
index|[]
name|splitUri
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|Pattern
name|p1
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"twitter:(//)*"
argument_list|)
decl_stmt|;
name|Pattern
name|p2
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"\\?.*"
argument_list|)
decl_stmt|;
name|uri
operator|=
name|p1
operator|.
name|matcher
argument_list|(
name|uri
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|uri
operator|=
name|p2
operator|.
name|matcher
argument_list|(
name|uri
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|""
argument_list|)
expr_stmt|;
return|return
name|uri
operator|.
name|split
argument_list|(
literal|"/"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

