begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.telegram.usage
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|telegram
operator|.
name|usage
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
name|CamelContext
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
name|ProducerTemplate
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
name|telegram
operator|.
name|TelegramService
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
name|telegram
operator|.
name|TelegramServiceProvider
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
name|telegram
operator|.
name|model
operator|.
name|MessageResult
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
name|telegram
operator|.
name|model
operator|.
name|OutgoingTextMessage
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
name|telegram
operator|.
name|model
operator|.
name|UpdateResult
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
name|example
operator|.
name|telegram
operator|.
name|Application
import|;
end_import

begin_class
DECL|class|GetUpdatesUsage
specifier|public
class|class
name|GetUpdatesUsage
implements|implements
name|TelegramMethodUsage
block|{
DECL|method|run (CamelContext context)
specifier|public
name|void
name|run
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|TelegramService
name|service
init|=
name|TelegramServiceProvider
operator|.
name|get
argument_list|()
operator|.
name|getService
argument_list|()
decl_stmt|;
name|UpdateResult
name|message
init|=
name|service
operator|.
name|getUpdates
argument_list|(
name|Application
operator|.
name|AUTHORIZATION_TOKEN
argument_list|,
operator|(
name|long
operator|)
literal|10
argument_list|,
literal|10
argument_list|,
literal|10
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

