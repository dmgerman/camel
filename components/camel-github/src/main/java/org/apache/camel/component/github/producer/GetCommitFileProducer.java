begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.github.producer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|github
operator|.
name|producer
package|;
end_package

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
name|github
operator|.
name|GitHubConstants
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
name|github
operator|.
name|GitHubEndpoint
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
name|Registry
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
name|org
operator|.
name|eclipse
operator|.
name|egit
operator|.
name|github
operator|.
name|core
operator|.
name|Blob
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|egit
operator|.
name|github
operator|.
name|core
operator|.
name|CommitFile
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|egit
operator|.
name|github
operator|.
name|core
operator|.
name|service
operator|.
name|DataService
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
comment|/**  * Producer endpoint that gets the file associated with a CommitFile object.  *  */
end_comment

begin_class
DECL|class|GetCommitFileProducer
specifier|public
class|class
name|GetCommitFileProducer
extends|extends
name|AbstractGitHubProducer
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|GetCommitFileProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|dataService
specifier|private
name|DataService
name|dataService
decl_stmt|;
DECL|field|encoding
specifier|private
name|String
name|encoding
init|=
name|Blob
operator|.
name|ENCODING_UTF8
decl_stmt|;
DECL|method|GetCommitFileProducer (GitHubEndpoint endpoint)
specifier|public
name|GetCommitFileProducer
parameter_list|(
name|GitHubEndpoint
name|endpoint
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|Registry
name|registry
init|=
name|endpoint
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
decl_stmt|;
name|Object
name|service
init|=
name|registry
operator|.
name|lookupByName
argument_list|(
name|GitHubConstants
operator|.
name|GITHUB_DATA_SERVICE
argument_list|)
decl_stmt|;
if|if
condition|(
name|service
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Using DataService found in registry {}"
argument_list|,
name|service
operator|.
name|getClass
argument_list|()
operator|.
name|getCanonicalName
argument_list|()
argument_list|)
expr_stmt|;
name|dataService
operator|=
operator|(
name|DataService
operator|)
name|service
expr_stmt|;
block|}
else|else
block|{
name|dataService
operator|=
operator|new
name|DataService
argument_list|()
expr_stmt|;
block|}
name|initService
argument_list|(
name|dataService
argument_list|)
expr_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getEncoding
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|encoding
operator|=
name|endpoint
operator|.
name|getEncoding
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|encoding
operator|.
name|equalsIgnoreCase
argument_list|(
name|Blob
operator|.
name|ENCODING_BASE64
argument_list|)
operator|&&
operator|!
name|encoding
operator|.
name|equalsIgnoreCase
argument_list|(
name|Blob
operator|.
name|ENCODING_UTF8
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unknown encoding '"
operator|+
name|encoding
operator|+
literal|"'"
argument_list|)
throw|;
block|}
block|}
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
name|CommitFile
name|file
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|CommitFile
operator|.
name|class
argument_list|)
decl_stmt|;
name|Blob
name|response
init|=
name|dataService
operator|.
name|getBlob
argument_list|(
name|getRepository
argument_list|()
argument_list|,
name|file
operator|.
name|getSha
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|text
init|=
name|response
operator|.
name|getContent
argument_list|()
decl_stmt|;
comment|// By default, if blob encoding is base64 then we convert to UTF-8. If
comment|// base64 encoding is required, then must be explicitly requested
if|if
condition|(
name|response
operator|.
name|getEncoding
argument_list|()
operator|.
name|equals
argument_list|(
name|Blob
operator|.
name|ENCODING_BASE64
argument_list|)
operator|&&
name|encoding
operator|!=
literal|null
operator|&&
name|encoding
operator|.
name|equalsIgnoreCase
argument_list|(
name|Blob
operator|.
name|ENCODING_UTF8
argument_list|)
condition|)
block|{
name|text
operator|=
operator|new
name|String
argument_list|(
name|Base64
operator|.
name|decodeBase64
argument_list|(
name|text
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// copy the header of in message to the out message
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|copyFrom
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

