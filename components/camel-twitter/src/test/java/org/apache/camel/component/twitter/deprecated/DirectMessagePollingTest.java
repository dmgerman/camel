begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.twitter.deprecated
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
name|deprecated
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
name|component
operator|.
name|twitter
operator|.
name|CamelTwitterConsumerTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
comment|/**  * consumes tweets  */
end_comment

begin_class
annotation|@
name|Deprecated
DECL|class|DirectMessagePollingTest
specifier|public
class|class
name|DirectMessagePollingTest
extends|extends
name|CamelTwitterConsumerTestSupport
block|{
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
comment|/* Uncomment when you need a test direct message         TwitterConfiguration properties = new TwitterConfiguration();         properties.setConsumerKey(consumerKey);         properties.setConsumerSecret(consumerSecret);         properties.setAccessToken(accessToken);         properties.setAccessTokenSecret(accessTokenSecret);         Twitter twitter = properties.getTwitter();         twitter.sendDirectMessage(twitter.getScreenName(), "Test Direct Message: " + new Date().toString());         */
block|}
annotation|@
name|Override
DECL|method|getUri ()
specifier|protected
name|String
name|getUri
parameter_list|()
block|{
return|return
literal|"twitter://directmessage?type=polling&"
return|;
block|}
annotation|@
name|Override
DECL|method|getLogger ()
specifier|protected
name|Logger
name|getLogger
parameter_list|()
block|{
return|return
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|DirectMessagePollingTest
operator|.
name|class
argument_list|)
return|;
block|}
block|}
end_class

end_unit

