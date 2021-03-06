begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.twitter.directmessage
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
name|directmessage
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
name|Consumer
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
name|Processor
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
name|Producer
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
name|AbstractTwitterEndpoint
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
name|TwitterConfiguration
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
name|TwitterHelper
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
name|Metadata
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
name|UriEndpoint
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
name|UriPath
import|;
end_import

begin_comment
comment|/**  * The Twitter Direct Message Component consumes/produces user's direct messages.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.10.0"
argument_list|,
name|scheme
operator|=
literal|"twitter-directmessage"
argument_list|,
name|title
operator|=
literal|"Twitter Direct Message"
argument_list|,
name|syntax
operator|=
literal|"twitter-directmessage:user"
argument_list|,
name|label
operator|=
literal|"api,social"
argument_list|)
DECL|class|TwitterDirectMessageEndpoint
specifier|public
class|class
name|TwitterDirectMessageEndpoint
extends|extends
name|AbstractTwitterEndpoint
block|{
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"The user name to send a direct message. This will be ignored for consumer."
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|user
specifier|private
name|String
name|user
decl_stmt|;
DECL|method|TwitterDirectMessageEndpoint (String uri, String remaining, TwitterDirectMessageComponent component, TwitterConfiguration properties)
specifier|public
name|TwitterDirectMessageEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|TwitterDirectMessageComponent
name|component
parameter_list|,
name|TwitterConfiguration
name|properties
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|,
name|properties
argument_list|)
expr_stmt|;
name|this
operator|.
name|user
operator|=
name|remaining
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|DirectMessageProducer
argument_list|(
name|this
argument_list|,
name|user
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|Consumer
name|answer
init|=
name|TwitterHelper
operator|.
name|createConsumer
argument_list|(
name|processor
argument_list|,
name|this
argument_list|,
operator|new
name|DirectMessageConsumerHandler
argument_list|(
name|this
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

