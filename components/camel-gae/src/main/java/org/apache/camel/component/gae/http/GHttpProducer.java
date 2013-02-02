begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.gae.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|gae
operator|.
name|http
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|appengine
operator|.
name|api
operator|.
name|urlfetch
operator|.
name|HTTPRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|appengine
operator|.
name|api
operator|.
name|urlfetch
operator|.
name|HTTPResponse
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|appengine
operator|.
name|api
operator|.
name|urlfetch
operator|.
name|URLFetchService
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
name|gae
operator|.
name|bind
operator|.
name|OutboundBinding
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

begin_class
DECL|class|GHttpProducer
specifier|public
class|class
name|GHttpProducer
extends|extends
name|DefaultProducer
block|{
DECL|method|GHttpProducer (GHttpEndpoint endpoint)
specifier|public
name|GHttpProducer
parameter_list|(
name|GHttpEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|GHttpEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|GHttpEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
DECL|method|getOutboundBinding ()
specifier|public
name|OutboundBinding
argument_list|<
name|GHttpEndpoint
argument_list|,
name|HTTPRequest
argument_list|,
name|HTTPResponse
argument_list|>
name|getOutboundBinding
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getOutboundBinding
argument_list|()
return|;
block|}
DECL|method|getUrlFetchService ()
specifier|public
name|URLFetchService
name|getUrlFetchService
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getUrlFetchService
argument_list|()
return|;
block|}
comment|/**      * Invokes the URL fetch service.      *       * @param exchange contains the request data in the in-message. The result is written to the out-message.      * @throws GHttpException if the response code is>= 400 and {@link GHttpEndpoint#isThrowExceptionOnFailure()}      * returns<code>true</code>.      *      * @see GHttpBinding      */
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
name|HTTPRequest
name|request
init|=
name|getOutboundBinding
argument_list|()
operator|.
name|writeRequest
argument_list|(
name|getEndpoint
argument_list|()
argument_list|,
name|exchange
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|HTTPResponse
name|response
init|=
name|getUrlFetchService
argument_list|()
operator|.
name|fetch
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|getOutboundBinding
argument_list|()
operator|.
name|readResponse
argument_list|(
name|getEndpoint
argument_list|()
argument_list|,
name|exchange
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

