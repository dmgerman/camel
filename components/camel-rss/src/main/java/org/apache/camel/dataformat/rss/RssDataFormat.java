begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.rss
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|rss
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
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
name|SyndEntry
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
name|spi
operator|.
name|DataFormat
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
name|ExchangeHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * RSS DataFormat  *<p/>  * This data format supports two operations:  *<ul>  *<li>marshal = from ROME SyndFeed to XML String</li>  *<li>unmarshal = from XML String to ROME SyndFeed</li>  *</ul>  *<p/>  * Uses<a href="https://rome.dev.java.net/">ROME</a> for RSS parsing.  *<p/>  */
end_comment

begin_class
DECL|class|RssDataFormat
specifier|public
class|class
name|RssDataFormat
implements|implements
name|DataFormat
block|{
DECL|field|LOG
specifier|protected
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|RssDataFormat
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|marshal (Exchange exchange, Object body, OutputStream out)
specifier|public
name|void
name|marshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|body
parameter_list|,
name|OutputStream
name|out
parameter_list|)
throws|throws
name|Exception
block|{
name|SyndFeed
name|feed
init|=
name|ExchangeHelper
operator|.
name|convertToMandatoryType
argument_list|(
name|exchange
argument_list|,
name|SyndFeed
operator|.
name|class
argument_list|,
name|body
argument_list|)
decl_stmt|;
name|String
name|xml
init|=
name|RssConverter
operator|.
name|feedToXml
argument_list|(
name|feed
argument_list|)
decl_stmt|;
if|if
condition|(
name|xml
operator|!=
literal|null
condition|)
block|{
name|out
operator|.
name|write
argument_list|(
name|xml
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Couldn't marshal RSS feed to XML."
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|unmarshal (Exchange exchange, InputStream in)
specifier|public
name|Object
name|unmarshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|InputStream
name|in
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|xml
init|=
name|ExchangeHelper
operator|.
name|convertToMandatoryType
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|in
argument_list|)
decl_stmt|;
return|return
name|RssConverter
operator|.
name|xmlToFeed
argument_list|(
name|xml
argument_list|)
return|;
block|}
block|}
end_class

end_unit

