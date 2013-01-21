begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.twitter.producer
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
operator|.
name|producer
package|;
end_package

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
name|CamelExchangeException
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
name|component
operator|.
name|twitter
operator|.
name|TwitterConstants
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
name|TwitterEndpoint
import|;
end_import

begin_import
import|import
name|twitter4j
operator|.
name|Query
import|;
end_import

begin_import
import|import
name|twitter4j
operator|.
name|QueryResult
import|;
end_import

begin_import
import|import
name|twitter4j
operator|.
name|Status
import|;
end_import

begin_import
import|import
name|twitter4j
operator|.
name|Twitter
import|;
end_import

begin_class
DECL|class|SearchProducer
specifier|public
class|class
name|SearchProducer
extends|extends
name|Twitter4JProducer
block|{
DECL|field|lastId
specifier|private
specifier|volatile
name|long
name|lastId
decl_stmt|;
DECL|method|SearchProducer (TwitterEndpoint te)
specifier|public
name|SearchProducer
parameter_list|(
name|TwitterEndpoint
name|te
parameter_list|)
block|{
name|super
argument_list|(
name|te
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|long
name|myLastId
init|=
name|lastId
decl_stmt|;
comment|// keywords from header take precedence
name|String
name|keywords
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|TwitterConstants
operator|.
name|TWITTER_KEYWORDS
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|keywords
operator|==
literal|null
condition|)
block|{
name|keywords
operator|=
name|te
operator|.
name|getProperties
argument_list|()
operator|.
name|getKeywords
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|keywords
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"No keywords to use for query"
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
name|Query
name|query
init|=
operator|new
name|Query
argument_list|(
name|keywords
argument_list|)
decl_stmt|;
if|if
condition|(
name|te
operator|.
name|getProperties
argument_list|()
operator|.
name|isFilterOld
argument_list|()
operator|&&
name|myLastId
operator|!=
literal|0
condition|)
block|{
name|query
operator|.
name|setSinceId
argument_list|(
name|myLastId
argument_list|)
expr_stmt|;
block|}
name|Twitter
name|twitter
init|=
name|te
operator|.
name|getProperties
argument_list|()
operator|.
name|getTwitter
argument_list|()
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Searching twitter with keywords: {}"
argument_list|,
name|keywords
argument_list|)
expr_stmt|;
name|QueryResult
name|results
init|=
name|twitter
operator|.
name|search
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Status
argument_list|>
name|list
init|=
name|results
operator|.
name|getTweets
argument_list|()
decl_stmt|;
if|if
condition|(
name|te
operator|.
name|getProperties
argument_list|()
operator|.
name|isFilterOld
argument_list|()
condition|)
block|{
for|for
control|(
name|Status
name|t
range|:
name|list
control|)
block|{
name|long
name|newId
init|=
name|t
operator|.
name|getId
argument_list|()
decl_stmt|;
if|if
condition|(
name|newId
operator|>
name|myLastId
condition|)
block|{
name|myLastId
operator|=
name|newId
expr_stmt|;
block|}
block|}
block|}
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|list
argument_list|)
expr_stmt|;
comment|// update the lastId after finished the processing
if|if
condition|(
name|myLastId
operator|>
name|lastId
condition|)
block|{
name|lastId
operator|=
name|myLastId
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

