begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|MailEndpointTest
specifier|public
class|class
name|MailEndpointTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testMailEndpointCtr ()
specifier|public
name|void
name|testMailEndpointCtr
parameter_list|()
throws|throws
name|Exception
block|{
name|MailEndpoint
name|endpoint
init|=
operator|new
name|MailEndpoint
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getContentTypeResolver
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|endpoint
operator|.
name|getBinding
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|endpoint
operator|.
name|getHeaderFilterStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|MailConfiguration
name|cfg
init|=
operator|new
name|MailConfiguration
argument_list|()
decl_stmt|;
name|cfg
operator|.
name|setPort
argument_list|(
literal|21
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setProtocol
argument_list|(
literal|"smtp"
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setHost
argument_list|(
literal|"myhost"
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setUsername
argument_list|(
literal|"james"
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setPassword
argument_list|(
literal|"secret"
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setConfiguration
argument_list|(
name|cfg
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|cfg
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMailEndpointCtrEndpoint ()
specifier|public
name|void
name|testMailEndpointCtrEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|MailEndpoint
name|endpoint
init|=
operator|new
name|MailEndpoint
argument_list|(
literal|"smtp://myhost"
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getContentTypeResolver
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|endpoint
operator|.
name|getBinding
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|endpoint
operator|.
name|getHeaderFilterStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|MailConfiguration
name|cfg
init|=
operator|new
name|MailConfiguration
argument_list|()
decl_stmt|;
name|cfg
operator|.
name|setPort
argument_list|(
literal|21
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setProtocol
argument_list|(
literal|"smtp"
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setHost
argument_list|(
literal|"myhost"
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setUsername
argument_list|(
literal|"james"
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setPassword
argument_list|(
literal|"secret"
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setConfiguration
argument_list|(
name|cfg
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|cfg
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMailEndpointCtrEndpointConfig ()
specifier|public
name|void
name|testMailEndpointCtrEndpointConfig
parameter_list|()
throws|throws
name|Exception
block|{
name|MailConfiguration
name|cfg
init|=
operator|new
name|MailConfiguration
argument_list|()
decl_stmt|;
name|cfg
operator|.
name|setPort
argument_list|(
literal|21
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setProtocol
argument_list|(
literal|"smtp"
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setHost
argument_list|(
literal|"myhost"
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setUsername
argument_list|(
literal|"james"
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setPassword
argument_list|(
literal|"secret"
argument_list|)
expr_stmt|;
name|MailComponent
name|comp
init|=
operator|new
name|MailComponent
argument_list|(
name|cfg
argument_list|)
decl_stmt|;
name|MailEndpoint
name|endpoint
init|=
operator|new
name|MailEndpoint
argument_list|(
literal|"smtp://myhost"
argument_list|,
name|comp
argument_list|,
name|cfg
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|cfg
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getContentTypeResolver
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|endpoint
operator|.
name|getBinding
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|endpoint
operator|.
name|getHeaderFilterStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|MyMailBinding
name|myBnd
init|=
operator|new
name|MyMailBinding
argument_list|()
decl_stmt|;
name|endpoint
operator|.
name|setBinding
argument_list|(
name|myBnd
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|myBnd
argument_list|,
name|endpoint
operator|.
name|getBinding
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|class|MyMailBinding
specifier|private
specifier|static
class|class
name|MyMailBinding
extends|extends
name|MailBinding
block|{      }
block|}
end_class

end_unit

