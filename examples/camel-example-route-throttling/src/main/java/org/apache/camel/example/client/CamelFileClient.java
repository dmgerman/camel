begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.client
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|client
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
name|util
operator|.
name|IOHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|AbstractApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_comment
comment|/**  * Client that uses the {@link org.apache.camel.ProducerTemplate} to easily exchange messages with the Server.  */
end_comment

begin_class
DECL|class|CamelFileClient
specifier|public
specifier|final
class|class
name|CamelFileClient
block|{
DECL|field|SIZE
specifier|private
specifier|static
specifier|final
name|int
name|SIZE
init|=
literal|5000
decl_stmt|;
DECL|method|CamelFileClient ()
specifier|private
name|CamelFileClient
parameter_list|()
block|{
comment|// Helper class
block|}
DECL|method|main (final String[] args)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
specifier|final
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|AbstractApplicationContext
name|context
init|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"camel-file-client.xml"
argument_list|)
decl_stmt|;
comment|// get the camel template for Spring template style sending of messages (= producer)
specifier|final
name|ProducerTemplate
name|producer
init|=
name|context
operator|.
name|getBean
argument_list|(
literal|"camelTemplate"
argument_list|,
name|ProducerTemplate
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// now send a lot of messages
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Writing files ..."
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|SIZE
condition|;
name|i
operator|++
control|)
block|{
name|producer
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file:target//inbox"
argument_list|,
literal|"File "
operator|+
name|i
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
name|i
operator|+
literal|".txt"
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"... Wrote "
operator|+
name|SIZE
operator|+
literal|" files"
argument_list|)
expr_stmt|;
comment|// we're done so let's properly close the application context
name|IOHelper
operator|.
name|close
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

