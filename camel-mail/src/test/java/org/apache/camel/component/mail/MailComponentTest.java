begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mail
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mail
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
name|ContextTestSupport
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
name|Endpoint
import|;
end_import

begin_comment
comment|/**  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|MailComponentTest
specifier|public
class|class
name|MailComponentTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testMailEndpointsAreConfiguredProperlyWhenUsingSmtp ()
specifier|public
name|void
name|testMailEndpointsAreConfiguredProperlyWhenUsingSmtp
parameter_list|()
throws|throws
name|Exception
block|{
name|MailEndpoint
name|endpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"smtp://james@myhost:30/subject"
argument_list|)
decl_stmt|;
name|MailConfiguration
name|config
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"getProtocol()"
argument_list|,
literal|"smtp"
argument_list|,
name|config
operator|.
name|getProtocol
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"getHost()"
argument_list|,
literal|"myhost"
argument_list|,
name|config
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"getPort()"
argument_list|,
literal|30
argument_list|,
name|config
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"getUsername()"
argument_list|,
literal|"james"
argument_list|,
name|config
operator|.
name|getUsername
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testMailEndpointsAreConfiguredProperlyWhenUsingImap ()
specifier|public
name|void
name|testMailEndpointsAreConfiguredProperlyWhenUsingImap
parameter_list|()
throws|throws
name|Exception
block|{
name|MailEndpoint
name|endpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"imap://james@myhost:30/subject"
argument_list|)
decl_stmt|;
name|MailConfiguration
name|config
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"getProtocol()"
argument_list|,
literal|"imap"
argument_list|,
name|config
operator|.
name|getProtocol
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"getHost()"
argument_list|,
literal|"myhost"
argument_list|,
name|config
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"getPort()"
argument_list|,
literal|30
argument_list|,
name|config
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"getUsername()"
argument_list|,
literal|"james"
argument_list|,
name|config
operator|.
name|getUsername
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testMailEndpointsAreConfiguredProperlyWhenUsingPop ()
specifier|public
name|void
name|testMailEndpointsAreConfiguredProperlyWhenUsingPop
parameter_list|()
throws|throws
name|Exception
block|{
name|MailEndpoint
name|endpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"pop://james@myhost:30/subject"
argument_list|)
decl_stmt|;
name|MailConfiguration
name|config
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"getProtocol()"
argument_list|,
literal|"pop"
argument_list|,
name|config
operator|.
name|getProtocol
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"getHost()"
argument_list|,
literal|"myhost"
argument_list|,
name|config
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"getPort()"
argument_list|,
literal|30
argument_list|,
name|config
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"getUsername()"
argument_list|,
literal|"james"
argument_list|,
name|config
operator|.
name|getUsername
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|resolveMandatoryEndpoint (String uri)
specifier|protected
name|MailEndpoint
name|resolveMandatoryEndpoint
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|Endpoint
name|endpoint
init|=
name|super
operator|.
name|resolveMandatoryEndpoint
argument_list|(
name|uri
argument_list|)
decl_stmt|;
return|return
name|assertIsInstanceOf
argument_list|(
name|MailEndpoint
operator|.
name|class
argument_list|,
name|endpoint
argument_list|)
return|;
block|}
block|}
end_class

end_unit

