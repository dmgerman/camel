begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.soroushbot.component
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|soroushbot
operator|.
name|component
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
name|soroushbot
operator|.
name|models
operator|.
name|Endpoint
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
name|soroushbot
operator|.
name|models
operator|.
name|SoroushMessage
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
name|support
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
comment|/**  * this producer is responsible for uri of type {@link Endpoint#downloadFile}  * e.g. "soroush:downloadFile/[token]"  * if you pass a message to this endpoint, it tries to download the resource  * ({@link SoroushMessage#fileUrl} and {@link SoroushMessage#thumbnailUrl})  * if provided and store them in  * {@link SoroushMessage#file} or {@link SoroushMessage#thumbnail}.  */
end_comment

begin_class
DECL|class|SoroushBotDownloadFileProducer
specifier|public
class|class
name|SoroushBotDownloadFileProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|log
specifier|static
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SoroushBotDownloadFileProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpoint
name|SoroushBotEndpoint
name|endpoint
decl_stmt|;
DECL|method|SoroushBotDownloadFileProducer (SoroushBotEndpoint endpoint)
specifier|public
name|SoroushBotDownloadFileProducer
parameter_list|(
name|SoroushBotEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
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
name|SoroushMessage
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|SoroushMessage
operator|.
name|class
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|handleDownloadFiles
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

