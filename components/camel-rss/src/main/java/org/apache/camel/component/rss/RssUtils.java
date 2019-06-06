begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|HttpURLConnection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|rometools
operator|.
name|rome
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
name|com
operator|.
name|rometools
operator|.
name|rome
operator|.
name|io
operator|.
name|SyndFeedInput
import|;
end_import

begin_import
import|import
name|com
operator|.
name|rometools
operator|.
name|rome
operator|.
name|io
operator|.
name|XmlReader
import|;
end_import

begin_class
DECL|class|RssUtils
specifier|public
specifier|final
class|class
name|RssUtils
block|{
DECL|method|RssUtils ()
specifier|private
name|RssUtils
parameter_list|()
block|{
comment|// Helper class
block|}
DECL|method|createFeed (String feedUri)
specifier|public
specifier|static
name|SyndFeed
name|createFeed
parameter_list|(
name|String
name|feedUri
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|createFeed
argument_list|(
name|feedUri
argument_list|,
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
argument_list|)
return|;
block|}
DECL|method|createFeed (String feedUri, ClassLoader classLoader)
specifier|public
specifier|static
name|SyndFeed
name|createFeed
parameter_list|(
name|String
name|feedUri
parameter_list|,
name|ClassLoader
name|classLoader
parameter_list|)
throws|throws
name|Exception
block|{
name|ClassLoader
name|tccl
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
try|try
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|classLoader
argument_list|)
expr_stmt|;
name|InputStream
name|in
init|=
operator|new
name|URL
argument_list|(
name|feedUri
argument_list|)
operator|.
name|openStream
argument_list|()
decl_stmt|;
name|SyndFeedInput
name|input
init|=
operator|new
name|SyndFeedInput
argument_list|()
decl_stmt|;
return|return
name|input
operator|.
name|build
argument_list|(
operator|new
name|XmlReader
argument_list|(
name|in
argument_list|)
argument_list|)
return|;
block|}
finally|finally
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|tccl
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createFeed (String feedUri, String username, String password)
specifier|public
specifier|static
name|SyndFeed
name|createFeed
parameter_list|(
name|String
name|feedUri
parameter_list|,
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|createFeed
argument_list|(
name|feedUri
argument_list|,
name|username
argument_list|,
name|password
argument_list|,
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
argument_list|)
return|;
block|}
DECL|method|createFeed (String feedUri, String username, String password, ClassLoader classLoader)
specifier|public
specifier|static
name|SyndFeed
name|createFeed
parameter_list|(
name|String
name|feedUri
parameter_list|,
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|,
name|ClassLoader
name|classLoader
parameter_list|)
throws|throws
name|Exception
block|{
name|ClassLoader
name|tccl
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
try|try
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|classLoader
argument_list|)
expr_stmt|;
name|URL
name|feedUrl
init|=
operator|new
name|URL
argument_list|(
name|feedUri
argument_list|)
decl_stmt|;
name|HttpURLConnection
name|httpcon
init|=
operator|(
name|HttpURLConnection
operator|)
name|feedUrl
operator|.
name|openConnection
argument_list|()
decl_stmt|;
name|String
name|encoding
init|=
name|Base64
operator|.
name|encodeBase64String
argument_list|(
name|username
operator|.
name|concat
argument_list|(
literal|":"
argument_list|)
operator|.
name|concat
argument_list|(
name|password
argument_list|)
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|httpcon
operator|.
name|setRequestProperty
argument_list|(
literal|"Authorization"
argument_list|,
literal|"Basic "
operator|+
name|encoding
argument_list|)
expr_stmt|;
name|SyndFeedInput
name|input
init|=
operator|new
name|SyndFeedInput
argument_list|()
decl_stmt|;
return|return
name|input
operator|.
name|build
argument_list|(
operator|new
name|XmlReader
argument_list|(
name|httpcon
argument_list|)
argument_list|)
return|;
block|}
finally|finally
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|tccl
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

