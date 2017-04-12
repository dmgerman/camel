begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.pubnub.example
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|pubnub
operator|.
name|example
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
name|builder
operator|.
name|RouteBuilder
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
name|main
operator|.
name|Main
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|pubnub
operator|.
name|example
operator|.
name|PubNubExampleConstants
operator|.
name|PUBNUB_SUBSCRIBE_KEY
import|;
end_import

begin_class
DECL|class|PubNubPresenseExample
specifier|public
specifier|final
class|class
name|PubNubPresenseExample
block|{
DECL|method|PubNubPresenseExample ()
specifier|private
name|PubNubPresenseExample
parameter_list|()
block|{     }
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
name|Main
name|main
init|=
operator|new
name|Main
argument_list|()
decl_stmt|;
name|main
operator|.
name|addRouteBuilder
argument_list|(
operator|new
name|PresensRoute
argument_list|()
argument_list|)
expr_stmt|;
name|main
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
DECL|class|PresensRoute
specifier|static
class|class
name|PresensRoute
extends|extends
name|RouteBuilder
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"pubnub:iot?withPresence=true&subscribeKey="
operator|+
name|PUBNUB_SUBSCRIBE_KEY
argument_list|)
operator|.
name|log
argument_list|(
literal|"${body}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

