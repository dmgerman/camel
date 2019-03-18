begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.remoting
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|remoting
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
name|spring
operator|.
name|SpringTestSupport
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
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|AbstractXmlApplicationContext
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

begin_class
DECL|class|MultiArgumentsWithDefaultBindingSpringRemotingPojoDirectTest
specifier|public
class|class
name|MultiArgumentsWithDefaultBindingSpringRemotingPojoDirectTest
extends|extends
name|SpringTestSupport
block|{
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/spring/remoting/multi-arguments-with-default-binding-pojo-direct.xml"
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|testMultiArgumentPojo ()
specifier|public
name|void
name|testMultiArgumentPojo
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
comment|// use the pojo directly to call the injected endpoint and have the
comment|// original runtime exception thrown
name|MultiArgumentsWithDefaultBinding
name|myMultArgumentPojo
init|=
name|applicationContext
operator|.
name|getBean
argument_list|(
literal|"multiArgumentsPojoDirect"
argument_list|,
name|MultiArgumentsWithDefaultBinding
operator|.
name|class
argument_list|)
decl_stmt|;
name|myMultArgumentPojo
operator|.
name|doSomethingMultiple
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
name|fail
argument_list|(
literal|""
operator|+
literal|"\nShould not have failed with multiple arguments on POJO @Produce @Consume."
operator|+
literal|"\nValues are incorrect in the consume for doSomething(String arg1, String arg2, Date arg3)"
operator|+
literal|"\nProduce called with doSomething(\"Hello World 1\", \"Hello World 2\", new Date())."
operator|+
literal|"\nConsume got something else."
operator|+
literal|"\n"
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|"\n"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

