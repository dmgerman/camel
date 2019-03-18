begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.catalog.maven
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|catalog
operator|.
name|maven
package|;
end_package

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
name|URL
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|util
operator|.
name|url
operator|.
name|BasicURLHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|util
operator|.
name|url
operator|.
name|HttpClientHandler
import|;
end_import

begin_comment
comment|/**  * A {@link HttpClientHandler} which uses HttpClient for downloading via http/https  * and have support for connection timeouts which otherwise is not supported by default in Apache Ivy.  */
end_comment

begin_class
DECL|class|TimeoutHttpClientHandler
specifier|public
class|class
name|TimeoutHttpClientHandler
extends|extends
name|HttpClientHandler
block|{
comment|// use basic handler for non http/https as it can load from jar/file etc
DECL|field|basic
specifier|private
name|BasicURLHandler
name|basic
init|=
operator|new
name|BasicURLHandler
argument_list|()
decl_stmt|;
DECL|field|timeout
specifier|private
name|int
name|timeout
init|=
literal|10000
decl_stmt|;
DECL|method|getTimeout ()
specifier|public
name|int
name|getTimeout
parameter_list|()
block|{
return|return
name|timeout
return|;
block|}
comment|/**      * Sets the timeout in millis (http.socket.timeout) when downloading via http/https protocols.      *<p/>      * The default value is 10000      */
DECL|method|setTimeout (int timeout)
specifier|public
name|void
name|setTimeout
parameter_list|(
name|int
name|timeout
parameter_list|)
block|{
name|this
operator|.
name|timeout
operator|=
name|timeout
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getURLInfo (URL url)
specifier|public
name|URLInfo
name|getURLInfo
parameter_list|(
name|URL
name|url
parameter_list|)
block|{
comment|// ensure we always use a timeout
name|String
name|protocol
init|=
name|url
operator|.
name|getProtocol
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"http"
operator|.
name|equals
argument_list|(
name|protocol
argument_list|)
operator|||
literal|"https"
operator|.
name|equals
argument_list|(
name|protocol
argument_list|)
condition|)
block|{
return|return
name|super
operator|.
name|getURLInfo
argument_list|(
name|url
argument_list|,
name|timeout
argument_list|)
return|;
block|}
else|else
block|{
comment|// use basic for non http
return|return
name|basic
operator|.
name|getURLInfo
argument_list|(
name|url
argument_list|,
name|timeout
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|openStream (URL url)
specifier|public
name|InputStream
name|openStream
parameter_list|(
name|URL
name|url
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|protocol
init|=
name|url
operator|.
name|getProtocol
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"http"
operator|.
name|equals
argument_list|(
name|protocol
argument_list|)
operator|||
literal|"https"
operator|.
name|equals
argument_list|(
name|protocol
argument_list|)
condition|)
block|{
return|return
name|super
operator|.
name|openStream
argument_list|(
name|url
argument_list|)
return|;
block|}
else|else
block|{
comment|// use basic for non http
return|return
name|basic
operator|.
name|openStream
argument_list|(
name|url
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

