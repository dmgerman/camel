begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.example.client
package|package
name|org
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
name|example
operator|.
name|server
operator|.
name|Multiplier
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
name|ApplicationContext
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
comment|/**  * Requires that the JMS broker is running, as well as CamelServer  *  * @author martin.gilday  */
end_comment

begin_class
DECL|class|CamelClientRemoting
specifier|public
specifier|final
class|class
name|CamelClientRemoting
block|{
DECL|method|CamelClientRemoting ()
specifier|private
name|CamelClientRemoting
parameter_list|()
block|{
comment|// the main class
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
block|{
name|ApplicationContext
name|context
init|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"camel-client-remoting.xml"
argument_list|)
decl_stmt|;
name|Multiplier
name|multiplier
init|=
operator|(
name|Multiplier
operator|)
name|context
operator|.
name|getBean
argument_list|(
literal|"multiplierProxy"
argument_list|)
decl_stmt|;
name|int
name|response
init|=
name|multiplier
operator|.
name|multiply
argument_list|(
literal|22
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Invoking the multiply with 22, the result is "
operator|+
name|response
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

