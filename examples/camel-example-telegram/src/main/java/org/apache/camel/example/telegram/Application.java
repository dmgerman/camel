begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.telegram
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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|impl
operator|.
name|DefaultCamelContext
import|;
end_import

begin_class
DECL|class|Application
specifier|public
specifier|final
class|class
name|Application
block|{
DECL|field|AUTHORIZATION_TOKEN
specifier|public
specifier|static
specifier|final
name|String
name|AUTHORIZATION_TOKEN
decl_stmt|;
DECL|field|CHAT_ID
specifier|public
specifier|static
specifier|final
name|String
name|CHAT_ID
decl_stmt|;
static|static
block|{
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|ClassLoader
name|loader
init|=
name|Application
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
decl_stmt|;
try|try
init|(
name|InputStream
name|resourceStream
init|=
name|loader
operator|.
name|getResourceAsStream
argument_list|(
literal|"application.properties"
argument_list|)
init|)
block|{
name|properties
operator|.
name|load
argument_list|(
name|resourceStream
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
name|AUTHORIZATION_TOKEN
operator|=
name|properties
operator|.
name|getProperty
argument_list|(
literal|"authorizationToken"
argument_list|)
expr_stmt|;
name|CHAT_ID
operator|=
name|properties
operator|.
name|getProperty
argument_list|(
literal|"chatId"
argument_list|)
expr_stmt|;
block|}
DECL|method|Application ()
specifier|private
name|Application
parameter_list|()
block|{
comment|// noop
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
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|TelegramRouteBuilder
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|addStartupListener
argument_list|(
operator|new
name|TelegramExamplesRunner
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

