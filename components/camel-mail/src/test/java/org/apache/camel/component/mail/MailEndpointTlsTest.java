begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|Parameterized
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|Parameterized
operator|.
name|class
argument_list|)
DECL|class|MailEndpointTlsTest
specifier|public
class|class
name|MailEndpointTlsTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|protocol
specifier|private
specifier|final
name|String
name|protocol
decl_stmt|;
DECL|method|MailEndpointTlsTest (String protocol)
specifier|public
name|MailEndpointTlsTest
parameter_list|(
name|String
name|protocol
parameter_list|)
block|{
name|this
operator|.
name|protocol
operator|=
name|protocol
expr_stmt|;
block|}
annotation|@
name|Parameterized
operator|.
name|Parameters
DECL|method|data ()
specifier|public
specifier|static
name|Collection
argument_list|<
name|Object
index|[]
argument_list|>
name|data
parameter_list|()
block|{
return|return
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
index|[]
block|{
block|{
literal|"smtp"
block|}
block|,
block|{
literal|"smtps"
block|}
block|,
block|{
literal|"pop3"
block|}
block|,
block|{
literal|"pop3s"
block|}
block|,
block|{
literal|"imap"
block|}
block|,
block|{
literal|"imaps"
block|}
block|}
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|testMailEndpointTslConfig ()
specifier|public
name|void
name|testMailEndpointTslConfig
parameter_list|()
throws|throws
name|Exception
block|{
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|properties
operator|.
name|setProperty
argument_list|(
literal|"mail."
operator|+
name|protocol
operator|+
literal|".starttls.enable"
argument_list|,
literal|"true"
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
name|protocol
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
name|cfg
operator|.
name|setAdditionalJavaMailProperties
argument_list|(
name|properties
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cfg
operator|.
name|isStartTlsEnabled
argument_list|()
argument_list|)
expr_stmt|;
name|Properties
name|javaMailProperties
init|=
name|cfg
operator|.
name|createJavaMailSender
argument_list|()
operator|.
name|getJavaMailProperties
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|javaMailProperties
operator|.
name|get
argument_list|(
literal|"mail."
operator|+
name|protocol
operator|+
literal|".ssl.socketFactory"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|javaMailProperties
operator|.
name|get
argument_list|(
literal|"mail."
operator|+
name|protocol
operator|+
literal|".ssl.socketFactory.port"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMailEndpointNoTslConfig ()
specifier|public
name|void
name|testMailEndpointNoTslConfig
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
name|protocol
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
name|cfg
operator|.
name|setSslContextParameters
argument_list|(
name|MailTestHelper
operator|.
name|createSslContextParameters
argument_list|()
argument_list|)
expr_stmt|;
name|Properties
name|javaMailProperties
init|=
name|cfg
operator|.
name|createJavaMailSender
argument_list|()
operator|.
name|getJavaMailProperties
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|cfg
operator|.
name|isStartTlsEnabled
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|protocol
operator|.
name|endsWith
argument_list|(
literal|"s"
argument_list|)
condition|)
block|{
name|assertTrue
argument_list|(
name|cfg
operator|.
name|isSecureProtocol
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|javaMailProperties
operator|.
name|get
argument_list|(
literal|"mail."
operator|+
name|protocol
operator|+
literal|".socketFactory"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|javaMailProperties
operator|.
name|get
argument_list|(
literal|"mail."
operator|+
name|protocol
operator|+
literal|".socketFactory.fallback"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|javaMailProperties
operator|.
name|get
argument_list|(
literal|"mail."
operator|+
name|protocol
operator|+
literal|".socketFactory.port"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertFalse
argument_list|(
name|cfg
operator|.
name|isSecureProtocol
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|javaMailProperties
operator|.
name|get
argument_list|(
literal|"mail."
operator|+
name|protocol
operator|+
literal|".socketFactory"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|javaMailProperties
operator|.
name|get
argument_list|(
literal|"mail."
operator|+
name|protocol
operator|+
literal|".socketFactory.fallback"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|javaMailProperties
operator|.
name|get
argument_list|(
literal|"mail."
operator|+
name|protocol
operator|+
literal|".socketFactory.port"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testMailEndpointTslSslContextParametersConfig ()
specifier|public
name|void
name|testMailEndpointTslSslContextParametersConfig
parameter_list|()
throws|throws
name|Exception
block|{
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|properties
operator|.
name|setProperty
argument_list|(
literal|"mail."
operator|+
name|protocol
operator|+
literal|".starttls.enable"
argument_list|,
literal|"true"
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
name|protocol
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
name|cfg
operator|.
name|setSslContextParameters
argument_list|(
name|MailTestHelper
operator|.
name|createSslContextParameters
argument_list|()
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setAdditionalJavaMailProperties
argument_list|(
name|properties
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cfg
operator|.
name|isStartTlsEnabled
argument_list|()
argument_list|)
expr_stmt|;
name|Properties
name|javaMailProperties
init|=
name|cfg
operator|.
name|createJavaMailSender
argument_list|()
operator|.
name|getJavaMailProperties
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|javaMailProperties
operator|.
name|get
argument_list|(
literal|"mail."
operator|+
name|protocol
operator|+
literal|".ssl.socketFactory"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|javaMailProperties
operator|.
name|get
argument_list|(
literal|"mail."
operator|+
name|protocol
operator|+
literal|".ssl.socketFactory.port"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMailEndpointTslDummyTrustManagerConfig ()
specifier|public
name|void
name|testMailEndpointTslDummyTrustManagerConfig
parameter_list|()
throws|throws
name|Exception
block|{
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|properties
operator|.
name|setProperty
argument_list|(
literal|"mail."
operator|+
name|protocol
operator|+
literal|".starttls.enable"
argument_list|,
literal|"true"
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
name|protocol
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
name|cfg
operator|.
name|setDummyTrustManager
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setAdditionalJavaMailProperties
argument_list|(
name|properties
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cfg
operator|.
name|isStartTlsEnabled
argument_list|()
argument_list|)
expr_stmt|;
name|Properties
name|javaMailProperties
init|=
name|cfg
operator|.
name|createJavaMailSender
argument_list|()
operator|.
name|getJavaMailProperties
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|javaMailProperties
operator|.
name|get
argument_list|(
literal|"mail."
operator|+
name|protocol
operator|+
literal|".ssl.socketFactory.class"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|javaMailProperties
operator|.
name|get
argument_list|(
literal|"mail."
operator|+
name|protocol
operator|+
literal|".ssl.socketFactory.port"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

