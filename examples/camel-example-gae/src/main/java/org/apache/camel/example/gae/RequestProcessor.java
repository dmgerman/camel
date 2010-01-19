begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.gae
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|gae
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
name|users
operator|.
name|UserService
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
name|users
operator|.
name|UserServiceFactory
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
name|Processor
import|;
end_import

begin_class
DECL|class|RequestProcessor
specifier|public
class|class
name|RequestProcessor
implements|implements
name|Processor
block|{
DECL|field|LOGGER
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|Logger
operator|.
name|getLogger
argument_list|(
name|RequestProcessor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
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
name|UserService
name|userService
init|=
name|UserServiceFactory
operator|.
name|getUserService
argument_list|()
decl_stmt|;
name|String
name|city
init|=
operator|(
name|String
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|removeHeader
argument_list|(
literal|"city"
argument_list|)
decl_stmt|;
name|String
name|requestor
init|=
name|userService
operator|.
name|getCurrentUser
argument_list|()
operator|.
name|getEmail
argument_list|()
decl_stmt|;
name|String
name|recipient
init|=
name|requestor
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|removeHeader
argument_list|(
literal|"mailtocurrent"
argument_list|)
operator|==
literal|null
condition|)
block|{
name|recipient
operator|=
operator|(
name|String
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|removeHeader
argument_list|(
literal|"mailto"
argument_list|)
expr_stmt|;
block|}
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
operator|new
name|ReportData
argument_list|(
name|city
argument_list|,
name|recipient
argument_list|,
name|requestor
argument_list|)
argument_list|)
expr_stmt|;
name|LOGGER
operator|.
name|info
argument_list|(
name|requestor
operator|+
literal|" requested weather data for "
operator|+
name|city
operator|+
literal|". Report will be sent to "
operator|+
name|recipient
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

