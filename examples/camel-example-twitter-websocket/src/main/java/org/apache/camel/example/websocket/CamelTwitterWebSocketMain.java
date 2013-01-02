begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.websocket
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|websocket
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
name|main
operator|.
name|Main
import|;
end_import

begin_comment
comment|/**  * A main to start this example.  */
end_comment

begin_class
DECL|class|CamelTwitterWebSocketMain
specifier|public
specifier|final
class|class
name|CamelTwitterWebSocketMain
block|{
comment|// Twitter now requires the use of OAuth for all client application authentication.
comment|// In order to use camel-twitter with your account, you'll need to create a new application
comment|// within Twitter at https://dev.twitter.com/apps/new and grant the application access to your account.
comment|// Finally, generate your access token and secret.
comment|// This uses the Twitter 'cameltweet' account for testing purposes.
comment|// do NOT use this twitter account in your applications!
DECL|field|consumerKey
specifier|private
specifier|static
name|String
name|consumerKey
init|=
literal|"NMqaca1bzXsOcZhP2XlwA"
decl_stmt|;
DECL|field|consumerSecret
specifier|private
specifier|static
name|String
name|consumerSecret
init|=
literal|"VxNQiRLwwKVD0K9mmfxlTTbVdgRpriORypnUbHhxeQw"
decl_stmt|;
DECL|field|accessToken
specifier|private
specifier|static
name|String
name|accessToken
init|=
literal|"26693234-W0YjxL9cMJrC0VZZ4xdgFMymxIQ10LeL1K8YlbBY"
decl_stmt|;
DECL|field|accessTokenSecret
specifier|private
specifier|static
name|String
name|accessTokenSecret
init|=
literal|"BZD51BgzbOdFstWZYsqB5p5dbuuDV12vrOdatzhY4E"
decl_stmt|;
DECL|method|CamelTwitterWebSocketMain ()
specifier|private
name|CamelTwitterWebSocketMain
parameter_list|()
block|{
comment|// to pass checkstyle we have a private constructor
block|}
DECL|method|main (String[] args)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"\n\n\n\n"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"==============================================="
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Open your web browser on http://localhost:9090/index.html"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Press ctrl+c to stop this example"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"==============================================="
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"\n\n\n\n"
argument_list|)
expr_stmt|;
comment|// create a new Camel Main so we can easily start Camel
name|Main
name|main
init|=
operator|new
name|Main
argument_list|()
decl_stmt|;
comment|// enable hangup support which mean we detect when the JVM terminates, and stop Camel graceful
name|main
operator|.
name|enableHangupSupport
argument_list|()
expr_stmt|;
name|TwitterWebSocketRoute
name|route
init|=
operator|new
name|TwitterWebSocketRoute
argument_list|()
decl_stmt|;
comment|// setup twitter application authentication
name|route
operator|.
name|setAccessToken
argument_list|(
name|accessToken
argument_list|)
expr_stmt|;
name|route
operator|.
name|setAccessTokenSecret
argument_list|(
name|accessTokenSecret
argument_list|)
expr_stmt|;
name|route
operator|.
name|setConsumerKey
argument_list|(
name|consumerKey
argument_list|)
expr_stmt|;
name|route
operator|.
name|setConsumerSecret
argument_list|(
name|consumerSecret
argument_list|)
expr_stmt|;
comment|// poll for gaga, every 2nd second
name|route
operator|.
name|setSearchTerm
argument_list|(
literal|"gaga"
argument_list|)
expr_stmt|;
name|route
operator|.
name|setDelay
argument_list|(
literal|2
argument_list|)
expr_stmt|;
comment|// web socket on port 9090
name|route
operator|.
name|setPort
argument_list|(
literal|9090
argument_list|)
expr_stmt|;
comment|// add our routes to Camel
name|main
operator|.
name|addRouteBuilder
argument_list|(
name|route
argument_list|)
expr_stmt|;
comment|// and run, which keeps blocking until we terminate the JVM (or stop CamelContext)
name|main
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

