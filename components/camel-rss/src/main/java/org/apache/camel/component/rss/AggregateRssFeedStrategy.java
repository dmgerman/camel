begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.rss
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|rss
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
name|com
operator|.
name|sun
operator|.
name|syndication
operator|.
name|feed
operator|.
name|synd
operator|.
name|SyndEntryImpl
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|syndication
operator|.
name|feed
operator|.
name|synd
operator|.
name|SyndFeed
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
name|processor
operator|.
name|aggregate
operator|.
name|AggregationStrategy
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
name|CastUtils
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

begin_class
DECL|class|AggregateRssFeedStrategy
specifier|public
class|class
name|AggregateRssFeedStrategy
implements|implements
name|AggregationStrategy
block|{
DECL|field|log
specifier|protected
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|AggregateRssFeedStrategy
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|aggregate (Exchange oldExchange, Exchange newExchange)
specifier|public
name|Exchange
name|aggregate
parameter_list|(
name|Exchange
name|oldExchange
parameter_list|,
name|Exchange
name|newExchange
parameter_list|)
block|{
if|if
condition|(
name|oldExchange
operator|==
literal|null
condition|)
block|{
return|return
name|newExchange
return|;
block|}
name|SyndFeed
name|oldFeed
init|=
name|oldExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|SyndFeed
operator|.
name|class
argument_list|)
decl_stmt|;
name|SyndFeed
name|newFeed
init|=
name|newExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|SyndFeed
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|oldFeed
operator|!=
literal|null
operator|&&
name|newFeed
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|SyndEntryImpl
argument_list|>
name|oldEntries
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|oldFeed
operator|.
name|getEntries
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|SyndEntryImpl
argument_list|>
name|newEntries
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|newFeed
operator|.
name|getEntries
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|SyndEntryImpl
argument_list|>
name|mergedList
init|=
operator|new
name|ArrayList
argument_list|<
name|SyndEntryImpl
argument_list|>
argument_list|(
name|oldEntries
operator|.
name|size
argument_list|()
operator|+
name|newEntries
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|mergedList
operator|.
name|addAll
argument_list|(
name|oldEntries
argument_list|)
expr_stmt|;
name|mergedList
operator|.
name|addAll
argument_list|(
name|newEntries
argument_list|)
expr_stmt|;
name|oldFeed
operator|.
name|setEntries
argument_list|(
name|mergedList
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Could not merge exchanges. One body was null."
argument_list|)
expr_stmt|;
block|}
return|return
name|oldExchange
return|;
block|}
block|}
end_class

end_unit

