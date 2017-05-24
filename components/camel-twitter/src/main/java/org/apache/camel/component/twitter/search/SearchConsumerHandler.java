begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.twitter.search
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
name|search
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|TwitterEndpoint
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
name|AbstractTwitterConsumerHandler
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
name|TwitterEventType
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
name|ObjectHelper
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

begin_import
import|import
name|twitter4j
operator|.
name|GeoLocation
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
name|Query
operator|.
name|Unit
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

begin_import
import|import
name|twitter4j
operator|.
name|TwitterException
import|;
end_import

begin_comment
comment|/**  * Consumes search requests  */
end_comment

begin_class
DECL|class|SearchConsumerHandler
specifier|public
class|class
name|SearchConsumerHandler
extends|extends
name|AbstractTwitterConsumerHandler
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
name|SearchConsumerHandler
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|keywords
specifier|private
name|String
name|keywords
decl_stmt|;
DECL|method|SearchConsumerHandler (TwitterEndpoint te, String keywords)
specifier|public
name|SearchConsumerHandler
parameter_list|(
name|TwitterEndpoint
name|te
parameter_list|,
name|String
name|keywords
parameter_list|)
block|{
name|super
argument_list|(
name|te
argument_list|)
expr_stmt|;
name|this
operator|.
name|keywords
operator|=
name|keywords
expr_stmt|;
block|}
DECL|method|pollConsume ()
specifier|public
name|List
argument_list|<
name|Exchange
argument_list|>
name|pollConsume
parameter_list|()
throws|throws
name|TwitterException
block|{
name|String
name|keywords
init|=
name|this
operator|.
name|keywords
decl_stmt|;
name|Query
name|query
decl_stmt|;
if|if
condition|(
name|keywords
operator|!=
literal|null
operator|&&
name|keywords
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|query
operator|=
operator|new
name|Query
argument_list|(
name|keywords
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Searching twitter with keywords: {}"
argument_list|,
name|keywords
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|query
operator|=
operator|new
name|Query
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Searching twitter without keywords."
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|endpoint
operator|.
name|getProperties
argument_list|()
operator|.
name|isFilterOld
argument_list|()
condition|)
block|{
name|query
operator|.
name|setSinceId
argument_list|(
name|getLastId
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|search
argument_list|(
name|query
argument_list|)
return|;
block|}
DECL|method|directConsume ()
specifier|public
name|List
argument_list|<
name|Exchange
argument_list|>
name|directConsume
parameter_list|()
throws|throws
name|TwitterException
block|{
name|String
name|keywords
init|=
name|this
operator|.
name|keywords
decl_stmt|;
if|if
condition|(
name|keywords
operator|==
literal|null
operator|||
name|keywords
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
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
name|LOG
operator|.
name|debug
argument_list|(
literal|"Searching twitter with keywords: {}"
argument_list|,
name|keywords
argument_list|)
expr_stmt|;
return|return
name|search
argument_list|(
name|query
argument_list|)
return|;
block|}
DECL|method|search (Query query)
specifier|private
name|List
argument_list|<
name|Exchange
argument_list|>
name|search
parameter_list|(
name|Query
name|query
parameter_list|)
throws|throws
name|TwitterException
block|{
name|Integer
name|numberOfPages
init|=
literal|1
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|endpoint
operator|.
name|getProperties
argument_list|()
operator|.
name|getLang
argument_list|()
argument_list|)
condition|)
block|{
name|query
operator|.
name|setLang
argument_list|(
name|endpoint
operator|.
name|getProperties
argument_list|()
operator|.
name|getLang
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|endpoint
operator|.
name|getProperties
argument_list|()
operator|.
name|getCount
argument_list|()
argument_list|)
condition|)
block|{
name|query
operator|.
name|setCount
argument_list|(
name|endpoint
operator|.
name|getProperties
argument_list|()
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|endpoint
operator|.
name|getProperties
argument_list|()
operator|.
name|getNumberOfPages
argument_list|()
argument_list|)
condition|)
block|{
name|numberOfPages
operator|=
name|endpoint
operator|.
name|getProperties
argument_list|()
operator|.
name|getNumberOfPages
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|endpoint
operator|.
name|getProperties
argument_list|()
operator|.
name|getLatitude
argument_list|()
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|endpoint
operator|.
name|getProperties
argument_list|()
operator|.
name|getLongitude
argument_list|()
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|endpoint
operator|.
name|getProperties
argument_list|()
operator|.
name|getRadius
argument_list|()
argument_list|)
condition|)
block|{
name|GeoLocation
name|location
init|=
operator|new
name|GeoLocation
argument_list|(
name|endpoint
operator|.
name|getProperties
argument_list|()
operator|.
name|getLatitude
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getProperties
argument_list|()
operator|.
name|getLongitude
argument_list|()
argument_list|)
decl_stmt|;
name|query
operator|.
name|setGeoCode
argument_list|(
name|location
argument_list|,
name|endpoint
operator|.
name|getProperties
argument_list|()
operator|.
name|getRadius
argument_list|()
argument_list|,
name|Unit
operator|.
name|valueOf
argument_list|(
name|endpoint
operator|.
name|getProperties
argument_list|()
operator|.
name|getDistanceMetric
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Searching with additional geolocation parameters."
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Searching with {} pages."
argument_list|,
name|numberOfPages
argument_list|)
expr_stmt|;
name|Twitter
name|twitter
init|=
name|getTwitter
argument_list|()
decl_stmt|;
name|QueryResult
name|qr
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
name|tweets
init|=
name|qr
operator|.
name|getTweets
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|numberOfPages
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
operator|!
name|qr
operator|.
name|hasNext
argument_list|()
condition|)
block|{
break|break;
block|}
name|qr
operator|=
name|twitter
operator|.
name|search
argument_list|(
name|qr
operator|.
name|nextQuery
argument_list|()
argument_list|)
expr_stmt|;
name|tweets
operator|.
name|addAll
argument_list|(
name|qr
operator|.
name|getTweets
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|endpoint
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
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|tweets
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|setLastIdIfGreater
argument_list|(
name|tweets
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|TwitterEventType
operator|.
name|STATUS
operator|.
name|createExchangeList
argument_list|(
name|endpoint
argument_list|,
name|tweets
argument_list|)
return|;
block|}
block|}
end_class

end_unit

