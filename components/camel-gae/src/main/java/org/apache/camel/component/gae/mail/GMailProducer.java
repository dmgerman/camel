begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.gae.mail
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
name|mail
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
name|mail
operator|.
name|MailService
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
name|mail
operator|.
name|MailService
operator|.
name|Message
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
DECL|class|GMailProducer
specifier|public
class|class
name|GMailProducer
extends|extends
name|DefaultProducer
block|{
DECL|method|GMailProducer (GMailEndpoint endpoint)
specifier|public
name|GMailProducer
parameter_list|(
name|GMailEndpoint
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
name|GMailEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|GMailEndpoint
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
name|GMailEndpoint
argument_list|,
name|Message
argument_list|,
name|Void
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
DECL|method|getMailService ()
specifier|public
name|MailService
name|getMailService
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getMailService
argument_list|()
return|;
block|}
comment|/**      * Invokes the mail service.      *       * @param exchange      *            contains the mail data in the in-message.      * @see GMailBinding      */
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
name|getMailService
argument_list|()
operator|.
name|send
argument_list|(
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
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

